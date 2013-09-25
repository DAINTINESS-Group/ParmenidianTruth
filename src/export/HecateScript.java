package export;

import hecateImports.Deletion;
import hecateImports.Delta;
import hecateImports.HecateParser;
import hecateImports.Insersion;
import hecateImports.Schema;
import hecateImports.TransitionList;
import hecateImports.Transitions;
import hecateImports.Update;

import java.io.File;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;


import fileFilter.SQLFileFilter;

public class HecateScript {	
	private File selectedDirectory;
	File[] sqlFiles;
	
	public HecateScript(File folder) throws Exception{
		Exception wrong = new Exception();

		selectedDirectory=folder;
		sqlFiles = selectedDirectory.listFiles(new SQLFileFilter());
		
		if(sqlFiles.length==0){
			throw wrong;
		}
		
		s(selectedDirectory);
		
	}
	
	public void createTransitions(){
		
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
		Transitions trs = new Transitions();

		
		for(int i=0;i<schemata.size()-1;++i){
			tl=delta.minus(schemata.get(i),schemata.get(i+1));
			trs.add(tl);			
		}
		
		marshal(trs);
		
		

		
		
	}

	private void marshal(Transitions trs) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Update.class, Deletion.class, Insersion.class, TransitionList.class, Transitions.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(trs, new File(selectedDirectory +"\\transitions.xml"));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
	}

	private void s(Object obj) {

		System.out.println(obj.toString());
		
	}
	
	

}
