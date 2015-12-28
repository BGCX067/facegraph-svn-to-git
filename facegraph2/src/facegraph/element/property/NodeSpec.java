package facegraph.element.property;

import java.util.Map;

import org.neo4j.graphdb.Node;

public interface NodeSpec
{
	/**
	 * Method verifies if properties might be even consider for properties for specific Node.
	 * @param properties
	 * @return True if properties has specific for this Node property, False if not.
	 */
	boolean hasSpecificProperty(Map<String, Object> properties);
	/**
	 * Checks whether passed properties would be good properties for specific Node.
	 * @param properties contains properties.
	 * @return True if properties might be valid for specific Node, False if not.
	 */
	boolean verify(Map<String, Object> properties);
	/**
	 * Check whether passed Node could be node for specific Node.
	 * @param node Node with properties to validate.
	 * @return True if node might be specific node, False if not.
	 */
	boolean verify(Node node);
}
