package core;

import java.awt.Component;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import dataImport.HecateImportManagerFactory;
import dataImport.IHecateImportManager;
import parmenidianEnumerations.Metric_Enums;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import export.ExportManagerFactory;
import export.IExportManager;

/**
 * 
 * Handles all use cases of PT tool.
 * @author MK
 * @version 1.0
 *
 */

public class ParmenidianTruthManager implements IParmenidianTruth{
	
	
	private ModelManager modelManager;

	
	private IExportManager exportManager;
	private ExportManagerFactory exManagerFactory;
	
	private IHecateImportManager importManager;
	private HecateImportManagerFactory imManagerFactory;
	

	public ParmenidianTruthManager(){
		
		imManagerFactory = new HecateImportManagerFactory();
		exManagerFactory = new ExportManagerFactory();
	
		
		modelManager = ModelManager.getInstance();
		importManager = imManagerFactory.createHecateManager();
		exportManager = exManagerFactory.createExportManager();

		
	}
	
	public void clear(){
		
		modelManager.clear();
	}

	public Component refresh(double forceMult, int repulsionRange) {
		
		return modelManager.refresh(forceMult,repulsionRange);
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
	
	public void visualize(VisualizationViewer< String, String> visualizationViewer,String projectIni,String targetFolder,int edgeType) throws IOException {
		
		modelManager.visualize(visualizationViewer,projectIni, targetFolder, edgeType);
	}
	
	public Component loadProject(String sql,String xml,String graphml, double frameX,double frameY,double scaleX,double scaleY,double centerX,double centerY,String targetFolder,int edgeType) throws Exception{
		

		return modelManager.loadProject( sql, xml, graphml,  frameX, frameY, scaleX, scaleY, centerX, centerY, targetFolder, edgeType);
		
	}
	
	public void createTransitions(File fileSelected) throws Exception{
		
		importManager.createTransitions(fileSelected);
		
	}
			
	public void createPowerPointPresentation(ArrayList<String> fileNames,String targetFolder,String projectName) throws FileNotFoundException, IOException{
		
		exportManager.createPowerPointPresentation(fileNames, targetFolder, projectName);
		
	}
	
	public void createVideo(File file) throws IOException{
		
		exportManager.createVideo(file);
	}
	

//created by KD on 13/04/17
	public void generateMetricsReport(String targetFolder, ArrayList<Metric_Enums> metrics){

		try{

			modelManager.generateMetricsReport(targetFolder, metrics);

		}catch(Exception e){

			System.out.println(e.getClass());

		}

	}
	
}