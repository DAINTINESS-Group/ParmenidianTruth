package model;

import java.util.ArrayList;

public class EpisodeFactory {
	
	public static Episode createEpisode(ArrayList<Table> tablesWithin,ArrayList<ForeignKey> versionForeignKeys,String vn){
		
		return new DatabaseVersion(tablesWithin,versionForeignKeys,vn);
		
	}
	
	public static Episode createEpisode(ArrayList<Episode> vrs){
		
		return new DiachronicGraph(vrs);
		
	}
	
	public static Episode createEpisode(GraphmlLoader gml){
		
		return new DiachronicGraph(gml);
		
	}
	
	

}
