package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.antlr.v4.runtime.RecognitionException;

import parmenidianEnumerations.Status;
import externalTools.Deletion;
import externalTools.Delta;
import externalTools.HecateParser;
import externalTools.Insersion;
import externalTools.Schema;
import externalTools.Table;
import externalTools.TransitionList;
import externalTools.Transitions;
import externalTools.Update;
import fileFilter.SQLFileFilter;

public class HecateManager implements IHecateManager{
	private ArrayList<DBVersion> lifetime= new ArrayList<DBVersion>();
	private ArrayList<Map<String,Integer>> transitions = new ArrayList<Map<String,Integer>>();

	
	public ArrayList<DBVersion> parseSql(String sqlFiles){
		
		//parsarw ta sql arxeia kai t organwnw sthn mnhmh
		File[] versions = new File(sqlFiles).listFiles(new SQLFileFilter());
		for(int i=0;i<versions.length;++i)
			parseLifetime(versions[i]);
		
		return lifetime;
		
	}
	
	public ArrayList<Map<String,Integer>> parseXml(String xmlFile){
		
		//parsarw kai to xml me ta transitions
		parseTransitions(new File(xmlFile));
		
		return transitions;
		
	}
	
	public void createTransitions(File[] sqlFiles,File directorySelected){
		
		HecateParser parser= new HecateParser();
		Schema currentSchema;
		ArrayList<Schema> schemata= new ArrayList<Schema>();

		//create schema per sql and store them
		for(int i=0;i<sqlFiles.length;++i){			
			currentSchema=parser.parse(sqlFiles[i].getAbsolutePath());
			currentSchema.setTitle(String.valueOf(i));
			schemata.add(currentSchema);
		}
		
		Delta delta = new Delta();
		TransitionList tl;
		Transitions transitions = new Transitions();

		
		for(int i=0;i<schemata.size()-1;++i){
			tl=(delta.minus(schemata.get(i),schemata.get(i+1))).tl;
			transitions.add(tl);			
		}
		
		marshal(transitions,directorySelected);
	}
	
	
	private void marshal(Transitions transitions,File directorySelected) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Update.class, Deletion.class, Insersion.class, TransitionList.class, Transitions.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(transitions, new File(directorySelected +"\\transitions.xml"));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
	}

	@SuppressWarnings("static-access")
	private void parseLifetime(File version){
		
		
		try {		
			
			
		  HecateParser parser= new HecateParser();
		  Schema schema1 = parser.parse(version.getAbsolutePath());		
		  
		  ArrayList<String> tableList =schema1.getAllTables();
		  ArrayList<model.Table> tablesWithin=new ArrayList<model.Table>();
			
		  for(int i=0;i<tableList.size();++i){
			model.Table  tb = new model.Table(tableList.get(i));
			tablesWithin.add(tb);
		  }
		  
		 TreeMap<String,Table> versionTables = schema1.getTables();
         ArrayList<ForeignKey> versionForeignKeys= new ArrayList<ForeignKey>(); 

			
		 for(Map.Entry<String, Table> iterator : versionTables.entrySet())			 
			 for(int i=0;i<iterator.getValue().getfKey().getForeingKeys().size();++i)
				 versionForeignKeys.add(iterator.getValue().getfKey().getForeingKeys().get(i));
			  
		  DBVersion current = new DBVersion(tablesWithin,versionForeignKeys,version.getName());
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
		
		for(TransitionList tl :t.getList()){

			Map<String,Integer>  temp = new HashMap<String,Integer>();

			//to tl.getTransitionList mporei na einai null an apo thn mia ekdosh sthn allh den uparxoun
			//allages giauto to logo vrisketai edw auth h if
			if(tl.getTransitionList()!=null){

				for(int i=0;i<tl.getTransitionList().size();++i){
					
					
					if(tl.getTransitionList().get(i).getType().equals("NewTable"))
						temp.put(tl.getTransitionList().get(i).getAffTable().getName(),Status.CREATION.getValue());
					else if(tl.getTransitionList().get(i).getType().equals("DeleteTable"))
						temp.put(tl.getTransitionList().get(i).getAffTable().getName(),Status.DELETION.getValue());
					else if(tl.getTransitionList().get(i).getType().equals("UpdateTable"))//sketo t else pianei k to keychange pou den me endiaferei emena
						temp.put(tl.getTransitionList().get(i).getAffTable().getName(),Status.UPDATE.getValue());
					
				}			
			}
			transitions.add(temp);

		}	
	}

}
