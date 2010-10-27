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

	private JPanel p0;

	private JSplitPane jsp0;

	//jsp0 left component.
	private JTabbedPane jtp;
	private SynsetInfoPanel synset_info_panel;
	private SearchPanel search_panel;

	//jsp0 right component.
	private WNGraph wngraph;
	private WNGraphPanel wngraphp;

	//for JWI.
	private IDictionary dict;

	public Explorer() {
		initWordNet();
		init();

		main_frame.setVisible(true);
		jsp0.setDividerLocation(.25);
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

		//setting main panel.
		p0 = new JPanel();
		p0.setLayout(new BorderLayout());
		p0.setBorder(new EmptyBorder(3, 3, 3, 3));
		main_frame.getContentPane().add(p0, BorderLayout.CENTER);

		//setting the JSplitPane.
		jsp0 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

		/*
		 * setting a divider on the JSplitPane (due to the bug of the GTK PLAF). 
		 */
		jsp0.setUI(new BasicSplitPaneUI() {
			public BasicSplitPaneDivider createDefaultDivider() {
				BasicSplitPaneDivider pane_divider = new BasicSplitPaneDivider(
						this) {
				};
				return pane_divider;
			}
		});

		jsp0.setBorder(null);
		p0.add(jsp0, BorderLayout.CENTER);

		//setting the jsp0 left component.
		jtp = new JTabbedPane();
		jsp0.add(jtp, JSplitPane.LEFT);

		search_panel = new SearchPanel(this);
		jtp.add("Search", search_panel.getSearch_panel());

		synset_info_panel = new SynsetInfoPanel(this);
		jtp.add("Synset info", synset_info_panel.getScrollpane());

		jtp.add("Graph", new JPanel());

		//setting the jsp0 right component.
		initGraphView();
		jsp0.add(wngraphp.getPanel(), JSplitPane.RIGHT);
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
		//setting the jsp0 right component.
		initGraphView();
		//save the divider location, install the new graph view and restore the divider location.
		int divider_location = jsp0.getDividerLocation();
		jsp0.add(wngraphp.getPanel(), JSplitPane.RIGHT);
		jsp0.setDividerLocation(divider_location);

	}

	/***************************************************************************
	 * getters and setters. 
	 **************************************************************************/

	public JSplitPane getJsp0() {
		return jsp0;
	}

	public void setJsp0(JSplitPane jsp0) {
		this.jsp0 = jsp0;
	}

	public JTabbedPane getJtp() {
		return jtp;
	}

	public void setJtp(JTabbedPane jtp) {
		this.jtp = jtp;
	}

	public WNGraph getWngraph() {
		return wngraph;
	}

	public void setWngraph(WNGraph wngraph) {
		this.wngraph = wngraph;
	}

	public WNGraphPanel getWngraphp() {
		return wngraphp;
	}

	public void setWngraphp(WNGraphPanel wngraphp) {
		this.wngraphp = wngraphp;
	}

	public IDictionary getDict() {
		return dict;
	}

	public void setDict(IDictionary dict) {
		this.dict = dict;
	}

}
