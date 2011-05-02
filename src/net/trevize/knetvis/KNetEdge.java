package net.trevize.knetvis;

public class KNetEdge {

	private KNetSemanticRelation semantic_relation;

	public KNetEdge(KNetSemanticRelation semantic_relation) {
		this.semantic_relation = semantic_relation;
	}

	public KNetSemanticRelation getSemanticRelation() {
		return semantic_relation;
	}

}
