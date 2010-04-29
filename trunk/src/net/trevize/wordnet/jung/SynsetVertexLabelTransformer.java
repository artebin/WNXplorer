package net.trevize.wordnet.jung;


import org.apache.commons.collections15.Transformer;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * SynsetVertexLabelTransformer.java - Mar 25, 2010
 */

public class SynsetVertexLabelTransformer implements
		Transformer<SynsetVertex, String> {

	@Override
	public String transform(SynsetVertex v) {
		return v.getShort_label();
	}

}
