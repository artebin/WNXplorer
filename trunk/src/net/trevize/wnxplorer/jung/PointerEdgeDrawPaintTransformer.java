package net.trevize.wnxplorer.jung;

import java.awt.Paint;

import net.trevize.wnxplorer.jwi.WNUtils;

import org.apache.commons.collections15.Transformer;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * PointerEdgeDrawPaintTransformer.java - Oct 27, 2010
 */

public class PointerEdgeDrawPaintTransformer implements
		Transformer<PointerEdge, Paint> {

	@Override
	public Paint transform(PointerEdge pointer_edge) {
		return WNUtils.getPointersColor().get(pointer_edge.getPointer_type());
	}

}