package net.trevize.wnxplorer.jung;

import edu.mit.jwi.item.Pointer;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * SemanticRelationEdge.java - Mar 25, 2010
 */

public class SemanticRelationEdge {

	private Pointer semantic_relation_type;

	public SemanticRelationEdge(Pointer semantic_relation_type) {
		this.semantic_relation_type = semantic_relation_type;
	}

	public Pointer getSemantic_relation_type() {
		return semantic_relation_type;
	}

	public void setSemantic_relation_type(Pointer semanticRelationType) {
		semantic_relation_type = semanticRelationType;
	}

}
