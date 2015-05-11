package model;

import java.util.ArrayList;

import edu.uci.ics.jung.algorithms.scoring.BetweennessCentrality;
import edu.uci.ics.jung.algorithms.scoring.DegreeScorer;
import edu.uci.ics.jung.algorithms.shortestpath.DistanceStatistics;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;

public class GraphMetrics {
	private Graph<String, String> graph;
	
	public GraphMetrics(ArrayList<Table> nodes, ArrayList<ForeignKey> edges){
		
		graph = new DirectedSparseGraph<String, String>();
		addNodes(nodes);
		addEdges(edges);

		
	}

	public Graph<String, String> getGraph() {
		return graph;
	}
	
	private void addNodes(ArrayList<Table> nodes) {

		for (int i = 0; i <nodes.size(); ++i)
			graph.addVertex(nodes.get(i).getKey());

	}

//	private void addEdges(ArrayList<ForeignKey> edges) {
//
//		for (int i = 0; i <edges.size(); ++i)
//			graph.addEdge(Integer.toString(i), edges.get(i)
//					.getSourceTable(), edges.get(i)
//					.getTargetTable());
//
//	}
	
	private void addEdges(ArrayList<ForeignKey> edges) {

		for (int i = 0; i <edges.size(); ++i)
			graph.addEdge(edges.get(i).getSourceTable()+"|"+ edges.get(i).getTargetTable(), edges.get(i)
					.getSourceTable(), edges.get(i)
					.getTargetTable());

	}
	
	public String generateVertexDegreeReport(String vertex){
		
		
		vertex=vertex.replace(",","").trim();
		
		DegreeScorer ds = new DegreeScorer(graph);

	
		return ds.getVertexScore(vertex)+",";
		
//		used for debuggging
//		return vertex+":"+ds.getVertexScore(vertex)+",";


		
	}
	
	
	public String generateVertexBetweennessReport(String vertex){
		
		vertex=vertex.replace(",","").trim();
		
		BetweennessCentrality bc = new BetweennessCentrality<>(graph);
		
		return bc.getVertexScore(vertex)+",";
		
//		used for debuggging
//		return vertex+":"+bc.getVertexScore(vertex)+",";

		
	}
	
	public String generateEdgeBetweennessReport(String edge){
		
		BetweennessCentrality bc = new BetweennessCentrality(graph); 
		
		edge=edge.replace(",","").trim();

		
		return bc.getEdgeScore(edge)+",";
		
//		used for debuggging
//		return edge+":"+bc.getEdgeScore(edge)+",";
		
		
	}
	
	public String getGraphDiameter(){
		
		DistanceStatistics ds = new DistanceStatistics();
		
		return ds.diameter(graph)+",";
		
		
	}

}
