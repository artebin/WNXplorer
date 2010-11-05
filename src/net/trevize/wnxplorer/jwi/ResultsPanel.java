package net.trevize.wnxplorer.jwi;

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

	public static final String STYLESHEET_FILEPATH = "./gfx/style.css";
	public static final int DEFAULT_NUMBER_OF_RESULTS_PER_PAGE = 5;

	private ArrayList<ISynset> results;

	//swing components.
	private JEditorPane view;
	private HTMLEditorKit kit;

	private int num_of_results_per_page;
	private int current_page_number;

	public ResultsPanel(ArrayList<ISynset> results) {
		this.results = results;
		num_of_results_per_page = DEFAULT_NUMBER_OF_RESULTS_PER_PAGE;
		current_page_number = 0;
		init();
	}

	private void init() {
		//create the jeditorpane.
		view = new JEditorPane();
		view.setEditable(false);
		kit = new HTMLEditorKit();
		view.setEditorKit(kit);

		//loading the stylesheet.
		StyleSheet ss = new StyleSheet();
		StringBuffer sb = new StringBuffer();

		try {
			FileReader fr = new FileReader(STYLESHEET_FILEPATH);
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

	private String getResultHTMLFragment(int result_id, ISynset synset) {
		StringBuffer sb = new StringBuffer();

		sb.append("[" + (result_id + 1) + "] ");
		String synset_id = synset.getID().toString();
		sb.append("<a href=\"synset_id:" + synset_id + "\">");
		sb.append(synset_id);
		sb.append("</a>");
		sb.append("<p>");
		sb.append(getDocumentShortTextDescription(synset));
		sb.append("</p>");

		return sb.toString();
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

		sb
				.append("<html><body><div style=\"font-size:12px;text-align:center;\">");

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

	public static String getDocumentShortTextDescription(ISynset synset) {
		StringBuffer sb = new StringBuffer();

		/*
		sb.append(synset.getID().toString());
		sb.append("\n");
		*/

		sb.append("<b>" + WNUtils.getWords(synset) + "</b>");
		sb.append("\n");
		sb.append(synset.getGloss());

		return sb.toString();
	}

	/***************************************************************************
	 * getters and setters.
	 **************************************************************************/

	public int getNum_of_results_per_page() {
		return num_of_results_per_page;
	}

	public void setNum_of_results_per_page(int numOfResultsPerPage) {
		num_of_results_per_page = numOfResultsPerPage;
	}

	public int getCurrent_page_number() {
		return current_page_number;
	}

	public void setCurrent_page_number(int currentPageNumber) {
		current_page_number = currentPageNumber;
	}

	public JEditorPane getView() {
		return view;
	}

	public void setView(JEditorPane view) {
		this.view = view;
	}

}
