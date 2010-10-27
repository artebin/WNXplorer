package net.trevize.wnxplorer.jung;

import java.util.HashMap;

import org.apache.commons.collections15.Transformer;

import edu.mit.jwi.item.Pointer;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * SemanticRelationEdgeLabelTranformer.java - Oct 27, 2010
 */

public class SemanticRelationEdgeLabelTranformer implements
		Transformer<SemanticRelationEdge, String> {

	private HashMap<Pointer, String> labels;

	public SemanticRelationEdgeLabelTranformer() {
		labels = new HashMap<Pointer, String>();
		labels.put(Pointer.HYPERNYM, "hypernym");
		labels.put(Pointer.HYPONYM, "hyponym");
	}

	@Override
	public String transform(SemanticRelationEdge e) {
		return labels.get(e.getSemantic_relation_type());
	}

}
