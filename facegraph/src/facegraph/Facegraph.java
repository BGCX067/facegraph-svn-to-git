/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package facegraph;

import java.util.Iterator;

import org.apache.log4j.PropertyConfigurator;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.kernel.EmbeddedGraphDatabase;

import com.rav.common.progress.WatchableTask;

import facegraph.core.FacegraphDB;

/**
 * Main class of project.
 */
public class Facegraph {
	private FacegraphDB _db = null;
	private static org.apache.log4j.Logger _logger = org.apache.log4j.Logger
			.getLogger( Facegraph.class );
	
	private WatchableTask loadDataTask;
	private boolean isDbEmpty;
	
	/**
	 * Constructor for Facegraph, creates Facegraph with database dir given by
	 * dirToDB
	 * 
	 * @param dirToDB
	 *            Path to database, if Null creates database in curdir.
	 */
	public Facegraph( String dirToDB ) {
		PropertyConfigurator.configure( "log4j.properties" );

		_logger.info( "Log4j configured" );
		_logger.info( "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" );

		if (dirToDB == null) {
			dirToDB = System.getProperty( "user.dir" ) + "/db";
		}

		_logger.info( "Starting Facegraph with database stored in: " + dirToDB );

		GraphDatabaseService graphDb = new EmbeddedGraphDatabase( dirToDB );
		
		registerShutdownHook( graphDb );
		_db = new FacegraphDB( graphDb );
		checkIfDbIsEmpty();
		_logger.info( "Facegraph started" );
	}

	/**
	 * Getter for Facegraph Database
	 * 
	 * @return Current instance of Facegraph Database
	 */
	public FacegraphDB getDB() {
		return _db;
	}

	public boolean checkIfDbIsEmpty()
	{
		Iterator<Node> i = _db.getDB().getAllNodes().iterator();
		i.next(); /* Node 0 - is always present */
		if(i.hasNext())
			return this.isDbEmpty = false;
		return this.isDbEmpty = true;
		
	}
	
	

	/**
	 * Registers a shutdown hook for the Neo4j instance so that it shuts down
	 * nicely when the VM exits (even if you "Ctrl-C" the running example before
	 * it's completed)
	 * 
	 * @param graphDb
	 *            the database instance
	 */
	private static void registerShutdownHook( final GraphDatabaseService graphDb ) {
		Runtime.getRuntime().addShutdownHook( new Thread() {
			@Override
			public void run() {
				graphDb.shutdown();
			}
		} );
	}

	/**
	 * Wrapper of GraphDatabaseService shutdown method for Facegraph.
	 */
	public void shutdown() {
		_db.getDB().shutdown();
	}

	/* For test only - START */

	public static void main( String[] args ) {
		PropertyConfigurator.configure( "log4j.properties" );
		Facegraph face = new Facegraph( System.getProperty( "user.dir" )
				+ "/db" );
		_logger.info( "Starting Facegraph" );
	}
	/* Fot test only - END */
}
