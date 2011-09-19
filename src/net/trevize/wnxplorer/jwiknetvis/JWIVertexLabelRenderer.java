package net.trevize.wnxplorer.jwiknetvis;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JComponent;

import net.trevize.knetvis.KNetVertex;
import edu.mit.jwi.item.POS;
import edu.uci.ics.jung.visualization.renderers.DefaultVertexLabelRenderer;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * SynsetVertexLabelRenderer.java - Mar 25, 2010
 * @param <V>
 */

public class JWIVertexLabelRenderer<V> extends DefaultVertexLabelRenderer {

	private Color background_color;
	private Color foreground_color;

	public JWIVertexLabelRenderer(Color pickedVertexLabelColor) {
		super(pickedVertexLabelColor);
		foreground_color = Color.BLACK;
		background_color = Color.WHITE;
	}

	/***************************************************************************
	 * implementation of DefaultVertexLabelRenderer.
	 **************************************************************************/

	@Override
	public <V> Component getVertexLabelRendererComponent(JComponent vv,
			Object value, Font font, boolean isSelected, V vertex) {
		Component res = super.getVertexLabelRendererComponent(vv, value, font,
				isSelected, vertex);

		KNetVertex knet_vertex = (KNetVertex) vertex;
		JWIConcept concept = (JWIConcept) knet_vertex.getConcept();

		if (concept.getPOS() == POS.NOUN) {
			res.setForeground(Color.BLACK);
			res.setBackground(Color.GREEN);
		}

		else


		if (concept.getPOS() == POS.VERB) {
			res.setForeground(Color.BLACK);
			res.setBackground(Color.RED);
		}

		else


		if (concept.getPOS() == POS.ADJECTIVE) {
			res.setForeground(Color.BLACK);
			res.setBackground(Color.YELLOW);
		}

		else


		if (concept.getPOS() == POS.ADVERB) {
			res.setForeground(Color.BLACK);
			res.setBackground(Color.BLUE);
		}

		return res;
	}

}
