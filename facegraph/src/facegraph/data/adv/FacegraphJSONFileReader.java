package facegraph.data.adv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;

import facegraph.data.IFacegraphInputReader;

public class FacegraphJSONFileReader 
implements IFacegraphInputReader 
{
	private String _dir = null;
	private Logger _logger = Logger.getLogger(FacegraphJSONFileReader.class);
	
	private JsonParser _parser = null;
	private FileReader fr = null;
	
	/**
	 * FacegraphJSONFileReader reads data from JSON files. To create valid reader constructor
	 * needs path to directory which contains JSON files for specific node.
	 * @param pathToFile
	 * @throws FileNotFoundException 
	 * @throws IllegalArgumentException when provided path does NOT leads to valid DIRECTORY file.
	 */
	public FacegraphJSONFileReader(String pathToFile) 
			throws FileNotFoundException
	{
		changeDir(pathToFile);
		_parser = new JsonParser();
//		fr = new FileReader( _dir );
	}
	
	public void changeDir(String pathToNewFile)
	{
		_dir = pathToNewFile;
		/*
		if(!_dir.isDirectory())
		{
			IllegalArgumentException ex = new IllegalArgumentException("Directory does NOT exist: " + pathToNewFile);
			_logger.error("Cannot read data because, directory does not exist!", ex);
			throw ex;
		}*/
		
	}
	
	@Override
	public Map<String, Object> readProfile()
	throws IOException
	{		
		JsonElement parseTree = null;

		try {
			fr = new FileReader( _dir + File.separator + "profile" );
			parseTree = _parser.parse(fr);
		} catch (Exception e) {
			_logger.error("Json file problem", e);
			
			/*SURE !? */
			return null;
		} finally {
			if(fr != null)
			{
				fr.close();
			}
		}
		
		Set<Map.Entry<String, JsonElement>> set = null;
		try
		{
			set =  parseTree.getAsJsonObject().entrySet();
		} catch(IllegalStateException e) {
			_logger.error("Bad state of Json input. Node: " + _dir + File.separator + "profile", e);
			return null;
		}
		
		
		Map<String, Object> properties = new HashMap<String, Object>(set.size());
		for (Map.Entry<String, JsonElement> entry : set)
		{
			String tmp = null;
			if(entry.getValue().isJsonPrimitive())
			{
				JsonPrimitive primitive = entry.getValue().getAsJsonPrimitive();
				if(primitive.isNumber())
				{
					properties.put(entry.getKey(), primitive.getAsNumber().longValue());
				} else if(primitive.isBoolean()) {
					properties.put(entry.getKey(), primitive.getAsBoolean());
				} else if(primitive.isString()) {
					tmp = primitive.toString();
					properties.put(entry.getKey(), tmp.substring( 1, tmp.length()-1 ));
				} else {
					throw new IllegalStateException("Primitive is in illegal state!");
				}
				
				/*And we go for next entry!*/
				continue;
			}
			
			/*If entry is NOT primitive we save it in JSON notation as a String*/
			properties.put(entry.getKey(), entry.getValue().toString());
		}
		
		return properties;
	}
	
	@Override
	public List<String> readFriends() 
	throws IOException
	{
		return getIdsFromDataJSON("friends");
	}

	@Override
	public List<String> readLikes() 
	throws IOException
	{
		return getIdsFromDataJSON("likes");
	}
	
	/**
	 * Reads JSON file of construction:
	 * {"data":[{"id":"100000226870131"},{"id":"100001910791029"}, ... ,{"id":"100000891052186"}]}
	 * Ids are examples, 3 dots means that there are more jsons primitives.
	 * @param pathExtension
	 * @return List of ids. List will be empty if there won't be file
	 */
	private List<String> getIdsFromDataJSON(String pathExtension)
	throws IOException
	{
		JsonElement parseTree = null;
		List<String> ids = new ArrayList<String>();
		
		try {
			fr = new FileReader( _dir + File.separator + pathExtension );
			parseTree = _parser.parse(fr);
			
			JsonArray jsonArray = parseTree.getAsJsonObject().get("data").getAsJsonArray();
			
			for(JsonElement i : jsonArray)
			{
				ids.add(i.getAsJsonObject().get("id").getAsString());
			}
		} catch (Exception e) {
			_logger.error("Json file problem : " + e.getMessage());
		} finally {
			if(fr != null)
				fr.close();
		}
		
		_logger.info("getIdsFromDataJSON returns: " + ids.size() + " ids, for file " + _dir + File.separator + pathExtension);
		
		return ids;
	}

}