package net.trevize.wnxplorer.jung;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * SemanticRelationEdge.java - Mar 25, 2010
 */

public class SemanticRelationEdge {

	private String semantic_relation_type;

	public SemanticRelationEdge(String semantic_relation_type) {
		this.semantic_relation_type = semantic_relation_type;
	}

	public String getSemantic_relation_type() {
		return semantic_relation_type;
	}

	public void setSemantic_relation_type(String semanticRelationType) {
		semantic_relation_type = semanticRelationType;
	}

}
