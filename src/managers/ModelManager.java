package managers;


import java.awt.Component;
import java.io.FileNotFoundException;
import java.io.IOException;

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
	
	
	public void visualize(String projectIni,String targetFolder,int edgeType) throws IOException {
		

			
		diachronicGraph.saveVertexCoordinates(projectIni);
		diachronicGraph.visualizeDiachronicGraph();
		diachronicGraph.visualizeIndividualDBVersions(targetFolder,edgeType);
		
		
//		for(int i=0;i<lifetime.size();++i){
//			DBVersionVisualRepresentation episode = new DBVersionVisualRepresentation(lifetime.get(i),targetFolder,edgeType);
//			episode.createEpisodes(diachronicGraph);
//			episode=null;
//		}

	}
	
	
	public Component loadProject(String sql,String xml,String graphml, double frameX,double frameY,double scaleX,double scaleY,double centerX,double centerY,String targetFolder,int edgeType) throws Exception{
		
//		Parser myParser;
//		GraphmlLoader savedChanges;
//
//		myParser = new Parser(sql,xml,graphml);
//		lifetime=myParser.getLifetime();
//		transitions=myParser.getTransitions();
//		updateLifetimeWithTransitions();
//		
////		an uparxei graphml ftiakse ton universal sumfwna me ton 
////		graph alliws ftiakston me ton default tropo
//		if(myParser.hasGraphml()){
//			savedChanges=myParser.getGraphmlLoader();
//			createDiachronicGraph(1,sql,frameX,frameY,scaleX,scaleY,centerX,centerY,targetFolder,edgeType,savedChanges);
//		}else{
//			createDiachronicGraph(0,sql,frameX,frameY,scaleX,scaleY,centerX,centerY,targetFolder,edgeType);
//		}
//		
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

	
//	private void createDiachronicGraph(int mode, String sql,double frameX,double frameY, double scaleX,double scaleY,double centerX,double centerY,String targetFolder,int edgeType,GraphmlLoader savedChanges) {
//		
//		diachronicGraph = new DiachronicGraph(savedChanges,sql,targetFolder,edgeType,mode,frameX,frameY,scaleX,scaleY,centerX,centerY);
//			
//	}
//	
//	private void createDiachronicGraph(int mode, String sql,double frameX,double frameY, double scaleX,double scaleY,double centerX,double centerY,String targetFolder,int edgeType) {
//		
//		diachronicGraph= new DiachronicGraph(lifetime,sql,targetFolder,edgeType,mode,frameX,frameY,scaleX,scaleY,centerX,centerY);
//	}
	
//	private void updateLifetimeWithTransitions(){
//		
//		for(int i=0;i<lifetime.size();++i)
//			if(i==0)
//				setFirstVersion(lifetime.get(i));
//			else if(i==lifetime.size()-1)
//				setFinalVersion(lifetime.get(i),i);
//			else
//				setIntermediateVersion(lifetime.get(i),i);
//	
//			
//		
//	}
//	
//	/**
//	 * Trexw thn prwth version me to prwto Dictionary kai checkarw n dw an sthn
//	 * 2h version exei svistei kapoios pinakas.Me endiaferei mono to deletion
//	 * An kapoioi exoun ginei updated tha tous vapsw sthn 2h ekdosh,oxi edw
//	 * @param fversion :firstVersion
//	 */
//	private void setFirstVersion(DBVersion fversion){
//		
//		for(int i=0;i<fversion.getTables().size();++i)
//			if(transitions.get(0).containsKey(fversion.getTables().get(i).getKey())
//			&& transitions.get(0).get(fversion.getTables().get(i).getKey())==1)
//				fversion.getTables().get(i).setColorCode(1);		
//		
//	}
//	
//	/**
//	 * Trexw thn teleutaia version mou me to teleutaio dictionary mou,h thesh tou
//	 * teleutaiou dictionary mou einai mia prin apo thn thesh ths teleutaias version mou.
//	 * Psaxnw gia tables pou periexontai st dictionary mou KAI DEN einai deletions,einai 
//	 * dhladh mono newTable kai UpdateTable kai tous vafw analoga me thn timh pou exei to
//	 * dictionary mou.
//	 * @param fversion :finalVersion
//	 * @param k :H thesh ths teleutaias Version mou sthn Lista
//	 */
//	private void setFinalVersion(DBVersion fversion,int k){
//		
//		for(int i=0;i<fversion.getTables().size();++i)
//			if(transitions.get(k-1).containsKey(fversion.getTables().get(i).getKey())
//			&& transitions.get(k-1).get(fversion.getTables().get(i).getKey())!=1)
//				fversion.getTables().get(i).setColorCode(transitions.get(k-1).get(fversion.getTables().get(i).getKey()));
//		
//	}
//	
//	private void setIntermediateVersion(DBVersion version,int k){
//		
//		for(int i=0;i<version.getTables().size();++i){
//			//koitaw to mellontiko m dictionary
//			if(transitions.get(k).containsKey(version.getTables().get(i).getKey())
//			&& transitions.get(k).get(version.getTables().get(i).getKey())==1)
//				version.getTables().get(i).setColorCode(1);
//			
//			//koitaw to palho m dictionary
//			if(transitions.get(k-1).containsKey(version.getTables().get(i).getKey())
//			&& transitions.get(k-1).get(version.getTables().get(i).getKey())!=1)
//				version.getTables().get(i).setColorCode(transitions.get(k-1).get(version.getTables().get(i).getKey()));
//		
//				
//		}
//	}

}
