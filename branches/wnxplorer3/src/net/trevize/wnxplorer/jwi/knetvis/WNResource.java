package net.trevize.wnxplorer.jwi.knetvis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.trevize.knetvis.KNetConcept;
import net.trevize.knetvis.KNetResource;
import net.trevize.knetvis.KNetSemanticRelation;
import net.trevize.wnxplorer.jwi.JWIUtils;
import edu.mit.jwi.item.IPointer;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.Pointer;

public class WNResource implements KNetResource {

	private static final WNResource resource = new WNResource();

	public static final WNResource getResource() {
		return resource;
	}

	private List<KNetSemanticRelation> semantic_relation_list;
	private HashMap<IPointer, WNSemanticRelation> pointer_index;

	private WNResource() {
		semantic_relation_list = new ArrayList<KNetSemanticRelation>();
		pointer_index = new HashMap<IPointer, WNSemanticRelation>();
		init();
	}

	private void init() {
		for (Pointer pointer : JWIUtils.getPointers()) {
			WNSemanticRelation wn_semrel = new WNSemanticRelation(pointer);
			wn_semrel.setColor(JWIUtils.getPointersColor().get(pointer));
			semantic_relation_list.add(wn_semrel);
			pointer_index.put(pointer, wn_semrel);
		}

		//setting the opposite
		for (Pointer pointer : JWIUtils.getPointers()) {
			WNSemanticRelation wn_semrel = pointer_index.get(pointer);
			wn_semrel.setOpposite(pointer_index.get(JWIUtils
					.getOpposite(pointer)));
		}
	}

	public WNSemanticRelation getWNSemanticRelation(IPointer pointer) {
		return pointer_index.get(pointer);
	}

	/***************************************************************************
	 * implementation of KNetResource
	 **************************************************************************/

	@Override
	public KNetConcept getKNetConcept(String key) {
		ISynsetID synset_id = JWIUtils.getISynsetIDFromString(key);
		ISynset synset = JWIUtils.getWN_JWI_dictionary().getSynset(synset_id);
		WNConcept concept = new WNConcept(synset);
		return concept;
	}

	@Override
	public List<KNetSemanticRelation> getSemanticRelationList() {
		return semantic_relation_list;
	}

}
