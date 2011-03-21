package net.trevize.wnxplorer.jung;

import org.apache.commons.collections15.Transformer;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * PointerEdgeLabelTranformer.java - Oct 27, 2010
 */

public class PointerEdgeLabelTranformer implements
		Transformer<PointerEdge, String> {

	@Override
	public String transform(PointerEdge e) {
		return e.getPointer_type().getName();
	}

}
