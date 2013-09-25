package export;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.apache.poi.util.IOUtils;
import org.apache.poi.xslf.usermodel.SlideLayout;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFPictureData;
import org.apache.poi.xslf.usermodel.XSLFPictureShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFSlideLayout;
import org.apache.poi.xslf.usermodel.XSLFSlideMaster;
import org.apache.poi.xslf.usermodel.XSLFTextShape;


public class PowerPointGenerator {
	private String targetWorkspace;
	private String presentation;
	
	public PowerPointGenerator(String workspace,String pres){
		
		targetWorkspace=workspace;
		presentation=pres;
		
	}
	
	public void createPresentation(ArrayList<String> args) throws FileNotFoundException, IOException{	
		
		XMLSlideShow ppt = new XMLSlideShow();
		
		ppt=initializePresentation(ppt,args);
		
		for(int i=0;i<args.size();++i)
			ppt=appendSlideShow(args.get(i),ppt);	
	
		
		
		FileOutputStream out = new FileOutputStream(targetWorkspace+"/"+presentation+".pptx");
        ppt.write(out);
        out.close();
		
	}
	
	private XMLSlideShow initializePresentation(XMLSlideShow ppt,ArrayList<String> files) throws IOException {

        XSLFSlideMaster defaultMaster = ppt.getSlideMasters()[0];
        
        for(int i=0;i<files.size();++i){
        	if(files.get(i).contains("Universal Graph")){                
                
                XSLFSlide slide0 = ppt.createSlide();
        		
                BufferedImage bimg = ImageIO.read(new File(files.get(i)));
                int width          = bimg.getWidth();
                int height         = bimg.getHeight();                
                
                byte[] data = IOUtils.toByteArray(new FileInputStream(files.get(i)));
                int pictureIndex = ppt.addPicture(data, XSLFPictureData.PICTURE_TYPE_JPEG);
                XSLFPictureShape shape = slide0.createPicture(pictureIndex);
                
                ppt.setPageSize(new java.awt.Dimension(1100,height+100));               
        		
        	}
        }
		
        XSLFSlideLayout title = defaultMaster.getLayout(SlideLayout.TITLE_ONLY);
        XSLFSlide slide = ppt.createSlide(title);
        
        XSLFTextShape title1 = slide.getPlaceholder(0);
        title1.setAnchor(new Rectangle(0,1200/4-100/2,1100,100));
        title1.setText(setSlideTitle("Evolution Story"));
		
		return ppt;
	}

	private XMLSlideShow appendSlideShow(String imgPath,XMLSlideShow ppt) throws FileNotFoundException, IOException{

        if(!imgPath.contains(".jpg")|| imgPath.contains("Universal Graph"))
        	return ppt;
		
        
        XSLFSlideMaster defaultMaster = ppt.getSlideMasters()[0];

        XSLFSlideLayout title = defaultMaster.getLayout(SlideLayout.TITLE);
        XSLFSlide slide = ppt.createSlide(title);
       
        XSLFTextShape title1 = slide.getPlaceholder(0);
        title1.setAnchor(new Rectangle(0,0,1100,100));
        title1.setText(setSlideTitle(imgPath));
        
        
        BufferedImage bimg = ImageIO.read(new File(imgPath));
        int width          = bimg.getWidth();
        int height         = bimg.getHeight();
        
        
        byte[] data = IOUtils.toByteArray(new FileInputStream(imgPath));
        int pictureIndex = ppt.addPicture(data, XSLFPictureData.PICTURE_TYPE_JPEG);
        XSLFPictureShape shape = slide.createPicture(pictureIndex);
        shape.setAnchor(new Rectangle(0,100,width,height));        
        
        ppt.setPageSize(new java.awt.Dimension(1100,height+100));
        
        return ppt;			
		
	}

	private String setSlideTitle(String imgPath) {

		String[] leftArray = imgPath.split(".jpg",2);
		String[] rightArray = leftArray[0].split("\\\\");
		return rightArray[rightArray.length-1];

	}
	
	

}
