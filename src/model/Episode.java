package model;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Episode {
	
	public abstract ArrayList<Table> getNodes();
	
	public abstract ArrayList<ForeignKey> getEdges();
	
	public abstract String getVersion();
	
	public abstract ConcurrentHashMap<String, Table> getGraph();

}
