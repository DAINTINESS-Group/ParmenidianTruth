package model;

public class HecateManagerFactory {
	private IHecateManager hm = new HecateManager();
	
	public IHecateManager getHecateManager(){
		 return hm;
	}
}
