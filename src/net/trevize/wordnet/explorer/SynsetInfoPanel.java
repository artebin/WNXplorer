package net.trevize.wordnet.explorer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import net.trevize.wordnet.WNUtils;
import edu.mit.jwi.item.IPointer;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.Pointer;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * SynsetInfoPanel.java - Mar 30, 2010
 */

public class SynsetInfoPanel implements HyperlinkListener {

	private static final String STYLESHEET_FILEPATH = "./gfx/style.css";

	private Explorer explorer;

	//the resulting panel.
	private JPanel synset_info_panel;

	//needed components.
	private JScrollPane scrollpane;
	private JEditorPane editorp;
	private StringBuffer sb;

	//the synset_id string of the currently displayed synset.
	private String synset_id;

	public SynsetInfoPanel(Explorer explorer) {
		this.explorer = explorer;
		init();
	}

	private void init() {
		//setting the main panel.
		synset_info_panel = new JPanel();
		synset_info_panel.setLayout(new BorderLayout());

		scrollpane = new JScrollPane();
		synset_info_panel.add(scrollpane, BorderLayout.CENTER);

		//remove the ugly border of the scrollpane viewport.
		//Border empty = new EmptyBorder(0, 0, 0, 0);
		//scrollpane.setViewportBorder(empty);
		//scrollpane.getHorizontalScrollBar().setBorder(empty);
		//scrollpane.getVerticalScrollBar().setBorder(empty);

		editorp = new JEditorPane();
		editorp.addHyperlinkListener(this);
		HTMLEditorKit kit = new HTMLEditorKit();
		StyleSheet ss = new StyleSheet();

		//loading the stylesheet.
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
		editorp.setEditorKit(kit);
		editorp.setEditable(false);
		scrollpane.setViewportView(editorp);
	}

	public void updateContent(ISynset synset) {
		StringBuffer sb = new StringBuffer();
		sb.append("<html><body>");

		sb.append("<h1>");
		sb.append(synset.getID().toString());
		sb.append("</h1>");

		sb.append(WNUtils.getWords(synset));
		sb.append("<br/>");
		sb.append(synset.getGloss());

		for (Pointer p : WNUtils.getPointers()) {
			List<ISynsetID> related = synset.getRelatedSynsets((IPointer) p);

			if (related.size() != 0) {
				sb.append("<h2>");
				sb.append(p.toString());
				sb.append("</h2>");
				sb.append("<ul>");
				for (ISynsetID synset_id : related) {
					sb.append("<li>");
					sb.append("<a href=\"synset_id:" + synset_id + "\">");
					sb.append(synset_id);
					sb.append("</a>");
					sb.append(": "
							+ WNUtils.getWords(explorer.getDict().getSynset(
									synset_id)));
					sb.append("</li>");
				}
				sb.append("</ul>");
			}
		}

		sb.append("</body></html>");

		editorp.setText(sb.toString());
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

	public JScrollPane getScrollpane() {
		return scrollpane;
	}

	public void setScrollpane(JScrollPane scrollpane) {
		this.scrollpane = scrollpane;
	}

	public JEditorPane getEditorp() {
		return editorp;
	}

	public void setEditorp(JEditorPane editorp) {
		this.editorp = editorp;
	}

	public String getSynset_id() {
		return synset_id;
	}

	public void setSynset_id(String synsetId) {
		synset_id = synsetId;
	}

}
