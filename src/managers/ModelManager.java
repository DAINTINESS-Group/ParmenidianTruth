package managers;


import java.awt.Component;
import java.io.FileNotFoundException;
import java.io.IOException;

import edu.uci.ics.jung.visualization.VisualizationViewer;
import model.DiachronicGraph;

public class ModelManager {
	
	private DiachronicGraph diachronicGraph=null; 

	
	public ModelManager(){}
	
	public void clear(){
		
		if(diachronicGraph!=null)		
			diachronicGraph.clear();
		
	}
	
	public String getTargetFolder(){
		
		return diachronicGraph.getTargetFolder();
		
	}
	
	public void stopConvergence(){
		
		diachronicGraph.stopConvergence();
		
	}
	
	public void saveVertexCoordinates(String projectIni) throws IOException{
		
		diachronicGraph.saveVertexCoordinates(projectIni);
		
	}
	
	public void setTransformingMode(){
		
		diachronicGraph.setTransformingMode();
		
	}
	
	public void setPickingMode(){
		
		diachronicGraph.setPickingMode();
		
	}
	
	
	public void visualize(VisualizationViewer< String, String> vv,String projectIni,String targetFolder,int edgeType) throws IOException {
			
		diachronicGraph.saveVertexCoordinates(projectIni);
		diachronicGraph.visualizeIndividualDBVersions(vv,targetFolder,edgeType);
		diachronicGraph.visualizeDiachronicGraph(vv);

	}
	
	public Component loadProject(String sql,String xml,String graphml, double frameX,double frameY,double scaleX,double scaleY,double centerX,double centerY,String targetFolder,int edgeType) throws Exception{
		
		diachronicGraph = new DiachronicGraph(sql,xml,graphml,targetFolder,edgeType,frameX,frameY,scaleX,scaleY,centerX,centerY);
		
		return diachronicGraph.show();
		
	}
	
	public void generateVertexDegreeReport(String targetFolder) throws FileNotFoundException{
		
		diachronicGraph.generateVertexDegreeReport(targetFolder);
		
	}
	
	public void generateVertexInDegreeReport(String targetFolder) throws FileNotFoundException{
		
		diachronicGraph.generateVertexInDegreeReport(targetFolder);
		
	}
	
	public void generateVertexOutDegreeReport(String targetFolder) throws FileNotFoundException{
		
		diachronicGraph.generateVertexOutDegreeReport(targetFolder);
		
	}
	
	public void generateVertexBetweennessReport(String targetFolder) throws FileNotFoundException{
		
		diachronicGraph.generateVertexBetweennessReport(targetFolder);
		
	}
	
	public void generateEdgeBetweennessReport(String targetFolder) throws FileNotFoundException{
		
		diachronicGraph.generateEdgeBetweennessReport(targetFolder);
		
	}
	
	public void generateGraphDiameterReport(String targetFolder) throws FileNotFoundException{
		
		diachronicGraph.generateGraphDiameterReport(targetFolder);
		
	}
	
	public void generateVertexCountReport(String targetFolder) throws FileNotFoundException{
		
		diachronicGraph.generateVertexCountReport(targetFolder);
		
	}
	
	public void generateEdgeCountReport(String targetFolder) throws FileNotFoundException{
		
		diachronicGraph.generateEdgeCountReport(targetFolder);
		
	}

	public Component refresh(double forceMult, int repulsionRange) {
		
		return diachronicGraph.refresh(forceMult,repulsionRange);
		
	}
	
	public void generateConnectedComponentsCountReport(String targetFolder) throws FileNotFoundException{
		
		diachronicGraph.generateConnectedComponentsCountReport(targetFolder);

	}
	
	public void generateClusteringCoefficientReport(String targetFolder) throws FileNotFoundException{
		
		diachronicGraph.generateClusteringCoefficientReport(targetFolder);
		
	}
	
	public void getArticulationVertices(){
		
		diachronicGraph.getArticulationVertices();
		
	}

}
