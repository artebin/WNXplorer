package net.trevize.wordnet.jung;

import java.awt.Color;
import java.awt.Paint;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.visualization.picking.PickedInfo;

public class VertexFillColor<V> implements Transformer<V, Paint> {

	private PickedInfo<V> pi;

	public VertexFillColor(PickedInfo<V> pi) {
		this.pi = pi;
	}

	public Paint transform(V v) {
		if (pi.isPicked(v)) {
			return Color.YELLOW;
		} else {
			return Color.WHITE;
		}
	}

}
