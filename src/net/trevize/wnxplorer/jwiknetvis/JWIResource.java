package net.trevize.wnxplorer.jwiknetvis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.trevize.knetvis.KNetConcept;
import net.trevize.knetvis.KNetResource;
import net.trevize.knetvis.KNetSemanticRelation;
import edu.mit.jwi.item.IPointer;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.Pointer;

public class JWIResource implements KNetResource {

	private static final JWIResource resource = new JWIResource();

	public static final JWIResource getResource() {
		return resource;
	}

	private List<KNetSemanticRelation> semantic_relation_list;
	private HashMap<IPointer, JWISemanticRelation> pointer_index;

	private JWIResource() {
		semantic_relation_list = new ArrayList<KNetSemanticRelation>();
		pointer_index = new HashMap<IPointer, JWISemanticRelation>();
		init();
	}

	private void init() {
		for (Pointer pointer : JWIUtils.getPointers()) {
			JWISemanticRelation wn_semrel = new JWISemanticRelation(pointer);
			wn_semrel.setColor(JWIUtils.getPointersColor().get(pointer));
			semantic_relation_list.add(wn_semrel);
			pointer_index.put(pointer, wn_semrel);
		}

		//setting the opposite
		for (Pointer pointer : JWIUtils.getPointers()) {
			JWISemanticRelation wn_semrel = pointer_index.get(pointer);
			wn_semrel.setOpposite(pointer_index.get(JWIUtils
					.getOpposite(pointer)));
		}
	}

	public JWISemanticRelation getWNSemanticRelation(IPointer pointer) {
		return pointer_index.get(pointer);
	}

	/***************************************************************************
	 * implementation of KNetResource
	 **************************************************************************/

	@Override
	public KNetConcept getKNetConcept(String key) {
		ISynsetID synset_id = JWIUtils.getISynsetIDFromString(key);
		ISynset synset = JWIUtils.getWN_JWI_dictionary().getSynset(synset_id);
		JWIConcept concept = new JWIConcept(synset);
		return concept;
	}

	@Override
	public List<KNetSemanticRelation> getSemanticRelationList() {
		return semantic_relation_list;
	}

}
