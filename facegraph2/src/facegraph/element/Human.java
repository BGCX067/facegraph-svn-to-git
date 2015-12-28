/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package facegraph.element;

import java.util.Map;

import org.apache.log4j.Logger;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;

import facegraph.connection.FacegraphRelationship;
import facegraph.core.FacegraphDB;
import facegraph.element.exception.HumanPreperationException;
import facegraph.element.property.HumanProperty;
/**
 * Represents human being in social network.
 */
public class Human 
extends FacegraphNode
{
    private static final String _humanProperty = "HUMAN";
    private static Logger _logger = Logger.getLogger(Human.class);
    
    /**
     * Private constructor of Human for usage of HumanBuilder.
     * @param   node    is nothing more but core of Human.
     **/
    private Human(Node node)
    {
        super(node);
    }
    
    /**
     * Method creates KNOWS relationship between this -> human.<BR>
     * KNOWS relationship is symmetric. So if the is already KNOWS relationship
     * between human -> this, new relationship will NOT be created.<BR>
     * @param human will be a friend of this.
     * <BR>
     * KNOWS relationship is irreflexive, it means that you can NOT be friend of 
     * yourself.
     */
    public Relationship friendWith(Human human)
    {    	    	
    	/*Main transaction of creating friends*/
    	Transaction tx = _wrappedNode.getGraphDatabase().beginTx();
    	Relationship rel = null;
    	try
    	{
        	rel = _wrappedNode.createRelationshipTo(human.asNode(), FacegraphRelationship.KNOWS);
        	tx.success();
        	
        	_logger.info("Creating friendship between: " + this.toString() + " and " 
        			+ human.toString() + " succeded.");
    	} catch (Exception e) {
    		_logger.error("Creating friendship between" + this.toString() + " and " 
    				+ human.toString() + " ended with error.", e);
    	} finally {
    		tx.finish();
    		return rel;
    	}
    	
    }
    
    
    public Relationship like(Thing thing)
    {   
    	/*Main transaction of creating friends*/
//    	Transaction tx = _wrappedNode.getGraphDatabase().beginTx();
    	Relationship rel = null;
    	try
    	{
    		rel = _wrappedNode.createRelationshipTo(thing.asNode(), FacegraphRelationship.LIKES);
//        	tx.success();
        	
        	_logger.info("Creating like between: " + this.toString() + " and " 
        			+ thing.toString() + " succeded.");
    	} catch (Exception e) {
    		_logger.error("Creating like between" + this.toString() + " and " 
    				+ thing.toString() + " ended with error.", e);
    	} finally {
//    		tx.finish();
    		return rel;
    	}
    	
    }
    
    @Override
    public String toString()
    {
        return "Human: " + getProperty(HumanProperty.FIRST_NAME.getRawName()) 
                + " " + getProperty(HumanProperty.LAST_NAME.getRawName()) + ", " + HumanProperty.GENDER.toString()
                + ": " + getProperty(HumanProperty.GENDER.getRawName());
    }
    
    @Override
    public boolean equals( Object o )
    {
        return o instanceof Human &&
                _wrappedNode.equals( ( (Human)o ).asNode() );
    }
    
    /**
     * Static factory for Human. Builds Human out of nodes, only if node has properties for valid Human.
     * @param node willHumanBeOK(node) is performed to check validation of node.
     * @return 1. node == null ? null : Human(node) <BR> 2. willHumanBeOK(node) ? Human(node) : null
     **/
    public static Human nodeAsHuman(Node node)
    {	
        Human out = null;
        if(node != null && HumanBuilder.willHumanBeOK(node))
            out = new Human(node); //HumanBuilder.build(null, node);
        else
            _logger.debug("There was no way to convert node to human!");
    
        return out;
    }
    
    /**
     * HumanBuilder is a builder pattern class for Human.<BR>
     * Before it will return Human, will check presence of properties defined 
     * in HumanProperties enum class.
     **/
    public static class HumanBuilder
    {                
    	/**
    	 * Main factory method, builds human.
    	 * @param properties Map with future human properties.
    	 * @param faceDB Facegraph database in which the new human will be saved.
    	 * @return New instance of Human with valid properties.
    	 * @throws HumanPreperationException if properties are not valid, or
    	 * are not complete Exception will be throw. Check willHumanBeOK method.
    	 */
        public static Human build(Map<String, Object> properties, FacegraphDB faceDB)
        throws HumanPreperationException
        {
            if(!willHumanBeOK(properties))
            {
                HumanPreperationException ex = new HumanPreperationException("Provided properties are not valid for Human.");
                _logger.error(ex.getMessage());
                throw ex;
            }
            
            Node node = faceDB.getDB().createNode();
            
            return HumanBuilder.build(properties, node);
        }
        
        /**
         * In this method properties have to be correct for Human!<BR>
         * Validation of properties for Human - willHumanBeOK(properties).
         * @param properties
         * @param node
         * @return Human build on node
         */
        private static Human build(Map<String, Object> properties, Node node)
        {
            if(properties != null)
                for(Map.Entry<String, Object> e : properties.entrySet())
                {
                    node.setProperty(e.getKey(), e.getValue());
                }
            
            node.setProperty(_nodeTypeProperty, _humanProperty);
            
            return new Human(node);
        }
        
        /**
         * Check whether node could be valid Human.
         * @param node 
         * @return True if node could be Human node, False if not.
         * <BR>
         * The check is performed over node.
         */
        private static boolean willHumanBeOK(Node node)
        {
            try
            {
            	for(HumanProperty i : HumanProperty.values())
            	{
            		if(node.hasProperty(i.getRawName()) == false)
            			throw new AssertionError(i.toString() + " not included!");
            	}
                if(!isGenderOK(node.getProperty(HumanProperty.GENDER.getRawName())))
                	throw new AssertionError("Bad value of Category property");
            } catch (AssertionError e) {
                _logger.error("Provided node: " + node.toString() + " can NOT be Human because: " + e.getMessage());
                return false;
            }
            return true;
        }
        
        /**
         * Check whether passed properties could be used for building a valid Human.
         * @param properties
         * @return True if properties are valid for Human, False if not.
         */
        private static boolean willHumanBeOK(Map<String, Object> properties)
        {
            try
            {
            	for(HumanProperty i : HumanProperty.values())
            	{
            		if(properties.containsKey(i.getRawName()) == false)
            			throw new AssertionError(i.toString() + " not included!");
            	}
                if(!isGenderOK(properties.get(HumanProperty.GENDER.getRawName())))
                	throw new AssertionError("Bad value of Category property");
            } catch (AssertionError e) {
                _logger.error("Provided properties are not valid for human: " + properties.toString() + " due to " + e.getMessage());
                return false;
            }
            return true;
        }
        
        /**
         * Check if passed object is valid for Property GENDER.
         * @param gender
         * @return True if gender is String with value ["male" | "female"].
         */
        private static boolean isGenderOK(Object gender)
        {
            if(!(gender instanceof String))
                return false;
            String strGender = (String)gender;
            if(strGender.equals("male") || strGender.equals("female"))
                return true;
            _logger.debug("Bad gender: " + strGender);
            return false;
        }
    }
}
