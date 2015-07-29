package gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSeparator;

public class MetricsChooser extends JDialog {
	private JCheckBox numberOfConnectedComponents;
	private JCheckBox numberOfEdges;
	private JCheckBox graphDiameter;
	private JCheckBox numberOfVertices;
	private JCheckBox edgeBetweenness;
	private JCheckBox vertexBetweenness;
	private JCheckBox outDegree;
	private JCheckBox inDegree;
	private JCheckBox vertexDegree;
	private JCheckBox clusteringCoefficient;
	
	
	public MetricsChooser(final Gui parent) {
		setResizable(false);
		setMinimumSize(new Dimension(450, 480));
		setMaximumSize(new Dimension(450, 485));
		getContentPane().setLayout(null);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setAlwaysOnTop(true);
		
		JLabel lblNewLabel = new JLabel("Metrics");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblNewLabel.setBounds(196, 11, 49, 17);
		getContentPane().add(lblNewLabel);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(20, 55, 404, 2);
		getContentPane().add(separator);
		
		JLabel lblNewLabel_1 = new JLabel("Vertex Properties");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_1.setBounds(20, 35, 404, 14);
		getContentPane().add(lblNewLabel_1);
		
		inDegree = new JCheckBox("Vertex InDegree");
		inDegree.setBounds(20, 67, 125, 23);
		getContentPane().add(inDegree);
		
		outDegree = new JCheckBox("Vertex OutDegree");
		outDegree.setBounds(168, 67, 157, 23);
		getContentPane().add(outDegree);
		
		vertexDegree = new JCheckBox("Vertex Degree");
		vertexDegree.setBounds(327, 67, 111, 23);
		getContentPane().add(vertexDegree);
		
		vertexBetweenness = new JCheckBox("Vertex Betweenness");
		vertexBetweenness.setBounds(20, 93, 146, 23);
		getContentPane().add(vertexBetweenness);
		
		clusteringCoefficient = new JCheckBox("Clustering Coefficient");
		clusteringCoefficient.setBounds(168, 93, 170, 23);
		getContentPane().add(clusteringCoefficient);
		
		JLabel lblNewLabel_2 = new JLabel("Edge Properties");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_2.setBounds(20, 130, 97, 14);
		getContentPane().add(lblNewLabel_2);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(20, 155, 404, 2);
		getContentPane().add(separator_1);
		
		edgeBetweenness = new JCheckBox("Edge Betweenness");
		edgeBetweenness.setBounds(20, 164, 146, 23);
		getContentPane().add(edgeBetweenness);
		
		JCheckBox chckbxNewCheckBox_6 = new JCheckBox("New check box");
		chckbxNewCheckBox_6.setEnabled(false);
		chckbxNewCheckBox_6.setBounds(168, 164, 97, 23);
		getContentPane().add(chckbxNewCheckBox_6);
		
		JCheckBox chckbxNewCheckBox_7 = new JCheckBox("New check box");
		chckbxNewCheckBox_7.setEnabled(false);
		chckbxNewCheckBox_7.setBounds(327, 164, 97, 23);
		getContentPane().add(chckbxNewCheckBox_7);
		
		JCheckBox chckbxNewCheckBox_8 = new JCheckBox("New check box");
		chckbxNewCheckBox_8.setEnabled(false);
		chckbxNewCheckBox_8.setBounds(20, 190, 97, 23);
		getContentPane().add(chckbxNewCheckBox_8);
		
		JCheckBox chckbxNewCheckBox_9 = new JCheckBox("New check box");
		chckbxNewCheckBox_9.setEnabled(false);
		chckbxNewCheckBox_9.setBounds(168, 190, 97, 23);
		getContentPane().add(chckbxNewCheckBox_9);
		
		JLabel lblNewLabel_3 = new JLabel("Graph Properties");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_3.setBounds(20, 227, 97, 14);
		getContentPane().add(lblNewLabel_3);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(20, 252, 404, 2);
		getContentPane().add(separator_2);
		
		graphDiameter = new JCheckBox("Graph Diameter");
		graphDiameter.setEnabled(true);
		graphDiameter.setBounds(20, 267, 125, 23);
		getContentPane().add(graphDiameter);
		
		numberOfVertices = new JCheckBox("# of Vertices");
		numberOfVertices.setBounds(168, 267, 125, 23);
		getContentPane().add(numberOfVertices);
		
		numberOfEdges = new JCheckBox("# of Edges");
		numberOfEdges.setBounds(327, 267, 97, 23);
		getContentPane().add(numberOfEdges);
		
		JCheckBox chckbxNewCheckBox_13 = new JCheckBox("New check box");
		chckbxNewCheckBox_13.setEnabled(false);
		chckbxNewCheckBox_13.setBounds(20, 293, 97, 23);
		getContentPane().add(chckbxNewCheckBox_13);
		
		numberOfConnectedComponents = new JCheckBox("# of Connected Components");
		numberOfConnectedComponents.setEnabled(true);
		numberOfConnectedComponents.setBounds(168, 293, 245, 23);
		getContentPane().add(numberOfConnectedComponents);
		
		JButton btnNewButton = new JButton("Calculate");
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				ArrayList<Metric_Enums> metrics = new ArrayList<Metric_Enums>();
				
				if(inDegree.isSelected())
					metrics.add(Metric_Enums.VERTEX_IN_DEGREE);
				if(outDegree.isSelected())
					metrics.add(Metric_Enums.VERTEX_OUT_DEGREE);
				if(vertexDegree.isSelected())
					metrics.add(Metric_Enums.VERTEX_DEGREE);
				if(vertexBetweenness.isSelected())
					metrics.add(Metric_Enums.VERTEX_BETWEENNESS);
				if(clusteringCoefficient.isSelected())
					metrics.add(Metric_Enums.CLUSTERING_COEFFICIENT);
				if(edgeBetweenness.isSelected())
					metrics.add(Metric_Enums.EDGE_BETWEENNESS);
				if(graphDiameter.isSelected())
					metrics.add(Metric_Enums.GRAPH_DIAMETER);
				if(numberOfVertices.isSelected())
					metrics.add(Metric_Enums.NUMBER_OF_VERTICES);
				if(numberOfEdges.isSelected())
					metrics.add(Metric_Enums.NUMBER_OF_EDGES);
				if(numberOfConnectedComponents.isSelected())
					metrics.add(Metric_Enums.NUMBER_OF_CONNECTED_COMPONENTS);
				
				dispose();

				parent.calculateMetrics(metrics);
				
			}
		});
		btnNewButton.setBounds(168, 354, 97, 23);
		getContentPane().add(btnNewButton);
		
		setLocationRelativeTo(parent);
		setVisible(true);
	}
}
