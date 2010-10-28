package net.trevize.wnxplorer.explorer;

import java.util.ArrayList;
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

	//to index the "ever encountered" synsets.
	//vertices are indexed by the synset ID.
	private HashMap<String, SynsetVertex> vertex_idx_0;

	public WNGraph(IDictionary dict) {
		this.dict = dict;
		full_graph = new DirectedSparseMultigraph<SynsetVertex, PointerEdge>();
		viewed_graph = new DirectedSparseMultigraph<SynsetVertex, PointerEdge>();
		vertex_idx_0 = new HashMap<String, SynsetVertex>();
	}

	public void clear() {
		full_graph = new DirectedSparseMultigraph<SynsetVertex, PointerEdge>();
		viewed_graph = new DirectedSparseMultigraph<SynsetVertex, PointerEdge>();
		vertex_idx_0 = new HashMap<String, SynsetVertex>();
	}

	public SynsetVertex addVertexForSynset(ISynset synset) {
		//get the synset ID.
		String synset_id = synset.getID().toString();

		if (vertex_idx_0.get(synset_id) != null) {
			return vertex_idx_0.get(synset_id);
		}

		//get the synset POS.
		String pos = synset.getPOS().toString();

		//get the words.
		String synset_words = WNUtils.getWords(synset);

		//get the short label.
		String short_label = synset.getWords().get(0).getLemma();

		//create the new vertex.
		SynsetVertex vertex = new SynsetVertex(synset_id, pos, synset_words,
				short_label);

		//add the vertex to the graph.
		full_graph.addVertex(vertex);
		viewed_graph.addVertex(vertex);

		//index the vertex.
		vertex_idx_0.put(synset_id, vertex);

		return vertex;
	}

	public boolean edgeExistsBetweenVerticesForRelationType(
			SynsetVertex vertex_src, SynsetVertex vertex_dest,
			Pointer relation_type) {
		Iterator<PointerEdge> edges_iter = full_graph.getOutEdges(vertex_src)
				.iterator();
		boolean found = false;
		while (edges_iter.hasNext()) {
			PointerEdge edge = edges_iter.next();
			if (full_graph.getDest(edge) == vertex_dest) {
				if (edge.getPointer_type().equals(relation_type)) {
					found = true;
					break;
				}
			}
		}
		return found;
	}

	public void develop(SynsetVertex picked_vertex) {
		//retrieving an ISynset from the vertex.
		ISynset picked_synset = dict.getSynset(WNUtils
				.getISynsetIDFromString(picked_vertex.getSynset_id()));

		for (Pointer pointer : WNUtils.getPointers()) {
			List<ISynsetID> pointered_synsets = picked_synset
					.getRelatedSynsets(pointer);
			for (ISynsetID next_sid : pointered_synsets) {
				ISynset sid = dict.getSynset(next_sid);

				//is this synset ever present in the graph ?
				SynsetVertex v = vertex_idx_0.get(sid.getID().toString());
				if (v != null) {
					if (edgeExistsBetweenVerticesForRelationType(picked_vertex,
							v, pointer)) {
						continue;
					}
					if (edgeExistsBetweenVerticesForRelationType(v,
							picked_vertex, pointer)) {
						continue;
					}
				}

				//add the vertex.
				SynsetVertex v1 = addVertexForSynset(sid);

				//create the edge.
				PointerEdge e1 = new PointerEdge(pointer);

				//add the edge to the graph.
				full_graph.addEdge(e1, picked_vertex, v1);
				viewed_graph.addEdge(e1, picked_vertex, v1);
			}
		}
	}

	public void updateView(HashMap<Pointer, Boolean> pointers_list) {
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
		for (SynsetVertex synset_vertex : full_graph.getVertices()) {
			if (viewed_graph.getNeighborCount(synset_vertex) == 0) {
				viewed_graph.removeVertex(synset_vertex);
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
