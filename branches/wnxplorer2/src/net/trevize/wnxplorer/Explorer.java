package net.trevize.wnxplorer;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import net.infonode.docking.RootWindow;
import net.infonode.docking.SplitWindow;
import net.infonode.docking.TabWindow;
import net.infonode.docking.View;
import net.infonode.docking.drop.DropFilter;
import net.infonode.docking.drop.DropInfo;
import net.infonode.docking.util.DockingUtil;
import net.infonode.docking.util.ViewMap;
import net.trevize.knetvis.KNetEdge;
import net.trevize.knetvis.KNetGraph;
import net.trevize.knetvis.KNetGraphImplementation;
import net.trevize.knetvis.KNetGraphViewer;
import net.trevize.knetvis.KNetGraphViewerImplementation;
import net.trevize.knetvis.KNetVertex;
import net.trevize.knetvis.KNetVisProperties;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.item.SynsetID;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * Explorer.java - Mar 24, 2010
 */

public class Explorer implements ComponentListener, ActionListener {

	public static final String ACTION_COMMAND_HELP = "ACTION_COMMAND_HELP";

	private JFrame main_frame;

	//Infonode components
	private RootWindow root_window;
	private ViewMap view_map;
	private View[] views;
	private static final int NUMBER_OF_VIEW = 5;
	public static final int VIEW_SEARCH = 0;
	public static final int VIEW_SYNSET_INFO = 1;
	public static final int VIEW_GRAPH = 2;
	public static final int VIEW_GRAPH_INFO = 3;
	public static final int VIEW_SATELLITE_VIEW = 4;

	/*
	//the two following objects are used to change the Infonode theme 
	private RootWindowProperties properties = new RootWindowProperties();
	private DockingWindowsTheme currentTheme = new ShapedGradientDockingTheme();
	*/

	//WNXplorer components
	private SearchPanel search_panel;
	private KNetGraph knetgraph;
	private KNetGraphViewer knetgraph_viewer;

	//for JWI, we instantiate a IDictionary, only once here
	public static IDictionary wn_jwi_dictionary;

	private HelpDialog help_dialog;

	public Explorer() {
		/*
		 * try to load the IDictionary
		 */
		initWordNet();

		/*
		 * main initialization: application components and GUI components.
		 */
		init();

		/*
		 * display the app.
		 */
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					main_frame.setVisible(true);
				}
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	private void initWordNet() {
		String wordnet_path = WNXplorerProperties.getWN_PATH();

		//if no WordNet installation path indicated in the properties file, show the directory selector.
		if (wordnet_path.equals("")) {
			GetWordNetPathDialog d = new GetWordNetPathDialog(main_frame);
			d.setVisible(true);
			wordnet_path = WNXplorerProperties.getWN_PATH();
		}

		URL url = null;
		try {
			url = new URL("file", null, wordnet_path);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		wn_jwi_dictionary = new Dictionary(url);
		try {
			wn_jwi_dictionary.open();

			//try to do a request for testing the WordNet installation path
			wn_jwi_dictionary.getSynset(new SynsetID(0, POS.NOUN));
		} catch (Exception e) {
			WNXplorerProperties.setWN_PATH("");
			JOptionPane
					.showMessageDialog(
							main_frame,
							"<html><body>The indicated <b>dict</b> directory (part of WordNet) is not readable or not accessible (or it is not the WordNet <b>dict</b> directory).</body></html>");
			initWordNet();
		}
	}

	private void initInfonodeView(View view) {
		view.getWindowProperties().getDropFilterProperties()
				.setChildDropFilter(new DropFilter() {
					@Override
					public boolean acceptDrop(DropInfo arg0) {
						return false;
					}
				});
		view.getWindowProperties().getDropFilterProperties()
				.setInsertTabDropFilter(new DropFilter() {
					@Override
					public boolean acceptDrop(DropInfo arg0) {
						return false;
					}
				});
		view.getWindowProperties().getDropFilterProperties()
				.setInteriorDropFilter(new DropFilter() {
					@Override
					public boolean acceptDrop(DropInfo arg0) {
						return false;
					}
				});
		view.getWindowProperties().getDropFilterProperties()
				.setSplitDropFilter(new DropFilter() {
					@Override
					public boolean acceptDrop(DropInfo arg0) {
						return false;
					}
				});
	}

	private void init() {
		/* setting the main frame *********************************************/

		main_frame = new JFrame("WNXplorer");
		main_frame.setIconImage(Toolkit.getDefaultToolkit().getImage(
				WNXplorerProperties.getWnxplorer_icon_path()));
		main_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main_frame
				.setSize(
						(int) (Toolkit.getDefaultToolkit().getScreenSize().width * .80),
						(int) (Toolkit.getDefaultToolkit().getScreenSize().height * .80));
		main_frame.setLocationRelativeTo(null);

		//adding the global key listener
		Toolkit.getDefaultToolkit().addAWTEventListener(
				new GlobalKeyListener(this), AWTEvent.KEY_EVENT_MASK);

		main_frame.getContentPane().setLayout(new BorderLayout());
		main_frame.getContentPane().addComponentListener(this);

		/* setting the application components *********************************/

		search_panel = new SearchPanel(this);

		initGraphView();

		/* instantiate the GUI ************************************************/

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				views = new View[NUMBER_OF_VIEW];
				view_map = new ViewMap();

				views[VIEW_SEARCH] = new View("Search", null, search_panel
						.getSearch_panel());
				initInfonodeView(views[VIEW_SEARCH]);
				view_map.addView(VIEW_SEARCH, views[VIEW_SEARCH]);

				views[VIEW_SYNSET_INFO] = new View("Synset info", null,
						knetgraph_viewer.getConceptDescriptionPanel()
								.getScrollpane());
				initInfonodeView(views[VIEW_SYNSET_INFO]);
				view_map.addView(VIEW_SYNSET_INFO, views[VIEW_SYNSET_INFO]);

				views[VIEW_GRAPH_INFO] = new View("Graph Info", null,
						knetgraph_viewer.getGraphInfoPanel().getScrollpane());
				initInfonodeView(views[VIEW_GRAPH_INFO]);
				view_map.addView(VIEW_GRAPH_INFO, views[VIEW_GRAPH_INFO]);

				views[VIEW_GRAPH] = new View("Graph view", null,
						knetgraph_viewer.getGraphViewPanel());
				initInfonodeView(views[VIEW_GRAPH]);
				view_map.addView(VIEW_GRAPH, views[VIEW_GRAPH]);

				views[VIEW_SATELLITE_VIEW] = new View("Satellite view", null,
						knetgraph_viewer.getSatelliteViewPanel());
				initInfonodeView(views[VIEW_SATELLITE_VIEW]);
				view_map.addView(VIEW_SATELLITE_VIEW,
						views[VIEW_SATELLITE_VIEW]);

				root_window = DockingUtil.createRootWindow(view_map, true);

				//the TabWindow for Search and SynsetInfo
				TabWindow tab_windows_1 = new TabWindow(new View[] {
						views[VIEW_SEARCH], views[VIEW_SYNSET_INFO],
						views[VIEW_GRAPH_INFO] });
				tab_windows_1.setSelectedTab(0);

				//the TabWindow for GraphInfo and SatelliteView
				TabWindow tab_windows_2 = new TabWindow(
						new View[] { views[VIEW_SATELLITE_VIEW] });
				tab_windows_2.setSelectedTab(0);

				SplitWindow split_window = new SplitWindow(true, 0.3f,
						new SplitWindow(false, 0.7f, tab_windows_1,
								tab_windows_2), views[VIEW_GRAPH]);

				root_window.setWindow(split_window);

				/*
				// Set gradient theme. The theme properties object is the super object
				// of our properties object, which
				// means our property value settings will override the theme values
				properties.addSuperObject(currentTheme
						.getRootWindowProperties());

				// Our properties object is the super object of the root window
				// properties object, so all property values of the
				// theme and in our property object will be used by the root window
				root_window.getRootWindowProperties().addSuperObject(properties);
				*/

				root_window.getRootWindowProperties()
						.getFloatingWindowProperties().setUseFrame(true);

				root_window.getRootWindowProperties().getTabWindowProperties()
						.getTabProperties().getTitledTabProperties()
						.setFocusMarkerEnabled(false);

				main_frame.getContentPane().add(root_window,
						BorderLayout.CENTER);
			}
		});
	}

	public void initGraphView() {
		//instantiate a new WNGraph.
		WNConceptFactory concept_factory = new WNConceptFactory();
		WNConcept.factory = concept_factory;
		knetgraph = new KNetGraphImplementation(new WNConceptFactory(),
				new WNSemanticRelationReference());

		//instantiate a new WNGraphPanel.
		knetgraph_viewer = new KNetGraphViewerImplementation(knetgraph);

		//setting the VertexShapeTransformer.
		knetgraph_viewer
				.getVisualizationViewer()
				.getRenderContext()
				.setVertexShapeTransformer(
						new WNVertexShapeSizeAspectTransformer<KNetVertex, KNetEdge>(
								knetgraph.getFilteredGraph()));

		//add the help button
		help_dialog = new HelpDialog(knetgraph_viewer.getGraphViewPanel());

		JButton help_button = new JButton(new ImageIcon(
				KNetVisProperties.getIcon_path_help()));
		help_button.setActionCommand(ACTION_COMMAND_HELP);
		help_button.addActionListener(this);
		knetgraph_viewer.getStatusBar()
				.addComponent("help button", help_button);
	}

	public View getView(int view_id) {
		return views[view_id];
	}

	/**
	 * This method is called by the GlobalKeyListener.
	 */
	public void clearGraphView() {
		initGraphView();
		getView(VIEW_GRAPH).setComponent(knetgraph_viewer.getGraphViewPanel());
		getView(VIEW_SATELLITE_VIEW).setComponent(
				knetgraph_viewer.getSatelliteViewPanel());
		refreshViews();
	}

	/**
	 * This method is called when the data model has been updated.
	 */
	public void refreshViews() {
		knetgraph_viewer.refreshViews();
	}

	public void hideAllPopups() {
		search_panel.getPos_selector_button().hidePopup();
		knetgraph_viewer.getPopupSemanticRelationButton().hidePopup();
	}

	/**
	 * copy the visible part of the graph to a file as a jpeg image
	 * @param file
	 */
	public void writeJPEGImage(File file) {
		int width = knetgraph_viewer.getVisualizationViewer().getWidth();
		int height = knetgraph_viewer.getVisualizationViewer().getHeight();

		BufferedImage bi = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = bi.createGraphics();
		knetgraph_viewer.getVisualizationViewer().paint(graphics);
		graphics.dispose();

		try {
			ImageIO.write(bi, "jpeg", file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***************************************************************************
	 * implementation of ComponentListener.
	 **************************************************************************/

	@Override
	public void componentHidden(ComponentEvent e) {
	}

	@Override
	public void componentMoved(ComponentEvent e) {
	}

	@Override
	public void componentResized(ComponentEvent e) {
		hideAllPopups();
	}

	@Override
	public void componentShown(ComponentEvent e) {
	}

	/***************************************************************************
	 * getters and setters. 
	 **************************************************************************/

	public JFrame getMain_frame() {
		return main_frame;
	}

	public KNetGraphViewer getKNetGraphViewer() {
		return knetgraph_viewer;
	}

	public KNetGraph getKnetgraph() {
		return knetgraph;
	}

	/***************************************************************************
	 * implementation of ActionListener
	 **************************************************************************/

	@Override
	public void actionPerformed(ActionEvent e) {
		String action_command = e.getActionCommand();

		if (action_command.equals(ACTION_COMMAND_HELP)) {
			help_dialog.setVisible(true);
		}
	}

}
