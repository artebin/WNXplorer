package net.trevize.knetvis.renderer;


import net.trevize.knetvis.KNetVertex;

import org.apache.commons.collections15.Transformer;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * SynsetVertexLabelTransformer.java - Mar 25, 2010
 */

public class KNetVertexLabelTransformer implements
		Transformer<KNetVertex, String> {

	@Override
	public String transform(KNetVertex v) {
		return v.getConcept().getShortLabel();
	}

}
