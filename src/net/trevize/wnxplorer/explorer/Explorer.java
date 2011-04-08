package net.trevize.wnxplorer.explorer;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import net.infonode.docking.RootWindow;
import net.infonode.docking.SplitWindow;
import net.infonode.docking.TabWindow;
import net.infonode.docking.View;
import net.infonode.docking.properties.RootWindowProperties;
import net.infonode.docking.theme.DockingWindowsTheme;
import net.infonode.docking.theme.ShapedGradientDockingTheme;
import net.infonode.docking.util.DockingUtil;
import net.infonode.docking.util.ViewMap;
import net.trevize.wnxplorer.jung.PickingGraphMousePlugin;
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

public class Explorer implements ComponentListener {

	public static final String WNXPLORER_APPLICATION_ICON_PATH = "./gfx/WNXplorer-icon81.png";

	private JFrame main_frame;
	private RootWindow root_window;
	private ViewMap view_map;
	private View[] views;
	private RootWindowProperties properties = new RootWindowProperties();
	private DockingWindowsTheme currentTheme = new ShapedGradientDockingTheme();


	private SearchPanel search_panel;
	private SynsetInfoPanel synset_info_panel;
	private WNGraphInfoPanel wngraph_info_panel;

	private WNGraph wngraph;
	private WNGraphPanel wngraph_panel;

	//for JWI.
	private IDictionary dict;

	public Explorer() {
		initWordNet();
		init();

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

		//if no wordnet installation path indicated in the properties file, show the directory selector.
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
		dict = new Dictionary(url);
		try {
			dict.open();

			//try to do a request for testing.
			dict.getSynset(new SynsetID(0, POS.NOUN));
		} catch (Exception e) {
			WNXplorerProperties.setWN_PATH("");
			JOptionPane
					.showMessageDialog(
							main_frame,
							"<html><body>The indicated <b>dict</b> directory (part of WordNet) is not readable or accessible (or not the WordNet <b>dict</b> directory).</body></html>");
			initWordNet();
		}
	}

	private void init() {
		/* setting the main frame. ********************************************/

		main_frame = new JFrame("WNXplorer");
		main_frame.setIconImage(Toolkit.getDefaultToolkit().getImage(
				WNXPLORER_APPLICATION_ICON_PATH));
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

		/* setting the application component for the GUI **********************/

		search_panel = new SearchPanel(this);

		synset_info_panel = new SynsetInfoPanel(this);

		wngraph_info_panel = new WNGraphInfoPanel();

		initGraphView();

		//instantiate the GUI
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				views = new View[4];
				view_map = new ViewMap();

				views[0] = new View("Search", null, search_panel
						.getSearch_panel());
				view_map.addView(0, views[0]);

				views[1] = new View("Synset info", null, synset_info_panel
						.getScrollpane());
				view_map.addView(1, views[1]);

				views[2] = new View("Graph", null, wngraph_info_panel);
				view_map.addView(2, views[2]);

				views[3] = new View("Graph view", null, wngraph_panel
						.getPanel());
				view_map.addView(3, views[3]);

				root_window = DockingUtil.createRootWindow(view_map, true);
				TabWindow tab_windows = new TabWindow(new View[] { views[0],
						views[1], views[2] });
				tab_windows.setSelectedTab(0);
				root_window.setWindow(new SplitWindow(true, 0.3f, tab_windows,
						views[3]));

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
				
				root_window.getRootWindowProperties().getFloatingWindowProperties().setUseFrame(true);

				main_frame.getContentPane().add(root_window,
						BorderLayout.CENTER);
			}
		});
	}

	public void initGraphView() {
		//instantiate a new WNGraph.
		wngraph = new WNGraph(dict);

		//instantiate a new WNGraphPanel.
		wngraph_panel = new WNGraphPanel(wngraph);

		/*
		 * set a new PickingGraphMousePlugin for allowing the "node picked mode".
		 * Basically there are two mode of mouse interaction with the WNGraphPanel:
		 * (1) pan mode
		 * (2) picking mode
		 * 
		 * Whatever the current mode is, if you use the left mouse button on a node, this has 
		 * for effect to display the synset properties in the synset_info_panel.
		 */

		PickingGraphMousePlugin picking_plugin = new PickingGraphMousePlugin(
				synset_info_panel, wngraph_panel, dict);
		wngraph_panel.addPickingPlugin(picking_plugin);
	}

	public void clearGraphView() {
		//setting the splitpane right component.
		initGraphView();

		//save the divider location, install the new graph view and restore the divider location.
		//		int divider_location = splitpane.getDividerLocation();
		//		splitpane.add(wngraph_panel.getPanel(), JSplitPane.RIGHT);
		//		splitpane.setDividerLocation(divider_location);
	}

	public void hideAllPopups() {
		search_panel.getPos_selector_button().hidePopup();
		wngraph_panel.getPointer_selector_button().hidePopup();
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

	public WNGraph getWngraph() {
		return wngraph;
	}

	public WNGraphPanel getWngraphp() {
		return wngraph_panel;
	}

	public IDictionary getDict() {
		return dict;
	}

	public JFrame getMain_frame() {
		return main_frame;
	}

}
