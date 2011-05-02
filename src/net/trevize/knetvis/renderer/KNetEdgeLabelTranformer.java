package net.trevize.knetvis.renderer;

import net.trevize.knetvis.KNetEdge;

import org.apache.commons.collections15.Transformer;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * PointerEdgeLabelTranformer.java - Oct 27, 2010
 */

public class KNetEdgeLabelTranformer implements Transformer<KNetEdge, String> {

	@Override
	public String transform(KNetEdge e) {
		return e.getSemanticRelation().getLabel();
	}

}
