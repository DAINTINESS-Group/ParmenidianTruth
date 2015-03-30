package loader;


import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;

import externalTools.HecateIntermediate;
import model.DatabaseVersion;
import model.Episode;
import model.GraphmlLoader;



public class Parser {
	private ArrayList<Episode> lifetime= new ArrayList<Episode>();
	private ArrayList<Map<String,Integer>> transitions = new ArrayList<Map<String,Integer>>();
	private boolean graphml=false;
	private GraphmlLoader graphmlLoader;
	private HecateIntermediate worker = new HecateIntermediate();

	
	public Parser(String sqlFiles,String xmlFile, String graphmlFile) throws Exception{
		
		lifetime=worker.parseSql(sqlFiles);
		
		transitions=worker.parseXml(xmlFile);
		

		//parsarw an uparxei to .graphml an uparxei
		if(graphmlFile!=null){
			graphml=true;
			try {
				graphmlLoader = new GraphmlLoader(graphmlFile);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		}

	}


	

	
	public ArrayList<Episode>getLifetime(){
		return lifetime;
	}
	
	public ArrayList<Map<String,Integer>>getTransitions(){
		return transitions;
	}


	public boolean hasGraphml() {
		return graphml;
	}

	public GraphmlLoader getGraphmlLoader() {
		return graphmlLoader;
	}

}
