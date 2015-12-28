/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package facegraph;



import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.log4j.PropertyConfigurator;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.index.RelationshipIndex;
import org.neo4j.kernel.EmbeddedGraphDatabase;

import facegraph.core.FacegraphDB;
import facegraph.data.FacegraphJSONFileReader;

/**
 * Main class of project.
 */
public class Facegraph 
{
    private FacegraphDB _db = null;
    private RelationshipIndex _friends = null;
    private static org.apache.log4j.Logger _logger = org.apache.log4j.Logger.getLogger(Facegraph.class);
    
    /**
     * Constructor for Facegraph, creates Facegraph with database dir given by dirToDB
     * @param dirToDB Path to database, if Null creates database in curdir.
     */
    public Facegraph(String dirToDB)
    {
    	PropertyConfigurator.configure("log4j.properties");

    	_logger.info("Log4j configured");
    	_logger.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    	
    	if(dirToDB == null)
    	{
    		dirToDB = System.getProperty("user.dir")+"/db";
    	}
    	
    	_logger.info("Starting Facegraph with database stored in: " + dirToDB);
    	
        GraphDatabaseService graphDb = new EmbeddedGraphDatabase(dirToDB);
        registerShutdownHook(graphDb);
        _db = new FacegraphDB(graphDb);
        
        _friends = _db.getDB().index().forRelationships("friends");
        
        _logger.info("Facegraph started");
    }
    
    
    /**
     * Getter for Facegraph Database
     * @return Current instance of Facegraph Database
     */
    public FacegraphDB getDB()
    {
        return _db;
    }
    
    /**
     * Loads data to database. 
     * @param pathToSrcDir points to directory which contains files directories. Each directory represents node.
     */
    public void getData(String pathToSrcDir)
    {
    	File mainDir = new File(pathToSrcDir);
    	
    	if(!mainDir.isDirectory())
    	{
    		IllegalArgumentException ex = new IllegalArgumentException("Path does NOT specify exisiting directory.");
    		_logger.error("Bad main data directory: " + mainDir.getAbsoluteFile(), ex);
    		throw ex;
    	}
    	 
    	final String[] nodesDirs = mainDir.list();
    	
    	/*If there are no nodesDirs, we can finish. It means that there will be no nodes!*/
    	if(nodesDirs.length == 0)
    		return;
    	
        getProfiles(mainDir.getAbsolutePath(), nodesDirs);
        
        getFriends(mainDir.getAbsolutePath(), nodesDirs);
        
        getLikes(mainDir.getAbsolutePath(), nodesDirs);
    	
    }
    
    private void getProfiles(String parentPath, String[] nodesDirs)
    {
    	FacegraphJSONFileReader reader = new FacegraphJSONFileReader(parentPath + File.separator + nodesDirs[0]);
    	for(String nodeFile : nodesDirs)
    	{
    		reader.changeDir(parentPath + File.separator + nodeFile);
    		
    		Map<String, Object> properties = reader.readProfile();
    		if(properties != null)
    			_db.addNode(properties);
    	}
    }
    
    private void getFriends(String parentPath, String[] nodesDirs)
    {
    	FacegraphJSONFileReader reader = new FacegraphJSONFileReader(parentPath + File.separator + nodesDirs[0]);
    	for(String nodeFile : nodesDirs)
    	{
    		reader.changeDir(parentPath + File.separator + nodeFile);
    		
    		List<String> friends = reader.readFriends();
    		if(friends.size() == 0)
    			continue;
    		for(String i : friends)
    		{
    			_db.makeFriends(nodeFile, i);
    		}
    	}
    }
    
    private void getLikes(String parentPath, String[] nodesDirs)
    {
    	FacegraphJSONFileReader reader = new FacegraphJSONFileReader(parentPath + File.separator + nodesDirs[0]);
    	for(String nodeFile : nodesDirs)
    	{
    		reader.changeDir(parentPath + File.separator + nodeFile);
    		
    		List<String> likes = reader.readLikes();
    		if(likes.size() == 0)
    			continue;
    		for(String i : likes)
    		{
    			_db.makeLike(nodeFile, i);
    		}
    	}
    }
    
    /**
     * Registers a shutdown hook for the Neo4j instance so that it
     * shuts down nicely when the VM exits (even if you "Ctrl-C" the
     * running example before it's completed)
     * @param graphDb the database instance 
     */
    private static void registerShutdownHook( final GraphDatabaseService graphDb )
    {
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
            graphDb.shutdown();
            }
        } 
        );
    }
    
    /**
     * Wrapper of GraphDatabaseService shutdown method for Facegraph.
     */
    public void shutdown()
    {
    	_db.getDB().shutdown();
    }
    
    
    /*For test only - START*/
    
    public static void main(String[] args) 
    {
        PropertyConfigurator.configure("log4j.properties");
        Facegraph face = new Facegraph(System.getProperty("user.dir")+"/db");
        _logger.info("Starting Facegraph");
    }
    /*Fot test only - END*/
}
