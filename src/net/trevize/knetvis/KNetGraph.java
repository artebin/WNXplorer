package net.trevize.knetvis;

import java.util.HashMap;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;

public interface KNetGraph {

	public DirectedSparseMultigraph<KNetVertex, KNetEdge> getFullGraph();

	public DirectedSparseMultigraph<KNetVertex, KNetEdge> getFilteredGraph();

	public KNetConceptFactory getKNetConceptFactory();

	public KNetSemanticRelationReference getSemanticRelationReference();

	public HashMap<String, KNetVertex> getVerticesIndex();

	public void clear();

	public KNetVertex addVertexForConcept(KNetConcept concept);

	public boolean edgeExistsBetweenVerticesForSemanticRelation(
			KNetVertex vertex_src, KNetVertex vertex_dest,
			KNetSemanticRelation semantic_relation);

	public void augmentGraphWithNeighborRing(KNetVertex vertex);

	public void setRenderingFilter(
			HashMap<KNetSemanticRelation, Boolean> semantic_relation_list,
			boolean show_all_vertices);

	public void applyRenderingFilter();

}
