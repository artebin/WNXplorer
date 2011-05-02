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
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * SynsetInfoPanel.java - Mar 30, 2010
 */

public class KNetConceptDescriptionPanel implements HyperlinkListener {

	/*
	 *   - interaction from the KNetDescriptionPanel -> KNetGraph: add new vertex
	 *   - interaction from the KNetGraph -> KNetDescriptionPanel: describe the currently selected vertex
	 */
	private KNetGraphViewer knetgraph_viewer;

	//the resulting panel.
	private JPanel description_panel;

	//needed components.
	private JScrollPane scrollpane;
	private JEditorPane editorp;
	private StringBuffer sb;

	//the knetconcept currently displayed in the KNetConceptDescriptionPanel
	//this knetconcept is also the currently selected concept
	private KNetConcept described_knetconcept;

	public KNetConceptDescriptionPanel(KNetGraphViewer knetgraph_viewer) {
		this.knetgraph_viewer = knetgraph_viewer;
		init();
	}

	private void init() {
		//setting the main panel.
		description_panel = new JPanel();
		description_panel.setLayout(new BorderLayout());

		scrollpane = new JScrollPane();
		description_panel.add(scrollpane, BorderLayout.CENTER);

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

	public void updateContent(KNetConcept knetconcept) {
		described_knetconcept = knetconcept;
		editorp.setText(knetconcept.getFullDescription());
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

			knetgraph_viewer.getKNetGraph().addVertexForConcept(knetconcept);
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

	public KNetConcept getDescribed_knetconcept() {
		return described_knetconcept;
	}

	public void setDescribed_knetconcept(KNetConcept described_knetconcept) {
		this.described_knetconcept = described_knetconcept;
	}

}
