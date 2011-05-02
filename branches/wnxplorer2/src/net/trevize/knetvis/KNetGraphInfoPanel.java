package net.trevize.knetvis;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * WNGraphInfoPanel.java - Oct 29, 2010
 */

public class KNetGraphInfoPanel extends JPanel implements HyperlinkListener {

	private KNetGraphViewer knetgraph_viewer;

	//the resulting panel.
	private JPanel graph_info_panel;

	//needed components.
	private JScrollPane scrollpane;
	private JEditorPane editorp;
	private StringBuffer sb;

	public KNetGraphInfoPanel(KNetGraphViewer knetgraph_viewer) {
		this.knetgraph_viewer = knetgraph_viewer;
		init();
	}

	private void init() {
		//setting the main panel.
		graph_info_panel = new JPanel();
		graph_info_panel.setLayout(new BorderLayout());

		scrollpane = new JScrollPane();
		graph_info_panel.add(scrollpane, BorderLayout.CENTER);

		//remove the ugly border of the scrollpane viewport.
		//		Border empty = new EmptyBorder(0, 0, 0, 0);
		//		scrollpane.setViewportBorder(empty);
		//		scrollpane.getHorizontalScrollBar().setBorder(empty);
		//		scrollpane.getVerticalScrollBar().setBorder(empty);

		//create a new JEditorPane derived for setting ANTIALIASING ON
		editorp = new JEditorPane() {
			public void paintComponent(Graphics g) {
				Graphics2D g2d = (Graphics2D) g;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
				super.paintComponent(g);
			}
		};
		editorp.addHyperlinkListener(this);
		HTMLEditorKit kit = new HTMLEditorKit();
		StyleSheet ss = new StyleSheet();

		//loading the stylesheet.
		try {
			FileReader fr = new FileReader(
					KNetVisProperties
							.getStylesheet_path_concept_description_panel());
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

	public void updateContent() {
		StringBuffer sb = new StringBuffer();
		sb.append("<html><body class=\"style1\">");

		sb.append("<h1>");
		sb.append("Concepts list:");
		sb.append("</h1>");

		sb.append("<ul>");

		for (KNetVertex vertex : knetgraph_viewer.getKNetGraph()
				.getFilteredGraph().getVertices()) {
			sb.append("<li>");
			sb.append("<b>");
			sb.append("[<a href=\"synset_id:" + vertex.getConcept().getKey()
					+ "\">" + vertex.getConcept().getKey() + "</a>]");
			sb.append(vertex.getConcept().getLabel());
			sb.append("</b><br/>");
			sb.append(vertex.getConcept().getDescription());
			sb.append("</li>");
		}

		sb.append("</ul>");

		sb.append("</body></html>");

		editorp.setText(sb.toString());

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

	/***************************************************************************
	 * implementation of HyperlinkListener.
	 **************************************************************************/

	@Override
	public void hyperlinkUpdate(HyperlinkEvent e) {
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			System.out.println("link clicked");
			System.out.println(e.getDescription());

			String key = e.getDescription().split(":")[1];
			KNetConcept knetconcept = knetgraph_viewer.getKNetGraph()
					.getKNetConceptFactory().getKNetConcept(key);

			knetgraph_viewer.selectConcept(knetconcept);
			knetgraph_viewer.centerViewsOnConcept(knetconcept);

			//refresh the views.
			knetgraph_viewer.refreshViews();
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

}
