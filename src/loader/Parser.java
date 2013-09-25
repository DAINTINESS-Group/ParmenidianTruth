package loader;


import hecateImports.Deletion;
import hecateImports.HecateParser;
import hecateImports.Insersion;
import hecateImports.Schema;
import hecateImports.TransitionList;
import hecateImports.Transitions;
import hecateImports.Update;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import model.DatabaseVersion;

import org.antlr.v4.runtime.RecognitionException;


import fileFilter.GRAPHMLFileFilter;
import fileFilter.SQLFileFilter;
import fileFilter.XMLFileFilter;



public class Parser {
	private File selectedDirectory;
	private ArrayList<DatabaseVersion> lifetime= new ArrayList<DatabaseVersion>();
	private ArrayList<Map<String,Integer>> transitions = new ArrayList<Map<String,Integer>>();
	private boolean graphml=false;
	private GraphmlLoader graphmlLoader;
	

	
	public Parser(File folder) throws Exception{
		
		selectedDirectory = new File(folder.getPath());	
//		Exception e = null;
//		
////		if(! isValidFolder())			
////			throw e;
//		
			
		
		//parsarw ta sql arxeia kai t organwnw sthn mnhmh
		File[] versions = selectedDirectory.listFiles(new SQLFileFilter());
		for(int i=0;i<versions.length;++i)
			parseLifetime(versions[i]);
		
		//parsarw kai to xml me ta transitions.Ypothetoume oti yparxei mono 1
		File transitions = selectedDirectory.listFiles(new XMLFileFilter())[0];
		parseTransitions(transitions);


		
		//parsarw an uparxei to .graphml an uparxei
		if(selectedDirectory.listFiles(new GRAPHMLFileFilter()).length==1){
			graphml=true;
			try {
				graphmlLoader = new GraphmlLoader(selectedDirectory.listFiles(new GRAPHMLFileFilter())[0].toString());
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		}

	}

//	private boolean isValidFolder(){
//		boolean sqlExists=false;
//		boolean xmlExists=false;
//		
//		for(int i=0;i<selectedDirectory.listFiles().length;++i){
//			if(selectedDirectory.listFiles()[i].getName().endsWith(".sql"))
//				sqlExists=true;
//			else if(selectedDirectory.listFiles()[i].getName().endsWith(".xml"))
//				xmlExists=true;
//			if(sqlExists && xmlExists)
//				break;
//		}
//		
//		return sqlExists && xmlExists ;
//		
//	}
	
	@SuppressWarnings("static-access")
	private void parseLifetime(File version){
		
		System.out.println(version.getAbsolutePath());
		
		try {		
			
			
		  HecateParser parser= new HecateParser();
		  Schema schema1 = parser.parse(version.getAbsolutePath());		  
			  
		  DatabaseVersion current = new DatabaseVersion(schema1,version.getName());
		  lifetime.add(current);
			  

		} catch (RecognitionException e) {
			e.printStackTrace();
		}
		
	}
	
	private void parseTransitions(File transition){
		
		// Unmarshal
		InputStream inputStream;
		Transitions t = null;
		try {
			inputStream = new FileInputStream(transition.getAbsolutePath());
			JAXBContext jaxbContext = JAXBContext.newInstance(Update.class, Deletion.class, Insersion.class, TransitionList.class, Transitions.class);
			Unmarshaller u = jaxbContext.createUnmarshaller();
			t = (Transitions)u.unmarshal( inputStream );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		System.out.println(t.getList().size());

		
		for(TransitionList tl :t.getList()){


			Map<String,Integer>  temp = new HashMap<String,Integer>();

			//to tl.getTransitionList mporei na einai null an apo thn mia ekdosh sthn allh den uparxoun
			//allages giauto to logo vrisketai edw auth h if
			if(tl.getTransitionList()!=null){

				for(int i=0;i<tl.getTransitionList().size();++i){
					System.out.println("| ");
					
					
					if(tl.getTransitionList().get(i).type.equals("NewTable"))
						temp.put(tl.getTransitionList().get(i).affectedTable.getName(),0);
					else if(tl.getTransitionList().get(i).type.equals("DeleteTable"))
						temp.put(tl.getTransitionList().get(i).affectedTable.getName(),1);
					else if(tl.getTransitionList().get(i).type.equals("UpdateTable"))//sketo t else pianei k to keychange pou den me endiaferei emena
						temp.put(tl.getTransitionList().get(i).affectedTable.getName(),2);
					
				}
				
				
			}

			transitions.add(temp);

			System.out.println("--------------------------------------");
			
		}

		
	}
	
	public ArrayList<DatabaseVersion>getLifetime(){
		return lifetime;
	}
	
	public ArrayList<Map<String,Integer>>getTransitions(){
		return transitions;
	}


	public boolean isGraphml() {
		return graphml;
	}

	public GraphmlLoader getGraphmlLoader() {
		return graphmlLoader;
	}

}
