package net.trevize.wnxplorer.jwi.knetvis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.trevize.knetvis.KNetConcept;
import net.trevize.knetvis.KNetSemanticRelation;
import net.trevize.wnxplorer.jwi.JWIUtils;
import edu.mit.jwi.item.IPointer;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.item.Pointer;

public class WNConcept extends KNetConcept {

	private ISynset synset;
	private String synset_words;
	private POS pos;

	private boolean leaf;
	private static final char CHAR_LEAF_INDICATOR = '\u25C6';

	private String key;
	private String short_label;
	private String label;
	private String tooltip_text;
	private String short_description;
	private String description;
	private String full_description;

	public WNConcept(ISynset synset) {
		this.synset = synset;
		synset_words = JWIUtils.getWords(synset);
		pos = synset.getPOS();

		leaf = (synset.getRelatedSynsets().size() == 1);

		key = synset.getID().toString();
		short_label = synset.getWords().get(0).getLemma();
		label = synset_words;
		tooltip_text = synset_words;
		short_description = "short_description";
		description = "description";
		full_description = "full_description";
	}

	public POS getPOS() {
		return pos;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public String getShortLabel() {
		return (leaf ? CHAR_LEAF_INDICATOR : "") + short_label;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public String getTooltipText() {
		return tooltip_text;
	}

	@Override
	public String getShortDescription() {
		return short_description;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getFullDescription() {
		StringBuffer sb = new StringBuffer();
		sb.append("<html><body class=\"style1\">");

		sb.append("<h1>");
		sb.append(synset.getID().toString());
		sb.append("</h1>");

		sb.append("<b>" + JWIUtils.getWords(synset) + "</b>");
		sb.append("<br/>");
		sb.append(synset.getGloss());

		for (Pointer p : JWIUtils.getPointers()) {
			List<ISynsetID> related = synset.getRelatedSynsets((IPointer) p);

			if (related.size() != 0) {
				sb.append("<h2>");
				sb.append(p.toString());
				sb.append("</h2>");
				sb.append("<ul>");
				for (ISynsetID synset_id : related) {
					sb.append("<li>");
					sb.append("<a href=\"synset_id:" + synset_id + "\">");
					sb.append(synset_id);
					sb.append("</a>");
					sb.append(": <b>"
							+ JWIUtils.getWords(JWIUtils.getWN_JWI_dictionary()
									.getSynset(synset_id)) + "</b>");
					sb.append("</li>");
				}
				sb.append("</ul>");
			}
		}

		sb.append("</body></html>");

		return sb.toString();
	}

	@Override
	public List<KNetConcept> getRelatedConcepts(
			KNetSemanticRelation semantic_relation) {
		Pointer pointer = ((WNSemanticRelation) semantic_relation).getPointer();
		List<ISynsetID> related_synsets = synset.getRelatedSynsets(pointer);

		ArrayList<KNetConcept> related_concepts = new ArrayList<KNetConcept>();
		for (ISynsetID synset_id : related_synsets) {
			related_concepts.add(WNResource.getResource().getKNetConcept(
					synset_id.toString()));
		}

		return related_concepts;
	}

	@Override
	public Map<KNetSemanticRelation, List<KNetConcept>> getRelatedConcepts() {
		Map<IPointer, List<ISynsetID>> related_synsets = synset.getRelatedMap();
		HashMap<KNetSemanticRelation, List<KNetConcept>> related_concepts = new HashMap<KNetSemanticRelation, List<KNetConcept>>();

		for (IPointer pointer : related_synsets.keySet()) {
			ArrayList<KNetConcept> concepts = new ArrayList<KNetConcept>();
			for (ISynsetID synset_id : related_synsets.get(pointer)) {
				concepts.add(new WNConcept(JWIUtils.getWN_JWI_dictionary()
						.getSynset(synset_id)));
			}
			related_concepts.put(WNResource.getResource()
					.getWNSemanticRelation(pointer), concepts);
		}

		return related_concepts;
	}

	@Override
	public boolean isLeaf() {
		return leaf;
	}

}
