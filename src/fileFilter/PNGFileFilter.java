package fileFilter;

import java.io.File;
import java.io.FileFilter;

public class PNGFileFilter implements FileFilter {
	
	public boolean accept(File pathname) {
		if(pathname.getName().endsWith(".png"))
			return true;
		return false;
		
	}
	
}