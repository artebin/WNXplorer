package net.trevize.wordnet.jung;


import org.apache.commons.collections15.Transformer;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * SynsetVertexTooltip.java - Mar 24, 2010
 */

public class SynsetVertexTooltip<E> implements
		Transformer<SynsetVertex, String> {

	@Override
	public String transform(SynsetVertex v) {
		return v.getSynset_words();
	}

}