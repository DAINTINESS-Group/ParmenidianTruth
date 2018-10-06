package core;


import java.awt.Component;
import java.io.IOException;
import java.util.ArrayList;


import dataImport.ParserFactory;
import dataImport.IGraphmlLoader;
import dataImport.IParser;
import edu.uci.ics.jung.visualization.VisualizationViewer;

import model.DiachronicGraphFactory;
import model.IDiachronicGraph;
import model.IMetricsReport;
import model.ReportFactory;

import parmenidianEnumerations.Metric_Enums;

/**
 * Operating as a manager that communicates with {@link model}
 * @author MK
 * @version {2.0 - modified by KD-MZ-IK}
 * @modified by KD on 2018-10-05
 */

public class ModelManager {
	
	private static ModelManager instance = null;
	private IDiachronicGraph diachronicGraph=null; 
	private DiachronicGraphFactory factory = new DiachronicGraphFactory();
	
	private ParserFactory parserFactory;
	private IParser parser;

	
	protected ModelManager(){
		
		parserFactory = new ParserFactory();
		parser = parserFactory.createHecateParser();
		
	}
	
	
	public static ModelManager getInstance() {
		
		if (instance == null)
			instance = new ModelManager();
		
		return instance;
	}
	
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
	
	
	public void visualize(VisualizationViewer< String, String> visualizationViewer,String projectIni,String targetFolder,int edgeType) throws IOException {
			
		diachronicGraph.saveVertexCoordinates(projectIni);
		diachronicGraph.visualizeIndividualDBVersions(visualizationViewer,targetFolder,edgeType);
		diachronicGraph.visualizeDiachronicGraph(visualizationViewer);

	}
	
	public Component loadProject(String sql,String xml,String graphml, double frameX,double frameY,double scaleX,double scaleY,double centerX,double centerY,String targetFolder,int edgeType) throws Exception{
		
		IGraphmlLoader gl; 
		diachronicGraph=factory.createDiachronicGraph();
		
		this.setVersions(sql);
		this.setTransitions(xml);
		diachronicGraph.updateLifetimeWithTransitions();
		parser = parserFactory.createHecateParser();//added on 2018-10-05 by KD
		if (graphml != null) {
			
			parser.createGraphmlLoader(graphml);
			gl = parser.getGraphmlLoader();
			diachronicGraph.loadDiachronicGraph(gl.getNodes(),gl.getEdges(), sql, targetFolder, edgeType, frameX, frameY, scaleX, scaleY, centerX, centerY);
		}else
			diachronicGraph.createDiachronicGraph(sql, targetFolder, edgeType, frameX, frameY, scaleX, scaleY, centerX, centerY);
		
		
		return diachronicGraph.show();
		
	}

	public Component refresh(double forceMult, int repulsionRange) {
		
		return diachronicGraph.refresh(forceMult,repulsionRange);
		
	}

//created by KD on 13/04/17
	public void generateMetricsReport(String targetFolder, ArrayList<Metric_Enums> metrics){
		
		ReportFactory reportFactory = new ReportFactory();
		ArrayList<IMetricsReport> reportEngine = new ArrayList<IMetricsReport>();
		
		for(int i=0;i<metrics.size();i++){

			reportEngine.add(reportFactory.getMetricsReportEngine(targetFolder, metrics.get(i),diachronicGraph));
			reportEngine.get(i).generateMetricsReport();
		
				
		}
		
		reportEngine.clear();
		
	}

	
	
	/**
	 * @author KD
	 * @param sqlFiles pathname of sql files.
	 * @since 2018-02-14
	 */

	private void setVersions(String sqlFiles) {


		diachronicGraph.setVersions(parser.getLifetime(sqlFiles));


	}

		
		
	/**
	 * @author KD
	 * @param pathname of file containing transitions.
	 * @since 2018-02-14
	 */
			
	private void setTransitions(String xmlFile) {


		diachronicGraph.setTransitions(parser.getTransitions(xmlFile));


	}

	
}
