package net.trevize.wnxplorer;

import java.awt.Shape;

import net.trevize.knetvis.KNetVertex;
import net.trevize.wnxplorer.jwiknetvis.WNConcept;

import org.apache.commons.collections15.Transformer;

import edu.mit.jwi.item.POS;
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

public class WNVertexShapeSizeAspectTransformer<V, E> extends
		AbstractVertexShapeTransformer<V> implements Transformer<V, Shape> {

	protected boolean stretch = false;
	protected boolean scale = false;
	protected Graph<V, E> graph;

	public WNVertexShapeSizeAspectTransformer(Graph<V, E> graphIn) {
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
		KNetVertex vertex = (KNetVertex) v;
		WNConcept concept = (WNConcept) vertex.getConcept();

		if (concept.getPOS().equals(POS.NOUN)) {
			return factory.getEllipse(v);
		}

		else

		if (concept.getPOS().equals(POS.VERB)) {
			return factory.getRectangle(v);
		}

		else

		if (concept.getPOS().equals(POS.ADJECTIVE)) {
			return factory.getRegularPolygon(v, 5);
		}

		else if (concept.getPOS().equals(POS.ADVERB)) {
			return factory.getRegularPolygon(v, 3);
		}

		return null;
	}

}