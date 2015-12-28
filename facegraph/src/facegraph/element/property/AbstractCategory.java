package facegraph.element.property;

import java.util.Map;

import org.neo4j.graphdb.Node;

public interface AbstractCategory 
{
	/**
	 * Validate if passed properties might be good for Node of specific category.
	 * @param properties map of properties to validate
	 * @return True if properties might be good for Node of specific category, False if not.
	 */
	public boolean validateCategory(Map<String, Object> properties);
	/**
	 * Validate if passed node might be good for Node of specific category.
	 * @param node to validate.
	 * @return True if node might be good for Node of specific category, False if not.
	 */
	public boolean validateCategory(Node node);
	/**
	 * Adds properties of specific Node category. For example for Human downloaded form facebook
	 * it will be 2 properties: ntype = "facebook", node_type = "Human". Be aware that first you should check if
	 * properties should be marked that way. Use validateCategory to do so.
	 * @param properties map of properties to which method add specific properties.
	 * @return changed properties.
	 */
	public Map<String, Object> addCategory(Map<String, Object> properties);
	/**
	 * Getter for properties.
	 * @return array of string with names of properties that might be found in specific node.
	 */
	public String[] getProperties();
}
