package managers;

import java.awt.Component;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import edu.uci.ics.jung.visualization.VisualizationViewer;
import gui.Metrics;

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

	public void calculateMetrics(String targetFolder,ArrayList<Metrics> metrics) throws FileNotFoundException {
		
		for(int i=0;i<metrics.size();i++)
			if(metrics.get(i)==Metrics.VERTEXINDEGREE)
				modelManager.generateVertexInDegreeReport(targetFolder);
			else if (metrics.get(i)==Metrics.VERTEXOUTDEGREE)
				modelManager.generateVertexOutDegreeReport(targetFolder);
			else if (metrics.get(i)==Metrics.VERTEXDEGREE)
				modelManager.generateVertexDegreeReport(targetFolder);
			else if (metrics.get(i)==Metrics.VERTEXBETWEENNESS)
				modelManager.generateVertexBetweennessReport( targetFolder);
			else if (metrics.get(i)==Metrics.EDGEBETWEENNESS)
				modelManager.generateEdgeBetweennessReport(targetFolder);
			else if (metrics.get(i)==Metrics.GRAPHDIAMETER)
				modelManager.generateGraphDiameterReport(targetFolder);
			else if (metrics.get(i)==Metrics.NUMBEROFVERTICES)
				modelManager.generateVertexCountReport(targetFolder);
			else if (metrics.get(i)==Metrics.NUMBEROFEDGES)
				modelManager.generateEdgeCountReport(targetFolder);
			else if (metrics.get(i)==Metrics.NUMBEROFCONNECTEDCOMPONENTS)
				modelManager.generateConnectedComponentsCountReport(targetFolder);
			
	}
	
	
	

}
