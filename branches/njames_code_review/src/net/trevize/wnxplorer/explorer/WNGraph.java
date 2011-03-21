package net.trevize.wnxplorer.explorer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.trevize.wnxplorer.jung.PointerEdge;
import net.trevize.wnxplorer.jung.SynsetVertex;
import net.trevize.wnxplorer.jwi.WNUtils;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.Pointer;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * WNGraph.java - Mar 24, 2010
 */

public class WNGraph {

	private IDictionary dict;

	//for jung.
	private DirectedSparseMultigraph<SynsetVertex, PointerEdge> full_graph;
	private DirectedSparseMultigraph<SynsetVertex, PointerEdge> viewed_graph;

	//for filtering the graph for the rendering view.
	private boolean show_all_vertices;
	private HashMap<Pointer, Boolean> pointers_list;

	/*
	 * index of the vertices by synset ID.
	 */
	private HashMap<String, SynsetVertex> vertex_idx;

	public WNGraph(IDictionary dict) {
		this.dict = dict;
		full_graph = new DirectedSparseMultigraph<SynsetVertex, PointerEdge>();
		viewed_graph = new DirectedSparseMultigraph<SynsetVertex, PointerEdge>();
		vertex_idx = new HashMap<String, SynsetVertex>();
	}

	public void clear() {
		full_graph = new DirectedSparseMultigraph<SynsetVertex, PointerEdge>();
		viewed_graph = new DirectedSparseMultigraph<SynsetVertex, PointerEdge>();
		vertex_idx = new HashMap<String, SynsetVertex>();
	}

	public SynsetVertex addVertexForSynset(ISynset synset) {
		if (vertex_idx.keySet().contains(synset.getID().toString())) {
			return vertex_idx.get(synset.getID().toString());
		}

		//create the new vertex.
		SynsetVertex synset_vertex = new SynsetVertex(synset);

		//add the vertex to the graph.
		full_graph.addVertex(synset_vertex);
		viewed_graph.addVertex(synset_vertex);

		//index the vertex.
		vertex_idx.put(synset_vertex.getSynset_id(), synset_vertex);

		applyRenderingFilter();

		return synset_vertex;
	}

	public boolean edgeExistsBetweenVerticesForPointerType(
			SynsetVertex vertex_src, SynsetVertex vertex_dest,
			Pointer pointer_type) {
		Iterator<PointerEdge> edges_iter = full_graph.getOutEdges(vertex_src)
				.iterator();

		boolean found = false;
		while (edges_iter.hasNext()) {
			PointerEdge edge = edges_iter.next();
			if (full_graph.getDest(edge) == vertex_dest) {
				if (edge.getPointer_type() == pointer_type) {
					found = true;
					break;
				}
			}
		}

		return found;
	}

	public void develop(SynsetVertex picked_vertex) {
		for (Pointer pointer : WNUtils.getPointers()) {

			List<ISynsetID> pointered_synsets = picked_vertex.getIsynset()
					.getRelatedSynsets(pointer);

			for (ISynsetID synset_id : pointered_synsets) {

				ISynset pointered_synset = dict.getSynset(synset_id);

				//is this synset ever present in the graph ?
				SynsetVertex v = vertex_idx.get(pointered_synset.getID()
						.toString());

				if (v != null) {
					if (edgeExistsBetweenVerticesForPointerType(picked_vertex,
							v, pointer)) {
						continue;
					}

					/*
					 * managing symmetric relations.
					 */
					if (pointer == Pointer.HYPERNYM
							&& edgeExistsBetweenVerticesForPointerType(v,
									picked_vertex, Pointer.HYPONYM)) {
						continue;
					}

					if (pointer == Pointer.HYPONYM
							&& edgeExistsBetweenVerticesForPointerType(v,
									picked_vertex, Pointer.HYPERNYM)) {
						continue;
					}

					if (pointer == Pointer.HYPERNYM_INSTANCE
							&& edgeExistsBetweenVerticesForPointerType(v,
									picked_vertex, Pointer.HYPONYM_INSTANCE)) {
						continue;
					}

					if (pointer == Pointer.HYPONYM_INSTANCE
							&& edgeExistsBetweenVerticesForPointerType(v,
									picked_vertex, Pointer.HYPERNYM_INSTANCE)) {
						continue;
					}

					if (pointer == Pointer.HOLONYM_PART
							&& edgeExistsBetweenVerticesForPointerType(v,
									picked_vertex, Pointer.MERONYM_PART)) {
						continue;
					}

					if (pointer == Pointer.MERONYM_PART
							&& edgeExistsBetweenVerticesForPointerType(v,
									picked_vertex, Pointer.HOLONYM_PART)) {
						continue;
					}

					if (pointer == Pointer.HOLONYM_SUBSTANCE
							&& edgeExistsBetweenVerticesForPointerType(v,
									picked_vertex, Pointer.MERONYM_SUBSTANCE)) {
						continue;
					}

					if (pointer == Pointer.MERONYM_SUBSTANCE
							&& edgeExistsBetweenVerticesForPointerType(v,
									picked_vertex, Pointer.HOLONYM_SUBSTANCE)) {
						continue;
					}

					if (pointer == Pointer.HOLONYM_MEMBER
							&& edgeExistsBetweenVerticesForPointerType(v,
									picked_vertex, Pointer.MERONYM_MEMBER)) {
						continue;
					}

					if (pointer == Pointer.MERONYM_MEMBER
							&& edgeExistsBetweenVerticesForPointerType(v,
									picked_vertex, Pointer.HOLONYM_MEMBER)) {
						continue;
					}
				}

				if (v == null) {
					//add the vertex.
					v = addVertexForSynset(pointered_synset);
				}

				//create the edge.
				PointerEdge e1 = new PointerEdge(pointer);

				//add the edge to the graph.
				full_graph.addEdge(e1, picked_vertex, v);
				viewed_graph.addEdge(e1, picked_vertex, v);

			}//end for all pointered synsets.

		}//end for all pointers types.

		applyRenderingFilter();
	}

	/*
	 * this method is used when the filter over the pointers types has changed.
	 */
	public void setRenderingFilter(HashMap<Pointer, Boolean> pointers_list,
			boolean show_all_vertices) {
		this.pointers_list = pointers_list;
		this.show_all_vertices = show_all_vertices;
	}

	public void applyRenderingFilter() {
		//removing the edges.
		for (PointerEdge pointer_edge : full_graph.getEdges()) {
			if (!pointers_list.get(pointer_edge.getPointer_type())) {
				viewed_graph.removeEdge(pointer_edge);
			} else {
				viewed_graph.addEdge(pointer_edge, full_graph
						.getIncidentVertices(pointer_edge));
			}
		}

		//removing the vertices.
		if (!show_all_vertices) {
			for (SynsetVertex synset_vertex : full_graph.getVertices()) {
				if (viewed_graph.getVertices().contains(synset_vertex)) {
					if (viewed_graph.getNeighborCount(synset_vertex) == 0) {
						viewed_graph.removeVertex(synset_vertex);
					}
				}
			}
		} else {
			for (SynsetVertex synset_vertex : full_graph.getVertices()) {
				if (!viewed_graph.getVertices().contains(synset_vertex)) {
					viewed_graph.addVertex(synset_vertex);
				}
			}
		}
	}

	/***************************************************************************
	 * getters and setters.
	 **************************************************************************/

	public DirectedSparseMultigraph<SynsetVertex, PointerEdge> getG() {
		return viewed_graph;
	}

	public void setG(DirectedSparseMultigraph<SynsetVertex, PointerEdge> g) {
		this.viewed_graph = g;
	}

}
