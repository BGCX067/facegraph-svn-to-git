package test;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.filters.api.FilterController;
import org.gephi.filters.api.Query;
import org.gephi.filters.api.Range;
import org.gephi.filters.plugin.graph.DegreeRangeBuilder.DegreeRangeFilter;
import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.GraphView;
import org.gephi.graph.api.UndirectedGraph;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.importer.api.ImportController;
import org.gephi.layout.plugin.force.StepDisplacement;
import org.gephi.layout.plugin.force.yifanHu.YifanHuLayout;
import org.gephi.neo4j.plugin.api.Neo4jImporter;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.types.EdgeColor;
import org.gephi.ranking.api.Ranking;
import org.gephi.ranking.api.RankingController;
import org.gephi.ranking.api.Transformer;
import org.gephi.ranking.plugin.transformer.AbstractColorTransformer;
import org.gephi.ranking.plugin.transformer.AbstractSizeTransformer;
import org.gephi.statistics.plugin.GraphDistance;
import org.openide.util.Lookup;

import com.rav.common.progress.WatchableTask;

import facegraph.Facegraph;

public class FacegraphExporter extends WatchableTask {
	protected final Facegraph face;
	protected final static Logger logger = Logger
			.getLogger( FacegraphExporter.class );

	public FacegraphExporter( Facegraph face ) {
		super( 100, "Generate view" );
		logger.info( "Creating new Facegraph Exporter" );
		this.face = face;
	}

	public void gen( Facegraph face ) {

		logger.info( "Generation of preview started ..." );
		Neo4jImporter importer = new org.gephi.neo4j.plugin.impl.Neo4jImporterImpl();

		importer.importDatabase( face.getDB().getDB() );
		this.setProgress( 20, "Database translated" );

		AttributeModel attributeModel = Lookup.getDefault()
				.lookup( AttributeController.class ).getModel();
		GraphModel graphModel = Lookup.getDefault()
				.lookup( GraphController.class ).getModel();
		PreviewModel model = Lookup.getDefault()
				.lookup( PreviewController.class ).getModel();
		ImportController importController = Lookup.getDefault().lookup(
				ImportController.class );
		FilterController filterController = Lookup.getDefault().lookup(
				FilterController.class );
		RankingController rankingController = Lookup.getDefault().lookup(
				RankingController.class );

		DirectedGraph graph = graphModel.getDirectedGraph();
		System.out.println( "Nodes: " + graph.getNodeCount() );
		System.out.println( "Edges: " + graph.getEdgeCount() );

		this.setProgress( 30, "Graph model generated" );

		DegreeRangeFilter degreeFilter = new DegreeRangeFilter();
		degreeFilter.init( graph );
		degreeFilter.setRange( new Range( 1, Integer.MAX_VALUE ) ); // Remove
																	// nodes
																	// with
																	// degree <
																	// 30
		Query query = filterController.createQuery( degreeFilter );
		GraphView view = filterController.filter( query );
		graphModel.setVisibleView( view ); // Set the filter result as the
											// visible view

		this.setProgress( 50, "Filtration ..." );

		// See visible graph stats
		UndirectedGraph graphVisible = graphModel.getUndirectedGraphVisible();
		System.out.println( "Nodes: " + graphVisible.getNodeCount() );
		System.out.println( "Edges: " + graphVisible.getEdgeCount() );

		this.setProgress( 70, "Applying layout ..." );

		// Run YifanHuLayout for 100 passes - The layout always takes the
		// current visible view
		YifanHuLayout layout = new YifanHuLayout( null, new StepDisplacement(
				1f ) );
		layout.setGraphModel( graphModel );
		layout.resetPropertiesValues();
		layout.setOptimalDistance( 200f );
		layout.initAlgo();

		for (int i = 0; i < 100 && layout.canAlgo(); i++) {
			layout.goAlgo();
		}
		layout.endAlgo();

		this.setProgress( 80, "Additional computations ..." );

		// Get Centrality
		GraphDistance distance = new GraphDistance();
		distance.setDirected( true );
		distance.execute( graphModel, attributeModel );

		// Rank color by Degree
		Ranking degreeRanking = rankingController.getModel().getRanking(
				Ranking.NODE_ELEMENT, Ranking.DEGREE_RANKING );
		AbstractColorTransformer colorTransformer = (AbstractColorTransformer) rankingController
				.getModel().getTransformer( Ranking.NODE_ELEMENT,
						Transformer.RENDERABLE_COLOR );
		colorTransformer.setColors( new Color[] { new Color( 0xff55d0 ),
				new Color( 0xB30000 ) } );
		rankingController.transform( degreeRanking, colorTransformer );

		// Rank size by centrality
		AttributeColumn centralityColumn = attributeModel.getNodeTable()
				.getColumn( GraphDistance.BETWEENNESS );
		Ranking centralityRanking = rankingController.getModel().getRanking(
				Ranking.NODE_ELEMENT, centralityColumn.getId() );
		AbstractSizeTransformer sizeTransformer = (AbstractSizeTransformer) rankingController
				.getModel().getTransformer( Ranking.NODE_ELEMENT,
						Transformer.RENDERABLE_SIZE );
		sizeTransformer.setMinSize( 3 );
		sizeTransformer.setMaxSize( 10 );
		rankingController.transform( centralityRanking, sizeTransformer );

		this.setProgress( 90, "Generate preview ..." );
		// Preview
		model.getProperties().putValue( PreviewProperty.SHOW_NODE_LABELS,
				Boolean.TRUE );
		model.getProperties().putValue( PreviewProperty.EDGE_COLOR,
				new EdgeColor( Color.GRAY ) );
		model.getProperties().putValue( PreviewProperty.EDGE_THICKNESS,
				new Float( 0.1f ) );
		model.getProperties().putValue(
				PreviewProperty.NODE_LABEL_FONT,
				model.getProperties()
						.getFontValue( PreviewProperty.NODE_LABEL_FONT )
						.deriveFont( 8 ) );

		this.setProgress( 95, "Export preview ..." );

		logger.info( "Preview generated. Exporting preview ..." );

		ExportController ec = Lookup.getDefault().lookup(
				ExportController.class );
		try {
			ec.exportFile( new File( System.getProperty( "user.dir" )
					+ "/vis/view.gexf" ) );
		} catch (IOException ex) {
			logger.error( "Exporting preview failed.", ex );
			this.setProgress( 100, "View generation failed -> logs" );
			return;
		}
		this.setProgress( 100, "View generated successfully." );
		logger.info( "Preview exported successfully" );
	}
}
