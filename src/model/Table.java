package model;

import java.awt.geom.Point2D;

import parmenidianEnumerations.Status;

public class Table {
	
	private String tableName;
	private Point2D coordinates;
	private int tableStatus = Status.UNDEFINED.getValue();
	
	public Table(String name){
		
		tableName=name.trim();
		
	}
	
	public Table(String name,Point2D c){
		
		tableName=name.trim();
		coordinates=c;
		
	}
	
	public String getKey(){
		
		return tableName;
	}
	
	public void setTableStatus(int status){
		
		tableStatus=status;
	}

	public int getTableStatus() {
		return tableStatus;
	}

	public Point2D getCoords() {
		return coordinates;
	}

	public void setCoords(Object object) {
		this.coordinates = (Point2D) object;
	}

}
 