/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package facegraph.element;

import org.apache.log4j.Logger;
import org.neo4j.graphdb.Node;

import facegraph.element.property.AbstractCategory;
import facegraph.element.property.HumanCategory;
import facegraph.element.property.ThingCategory;

/**
 *
 * Represents thing node in facegraph database.
 */
public class Thing 
extends FacegraphNode
{
    private static Logger _logger = Logger.getLogger(Thing.class);
    private static AbstractCategory category = new ThingCategory();

    /**
     * Private constructor of Thing for usage of ThingBuilder.
     * @param   node    is nothing more but the core of Thing.
     **/
    private Thing(Node node)
    {
        super(node);
    }
    
    @Override
    public String toString() {
    	StringBuilder buildStr = new StringBuilder("Thing : ");
    	for(String i : category.getProperties())
    	{
    		if(_wrappedNode.hasProperty( i ))
    			buildStr.append( "{" +  i + " : " + _wrappedNode.getProperty( i ) + " }" );
    	}
    	buildStr.append( "." );
    	return buildStr.toString();
    }

    @Override
    public boolean equals( Object o )
    {
        return o instanceof Thing &&
                _wrappedNode.equals( ( (Thing)o ).asNode() );
    }
    
    /**
     * Static factory of Things. Thing is being build of node, only if node has valid properties for being Thing.
     * @param node willThingBeOK(node) is check to validate if node will be good Thing.
     * @return 1. node == null ? null : Thing(node) <BR> 2. willThingBeOK(node) ? Thing(node) : null
     * 
     */
    public static Thing nodeAsThing(Node node)
    {
        Thing out = null;
        if(node != null && category.validateCategory( node ))
            out = new Thing(node);
        else
            _logger.debug("There was no way to convert node to thing!");
        return out;
    }
 }
