package test;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

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

import facegraph.Facegraph;


public class FacegraphExporter 
{
	public static void gen(Facegraph face)
	{
		 Neo4jImporter importer = new org.gephi.neo4j.plugin.impl.Neo4jImporterImpl();
	     
	     importer.importDatabase(face.getDB().getDB());
	     
	     AttributeModel attributeModel = Lookup.getDefault().lookup(AttributeController.class).getModel();
	     GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();
	     PreviewModel model = Lookup.getDefault().lookup(PreviewController.class).getModel();
	     ImportController importController = Lookup.getDefault().lookup(ImportController.class);
	     FilterController filterController = Lookup.getDefault().lookup(FilterController.class);
	     RankingController rankingController = Lookup.getDefault().lookup(RankingController.class);
	     
	     DirectedGraph graph = graphModel.getDirectedGraph();
	     System.out.println("Nodes: " + graph.getNodeCount());
	     System.out.println("Edges: " + graph.getEdgeCount());
	     
	     DegreeRangeFilter degreeFilter = new DegreeRangeFilter();
	     degreeFilter.init(graph);
	     degreeFilter.setRange(new Range(1, Integer.MAX_VALUE));     //Remove nodes with degree < 30
	     Query query = filterController.createQuery(degreeFilter);
	     GraphView view = filterController.filter(query);
	     graphModel.setVisibleView(view);    //Set the filter result as the visible view
	      
	     //See visible graph stats
	     UndirectedGraph graphVisible = graphModel.getUndirectedGraphVisible();
	     System.out.println("Nodes: " + graphVisible.getNodeCount());
	     System.out.println("Edges: " + graphVisible.getEdgeCount());
	     
	   //Run YifanHuLayout for 100 passes - The layout always takes the current visible view
	     YifanHuLayout layout = new YifanHuLayout(null, new StepDisplacement(1f));
	     layout.setGraphModel(graphModel);
	     layout.resetPropertiesValues();
	     layout.setOptimalDistance(200f);
	     layout.initAlgo();
	      
	     for (int i = 0; i < 100 && layout.canAlgo(); i++) {
	         layout.goAlgo();
	     }
	     layout.endAlgo();
	      
	     //Get Centrality
	     GraphDistance distance = new GraphDistance();
	     distance.setDirected(true);
	     distance.execute(graphModel, attributeModel);
	      
	     //Rank color by Degree
	     Ranking degreeRanking = rankingController.getModel().getRanking(Ranking.NODE_ELEMENT, Ranking.DEGREE_RANKING);
	     AbstractColorTransformer colorTransformer = (AbstractColorTransformer) rankingController.getModel().getTransformer(Ranking.NODE_ELEMENT, Transformer.RENDERABLE_COLOR);
	     colorTransformer.setColors(new Color[]{new Color(0xff55d0), new Color(0xB30000)});
	     rankingController.transform(degreeRanking,colorTransformer);
	      
	     //Rank size by centrality
	     AttributeColumn centralityColumn = attributeModel.getNodeTable().getColumn(GraphDistance.BETWEENNESS);
	     Ranking centralityRanking = rankingController.getModel().getRanking(Ranking.NODE_ELEMENT, centralityColumn.getId());
	     AbstractSizeTransformer sizeTransformer = (AbstractSizeTransformer) rankingController.getModel().getTransformer(Ranking.NODE_ELEMENT, Transformer.RENDERABLE_SIZE);
	     sizeTransformer.setMinSize(3);
	     sizeTransformer.setMaxSize(10);
	     rankingController.transform(centralityRanking,sizeTransformer);
	      
	     //Preview
	     model.getProperties().putValue(PreviewProperty.SHOW_NODE_LABELS, Boolean.TRUE);
	     model.getProperties().putValue(PreviewProperty.EDGE_COLOR, new EdgeColor(Color.GRAY));
	     model.getProperties().putValue(PreviewProperty.EDGE_THICKNESS, new Float(0.1f));
	     model.getProperties().putValue(PreviewProperty.NODE_LABEL_FONT, model.getProperties().getFontValue(PreviewProperty.NODE_LABEL_FONT).deriveFont(8));
	    
	     ExportController ec = Lookup.getDefault().lookup(ExportController.class);
	        try
	        {
	        	ec.exportFile(new File("test3.gexf"));
	        } catch(IOException ex) {
	        	ex.printStackTrace();
	        	return;
	        }
	}
}
