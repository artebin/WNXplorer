package net.trevize.wnxplorer;

import java.awt.AWTEvent;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;

import javax.imageio.ImageIO;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import net.infonode.docking.DockingWindow;
import net.infonode.docking.DockingWindowListener;
import net.infonode.docking.OperationAbortedException;
import net.infonode.docking.RootWindow;
import net.infonode.docking.SplitWindow;
import net.infonode.docking.TabWindow;
import net.infonode.docking.View;
import net.infonode.docking.drop.DropFilter;
import net.infonode.docking.drop.DropInfo;
import net.infonode.docking.drop.InteriorDropInfo;
import net.infonode.docking.properties.RootWindowProperties;
import net.infonode.docking.theme.DockingWindowsTheme;
import net.infonode.docking.util.DockingUtil;
import net.infonode.docking.util.ViewMap;
import net.infonode.util.Direction;
import net.trevize.knetvis.KNetEdge;
import net.trevize.knetvis.KNetGraph;
import net.trevize.knetvis.KNetGraphImplementation;
import net.trevize.knetvis.KNetGraphViewer;
import net.trevize.knetvis.KNetGraphViewerImplementation;
import net.trevize.knetvis.KNetVertex;
import net.trevize.wnxplorer.dialogs.AboutDialog;
import net.trevize.wnxplorer.dialogs.GetWordNetPathDialog;
import net.trevize.wnxplorer.dialogs.HelpDialog;
import net.trevize.wnxplorer.jwiknetvis.JWIUtils;
import net.trevize.wnxplorer.jwiknetvis.JWIResource;

import org.apache.commons.collections15.Transformer;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * Explorer.java - Mar 24, 2010
 */

public class Explorer implements ComponentListener, ActionListener {

	public static final String ACTION_COMMAND_EXPORT_AS_JPG = "ACTION_COMMAND_EXPORT_AS_JPG";
	public static final String ACTION_COMMAND_ABOUT = "ACTION_COMMAND_ABOUT";
	public static final String ACTION_COMMAND_HELP = "ACTION_COMMAND_HELP";
	public static final String ACTION_COMMAND_EXIT = "ACTION_COMMAND_EXIT";

	private JFrame main_frame;

	//Infonode components
	private RootWindow root_window;
	private ViewMap view_map;
	private View[] views;

	public static final int NUMBER_OF_VIEW = 5;
	public static final int VIEW_SEARCH = 0;
	public static final int VIEW_SYNSET_DESCRIPTION = 1;
	public static final int VIEW_GRAPH = 2;
	public static final int VIEW_GRAPH_INFO = 3;
	public static final int VIEW_SATELLITE_VIEW = 4;
	public static final String[] VIEWS_TITLE = new String[] { "Search",
			"Synset description", "Graph view", "Graph info", "Satellite view" };

	//WNXplorer components
	private SearchPanel search_panel;
	private KNetGraph knetgraph;
	private KNetGraphViewer knetgraph_viewer;

	private AboutDialog about_dialog;
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

		//set the Infonode theme
		//setInfonodeTheme(new ShapedGradientDockingTheme());
		try {
			setInfonodeTheme((DockingWindowsTheme) Class.forName(
					WNXplorerProperties.getDocking_windows_theme())
					.newInstance());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void initWordNet() {
		String wn_dict_path = WNXplorerProperties.getWordnet_dict_path();

		//if no WordNet installation path indicated in the properties file, show the directory selector.
		if (wn_dict_path.equals("")) {
			GetWordNetPathDialog d = new GetWordNetPathDialog(main_frame);
			d.setVisible(true);
			wn_dict_path = WNXplorerProperties.getWordnet_dict_path();
		}

		JWIUtils.setWN_dict_path(wn_dict_path);
		if (JWIUtils.getWN_JWI_dictionary() == null) {
			WNXplorerProperties.setWordnet_dict_path("");
			JFrame dummy_frame = new JFrame();
			dummy_frame.setIconImage(Toolkit.getDefaultToolkit().getImage(
					WNXplorerProperties.getIcon_path_wnxplorer()));
			JOptionPane
					.showMessageDialog(
							main_frame,
							"<html><body>The indicated <b>dict</b> directory (part of WordNet) is not readable or not accessible (or it is not the WordNet <b>dict</b> directory).</body></html>");
			dummy_frame.dispose();
			initWordNet();
		}
	}

	private void initInfonodeView(View view) {
		//		view.getWindowProperties().getDropFilterProperties()
		//				.setChildDropFilter(new DropFilter() {
		//					@Override
		//					public boolean acceptDrop(DropInfo arg0) {
		//						return false;
		//					}
		//				});
		//		view.getWindowProperties().getDropFilterProperties()
		//				.setInsertTabDropFilter(new DropFilter() {
		//					@Override
		//					public boolean acceptDrop(DropInfo arg0) {
		//						return false;
		//					}
		//				});
		//		view.getWindowProperties().getDropFilterProperties()
		//				.setInteriorDropFilter(new DropFilter() {
		//					@Override
		//					public boolean acceptDrop(DropInfo arg0) {
		//						return false;
		//					}
		//				});
		//		view.getWindowProperties().getDropFilterProperties()
		//				.setSplitDropFilter(new DropFilter() {
		//					@Override
		//					public boolean acceptDrop(DropInfo arg0) {
		//						return false;
		//					}
		//				});
	}

	private void init() {
		/* setting the main frame *********************************************/

		main_frame = new JFrame("WNXplorer");
		main_frame.setIconImage(Toolkit.getDefaultToolkit().getImage(
				WNXplorerProperties.getIcon_path_wnxplorer()));
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

				/* Search view ************************************************/

				views[VIEW_SEARCH] = new View(VIEWS_TITLE[VIEW_SEARCH], null,
						search_panel.getSearch_panel());
				initInfonodeView(views[VIEW_SEARCH]);
				view_map.addView(VIEW_SEARCH, views[VIEW_SEARCH]);

				/* Synset description view ************************************/

				views[VIEW_SYNSET_DESCRIPTION] = new View(
						VIEWS_TITLE[VIEW_SYNSET_DESCRIPTION], null,
						knetgraph_viewer.getConceptDescriptionPanel()
								.getScrollpane());
				initInfonodeView(views[VIEW_SYNSET_DESCRIPTION]);
				view_map.addView(VIEW_SYNSET_DESCRIPTION,
						views[VIEW_SYNSET_DESCRIPTION]);

				/* Graph info view ********************************************/

				views[VIEW_GRAPH_INFO] = new View(VIEWS_TITLE[VIEW_GRAPH_INFO],
						null, knetgraph_viewer.getGraphInfoPanel()
								.getScrollpane());
				initInfonodeView(views[VIEW_GRAPH_INFO]);
				view_map.addView(VIEW_GRAPH_INFO, views[VIEW_GRAPH_INFO]);

				/* Graph view *************************************************/

				views[VIEW_GRAPH] = new View(VIEWS_TITLE[VIEW_GRAPH], null,
						knetgraph_viewer.getGraphViewPanel());
				initInfonodeView(views[VIEW_GRAPH]);
				view_map.addView(VIEW_GRAPH, views[VIEW_GRAPH]);

				/* Satellite view *********************************************/

				views[VIEW_SATELLITE_VIEW] = new View(
						VIEWS_TITLE[VIEW_SATELLITE_VIEW], null,
						knetgraph_viewer.getSatelliteViewPanel());
				initInfonodeView(views[VIEW_SATELLITE_VIEW]);
				view_map.addView(VIEW_SATELLITE_VIEW,
						views[VIEW_SATELLITE_VIEW]);

				//now adding the views to the root window

				root_window = DockingUtil.createRootWindow(view_map, true);
				root_window.getRootWindowProperties().setRecursiveTabsEnabled(
						false);
				root_window.getWindowBar(Direction.RIGHT).setEnabled(true);
				root_window.getWindowBar(Direction.RIGHT).setContentPanelSize(256);

				root_window.getRootWindowProperties()
						.getDockingWindowProperties().getDropFilterProperties()
						.setInteriorDropFilter(new DropFilter() {
							public boolean acceptDrop(DropInfo dropInfo) {
								InteriorDropInfo interiorDropInfo = (InteriorDropInfo) dropInfo;
								// If the drop window is a split window and the drag window is a
								// tab window, no drops are allowed
								if (interiorDropInfo.getDropWindow() instanceof SplitWindow
										&& interiorDropInfo.getWindow() instanceof TabWindow)
									return false;
								return true;
							}
						});

				//now creates tabwindows and splitwindows for the frame layout

				//the TabWindow for Search
				TabWindow tab_windows_1 = new TabWindow(
						new View[] { views[VIEW_SEARCH] });
				tab_windows_1.setSelectedTab(0);

				//the TabWindow for SatelliteView
				TabWindow tab_windows_2 = new TabWindow(
						new View[] { views[VIEW_SATELLITE_VIEW] });
				tab_windows_2.setSelectedTab(0);

				//the TabWindow for ConceptDescriptionPanel and GraphInfoPanel
				TabWindow tab_windows_3 = new TabWindow(
						new View[] { views[VIEW_SYNSET_DESCRIPTION],
								views[VIEW_GRAPH_INFO] });
				tab_windows_3.setSelectedTab(0);

				SplitWindow split_window = new SplitWindow(true, 0.2f,
						new SplitWindow(false, 0.7f, tab_windows_1,
								tab_windows_2), new SplitWindow(true, 0.75f,
								views[VIEW_GRAPH], tab_windows_3));

				root_window.setWindow(split_window);

				//removing the ugly border on the InfoNode default theme
				root_window.getRootWindowProperties().getWindowAreaProperties()
						.setBorder(new EmptyBorder(0, 0, 0, 0));

				root_window.getRootWindowProperties()
						.getFloatingWindowProperties().setUseFrame(true);

				root_window.getRootWindowProperties().getTabWindowProperties()
						.getTabProperties().getTitledTabProperties()
						.setFocusMarkerEnabled(false);

				views[VIEW_SYNSET_DESCRIPTION].minimize();
				views[VIEW_GRAPH_INFO].minimize();

				main_frame.getContentPane().add(root_window,
						BorderLayout.CENTER);
			}
		});

		//initialize the dialogs
		about_dialog = new AboutDialog(main_frame.getRootPane());
		help_dialog = new HelpDialog(main_frame.getRootPane());

		//initialize the menu bar
		initApplicationMenuBar();
	}

	public void initGraphView() {
		//instantiate a new WNGraph
		knetgraph = new KNetGraphImplementation(JWIResource.getResource());

		//instantiate a new WNGraphPanel
		knetgraph_viewer = new KNetGraphViewerImplementation(knetgraph);

		//setting a VertexShapeTransformer
		knetgraph_viewer
				.getVisualizationViewer()
				.getRenderContext()
				.setVertexShapeTransformer(
						new WNVertexShapeSizeAspectTransformer<KNetVertex, KNetEdge>(
								knetgraph.getFilteredGraph()));

		//setting an EdgeStrokeTrsnaformer
		knetgraph_viewer.getVisualizationViewer().getRenderContext()
				.setEdgeStrokeTransformer(new Transformer<KNetEdge, Stroke>() {
					@Override
					public Stroke transform(KNetEdge edge) {
						return new BasicStroke(2f);
					}
				});
	}

	private void initApplicationMenuBar() {
		JMenuBar menu_bar = new JMenuBar();
		main_frame.setJMenuBar(menu_bar);

		/* File menu **********************************************************/

		JMenu menu1 = new JMenu("File");
		menu1.setMnemonic('F');
		menu_bar.add(menu1);

		JMenuItem item7 = new JMenuItem("Export graph as JPG");
		item7.setMnemonic('J');
		item7.setActionCommand(ACTION_COMMAND_EXPORT_AS_JPG);
		item7.addActionListener(this);
		menu1.add(item7);

		JMenuItem item8 = new JMenuItem("Export graph as GraphML");
		item8.setMnemonic('G');
		item8.setDisplayedMnemonicIndex(16);
		menu1.add(item8);

		JMenuItem item10 = new JMenuItem("apply TreeLayout");
		item10.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				((KNetGraphViewerImplementation) knetgraph_viewer)
						.applyTreeLayout();
			}
		});
		menu1.add(item10);

		menu1.add(new JSeparator());

		JMenuItem item6 = new JMenuItem("Exit");
		item6.setMnemonic('x');
		item6.setActionCommand(ACTION_COMMAND_EXIT);
		item6.addActionListener(this);
		menu1.add(item6);

		/* Views menu *********************************************************/

		JMenu menu2 = new JMenu("Views");
		menu2.setMnemonic('V');
		menu_bar.add(menu2);

		final JCheckBox item2 = new JCheckBox(VIEWS_TITLE[VIEW_SEARCH]);
		item2.setBorder(new JMenuItem().getBorder());
		item2.setSelected(true);
		item2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!item2.isSelected()) {
					views[VIEW_SEARCH].close();
				} else {
					views[VIEW_SEARCH].restore();
				}
			}
		});
		views[VIEW_SEARCH].addListener(new DockingWindowListener() {
			@Override
			public void windowUndocking(DockingWindow arg0)
					throws OperationAbortedException {
			}

			@Override
			public void windowUndocked(DockingWindow arg0) {
			}

			@Override
			public void windowShown(DockingWindow arg0) {
			}

			@Override
			public void windowRestoring(DockingWindow arg0)
					throws OperationAbortedException {
			}

			@Override
			public void windowRestored(DockingWindow arg0) {
			}

			@Override
			public void windowRemoved(DockingWindow arg0, DockingWindow arg1) {
			}

			@Override
			public void windowMinimizing(DockingWindow arg0)
					throws OperationAbortedException {
			}

			@Override
			public void windowMinimized(DockingWindow arg0) {
			}

			@Override
			public void windowMaximizing(DockingWindow arg0)
					throws OperationAbortedException {
			}

			@Override
			public void windowMaximized(DockingWindow arg0) {
			}

			@Override
			public void windowHidden(DockingWindow arg0) {
			}

			@Override
			public void windowDocking(DockingWindow arg0)
					throws OperationAbortedException {
			}

			@Override
			public void windowDocked(DockingWindow arg0) {
			}

			@Override
			public void windowClosing(DockingWindow arg0)
					throws OperationAbortedException {
			}

			@Override
			public void windowClosed(DockingWindow arg0) {
				item2.setSelected(false);
			}

			@Override
			public void windowAdded(DockingWindow arg0, DockingWindow arg1) {
			}

			@Override
			public void viewFocusChanged(View arg0, View arg1) {
			}
		});
		menu2.add(item2);

		/**********************************************************************/

		final JCheckBox item11 = new JCheckBox(VIEWS_TITLE[VIEW_GRAPH]);
		item11.setBorder(new JMenuItem().getBorder());
		item11.setSelected(true);
		item11.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!item11.isSelected()) {
					views[VIEW_GRAPH].close();
				} else {
					views[VIEW_GRAPH].restore();
				}
			}
		});
		views[VIEW_GRAPH].addListener(new DockingWindowListener() {
			@Override
			public void windowUndocking(DockingWindow arg0)
					throws OperationAbortedException {
			}

			@Override
			public void windowUndocked(DockingWindow arg0) {
			}

			@Override
			public void windowShown(DockingWindow arg0) {
			}

			@Override
			public void windowRestoring(DockingWindow arg0)
					throws OperationAbortedException {
			}

			@Override
			public void windowRestored(DockingWindow arg0) {
			}

			@Override
			public void windowRemoved(DockingWindow arg0, DockingWindow arg1) {
			}

			@Override
			public void windowMinimizing(DockingWindow arg0)
					throws OperationAbortedException {
			}

			@Override
			public void windowMinimized(DockingWindow arg0) {
			}

			@Override
			public void windowMaximizing(DockingWindow arg0)
					throws OperationAbortedException {
			}

			@Override
			public void windowMaximized(DockingWindow arg0) {
			}

			@Override
			public void windowHidden(DockingWindow arg0) {
			}

			@Override
			public void windowDocking(DockingWindow arg0)
					throws OperationAbortedException {
			}

			@Override
			public void windowDocked(DockingWindow arg0) {
			}

			@Override
			public void windowClosing(DockingWindow arg0)
					throws OperationAbortedException {
			}

			@Override
			public void windowClosed(DockingWindow arg0) {
				item11.setSelected(false);
			}

			@Override
			public void windowAdded(DockingWindow arg0, DockingWindow arg1) {
			}

			@Override
			public void viewFocusChanged(View arg0, View arg1) {
			}
		});
		menu2.add(item11);

		/**********************************************************************/

		final JCheckBox item3 = new JCheckBox(
				VIEWS_TITLE[VIEW_SYNSET_DESCRIPTION]);
		item3.setBorder(new JMenuItem().getBorder());
		item3.setSelected(true);
		item3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!item3.isSelected()) {
					views[VIEW_SYNSET_DESCRIPTION].close();
				} else {
					views[VIEW_SYNSET_DESCRIPTION].restore();
				}
			}
		});
		views[VIEW_SYNSET_DESCRIPTION].addListener(new DockingWindowListener() {
			@Override
			public void windowUndocking(DockingWindow arg0)
					throws OperationAbortedException {
			}

			@Override
			public void windowUndocked(DockingWindow arg0) {
			}

			@Override
			public void windowShown(DockingWindow arg0) {
			}

			@Override
			public void windowRestoring(DockingWindow arg0)
					throws OperationAbortedException {
			}

			@Override
			public void windowRestored(DockingWindow arg0) {
			}

			@Override
			public void windowRemoved(DockingWindow arg0, DockingWindow arg1) {
			}

			@Override
			public void windowMinimizing(DockingWindow arg0)
					throws OperationAbortedException {
			}

			@Override
			public void windowMinimized(DockingWindow arg0) {
			}

			@Override
			public void windowMaximizing(DockingWindow arg0)
					throws OperationAbortedException {
			}

			@Override
			public void windowMaximized(DockingWindow arg0) {
			}

			@Override
			public void windowHidden(DockingWindow arg0) {
			}

			@Override
			public void windowDocking(DockingWindow arg0)
					throws OperationAbortedException {
			}

			@Override
			public void windowDocked(DockingWindow arg0) {
			}

			@Override
			public void windowClosing(DockingWindow arg0)
					throws OperationAbortedException {
			}

			@Override
			public void windowClosed(DockingWindow arg0) {
				item3.setSelected(false);
			}

			@Override
			public void windowAdded(DockingWindow arg0, DockingWindow arg1) {
			}

			@Override
			public void viewFocusChanged(View arg0, View arg1) {
			}
		});
		menu2.add(item3);

		/**********************************************************************/

		final JCheckBox item5 = new JCheckBox(VIEWS_TITLE[VIEW_GRAPH_INFO]);
		item5.setBorder(new JMenuItem().getBorder());
		item5.setSelected(true);
		item5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!item5.isSelected()) {
					views[VIEW_GRAPH_INFO].close();
				} else {
					views[VIEW_GRAPH_INFO].restore();
				}
			}
		});
		views[VIEW_GRAPH_INFO].addListener(new DockingWindowListener() {
			@Override
			public void windowUndocking(DockingWindow arg0)
					throws OperationAbortedException {
			}

			@Override
			public void windowUndocked(DockingWindow arg0) {
			}

			@Override
			public void windowShown(DockingWindow arg0) {
			}

			@Override
			public void windowRestoring(DockingWindow arg0)
					throws OperationAbortedException {
			}

			@Override
			public void windowRestored(DockingWindow arg0) {
			}

			@Override
			public void windowRemoved(DockingWindow arg0, DockingWindow arg1) {
			}

			@Override
			public void windowMinimizing(DockingWindow arg0)
					throws OperationAbortedException {
			}

			@Override
			public void windowMinimized(DockingWindow arg0) {
			}

			@Override
			public void windowMaximizing(DockingWindow arg0)
					throws OperationAbortedException {
			}

			@Override
			public void windowMaximized(DockingWindow arg0) {
			}

			@Override
			public void windowHidden(DockingWindow arg0) {
			}

			@Override
			public void windowDocking(DockingWindow arg0)
					throws OperationAbortedException {
			}

			@Override
			public void windowDocked(DockingWindow arg0) {
			}

			@Override
			public void windowClosing(DockingWindow arg0)
					throws OperationAbortedException {
			}

			@Override
			public void windowClosed(DockingWindow arg0) {
				item5.setSelected(false);
			}

			@Override
			public void windowAdded(DockingWindow arg0, DockingWindow arg1) {
			}

			@Override
			public void viewFocusChanged(View arg0, View arg1) {
			}
		});
		menu2.add(item5);

		/**********************************************************************/

		final JCheckBox item4 = new JCheckBox(VIEWS_TITLE[VIEW_SATELLITE_VIEW]);
		item4.setBorder(new JMenuItem().getBorder());
		item4.setSelected(true);
		item4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!item4.isSelected()) {
					views[VIEW_SATELLITE_VIEW].close();
				} else {
					views[VIEW_SATELLITE_VIEW].restore();
				}
			}
		});
		views[VIEW_SATELLITE_VIEW].addListener(new DockingWindowListener() {
			@Override
			public void windowUndocking(DockingWindow arg0)
					throws OperationAbortedException {
			}

			@Override
			public void windowUndocked(DockingWindow arg0) {
			}

			@Override
			public void windowShown(DockingWindow arg0) {
			}

			@Override
			public void windowRestoring(DockingWindow arg0)
					throws OperationAbortedException {
			}

			@Override
			public void windowRestored(DockingWindow arg0) {
			}

			@Override
			public void windowRemoved(DockingWindow arg0, DockingWindow arg1) {
			}

			@Override
			public void windowMinimizing(DockingWindow arg0)
					throws OperationAbortedException {
			}

			@Override
			public void windowMinimized(DockingWindow arg0) {
			}

			@Override
			public void windowMaximizing(DockingWindow arg0)
					throws OperationAbortedException {
			}

			@Override
			public void windowMaximized(DockingWindow arg0) {
			}

			@Override
			public void windowHidden(DockingWindow arg0) {
			}

			@Override
			public void windowDocking(DockingWindow arg0)
					throws OperationAbortedException {
			}

			@Override
			public void windowDocked(DockingWindow arg0) {
			}

			@Override
			public void windowClosing(DockingWindow arg0)
					throws OperationAbortedException {
			}

			@Override
			public void windowClosed(DockingWindow arg0) {
				item4.setSelected(false);
			}

			@Override
			public void windowAdded(DockingWindow arg0, DockingWindow arg1) {
			}

			@Override
			public void viewFocusChanged(View arg0, View arg1) {
			}
		});
		menu2.add(item4);

		/* Help menu **********************************************************/

		JMenu menu3 = new JMenu("Help");
		menu3.setMnemonic('H');
		menu_bar.add(menu3);

		JMenuItem item1 = new JMenuItem("About");
		item1.setMnemonic('A');
		item1.setActionCommand(ACTION_COMMAND_ABOUT);
		item1.addActionListener(this);
		menu3.add(item1);

		JMenuItem item9 = new JMenuItem("Help");
		item9.setMnemonic('H');
		item9.setActionCommand(ACTION_COMMAND_HELP);
		item9.addActionListener(this);
		menu3.add(item9);
	}

	public void setInfonodeTheme(DockingWindowsTheme docking_windows_theme) {
		RootWindowProperties properties = new RootWindowProperties();

		// Set gradient theme. The theme properties object is the super object
		// of our properties object, which
		// means our property value settings will override the theme values
		properties.addSuperObject(docking_windows_theme
				.getRootWindowProperties());

		// Our properties object is the super object of the root window
		// properties object, so all property values of the
		// theme and in our property object will be used by the root window
		root_window.getRootWindowProperties().addSuperObject(properties);
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

		getView(VIEW_SYNSET_DESCRIPTION).setComponent(
				knetgraph_viewer.getConceptDescriptionPanel().getScrollpane());

		getView(VIEW_GRAPH_INFO).setComponent(
				knetgraph_viewer.getGraphInfoPanel().getScrollpane());

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

	public KNetGraph getKnetGraph() {
		return knetgraph;
	}

	/***************************************************************************
	 * implementation of ActionListener
	 **************************************************************************/

	@Override
	public void actionPerformed(ActionEvent e) {
		String action_command = e.getActionCommand();

		if (action_command.equals(ACTION_COMMAND_EXPORT_AS_JPG)) {
			writeJPEGImage(new File("./"
					+ Calendar.getInstance().getTimeInMillis() + ".jpg"));
		}

		else

		if (action_command.equals(ACTION_COMMAND_ABOUT)) {
			about_dialog.setVisible(true);
		}

		else

		if (action_command.equals(ACTION_COMMAND_HELP)) {
			help_dialog.setVisible(true);
		}

		else

		if (action_command.equals(ACTION_COMMAND_EXIT)) {
			System.exit(0);
		}
	}

}
