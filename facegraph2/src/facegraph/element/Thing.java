/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package facegraph.element;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.neo4j.graphdb.Node;

import facegraph.core.FacegraphDB;
import facegraph.element.exception.ThingPreperationException;
import facegraph.element.property.HumanProperty;
import facegraph.element.property.ThingProperty;

/**
 *
 * Represents thing node in facegraph database.
 */
public class Thing 
extends FacegraphNode
{
    private static final String _thingProperty = "THING";
    private static Logger _logger = Logger.getLogger(Thing.class);
    
    /**
     * Private constructor of Thing for usage of ThingBuilder.
     * @param   node    is nothing more but the core of Thing.
     **/
    private Thing(Node node)
    {
        super(node);
    }
    
    @Override
    public String toString()
    {
        return "Thing: " + getProperty(ThingProperty.NAME.getRawName()) + ", " +
                ThingProperty.CATEGORY.toString() + ": " + getProperty(ThingProperty.CATEGORY.getRawName());
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
        if(node != null && ThingBuilder.willThingBeOK(node))
            out = new Thing(node);
        else
            _logger.debug("There was no way to convert node to thing!");
        return out;
    }
    
    public static class ThingBuilder
    {
        public static Thing build(Map<String, Object> properties, FacegraphDB faceDB)
        throws ThingPreperationException
        {
            if(!willThingBeOK(properties))
            {
                ThingPreperationException ex = new ThingPreperationException("Provided properties are not valid for Human.");
                _logger.error(ex.getMessage());
                throw ex;
            }
            
            Node node = faceDB.getDB().createNode();
            return build(properties, node);
        }
        
        /**
         * Properties have to be valid for Thing!
         * Check properties by willThingBeOK(properties)
         * @param properties
         * @return Thing build on top of node.
         */
        private static Thing build(Map<String, Object> properties, Node node)
        {
            if(properties != null)
                for(Map.Entry<String, Object> i : properties.entrySet())
                {
                    node.setProperty(i.getKey(), i.getValue());
                }
            
            node.setProperty(_nodeTypeProperty, _thingProperty);
            
            return new Thing(node);
        }
        
        /**
         * Check whether passed properties could be used for building a valid Thing.
         * @param node
         * @return True if node is valid for Thing, False if not.
         */
        private static boolean willThingBeOK(Node node) 
        {
            try
            {
            	for(ThingProperty i : ThingProperty.values())
            	{
            		if(node.hasProperty(i.getRawName()) == false)
            			throw new AssertionError(i.toString() + " not included!");
            	}
            	if(!isCategoryOK(node.getProperty(ThingProperty.CATEGORY.getRawName())))
            		throw new AssertionError("Bad value of Category property");
            } catch (AssertionError e) {
                _logger.error("Provided node: " + node.toString() + " can NOT be Human becouse: " + e.getMessage());
                return false;
            }
            return true;
        }
        
        /**
         * Check whether passed properties could be used for building a valid Thing node.
         * @param properties
         * @return True if properties are valid for Thing, False if not.
         */
        private static boolean willThingBeOK(Map<String, Object> properties) 
        {
            try
            {
            	for(ThingProperty i : ThingProperty.values())
            	{
            		if(properties.containsKey(i.getRawName()) == false)
            			throw new AssertionError(i.toString() + " not included!");
            	}  
            	if(!isCategoryOK(properties.get(ThingProperty.CATEGORY.getRawName())))
            		throw new AssertionError("Bad value of Category property");
            } catch (AssertionError e) {
                _logger.error("Provided properties are not valid for human: " + properties.toString() + " due to " + e.getMessage());
                return false;
            }
            return true;
        }
        
        private static boolean isCategoryOK(Object category)
        {
            /*TODO*/ 	
            return true;
        }
    }
    
 }
