package facegraph.data;

import java.io.File;
import java.io.FileReader;
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

import facegraph.element.property.HumanProperty;

public class FacegraphJSONFileReader 
implements IFacegraphInputReader 
{
	private File _dir = null;
	private Logger _logger = Logger.getLogger(FacegraphJSONFileReader.class);
	
	private JsonParser _parser = null;
	
	/**
	 * FacegraphJSONFileReader reads data from JSON files. To create valid reader constructor
	 * needs path to directory which contains JSON files for specific node.
	 * @param pathToFile
	 * @throws IllegalArgumentException when provided path does NOT leads to valid DIRECTORY file.
	 */
	public FacegraphJSONFileReader(String pathToFile)
	{
		changeDir(pathToFile);
		_parser = new JsonParser();
	}
	
	public void changeDir(String pathToNewFile)
	{
		_dir = new File(pathToNewFile);
		if(!_dir.isDirectory())
		{
			IllegalArgumentException ex = new IllegalArgumentException("Directory does NOT exist: " + pathToNewFile);
			_logger.error("Cannot read data because, directory does not exist!", ex);
			throw ex;
		}
	}
	
	@Override
	public Map<String, Object> readProfile() 
	{		
		JsonElement parseTree = null;
		try {
			parseTree = _parser.parse(new FileReader(_dir + File.separator + "profile"));
		} catch (Exception e) {
			_logger.error("Json file problem", e);
			
			/*SURE !? */
			return null;
		}
		
		Set<Map.Entry<String, JsonElement>> set = null;
		try
		{
			set =  parseTree.getAsJsonObject().entrySet();
		} catch(IllegalStateException e) {
			_logger.error("Bad state of Json input. Node: " + _dir.getAbsolutePath(), e);
			return null;
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
		
		/*It doesn't matters if it's NTYPE property form HumanProperty or ThingProperty as long as RawName are the same*/
		properties.put(HumanProperty.NTYPE.getRawName(), FacegraphConst.FACEBOOKTYPE.toString());
		
		return properties;
	}
	
	@Override
	public List<String> readFriends() 
	{
		return getIdsFromDataJSON("friends");
	}

	@Override
	public List<String> readLikes() 
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
	{
		JsonElement parseTree = null;
		List<String> ids = new ArrayList<String>();
		
		try {
			parseTree = _parser.parse(new FileReader(_dir + File.separator + pathExtension));
			
			JsonArray jsonArray = parseTree.getAsJsonObject().get("data").getAsJsonArray();
			
			for(JsonElement i : jsonArray)
			{
				ids.add(i.getAsJsonObject().get("id").getAsString());
			}
		} catch (Exception e) {
			_logger.error("Json file problem : " + e.getMessage());
		}
		
		_logger.info("getIdsFromDataJSON returns: " + ids.size() + " ids, for file " + _dir + File.separator + pathExtension);
		
		return ids;
	}

}