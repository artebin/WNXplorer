package net.trevize.wnxplorer;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JEditorPane;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import edu.mit.jwi.item.ISynset;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * ResultsPanel.java - Apr 6, 2010
 */

public class ResultsPanel {

	private static String getResultHTMLFragment(int result_id, ISynset synset) {
		StringBuffer sb = new StringBuffer();

		sb.append("[" + (result_id + 1) + "] ");
		String synset_id = synset.getID().toString();
		sb.append("<a href=\"synset_id:" + synset_id + "\">");
		sb.append(synset_id);
		sb.append("</a>");
		sb.append("<p>");
		sb.append(getSynsetShortTextDescription(synset));
		sb.append("</p>");

		return sb.toString();
	}

	public static String getSynsetShortTextDescription(ISynset synset) {
		StringBuffer sb = new StringBuffer();

		sb.append("<b>" + WNUtils.getWords(synset) + "</b>");
		sb.append("\n");
		sb.append(synset.getGloss());

		return sb.toString();
	}

	private ArrayList<ISynset> results;

	//swing components.
	private JEditorPane view;
	private HTMLEditorKit kit;

	private int num_of_results_per_page = WNXplorerProperties
			.getNum_of_results_per_page_into_search_panel();
	private int current_page_number;

	public ResultsPanel(ArrayList<ISynset> results) {
		this.results = results;
		current_page_number = 0;
		init();
	}

	private void init() {
		//create a new JEditorPane derived for setting ANTIALIASING ON
		view = new JEditorPane() {
			public void paintComponent(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
				super.paintComponent(g);
			}
		};
		view.setEditable(false);
		kit = new HTMLEditorKit();
		view.setEditorKit(kit);

		//loading the stylesheet.
		StyleSheet ss = new StyleSheet();
		StringBuffer sb = new StringBuffer();

		try {
			FileReader fr = new FileReader(
					WNXplorerProperties.getResults_panel_stylesheet_filepath());
			BufferedReader br = new BufferedReader(fr);
			sb = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
			fr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ss.addRule(sb.toString());
		kit.getStyleSheet().addStyleSheet(ss);
	}

	/**
	 * 0 < idx_start < number of results aka results list size
	 * 1 < idx_stop <= number_of_results aka results list size
	 * 
	 * the results at idx_stop is not taken in account, i.e. from idx_start to idx_stop-1
	 */
	public void retrieveResults(int idx_start, int idx_stop) {
		StringBuffer sb = new StringBuffer();

		sb.append("<html><body class=\"style1\">");

		sb.append("<ul>");

		for (int result_id = idx_start; result_id < results.size()
				&& result_id < idx_stop; ++result_id) {
			sb.append("<li>");
			sb.append(getResultHTMLFragment(result_id, results.get(result_id)));
			sb.append("</li>");
		}

		sb.append("</ul>");
		sb.append("</body></html>");

		view.setText(sb.toString());
	}

	public void retrievePage(int page_number) {
		retrieveResults(num_of_results_per_page * page_number,
				num_of_results_per_page * (page_number + 1));
		current_page_number = page_number;
	}

	public int getPageCount() {
		return results.size() / num_of_results_per_page
				+ ((results.size() % num_of_results_per_page == 0) ? 0 : 1);
	}

	public boolean isTheFirstPage() {
		return (current_page_number == 0);
	}

	public boolean isTheLastPage() {
		return (current_page_number == (getPageCount() - 1));
	}

	public String getResultsStatus() {
		StringBuffer sb = new StringBuffer();

		if (results.size() != 0) {
			int first_idx = current_page_number * num_of_results_per_page;
			int last_idx = -1;

			if (isTheLastPage()) {
				last_idx = results.size() - 1;
			} else {
				last_idx = (current_page_number + 1) * num_of_results_per_page
						- 1;
			}

			sb.append(first_idx + 1);
			sb.append(" - ");
			sb.append(last_idx + 1);
			sb.append(" | ");
		}

		sb.append(results.size() + " results");

		return sb.toString();
	}

	public String getResultsStatusHTML() {
		StringBuffer sb = new StringBuffer();

		sb.append("<html><body><div style=\"font-size:12px;text-align:center;\">");

		if (results.size() != 0) {
			int first_idx = current_page_number * num_of_results_per_page;
			int last_idx = -1;

			if (isTheLastPage()) {
				last_idx = results.size() - 1;
			} else {
				last_idx = (current_page_number + 1) * num_of_results_per_page
						- 1;
			}

			sb.append(first_idx + 1);
			sb.append(" - ");
			sb.append(last_idx + 1);
			sb.append(" | ");
		}

		sb.append(results.size() + " results");

		sb.append("</div></body></html>");

		return sb.toString();
	}

	/***************************************************************************
	 * getters and setters.
	 **************************************************************************/

	public JEditorPane getView() {
		return view;
	}

	public int getCurrent_page_number() {
		return current_page_number;
	}

}
