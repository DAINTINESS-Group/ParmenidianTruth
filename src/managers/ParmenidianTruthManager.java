package managers;

import java.awt.Component;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

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
	
	public void visualize(String projectIni,String targetFolder,int edgeType) throws IOException {
		
		modelManager.visualize(projectIni, targetFolder, edgeType);
	}
	
	public Component loadProject(String sql,String xml,String graphml, double frameX,double frameY,double scaleX,double scaleY,double centerX,double centerY,String targetFolder,int edgeType) throws Exception{
		
		return modelManager.loadProject( sql, xml, graphml,  frameX, frameY, scaleX, scaleY, centerX, centerY, targetFolder, edgeType);
		
	}
	
	public void createTransitions(File selectedFile) throws Exception{
		
//		HecateScript hecate= new HecateScript(selectedFile);
//		hecate.createTransitions();		
		exportManager.createTransitions(selectedFile);
		
	}
			
	public void createPowerPointPresentation(ArrayList<String> FileNames,String targetFolder,String projectName) throws FileNotFoundException, IOException{
		
		
//		PowerPointGenerator pptx=new PowerPointGenerator(targetFolder,projectName);			
//		pptx.createPresentation(FileNames);
		exportManager.createPowerPointPresentation(FileNames, targetFolder, projectName);
		
	}
	
	public void createVideo(File file) throws IOException{
		
//		VideoGenerator vg = new VideoGenerator(file);
//		vg.exportToVideo();
		exportManager.createVideo(file);
	}
	
	public void generateVertexDegreeReport(String targetFolder) throws FileNotFoundException{
		
		modelManager.generateVertexDegreeReport(targetFolder);
		
	}
	
	public void generateVertexBetweennessReport(String targetFolder) throws FileNotFoundException{
		
		modelManager.generateVertexBetweennessReport( targetFolder);
		
	}
	
	public void generateEdgeBetweennessReport(String targetFolder) throws FileNotFoundException{
		
		modelManager.generateEdgeBetweennessReport(targetFolder);
		
	}
	
	public void generateGraphDiameterReport(String targetFolder) throws FileNotFoundException{
		
		modelManager.generateGraphDiameterReport(targetFolder);
		
	}
	
	
	

}
