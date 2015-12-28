/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package facegraph.element;

import org.apache.log4j.Logger;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;

import facegraph.connection.FacegraphRelationship;
import facegraph.element.property.AbstractCategory;
import facegraph.element.property.HumanCategory;
/**
 * Represents human being in social network.
 */
public class Human 
extends FacegraphNode
{
    private static Logger _logger = Logger.getLogger(Human.class);
    private static AbstractCategory category = new HumanCategory();
    
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
//    	Transaction tx = _wrappedNode.getGraphDatabase().beginTx();
    	Relationship rel = null;
    	try
    	{
        	rel = _wrappedNode.createRelationshipTo(human.asNode(), FacegraphRelationship.KNOWS);
//        	tx.success();
        	
        	_logger.info("Creating friendship between: " + this.toString() + " and " 
        			+ human.toString() + " succeded.");
    	} catch (Exception e) {
    		_logger.error("Creating friendship between" + this.toString() + " and " 
    				+ human.toString() + " ended with error.", e);
    	} /*finally {
    		tx.finish();
    	}*/
		return rel;

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
    	} /*finally {
    		tx.finish();
    		return rel;
    	}*/
    	return rel;
    }
    
    @Override
    public String toString() {
    	StringBuilder buildStr = new StringBuilder("Human : ");
    	for(String i : category.getProperties())
    	{
    		if(_wrappedNode.hasProperty( i ))
    			buildStr.append( "{" + i + " : " + _wrappedNode.getProperty( i ) + " }" );
    	}
    	buildStr.append( "." );
    	return buildStr.toString();
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
        if(node != null && category.validateCategory( node ))
            out = new Human(node); //HumanBuilder.build(null, node);
        else
            _logger.debug("There was no way to convert node to human!");
    
        return out;
    }
}
