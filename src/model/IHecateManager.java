package model;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

public interface IHecateManager {
	
	public ArrayList<DBVersion> parseSql(String sqlFiles);
	
	public ArrayList<Map<String,Integer>> parseXml(String xmlFile);
	
	public void createTransitions(File[] sqlFiles,File selectedDirectory);

}
