package model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import edu.uci.ics.jung.visualization.transform.MutableTransformer;

public class DBVersionVisualRepresentation {
	public Graph<String, String> g;
//	public DBVersion episode;
	public Layout<String, String> layout;
	public String targetFolder;
	public Transformer edgeType;
	private ArrayList<Table> nodes = new ArrayList<Table>();
	private ArrayList<ForeignKey> edges= new ArrayList<ForeignKey>();
	private String episodeName;

	//for episodes
	public DBVersionVisualRepresentation (ArrayList tables,ArrayList fks,String versionName/*, String tf, int et*/) {		
		

//		edgeType = et == 0 ? new EdgeShape.Line<String, String>(): new EdgeShape.Orthogonal<String, String>();
		
		
		nodes=tables;
		edges=fks;

		g = new DirectedSparseGraph<String, String>();
		episodeName=versionName;
//		episode = ep;
//		targetFolder = tf+"//screenshots";
//		new File(targetFolder ).mkdir();
		addNodes();
		addEdges();



	}
	
	public void setDetails(String tf, int et){
		
		edgeType = et == 0 ? new EdgeShape.Line<String, String>(): new EdgeShape.Orthogonal<String, String>();
		
		targetFolder = tf+"//screenshots";		
		new File(targetFolder ).mkdir();

	}
	
	public void createEpisodes(ConcurrentHashMap<String, Table> graph,Dimension universalFrame,Point2D universalCenter,double frameX,double frameY,double scaleX,double scaleY) {

		
		layout = new StaticLayout<String, String>(g);

		layout.setSize(universalFrame);
		VisualizationViewer<String, String> vv = new VisualizationViewer<String, String>(layout);
		
		vv.setSize(new Dimension(universalFrame.width+300, universalFrame.height+300));
		

		
		// Setup up a new vertex to paint transformer...
		Transformer<String, Paint> vertexPaint = new Transformer<String, Paint>() {
			public Paint transform(String v) {

				for (int i = 0; i <nodes.size(); ++i)
					if (nodes.get(i).getKey().equals(v))
						if (nodes.get(i).getColorCode() == -1)
							return new Color(129, 215, 244, 200);
						else if (nodes.get(i).getColorCode() == 0)
							return new Color(0, 255, 0, 200);
						else if (nodes.get(i).getColorCode() == 1)
							return new Color(255, 0, 0, 200);
						else
							return new Color(255, 255, 0, 200);

				return Color.BLACK;

			}
		};

		
		//gia ton kathe komvo tou kathe episode,koitaw ton universalGraph
		//gia na dw th coordinates exei hdh kai ths lockarw ston grafo
		for (int i = 0; i < nodes.size(); ++i) {
			layout.setLocation(nodes.get(i).getKey(),(graph.get(nodes.get(i).getKey()).getCoords()));
			layout.lock(nodes.get(i).getKey(), true);
		}
		
		

		MutableTransformer layoutTranformer = vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT);
		
		if(scaleX<=1 && scaleY<=1){	
			layoutTranformer.setTranslate(frameX, frameY);

			MutableTransformer scaleTranformer = vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.VIEW);
			scaleTranformer.scale(scaleX, scaleY, universalCenter);
			
		}else{
			layoutTranformer.setScale(scaleX, scaleY, universalCenter);
			layoutTranformer.setTranslate(frameX, frameY);
		}
		



		

		vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.NE);
		vv.getRenderContext().setEdgeShapeTransformer(edgeType);
		vv.setBackground(Color.WHITE);

		
		writeJPEGImage(new File(targetFolder + "/"+ episodeName + ".jpg"), vv);

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
	
	private void addNodes() {

		for (int i = 0; i <nodes.size(); ++i)
			g.addVertex(nodes.get(i).getKey());

	}

	private void addEdges() {

		for (int i = 0; i <edges.size(); ++i)
			g.addEdge(Integer.toString(i), edges.get(i)
					.getSourceTable(), edges.get(i)
					.getTargetTable());

	}


}