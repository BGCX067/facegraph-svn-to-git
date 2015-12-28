package facegraph.constants;

import java.io.File;

public interface Const 
{

	public final static char PATH_SEPARATOR = File.separatorChar;
	
	public final static String USER_DATABASE_LOCATION = System.getProperty("user.dir") + PATH_SEPARATOR + "db";

	
	/*properties*/
	
	public static final String NID_PROPERTY_RAW = "nid";
	public static final String NID_PROPERTY_PRETTY = "Network Id";
	
	public static final String NTYPE_PROPERTY_RAW = "ntype";
	public static final String NTYPE_PROPERTY_PRETTY = "Network Id";
	
	
}
