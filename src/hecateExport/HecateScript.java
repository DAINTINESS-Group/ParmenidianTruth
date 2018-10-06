package hecateExport;

import java.io.File;

import fileFilter.SQLFileFilter;
import model.HecateManagerFactory;
import model.IHecateManager;

public class HecateScript {	
	private File directorySelected;
	private File[] sqlFiles;
	private HecateManagerFactory worker = new HecateManagerFactory();
	
	public HecateScript(File folder) throws Exception{
		Exception wrong = new Exception();

		directorySelected=folder;
		sqlFiles = directorySelected.listFiles(new SQLFileFilter());
		
		if(sqlFiles.length==0){
			throw wrong;
		}	
	}
	
	public void createTransitions(){
		IHecateManager newWorker;
		newWorker = worker.getHecateManager();
		newWorker.createTransitions(sqlFiles,directorySelected);
		
	}
}
