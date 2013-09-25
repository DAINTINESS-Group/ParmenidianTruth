package model;



import hecateImports.Schema;
import hecateImports.Table;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 
 * @author libathos
 *h klash Table einai ths Ekaths kai afora thn sql anaparastash twn table
 *h dikia m klash table edw einai model.table gia na apofigoumai ta conflicts
 *kai periexei plirofories sxetika me thn anaparastash tou table ws komvo
 *
 */
public class DatabaseVersion extends Episode {
	
	private String versionName;
	private ArrayList<model.Table> tablesWithin=new ArrayList<model.Table>();
	private ArrayList<ForeignKey> versionForeignKeys= new ArrayList<ForeignKey>(); 

	public DatabaseVersion(Schema schema,String vn){
		
		//1 set VersionName
		
		
		String[] array =vn.split(".sql",2);
		versionName=array[0];
		
		//2 set all the tables of the current version
		ArrayList<String> tableList =schema.getAllTables();
		
		for(int i=0;i<tableList.size();++i){
			model.Table  tb = new model.Table(tableList.get(i));
			tablesWithin.add(tb);
		}
		
		//3 set all the FK dependencies of the current version
		 TreeMap<String,Table> VersionTables = schema.getTables();
		
		 for(Map.Entry<String, Table> iterator : VersionTables.entrySet())			 
			 for(int i=0;i<iterator.getValue().getfKey().getForeingKeys().size();++i)
				 versionForeignKeys.add(iterator.getValue().getfKey().getForeingKeys().get(i));
		
		 //just check FK dependencies are well put
		 for(int i=0;i<versionForeignKeys.size();++i)
			 System.out.println(versionForeignKeys.get(i).getKey());
			 
			 
		 
		
	}

	
	public ArrayList<model.Table> getTables() {
		
		return tablesWithin;
	}

//	public String getVersionName() {
//		return versionName;
//	}


	public ArrayList<ForeignKey> getVersionForeignKeys() {
		return versionForeignKeys;
	}


	@Override
	public ArrayList<model.Table> getNodes() {
		
		return getTables();
		
	}


	@Override
	public ArrayList<ForeignKey> getEdges() {
		
		return getVersionForeignKeys();

	}


	@Override
	public String getVersion() {
		
		return this.versionName;
	}


	@Override
	public ConcurrentHashMap<String, model.Table> getGraph() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
