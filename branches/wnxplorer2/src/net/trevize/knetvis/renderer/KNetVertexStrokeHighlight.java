package net.trevize.knetvis.renderer;

import java.awt.BasicStroke;
import java.awt.Stroke;

import net.trevize.knetvis.KNetGraphViewer;
import net.trevize.knetvis.KNetVertex;

import org.apache.commons.collections15.Transformer;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * VertexStrokeHighlight.java - Mar 30, 2010
 * @param <V>
 * @param <E>
 */

public class KNetVertexStrokeHighlight<V, E> implements Transformer<V, Stroke> {

	private boolean highlight = true;
	private Stroke heavy = new BasicStroke(5);
	private Stroke medium = new BasicStroke(3);
	private Stroke light = new BasicStroke(1);
	private KNetGraphViewer knetgraph_viewer;

	public KNetVertexStrokeHighlight(KNetGraphViewer knetgraph_viewer) {
		this.knetgraph_viewer = knetgraph_viewer;
	}

	public void setHighlight(boolean highlight) {
		this.highlight = highlight;
	}

	public Stroke transform(V v) {
		if (highlight) {
			KNetVertex vertex = (KNetVertex) v;
			if (v.equals(knetgraph_viewer.getSelectedvertex())) {
				return medium;
			}
		}
		return light;
	}

}