package facegraph.layout;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import facegraph.constants.Const;

public class VisConfigGenerator {
	private String fileName;
	private int nodeSizeFactory;
	private int edgeWidthFactor;
	private boolean curvedEdges;
	
	private final String Config_start = "setParams({showEdges : true, useLens : false, zoomLevel : 0,";
	private final String Config_end = "});";

	public VisConfigGenerator() {
		fileName = "view.gexf";
		nodeSizeFactory = 8;
		edgeWidthFactor = 15;
		curvedEdges = true;
	}

	public VisConfigGenerator setFileName( String filename ) {
		this.fileName = filename;
		return this;
	}

	public VisConfigGenerator setNodeSizeFactory( int nodeSizeFactory ) {
		this.nodeSizeFactory = nodeSizeFactory;
		return this;
	}

	public VisConfigGenerator setEdgeWidthFactor( int edgeWidthFactor ) {
		this.edgeWidthFactor = edgeWidthFactor;
		return this;
	}

	public VisConfigGenerator setCurvedEdges( boolean curvedEdges ) {
		this.curvedEdges = curvedEdges;
		return this;
	}
	
	public String getFileName() {
		return  "graphFile : \"" + fileName + "\",";
	}

	public String getNodeSizeFactory() {
		return "nodeSizeFactor : " + nodeSizeFactory +',';
	}

	public String getEdgeWidthFactor() {
		return " edgeWidthFactor :" + edgeWidthFactor +',';
	}

	public String isCurvedEdges() {
		return "curvedEdges : " + curvedEdges + ',';
	}

	public void generate()
			throws IOException {
		BufferedWriter writer = new BufferedWriter( new FileWriter( new File( Const.USER_VIS_LOCATION + "/config.js" ) ) );
		writer.write( Config_start );
		writer.write( isCurvedEdges() );
		writer.write( getEdgeWidthFactor() );
		writer.write( getNodeSizeFactory() );
		writer.write( getFileName() );
		writer.write( Config_end );
		writer.flush();
		writer.close();
	}
	
	/*public static void main(String[] args) throws IOException
	{
		new VisConfigGenerator().generate();
	}*/

}
