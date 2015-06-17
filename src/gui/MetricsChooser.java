package gui;

import javax.swing.JDialog;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JSeparator;
import javax.swing.JCheckBox;
import javax.swing.JButton;

import java.awt.Dimension;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MetricsChooser extends JDialog {
	
	public interface setMethods{
		
		public void setDegree(boolean b);
		public void setInDegree(boolean b);
		public void setOutDegree(boolean b);
		public void setVertexBetweeness(boolean b);
		public void setEdgeBetweeness(boolean b);
		public void setGraphVertices(boolean b);
		public void setGraphEdges(boolean b);
		public void setGraphDiameter(boolean b);
		public void calculateMetrics();
		
	}
	
	
	public MetricsChooser(Gui parent) {
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
		
		JCheckBox inDegree = new JCheckBox("Vertex InDegree");
		inDegree.setBounds(20, 67, 125, 23);
		getContentPane().add(inDegree);
		
		JCheckBox outDegree = new JCheckBox("Vertex OutDegree");
		outDegree.setBounds(168, 67, 157, 23);
		getContentPane().add(outDegree);
		
		JCheckBox vertexDegree = new JCheckBox("Vertex Degree");
		vertexDegree.setBounds(327, 67, 111, 23);
		getContentPane().add(vertexDegree);
		
		JCheckBox vertexBetweenness = new JCheckBox("Vertex Betweenness");
		vertexBetweenness.setBounds(20, 93, 146, 23);
		getContentPane().add(vertexBetweenness);
		
		JCheckBox chckbxNewCheckBox_4 = new JCheckBox("New check box");
		chckbxNewCheckBox_4.setEnabled(false);
		chckbxNewCheckBox_4.setBounds(168, 93, 97, 23);
		getContentPane().add(chckbxNewCheckBox_4);
		
		JLabel lblNewLabel_2 = new JLabel("Edge Properties");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_2.setBounds(20, 130, 97, 14);
		getContentPane().add(lblNewLabel_2);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(20, 155, 404, 2);
		getContentPane().add(separator_1);
		
		JCheckBox edgeBetweenness = new JCheckBox("Edge Betweenness");
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
		
		JCheckBox graphDiameter = new JCheckBox("Graph Diameter");
		graphDiameter.setBounds(20, 267, 125, 23);
		getContentPane().add(graphDiameter);
		
		JCheckBox graphVertices = new JCheckBox("Graph Vertices");
		graphVertices.setBounds(168, 267, 125, 23);
		getContentPane().add(graphVertices);
		
		JCheckBox graphEdges = new JCheckBox("Graph Edges");
		graphEdges.setBounds(327, 267, 97, 23);
		getContentPane().add(graphEdges);
		
		JCheckBox chckbxNewCheckBox_13 = new JCheckBox("New check box");
		chckbxNewCheckBox_13.setEnabled(false);
		chckbxNewCheckBox_13.setBounds(20, 293, 97, 23);
		getContentPane().add(chckbxNewCheckBox_13);
		
		JCheckBox chckbxNewCheckBox_14 = new JCheckBox("New check box");
		chckbxNewCheckBox_14.setEnabled(false);
		chckbxNewCheckBox_14.setBounds(168, 293, 97, 23);
		getContentPane().add(chckbxNewCheckBox_14);
		
		JButton btnNewButton = new JButton("Calculate");
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				dispose();
				parent.setDegree(vertexDegree.isSelected());
				parent.setInDegree(inDegree.isSelected());
				parent.setOutDegree(outDegree.isSelected());
				parent.setVertexBetweeness(vertexBetweenness.isSelected());
				parent.setEdgeBetweeness(edgeBetweenness.isSelected());
				parent.setGraphVertices(graphVertices.isSelected());
				parent.setGraphEdges(graphEdges.isSelected());
				parent.setGraphDiameter(graphDiameter.isSelected());
				parent.calculateMetrics();
				
				
			}
		});
		btnNewButton.setBounds(168, 354, 97, 23);
		getContentPane().add(btnNewButton);
		
		setLocationRelativeTo(parent);
		setVisible(true);
	}
}
