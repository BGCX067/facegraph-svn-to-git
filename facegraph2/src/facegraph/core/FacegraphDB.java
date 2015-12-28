/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package facegraph.core;

import java.util.Map;

import org.apache.log4j.Logger;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.RelationshipIndex;

import facegraph.element.FacegraphNode;
import facegraph.element.Human;
import facegraph.element.Thing;
import facegraph.element.exception.HumanPreperationException;
import facegraph.element.exception.ThingPreperationException;
import facegraph.element.property.HumanProperty;
import facegraph.element.property.ThingProperty;

/**
 * 
 * @author Administrator
 */
public class FacegraphDB 
{
    private GraphDatabaseService _db = null;
    private static Logger _logger = Logger.getLogger(FacegraphDB.class);
    
    private Index<Node> _nIdsIndex = null;
    private Index<Node> _namesIndex = null;
    private RelationshipIndex _friends = null;
    private RelationshipIndex _likes = null;
    
    private final String _nIdsIndexName = "nid";
    private final String _nameIndexName = "name";
    private final String _friendsName = "friend";
    private final String _likesName = "like";

    
    /**
     * Constructor wraps neo4j read/write database.
     * @param db    neo4j database
     **/
    public FacegraphDB(GraphDatabaseService db)
    {
        _db = db;
        _nIdsIndex = _db.index().forNodes("nids");
        _namesIndex = _db.index().forNodes("names");
        _friends = _db.index().forRelationships("friends");
        _likes = _db.index().forRelationships("likes");
    }
    
    /**
     * @return neo4j database.
     */
    public GraphDatabaseService getDB()
    {
        return _db;
    }
    
    public void makeFriends(String friend1, String friend2)
    {
    	/*Tring to be friend of me*/
    	if(friend1.equals(friend2))
    	{
    		IllegalArgumentException ex = new IllegalArgumentException("Human cannot be friend with it self.");
    		_logger.error("Cannot be friend with it self!", ex);
    		return;
    	}
    	
    	Human friend1Node = Human.nodeAsHuman(findNodeByNid(friend1));
		if(friend1Node == null)
		{
			/*Its bad because adding human want bad for this user so will end adding friends*/
			_logger.warn("Tring to add friends of human that does NOT exist - skipping adding friends. Human id = " + friend1);
			return;
		}
		
		Human friend2Node = Human.nodeAsHuman(findNodeByNid(friend2));
		if(friend2Node == null)
		{
			_logger.warn("Tring to add friend for " + friend1Node.toString() + " but friend of id = " + friend2 + " does NOT exist in DB");
			return;
		}
		
    	/*Check if the KNOWS relationship between this and human already exist*/
    	if(findFriendship(friend1, friend2) != null)
    	{
    		_logger.info("Creating friendship between: " + friend1Node.toString() + " and " 
        			+ friend2Node.toString() + " ended, humans are already friends");
    		return;
    	}

		Relationship rel = friend1Node.friendWith(friend2Node);
		
		if(rel != null)
		{
			Transaction tx = _db.beginTx();
			try
			{
				_friends.add(rel, _friendsName, FacegraphNode.getIndexObject(friend1, friend2));
				tx.success();
			} catch (Exception e) {
				_logger.error("Error of update friends index ", e);
			} finally {
				tx.finish();
			}
		}
    }
    
    public void makeLike(String humanId, String thingId)
    {        
        Human human = Human.nodeAsHuman(findNodeByNid(humanId));
        
		if(human == null)
		{
			/*Its bad because adding human want bad for this user so will end adding friends*/
			_logger.warn("Tring to add likes of human that does NOT exist - skipping adding likes. Human id = " + humanId);
			return;
		}
		
		Thing thing = Thing.nodeAsThing(findNodeByNid(thingId));
		if(thing == null)
		{
			_logger.warn("Tring to add like for " + human.toString() + " but thing of id = " + thingId + " does NOT exist in DB");
			return;
		}
		
		if(findLike(humanId, thingId) != null)
		{
    		_logger.info("Creating like between: " + human.toString() + " and " 
        			+ thing.toString() + " ended, human already likes thing.");
    		return;
		}
		
		Relationship rel = human.like(thing);
		if(rel != null)
		{
			Transaction tx = _db.beginTx();
			try
			{
				
				_likes.add(rel, _likesName, FacegraphNode.getIndexObject(humanId, thingId));
				tx.success();
			} catch (Exception e) {
				_logger.error("Error of update Likes index ", e);
			} finally {
				tx.finish();
			}
		}
		
    }
    
    /**
     * Method try to create Human out of properties and add it to Facebook Database.
     * Properties have to specify at least properties (String, Object) - defined by HumanProperties.
     * Properties have to specify for example NID which stands for Network ID. If there is already Node with 
     * the same NID in Database then new node will NOT be added to Database.
     */
    private Human addHuman(Map<String, Object> properties)
    {           
        Transaction tx = _db.beginTx();
        
        Human out = null;
        try
        {
        	if(!properties.containsKey(HumanProperty.NID.getRawName()))
        		throw new HumanPreperationException("Properties for future HUMAN does not specify Network ID");
        	
        	/*NID is specify, so we can check whether HUMAN is already in database*/
        	if(findNodeByNid(properties.get(HumanProperty.NID.getRawName())) == null)
        	{
        		/*Building HUMAN node*/
        		out = Human.HumanBuilder.build(properties, this);
        		
        		/*Adding created HUMAN node to index. Index of NIDs*/
        		_nIdsIndex.add(out.asNode(), _nIdsIndexName, out.getProperty(HumanProperty.NID.getRawName()));
        		
        		/*Adding created HUMAN node to index. Index of Names. Name = First Name + Last Name*/
        		_namesIndex.add(out.asNode(), _nameIndexName, 
        				out.getProperty(HumanProperty.FIRST_NAME.getRawName()).toString() + 
        				out.getProperty(HumanProperty.LAST_NAME.getRawName()).toString());
        		
        		/*SUCCESS!*/
        		tx.success();
        		
        		_logger.info("Creating: " + out.toString() + " succeded.");
        	}
        	else
        	{
        		/*else if node with that NID is already in Database*/
        		_logger.info("Human: " + findNodeByNid(properties.get(HumanProperty.NID.getRawName())).toString() 
        				+ " was alraedy in database");
        	}
        } catch (Exception e) {
            _logger.error("Creating new human error for: " + properties.toString(), e);
        } finally {
            tx.finish();
        }
        return out;
    }
    
    
    /**
     * Method try to create new Thing node and add it to Facebook Database.
     */
    private Thing addThing(Map<String, Object> properties)
    {
        Transaction tx = _db.beginTx();
        
        Thing out = null;
        try
        {
        	if(!properties.containsKey(ThingProperty.NID.getRawName()))
        		throw new ThingPreperationException("Properties for future Thing does not specify Network ID");
            
        	if(findNodeByNid(properties.get(HumanProperty.NID.getRawName())) == null)
        	{
        		/*Building THING node*/
	        	out = Thing.ThingBuilder.build(properties, this);
	        	
	        	/*Adding created THING node to index. Index of NIDs*/
	        	_nIdsIndex.add(out.asNode(), _nIdsIndexName, out.getProperty(ThingProperty.NID.getRawName()));
	        	
	        	/*Adding created THIGN node to index. Index of Names. Name = Name*/
	        	_namesIndex.add(out.asNode(), _nameIndexName, out.getProperty(ThingProperty.NAME.getRawName()));
	        	
	        	/*SUCCESS*/
	            tx.success();
	            
	            _logger.debug("Creating: " + out.toString() + " succeded.");
        	}
        	else 
        	{
        		/*else if node with that NID is already in Database*/
        		_logger.info("Thing: " + findNodeByNid(properties.get(HumanProperty.NID.getRawName())).toString() 
        				+ " was alraedy in database");
        	}
        } catch (Exception e) {
            _logger.error("Creating new thing error for: " + properties.toString(), e);
        } finally {
            tx.finish();
        }
        return out;
    }
    
    /**
     * Method create new node [Human | Thing] depending on properties.
     * First if properties contains key GENDER then method treats it like a human.
     * If properties doesn't contain key GENDER and contains key CATEGORY then its
     * Thing. Every other situation will throw exception.
     * Note that there can be human with property - CATEGORY.
     * @param properties contains parameters of node
     * @return new Node based on properties.
     */
    public facegraph.element.FacegraphNode addNode(Map<String, Object> properties)
    {
        _logger.debug("Will try to add new Node to database.");
        
        
       if(properties.containsKey(HumanProperty.FIRST_NAME.getRawName()))
           return addHuman(properties);
       else if(properties.containsKey(ThingProperty.CATEGORY.getRawName()))
           return addThing(properties);
       
       IllegalArgumentException ex = new IllegalArgumentException("Bad proprties: " + properties.toString());
       _logger.error("Can't add node of any type - bad properties.", ex);
       throw ex;
    }
    
    
    public Node findNodeByNid(String nid)
    {
    	return _nIdsIndex.get(_nIdsIndexName, nid).getSingle();
    }
    
    public Node findNodeByNid(Object nid)
    {
    	return _nIdsIndex.get(_nIdsIndexName, nid).getSingle();
    }
    
    public Relationship findFriendship(String friend1, String friend2)
    {
    	return _friends.get(_friendsName, FacegraphNode.getIndexObject(friend1, friend2)).getSingle();
    }
    
    public Relationship findLike(String human, String thing)
    {
    	return _likes.get(_likesName, FacegraphNode.getIndexObject(human, thing)).getSingle();
    }
}
