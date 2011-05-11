package net.trevize.wnxplorer.knetvis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.trevize.knetvis.KNetSemanticRelation;
import net.trevize.knetvis.KNetSemanticRelationReference;
import net.trevize.wnxplorer.WNUtils;
import edu.mit.jwi.item.IPointer;
import edu.mit.jwi.item.Pointer;

public class WNSemanticRelationReference extends KNetSemanticRelationReference {

	private List<KNetSemanticRelation> semantic_relation_list;
	private HashMap<IPointer, WNSemanticRelation> pointer_index;

	public WNSemanticRelationReference() {
		semantic_relation_list = new ArrayList<KNetSemanticRelation>();
		pointer_index = new HashMap<IPointer, WNSemanticRelation>();
		init();
	}

	private void init() {
		for (Pointer pointer : WNUtils.getPointers()) {
			WNSemanticRelation wn_semrel = new WNSemanticRelation(pointer);
			wn_semrel.setColor(WNUtils.getPointersColor().get(pointer));
			semantic_relation_list.add(wn_semrel);
			pointer_index.put(pointer, wn_semrel);
		}

		//setting the opposite
		for (Pointer pointer : WNUtils.getPointers()) {
			WNSemanticRelation wn_semrel = pointer_index.get(pointer);
			wn_semrel.setOpposite(pointer_index.get(WNUtils
					.getOpposite(pointer)));
		}
	}

	public WNSemanticRelation getWNSemanticRelation(IPointer pointer) {
		return pointer_index.get(pointer);
	}

	/***************************************************************************
	 * implementation of KNetSemanticRelationReference
	 **************************************************************************/

	@Override
	public List<KNetSemanticRelation> getSemanticRelationList() {
		return semantic_relation_list;
	}

}
