package model;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout2;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.io.GraphMLWriter;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.PickableVertexPaintTransformer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import edu.uci.ics.jung.visualization.transform.MutableTransformer;

public class DiachronicGraphVisualRepresentation {
//	private Graph<String, String> g;
	private ArrayList<Table> nodes = new ArrayList<Table>();
	private ArrayList<ForeignKey> edges= new ArrayList<ForeignKey>();
	private String inputFolder;
	private SpringLayout2<String, String> layout;
	private static DefaultModalGraphMouse<String, Number> graphMouse = new DefaultModalGraphMouse<String, Number>();
	private VisualizationViewer<String, String> vv;
//	private double frameX = 0;
//	private double frameY = 0;
//	private double scaleX =1;
//	private double scaleY =1;
	private Dimension universalFrame;
//	private Point2D universalCenter;
	private String targetFolder;
	private Transformer edgeType;
	private MutableTransformer universalTransformerForTranslation;
	private MutableTransformer universalTransformerForScaling;
	private DiachronicGraph parent;

	
//	//for episodes
//	public EpisodeGenerator(DatabaseVersion ep, String tf, int et) {		
//		
//
//		edgeType = et == 0 ? new EdgeShape.Line<String, String>(): new EdgeShape.Orthogonal<String, String>();
//		
//		
//
//		g = new DirectedSparseGraph<String, String>();
//		episode = ep;
//		targetFolder = tf+"//screenshots";
//		new File(targetFolder ).mkdir();
//		addNodes(episode);
//		addEdges(episode);
//
//
//
//	}
	
	//for universalGraph mode==0 kainourgio layout,mode==1 savedLayout 
	public DiachronicGraphVisualRepresentation(DiachronicGraph p,ArrayList<Table> tables,ArrayList<ForeignKey> fks,String in, String tf, int et,int mode,double frameX,double frameY,double scaleX,double scaleY,double centerX,double centerY) {		
		

		edgeType = et == 0 ? new EdgeShape.Line<String, String>(): new EdgeShape.Orthogonal<String, String>();
		parent=p;

//		g = new DirectedSparseGraph<String, String>();
		nodes=tables;
		edges=fks;
		inputFolder=in;
		targetFolder = tf+"//screenshots";
		new File(targetFolder ).mkdir();
//		addNodes();
//		addEdges();

		layout= new SpringLayout2<String, String>(parent.getGraph());


		
		universalFrame =new Dimension(nodes.size()*26, nodes.size()*26);

		layout.setSize(universalFrame);
		vv = new VisualizationViewer<String, String>(layout);
		vv.setSize(new Dimension(universalFrame.width+300, universalFrame.height+300));
		
		
		

		// Setup up a new vertex to paint transformer...
		Transformer<String, Paint> vertexPaint = new Transformer<String, Paint>() {
			public Paint transform(String i) {
				return new Color(207, 247, 137, 200);
			}
		};

		vv.getRenderContext().setVertexFillPaintTransformer((new PickableVertexPaintTransformer<String>(vv.getPickedVertexState(),new Color(207, 247, 137, 200), Color.yellow)));
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.N);
		vv.setBackground(Color.WHITE);
		vv.getRenderContext().setEdgeShapeTransformer(edgeType);

		// ---------------default graph moving
		graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);
		vv.setGraphMouse(graphMouse);
		
		
		System.out.println("Diachronic Graph's Bounds: "+vv.getBounds());
		
		
		//ekteleitai mono an exei anoiksei apo arxeio graphml
		if(mode==1){
			for (int i = 0; i < nodes.size(); ++i) {
				layout.setLocation(nodes.get(i).getKey(),nodes.get(i).getCoords());
				layout.lock(nodes.get(i).getKey(), true);
			}
			
			MutableTransformer layoutTranformer = vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT);
			
			if(scaleX<=1 && scaleY<=1){	
				layoutTranformer.setTranslate(frameX, frameY);

				MutableTransformer scaleTranformer = vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.VIEW);
				scaleTranformer.scale(scaleX, scaleY, new Point2D.Double(centerX,centerY));
				
			}else{
				layoutTranformer.setScale(scaleX, scaleY, new Point2D.Double(centerX,centerY) );
				layoutTranformer.setTranslate(frameX, frameY);
			}
		}

	}

	public VisualizationViewer show() {
		
		universalTransformerForTranslation = vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT);
		universalTransformerForScaling  = vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.VIEW);
		
		vv.repaint();

		return vv;
	}

	public void setTransformingMode() {

		graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);
		vv.setGraphMouse(graphMouse);

	}

	public void setPickingMode() {

		graphMouse.setMode(ModalGraphMouse.Mode.PICKING);
		vv.setGraphMouse(graphMouse);

	}

	public void saveVertexCoordinates(String projectIni) throws IOException {


		//save sthn mnhmh
		for (int i = 0; i <nodes.size(); ++i)
			nodes.get(i).setCoords(layout.transform(nodes.get(i).getKey()));
		
		//save sto .graphml arxeio
		GraphMLWriter<String,String> graphWriter = new GraphMLWriter<String, String> ();
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(inputFolder+"\\uber.graphml")));
		

		
		graphWriter.addVertexData("x", null, "0",
			    new Transformer<String, String>() {
			        public String transform(String v) {
			        	
				            return Double.toString(((AbstractLayout)layout).getX(v));
			        		
			        	}		        	
			        }		    
			);
		
		
		graphWriter.addVertexData("y", null, "0",
			    new Transformer<String, String>() {
			        public String transform(String v) {
			        	
				            return Double.toString(((AbstractLayout<String, String>) layout).getY(v));
			        		
			        	}			        	
			        }		    
			);
		
		
//		updateFrameInfo();
		
		graphWriter.save(parent.getGraph(), out);
		
		
		updateIniFile(projectIni);
		


	}




	public void createEpisode() {

//		System.out.println("size of Diachronic Graph: "+vv.getWidth()+","+vv.getHeight());

		
		File file =new File(targetFolder + "/"+	"Diachronic Graph"  + ".jpg");
		
		int width = vv.getWidth();
		int height = vv.getHeight();

		BufferedImage bi = new BufferedImage(width, height,	BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = bi.createGraphics();
		vv.paint(graphics);
		graphics.dispose();

		
		try {
			ImageIO.write(bi, "jpeg", file);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}	




//    public void spreadOut(Component nowShowing) {
//    	
//    	PickedState<String> ps =  ps = vv.getPickedVertexState();
//    	
//    		Collection<String> picked = ps.getPicked();
//    		if(picked.size() > 1) {
//    			Point2D center = new Point2D.Double();
//    			double x = 0;
//    			double y = 0;
//    			for(String vertex : picked) {
//    				Point2D p = layout.transform(vertex);
//    				x += p.getX();
//    				y += p.getY();
//    			}
//    			x /= picked.size();
//    			y /= picked.size();
//				center.setLocation(x,y);
//
//				
//    			Graph<String, String> subGraph;
//    			try {
//    				subGraph = g.getClass().newInstance();
//    				for(String vertex : picked) {
//    					subGraph.addVertex(vertex);
//    					Collection<String> incidentEdges = g.getIncidentEdges(vertex);
//    					for(String edge : incidentEdges) {
//    						Pair<String> endpoints = g.getEndpoints(edge);
//    						if(picked.containsAll(endpoints)) {
//    							// put this edge into the subgraph
//    							subGraph.addEdge(edge, endpoints.getFirst(), endpoints.getSecond());
//    						}
//    					}
//    				}
//
//    				Layout<String,String> subLayout = getLayoutFor(FRLayout.class, subGraph);
//    				subLayout.setInitializer(vv.getGraphLayout());
//    				subLayout.setSize(new Dimension(picked.size()*23,picked.size()*23));
//    				layout.put(subLayout,center);
//    				vv.setGraphLayout(layout);
//    				
//
//    			} catch (Exception e) {
//    				e.printStackTrace();
//    			}
//    		}
//    		
//    		parent.getContentPane().remove(nowShowing);
//			parent.setNowShowing(parent.getContentPane().add(vv));				
//
//    }

	public VisualizationViewer<String, String> getVv() {
		return vv;
	}

	public String getTargetFolder() {
		return targetFolder;
	}
	
	public void stop(){
		((SpringLayout2)layout).lock(true);
	}
	
//	public void updateVisualizationViewer(double attraction,double repulsion,Component nowShowing){
//		
//		
//		((FRLayout) layout.getDelegate()).setAttractionMultiplier(attraction);
//		((FRLayout) layout.getDelegate()).setRepulsionMultiplier(repulsion);
//
//		
//		vv.removeAll();
//		
//		
//		vv.setGraphLayout(layout);
//		
//		
//
//		
//		Point2D ivtfrom = vv
//				.getRenderContext()
//				.getMultiLayerTransformer()
//				.inverseTransform(Layer.VIEW,
//						new Point2D.Double(vv.getWidth(), vv.getHeight()));
//		MutableTransformer modelTransformer = vv.getRenderContext()
//				.getMultiLayerTransformer().getTransformer(Layer.LAYOUT);
//		modelTransformer.scale(1,1, ivtfrom);
//		
//
//		parent.getContentPane().remove(nowShowing);
//		parent.getContentPane().add(vv);
//		parent.setNowShowing(parent.getContentPane().add(vv));				
//
//
//		
//		
//	}
	
	
	private void updateIniFile(String projectIni) {
		
		
		try {
			
			
			BufferedReader reader = new BufferedReader(new FileReader(projectIni));
			String line;
			String restOfFile = "";
			while((line = reader.readLine()) != null )
			{
				
				if(line.contains("Project Name") || line.contains("sql@") ||line.contains("transition@")||line.contains("output@"))
					restOfFile+=line+"\n";
			    	
			}
			
			reader.close();
			
				
				PrintWriter writer;
				
				writer = new PrintWriter(new FileWriter(projectIni));
				writer.println(restOfFile);
				writer.println("graphml@"+inputFolder+"\\uber.graphml");
				writer.println("frameX@"+universalTransformerForTranslation.getTranslateX());
				writer.println("frameY@"+universalTransformerForTranslation.getTranslateY());
				writer.println("centerX@"+vv.getCenter().getX());
				writer.println("centerY@"+vv.getCenter().getY());
				
				if(universalTransformerForScaling.getScaleX()==1 && universalTransformerForScaling.getScaleY()==1 ){
					writer.println("scaleX@"+universalTransformerForTranslation.getScaleX());
					writer.println("scaleY@"+universalTransformerForTranslation.getScaleY());
				}else{
					writer.println("scaleX@"+universalTransformerForScaling.getScaleX());
					writer.println("scaleY@"+universalTransformerForScaling.getScaleY());
				}	
				
				
				writer.close();
				
			
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
//	private void updateFrameInfo() {
//		
//		frameX = universalTransformerForTranslation.getTranslateX();
//		frameY = universalTransformerForTranslation.getTranslateY();
//		
//		scaleX = universalTransformerForScaling.getScaleX();
//		scaleY = universalTransformerForScaling.getScaleY();
//		
//		if(scaleX==1 && scaleY ==1){
//			
//			scaleX = universalTransformerForTranslation.getScaleX();
//			scaleY = universalTransformerForTranslation.getScaleY();
//			
//		}
			
		
//		universalCenter = vv.getCenter();
//				.getRenderContext()
//				.getMultiLayerTransformer()
//				.inverseTransform(Layer.VIEW,
//						new Point2D.Double(vv.getWidth(), vv.getHeight()));
		
//		
//		System.out.println(((SpringLayout2)layout).g)
		
//	}
	
//	private void addNodes() {
//
//		for (int i = 0; i < nodes.size(); ++i)
//			g.addVertex(nodes.get(i).getKey());
//
//	}
//
//	private void addEdges() {
//
//		for (int i = 0; i < edges.size(); ++i)
//			g.addEdge(Integer.toString(i), edges.get(i)
//					.getSourceTable(),edges.get(i)
//					.getTargetTable());
//
//	}

	public  double getFrameX() {
//		return frameX;
		return universalTransformerForTranslation.getTranslateX();
		
	}

	public  double getFrameY() {
//		return frameY;
		return universalTransformerForTranslation.getTranslateY();
	}

	public double getScaleX() {
//		return scaleX;
//		return universalTransformerForScaling.getScaleX();
		
		if(universalTransformerForScaling.getScaleX()==1 && universalTransformerForScaling.getScaleY()==1 ){
			return universalTransformerForTranslation.getScaleX();
		}else{
			return universalTransformerForScaling.getScaleX();
		}	

	}

	public double getScaleY() {
//		return scaleY;
//		return universalTransformerForScaling.getScaleY();
		
		if(universalTransformerForScaling.getScaleX()==1 && universalTransformerForScaling.getScaleY()==1 ){
			return universalTransformerForTranslation.getScaleY();
		}else{
			return universalTransformerForScaling.getScaleY();
		}	
	}

	public Dimension getUniversalFrame() {
		return universalFrame;
	}

	public Point2D getUniversalCenter() {
		return vv.getCenter();
	}

	public int getWidthOfVisualizationViewer(){
		
		return vv.getWidth();
		
	}
	
	public int getHeightOfVisualizationViewer(){
		
		return vv.getHeight();
		
	}

	public Component refresh(double forceMult, int repulsionRange) {
		
		layout.setForceMultiplier(forceMult);
		layout.setRepulsionRange(repulsionRange);
		
		
		vv.removeAll();
		
		System.out.println(forceMult+"||"+repulsionRange);
		
		vv.setGraphLayout(layout);
		
		return vv;
	}

//	public Dimension getUniversalFrame() {
//		return universalFrame;
//	}
//
//	public Point2D getUniversalCenter() {
//		return universalCenter;
//	}
	
//    private Layout getLayoutFor(Class layoutClass, Graph graph) throws Exception {
//    	Object[] args = new Object[]{graph};
//    	Constructor constructor = layoutClass.getConstructor(new Class[] {Graph.class});
//    	return  (Layout)constructor.newInstance(args);
//    }


}
