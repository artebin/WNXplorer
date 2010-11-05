package net.trevize.wnxplorer.explorer;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.border.MatteBorder;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import net.trevize.wnxplorer.jung.PickingGraphMousePlugin;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * Explorer.java - Mar 24, 2010
 */

public class Explorer implements ComponentListener {

	public static final String WNXPLORER_APPLICATION_ICON_PATH = "./gfx/WNXplorer-icon.png";

	private JFrame main_frame;
	private JSplitPane splitpane;

	//splitpane left component.
	private JTabbedPane tabbedpane;
	private SearchPanel search_panel;
	private SynsetInfoPanel synset_info_panel;
	private WNGraphInfoPanel wngraph_info_panel;

	//splitpane right component.
	private WNGraph wngraph;
	private WNGraphPanel wngraph_panel;

	//for JWI.
	private IDictionary dict;

	public Explorer() {
		initWordNet();
		init();

		main_frame.setVisible(true);

		splitpane.setDividerLocation(.25); //have to be done after to switch the frame visibility to be taken in account.
	}

	private void initWordNet() {
		String wordnet_path = WNXplorerProperties.getWN_PATH();

		//if no wordnet installation path indicated in the properties file, show the directory selector.
		if (wordnet_path.equals("")) {
			GetWordNetPathDialog d = new GetWordNetPathDialog(splitpane);
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
		dict.open();
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

		/* setting the JSplitPane *********************************************/

		splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

		/*
		 * setting the divider of the JSplitPane (due to the bug of the GTK PLAF). 
		 */
		splitpane.setUI(new BasicSplitPaneUI() {
			public BasicSplitPaneDivider createDefaultDivider() {
				BasicSplitPaneDivider pane_divider = new BasicSplitPaneDivider(
						this) {
				};
				return pane_divider;
			}
		});

		splitpane.setBorder(new MatteBorder(3, 3, 3, 3, Color.LIGHT_GRAY));
		main_frame.getContentPane().add(splitpane, BorderLayout.CENTER);

		/* setting the splitpane left component *******************************/

		tabbedpane = new JTabbedPane();
		splitpane.add(tabbedpane, JSplitPane.LEFT);

		search_panel = new SearchPanel(this);
		tabbedpane.add("<html><body><b><u>S</u></b>earch</body></html>",
				search_panel.getSearch_panel());

		synset_info_panel = new SynsetInfoPanel(this);
		tabbedpane.add("<html><body>Synset <b><u>i</u></b>nfo</body></html>",
				synset_info_panel.getScrollpane());

		wngraph_info_panel = new WNGraphInfoPanel();
		tabbedpane.add("<html><body><b><u>G</u></b>raph</body></html>",
				wngraph_info_panel);

		/* setting the splitpane right component. *****************************/
		initGraphView();
		splitpane.add(wngraph_panel.getPanel(), JSplitPane.RIGHT);
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
		int divider_location = splitpane.getDividerLocation();
		splitpane.add(wngraph_panel.getPanel(), JSplitPane.RIGHT);
		splitpane.setDividerLocation(divider_location);
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

	public JSplitPane getSplitpane() {
		return splitpane;
	}

	public WNGraph getWngraph() {
		return wngraph;
	}

	public WNGraphPanel getWngraphp() {
		return wngraph_panel;
	}

	public IDictionary getDict() {
		return dict;
	}

	public JTabbedPane getTabbedpane() {
		return tabbedpane;
	}

	public JFrame getMain_frame() {
		return main_frame;
	}

}
