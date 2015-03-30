package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings({ "rawtypes", "unused" })
public class DiachronicGraph extends Episode {

	private ArrayList<Episode> versions = new ArrayList<Episode>();
	private static ConcurrentHashMap<String, Table> graph = new ConcurrentHashMap<String, Table>();
	private ConcurrentHashMap<String, ForeignKey> graphEdges = new ConcurrentHashMap<String, ForeignKey>();
	
	private ArrayList<Table>  vertices= new ArrayList<Table>();
	private ArrayList<ForeignKey> edges= new ArrayList<ForeignKey>();

	
	
	public DiachronicGraph(ArrayList<Episode> vrs) {
		
		versions = vrs;
		createDiachronicGraph();
		showDiachronicGraph();
		
	}
	
	public DiachronicGraph(GraphmlLoader gml){
		
		vertices=gml.getNodes();
		edges=gml.getEdges();
		fixGraph();
		
	}

	//se periptwsh pou o UniversalGraph kataskeuazetai apto graphml
	//ektos apo tis listes exw enhmerwmeno kai to graph gia thn grhgorh
	//prospelash twn komvwn
	private void fixGraph() {
		
		for(int i=0;i<vertices.size();++i){
			graph.put(vertices.get(i).getKey(),vertices.get(i));
		}		
	}

	private void createDiachronicGraph() {
		
		for(int i=0;i<versions.size();++i){
			for(Table mt : versions.get(i).getTables()) graph.putIfAbsent(mt.getKey(), mt);//union of tables
			for(ForeignKey fk : versions.get(i).getVersionForeignKeys()) graphEdges.putIfAbsent(fk.getKey(), fk);//union of FK
		}
		
		transformNodes();
		transformEdges();
		
	}

	private void transformEdges() {

		//metatroph tou hashmap se ArrayList 
		Iterator i=graphEdges.entrySet().iterator();
		
		while(i.hasNext()){
			
			  Map.Entry entry = (Map.Entry) i.next();

			  String key = (String)entry.getKey();

			  ForeignKey value = (ForeignKey)entry.getValue();
			  
			  edges.add(value);
		}
		
		
	}

	private void transformNodes() {
		
		
		//metatroph tou hashmap se ArrayList 
		Iterator i=graph.entrySet().iterator();		
		while(i.hasNext()){
			
			  Map.Entry entry = (Map.Entry) i.next();

			  String key = (String)entry.getKey();

			  Table value = (Table)entry.getValue();
			  
			  vertices.add(value);

		}
		
		
		
	}

	public void showDiachronicGraph(){
		
		//info about tables
			
			Iterator i=graph.entrySet().iterator();
			
			while(i.hasNext()){
				
				  Map.Entry entry = (Map.Entry) i.next();

				  String key = (String)entry.getKey();

				  Table value = (Table)entry.getValue();

			}
			
			
		//info about FK
//			System.out.println(graphEdges.size());
			
			i=graphEdges.entrySet().iterator();
			
			while(i.hasNext()){
				
				  Map.Entry entry = (Map.Entry) i.next();

				  String key = (String)entry.getKey();

				  ForeignKey value = (ForeignKey)entry.getValue();


			}
			
			
	}
	

	@Override
	public ArrayList<Table> getNodes() {

		return vertices;
		
	}

	@Override
	public ArrayList<ForeignKey> getEdges() {
		
		return edges;

	}

	public ConcurrentHashMap<String, Table> getGraph() {
		return graph;
	}

	@Override
	public String getVersion() {
		
		return "Universal Graph";
		
	}
	
	public void clear(){
		
		versions.clear();
		graph.clear();
		graphEdges.clear();
		vertices.clear();
		edges.clear();
		
	}

	@Override
	public ArrayList<Table> getTables() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<ForeignKey> getVersionForeignKeys() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
