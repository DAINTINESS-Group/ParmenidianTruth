package model;

import java.util.ArrayList;

import edu.uci.ics.jung.algorithms.importance.BetweennessCentrality;
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

	private void addEdges(ArrayList<ForeignKey> edges) {

		for (int i = 0; i <edges.size(); ++i)
			graph.addEdge(edges.get(i).getSourceTable()+"|"+ edges.get(i).getTargetTable(), edges.get(i)
					.getSourceTable(), edges.get(i)
					.getTargetTable());

	}
	
	public String generateVertexDegree(String vertex){
		
		
		vertex=vertex.replace(",","").trim();
		
		DegreeScorer ds = new DegreeScorer(graph);

	
		return ds.getVertexScore(vertex)+",";
		
	}
	
	
	public String generateVertexBetweenness(String vertex){
		
		vertex=vertex.replace(",","").trim();
		
		
		 BetweennessCentrality ranker = new BetweennessCentrality(graph);
		 ranker.setRemoveRankScoresOnFinalize(false);
		 ranker.evaluate();
		
		return ranker.getVertexRankScore(vertex)+",";
		
	}
	
	public String generateEdgeBetweenness(String edge){
		
		
		edge=edge.replace(",","").trim();

		 BetweennessCentrality ranker = new BetweennessCentrality(graph);
		 ranker.setRemoveRankScoresOnFinalize(false);
		 ranker.evaluate();
		
		return ranker.getEdgeRankScore(edge)+",";
		
		
	}
	
	public String generateVertexInDegree(String vertex){
		
		vertex=vertex.replace(",","").trim();
		
		return graph.inDegree(vertex)+",";
	}
	
	public String generateVertexOutDegree(String vertex){
		
		vertex=vertex.replace(",","").trim();
		
		return graph.outDegree(vertex)+",";
	}
	
	public String getGraphDiameter(){
		
		DistanceStatistics ds = new DistanceStatistics();
		
		return ds.diameter(graph)+",";
		
		
	}
	
	public String getVertexCount(){
		
		
		return graph.getVertexCount()+",";
		
		
	}
	
	public String getEdgeCount(){
		
		
		return graph.getEdgeCount()+",";
		
		
	}

}
