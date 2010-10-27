package net.trevize.wnxplorer.explorer;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import net.trevize.gui.layout.CellStyle;
import net.trevize.gui.layout.XGridBag;
import net.trevize.wnxplorer.jwi.JWIWNResultsPanel;
import net.trevize.wnxplorer.jwi.JWIWNSearcher;
import net.trevize.wnxplorer.jwi.WNUtils;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.POS;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * SearchPanel.java - Mar 30, 2010
 */

public class SearchPanel implements ActionListener, HyperlinkListener {

	public static final String ACTION_COMMAND_DO_QUERY = "ACTION_COMMAND_DO_QUERY";
	public static final String ACTION_COMMAND_PREVIOUS_RESULT_PAGE = "ACTION_COMMAND_PREVIOUS_RESULT_PAGE";
	public static final String ACTION_COMMAND_NEXT_RESULT_PAGE = "ACTION_COMMAND_NEXT_RESULT_PAGE";
	public static final String ACTION_COMMAND_ADD_SYNSET_TO_GRAPH = "ACTION_COMMAND_ADD_SYNSET_TO_GRAPH";

	/*
	 * this class needs a reference to the Explorer, for updating the graph when the
	 * user choose a result and put it in the graph.
	 */
	private Explorer explorer;

	//the resulting panel.
	private JPanel search_panel;
	private XGridBag xgb;

	//components for the query search field.
	private JTextField jtf0;
	private JButton jb0;

	//components of the toolbar.
	private JButton b1; //previous page button.
	private JButton b2; //next page button.
	private JButton b3;
	private JLabel results_status;

	//components for displaying the results.
	private JScrollPane scrollpane;
	private JWIWNResultsPanel results_panel;
	private int num_of_results_per_page = 10;

	private CellStyle style_p0 = new CellStyle(1., 0.,
			GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL,
			new Insets(0, 0, 0, 0), 0, 0);
	private CellStyle style_p1 = new CellStyle(1., 1.,
			GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0,
					0, 0, 0), 0, 0);

	public SearchPanel(Explorer explorer) {
		this.explorer = explorer;
		init();
	}

	private void init() {
		//setting the resulting panel.
		search_panel = new JPanel();
		search_panel.setLayout(new GridBagLayout());
		xgb = new XGridBag(search_panel);

		//setting the search query text field.
		JPanel p0 = new JPanel();
		p0.setLayout(new BorderLayout());
		JLabel l0 = new JLabel("Query:");
		p0.add(l0, BorderLayout.WEST);
		jtf0 = new JTextField();
		p0.add(jtf0, BorderLayout.CENTER);
		jb0 = new JButton("search \u21B5");
		jb0.addActionListener(this);
		jb0.setActionCommand(ACTION_COMMAND_DO_QUERY);
		p0.add(jb0, BorderLayout.EAST);
		p0.add(Box.createVerticalStrut(3), BorderLayout.SOUTH);
		xgb.add(p0, style_p0, 0, 0);

		//setting the toolbar.
		JPanel p1 = new JPanel();
		p1.setLayout(new BoxLayout(p1, BoxLayout.X_AXIS));

		b1 = new JButton(new ImageIcon("./gfx/left.jpg"));
		b1.setActionCommand(ACTION_COMMAND_PREVIOUS_RESULT_PAGE);
		b1.addActionListener(this);
		p1.add(b1);

		b2 = new JButton(new ImageIcon("./gfx/right.jpg"));
		b2.setActionCommand(ACTION_COMMAND_NEXT_RESULT_PAGE);
		b2.addActionListener(this);

		p1.add(b2);
		p1.add(Box.createHorizontalGlue());
		results_status = new JLabel();
		p1.add(results_status);
		p1.add(Box.createHorizontalGlue());

		/*
		b3 = new JButton(new ImageIcon("./gfx/plus.jpg"));
		b3.setActionCommand(ACTION_COMMAND_ADD_SYNSET_TO_GRAPH);
		b3.addActionListener(this);
		p1.add(b3);
		*/

		xgb.add(p1, style_p0, 1, 0);
		xgb.add(Box.createVerticalStrut(3), style_p0, 2, 0);

		//setting the results panel.
		scrollpane = new JScrollPane();
		scrollpane.setViewportView(new JPanel());

		//remove the ugly border of the scrollpane viewport.
		//Border empty = new EmptyBorder(0, 0, 0, 0);
		//scrollpane.setViewportBorder(empty);
		//scrollpane.getHorizontalScrollBar().setBorder(empty);
		//scrollpane.getVerticalScrollBar().setBorder(empty);

		xgb.add(scrollpane, style_p1, 2, 0);
	}

	public String getQuery() {
		return jtf0.getText();
	}

	/***************************************************************************
	 * implementation of ActionListener.
	 **************************************************************************/

	@Override
	public void actionPerformed(ActionEvent e) {
		String action_command = e.getActionCommand();

		if (action_command.equals(ACTION_COMMAND_DO_QUERY)) {
			System.out.println("ACTION_COMMAND_DO_QUERY: " + jtf0.getText());

			//instantiate a new JWIWNSearcher and make the search.
			JWIWNSearcher searcher = new JWIWNSearcher(explorer.getDict());
			searcher.search(POS.NOUN, jtf0.getText());

			//instantiate the JWIWNResultsPanel.
			results_panel = new JWIWNResultsPanel(explorer.getDict(), searcher
					.getResults());
			results_panel.getView().addHyperlinkListener(this);
			results_panel.setNum_of_results_per_page(num_of_results_per_page);
			results_panel.retrievePage(0);

			//update the results_status.
			results_status.setText(results_panel.getResultsStatus());

			//save the divider location, update the results_container and restore the divider location.
			int divider_location = explorer.getJsp0().getDividerLocation();
			scrollpane.setViewportView(results_panel.getView());
			explorer.getJsp0().setDividerLocation(divider_location);

			/*
			 * put the scrollbars value of the scrollpane that contained the JeditorPane
			 * to 0. 
			 * Because of the JEditorPane, this has to be done in a separate thread.
			 */
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					scrollpane.getVerticalScrollBar().setValue(0);
					scrollpane.getHorizontalScrollBar().setValue(0);
				}
			});
		}

		else

		if (action_command.equals(ACTION_COMMAND_PREVIOUS_RESULT_PAGE)) {
			System.out.println("ACTION_COMMAND_PREVIOUS_RESULT_PAGE");
			if (results_panel.isTheFirstPage()) {
				return;
			}
			results_panel
					.retrievePage(results_panel.getCurrent_page_number() - 1);

			//update the results_status.
			results_status.setText(results_panel.getResultsStatus());

			/*
			 * put the scrollbars value of the scrollpane that contained the JeditorPane
			 * to 0. 
			 * Because of the JEditorPane, this has to be done in a separate thread.
			 */
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					scrollpane.getVerticalScrollBar().setValue(0);
					scrollpane.getHorizontalScrollBar().setValue(0);
				}
			});

			results_panel.getView().repaint();
		}

		else

		if (action_command.equals(ACTION_COMMAND_NEXT_RESULT_PAGE)) {
			System.out.println("ACTION_COMMAND_NEXT_RESULT_PAGE");
			if (results_panel.isTheLastPage()) {
				return;
			}
			results_panel
					.retrievePage(results_panel.getCurrent_page_number() + 1);

			//update the results_status.
			results_status.setText(results_panel.getResultsStatus());

			/*
			 * put the scrollbars value of the scrollpane that contained the JeditorPane
			 * to 0. 
			 * Because of the JEditorPane, this has to be done in a separate thread.
			 */
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					scrollpane.getVerticalScrollBar().setValue(0);
					scrollpane.getHorizontalScrollBar().setValue(0);
				}
			});

			results_panel.getView().repaint();
		}

		else

		if (action_command.equals(ACTION_COMMAND_ADD_SYNSET_TO_GRAPH)) {
			System.out.println("ACTION_COMMAND_ADD_SYNSET_TO_GRAPH");
		}

	}

	/***************************************************************************
	 * implementation of HyperlinkListener.
	 **************************************************************************/

	@Override
	public void hyperlinkUpdate(HyperlinkEvent e) {
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			System.out.println("link clicked");
			System.out.println(e.getDescription());

			String synset_id_string = e.getDescription().split(":")[1];
			ISynsetID synset_id = WNUtils
					.getISynsetIDFromString(synset_id_string);
			ISynset synset = explorer.getDict().getSynset(synset_id);
			explorer.getWngraph().addVertexForSynset(synset);
			explorer.getWngraphp().getVv().repaint();
		}
	}

	/***************************************************************************
	 * getters and setters.
	 **************************************************************************/

	public JPanel getSearch_panel() {
		return search_panel;
	}

	public void setSearch_panel(JPanel searchPanel) {
		search_panel = searchPanel;
	}

}
