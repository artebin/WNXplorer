package net.trevize.wnxplorer.lucene;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import net.trevize.wnxplorer.WNXplorerProperties;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TopDocs;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * LuceneWNResultsPanel.java - Apr 6, 2010
 */

public class LWNResultsPanel {

	//for Lucene.
	private TopDocs topdocs;
	private Searcher searcher;

	//swing components.
	private JEditorPane view;
	private HTMLEditorKit kit;

	public static final int DEFAULT_NUMBER_OF_RESULTS_PER_PAGE = 5;
	private int num_of_results_per_page;
	private int current_page_number;

	public LWNResultsPanel(TopDocs topdocs, Searcher searcher) {
		this.topdocs = topdocs;
		this.searcher = searcher;
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
			FileReader fr = new FileReader(
					WNXplorerProperties.getCss_path_main());
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

		sb.append("<ul>");

		for (int result_id = idx_start; result_id < topdocs.totalHits
				&& result_id < idx_stop; ++result_id) {
			sb.append("<li>");
			sb.append(getResultHTMLFragment(result_id,
					topdocs.scoreDocs[result_id]));
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

	private String getResultHTMLFragment(int result_id, ScoreDoc score_doc) {
		StringBuffer sb = new StringBuffer();

		int doc_id = topdocs.scoreDocs[result_id].doc;
		Document doc = null;
		try {
			doc = searcher.doc(doc_id);
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		sb.append("[" + result_id + " : " + score_doc.score + "] ");
		String synset_id = doc.getField(LWNUtils.SYNSET_DATA_ID).stringValue();
		sb.append("<a href=\"synset_id:" + synset_id + "\">");
		sb.append(synset_id);
		sb.append("</a>");
		sb.append("<p>");
		sb.append(LWNUtils.getDocumentShortTextDescription(doc));
		sb.append("</p>");

		return sb.toString();
	}

	public int getPageCount() {
		return topdocs.totalHits / num_of_results_per_page + 1;
	}

	public boolean isTheFirstPage() {
		return (current_page_number == 0);
	}

	public boolean isTheLastPage() {
		return (current_page_number == (getPageCount() - 1));
	}

	public String getResultsStatus() {
		StringBuffer sb = new StringBuffer();

		int first_idx = current_page_number * num_of_results_per_page;
		int last_idx = -1;

		if (isTheLastPage()) {
			last_idx = topdocs.totalHits - 1;
		} else {
			last_idx = (current_page_number + 1) * num_of_results_per_page - 1;
		}

		sb.append(first_idx);
		sb.append(" - ");
		sb.append(last_idx);
		sb.append(" | ");
		sb.append(topdocs.totalHits + " results");

		return sb.toString();
	}

	public String getResultsStatusHTML() {
		StringBuffer sb = new StringBuffer();

		sb.append("<html><body><div style=\"font-size:12px;text-align:center;\">");

		int first_idx = current_page_number * num_of_results_per_page;
		int last_idx = -1;

		if (isTheLastPage()) {
			last_idx = topdocs.totalHits - 1;
		} else {
			last_idx = (current_page_number + 1) * num_of_results_per_page - 1;
		}

		sb.append(first_idx);
		sb.append(" - ");
		sb.append(last_idx);
		sb.append(" | ");
		sb.append(topdocs.totalHits + " results");

		sb.append("</div></body></html>");

		return sb.toString();
	}

	/***************************************************************************
	 * getters and setters.
	 **************************************************************************/

	public TopDocs getTopdocs() {
		return topdocs;
	}

	public void setTopdocs(TopDocs topdocs) {
		this.topdocs = topdocs;
	}

	public Searcher getSearcher() {
		return searcher;
	}

	public void setSearcher(Searcher searcher) {
		this.searcher = searcher;
	}

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
