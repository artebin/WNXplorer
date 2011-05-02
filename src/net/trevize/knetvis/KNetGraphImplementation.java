package net.trevize.knetvis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;

public class KNetGraphImplementation implements KNetGraph {

	private DirectedSparseMultigraph<KNetVertex, KNetEdge> full_graph;
	private DirectedSparseMultigraph<KNetVertex, KNetEdge> filtered_graph;

	private KNetConceptFactory concept_factory;
	private KNetSemanticRelationReference semantic_relation_reference;

	//toogle display isolated vertices after filtering
	private boolean show_all_vertices;

	//semantic relations to display
	private HashMap<KNetSemanticRelation, Boolean> semantic_relation_list;

	private HashMap<String, KNetVertex> vertices_index;

	public KNetGraphImplementation(KNetConceptFactory concept_factory,
			KNetSemanticRelationReference semantic_relation_reference) {
		this.concept_factory = concept_factory;
		this.semantic_relation_reference = semantic_relation_reference;

		full_graph = new DirectedSparseMultigraph<KNetVertex, KNetEdge>();
		filtered_graph = new DirectedSparseMultigraph<KNetVertex, KNetEdge>();
		vertices_index = new HashMap<String, KNetVertex>();
	}

	@Override
	public void clear() {
		full_graph = new DirectedSparseMultigraph<KNetVertex, KNetEdge>();
		filtered_graph = new DirectedSparseMultigraph<KNetVertex, KNetEdge>();
		vertices_index = new HashMap<String, KNetVertex>();
	}

	@Override
	public KNetVertex addVertexForConcept(KNetConcept concept) {
		if (vertices_index.keySet().contains(concept.getKey())) {
			return vertices_index.get(concept.getKey());
		}

		//create the new vertex
		KNetVertex vertex = new KNetVertex(concept);

		//add the vertex to the graph
		full_graph.addVertex(vertex);
		filtered_graph.addVertex(vertex);

		//index the vertex
		vertices_index.put(concept.getKey(), vertex);

		applyRenderingFilter();

		return vertex;
	}

	@Override
	public boolean edgeExistsBetweenVerticesForSemanticRelation(
			KNetVertex vertex_src, KNetVertex vertex_dest,
			KNetSemanticRelation semantic_relation) {
		Iterator<KNetEdge> edges_iter = full_graph.getOutEdges(vertex_src)
				.iterator();

		boolean found = false;
		while (edges_iter.hasNext()) {
			KNetEdge edge = edges_iter.next();
			if (full_graph.getDest(edge) == vertex_dest) {
				if (edge.getSemanticRelation() == semantic_relation) {
					found = true;
					break;
				}
			}
		}

		return found;
	}

	@Override
	public void augmentGraphWithNeighborRing(KNetVertex vertex_src) {
		for (KNetSemanticRelation semantic_relation : semantic_relation_reference
				.getSemanticRelationList()) {

			List<KNetConcept> related_concepts = vertex_src.getConcept()
					.getRelatedConcepts(semantic_relation);

			for (KNetConcept concept : related_concepts) {

				//is this concept ever present in the graph?
				KNetVertex vertex_dest = vertices_index.get(concept.getKey());

				if (vertex_dest != null) {
					if (edgeExistsBetweenVerticesForSemanticRelation(
							vertex_src, vertex_dest, semantic_relation)
							|| edgeExistsBetweenVerticesForSemanticRelation(
									vertex_dest, vertex_src, semantic_relation)) {
						continue;
					}

					/*
					 * managing symmetric relations
					 */
					if (semantic_relation.getOpposite() != null
							&& edgeExistsBetweenVerticesForSemanticRelation(
									vertex_src, vertex_dest,
									semantic_relation.getOpposite())
							|| edgeExistsBetweenVerticesForSemanticRelation(
									vertex_dest, vertex_src,
									semantic_relation.getOpposite())) {
						continue;
					}
				}

				if (vertex_dest == null) {
					//add the vertex
					vertex_dest = addVertexForConcept(concept);
				}

				//create the edge
				KNetEdge edge = new KNetEdge(semantic_relation);

				//add the edge to the graph.
				full_graph.addEdge(edge, vertex_src, vertex_dest);
				filtered_graph.addEdge(edge, vertex_src, vertex_dest);

			}//end for all related concepts to vertex_src

		}//end for all semantic relations

		applyRenderingFilter();
	}

	@Override
	public void setRenderingFilter(
			HashMap<KNetSemanticRelation, Boolean> semantic_relation_list,
			boolean show_all_vertices) {
		this.semantic_relation_list = semantic_relation_list;
		this.show_all_vertices = show_all_vertices;
	}

	@Override
	public void applyRenderingFilter() {
		//removing the edges
		for (KNetEdge edge : full_graph.getEdges()) {
			if (!semantic_relation_list.get(edge.getSemanticRelation())) {
				filtered_graph.removeEdge(edge);
			} else {
				filtered_graph.addEdge(edge,
						full_graph.getIncidentVertices(edge));
			}
		}

		//removing the vertices
		if (!show_all_vertices) {
			for (KNetVertex synset_vertex : full_graph.getVertices()) {
				if (filtered_graph.getVertices().contains(synset_vertex)) {
					if (filtered_graph.getNeighborCount(synset_vertex) == 0) {
						filtered_graph.removeVertex(synset_vertex);
					}
				}
			}
		} else {
			for (KNetVertex synset_vertex : full_graph.getVertices()) {
				if (!filtered_graph.getVertices().contains(synset_vertex)) {
					filtered_graph.addVertex(synset_vertex);
				}
			}
		}
	}

	@Override
	public DirectedSparseMultigraph<KNetVertex, KNetEdge> getFullGraph() {
		return full_graph;
	}

	@Override
	public DirectedSparseMultigraph<KNetVertex, KNetEdge> getFilteredGraph() {
		return filtered_graph;
	}

	@Override
	public KNetConceptFactory getKNetConceptFactory() {
		return concept_factory;
	}

	@Override
	public KNetSemanticRelationReference getSemanticRelationReference() {
		return semantic_relation_reference;
	}

	@Override
	public HashMap<String, KNetVertex> getVerticesIndex() {
		return vertices_index;
	}

}
