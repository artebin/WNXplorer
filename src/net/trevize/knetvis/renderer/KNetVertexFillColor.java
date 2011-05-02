package net.trevize.knetvis.renderer;

import java.awt.Color;
import java.awt.Paint;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.visualization.picking.PickedInfo;

public class KNetVertexFillColor<V> implements Transformer<V, Paint> {

	private PickedInfo<V> pi;

	public KNetVertexFillColor(PickedInfo<V> pi) {
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
