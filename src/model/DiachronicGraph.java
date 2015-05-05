package model;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import loader.GraphmlLoader;
import loader.Parser;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;

@SuppressWarnings({ "rawtypes", "unused" })
public class DiachronicGraph {

	private ArrayList<DBVersion> versions = new ArrayList<DBVersion>();
	private ArrayList<Map<String,Integer>> transitions = new ArrayList<Map<String,Integer>>();//auxiliary class for creating DiachronicGraph
	
	private static ConcurrentHashMap<String, Table> graph = new ConcurrentHashMap<String, Table>();//union of tables
	private ConcurrentHashMap<String, ForeignKey> graphEdges = new ConcurrentHashMap<String, ForeignKey>();//union of edges
	
	private ArrayList<Table>  vertices= new ArrayList<Table>();//union of tables
	private ArrayList<ForeignKey> edges= new ArrayList<ForeignKey>();//union of edges

	private DiachronicGraphVisualRepresentation visualizationOfDiachronicGraph;
	private GraphMetrics graphMetricsOfDiachronicGraph;
	
	public DiachronicGraph(String sql,String xml,String graphml, String targetFolder, int et,double frameX,double frameY,double scaleX,double scaleY,double centerX,double centerY) throws Exception {
		
		Parser myParser;
		GraphmlLoader savedChanges;

		myParser = new Parser(sql,xml,graphml);
		versions=myParser.getLifetime();
		transitions=myParser.getTransitions();
		updateLifetimeWithTransitions();
		
		int mode;
		if(myParser.hasGraphml()){
			savedChanges=myParser.getGraphmlLoader();
			
			vertices=savedChanges.getNodes();
			edges=savedChanges.getEdges();
			fixGraph();	
			mode=1;
			graphMetricsOfDiachronicGraph = new GraphMetrics(vertices,edges);
			visualizationOfDiachronicGraph = new DiachronicGraphVisualRepresentation(this,vertices,edges,sql,targetFolder,et,mode,frameX,frameY,scaleX,scaleY,centerX,centerY);
		}else{
			
			createDiachronicGraph();
			mode=0;
			graphMetricsOfDiachronicGraph = new GraphMetrics(vertices,edges);
			visualizationOfDiachronicGraph = new DiachronicGraphVisualRepresentation(this,vertices,edges,sql,targetFolder,et,mode,frameX,frameY,scaleX,scaleY,centerX,centerY);
		}
		
		
		
		
	}
	
	private void updateLifetimeWithTransitions(){
		
		for(int i=0;i<versions.size();++i)
			if(i==0)
				setFirstVersion(versions.get(i));
			else if(i==versions.size()-1)
				setFinalVersion(versions.get(i),i);
			else
				setIntermediateVersion(versions.get(i),i);
	}
	
	
	/**
	 * Trexw thn prwth version me to prwto Dictionary kai checkarw n dw an sthn
	 * 2h version exei svistei kapoios pinakas.Me endiaferei mono to deletion
	 * An kapoioi exoun ginei updated tha tous vapsw sthn 2h ekdosh,oxi edw
	 * @param fversion :firstVersion
	 */
	private void setFirstVersion(DBVersion fversion){
		
		for(int i=0;i<fversion.getTables().size();++i)
			if(transitions.get(0).containsKey(fversion.getTables().get(i).getKey())
			&& transitions.get(0).get(fversion.getTables().get(i).getKey())==Status.DELETION.getValue())
				fversion.getTables().get(i).setTableStatus(Status.DELETION.getValue());		
		
	}
	
	/**
	 * Trexw thn teleutaia version mou me to teleutaio dictionary mou,h thesh tou
	 * teleutaiou dictionary mou einai mia prin apo thn thesh ths teleutaias version mou.
	 * Psaxnw gia tables pou periexontai st dictionary mou KAI DEN einai deletions,einai 
	 * dhladh mono newTable kai UpdateTable kai tous vafw analoga me thn timh pou exei to
	 * dictionary mou.
	 * @param fversion :finalVersion
	 * @param k :H thesh ths teleutaias Version mou sthn Lista
	 */
	private void setFinalVersion(DBVersion fversion,int k){
		
		for(int i=0;i<fversion.getTables().size();++i)
			if(transitions.get(k-1).containsKey(fversion.getTables().get(i).getKey())
			&& transitions.get(k-1).get(fversion.getTables().get(i).getKey())!=Status.DELETION.getValue())
				fversion.getTables().get(i).setTableStatus(transitions.get(k-1).get(fversion.getTables().get(i).getKey()));
		
	}
	
	private void setIntermediateVersion(DBVersion version,int k){
		
		for(int i=0;i<version.getTables().size();++i){
			//koitaw to mellontiko m dictionary
			if(transitions.get(k).containsKey(version.getTables().get(i).getKey())
			&& transitions.get(k).get(version.getTables().get(i).getKey())==Status.DELETION.getValue())
				version.getTables().get(i).setTableStatus(Status.DELETION.getValue());
			
			//koitaw to palho m dictionary
			if(transitions.get(k-1).containsKey(version.getTables().get(i).getKey())
			&& transitions.get(k-1).get(version.getTables().get(i).getKey())!=Status.DELETION.getValue())
				version.getTables().get(i).setTableStatus(transitions.get(k-1).get(version.getTables().get(i).getKey()));
		
				
		}
	}
	
//	public DiachronicGraph(ArrayList<DBVersion> vrs,String sql, String targetFolder, int et,int mode,double frameX,double frameY,double scaleX,double scaleY,double centerX,double centerY) {
//		
//		versions = vrs;
//		createDiachronicGraph();
//		
//		visualizationOfDiachronicGraph = new DiachronicGraphVisualRepresentation(vertices,edges,sql,targetFolder,et,mode,frameX,frameY,scaleX,scaleY,centerX,centerY);
//		
//	}
//	
//	public DiachronicGraph(GraphmlLoader gml,String sql, String targetFolder, int et,int mode,double frameX,double frameY,double scaleX,double scaleY,double centerX,double centerY){
//		
//		vertices=gml.getNodes();
//		edges=gml.getEdges();
//		fixGraph();
//		
//		visualizationOfDiachronicGraph = new DiachronicGraphVisualRepresentation(vertices,edges,sql,targetFolder,et,mode,frameX,frameY,scaleX,scaleY,centerX,centerY);
//		
//	}

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
			
			for(Table mt : versions.get(i).getTables())
				graph.putIfAbsent(mt.getKey(), mt);//union of tables
			
			for(ForeignKey fk : versions.get(i).getVersionForeignKeys())
				graphEdges.putIfAbsent(fk.getKey(), fk);//union of FK
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

	public ArrayList<Table> getNodes() {

		return vertices;
		
	}

	public ArrayList<ForeignKey> getEdges() {
		
		return edges;

	}

	public ConcurrentHashMap<String, Table> getDictionaryOfGraph() {
		return graph;
	}

	public String getVersion() {
		
		return "Universal Graph";
		
	}
	
	public void clear(){
		
		versions.clear();
		graph.clear();
		graphEdges.clear();
		vertices.clear();
		edges.clear();
		visualizationOfDiachronicGraph=null;
		
	}
	
	public void setPickingMode(){
		
		visualizationOfDiachronicGraph.setPickingMode();
		
	}
	
	public void setTransformingMode(){
		
		visualizationOfDiachronicGraph.setTransformingMode();
		
	}
	
	public void saveVertexCoordinates(String projectIni) throws IOException{

		visualizationOfDiachronicGraph.saveVertexCoordinates(projectIni);
		
	}
	
	public void stopConvergence(){
		
		visualizationOfDiachronicGraph.stop();
	}

	public String getTargetFolder(){
		
		return visualizationOfDiachronicGraph.getTargetFolder();
	}
	
	public void visualizeDiachronicGraph(){
		
		visualizationOfDiachronicGraph.createEpisode();
		
	}
	
	public void visualizeIndividualDBVersions(String targetFolder,int edgeType){
		
		int width = visualizationOfDiachronicGraph.getWidthOfVisualizationViewer();
		int height = visualizationOfDiachronicGraph.getHeightOfVisualizationViewer();
		
		for(int i=0;i<versions.size();++i){
//			DBVersionVisualRepresentation episode = new DBVersionVisualRepresentation(versions.get(i),targetFolder,edgeType);
//			episode.createEpisodes(this);
//			episode=null;
			versions.get(i).setDetails(targetFolder, edgeType,width,height);
			versions.get(i).visualizeEpisode(this);
		}
		
	}
		
	public VisualizationViewer show(){
		
		return visualizationOfDiachronicGraph.show();
		
	}
	
	
	public Dimension getUniversalFrame(){
		
		return visualizationOfDiachronicGraph.getUniversalFrame();
	}
	
	public Point2D getUniversalCenter(){
		
		return visualizationOfDiachronicGraph.getUniversalCenter();

	}
	
	public double getScaleX(){
		
		return visualizationOfDiachronicGraph.getScaleX();
	}
	
	public double getScaleY(){
		
		return visualizationOfDiachronicGraph.getScaleY();
	}
	
	public  double getFrameX(){
		
		return visualizationOfDiachronicGraph.getFrameX();
	}
	
	public  double getFrameY(){
		
		return visualizationOfDiachronicGraph.getFrameY();
	}
	
	public Graph getGraph(){
		
		return graphMetricsOfDiachronicGraph.getGraph();
		
	}
	
	
}
