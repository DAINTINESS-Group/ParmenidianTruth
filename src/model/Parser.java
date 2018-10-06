package model;


import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;



public class Parser {
	private ArrayList<DBVersion> lifetime= new ArrayList<DBVersion>();
	private ArrayList<Map<String,Integer>> transitions = new ArrayList<Map<String,Integer>>();
	private boolean graphml=false;
	private GraphmlLoader graphmlLoader;
	private HecateManager worker = new HecateManager();

	
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
	
	public ArrayList<DBVersion>getLifetime(){
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
