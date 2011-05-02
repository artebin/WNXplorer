package net.trevize.knetvis.renderer;

import java.awt.Shape;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.decorators.AbstractVertexShapeTransformer;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * SynsetVertexShapeSizeAspectTransformer.java - Mar 25, 2010
 * @param <V>
 * @param <E>
 */

public class KNetVertexShapeSizeAspectTransformer<V, E> extends
		AbstractVertexShapeTransformer<V> implements Transformer<V, Shape> {

	protected boolean stretch = false;
	protected boolean scale = false;
	protected Graph<V, E> graph;

	public KNetVertexShapeSizeAspectTransformer(Graph<V, E> graphIn) {
		this.graph = graphIn;
		setSizeTransformer(new Transformer<V, Integer>() {
			public Integer transform(V v) {
				return 12;
			}
		});

		setAspectRatioTransformer(new Transformer<V, Float>() {
			public Float transform(V v) {
				if (stretch) {
					return (float) (graph.inDegree(v) + 1)
							/ (graph.outDegree(v) + 1);
				} else {
					return 1.0f;
				}
			}
		});
	}

	public void setStretching(boolean stretch) {
		this.stretch = stretch;
	}

	public void setScaling(boolean scale) {
		this.scale = scale;
	}

	public Shape transform(V v) {
		return factory.getEllipse(v);
	}

}