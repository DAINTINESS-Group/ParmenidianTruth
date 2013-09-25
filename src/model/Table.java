package model;

import java.awt.geom.Point2D;

public class Table {
	
	private String tableName;
	private Point2D coords;
	private int colorCode=-1;
	
	public Table(String name){
		
		tableName=name.trim();
		
	}
	
	public Table(String name,Point2D c){
		
		tableName=name.trim();
		coords=c;
		
	}
	
	public String getKey(){
		
		return tableName;
	}
	
	public void setColorCode(int code){
		
		colorCode=code;
	}

	public int getColorCode() {
		return colorCode;
	}

	public Point2D getCoords() {
		return coords;
	}

	public void setCoords(Object object) {
		this.coords = (Point2D) object;
	}

}
 