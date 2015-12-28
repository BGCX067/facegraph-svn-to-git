package facegraph.constants;

import java.io.File;

public interface Const 
{
	public final static String version = "0.2a";
	public final static int TRANSACTION_RESET_INTERVAL = 10000;
	public final static int TRANSACTION_RESET_INTERVAL_REL = 1000;
	public final static int TRANSACTION_RESET_INTERVAL_REL_LIKE = 500;
	
	public final static char PATH_SEPARATOR = File.separatorChar;
	
	public final static String USER_DATABASE_LOCATION = System.getProperty("user.dir") + PATH_SEPARATOR + "db";

	public final static String USER_DIR = System.getProperty("user.dir");
	
	public final static String USER_VIS_LOCATION = System.getProperty("user.dir") + PATH_SEPARATOR + "vis";
	
	/*Properties*/
	
	public static final String NID_PROPERTY_RAW = "id";
	public static final String NID_PROPERTY_PRETTY = "Network Id";
	
	public static final String NTYPE_PROPERTY_RAW = "ntype";
	public static final String NTYPE_PROPERTY_PRETTY = "Network Id";
	
	public static final String NAME = "name";
	
	/*Human specific*/
	
	public static final String HUMAN_FIRST_NAME = "first_name";
	public static final String HUMAN_GENDER = "gender";
	
	/*Thing specific*/
	
	public static final String THING_CATEGORY = "category";
	
	
	/*Nodes category*/
	public static final String NODE_TYPE = "node_type";
	public static final String HUMAN_TYPE = "Human";
	public static final String THING_TYPE = "Thing";
	
	/*Network type*/
	public static final String NETWORK_TYPE = "ntype";
	public static final String FACEBOOK_NETWORK_TYPE = "Facebook";
	
	
	/*indexes*/
	public static final String INDEX_NAME_NODE_ID = "nids";
	public static final String INDEX_NAME_NODE_NAME = "names";
	public static final String INDEX_NAME_RELATIONSHIP_FRIEND = "friends";
	public static final String INDEX_NAME_RELATIONSHIP_LIKE = "likes";
	public static final String INDEX_ELEM_NODE_ID = "nid";
	public static final String INDEX_ELEM_NODE_NAME = "name";
	public static final String INDEX_ELEM_RELETIONSHIP_FIREND = "friend";
	public static final String INDEX_ELEM_RELETIONSHIP_LIKE = "like";
	
	
	/* Layouts */
	
	public static final String LAYOUY_FORCE_ATLAS_2 = "ForceAtlas 2";
	public static final String LAYOUT_YIFAN_HU = "Yifan Hu";
	
}
