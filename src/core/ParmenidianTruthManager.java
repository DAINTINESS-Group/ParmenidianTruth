package core;

import java.awt.Component;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import parmenidianEnumerations.Metric_Enums;
import edu.uci.ics.jung.visualization.VisualizationViewer;

public class ParmenidianTruthManager {
	
	private ModelManager modelManager;
	private ExportManager exportManager;
	
	public ParmenidianTruthManager(){
		
		
		modelManager = new ModelManager();
		exportManager = new ExportManager();
		
	}
	
	public void clear(){
		
		modelManager.clear();
	}
	
	public String getTargetFolder(){
		
		return modelManager.getTargetFolder();
		
	}
	
	public void stopConvergence(){
		
		modelManager.stopConvergence();
		
	}
	
	public void saveVertexCoordinates(String projectIni) throws IOException{
		
		modelManager.saveVertexCoordinates(projectIni);
		
	}
	
	public void setTransformingMode(){
		
		modelManager.setTransformingMode();
		
	}
	
	public void setPickingMode(){
		
		modelManager.setPickingMode();
		
	}
	
	public void visualize(VisualizationViewer< String, String> vv,String projectIni,String targetFolder,int edgeType) throws IOException {
		
		modelManager.visualize(vv,projectIni, targetFolder, edgeType);
	}
	
	public Component loadProject(String sql,String xml,String graphml, double frameX,double frameY,double scaleX,double scaleY,double centerX,double centerY,String targetFolder,int edgeType) throws Exception{
		
		return modelManager.loadProject( sql, xml, graphml,  frameX, frameY, scaleX, scaleY, centerX, centerY, targetFolder, edgeType);
		
	}
	
	public void createTransitions(File selectedFile) throws Exception{
		
		exportManager.createTransitions(selectedFile);
		
	}
			
	public void createPowerPointPresentation(ArrayList<String> FileNames,String targetFolder,String projectName) throws FileNotFoundException, IOException{
		
		exportManager.createPowerPointPresentation(FileNames, targetFolder, projectName);
		
	}
	
	public void createVideo(File file) throws IOException{
		
		exportManager.createVideo(file);
	}
	


	public Component refresh(double forceMult, int repulsionRange) {
		
		return modelManager.refresh(forceMult,repulsionRange);
	}

	public void calculateMetrics(String targetFolder,ArrayList<Metric_Enums> metrics) throws FileNotFoundException {
		
		
		for(int i=0;i<metrics.size();i++)
			if(metrics.get(i)==Metric_Enums.VERTEX_IN_DEGREE)
				modelManager.generateVertexInDegreeReport(targetFolder);
			else if (metrics.get(i)==Metric_Enums.VERTEX_OUT_DEGREE)
				modelManager.generateVertexOutDegreeReport(targetFolder);
			else if (metrics.get(i)==Metric_Enums.VERTEX_DEGREE)
				modelManager.generateVertexDegreeReport(targetFolder);
			else if (metrics.get(i)==Metric_Enums.VERTEX_BETWEENNESS)
				modelManager.generateVertexBetweennessReport( targetFolder);
			else if (metrics.get(i)==Metric_Enums.CLUSTERING_COEFFICIENT)
				modelManager.generateClusteringCoefficientReport( targetFolder);
			else if (metrics.get(i)==Metric_Enums.EDGE_BETWEENNESS)
				modelManager.generateEdgeBetweennessReport(targetFolder);
			else if (metrics.get(i)==Metric_Enums.GRAPH_DIAMETER)
				modelManager.generateGraphDiameterReport(targetFolder);
			else if (metrics.get(i)==Metric_Enums.NUMBER_OF_VERTICES)
				modelManager.generateVertexCountReport(targetFolder);
			else if (metrics.get(i)==Metric_Enums.NUMBER_OF_EDGES)
				modelManager.generateEdgeCountReport(targetFolder);
			else if (metrics.get(i)==Metric_Enums.NUMBER_OF_CONNECTED_COMPONENTS)
				modelManager.generateConnectedComponentsCountReport(targetFolder);
			else if(metrics.get(i)==Metric_Enums.NUMBER_OF_VERTICES_IN_GCC)
				modelManager.generateVertexCountReportForGCC(targetFolder);
			else if(metrics.get(i)==Metric_Enums.NUMBER_OF_EDGES_IN_GCC)
				modelManager.generateEdgeCountReportForGCC(targetFolder);
		
//		modelManager.getArticulationVertices();
		
		
			
	}
	
	
	

}
