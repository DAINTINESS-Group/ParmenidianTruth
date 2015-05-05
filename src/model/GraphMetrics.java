package model;

import java.util.ArrayList;

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
			graph.addEdge(Integer.toString(i), edges.get(i)
					.getSourceTable(), edges.get(i)
					.getTargetTable());

	}

}
