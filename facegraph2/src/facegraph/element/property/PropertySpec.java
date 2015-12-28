package facegraph.element.property;

import java.util.Map;

import org.neo4j.graphdb.Node;


/**
 * Represents Specification of Node.
 * @author ravwojdyla
 *
 */
public interface PropertySpec
{
	/**
	 * Getter for Raw name of property. Raw name is a short name, without whitespaces.
	 */
	String getRawName();
	/**
	 * Getter for pretty name of property.
	 */
	String getName();
}

