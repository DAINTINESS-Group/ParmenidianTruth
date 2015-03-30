package export;

import java.awt.Color;
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
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import model.Episode;
import org.apache.commons.collections15.Transformer;
import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout2;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
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

public class EpisodeGenerator{
	private Graph<String, String> g;
//	private Gui parent;
	private Episode episode;
	private String inputFolder;
	private Layout<String, String> layout;
//	private AggregateLayout <String, String> layout;
	private static DefaultModalGraphMouse<String, Number> graphMouse = new DefaultModalGraphMouse<String, Number>();
	private VisualizationViewer<String, String> vv;
	private static double frameX = 0;
	private static double frameY = 0;
	private double scaleX =1;
	private double scaleY =1;
	private String targetFolder;
	private Transformer edgeType;
	private Dimension universalFrame;
	private Point2D universalCenter;
	private MutableTransformer universalTransformerForTranslation;
	private MutableTransformer universalTransformerForScaling;

	
	//for episodes
	public EpisodeGenerator(Episode ep, /*Gui gui,*/ String tf, int et) {		
		

		edgeType = et == 0 ? new EdgeShape.Line<String, String>(): new EdgeShape.Orthogonal<String, String>();
		
		

		g = new DirectedSparseGraph<String, String>();
//		parent = gui;
		episode = ep;
		targetFolder = tf+"//screenshots";
		new File(targetFolder ).mkdir();
		addNodes(episode);
		addEdges(episode);

//		layout = new StaticLayout<String, String>(g);
//		layout.setSize(universalFrame);
//		vv = new VisualizationViewer<String, String>(layout);
//		vv.setPreferredSize(new Dimension(universalFrame.width+300, universalFrame.height+300));
//
//		// Setup up a new vertex to paint transformer...
//		Transformer<String, Paint> vertexPaint = new Transformer<String, Paint>() {
//			public Paint transform(String i) {
//				return new Color(207, 247, 137, 200);
//			}
//		};
//		vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
//		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
//		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.NE);
//		vv.setBackground(Color.WHITE);
//
//		vv.getRenderContext().setEdgeShapeTransformer(edgeType);

	}
	
	//for universalGraph mode==0 kainourgio layout,mode==1 savedLayout 
	public EpisodeGenerator(Episode ep,String in,/*Gui gui,*/ String tf, int et,int mode,double frameX,double frameY,double scaleX,double scaleY,double centerX,double centerY) {		
		

		edgeType = et == 0 ? new EdgeShape.Line<String, String>(): new EdgeShape.Orthogonal<String, String>();
		

		g = new DirectedSparseGraph<String, String>();
//		parent = gui;
		inputFolder=in;
		episode = ep;
		targetFolder = tf+"//screenshots";
		new File(targetFolder ).mkdir();
		addNodes(episode);
		addEdges(episode);

//		layout = new FRLayout<String, String>(g);
		layout= new SpringLayout2<String, String>(g);


//		layout = new SpringLayout2<String,String>(g);
//		((SpringLayout2)layout).setForceMultiplier(0.7);
//		((SpringLayout2)layout).setRepulsionRange(25000);
		
		universalFrame =new Dimension(ep.getNodes().size()*26, ep.getNodes().size()*26);

		layout.setSize(universalFrame);
		vv = new VisualizationViewer<String, String>(layout);
		vv.setPreferredSize(new Dimension(universalFrame.width+300, universalFrame.height+300));
		

		// Setup up a new vertex to paint transformer...
		Transformer<String, Paint> vertexPaint = new Transformer<String, Paint>() {
			public Paint transform(String i) {
				return new Color(207, 247, 137, 200);
			}
		};

//		vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
		vv.getRenderContext().setVertexFillPaintTransformer((new PickableVertexPaintTransformer<String>(vv.getPickedVertexState(),new Color(207, 247, 137, 200), Color.yellow)));
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.N);
		vv.setBackground(Color.WHITE);
		vv.getRenderContext().setEdgeShapeTransformer(edgeType);

		// ---------------default graph moving
		graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);
		vv.setGraphMouse(graphMouse);
		
		//ekteleitai mono an exei anoiksei apo arxeio graphml
		if(mode==1){
			for (int i = 0; i < episode.getNodes().size(); ++i) {
				layout.setLocation(episode.getNodes().get(i).getKey(),episode.getNodes().get(i).getCoords());
				layout.lock(episode.getNodes().get(i).getKey(), true);
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

//		Point2D ivtfrom = vv
//				.getRenderContext()
//				.getMultiLayerTransformer()
//				.inverseTransform(Layer.VIEW,
//						new Point2D.Double(vv.getWidth(), vv.getHeight()));
		
		universalTransformerForTranslation = vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT);
		universalTransformerForScaling  = vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.VIEW);
//		universalTransformer.scale(1.0, 1.0, vv.getCenter());
		
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

	public Episode saveVertexCoordinates(Episode episode,String projectIni) throws IOException {


		//save sthn mnhmh
		for (int i = 0; i < episode.getNodes().size(); ++i)
			episode.getNodes()
					.get(i)
					.setCoords(layout.transform(episode.getNodes().get(i).getKey()));
		
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
		
		
		updateFrameInfo();
		
		graphWriter.save(g, out);
		
		
		updateIniFile(projectIni);
		
		return episode;


	}




	public void createEpisodes(final Episode diachronic,EpisodeGenerator eg) {

		
//		layout = new FRLayout<String, String>(g);
		layout = new StaticLayout<String, String>(g);

		layout.setSize(eg.universalFrame);
		VisualizationViewer<String, String> vv = new VisualizationViewer<String, String>(layout);
		
		//vv.setPreferredSize(new Dimension(1100, 1100));
//		vv.setPreferredSize(new Dimension(this.vv.getSize().width+200, this.vv.getSize().height+200));
		vv.setPreferredSize(new Dimension(eg.universalFrame.width+300, eg.universalFrame.height+300));
		
		// Setup up a new vertex to paint transformer...
		Transformer<String, Paint> vertexPaint = new Transformer<String, Paint>() {
			public Paint transform(String v) {

				for (int i = 0; i < episode.getNodes().size(); ++i)
					if (episode.getNodes().get(i).getKey().equals(v))
						if (episode.getNodes().get(i).getColorCode() == -1)
							return new Color(129, 215, 244, 200);
						else if (episode.getNodes().get(i).getColorCode() == 0)
							return new Color(0, 255, 0, 200);
						else if (episode.getNodes().get(i).getColorCode() == 1)
							return new Color(255, 0, 0, 200);
						else
							return new Color(255, 255, 0, 200);

				return Color.BLACK;

			}
		};

		
		//gia ton kathe komvo tou kathe episode,koitaw ton universalGraph
		//gia na dw th coordinates exei hdh kai ths lockarw ston grafo
		for (int i = 0; i < episode.getNodes().size(); ++i) {
			layout.setLocation(episode.getNodes().get(i).getKey(),(diachronic.getGraph().get(episode.getNodes().get(i).getKey()).getCoords()));
			layout.lock(episode.getNodes().get(i).getKey(), true);
		}
		
		

		MutableTransformer layoutTranformer = vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT);
		
		if(eg.scaleX<=1 && eg.scaleY<=1){	
			layoutTranformer.setTranslate(frameX, frameY);

			MutableTransformer scaleTranformer = vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.VIEW);
			scaleTranformer.scale(eg.scaleX, eg.scaleY, eg.universalCenter);
			
		}else{
			layoutTranformer.setScale(eg.scaleX, eg.scaleY, eg.universalCenter);
			layoutTranformer.setTranslate(frameX, frameY);
		}
		

		

		

		vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.NE);
		vv.getRenderContext().setEdgeShapeTransformer(edgeType);
		vv.setBackground(Color.WHITE);

		JFrame frame = new JFrame((episode.getVersion()));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(vv);
		frame.pack();
		frame.setVisible(true);
		
		writeJPEGImage(new File(targetFolder + "/"
				+ episode.getVersion() + ".jpg"), vv);

	}

	protected void writeJPEGImage(File file, VisualizationViewer vv) {
		int width = vv.getWidth();
		int height = vv.getHeight();

		BufferedImage bi = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
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
		
//		boolean existAlready=false;
		
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
			
//			if(!existAlready){
				
				PrintWriter writer;
				
				writer = new PrintWriter(new FileWriter(projectIni));
				writer.println(restOfFile);
				writer.println("graphml@"+inputFolder+"\\uber.graphml");
				writer.println("frameX@"+frameX);
				writer.println("frameY@"+frameY);
				writer.println("scaleX@"+scaleX);
				writer.println("scaleY@"+scaleY);
				writer.println("centerX@"+universalCenter.getX());
				writer.println("centerY@"+universalCenter.getY());
				writer.close();
				
//			}
			
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void updateFrameInfo() {
		
		frameX = universalTransformerForTranslation.getTranslateX();
		frameY = universalTransformerForTranslation.getTranslateY();
		
		scaleX = universalTransformerForScaling.getScaleX();
		scaleY = universalTransformerForScaling.getScaleY();
		
		if(scaleX==1 && scaleY ==1){
			
			scaleX = universalTransformerForTranslation.getScaleX();
			scaleY = universalTransformerForTranslation.getScaleY();
			
		}
			
		
		universalCenter = vv.getCenter();
//				.getRenderContext()
//				.getMultiLayerTransformer()
//				.inverseTransform(Layer.VIEW,
//						new Point2D.Double(vv.getWidth(), vv.getHeight()));
		
//		
//		System.out.println(((SpringLayout2)layout).g)
		
	}
	
	private void addNodes(Episode episode) {

		for (int i = 0; i < episode.getNodes().size(); ++i)
			g.addVertex(episode.getNodes().get(i).getKey());

	}

	private void addEdges(Episode episode) {

		for (int i = 0; i < episode.getEdges().size(); ++i)
			g.addEdge(Integer.toString(i), episode.getEdges().get(i)
					.getSourceTable(), episode.getEdges().get(i)
					.getTargetTable());

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
