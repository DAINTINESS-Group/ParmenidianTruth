package model;

import java.awt.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import edu.uci.ics.jung.visualization.VisualizationViewer;


/**
 * 
 * Providing functionalities related to the model of DB schema.
 * @author KD
 * @since 2018-02-19
 *
 */

public interface IDiachronicGraph {
	
	public void clear();
	
	public void setPickingMode();
	
	public void setTransformingMode();
	
	public void saveVertexCoordinates(String projectIni) throws IOException;
	
	public void stopConvergence();
	
	public String getTargetFolder();
	
	public void visualizeDiachronicGraph(VisualizationViewer< String, String> vv);
	
	public void visualizeIndividualDBVersions(VisualizationViewer< String, String> vv,String targetFolder,int edgeType);
	
	@SuppressWarnings("rawtypes")
	public VisualizationViewer show();
	
	public Component refresh(double forceMult, int repulsionRange);
	
	public ArrayList<Table> getNodes();
	
	public ArrayList<ForeignKey> getEdges();
	
	public ArrayList<DBVersion> getVersions();
	
	public IGraphMetrics getGraphMetrics();

	public void setVersions(ArrayList<DBVersion> vrs);

	public void setTransitions(ArrayList<Map<String, Integer>> trs);

	public void updateLifetimeWithTransitions();

	public void loadDiachronicGraph(ArrayList<Table> v,ArrayList<ForeignKey> e, String in, String tf, int et, double frameX,
			double frameY, double scaleX, double scaleY, double centerX, double centerY);

	public void createDiachronicGraph(String in, String tf, int et, double frameX,
			double frameY, double scaleX, double scaleY, double centerX, double centerY);
}
