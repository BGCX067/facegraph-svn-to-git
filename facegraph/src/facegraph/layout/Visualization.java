package facegraph.layout;

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
import org.gephi.filters.spi.Filter;
import org.gephi.filters.spi.FilterProperty;
import org.gephi.filters.spi.NodeFilter;
import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.GraphView;
import org.gephi.graph.api.Node;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.exporter.spi.GraphExporter;
import org.gephi.io.importer.api.ImportController;
import org.gephi.layout.plugin.force.StepDisplacement;
import org.gephi.layout.plugin.force.yifanHu.YifanHuLayout;
import org.gephi.layout.plugin.forceAtlas2.ForceAtlas2;
import org.gephi.layout.plugin.forceAtlas2.ForceAtlas2Builder;
import org.gephi.neo4j.plugin.api.Neo4jImporter;
import org.gephi.partition.api.Partition;
import org.gephi.partition.api.PartitionController;
import org.gephi.partition.plugin.NodeColorTransformer;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.ranking.api.Ranking;
import org.gephi.ranking.api.RankingController;
import org.gephi.ranking.api.Transformer;
import org.gephi.ranking.plugin.transformer.AbstractColorTransformer;
import org.gephi.ranking.plugin.transformer.AbstractSizeTransformer;
import org.gephi.statistics.plugin.GraphDistance;
import org.gephi.statistics.plugin.Modularity;
import org.openide.util.Lookup;

import com.rav.common.progress.WatchableTask;

import facegraph.Facegraph;
import facegraph.constants.Const;

public class Visualization
		extends WatchableTask {
	private final static Logger logger = Logger.getLogger( Visualization.class );
	private AttributeModel attributeModel;
	private GraphModel graphModel;
	private PreviewModel model;
	private ImportController importController;
	private FilterController filterController;
	private RankingController rankingController;
	private DirectedGraph graph;
	private ExportController exportController;
	private PartitionController partitionController;
	private final String name;
	private final Facegraph facegraph;

	/**
	 * Constructor for Visualization object. Represent process of creating
	 * visualization.
	 */
	public Visualization( String name, Facegraph facegraph ) {
		super( 100, name );
		logger.info( "New visualization object created" );

		this.facegraph = facegraph;
		this.name = name;
	}
	
	public void configure()
	{
		this.setProgress( 10, "Initializing visulization" );

		Neo4jImporter importer = new org.gephi.neo4j.plugin.impl.Neo4jImporterImpl();

		/*TODO it should be done only once in lifetime of program */
		importer.importDatabase( this.facegraph.getDB().getDB() );

		attributeModel = Lookup.getDefault().lookup( AttributeController.class ).getModel();
		graphModel = Lookup.getDefault().lookup( GraphController.class ).getModel();
		model = Lookup.getDefault().lookup( PreviewController.class ).getModel();
		importController = Lookup.getDefault().lookup( ImportController.class );
		filterController = Lookup.getDefault().lookup( FilterController.class );
		rankingController = Lookup.getDefault().lookup( RankingController.class );
		exportController = Lookup.getDefault().lookup( ExportController.class );
		partitionController = Lookup.getDefault().lookup( PartitionController.class );

		graph = graphModel.getDirectedGraph();

		logger.info( "Nodes: " + graph.getNodeCount() + "\n Edges: " + graph.getEdgeCount() );

		logger.info( "Visualization object configured" );
	}
	
	/**
	 * Filteres graph by the degree of nodes.
	 * 
	 * @param range
	 *            specifies the range of possible degrees for nodes in graph
	 *            view.
	 * @return return View of graph after processing Filter.
	 */
	public GraphView setDegreeFilter( Range range ) {
		logger.info( "Filter start, for: " + range.toString() );

		DegreeRangeFilter degreeFilter = new DegreeRangeFilter();
		degreeFilter.init( graph );
		degreeFilter.setRange( range );
		Query query = filterController.createQuery( degreeFilter );
		graphModel.setVisibleView( filterController.filter( query ) );

		logger.info( "Filter finished, for " + range.toString() );
		return this.getVisibleView();
	}

	public GraphView setFilter( Filter filter ) {
		logger.info( "Filter start " + filter.toString() + " starts ..." );

		Query query = filterController.createQuery( filter );
		graphModel.setVisibleView( filterController.filter( query ) );

		logger.info( "Filter eneded - success." );

		return this.getVisibleView();
	}
	
	public GraphView removeThings()
	{
		return setFilter( new NodeFilter() {
			
			@Override
			public FilterProperty[] getProperties() {
				return null;
			}
			
			@Override
			public String getName() {
				return "Things remove filter";
			}
			
			@Override
			public boolean init( Graph arg0 ) {
				return true;
			}
			
			@Override
			public void finish() {
			}
			
			@Override
			public boolean evaluate( Graph arg0, Node arg1 ) {
				Object node_type = arg1.getAttributes().getValue( Const.NODE_TYPE );
				
				if(node_type != null && node_type.equals( Const.HUMAN_TYPE ))
					return true;
				return false;
			}
		} );
	}

	/**
	 * Computes Centrality
	 */
	public void computeCentrality() {
		logger.info( "Compute centrality starts ..." );
		GraphDistance distance = new GraphDistance();
		distance.setDirected( true );
		distance.execute( graphModel, attributeModel );
	}

	/**
	 * Ranks nodes color by degree. <BR>
	 * Default colors.
	 */
	public void rankColorByDegree() {
		this.rankColorByDegree( new Color[] { new Color( 0xff55d0 ), new Color( 0xB30000 ) } );
	}

	/**
	 * Ranks nodes color by degree.
	 * 
	 * @param colors
	 *            colors of nodes.
	 */
	public void rankColorByDegree( Color[] colors ) {
		logger.info( "Ranking Color of Node by Degree" );

		Ranking degreeRanking = rankingController.getModel().getRanking( Ranking.NODE_ELEMENT, Ranking.DEGREE_RANKING );
		AbstractColorTransformer colorTransformer = (AbstractColorTransformer) rankingController.getModel().getTransformer( Ranking.NODE_ELEMENT,
				Transformer.RENDERABLE_COLOR );
		colorTransformer.setColors( colors );
		rankingController.transform( degreeRanking, colorTransformer );
	}

	public void rankSizeByCentrality() {
		this.rankSizeByCentrality( new Range( 8, 20 ) );
	}

	/**
	 * Ranks size of node by centrality of node.
	 * 
	 * @param range
	 *            range of sizes of nodes.
	 */
	public void rankSizeByCentrality( Range range ) {
		logger.info( "Ranking Size of Node by Centrality" );

		AttributeColumn centralityColumn = attributeModel.getNodeTable().getColumn( GraphDistance.BETWEENNESS );
		Ranking centralityRanking = rankingController.getModel().getRanking( Ranking.NODE_ELEMENT, centralityColumn.getId() );
		AbstractSizeTransformer sizeTransformer = (AbstractSizeTransformer) rankingController.getModel().getTransformer( Ranking.NODE_ELEMENT,
				Transformer.RENDERABLE_SIZE );
		sizeTransformer.setMinSize( range.getLowerBound().intValue() );
		sizeTransformer.setMaxSize( range.getUpperBound().intValue() );
		rankingController.transform( centralityRanking, sizeTransformer );
	}

	public String export() {
		logger.info( "Export starts ..." );

		GraphExporter exporter = (GraphExporter) exportController.getExporter( "gexf" ); /*
																						 * Gexf
																						 * exporter
																						 */
		exporter.setExportVisible( true );
		/* Only exports the visible (filtered) graph */
		try {
			exportController.exportFile( new File( System.getProperty( "user.dir" ) + "/vis/" + name + ".gexf" ), exporter );
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
		return name;
	}

	/**
	 * Getter for actual Visible View of graph.
	 * 
	 * @return view of visible view of graph.
	 */
	public GraphView getVisibleView() {
		return graphModel.getVisibleView();
	}

	public void modularityClass() {
		Modularity modularity = new Modularity();
		modularity.execute( graphModel, attributeModel );
		// Partition with ‘modularity_class’, just created by Modularity
		// algorithm
		AttributeColumn modColumn = attributeModel.getNodeTable().getColumn( Modularity.MODULARITY_CLASS );
		Partition p = partitionController.buildPartition( modColumn, graph );
		
		logger.info( p.getPartsCount() + " partitions found" );
		
		NodeColorTransformer nodeColorTransformer = new NodeColorTransformer();
		nodeColorTransformer.randomizeColors( p );
		partitionController.transform( p, nodeColorTransformer );
	}
	
	public void vis(VisConfig config)
	{
		this.setProgress( 20, "Filtering" );
		this.setDegreeFilter(config.getFilterByDegree());
		
		if(!config.isShowInterests())
			this.removeThings();

		this.setProgress( 30, "Applying layout" );
		config.getLayout().run( graphModel, config.getArgs() );
		
		this.setProgress( 50, "Computing centrality" );
		this.computeCentrality();

		this.setProgress( 70, "Applying rankings" );
		if(config.isRankByCentrality())
			this.rankSizeByCentrality();
		if(config.isRankByDegree())
			this.rankColorByDegree();

		if(config.isModularityClass())
			this.modularityClass();
		
		this.setProgress( 80, "Exporting" );
		this.export();

		this.setProgress( 100, "Visualization generating - success." );
	}

	public void defaultVis() {
		this.setProgress( 20, "Filtering" );
		this.setDegreeFilter( new Range( 1, Integer.MAX_VALUE ) );

		this.setProgress( 30, "Applying layout" );
		Layouts.YIFAN_HU.run( graphModel, new Object[] {1000, 200f} );

		this.setProgress( 50, "Computing centrality" );
		this.computeCentrality();

		this.setProgress( 70, "Applying rankings" );
		this.rankColorByDegree();
		this.rankSizeByCentrality();

		this.modularityClass();
		
		this.setProgress( 80, "Exporting" );
		this.export();

		this.setProgress( 100, "Visualization generating - success." );
	}
}
