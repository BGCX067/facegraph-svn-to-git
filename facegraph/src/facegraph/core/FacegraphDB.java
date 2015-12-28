/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package facegraph.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.RelationshipIndex;

import facegraph.constants.Const;
import facegraph.element.FacegraphNode;
import facegraph.element.Human;
import facegraph.element.Thing;
import facegraph.element.exception.HumanPreperationException;
import facegraph.element.exception.ThingPreperationException;
import facegraph.element.property.AbstractCategory;
import facegraph.element.property.HumanCategory;
import facegraph.element.property.ThingCategory;

/**
 * Wrapper of Neo4j database. Holds the model of database.
 * 
 * @author ravwojdyla
 */
public class FacegraphDB {
	private GraphDatabaseService _db = null;
	private static Logger _logger = Logger.getLogger( FacegraphDB.class );

	/** Indexes of database */
	private Index<Node> _nIdsIndex = null;
	private Index<Node> _namesIndex = null;
	private RelationshipIndex _friends = null;
	private RelationshipIndex _likes = null;

	/**
	 * Categories valid in Facegraph, use registerCategory to add new category
	 * of nodes
	 */
	private List<AbstractCategory> categories = null;

	/**
	 * Constructor wraps neo4j read/write database.
	 * 
	 * @param db
	 *            neo4j database, exactly the GraphDatabaseService
	 **/
	public FacegraphDB( GraphDatabaseService db ) {
		_db = db;
		_nIdsIndex = _db.index().forNodes( Const.INDEX_NAME_NODE_ID );
		_namesIndex = _db.index().forNodes( Const.INDEX_NAME_NODE_NAME );
		_friends = _db.index().forRelationships(
				Const.INDEX_NAME_RELATIONSHIP_FRIEND );
		_likes = _db.index().forRelationships(
				Const.INDEX_NAME_RELATIONSHIP_LIKE );

		categories = new ArrayList<AbstractCategory>();
		registerCategory( new HumanCategory() );
		registerCategory( new ThingCategory() );
	}

	/**
	 * @return neo4j database.
	 */
	public GraphDatabaseService getDB() {
		return _db;
	}

	/**
	 * Register category that will be valid in Facegraph. <BR>
	 * Category describe possible node in database. <BR>
	 * For example there is category for Human and Thing from facebook network.
	 * 
	 * @param category
	 *            new category, which implements AbstractCategory interface
	 */
	public void registerCategory( AbstractCategory category ) {
		_logger.info( "Registering category: " + category.getClass().toString()
				+ " " + category.toString() );
		categories.add( category );
	}

	/**
	 * Method tries to register friendship between two nodes. <BR>
	 * Both nodes have to be Human nodes, and can NOT be friends so far. Note
	 * that you can NOT be friend of yourself, and also can NOT be friend of
	 * node which is not type of Human.
	 * 
	 * @param friend1
	 *            id of first friend.
	 * @param friend2
	 *            id of second friend.
	 */
	public void makeFriends( String friend1, String friend2 ) {
		/* Tring to be friend of me */
		if (friend1.equals( friend2 )) {
			IllegalArgumentException ex = new IllegalArgumentException(
					"Human cannot be friend with it self." );
			_logger.error( "Cannot be friend with it self!", ex );
			return;
		}

		Human friend1Node = Human.nodeAsHuman( findNodeByNid( friend1 ) );
		if (friend1Node == null) {
			/*
			 * Its bad because adding human want bad for this user so will end
			 * adding friends
			 */
			_logger.warn( "Tring to add friends of human that does NOT exist - skipping adding friends. Human id = "
					+ friend1 );
			return;
		}

		Human friend2Node = Human.nodeAsHuman( findNodeByNid( friend2 ) );
		if (friend2Node == null) {
			_logger.warn( "Tring to add friend for " + friend1Node.toString()
					+ " but friend of id = " + friend2
					+ " does NOT exist in DB" );
			return;
		}

		/* Check if the KNOWS relationship between this and human already exist */
		if (findFriendship( friend1, friend2 ) != null) {
			_logger.info( "Creating friendship between: "
					+ friend1Node.toString() + " and " + friend2Node.toString()
					+ " ended, humans are already friends" );
			return;
		}

		Relationship rel = friend1Node.friendWith( friend2Node );

		if (rel != null) {
//			Transaction tx = _db.beginTx();
			try {
				_friends.add( rel, Const.INDEX_ELEM_RELETIONSHIP_FIREND,
						FacegraphNode.getIndexObject( friend1, friend2 ) );
//				tx.success();
			} catch (Exception e) {
				_logger.error( "Error of update friends index ", e );
			} /*finally {
				tx.finish();
			}*/
		}
	}

	/**
	 * Method tries to register like relationship between Human and Thing. <BR>
	 * Note that you can create that relationship only between Human and Thing.
	 * 
	 * @param humanId
	 *            id of human.
	 * @param thingId
	 *            id of thing.
	 */
	public void makeLike( String humanId, String thingId ) {
		Human human = Human.nodeAsHuman( findNodeByNid( humanId ) );

		if (human == null) {
			/*
			 * Its bad because adding human want bad for this user so will end
			 * adding friends
			 */
			_logger.warn( "Tring to add likes of human that does NOT exist - skipping adding likes. Human id = "
					+ humanId );
			return;
		}

		Thing thing = Thing.nodeAsThing( findNodeByNid( thingId ) );
		if (thing == null) {
			_logger.warn( "Tring to add like for " + human.toString()
					+ " but thing of id = " + thingId + " does NOT exist in DB" );
			return;
		}

		if (findLike( humanId, thingId ) != null) {
			_logger.info( "Creating like between: " + human.toString()
					+ " and " + thing.toString()
					+ " ended, human already likes thing." );
			return;
		}

		Relationship rel = human.like( thing );
		if (rel != null) {
//			Transaction tx = _db.beginTx();
			try {

				_likes.add( rel, Const.INDEX_ELEM_RELETIONSHIP_LIKE,
						FacegraphNode.getIndexObject( humanId, thingId ) );
//				tx.success();
			} catch (Exception e) {
				_logger.error( "Error of update Likes index ", e );
			} /*finally {
				tx.finish();
			}
*/		}

	}

	/**
	 * Sets properties from properties to node.
	 */
	private Node setProperties( Map<String, Object> properties, Node node ) {
		for (Entry<String, Object> i : properties.entrySet()) {
			node.setProperty( i.getKey(), i.getValue() );
		}
		return node;
	}

	/**
	 * Create node and set properties and saves all to database.
	 */
	private Node addToDb( Map<String, Object> properties ) {
//		Transaction tx = _db.beginTx();

		Node out = null;
		try {
			if (findNodeByNid( properties.get( Const.NID_PROPERTY_RAW ).toString() ) == null) {
				/* Building node */
				out = setProperties( properties, _db.createNode() );

				/* Adding created node to index. Index of NIDs */
				_nIdsIndex.add( out, Const.INDEX_ELEM_NODE_ID,
						out.getProperty( Const.NID_PROPERTY_RAW ) );

				/* Adding created node to index. Index of Names. Name = Name */
				_namesIndex.add( out, Const.INDEX_ELEM_NODE_NAME,
						out.getProperty( Const.NAME ) );

				/* SUCCESS */
//				tx.success();

				_logger.debug( "Creating: " + out.toString() + " succeded." );
			} else {
				/* else if node with that NID is already in Database */
				_logger.info( "Node of id: "
						+ properties.get( Const.NID_PROPERTY_RAW )
						+ " was alraedy in database" );
//				tx.success();
			}
		} catch (Exception e) {
			_logger.error(
					"Creating new node error for: " + properties.toString(), e );
		} /*finally {
			tx.finish();
		}*/
		return out;
	}

	/**
	 * Check if properties is some of possible categories registered in
	 * Facegraph. If so sets specific properties of specific category.
	 */
	private Map<String, Object> categories( Map<String, Object> properties ) {
		_logger.debug( "Trying to categorias properties: " + properties );
		for (AbstractCategory i : categories) {
			if (i.validateCategory( properties ))
				return i.addCategory( properties );
		}
		return properties;
	}

	/**
	 * Method tries to create new node of properties. First it check if 
	 * properties contains ID - which is A MUST!. After that properties
	 * are check if are any category and after that if every thing is 
	 * ok, new node is created and saved in db.
	 * @param properties map of properties of future node
	 * @return created node, or null if something went wrong.
	 */
	public Node addNode( Map<String, Object> properties ) {
		_logger.debug( "Will try to add new Node to database." );

		if (!properties.containsKey( Const.NID_PROPERTY_RAW )) {
			_logger.error( "Tring to add node without property of ID - that's MADNESS! It will be skiped. Properties: "
					+ properties );
		}
		return addToDb( categories( properties ) );
	}

	/**
	 * Id Index method.
	 * @param nid id of node that you look for.
	 * @return found node, or null if there wasn't node with that id.
	 */
	public Node findNodeByNid( String nid ) {
		return _nIdsIndex.get( Const.INDEX_ELEM_NODE_ID, nid ).getSingle();
	}

	/**
	 * Friendship Index method
	 * @param friend1 first friend's id.
 	 * @param friend2 second friend's id.
	 * @return Relationship of friend if there is any - null in other case.
	 */
	public Relationship findFriendship( String friend1, String friend2 ) {
		return _friends.get( Const.INDEX_ELEM_RELETIONSHIP_FIREND,
				FacegraphNode.getIndexObject( friend1, friend2 ) ).getSingle();
	}

	/**
	 * Like Index method.
	 * @param human id of human.
	 * @param thing id of thing.
	 * @return Relationship of like if there is any - null in other case.
	 */
	public Relationship findLike( String human, String thing ) {
		return _likes.get( Const.INDEX_ELEM_RELETIONSHIP_LIKE,
				FacegraphNode.getIndexObject( human, thing ) ).getSingle();
	}
}
