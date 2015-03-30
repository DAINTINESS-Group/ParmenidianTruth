package model;




import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 
 * @author libathos
 *h klash Table einai ths Ekaths kai afora thn sql anaparastash twn table
 *h dikia m klash table edw einai model.table gia na apofigoume ta conflicts
 *kai periexei plirofories sxetika me thn anaparastash tou table ws komvo
 *
 */
public class DatabaseVersion extends Episode {
	
	private String versionName;
	private ArrayList<Table> tablesWithin=new ArrayList<Table>();
	private ArrayList<ForeignKey> versionForeignKeys= new ArrayList<ForeignKey>(); 

	public DatabaseVersion(ArrayList<Table> tablesWithin,ArrayList<ForeignKey> versionForeignKeys,String vn){
		
		//1 set VersionName				
		String[] array =vn.split(".sql",2);
		versionName=array[0];
		
		//2 set all the tables of the current version		
		this.tablesWithin=tablesWithin;

		
		//3 set all the FK dependencies of the current version
		 this.versionForeignKeys=versionForeignKeys;

		 
		
	}



	public ArrayList<Table> getTables() {
		
		return tablesWithin;
	}

//	public String getVersionName() {
//		return versionName;
//	}


	public ArrayList<ForeignKey> getVersionForeignKeys() {
		return versionForeignKeys;
	}


	@Override
	public ArrayList<Table> getNodes() {
		
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
	public ConcurrentHashMap<String,Table> getGraph() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}
	
}
