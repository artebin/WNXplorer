package net.trevize.wnxplorer.explorer;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
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

public class Explorer {

	private JFrame main_frame;
	private JSplitPane splitpane;

	//splitpane left component.
	private JTabbedPane tabbedpane;
	private SynsetInfoPanel synset_info_panel;
	private SearchPanel search_panel;

	//splitpane right component.
	private WNGraph wngraph;
	private WNGraphPanel wngraphp;

	//for JWI.
	private IDictionary dict;

	public Explorer() {
		initWordNet();
		init();

		main_frame.setVisible(true);

		splitpane.setDividerLocation(.25); //have to be done after the switch of the frame visibility to be taken in account.
	}

	private void initWordNet() {
		String wordnet_path = WNXplorerProperties.getWN_PATH();
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
		//setting the main frame.
		main_frame = new JFrame("WNXplorer");
		main_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main_frame
				.setSize(
						(int) (Toolkit.getDefaultToolkit().getScreenSize().width * .80),
						(int) (Toolkit.getDefaultToolkit().getScreenSize().width * .60));
		main_frame.setLocationRelativeTo(null);

		//adding a global key listener
		Toolkit.getDefaultToolkit().addAWTEventListener(
				new GlobalKeyListener(this), AWTEvent.KEY_EVENT_MASK);

		main_frame.getContentPane().setLayout(new BorderLayout());

		//setting the JSplitPane.
		splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

		/*
		 * setting a divider on the JSplitPane (due to the bug of the GTK PLAF). 
		 */
		splitpane.setUI(new BasicSplitPaneUI() {
			public BasicSplitPaneDivider createDefaultDivider() {
				BasicSplitPaneDivider pane_divider = new BasicSplitPaneDivider(
						this) {
				};
				return pane_divider;
			}
		});

		splitpane.setBorder(new EmptyBorder(3, 3, 3, 3));
		main_frame.getContentPane().add(splitpane, BorderLayout.CENTER);

		//setting the splitpane left component.
		tabbedpane = new JTabbedPane();
		splitpane.add(tabbedpane, JSplitPane.LEFT);

		search_panel = new SearchPanel(this);
		tabbedpane.add("<html><body><b><u>S</u></b>earch</body></html>", search_panel.getSearch_panel());

		synset_info_panel = new SynsetInfoPanel(this);
		tabbedpane.add("<html><body>Synset <b><u>i</u></b>nfo</body></html>", synset_info_panel.getScrollpane());

		tabbedpane.add("<html><body><b><u>G</u></b>raph</body></html>", new JPanel());

		//setting the splitpane right component.
		initGraphView();
		splitpane.add(wngraphp.getPanel(), JSplitPane.RIGHT);
	}

	public void initGraphView() {
		wngraph = new WNGraph(dict);

		wngraphp = new WNGraphPanel(wngraph);

		/*
		 * set a new PickingGraphMousePlugin for allowing the "selection mode".
		 */
		PickingGraphMousePlugin picking_plugin = new PickingGraphMousePlugin(
				synset_info_panel, wngraphp, dict);
		wngraphp.addPickingPlugin(picking_plugin);
	}

	public void clearView() {
		//setting the splitpane right component.
		initGraphView();
		//save the divider location, install the new graph view and restore the divider location.
		int divider_location = splitpane.getDividerLocation();
		splitpane.add(wngraphp.getPanel(), JSplitPane.RIGHT);
		splitpane.setDividerLocation(divider_location);

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
		return wngraphp;
	}

	public IDictionary getDict() {
		return dict;
	}

	public JTabbedPane getTabbedpane() {
		return tabbedpane;
	}

}
