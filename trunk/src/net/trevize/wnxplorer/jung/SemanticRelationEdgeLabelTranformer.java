package net.trevize.wnxplorer.jung;

import java.util.HashMap;

import net.trevize.wnxplorer.jwi.JWISynsetDocument;

import org.apache.commons.collections15.Transformer;

public class SemanticRelationEdgeLabelTranformer implements
		Transformer<SemanticRelationEdge, String> {

	private HashMap<String, String> labels;

	public SemanticRelationEdgeLabelTranformer() {
		labels = new HashMap<String, String>();
		labels.put(JWISynsetDocument.FIELD_POINTER_HYPERNYM, "hypernym");
		labels.put(JWISynsetDocument.FIELD_POINTER_HYPONYM, "hypernym");
	}

	@Override
	public String transform(SemanticRelationEdge e) {
		return labels.get(e.getSemantic_relation_type());
	}

}
