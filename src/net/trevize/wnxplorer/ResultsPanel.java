package net.trevize.wnxplorer;

import java.io.File;
import java.util.ArrayList;

import net.trevize.wnxplorer.jwiknetvis.JWIUtils;

import org.xhtmlrenderer.simple.XHTMLPanel;
import org.xhtmlrenderer.simple.extend.XhtmlNamespaceHandler;
import org.xhtmlrenderer.swing.SelectionHighlighter;

import edu.mit.jwi.item.ISynset;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * ResultsPanel.java - Apr 6, 2010
 */

public class ResultsPanel {

	private ArrayList<ISynset> results;

	private XHTMLPanel xhtml_panel;

	private int num_of_results_per_page = WNXplorerProperties
			.getNum_of_results_per_page_into_search_panel();
	private int current_page_number;

	public ResultsPanel(ArrayList<ISynset> results) {
		this.results = results;
		current_page_number = 0;
		init();
	}

	private void init() {
		xhtml_panel = new XHTMLPanel();
		xhtml_panel.getSharedContext().getTextRenderer()
				.setSmoothingThreshold(1.f);
		SelectionHighlighter sh = new SelectionHighlighter();
		sh.install(xhtml_panel);
	}

	/**
	 * 0 < idx_start < number of results aka results list size
	 * 1 < idx_stop <= number_of_results aka results list size
	 * 
	 * the results at idx_stop is not taken in account, i.e. from idx_start to idx_stop-1
	 */
	public void retrieveResults(int idx_start, int idx_stop) {
		StringBuffer sb = new StringBuffer();

		sb.append("<html>");
		sb.append("<head><link rel=\"stylesheet\" type=\"text/css\" href=\""
				+ new File(WNXplorerProperties.getCss_path_main()).toURI()
						.toString() + "\" /></head>");
		sb.append("<body class=\"style1\">");

		for (int result_id = idx_start; result_id < results.size()
				&& result_id < idx_stop; ++result_id) {
			sb.append("<div>");
			sb.append("[" + (result_id + 1) + "] ");
			String synset_id = results.get(result_id).getID().toString();
			sb.append("<a href=\"synset_id:" + synset_id + "\">");
			sb.append(synset_id);
			sb.append("</a>");
			sb.append("<br/>");
			sb.append("<b>" + JWIUtils.getWords(results.get(result_id))
					+ "</b>");
			sb.append("<br/>");
			sb.append(results.get(result_id).getGloss());
			sb.append("</div>");
		}

		sb.append("</body></html>");

		xhtml_panel.setDocumentFromString(sb.toString(), null,
				new XhtmlNamespaceHandler());
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

	public XHTMLPanel getView() {
		return xhtml_panel;
	}

	public int getCurrent_page_number() {
		return current_page_number;
	}

}
