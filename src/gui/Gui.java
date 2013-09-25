package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.prefs.Preferences;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.filechooser.FileNameExtensionFilter;
import loader.GraphmlLoader;
import loader.Parser;
import model.DatabaseVersion;
import model.DiachronicGraph;
import export.EpisodeGenerator;
import export.HecateScript;
import export.PowerPointGenerator;
import export.VideoGenerator;


public class Gui extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<String> FileNames= new ArrayList<String>();
	private Gui me;
	private ArrayList<DatabaseVersion> lifetime= new ArrayList<DatabaseVersion>();
	private ArrayList<Map<String,Integer>> transitions = new ArrayList<Map<String,Integer>>();
	private GraphmlLoader savedChanges;
	private static String workspace;
	private String input;
	private String presentationName;
	private EpisodeGenerator eg;
	private DiachronicGraph wholeGraph; 
	private Component canvas;
	private final JToolBar toolBar = new JToolBar();
	private JToggleButton btnNewButton = new JToggleButton("");
	private JToggleButton btnNewButton_1 = new JToggleButton("");
	private JButton button = new JButton("");
	private JButton btnNewButton_3 = new JButton("");
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private ButtonGroup buttons = new ButtonGroup();
	private JTextField txtDropResourceFolder;
	private JPanel panel = new JPanel();
	private String targetFolder;
	private EdgeChooser edgeChooser;
	public static  Preferences prefs;






	
	public Gui(final String workspace) {
		getContentPane().setBackground(new Color(214,217,223));
		
		buttons.add(btnNewButton);
		buttons.add(btnNewButton_1);
		
		final JPopupMenu popupMenu = new JPopupMenu();
		
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(Gui.class.getResource("/icons/omega.jpg"))); 

		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Lock / Unlock ToolBar");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(toolBar.isFloatable())
					toolBar.setFloatable(false);					
				else 
					toolBar.setFloatable(true);
			}
		});
		popupMenu.add(mntmNewMenuItem_1);
		
		JSeparator separator_5 = new JSeparator();
		popupMenu.add(separator_5);
		
		JMenuItem mntmHideToolbar = new JMenuItem("Hide ToolBar");
		mntmHideToolbar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				toolBar.setVisible(false);
				
			}
		});
		popupMenu.add(mntmHideToolbar);
		
		toolBar.add(popupMenu);

		toolBar.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
						if(arg0.getButton()==3){
							
							Point point = new Point(arg0.getPoint());
							
							popupMenu.setFocusable(false);
							
							popupMenu.show(toolBar, point.x,point.y);
						}
			}
		});
		toolBar.setBackground(new Color(240,240,240));
		buttonGroup.add(btnNewButton);
		
		
		
		btnNewButton.setEnabled(false);
		btnNewButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnNewButton.setToolTipText("Pick Vertex");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				eg.setPickingMode();
				
			}
		});
		btnNewButton.setIcon(new ImageIcon(Gui.class.getResource("/icons/click.png")));
		toolBar.add(btnNewButton);
		buttonGroup.add(btnNewButton_1);
		
		btnNewButton_1.setEnabled(false);
		btnNewButton_1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnNewButton_1.setToolTipText("Translate Graph");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				eg.setTransformingMode();
				
			}
		});
		btnNewButton_1.setIcon(new ImageIcon(Gui.class.getResource("/icons/handCursor.png")));
		toolBar.add(btnNewButton_1);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setMaximumSize(new Dimension(10, 10));
		separator_2.setOrientation(SwingConstants.VERTICAL);
		toolBar.add(separator_2);
		
		JButton btnNewButton_2 = new JButton("");
		btnNewButton_2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnNewButton_2.setToolTipText("Load Parmenidian Project");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				JFileChooser fileopen = new JFileChooser();
				fileopen.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				
				 if(fileopen.showOpenDialog(me)==JFileChooser.APPROVE_OPTION){
					 try {
						loadLifetime(fileopen.getSelectedFile());
					} catch (Exception e) {
						JOptionPane.showMessageDialog(me, "Invalid Data Entry","Error",JOptionPane.ERROR_MESSAGE);
						panel.setVisible(true);
					}
				 }

			}
		});
		btnNewButton_2.setIcon(new ImageIcon(Gui.class.getResource("/icons/open-task.gif")));
		toolBar.add(btnNewButton_2);
		
		JButton btnNewButton_5 = new JButton("");
		btnNewButton_5.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnNewButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				clear();
				
			}
		});
		
		JButton btnNewButton_6 = new JButton("");
		btnNewButton_6.setToolTipText("Run Hecate Script");
		btnNewButton_6.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnNewButton_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				createTransitions();
				
			}
		});
		btnNewButton_6.setIcon(new ImageIcon(Gui.class.getResource("/icons/icon.png")));
		toolBar.add(btnNewButton_6);
		btnNewButton_5.setToolTipText("Clear");
		btnNewButton_5.setIcon(new ImageIcon(Gui.class.getResource("/icons/clear.png")));
		toolBar.add(btnNewButton_5);
		
		JSeparator separator_3 = new JSeparator();
		separator_3.setOrientation(SwingConstants.VERTICAL);
		separator_3.setMaximumSize(new Dimension(10, 10));
		toolBar.add(separator_3);
		
		button.setEnabled(false);
		button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		button.setToolTipText("Save Layout");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					wholeGraph=(DiachronicGraph) eg.saveVertexCoordinates(wholeGraph);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}
		});
		button.setIcon(new ImageIcon(Gui.class.getResource("/icons/layout.png")));
		toolBar.add(button);
		
		JButton button_1 = new JButton("");
		button_1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		button_1.setToolTipText("Produce PowerPoint Presentation");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				createPowerPointPresentation();
			}
		});
		
		btnNewButton_3.setEnabled(false);
		btnNewButton_3.setToolTipText("Create Images Of Each Version");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				visualize();
				
			}
		});
		btnNewButton_3.setIcon(new ImageIcon(Gui.class.getResource("/icons/pic.png")));
		toolBar.add(btnNewButton_3);
		button_1.setIcon(new ImageIcon(Gui.class.getResource("/icons/preview.png")));
		toolBar.add(button_1);
		
		JButton btnNewButton_7 = new JButton("");
		btnNewButton_7.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnNewButton_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					long startTime = System.nanoTime();
					createVideo();
					System.out.println(System.nanoTime() - startTime);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}
		});
		btnNewButton_7.setToolTipText("Produce Video");
		btnNewButton_7.setIcon(new ImageIcon(Gui.class.getResource("/icons/Movies.png")));
		toolBar.add(btnNewButton_7);
		
		JSeparator separator_4 = new JSeparator();
		separator_4.setOrientation(SwingConstants.VERTICAL);
		separator_4.setMaximumSize(new Dimension(10, 10));
		toolBar.add(separator_4);
		
		JButton btnNewButton_4 = new JButton("");
		btnNewButton_4.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		btnNewButton_4.setToolTipText("Change Workspace");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				changeWorkspace();
			}
		});
		btnNewButton_4.setIcon(new ImageIcon(Gui.class.getResource("/icons/workspace.gif")));
		toolBar.add(btnNewButton_4);
		panel.setLayout(null);
		
		txtDropResourceFolder = new JTextField();
		txtDropResourceFolder.setEditable(false);
		txtDropResourceFolder.setBounds(124, 494, 125, 25);
		txtDropResourceFolder.setColumns(10);
		txtDropResourceFolder.setDragEnabled(true);
		
		TransferHandler handler =   new TransferHandler() {

	        @Override
	        public boolean canImport(TransferHandler.TransferSupport info) {
	            // we only import FileList
	            return true;
	        }

	        @Override
	        public boolean importData(TransferHandler.TransferSupport info) {
	        	
	        	Transferable t = info.getTransferable();
	        	try {
	        		

	        		txtDropResourceFolder.setText(getRefinedText(t.getTransferData(DataFlavor.javaFileListFlavor).toString()));
					try {
						loadLifetime(getDnDFilename(t.getTransferData(DataFlavor.javaFileListFlavor).toString()));
//						createUniversalGraph();
					} catch (Exception e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(me, "Invalid Data Entry","Error",JOptionPane.ERROR_MESSAGE);
						panel.setVisible(true);
					}

	        		
				} catch (UnsupportedFlavorException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
	            return true;
	        }

	        private void displayDropLocation(String string) {
	            System.out.println(string);
	        }
	    };
	    txtDropResourceFolder.setTransferHandler(handler);
		
		
		panel.add(txtDropResourceFolder);
		
		
		
		
		JLabel lblDropResourceFolder = new JLabel("Quick Load:");
		lblDropResourceFolder.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblDropResourceFolder.setIcon(new ImageIcon(Gui.class.getResource("/icons/open-task.gif")));
		lblDropResourceFolder.setBounds(10, 497, 104, 18);
		panel.add(lblDropResourceFolder);
		
		JSeparator separator_6 = new JSeparator();
		separator_6.setBounds(10, 488, 990, 27);
		panel.add(separator_6);
		getContentPane().setLayout(new BorderLayout(0, 0));
		getContentPane().add(toolBar, BorderLayout.NORTH);
		getContentPane().add(panel);
		
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(Gui.class.getResource("/icons/omega.jpg")));
		me  =this;
		
		workspace.replace('\\','/');
		this.workspace=workspace;
		
		setTitle("Parmenidian Truth");
		setMinimumSize(new Dimension(1020, 600));
		setLocation(new Point(java.awt.Toolkit.getDefaultToolkit().getScreenSize().width/2-getSize().width/2,java.awt.Toolkit.getDefaultToolkit().getScreenSize().height/2-getSize().height/2));
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile_1 = new JMenu("File");
		menuBar.add(mnFile_1);
		
		JMenuItem mntmLoadDbVersion = new JMenuItem("Load Parmenidian Project");
		mntmLoadDbVersion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				JFileChooser fileopen = new JFileChooser();
				fileopen.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				
				 if(fileopen.showOpenDialog(me)==JFileChooser.APPROVE_OPTION){
				
					 
					 try {
						loadLifetime(fileopen.getSelectedFile());
//						 createUniversalGraph();
					} catch (Exception e) {
						JOptionPane.showMessageDialog(me, "Invalid Data Entry","Error",JOptionPane.ERROR_MESSAGE);
						panel.setVisible(true);
					}
					

				 }

				
			}

		});
		
		mntmLoadDbVersion.setIcon(new ImageIcon(Gui.class.getResource("/icons/open-task.gif")));
		mnFile_1.add(mntmLoadDbVersion);
		
		JMenuItem mntmNewMenuItem_2 = new JMenuItem("Run Hecate Script");
		mntmNewMenuItem_2.setIcon(new ImageIcon(Gui.class.getResource("/icons/icon.png")));
		mnFile_1.add(mntmNewMenuItem_2);
		
		JSeparator separator_1 = new JSeparator();
		mnFile_1.add(separator_1);
		
		JMenuItem mntmClear = new JMenuItem("Clear");
		mntmClear.setIcon(new ImageIcon(Gui.class.getResource("/icons/clear.png")));
		mntmClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				clear();

			}
		});
		mnFile_1.add(mntmClear);
		
		JMenu mnFile = new JMenu("Export");
		menuBar.add(mnFile);
		
		JMenuItem mntmPreview = new JMenuItem("Create Presentation");
		mntmPreview.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				s(workspace+" listener");
				createPowerPointPresentation();
				
			}
		});
		mntmPreview.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
		mntmPreview.setIcon(new ImageIcon(Gui.class.getResource("/icons/preview.png")));
		mnFile.add(mntmPreview);
		
		JMenuItem mntmCreateMovie = new JMenuItem("Produce Video [C#]");
		mntmCreateMovie.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String temp[];
				
				String executeCommand=Gui.class.getResource("/resources").toString();
				
				temp=executeCommand.split("/",2);
				s(workspace);s(presentationName);
				
				executeCommand=temp[1]+"/PPTtoALL.exe "+workspace+"/"+presentationName+".pptx "+workspace+"/"+presentationName+".wmv";
				System.out.println(executeCommand);
				
				Runtime rt = Runtime.getRuntime();
				try {
					Process p = rt.exec("cmd /c "+executeCommand);
				
				} catch (IOException e) {
					e.printStackTrace();
				}

				
			}
		});
		
		JSeparator separator_7 = new JSeparator();
		mnFile.add(separator_7);
		mntmCreateMovie.setIcon(new ImageIcon(Gui.class.getResource("/icons/movie.png")));
		mntmCreateMovie.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.CTRL_MASK));
		mnFile.add(mntmCreateMovie);
		
		JMenuItem mntmProduceVideojv = new JMenuItem("Produce Video [Jv]");
		mntmProduceVideojv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					createVideo();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		mntmProduceVideojv.setIcon(new ImageIcon(Gui.class.getResource("/icons/Movies.png")));
		mnFile.add(mntmProduceVideojv);
		
		JMenu mnLayout = new JMenu("Graph Options");
		menuBar.add(mnLayout);
		
		JMenuItem mntmSaveLayout = new JMenuItem("Save Layout");
		mntmSaveLayout.setIcon(new ImageIcon(Gui.class.getResource("/icons/layout.png")));
		mntmSaveLayout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					wholeGraph=(DiachronicGraph) eg.saveVertexCoordinates(wholeGraph);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		});
		mnLayout.add(mntmSaveLayout);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Produce Episodes");
		mntmNewMenuItem.setIcon(new ImageIcon(Gui.class.getResource("/icons/pic.png")));
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				visualize();
				
			}
		});
		mnLayout.add(mntmNewMenuItem);
		
		JMenu mnAbout = new JMenu("Preferences");
		menuBar.add(mnAbout);
		
		JMenuItem mntmAboutTheProject = new JMenuItem("About The Project");
		mntmAboutTheProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {		
			
			}
		});
		
		JMenuItem mntmChangeWorkspace = new JMenuItem("Change Workspace");
		mntmChangeWorkspace.setIcon(new ImageIcon(Gui.class.getResource("/icons/workspace.gif")));
		mntmChangeWorkspace.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				changeWorkspace();

			}
		});
		mnAbout.add(mntmChangeWorkspace);
		
		JMenuItem mntmToolbaronoff = new JMenuItem("ToolBar [On/Off]");
		mntmToolbaronoff.setIcon(new ImageIcon(Gui.class.getResource("/icons/toolbar.png")));
		mntmToolbaronoff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(toolBar.isVisible())
					toolBar.setVisible(false);
				else
					toolBar.setVisible(true);				
				
			}
		});
		mnAbout.add(mntmToolbaronoff);
		
		JSeparator separator = new JSeparator();
		mnAbout.add(separator);
		mntmAboutTheProject.setIcon(new ImageIcon(Gui.class.getResource("/icons/info.png")));
		mnAbout.add(mntmAboutTheProject);


		
	}
	
	protected void createVideo() throws IOException {
		
		FileNameExtensionFilter filter = new FileNameExtensionFilter("PowerPoint Presentation", "pptx");
		
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(filter);
		
		if(chooser.showOpenDialog(me)==JFileChooser.APPROVE_OPTION){
			
			VideoGenerator vg = new VideoGenerator(chooser.getSelectedFile());
			vg.exportToVideo();
		}
		
		
	}

	protected void createTransitions() {

		JFileChooser fileopen = new JFileChooser();
		fileopen.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		 if(fileopen.showOpenDialog(me)==JFileChooser.APPROVE_OPTION){
			 try {
				HecateScript hecate= new HecateScript(fileopen.getSelectedFile());
				hecate.createTransitions();
			} catch (Exception e) {
				JOptionPane.showMessageDialog(me, "Invalid Data Entry","Error",JOptionPane.ERROR_MESSAGE);
			}
		 }
		
	}

	protected void loadImagesForPptx() {

		JFileChooser fileopen = new JFileChooser(workspace);
		fileopen.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		if(fileopen.showOpenDialog(me)==JFileChooser.APPROVE_OPTION){
			File f = fileopen.getSelectedFile();
			presentationName=f.getName();		
		
			File dir = new File(f.getPath());				
			File[] list = dir.listFiles();
			for(int i=0;i<list.length;i++){
				FileNames.add(list[i].toString());
			}
		}
		
	}

	protected void changeWorkspace() {
		
		WorkspaceChooser c = new WorkspaceChooser(me);
		c.setVisible(true);
		
	}

	protected void s(String string) {
		
		System.out.println(string);
		
	}

	protected void createPowerPointPresentation() {
		
		loadImagesForPptx();
		
		if(FileNames.isEmpty())
			return;
		
		PowerPointGenerator pptx=new PowerPointGenerator(workspace,presentationName);
		
		try {
			pptx.createPresentation(FileNames);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			FileNames.clear();
		}
		
	}

	protected void createDiachronicGraph(int mode) {
		me.setResizable(true);
		me.setLocation(0, 0);
		
		if(mode==0)
			wholeGraph= new DiachronicGraph(lifetime);
		else
			wholeGraph = new DiachronicGraph(savedChanges);
		
		

	}

	protected void loadLifetime(File selectedFile) throws Exception {
		
		
		edgeChooser = new EdgeChooser(me);
		input=selectedFile.toString();
		String [] array= selectedFile.toString().split("\\\\");
		targetFolder=workspace+"\\"+array[array.length-1];
		
		boolean done =new File(targetFolder).mkdirs();
		int episodeGeneratorMode;
		panel.setVisible(false);

		Parser myParser;
			
			myParser = new Parser(selectedFile);
			lifetime=myParser.getLifetime();
			transitions=myParser.getTransitions();
			updateLifetimeWithTransitions();
			
			//an uparxei graphml ftiakse ton universal sumfwna me ton 
			//graph alliws ftiakston me ton default tropo
			if(myParser.isGraphml()){
				savedChanges=myParser.getGraphmlLoader();
				createDiachronicGraph(1);
				episodeGeneratorMode=1;
			}else{
				createDiachronicGraph(0);
				episodeGeneratorMode=0;
			}
			

			eg = new EpisodeGenerator(wholeGraph,input,me,targetFolder,edgeChooser.getEdgeType(),episodeGeneratorMode);
			canvas=eg.show();
			
			button.setEnabled(true);
			btnNewButton_3.setEnabled(true);
			btnNewButton.setEnabled(true);
			btnNewButton_1.setEnabled(true);
			btnNewButton.doClick();

			
	}
	
	private  void visualize() {
		
		try {
			wholeGraph=(DiachronicGraph) eg.saveVertexCoordinates(wholeGraph);
			eg.createEpisodes(wholeGraph);
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		for(int i=0;i<lifetime.size();++i){
			s(lifetime.get(i).getVersion());
			eg = new EpisodeGenerator(lifetime.get(i),me,targetFolder,edgeChooser.getEdgeType());
			eg.createEpisodes(wholeGraph);
		}
		
	}

	/**
	 * Clear the JFrame of any graph,return window to its former position
	 */
	private void clear() {
		
		eg= null;
		if(canvas==null)
			return;
					
		panel.setVisible(true);
		me.getContentPane().remove(canvas);
		me.repaint();
		me.setSize(1020, 600);
		me.setResizable(false);
		setLocation(new Point(java.awt.Toolkit.getDefaultToolkit().getScreenSize().width/2-getSize().width/2,java.awt.Toolkit.getDefaultToolkit().getScreenSize().height/2-getSize().height/2));
		button.setEnabled(false);
		btnNewButton_3.setEnabled(false);
		btnNewButton.setEnabled(false);
		btnNewButton_1.setEnabled(false);
		txtDropResourceFolder.setText("");
		
	}

	private void updateLifetimeWithTransitions(){
		
		for(int i=0;i<lifetime.size();++i)
			if(i==0)
				setFirstVersion(lifetime.get(i));
			else if(i==lifetime.size()-1)
				setFinalVersion(lifetime.get(i),i);
			else
				setIntermediateVersion(lifetime.get(i),i);
	
			
		
	}
	
	/**
	 * Trexw thn prwth version me to prwto Dictionary kai checkarw n dw an sthn
	 * 2h version exei svistei kapoios pinakas.Me endiaferei mono to deletion
	 * An kapoioi exoun ginei updated tha tous vapsw sthn 2h ekdosh,oxi edw
	 * @param fversion :firstVersion
	 */
	private void setFirstVersion(DatabaseVersion fversion){
		
		for(int i=0;i<fversion.getTables().size();++i)
			if(transitions.get(0).containsKey(fversion.getTables().get(i).getKey())
			&& transitions.get(0).get(fversion.getTables().get(i).getKey())==1)
				fversion.getTables().get(i).setColorCode(1);		
		
	}
	
	/**
	 * Trexw thn teleutaia version mou me to teleutaio dictionary mou,h thesh tou
	 * teleutaiou dictionary mou einai mia prin apo thn thesh ths teleutaias version mou.
	 * Psaxnw gia tables pou periexontai st dictionary mou KAI DEN einai deletions,einai 
	 * dhladh mono newTable kai UpdateTable kai tous vafw analoga me thn timh pou exei to
	 * dictionary mou.
	 * @param fversion :finalVersion
	 * @param k :H thesh ths teleutaias Version mou sthn Lista
	 */
	private void setFinalVersion(DatabaseVersion fversion,int k){
		
		for(int i=0;i<fversion.getTables().size();++i)
			if(transitions.get(k-1).containsKey(fversion.getTables().get(i).getKey())
			&& transitions.get(k-1).get(fversion.getTables().get(i).getKey())!=1)
				fversion.getTables().get(i).setColorCode(transitions.get(k-1).get(fversion.getTables().get(i).getKey()));
		
	}
	
	private void setIntermediateVersion(DatabaseVersion version,int k){
		
		for(int i=0;i<version.getTables().size();++i){
			//koitaw to mellontiko m dictionary
			if(transitions.get(k).containsKey(version.getTables().get(i).getKey())
			&& transitions.get(k).get(version.getTables().get(i).getKey())==1)
				version.getTables().get(i).setColorCode(1);
			
			//koitaw to palho m dictionary
			if(transitions.get(k-1).containsKey(version.getTables().get(i).getKey())
			&& transitions.get(k-1).get(version.getTables().get(i).getKey())!=1)
				version.getTables().get(i).setColorCode(transitions.get(k-1).get(version.getTables().get(i).getKey()));
		
				
		}
	}
	
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					try {
					    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
					        if ("Nimbus".equals(info.getName())) {
						  UIManager.setLookAndFeel(info.getClassName());
						  break;
					        }
					    }
					} catch (Exception e) {
					    
					}
					initiate();
				} catch (Exception e) {
					WorkspaceChooser c = new WorkspaceChooser();
					c.setVisible(true);
				}
			}
		});
	}
	
	private static void initiate() throws FileNotFoundException{
		
		
		prefs = Preferences.userRoot().node("preferences");
		System.out.println(prefs.getBoolean("useDefault", false));
		if(prefs.getBoolean("useDefault", false)){
			Gui gui = new Gui(retrieveSelectedWorkspace());
			gui.setVisible(true);
		}else{
			WorkspaceChooser c = new WorkspaceChooser();
			c.setVisible(true);
		}
		
	}
	
	private static String retrieveSelectedWorkspace() throws FileNotFoundException{
		
		String temp[];		
		String str=Gui.class.getResource("/resources").toString();
		
		temp=str.split("/",2);
		
		FileReader fr = null;
			fr = new FileReader(temp[1]+"/log.ascii");
			
        Scanner scanner = new Scanner(fr);
        
        while(scanner.hasNextLine())        	
        	return scanner.nextLine().toString();
        
		return null;
	}
	
	protected String getRefinedText(String str) {
		
		String [] array=str.split("\\[",2);
		array=array[1].split("\\]",2);
		return array[0].split("\\\\")[array[0].split("\\\\").length-1];
	}
	
	
	protected File getDnDFilename(String str) {

		String [] array=str.split("\\[",2);
		array=array[1].split("\\]",2);
		
		return new File(array[0]);
	}
}
