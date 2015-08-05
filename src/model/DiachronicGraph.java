package model;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import loader.GraphmlLoader;
import loader.Parser;
import edu.uci.ics.jung.algorithms.scoring.BetweennessCentrality;
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
	
//	public void getArticulationVertices(){
//		
//		System.out.print("Diachronic graph: ");
//		graphMetricsOfDiachronicGraph.getArticulationVertices();
//		
////		for(int i=0;i<versions.size();++i){
////			System.out.print(versions.get(i).getVersion()+":");
////		}
//		
//	}
	
	
	public void generateClusteringCoefficientReport(String targetFolder) throws FileNotFoundException{
		
		File reportFile = new File(targetFolder+"\\Report of clustering coefficient.csv");
		
		Map<String,Double> collection=null;
		
		
		PrintWriter writer = new PrintWriter(reportFile);
		
		int lines = vertices.size()+1;
		int columns =versions.size()+2;
		
		String[][] report= new String[lines][columns];
		
//		create 1st line
		report[0][0]=" ,";
		report[0][1]="Diachronic Graph,";		
		for(int i=0;i<versions.size();i++)			
			report[0][i+2]=versions.get(i).getVersion()+",";
		
//		create 1st column		
		for(int i=0;i<vertices.size();i++)
			report[i+1][0]=vertices.get(i).getKey()+",";		
		
//		fill in the rest
		for(int i=1;i<columns;i++){
			for(int j=1;j<lines;j++)
				if(i==1){
					if(collection==null)
						collection = graphMetricsOfDiachronicGraph.getClusteringCoefficient();
					
					String candidate =report[j][0].replace(",", "");
					String clusteringCoefficientScore=String.valueOf(collection.get(candidate));
					if(clusteringCoefficientScore.equals("null"))
						clusteringCoefficientScore="*";
					report[j][i] =  clusteringCoefficientScore +",";
					
				}else{
					if(collection==null)
						collection = versions.get(i-2).getClusteringCoefficient();
					
					String candidate =report[j][0].replace(",", "");
					String clusteringCoefficientScore=String.valueOf(collection.get(candidate));
					if(clusteringCoefficientScore.equals("null"))
						clusteringCoefficientScore="*";
					report[j][i] =  clusteringCoefficientScore +",";
				}
			collection.clear();
			collection=null;
		}
		
		
//		print array into file
		for(int i=0;i<lines;i++){
			for(int j=0;j<columns;j++)
				writer.print(report[i][j]);
			writer.print("\n");
		}
				
		writer.close();

		
	}
	
	public void generateConnectedComponentsCountReport(String targetFolder) throws FileNotFoundException{
		
		File reportFile = new File(targetFolder+"\\Report of graph's connected-components.csv");
		
		
		PrintWriter writer = new PrintWriter(reportFile);
		
		int lines = 2;
		int columns =versions.size()+2;
		
		String[][] report= new String[lines][columns];
		
//		create 1st line
		report[0][0]=" ,";
		report[0][1]="Diachronic Graph,";		
		for(int i=0;i<versions.size();i++)			
			report[0][i+2]=versions.get(i).getVersion()+",";
		
//		create 1st column		
		report[1][0]="# of connected-components ,";
		
//		fill in the rest
		for(int i=1;i<columns;i++)
				if(i==1)
					report[1][i] = graphMetricsOfDiachronicGraph.getNumberOfConnectedComponents();
				else
					report[1][i]=versions.get(i-2).generateConnectedComponentsCountReport();
		
		
//		print array into file
		for(int i=0;i<lines;i++){
			for(int j=0;j<columns;j++)
				writer.print(report[i][j]);
			writer.print("\n");
		}
				
		writer.close();
		
		
	}
	
	public void generateEdgeCountReport(String targetFolder) throws FileNotFoundException{
		
		File vertexReport = new File(targetFolder+"\\Report of graph edgeCount.csv");
		
		
		PrintWriter writer = new PrintWriter(vertexReport);
		
		int lines = 2;
		int columns =versions.size()+2;
		
		String[][] report= new String[lines][columns];
		
//		create 1st line
		report[0][0]=" ,";
		report[0][1]="Diachronic Graph,";		
		for(int i=0;i<versions.size();i++)			
			report[0][i+2]=versions.get(i).getVersion()+",";
		
//		create 1st column		
		report[1][0]="# of Edges ,";
		
//		fill in the rest
		for(int i=1;i<columns;i++)
				if(i==1)
					report[1][i] = graphMetricsOfDiachronicGraph.getEdgeCount();
				else
					report[1][i]=versions.get(i-2).getEdgeCount();
		
		
//		print array into file
		for(int i=0;i<lines;i++){
			for(int j=0;j<columns;j++)
				writer.print(report[i][j]);
			writer.print("\n");
		}
				
		writer.close();
		
		
	}
	
	public void generateVertexCountReport(String targetFolder) throws FileNotFoundException{
		
		File vertexReport = new File(targetFolder+"\\Report of graph vertexCount.csv");
		
		
		PrintWriter writer = new PrintWriter(vertexReport);
		
		int lines = 2;
		int columns =versions.size()+2;
		
		String[][] report= new String[lines][columns];
		
//		create 1st line
		report[0][0]=" ,";
		report[0][1]="Diachronic Graph,";		
		for(int i=0;i<versions.size();i++)			
			report[0][i+2]=versions.get(i).getVersion()+",";
		
//		create 1st column		
		report[1][0]="# of Vertices,";
		
//		fill in the rest
		for(int i=1;i<columns;i++)
				if(i==1)
					report[1][i] = graphMetricsOfDiachronicGraph.getVertexCount();
				else
					report[1][i]=versions.get(i-2).getVertexCount();
		
		
//		print array into file
		for(int i=0;i<lines;i++){
			for(int j=0;j<columns;j++)
				writer.print(report[i][j]);
			writer.print("\n");
		}
				
		writer.close();
		
		
	}
	
	public void generateVertexOutDegreeReport(String targetFolder) throws FileNotFoundException {
		
		File vertexReport = new File(targetFolder+"\\Report of vertex outDegree.csv");
		
		//assert vertexReport.exists(); 
		
		PrintWriter writer = new PrintWriter(vertexReport);
		
		int lines = vertices.size()+1;
		int columns =versions.size()+2;
		
		String[][] report= new String[lines][columns];
		
//		create 1st line
		report[0][0]=" ,";
		report[0][1]="Diachronic Graph,";		
		for(int i=0;i<versions.size();i++)			
			report[0][i+2]=versions.get(i).getVersion()+",";
		
//		create 1st column		
		for(int i=0;i<vertices.size();i++)
			report[i+1][0]=vertices.get(i).getKey()+",";		
		
//		fill in the rest
		for(int i=1;i<columns;i++)
			for(int j=1;j<lines;j++)
				if(i==1)
					report[j][i] = graphMetricsOfDiachronicGraph.generateVertexOutDegree(report[j][0]);
				else
					report[j][i]=versions.get(i-2).generateVertexOutDegree(report[j][0]);
		
		
//		print array into file
		for(int i=0;i<lines;i++){
			for(int j=0;j<columns;j++)
				writer.print(report[i][j]);
			writer.print("\n");
		}
				
		writer.close();

		
	}
	
	public void generateVertexInDegreeReport(String targetFolder) throws FileNotFoundException {
		
		File vertexReport = new File(targetFolder+"\\Report of vertex inDegree.csv");
		
		//assert vertexReport.exists(); 
		
		PrintWriter writer = new PrintWriter(vertexReport);
		
		int lines = vertices.size()+1;
		int columns =versions.size()+2;
		
		String[][] report= new String[lines][columns];
		
//		create 1st line
		report[0][0]=" ,";
		report[0][1]="Diachronic Graph,";		
		for(int i=0;i<versions.size();i++)			
			report[0][i+2]=versions.get(i).getVersion()+",";
		
//		create 1st column		
		for(int i=0;i<vertices.size();i++)
			report[i+1][0]=vertices.get(i).getKey()+",";		
		
//		fill in the rest
		for(int i=1;i<columns;i++)
			for(int j=1;j<lines;j++)
				if(i==1)
					report[j][i] = graphMetricsOfDiachronicGraph.generateVertexInDegree(report[j][0]);
				else
					report[j][i]=versions.get(i-2).generateVertexInDegree(report[j][0]);
		
		
//		print array into file
		for(int i=0;i<lines;i++){
			for(int j=0;j<columns;j++)
				writer.print(report[i][j]);
			writer.print("\n");
		}
				
		writer.close();

		
	}
	
	
	public void generateGraphDiameterReport(String targetFolder) throws FileNotFoundException{
		
		File vertexReport = new File(targetFolder+"\\Report of graph diameter.csv");
		
		
		PrintWriter writer = new PrintWriter(vertexReport);
		
		int lines = 2;
		int columns =versions.size()+2;
		
		String[][] report= new String[lines][columns];
		
//		create 1st line
		report[0][0]=" ,";
		report[0][1]="Diachronic Graph,";		
		for(int i=0;i<versions.size();i++)			
			report[0][i+2]=versions.get(i).getVersion()+",";
		
//		create 1st column		
		report[1][0]="Graph Diameter,";
		
//		fill in the rest
		for(int i=1;i<columns;i++)
				if(i==1)
					report[1][i] = graphMetricsOfDiachronicGraph.getGraphDiameter();
				else
					report[1][i]=versions.get(i-2).getGraphDiameter();
		
		
//		print array into file
		for(int i=0;i<lines;i++){
			for(int j=0;j<columns;j++)
				writer.print(report[i][j]);
			writer.print("\n");
		}
				
		writer.close();
		
		
	}
	
	
	public void generateEdgeBetweennessReport(String targetFolder) throws FileNotFoundException{
		
		File vertexReport = new File(targetFolder+"\\Report of edge betweenness.csv");
		
		
		PrintWriter writer = new PrintWriter(vertexReport);
		
		int lines = edges.size()+1;
		int columns =versions.size()+2;
		
		String[][] report= new String[lines][columns];
		
//		create 1st line
		report[0][0]=" ,";
		report[0][1]="Diachronic Graph,";		
		for(int i=0;i<versions.size();i++)			
			report[0][i+2]=versions.get(i).getVersion()+",";
		
//		create 1st column		
		for(int i=0;i<edges.size();i++)
			report[i+1][0]=edges.get(i).getSourceTable()+"|"+edges.get(i).getTargetTable()+",";		
		
//		fill in the rest
		for(int i=1;i<columns;i++)
			for(int j=1;j<lines;j++)
				if(i==1)
					report[j][i] = graphMetricsOfDiachronicGraph.generateEdgeBetweenness(report[j][0]);
				else
					report[j][i]=versions.get(i-2).generateEdgeBetweenness(report[j][0]);
		
		
//		print array into file
		for(int i=0;i<lines;i++){
			for(int j=0;j<columns;j++)
				writer.print(report[i][j]);
			writer.print("\n");
		}
				
		writer.close();
		
		
	}
	
	public void generateVertexBetweennessReport(String targetFolder) throws FileNotFoundException{
		
		File vertexReport = new File(targetFolder+"\\Report of vertex betweenness.csv");
		
		
		PrintWriter writer = new PrintWriter(vertexReport);
		
		int lines = vertices.size()+1;
		int columns =versions.size()+2;
		
		String[][] report= new String[lines][columns];
		
//		create 1st line
		report[0][0]=" ,";
		report[0][1]="Diachronic Graph,";		
		for(int i=0;i<versions.size();i++)			
			report[0][i+2]=versions.get(i).getVersion()+",";
		
//		create 1st column		
		for(int i=0;i<vertices.size();i++)
			report[i+1][0]=vertices.get(i).getKey()+",";		
		
//		fill in the rest
		for(int i=1;i<columns;i++)
			for(int j=1;j<lines;j++)
				if(i==1)
					report[j][i] = graphMetricsOfDiachronicGraph.generateVertexBetweenness(report[j][0]);
				else
					report[j][i]=versions.get(i-2).generateVertexBetweenness(report[j][0]);
		
		
//		print array into file
		for(int i=0;i<lines;i++){
			for(int j=0;j<columns;j++)
				writer.print(report[i][j]);
			writer.print("\n");
		}
				
		writer.close();
		
		
	}
	
	
	
	public void generateVertexDegreeReport(String targetFolder) throws FileNotFoundException {
		
		File vertexReport = new File(targetFolder+"\\Report of vertex degree.csv");
		
		PrintWriter writer = new PrintWriter(vertexReport);
		
		int lines = vertices.size()+1;
		int columns =versions.size()+2;
		
		String[][] report= new String[lines][columns];
		
//		create 1st line
		report[0][0]=" ,";
		report[0][1]="Diachronic Graph,";		
		for(int i=0;i<versions.size();i++)			
			report[0][i+2]=versions.get(i).getVersion()+",";
		
//		create 1st column		
		for(int i=0;i<vertices.size();i++)
			report[i+1][0]=vertices.get(i).getKey()+",";		
		
//		fill in the rest
		for(int i=1;i<columns;i++)
			for(int j=1;j<lines;j++)
				if(i==1)
					report[j][i] = graphMetricsOfDiachronicGraph.generateVertexDegree(report[j][0]);
				else
					report[j][i]=versions.get(i-2).generateVertexDegree(report[j][0]);
		
		
//		print array into file
		for(int i=0;i<lines;i++){
			for(int j=0;j<columns;j++)
				writer.print(report[i][j]);
			writer.print("\n");
		}
				
		writer.close();

		
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
	
	public void visualizeDiachronicGraph(VisualizationViewer< String, String> vv){
		
		visualizationOfDiachronicGraph.createEpisode(vv);
		
	}
	
	public void visualizeIndividualDBVersions(VisualizationViewer< String, String> vv,String targetFolder,int edgeType){
		
		int width = visualizationOfDiachronicGraph.getWidthOfVisualizationViewer();
		int height = visualizationOfDiachronicGraph.getHeightOfVisualizationViewer();
		
		for(int i=0;i<versions.size();++i){
			versions.get(i).setDetails(targetFolder, edgeType,width,height);
			versions.get(i).visualizeEpisode(vv,this);
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

	public Component refresh(double forceMult, int repulsionRange) {
		
		return this.visualizationOfDiachronicGraph.refresh(forceMult,repulsionRange);
	}
	
	public Rectangle getUniversalBounds(){
		
		
		return visualizationOfDiachronicGraph.getUniversalBounds();
		

	}
	
	
}
