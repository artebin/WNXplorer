package net.trevize.wordnet.jung;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JComponent;

import edu.uci.ics.jung.visualization.renderers.DefaultVertexLabelRenderer;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * SynsetVertexLabelRenderer.java - Mar 25, 2010
 * @param <V>
 */

public class SynsetVertexLabelRenderer<V> extends DefaultVertexLabelRenderer {

	private Color background_color;
	private Color foreground_color;

	public SynsetVertexLabelRenderer(Color pickedVertexLabelColor) {
		super(pickedVertexLabelColor);
	}

	public void setBackgroundColor(Color background_color) {
		this.background_color = background_color;
	}

	public void setForegroundColor(Color foreground_color) {
		this.foreground_color = foreground_color;
	}

	/***************************************************************************
	 * implementation of DefaultVertexLabelRenderer.
	 **************************************************************************/

	@Override
	public <V> Component getVertexLabelRendererComponent(JComponent vv,
			Object value, Font font, boolean isSelected, V vertex) {
		Component res = super.getVertexLabelRendererComponent(vv, value, font,
				isSelected, vertex);
		res.setBackground(background_color);
		if (!isSelected) {
			res.setForeground(foreground_color);
		}
		return res;
	}

}
