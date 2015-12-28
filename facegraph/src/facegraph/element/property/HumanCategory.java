package facegraph.element.property;

import java.util.Map;

import org.neo4j.graphdb.Node;

import facegraph.constants.Const;

public class HumanCategory 
implements AbstractCategory
{
	public static final String NODE_TYPE = Const.HUMAN_TYPE;
	private static final String[] properties = {"id", "first_name", "last_name", "gender", "name", "middle_name"};
	
	@Override
	public boolean validateCategory( Map<String, Object> properties ) {
		if(properties.containsKey( Const.HUMAN_FIRST_NAME ))
			return true;
		if(properties.containsKey( Const.HUMAN_GENDER ))
			return true;
		return false;
	}

	@Override
	public Map<String, Object> addCategory( Map<String, Object> properties ) {
		properties.put( Const.NODE_TYPE, NODE_TYPE );
		properties.put( Const.NETWORK_TYPE, Const.FACEBOOK_NETWORK_TYPE );
		return properties;
	}
	
	@Override
	public String[] getProperties() {
		return properties;
	}

	@Override
	public boolean validateCategory( Node node ) {
		if(node.hasProperty( Const.HUMAN_FIRST_NAME ))
			return true;
		if(node.hasProperty( Const.HUMAN_GENDER ))
			return true;
		return false;
	}
}
