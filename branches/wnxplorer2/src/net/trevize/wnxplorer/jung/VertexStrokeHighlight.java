package net.trevize.wnxplorer.jung;

import java.awt.BasicStroke;
import java.awt.Stroke;

import net.trevize.wnxplorer.explorer.WNGraphPanel;

import org.apache.commons.collections15.Transformer;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * VertexStrokeHighlight.java - Mar 30, 2010
 * @param <V>
 * @param <E>
 */

public class VertexStrokeHighlight<V, E> implements Transformer<V, Stroke> {

	private boolean highlight = true;
	private Stroke heavy = new BasicStroke(5);
	private Stroke medium = new BasicStroke(3);
	private Stroke light = new BasicStroke(1);
	private WNGraphPanel wngraphp;

	public VertexStrokeHighlight(WNGraphPanel wngraphp) {
		this.wngraphp = wngraphp;
	}

	public void setHighlight(boolean highlight) {
		this.highlight = highlight;
	}

	public Stroke transform(V v) {
		if (highlight) {
			SynsetVertex vertex = (SynsetVertex) v;
			if (v.equals(wngraphp.getLast_clicked_vertex())) {
				return medium;
			}
		}
		return light;
	}

}