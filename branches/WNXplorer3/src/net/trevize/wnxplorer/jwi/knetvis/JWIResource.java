package net.trevize.wnxplorer.jwi.knetvis;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.trevize.knetvis.KNetConcept;
import net.trevize.knetvis.KNetResource;
import net.trevize.knetvis.KNetSemanticRelation;
import net.trevize.tinker.X11Colors2;
import net.trevize.wnxplorer.jwi.JWIUtils;
import edu.mit.jwi.item.IPointer;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.Pointer;

public class JWIResource implements KNetResource {

	private static final JWIResource resource = new JWIResource();

	public static final JWIResource getResource() {
		return resource;
	}

	/***************************************************************************
	 * Part Of Speech
	 **************************************************************************/

	public static final String POS_NOUN = "POS_NOUN";
	public static final String POS_VERB = "POS_VERB";
	public static final String POS_ADJECTIVE = "POS_ADJECTIVE";
	public static final String POS_ADVERB = "POS_ADVERB";

	private static List<String> pos;

	public static List<String> getPOS() {
		if (pos == null) {
			initPOS();
		}
		return pos;
	}

	private static void initPOS() {
		pos = new ArrayList<String>();
		pos.add(POS_NOUN);
		pos.add(POS_VERB);
		pos.add(POS_ADJECTIVE);
		pos.add(POS_ADVERB);
	}

	/***************************************************************************
	 * semantic relation keys
	 **************************************************************************/

	public static final int NUMBER_OF_SEMANTIC_RELATION = 17;

	public static final String SEMANTIC_RELATION_KEY_ALSO_SEE = "SEMANTIC_RELATION_KEY_ALSO_SEE";
	public static final String SEMANTIC_RELATION_KEY_ANTONYM = "SEMANTIC_RELATION_KEY_ANTONYM";
	public static final String SEMANTIC_RELATION_KEY_ATTRIBUTE = "SEMANTIC_RELATION_KEY_ATTRIBUTE";
	public static final String SEMANTIC_RELATION_KEY_CAUSE = "SEMANTIC_RELATION_KEY_CAUSE";
	public static final String SEMANTIC_RELATION_KEY_DERIVATIONALLY_RELATED = "SEMANTIC_RELATION_KEY_DERIVATIONALLY_RELATED";
	public static final String SEMANTIC_RELATION_KEY_DERIVED_FROM_ADJ = "SEMANTIC_RELATION_KEY_DERIVED_FROM_ADJ";
	public static final String SEMANTIC_RELATION_KEY_ENTAILMENT = "SEMANTIC_RELATION_KEY_ENTAILMENT";
	public static final String SEMANTIC_RELATION_KEY_HOLONYM_MEMBER = "SEMANTIC_RELATION_KEY_HOLONYM_MEMBER";
	public static final String SEMANTIC_RELATION_KEY_HOLONYM_PART = "SEMANTIC_RELATION_KEY_HOLONYM_PART";
	public static final String SEMANTIC_RELATION_KEY_HOLONYM_SUBSTANCE = "SEMANTIC_RELATION_KEY_HOLONYM_SUBSTANCE";
	public static final String SEMANTIC_RELATION_KEY_HYPERNYM = "SEMANTIC_RELATION_KEY_HYPERNYM";
	public static final String SEMANTIC_RELATION_KEY_HYPERNYM_INSTANCE = "SEMANTIC_RELATION_KEY_HYPERNYM_INSTANCE";
	public static final String SEMANTIC_RELATION_KEY_HYPONYM = "SEMANTIC_RELATION_KEY_HYPONYM";
	public static final String SEMANTIC_RELATION_KEY_HYPONYM_INSTANCE = "SEMANTIC_RELATION_KEY_HYPONYM_INSTANCE";
	public static final String SEMANTIC_RELATION_KEY_MERONYM_MEMBER = "SEMANTIC_RELATION_KEY_MERONYM_MEMBER";
	public static final String SEMANTIC_RELATION_KEY_MERONYM_PART = "SEMANTIC_RELATION_KEY_MERONYM_PART";
	public static final String SEMANTIC_RELATION_KEY_MERONYM_SUBSTANCE = "SEMANTIC_RELATION_KEY_MERONYM_SUBSTANCE";
	public static final String SEMANTIC_RELATION_KEY_PARTICIPLE = "SEMANTIC_RELATION_KEY_PARTICIPLE";
	public static final String SEMANTIC_RELATION_KEY_PERTAINYM = "SEMANTIC_RELATION_KEY_PERTAINYM";
	public static final String SEMANTIC_RELATION_KEY_REGION = "SEMANTIC_RELATION_KEY_REGION";
	public static final String SEMANTIC_RELATION_KEY_REGION_MEMBER = "SEMANTIC_RELATION_KEY_REGION_MEMBER";
	public static final String SEMANTIC_RELATION_KEY_SIMILAR_TO = "SEMANTIC_RELATION_KEY_SIMILAR_TO";
	public static final String SEMANTIC_RELATION_KEY_TOPIC = "SEMANTIC_RELATION_KEY_TOPIC";
	public static final String SEMANTIC_RELATION_KEY_TOPIC_MEMBER = "SEMANTIC_RELATION_KEY_TOPIC_MEMBER";
	public static final String SEMANTIC_RELATION_KEY_USAGE = "SEMANTIC_RELATION_KEY_USAGE";
	public static final String SEMANTIC_RELATION_KEY_USAGE_MEMBER = "SEMANTIC_RELATION_KEY_USAGE_MEMBER";
	public static final String SEMANTIC_RELATION_KEY_VERB_GROUP = "SEMANTIC_RELATION_KEY_VERB_GROUP";

	private static List<String> semantic_relation_keys;

	public static List<String> getSemanticRelationKeys() {
		if (semantic_relation_keys == null) {
			initSemanticRelationKeys();
		}
		return semantic_relation_keys;
	}

	public static void initSemanticRelationKeys() {
		semantic_relation_keys = new ArrayList<String>();
		semantic_relation_keys.add(SEMANTIC_RELATION_KEY_ALSO_SEE);
		semantic_relation_keys.add(SEMANTIC_RELATION_KEY_ANTONYM);
		semantic_relation_keys.add(SEMANTIC_RELATION_KEY_ATTRIBUTE);
		semantic_relation_keys.add(SEMANTIC_RELATION_KEY_CAUSE);
		semantic_relation_keys
				.add(SEMANTIC_RELATION_KEY_DERIVATIONALLY_RELATED);
		semantic_relation_keys.add(SEMANTIC_RELATION_KEY_DERIVED_FROM_ADJ);
		semantic_relation_keys.add(SEMANTIC_RELATION_KEY_ENTAILMENT);
		semantic_relation_keys.add(SEMANTIC_RELATION_KEY_HOLONYM_MEMBER);
		semantic_relation_keys.add(SEMANTIC_RELATION_KEY_HOLONYM_PART);
		semantic_relation_keys.add(SEMANTIC_RELATION_KEY_HOLONYM_SUBSTANCE);
		semantic_relation_keys.add(SEMANTIC_RELATION_KEY_HYPERNYM);
		semantic_relation_keys.add(SEMANTIC_RELATION_KEY_HYPERNYM_INSTANCE);
		semantic_relation_keys.add(SEMANTIC_RELATION_KEY_HYPONYM);
		semantic_relation_keys.add(SEMANTIC_RELATION_KEY_HYPONYM_INSTANCE);
		semantic_relation_keys.add(SEMANTIC_RELATION_KEY_MERONYM_MEMBER);
		semantic_relation_keys.add(SEMANTIC_RELATION_KEY_MERONYM_PART);
		semantic_relation_keys.add(SEMANTIC_RELATION_KEY_MERONYM_SUBSTANCE);
		semantic_relation_keys.add(SEMANTIC_RELATION_KEY_PARTICIPLE);
		semantic_relation_keys.add(SEMANTIC_RELATION_KEY_PERTAINYM);
		semantic_relation_keys.add(SEMANTIC_RELATION_KEY_REGION);
		semantic_relation_keys.add(SEMANTIC_RELATION_KEY_REGION_MEMBER);
		semantic_relation_keys.add(SEMANTIC_RELATION_KEY_SIMILAR_TO);
		semantic_relation_keys.add(SEMANTIC_RELATION_KEY_TOPIC);
		semantic_relation_keys.add(SEMANTIC_RELATION_KEY_TOPIC_MEMBER);
		semantic_relation_keys.add(SEMANTIC_RELATION_KEY_USAGE);
		semantic_relation_keys.add(SEMANTIC_RELATION_KEY_USAGE_MEMBER);
		semantic_relation_keys.add(SEMANTIC_RELATION_KEY_VERB_GROUP);
	}

	/***************************************************************************
	 * semantic relation names
	 **************************************************************************/

	public static final String SEMANTIC_RELATION_NAME_ALSO_SEE = "SEMANTIC_RELATION_NAME_ALSO_SEE";
	public static final String SEMANTIC_RELATION_NAME_ANTONYM = "SEMANTIC_RELATION_NAME_ANTONYM";
	public static final String SEMANTIC_RELATION_NAME_ATTRIBUTE = "SEMANTIC_RELATION_NAME_ATTRIBUTE";
	public static final String SEMANTIC_RELATION_NAME_CAUSE = "SEMANTIC_RELATION_NAME_CAUSE";
	public static final String SEMANTIC_RELATION_NAME_DERIVATIONALLY_RELATED = "SEMANTIC_RELATION_NAME_DERIVATIONALLY_RELATED";
	public static final String SEMANTIC_RELATION_NAME_DERIVED_FROM_ADJ = "SEMANTIC_RELATION_NAME_DERIVED_FROM_ADJ";
	public static final String SEMANTIC_RELATION_NAME_ENTAILMENT = "SEMANTIC_RELATION_NAME_ENTAILMENT";
	public static final String SEMANTIC_RELATION_NAME_HOLONYM_MEMBER = "SEMANTIC_RELATION_NAME_HOLONYM_MEMBER";
	public static final String SEMANTIC_RELATION_NAME_HOLONYM_PART = "SEMANTIC_RELATION_NAME_HOLONYM_PART";
	public static final String SEMANTIC_RELATION_NAME_HOLONYM_SUBSTANCE = "SEMANTIC_RELATION_NAME_HOLONYM_SUBSTANCE";
	public static final String SEMANTIC_RELATION_NAME_HYPERNYM = "SEMANTIC_RELATION_NAME_HYPERNYM";
	public static final String SEMANTIC_RELATION_NAME_HYPERNYM_INSTANCE = "SEMANTIC_RELATION_NAME_HYPERNYM_INSTANCE";
	public static final String SEMANTIC_RELATION_NAME_HYPONYM = "SEMANTIC_RELATION_NAME_HYPONYM";
	public static final String SEMANTIC_RELATION_NAME_HYPONYM_INSTANCE = "SEMANTIC_RELATION_NAME_HYPONYM_INSTANCE";
	public static final String SEMANTIC_RELATION_NAME_MERONYM_MEMBER = "SEMANTIC_RELATION_NAME_MERONYM_MEMBER";
	public static final String SEMANTIC_RELATION_NAME_MERONYM_PART = "SEMANTIC_RELATION_NAME_MERONYM_PART";
	public static final String SEMANTIC_RELATION_NAME_MERONYM_SUBSTANCE = "SEMANTIC_RELATION_NAME_MERONYM_SUBSTANCE";
	public static final String SEMANTIC_RELATION_NAME_PARTICIPLE = "SEMANTIC_RELATION_NAME_PARTICIPLE";
	public static final String SEMANTIC_RELATION_NAME_PERTAINYM = "SEMANTIC_RELATION_NAME_PERTAINYM";
	public static final String SEMANTIC_RELATION_NAME_REGION = "SEMANTIC_RELATION_NAME_REGION";
	public static final String SEMANTIC_RELATION_NAME_REGION_MEMBER = "SEMANTIC_RELATION_NAME_REGION_MEMBER";
	public static final String SEMANTIC_RELATION_NAME_SIMILAR_TO = "SEMANTIC_RELATION_NAME_SIMILAR_TO";
	public static final String SEMANTIC_RELATION_NAME_TOPIC = "SEMANTIC_RELATION_NAME_TOPIC";
	public static final String SEMANTIC_RELATION_NAME_TOPIC_MEMBER = "SEMANTIC_RELATION_NAME_TOPIC_MEMBER";
	public static final String SEMANTIC_RELATION_NAME_USAGE = "SEMANTIC_RELATION_NAME_USAGE";
	public static final String SEMANTIC_RELATION_NAME_USAGE_MEMBER = "SEMANTIC_RELATION_NAME_USAGE_MEMBER";
	public static final String SEMANTIC_RELATION_NAME_VERB_GROUP = "SEMANTIC_RELATION_NAME_VERB_GROUP";

	private static List<String> semantic_relation_names;

	public static List<String> getSemantic_relation_names() {
		if (semantic_relation_names == null) {
			initSemanticRelationNames();
		}
		return semantic_relation_names;
	}

	private static void initSemanticRelationNames() {
		semantic_relation_names = new ArrayList<String>();
		semantic_relation_names.add(SEMANTIC_RELATION_NAME_ALSO_SEE);
		semantic_relation_names.add(SEMANTIC_RELATION_NAME_ANTONYM);
		semantic_relation_names.add(SEMANTIC_RELATION_NAME_ATTRIBUTE);
		semantic_relation_names.add(SEMANTIC_RELATION_NAME_CAUSE);
		semantic_relation_names
				.add(SEMANTIC_RELATION_NAME_DERIVATIONALLY_RELATED);
		semantic_relation_names.add(SEMANTIC_RELATION_NAME_DERIVED_FROM_ADJ);
		semantic_relation_names.add(SEMANTIC_RELATION_NAME_ENTAILMENT);
		semantic_relation_names.add(SEMANTIC_RELATION_NAME_HOLONYM_MEMBER);
		semantic_relation_names.add(SEMANTIC_RELATION_NAME_HOLONYM_PART);
		semantic_relation_names.add(SEMANTIC_RELATION_NAME_HOLONYM_SUBSTANCE);
		semantic_relation_names.add(SEMANTIC_RELATION_NAME_HYPERNYM);
		semantic_relation_names.add(SEMANTIC_RELATION_NAME_HYPERNYM_INSTANCE);
		semantic_relation_names.add(SEMANTIC_RELATION_NAME_HYPONYM);
		semantic_relation_names.add(SEMANTIC_RELATION_NAME_HYPONYM_INSTANCE);
		semantic_relation_names.add(SEMANTIC_RELATION_NAME_MERONYM_MEMBER);
		semantic_relation_names.add(SEMANTIC_RELATION_NAME_MERONYM_PART);
		semantic_relation_names.add(SEMANTIC_RELATION_NAME_MERONYM_SUBSTANCE);
		semantic_relation_names.add(SEMANTIC_RELATION_NAME_PARTICIPLE);
		semantic_relation_names.add(SEMANTIC_RELATION_NAME_PERTAINYM);
		semantic_relation_names.add(SEMANTIC_RELATION_NAME_REGION);
		semantic_relation_names.add(SEMANTIC_RELATION_NAME_REGION_MEMBER);
		semantic_relation_names.add(SEMANTIC_RELATION_NAME_SIMILAR_TO);
		semantic_relation_names.add(SEMANTIC_RELATION_NAME_TOPIC);
		semantic_relation_names.add(SEMANTIC_RELATION_NAME_TOPIC_MEMBER);
		semantic_relation_names.add(SEMANTIC_RELATION_NAME_USAGE);
		semantic_relation_names.add(SEMANTIC_RELATION_NAME_USAGE_MEMBER);
		semantic_relation_names.add(SEMANTIC_RELATION_NAME_VERB_GROUP);
	}

	/***************************************************************************
	 * semantic relation colors
	 **************************************************************************/

	private static HashMap<String, Color> semantic_relation_color;

	public static HashMap<String, Color> getSemanticRelationColor() {
		if (semantic_relation_color == null) {
			initPointersColor();
		}
		return semantic_relation_color;
	}

	private static void initPointersColor() {
		semantic_relation_color = new HashMap<String, Color>();

		X11Colors2.excludeGray();
		X11Colors2.excludeGrey();
		X11Colors2.excludeLight();
		X11Colors2.excludeDark();
		X11Colors2.excludeBlack();

		int nb_x11_color = X11Colors2.getX11_colors().size();
		int pitch = (int) (nb_x11_color / NUMBER_OF_SEMANTIC_RELATION);

		for (int i = 0; i < NUMBER_OF_SEMANTIC_RELATION; ++i) {
			int color_index = pitch * i + (pitch / 2);
			String color_name = X11Colors2.getX11_colors().keySet()
					.toArray(new String[] {})[color_index];
			semantic_relation_color.put(getSemanticRelationKeys().get(i),
					new Color(X11Colors2.getX11_colors().get(color_name)));
		}
	}

	/***************************************************************************
	 * semantic relation opposites
	 **************************************************************************/

	public static final String SEMANTIC_RELATION_OPPOSITE_ALSO_SEE = "SEMANTIC_RELATION_OPPOSITE_ALSO_SEE";
	public static final String SEMANTIC_RELATION_OPPOSITE_ANTONYM = "SEMANTIC_RELATION_OPPOSITE_ANTONYM";
	public static final String SEMANTIC_RELATION_OPPOSITE_ATTRIBUTE = "SEMANTIC_RELATION_OPPOSITE_ATTRIBUTE";
	public static final String SEMANTIC_RELATION_OPPOSITE_CAUSE = "SEMANTIC_RELATION_OPPOSITE_CAUSE";
	public static final String SEMANTIC_RELATION_OPPOSITE_DERIVATIONALLY_RELATED = "SEMANTIC_RELATION_OPPOSITE_DERIVATIONALLY_RELATED";
	public static final String SEMANTIC_RELATION_OPPOSITE_DERIVED_FROM_ADJ = "SEMANTIC_RELATION_OPPOSITE_DERIVED_FROM_ADJ";
	public static final String SEMANTIC_RELATION_OPPOSITE_ENTAILMENT = "SEMANTIC_RELATION_OPPOSITE_ENTAILMENT";
	public static final String SEMANTIC_RELATION_OPPOSITE_HOLONYM_MEMBER = "SEMANTIC_RELATION_OPPOSITE_HOLONYM_MEMBER";
	public static final String SEMANTIC_RELATION_OPPOSITE_HOLONYM_PART = "SEMANTIC_RELATION_OPPOSITE_HOLONYM_PART";
	public static final String SEMANTIC_RELATION_OPPOSITE_HOLONYM_SUBSTANCE = "SEMANTIC_RELATION_OPPOSITE_HOLONYM_SUBSTANCE";
	public static final String SEMANTIC_RELATION_OPPOSITE_HYPERNYM = "SEMANTIC_RELATION_OPPOSITE_HYPERNYM";
	public static final String SEMANTIC_RELATION_OPPOSITE_HYPERNYM_INSTANCE = "SEMANTIC_RELATION_OPPOSITE_HYPERNYM_INSTANCE";
	public static final String SEMANTIC_RELATION_OPPOSITE_HYPONYM = "SEMANTIC_RELATION_OPPOSITE_HYPONYM";
	public static final String SEMANTIC_RELATION_OPPOSITE_HYPONYM_INSTANCE = "SEMANTIC_RELATION_OPPOSITE_HYPONYM_INSTANCE";
	public static final String SEMANTIC_RELATION_OPPOSITE_MERONYM_MEMBER = "SEMANTIC_RELATION_OPPOSITE_MERONYM_MEMBER";
	public static final String SEMANTIC_RELATION_OPPOSITE_MERONYM_PART = "SEMANTIC_RELATION_OPPOSITE_MERONYM_PART";
	public static final String SEMANTIC_RELATION_OPPOSITE_MERONYM_SUBSTANCE = "SEMANTIC_RELATION_OPPOSITE_MERONYM_SUBSTANCE";
	public static final String SEMANTIC_RELATION_OPPOSITE_PARTICIPLE = "SEMANTIC_RELATION_OPPOSITE_PARTICIPLE";
	public static final String SEMANTIC_RELATION_OPPOSITE_PERTAINYM = "SEMANTIC_RELATION_OPPOSITE_PERTAINYM";
	public static final String SEMANTIC_RELATION_OPPOSITE_REGION = "SEMANTIC_RELATION_OPPOSITE_REGION";
	public static final String SEMANTIC_RELATION_OPPOSITE_REGION_MEMBER = "SEMANTIC_RELATION_OPPOSITE_REGION_MEMBER";
	public static final String SEMANTIC_RELATION_OPPOSITE_SIMILAR_TO = "SEMANTIC_RELATION_OPPOSITE_SIMILAR_TO";
	public static final String SEMANTIC_RELATION_OPPOSITE_TOPIC = "SEMANTIC_RELATION_OPPOSITE_TOPIC";
	public static final String SEMANTIC_RELATION_OPPOSITE_TOPIC_MEMBER = "SEMANTIC_RELATION_OPPOSITE_TOPIC_MEMBER";
	public static final String SEMANTIC_RELATION_OPPOSITE_USAGE = "SEMANTIC_RELATION_OPPOSITE_USAGE";
	public static final String SEMANTIC_RELATION_OPPOSITE_USAGE_MEMBER = "SEMANTIC_RELATION_OPPOSITE_USAGE_MEMBER";
	public static final String SEMANTIC_RELATION_OPPOSITE_VERB_GROUP = "SEMANTIC_RELATION_OPPOSITE_VERB_GROUP";

	private ArrayList<String> semantic_relation_opposites;

	public ArrayList<String> getSemanticRelationOpposites() {
		if (semantic_relation_opposites == null) {
			initSemanticRelationOpposites();
		}
		return semantic_relation_opposites;
	}

	private void initSemanticRelationOpposites() {
		semantic_relation_opposites = new ArrayList<String>();
		semantic_relation_opposites.add(SEMANTIC_RELATION_OPPOSITE_ALSO_SEE);
		semantic_relation_opposites.add(SEMANTIC_RELATION_OPPOSITE_ANTONYM);
		semantic_relation_opposites.add(SEMANTIC_RELATION_OPPOSITE_ATTRIBUTE);
		semantic_relation_opposites.add(SEMANTIC_RELATION_OPPOSITE_CAUSE);
		semantic_relation_opposites
				.add(SEMANTIC_RELATION_OPPOSITE_DERIVATIONALLY_RELATED);
		semantic_relation_opposites
				.add(SEMANTIC_RELATION_OPPOSITE_DERIVED_FROM_ADJ);
		semantic_relation_opposites.add(SEMANTIC_RELATION_OPPOSITE_ENTAILMENT);
		semantic_relation_opposites
				.add(SEMANTIC_RELATION_OPPOSITE_HOLONYM_MEMBER);
		semantic_relation_opposites
				.add(SEMANTIC_RELATION_OPPOSITE_HOLONYM_PART);
		semantic_relation_opposites
				.add(SEMANTIC_RELATION_OPPOSITE_HOLONYM_SUBSTANCE);
		semantic_relation_opposites.add(SEMANTIC_RELATION_OPPOSITE_HYPERNYM);
		semantic_relation_opposites
				.add(SEMANTIC_RELATION_OPPOSITE_HYPERNYM_INSTANCE);
		semantic_relation_opposites.add(SEMANTIC_RELATION_OPPOSITE_HYPONYM);
		semantic_relation_opposites
				.add(SEMANTIC_RELATION_OPPOSITE_HYPONYM_INSTANCE);
		semantic_relation_opposites
				.add(SEMANTIC_RELATION_OPPOSITE_MERONYM_MEMBER);
		semantic_relation_opposites
				.add(SEMANTIC_RELATION_OPPOSITE_MERONYM_PART);
		semantic_relation_opposites
				.add(SEMANTIC_RELATION_OPPOSITE_MERONYM_SUBSTANCE);
		semantic_relation_opposites.add(SEMANTIC_RELATION_OPPOSITE_PARTICIPLE);
		semantic_relation_opposites.add(SEMANTIC_RELATION_OPPOSITE_PERTAINYM);
		semantic_relation_opposites.add(SEMANTIC_RELATION_OPPOSITE_REGION);
		semantic_relation_opposites
				.add(SEMANTIC_RELATION_OPPOSITE_REGION_MEMBER);
		semantic_relation_opposites.add(SEMANTIC_RELATION_OPPOSITE_SIMILAR_TO);
		semantic_relation_opposites.add(SEMANTIC_RELATION_OPPOSITE_TOPIC);
		semantic_relation_opposites
				.add(SEMANTIC_RELATION_OPPOSITE_TOPIC_MEMBER);
		semantic_relation_opposites.add(SEMANTIC_RELATION_OPPOSITE_USAGE);
		semantic_relation_opposites
				.add(SEMANTIC_RELATION_OPPOSITE_USAGE_MEMBER);
		semantic_relation_opposites.add(SEMANTIC_RELATION_OPPOSITE_VERB_GROUP);
	}

	/**************************************************************************/

	private List<KNetSemanticRelation> semantic_relation_list;
	private HashMap<IPointer, JWISemanticRelation> pointer_index;

	private JWIResource() {
		semantic_relation_list = new ArrayList<KNetSemanticRelation>();
		pointer_index = new HashMap<IPointer, JWISemanticRelation>();
		init();
	}

	private void init() {
		for (Pointer pointer : JWIUtils.getPointers()) {
			JWISemanticRelation semantic_relation = new JWISemanticRelation(
					pointer);
			semantic_relation
					.setColor(JWIUtils.getPointersColor().get(pointer));
			semantic_relation_list.add(semantic_relation);
			pointer_index.put(pointer, semantic_relation);
		}

		//setting the opposite
		for (Pointer pointer : JWIUtils.getPointers()) {
			JWISemanticRelation semantic_relation = pointer_index.get(pointer);
			semantic_relation.setOpposite(pointer_index.get(JWIUtils
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
