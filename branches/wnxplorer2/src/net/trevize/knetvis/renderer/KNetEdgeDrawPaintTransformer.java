package net.trevize.knetvis.renderer;

import java.awt.Paint;

import net.trevize.knetvis.KNetEdge;

import org.apache.commons.collections15.Transformer;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * PointerEdgeDrawPaintTransformer.java - Oct 27, 2010
 */

public class KNetEdgeDrawPaintTransformer implements
		Transformer<KNetEdge, Paint> {

	@Override
	public Paint transform(KNetEdge edge) {
		return edge.getSemanticRelation().getColor();
	}

}