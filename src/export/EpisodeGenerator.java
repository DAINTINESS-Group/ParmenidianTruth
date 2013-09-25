package export;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import model.Episode;
import model.DiachronicGraph;
import org.apache.commons.collections15.Transformer;
import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.io.GraphMLWriter;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import edu.uci.ics.jung.visualization.transform.MutableTransformer;
import gui.Gui;

public class EpisodeGenerator {
	private Graph<String, String> g;
	private Gui context;
	private Episode episode;
	private String inputFolder;
	private Layout<String, String> layout;
	private static DefaultModalGraphMouse<String, Number> graphMouse = new DefaultModalGraphMouse<String, Number>();
	private VisualizationViewer<String, String> vv;
	private static double frameX = 0;
	private static double frameY = 0;
	private String targetFolder;
	private Transformer edgeType;

	
	//for episodes
	public EpisodeGenerator(Episode ep, Gui gui, String tf, int et) {		
		

		edgeType = et == 0 ? new EdgeShape.Line<String, String>(): new EdgeShape.Orthogonal<String, String>();

		g = new DirectedSparseGraph<String, String>();
		context = gui;
		episode = ep;
		targetFolder = tf;
		addNodes(episode);
		addEdges(episode);

		layout = new FRLayout<String, String>(g);
		layout.setSize(new Dimension(700, 700));
		vv = new VisualizationViewer<String, String>(layout);
		vv.setPreferredSize(new Dimension(1000, 1000));

		// Setup up a new vertex to paint transformer...
		Transformer<String, Paint> vertexPaint = new Transformer<String, Paint>() {
			public Paint transform(String i) {
				return new Color(207, 247, 137, 200);
			}
		};

		vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.NE);
		vv.setBackground(Color.WHITE);

		vv.getRenderContext().setEdgeShapeTransformer(edgeType);

		// ---------------mouse picking
		graphMouse.setMode(ModalGraphMouse.Mode.PICKING);
		vv.setGraphMouse(graphMouse);

	}
	
	//for universalGraph mode==0 kainourgio layout,mode==1 savedLayout 
	public EpisodeGenerator(Episode ep,String in,Gui gui, String tf, int et,int mode) {		
		

		edgeType = et == 0 ? new EdgeShape.Line<String, String>(): new EdgeShape.Orthogonal<String, String>();

		g = new DirectedSparseGraph<String, String>();
		context = gui;
		inputFolder=in;
		episode = ep;
		targetFolder = tf;
		addNodes(episode);
		addEdges(episode);

		layout = new FRLayout<String, String>(g);
		layout.setSize(new Dimension(700, 700));
		vv = new VisualizationViewer<String, String>(layout);
		vv.setPreferredSize(new Dimension(1000, 1000));

		// Setup up a new vertex to paint transformer...
		Transformer<String, Paint> vertexPaint = new Transformer<String, Paint>() {
			public Paint transform(String i) {
				return new Color(207, 247, 137, 200);
			}
		};

		vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.NE);
		vv.setBackground(Color.WHITE);
		vv.getRenderContext().setEdgeShapeTransformer(edgeType);

		// ---------------mouse picking
		graphMouse.setMode(ModalGraphMouse.Mode.PICKING);
		vv.setGraphMouse(graphMouse);
		
		//ekteleitai mono an exei anoiksei apo arxeio graphml
		if(mode==1)
			for (int i = 0; i < episode.getNodes().size(); ++i) {
				layout.setLocation(episode.getNodes().get(i).getKey(),episode.getNodes().get(i).getCoords());
				layout.lock(episode.getNodes().get(i).getKey(), true);
			}

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

	public Component show() {

		Point2D ivtfrom = vv
				.getRenderContext()
				.getMultiLayerTransformer()
				.inverseTransform(Layer.VIEW,
						new Point2D.Double(vv.getWidth(), vv.getHeight()));
		MutableTransformer modelTransformer = vv.getRenderContext()
				.getMultiLayerTransformer().getTransformer(Layer.LAYOUT);
		modelTransformer.scale(0.9, 0.9, ivtfrom);
		vv.repaint();

		Component a = context.getContentPane().add(vv);
		context.pack();
		return a;
	}

	public void setTransformingMode() {

		graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);
		vv.setGraphMouse(graphMouse);

	}

	public void setPickingMode() {

		graphMouse.setMode(ModalGraphMouse.Mode.PICKING);
		vv.setGraphMouse(graphMouse);

	}

	public Episode saveVertexCoordinates(Episode episode) throws IOException {

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
			            return Double.toString(((AbstractLayout<String, String>) layout).getX(v));
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
		
		
		check();
		
		graphWriter.save(g, out);
		
		
		return episode;


	}

	public void createEpisodes(final Episode diachronic) {

		layout = new FRLayout<String, String>(g);
		layout.setSize(new Dimension(1000, 1000));
		VisualizationViewer<String, String> vv = new VisualizationViewer<String, String>(
				layout);
		vv.setPreferredSize(new Dimension(1100, 1100));

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
		s("contains: "+episode.getNodes().size());
		for (int i = 0; i < episode.getNodes().size(); ++i) {
			layout.setLocation(episode.getNodes().get(i).getKey(),(diachronic.getGraph().get(episode.getNodes().get(i).getKey()).getCoords()));
			layout.lock(episode.getNodes().get(i).getKey(), true);
		}
		
		for (int i = 0; i < episode.getNodes().size(); ++i) {
			s(episode.getNodes().get(i).getKey()+diachronic.getNodes().get(i).getCoords());
		}
		

		MutableTransformer modelTransformer = vv.getRenderContext()
				.getMultiLayerTransformer().getTransformer(Layer.LAYOUT);
		modelTransformer.setTranslate(frameX, frameY);
		s(frameX);
		s(frameY);

		vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.NE);
		vv.getRenderContext().setEdgeShapeTransformer(edgeType);
		vv.setBackground(Color.WHITE);

		JFrame frame = new JFrame((episode.getVersion()));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(vv);
		frame.pack();
		frame.setVisible(false);
		
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

		s(file);
		
		try {
			ImageIO.write(bi, "jpeg", file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void s(Object string) {

		System.out.println(string);

	}

	public void check() {

		MutableTransformer modelTransformer = vv.getRenderContext()
				.getMultiLayerTransformer().getTransformer(Layer.LAYOUT);
		frameX = modelTransformer.getTranslateX();
		frameY = modelTransformer.getTranslateY();

		s(frameX + "|" + frameY);

	}	

}
