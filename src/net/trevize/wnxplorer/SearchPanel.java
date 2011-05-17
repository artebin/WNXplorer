package net.trevize.wnxplorer;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import net.trevize.gui.layout.CellStyle;
import net.trevize.gui.layout.XGridBag;
import net.trevize.wnxplorer.knetvis.WNConcept;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.ISynsetID;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * SearchPanel.java - Mar 30, 2010
 */

public class SearchPanel implements ActionListener, HyperlinkListener,
		KeyListener {

	public static final String ACTION_COMMAND_DO_QUERY = "ACTION_COMMAND_DO_QUERY";
	public static final String ACTION_COMMAND_PREVIOUS_RESULT_PAGE = "ACTION_COMMAND_PREVIOUS_RESULT_PAGE";
	public static final String ACTION_COMMAND_NEXT_RESULT_PAGE = "ACTION_COMMAND_NEXT_RESULT_PAGE";

	/*
	 * this class needs a reference to the Explorer, for updating the graph when the
	 * user click on a result to put it into the graph.
	 */
	private Explorer explorer;

	//the panel for the search panel.
	private JPanel search_panel;
	private XGridBag xgb;

	//components for the query search field.
	private JTextField search_textfield;
	private JButton do_query_button;
	private PopupPOSButton pos_selector_button;

	//components of the toolbar.
	private JButton button_previous_result_page;
	private JButton button_next_result_page;
	private JLabel results_status; //how many results etc.

	//components for displaying the results.
	private JScrollPane scrollpane;
	private ResultsPanel results_panel;

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
		//setting the search panel.
		search_panel = new JPanel();
		search_panel.setLayout(new GridBagLayout());
		xgb = new XGridBag(search_panel);

		//setting the search query text field.
		JPanel p0 = new JPanel();
		p0.setLayout(new BorderLayout());

		JLabel l0 = new JLabel("Query: ");
		p0.add(l0, BorderLayout.WEST);

		search_textfield = new JTextField();
		search_textfield.addKeyListener(this);
		p0.add(search_textfield, BorderLayout.CENTER);

		JPanel p2 = new JPanel();
		p0.add(p2, BorderLayout.EAST);
		p2.setLayout(new BoxLayout(p2, BoxLayout.X_AXIS));

		pos_selector_button = new PopupPOSButton();
		p2.add(pos_selector_button);

		do_query_button = new JButton("\u21B5");
		do_query_button.setActionCommand(ACTION_COMMAND_DO_QUERY);
		do_query_button.addActionListener(this);
		p2.add(do_query_button);

		xgb.add(p0, style_p0, 0, 0);

		//setting the toolbar (that contains the "next page" and "previous page" buttons.
		JPanel p1 = new JPanel();
		p1.setLayout(new BoxLayout(p1, BoxLayout.X_AXIS));

		button_previous_result_page = new JButton(new ImageIcon(
				WNXplorerProperties.getIcon_path_go_previous()));
		button_previous_result_page.setMargin(new Insets(0, 0, 0, 0));
		button_previous_result_page
				.setActionCommand(ACTION_COMMAND_PREVIOUS_RESULT_PAGE);
		button_previous_result_page.addActionListener(this);
		p1.add(button_previous_result_page);

		button_next_result_page = new JButton(new ImageIcon(
				WNXplorerProperties.getIcon_path_go_next()));
		button_next_result_page.setMargin(new Insets(0, 0, 0, 0));
		button_next_result_page
				.setActionCommand(ACTION_COMMAND_NEXT_RESULT_PAGE);
		button_next_result_page.addActionListener(this);
		p1.add(button_next_result_page);

		p1.add(Box.createHorizontalGlue());

		results_status = new JLabel();
		p1.add(results_status);

		p1.add(Box.createHorizontalGlue());

		xgb.add(p1, style_p0, 1, 0);

		//setting the scrollpane for the results panel.
		scrollpane = new JScrollPane();
		scrollpane.setViewportView(new JPanel()); //at starting time no results, hence an empty panel is used.

		xgb.add(scrollpane, style_p1, 2, 0);
	}

	public String getQuery() {
		return search_textfield.getText();
	}

	/***************************************************************************
	 * implementation of ActionListener.
	 **************************************************************************/

	@Override
	public void actionPerformed(ActionEvent e) {
		String action_command = e.getActionCommand();

		if (action_command.equals(ACTION_COMMAND_DO_QUERY)) {
			doQuery();
		}

		else

		if (action_command.equals(ACTION_COMMAND_PREVIOUS_RESULT_PAGE)) {
			displayPreviousResultPage();
		}

		else

		if (action_command.equals(ACTION_COMMAND_NEXT_RESULT_PAGE)) {
			displayNextResultPage();
		}
	}

	private void doQuery() {
		//instantiate a new Searcher and process to the search.
		Searcher searcher = new Searcher(WNUtils.getWN_JWI_dictionary());
		searcher.search(pos_selector_button.getSelectedPOS(), getQuery());

		//instantiate the ResultsPanel.
		results_panel = new ResultsPanel(searcher.getResults());
		results_panel.getView().addHyperlinkListener(this);
		results_panel.retrievePage(0);

		//update the results_status.
		results_status.setText(results_panel.getResultsStatus());

		//set the results panel in the GUI
		scrollpane.setViewportView(results_panel.getView());

		/*
		 * put the scrollbar value of the scrollpane that contained the JeditorPane
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

	private void displayPreviousResultPage() {
		if (results_panel == null || results_panel.isTheFirstPage()) {
			return;
		}

		results_panel.retrievePage(results_panel.getCurrent_page_number() - 1);

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

	private void displayNextResultPage() {
		if (results_panel == null || results_panel.isTheLastPage()) {
			return;
		}

		results_panel.retrievePage(results_panel.getCurrent_page_number() + 1);

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

	/***************************************************************************
	 * implementation of HyperlinkListener.
	 **************************************************************************/

	@Override
	public void hyperlinkUpdate(HyperlinkEvent e) {
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			String concept_key = e.getDescription().split(":")[1];

			ISynsetID synset_id = WNUtils.getISynsetIDFromString(concept_key);
			ISynset synset = WNUtils.getWN_JWI_dictionary()
					.getSynset(synset_id);
			WNConcept concept = new WNConcept(synset);
			explorer.getKnetGraph().addVertexForConcept(concept);
			explorer.getKNetGraphViewer().fireGraphStructureChanged();

			//center the graph view of the added node
			explorer.getKNetGraphViewer().centerViewsOnConcept(concept);

			//refresh the views.
			explorer.refreshViews();
		}
	}

	/***************************************************************************
	 * implementation of KeyListener.
	 **************************************************************************/

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_ENTER) {
			doQuery();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	/***************************************************************************
	 * getters and setters.
	 **************************************************************************/

	public JPanel getSearch_panel() {
		return search_panel;
	}

	public PopupPOSButton getPos_selector_button() {
		return pos_selector_button;
	}

}
