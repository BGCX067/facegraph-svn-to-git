import org.neo4j.graphdb.Node;

import facegraph.Facegraph;


public class PreDemo 
{
	public static void main(String[] args)
	{		
        Facegraph face = new Facegraph(System.getProperty("user.dir")+"/db");

        face.getData("C://Users//ravwojdyla//workspace//facegraph//harvested");
        
        face.shutdown();
	}	
}