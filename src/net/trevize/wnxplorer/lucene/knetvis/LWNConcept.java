package net.trevize.wnxplorer.lucene.knetvis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.trevize.knetvis.KNetConcept;
import net.trevize.knetvis.KNetSemanticRelation;
import net.trevize.wnxplorer.lucene.LWNUtils;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * WordNetSynsetDocument.java - Mar 25, 2010
 */

public class LWNConcept extends KNetConcept {

	private Document document;
	private String synset_words;
	private String synset_pos;

	private boolean leaf;
	private static final char CHAR_LEAF_INDICATOR = '\u25C6';

	private String key;
	private String short_label;
	private String label;
	private String tooltip_text;
	private String short_description;
	private String description;
	private String full_description;

	public LWNConcept(Document document) {
		this.document = document;
		synset_words = document.getField(LWNUtils.SYNSET_DATA_WORD_LEMMA)
				.stringValue();
		synset_pos = document.getField(LWNUtils.SYNSET_DATA_POS).stringValue();
		System.out.println(synset_pos);
		leaf = true;
		key = document.getField(LWNUtils.SYNSET_DATA_ID).stringValue();
		short_label = LWNUtils.getWords(document);
		label = synset_words;
		tooltip_text = synset_words;
		short_description = "short_description";
		description = "description";
		full_description = "full_description";
	}

	public String getPOS() {
		return synset_pos;
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
		sb.append(key);
		sb.append("</h1>");

		sb.append("<b>" + synset_words + "</b>");
		sb.append("<br/>");
		sb.append(document.getField(LWNUtils.SYNSET_DATA_GLOSS));

		for (String semantic_relation_key : LWNResource
				.getSemanticRelationKeys()) {
			Field[] related = document.getFields(semantic_relation_key);
			if (related != null && related.length != 0) {
				sb.append("<h2>");
				sb.append(semantic_relation_key);
				sb.append("</h2>");
				sb.append("<ul>");
				for (Field key : document.getFields(semantic_relation_key)) {
					sb.append("<li>");
					sb.append("<a href=\"synset_id:" + key.stringValue()
							+ "\">");
					sb.append(key.stringValue());
					sb.append("</a>");
					sb.append(": <b>"
							+ LWNUtils.getWords(LWNUtils.getDocument(key
									.stringValue())) + "</b>");
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
		ArrayList<KNetConcept> related_concepts = new ArrayList<KNetConcept>();
		for (Field related : document.getFields(semantic_relation.getKey())) {
			related_concepts.add(LWNResource.getResource().getKNetConcept(
					related.stringValue()));
		}

		return related_concepts;
	}

	@Override
	public Map<KNetSemanticRelation, List<KNetConcept>> getRelatedConcepts() {
		HashMap<KNetSemanticRelation, List<KNetConcept>> related_concepts = new HashMap<KNetSemanticRelation, List<KNetConcept>>();

		for (String semantic_relation_key : LWNResource
				.getSemanticRelationKeys()) {
			ArrayList<KNetConcept> concepts = new ArrayList<KNetConcept>();
			for (Field related : document.getFields(semantic_relation_key)) {
				concepts.add(LWNResource.getResource().getKNetConcept(
						related.stringValue()));
			}
			related_concepts.put(LWNResource.getResource()
					.getLWNSemanticRelation(semantic_relation_key), concepts);
		}

		return related_concepts;
	}

	@Override
	public boolean isLeaf() {
		return leaf;
	}

}
