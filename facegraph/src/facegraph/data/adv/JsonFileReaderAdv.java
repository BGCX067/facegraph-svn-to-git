package facegraph.data.adv;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import facegraph.data.FacegraphInput;
import facegraph.data.IFacegraphInputReader;

public class JsonFileReaderAdv 
extends Thread
implements FacegraphInput
{
	private String base_dir = null;
	private JsonParser parser = null;
	private final Logger _logger = Logger.getLogger( JsonFileReaderAdv.class );
	private final Mode mode;
	
	private BlockingQueue<Map<String,Object>> profiles = null;
	private BlockingQueue<List<String>> ids = null;
	
	public enum Mode { PROFILE, LIKES, FRIENDS };
	
	public JsonFileReaderAdv(String base_dir)
	{
		this( base_dir, Mode.PROFILE );
	}
	
	public JsonFileReaderAdv(String base_dir, Mode mode)
	{
		this.base_dir = base_dir;
		this.parser = new JsonParser();
		this.mode = mode;
		if(mode == Mode.PROFILE)
			this.profiles = new ArrayBlockingQueue( 50, true );
		else
			this.ids = new ArrayBlockingQueue<List<String>>( 50, true );
	}
	
	private void readProfile() throws IOException {
		FileCollector collector = new FileCollector( base_dir, "profile" );
		collector.start();
		BlockingQueue<String> queue = collector.getInput();
		
		String json = null;
		JsonElement parseTree = null;
		while(collector.isAlive() || !queue.isEmpty())
		{
			try {
				json = queue.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			parseTree = parser.parse( json );
			
			Set<Map.Entry<String, JsonElement>> set = null;
			try
			{
				set =  parseTree.getAsJsonObject().entrySet();
			} catch(IllegalStateException e) {
				_logger.error("Bad state of Json input. Node: " + base_dir + File.separator + "profile", e);
			}
			
			
			Map<String, Object> properties = new HashMap<String, Object>(set.size());
			for (Map.Entry<String, JsonElement> entry : set)
			{
				if(entry.getValue().isJsonPrimitive())
				{
					JsonPrimitive primitive = entry.getValue().getAsJsonPrimitive();
					if(primitive.isNumber())
					{
						properties.put(entry.getKey(), primitive.getAsNumber().longValue());
					} else if(primitive.isBoolean()) {
						properties.put(entry.getKey(), primitive.getAsBoolean());
					} else if(primitive.isString()) {
						properties.put(entry.getKey(), primitive.getAsString());
					} else {
						throw new IllegalStateException("Primitive is in illegal state!");
					}
					
					/*And we go for next entry!*/
					continue;
				}
				
				/*If entry is NOT primitive we save it in JSON notation as a String*/
				properties.put(entry.getKey(), entry.getValue().toString());
			}
			
			try {
				this.profiles.put( properties );
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void readFriends() throws IOException {
		getIdsFromDataJSON( "friends" );
	}

	private void readLikes() throws IOException {
		getIdsFromDataJSON( "likes" );
	}

	/**
	 * Parse JSON String of construction:
	 * {"data":[{"id":"100000226870131"},{"id":"100001910791029"}, ... ,{"id":"100000891052186"}]}
	 * Ids are examples, 3 dots means that there are more jsons primitives.
	 * @param pathExtension
	 * @return List of ids. List will be empty if there won't be file
	 */
	private void getIdsFromDataJSON(String pattern)
	throws IOException
	{
		FileCollector collector = new FileCollector( base_dir, pattern );
		collector.start();
		BlockingQueue<String> queue = collector.getInput();
		
		String json = null;
		
		JsonElement parseTree = null;
		List<String> ids = new ArrayList<String>();
		
		while(collector.isAlive() || !queue.isEmpty())
		{
			try {
				json = queue.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			parseTree = parser.parse( json );
			
			JsonArray jsonArray = parseTree.getAsJsonObject().get("data").getAsJsonArray();
			
			for(JsonElement i : jsonArray)
			{
				ids.add(i.getAsJsonObject().get("id").getAsString());
			}
		}
		
		_logger.info("getIdsFromDataJSON returns: " + ids.size() + " ids, for file " + base_dir + File.separator + pattern);
		
		try {
			this.ids.put( ids );
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<String> getIds() {
		if(this.isAlive())
		{
			try {
				return ids.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return ids.poll();
	}
	
	@Override
	public Map<String, Object> getProperties() {
		if(this.isAlive())
		{
			try {
				return profiles.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return profiles.poll();
	}

	
	@Override
	public void run() {
		switch(mode)
		{
		case PROFILE:
			try {
				readProfile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case FRIENDS:
			try {
				readFriends();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case LIKES:
			try {
				readLikes();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		default:
			throw new NullPointerException();
		}
	}
}
