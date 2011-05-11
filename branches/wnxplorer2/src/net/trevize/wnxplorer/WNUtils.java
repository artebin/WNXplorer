package net.trevize.wnxplorer;

import java.awt.Color;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.trevize.tinker.X11Colors2;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IPointer;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.item.Pointer;
import edu.mit.jwi.item.SynsetID;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * WNUtils.java - Apr 6, 2010
 */

public class WNUtils {

	private static String wn_dict_path;

	public static String getWN_dict_path() {
		return wn_dict_path;
	}

	public static void setWN_dict_path(String wn_dict_path) {
		WNUtils.wn_dict_path = wn_dict_path;
	}

	private static IDictionary wn_jwi_dictionary;

	public static IDictionary getWN_JWI_dictionary() {
		if (wn_jwi_dictionary == null) {
			initWN_JWI_dictionary();
		}
		return wn_jwi_dictionary;
	}

	private static void initWN_JWI_dictionary() {
		if (wn_dict_path == null) {
			return;
		}

		URL url = null;
		try {
			url = new URL("file", null, wn_dict_path);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		wn_jwi_dictionary = new Dictionary(url);

		//we try to open the dictionary
		try {
			wn_jwi_dictionary.open();

			/*
			 * Try to do a request for testing the WordNet installation path. It is
			 * necessary as it is not tested in the Dictionary constructor.
			 */
			wn_jwi_dictionary.getSynset(new SynsetID(0, POS.NOUN));
		} catch (Exception e) {
			wn_dict_path = null;
		}
	}

	private static ArrayList<Pointer> pointers;

	public static ArrayList<Pointer> getPointers() {
		if (pointers == null) {
			initPointers();
		}
		return pointers;
	}

	private static void initPointers() {
		pointers = new ArrayList<Pointer>();
		pointers.add(Pointer.ALSO_SEE);
		pointers.add(Pointer.ANTONYM);
		pointers.add(Pointer.ATTRIBUTE);
		pointers.add(Pointer.CAUSE);
		pointers.add(Pointer.DERIVATIONALLY_RELATED);
		pointers.add(Pointer.DERIVED_FROM_ADJ);
		pointers.add(Pointer.ENTAILMENT);
		pointers.add(Pointer.HOLONYM_MEMBER);
		pointers.add(Pointer.HOLONYM_PART);
		pointers.add(Pointer.HOLONYM_SUBSTANCE);
		pointers.add(Pointer.HYPERNYM);
		pointers.add(Pointer.HYPERNYM_INSTANCE);
		pointers.add(Pointer.HYPONYM);
		pointers.add(Pointer.HYPONYM_INSTANCE);
		pointers.add(Pointer.MERONYM_MEMBER);
		pointers.add(Pointer.MERONYM_PART);
		pointers.add(Pointer.MERONYM_SUBSTANCE);
		pointers.add(Pointer.PARTICIPLE);
		pointers.add(Pointer.PERTAINYM);
		pointers.add(Pointer.REGION);
		pointers.add(Pointer.REGION_MEMBER);
		pointers.add(Pointer.SIMILAR_TO);
		pointers.add(Pointer.TOPIC);
		pointers.add(Pointer.TOPIC_MEMBER);
		pointers.add(Pointer.USAGE);
		pointers.add(Pointer.USAGE_MEMBER);
		pointers.add(Pointer.VERB_GROUP);
	}

	private static HashMap<Pointer, Boolean> symmetric;

	public static boolean isSymmetric(Pointer pointer) {
		if (symmetric == null) {
			initSymmetric();
		}
		return symmetric.get(pointer);
	}

	private static void initSymmetric() {
		symmetric = new HashMap<Pointer, Boolean>();
		symmetric.put(Pointer.ALSO_SEE, true);
		symmetric.put(Pointer.ANTONYM, true);
		symmetric.put(Pointer.ATTRIBUTE, false);
		symmetric.put(Pointer.CAUSE, false);
		symmetric.put(Pointer.DERIVATIONALLY_RELATED, false);
		symmetric.put(Pointer.DERIVED_FROM_ADJ, false);
		symmetric.put(Pointer.ENTAILMENT, false);
		symmetric.put(Pointer.HOLONYM_MEMBER, false);
		symmetric.put(Pointer.HOLONYM_PART, false);
		symmetric.put(Pointer.HOLONYM_SUBSTANCE, false);
		symmetric.put(Pointer.HYPERNYM, false);
		symmetric.put(Pointer.HYPERNYM_INSTANCE, false);
		symmetric.put(Pointer.HYPONYM, false);
		symmetric.put(Pointer.HYPONYM_INSTANCE, false);
		symmetric.put(Pointer.MERONYM_MEMBER, false);
		symmetric.put(Pointer.MERONYM_PART, false);
		symmetric.put(Pointer.MERONYM_SUBSTANCE, false);
		symmetric.put(Pointer.PARTICIPLE, false);
		symmetric.put(Pointer.PERTAINYM, false);
		symmetric.put(Pointer.REGION, true);
		symmetric.put(Pointer.REGION_MEMBER, true);
		symmetric.put(Pointer.SIMILAR_TO, true);
		symmetric.put(Pointer.TOPIC, true);
		symmetric.put(Pointer.TOPIC_MEMBER, true);
		symmetric.put(Pointer.USAGE, false);
		symmetric.put(Pointer.USAGE_MEMBER, false);
		symmetric.put(Pointer.VERB_GROUP, true);
	}

	private static HashMap<Pointer, Pointer> opposites;

	public static Pointer getOpposite(Pointer pointer) {
		if (opposites == null) {
			initOpposites();
		}
		return opposites.get(pointer);
	}

	private static void initOpposites() {
		opposites = new HashMap<Pointer, Pointer>();
		opposites.put(Pointer.ALSO_SEE, null);
		opposites.put(Pointer.ANTONYM, null);
		opposites.put(Pointer.ATTRIBUTE, null);
		opposites.put(Pointer.CAUSE, null);
		opposites.put(Pointer.DERIVATIONALLY_RELATED, null);
		opposites.put(Pointer.DERIVED_FROM_ADJ, null);
		opposites.put(Pointer.ENTAILMENT, null);
		opposites.put(Pointer.HOLONYM_MEMBER, Pointer.MERONYM_MEMBER);
		opposites.put(Pointer.HOLONYM_PART, Pointer.MERONYM_PART);
		opposites.put(Pointer.HOLONYM_SUBSTANCE, Pointer.MERONYM_SUBSTANCE);
		opposites.put(Pointer.HYPERNYM, Pointer.HYPONYM);
		opposites.put(Pointer.HYPERNYM_INSTANCE, Pointer.HYPONYM_INSTANCE);
		opposites.put(Pointer.HYPONYM, Pointer.HYPERNYM);
		opposites.put(Pointer.HYPONYM_INSTANCE, Pointer.HYPERNYM_INSTANCE);
		opposites.put(Pointer.MERONYM_MEMBER, Pointer.HOLONYM_MEMBER);
		opposites.put(Pointer.MERONYM_PART, Pointer.HOLONYM_PART);
		opposites.put(Pointer.MERONYM_SUBSTANCE, Pointer.HOLONYM_SUBSTANCE);
		opposites.put(Pointer.PARTICIPLE, null);
		opposites.put(Pointer.PERTAINYM, null);
		opposites.put(Pointer.REGION, null);
		opposites.put(Pointer.REGION_MEMBER, null);
		opposites.put(Pointer.SIMILAR_TO, null);
		opposites.put(Pointer.TOPIC, null);
		opposites.put(Pointer.TOPIC_MEMBER, null);
		opposites.put(Pointer.USAGE, null);
		opposites.put(Pointer.USAGE_MEMBER, null);
		opposites.put(Pointer.VERB_GROUP, null);
	}

	private static HashMap<Pointer, Color> pointers_color;

	public static HashMap<Pointer, Color> getPointersColor() {
		if (pointers_color == null) {
			initPointersColor();
		}
		return pointers_color;
	}

	private static void initPointersColor() {
		pointers_color = new HashMap<Pointer, Color>();

		X11Colors2.excludeGray();
		X11Colors2.excludeGrey();
		X11Colors2.excludeLight();
		X11Colors2.excludeDark();
		X11Colors2.excludeBlack();

		int nb_x11_color = X11Colors2.getX11_colors().size();
		int nb_pointers = getPointers().size();
		int pitch = (int) (nb_x11_color / nb_pointers);

		for (Pointer pointer : getPointers()) {
			int pointer_index = WNUtils.getPointers().indexOf(pointer);
			int color_index = pitch * pointer_index + (pitch / 2);
			String color_name = X11Colors2.getX11_colors().keySet()
					.toArray(new String[] {})[color_index];
			pointers_color.put(pointer, new Color(X11Colors2.getX11_colors()
					.get(color_name)));
		}
	}

	/*
	 * As there is no POS descriptions in JWI, we introduced them here.
	 */
	private static HashMap<POS, String> pos_labels;

	public static HashMap<POS, String> getPOSLabels() {
		if (pos_labels == null) {
			initPOSLabels();
		}
		return pos_labels;
	}

	private static void initPOSLabels() {
		pos_labels = new HashMap<POS, String>();
		pos_labels.put(POS.ADJECTIVE, "ADJECTIF");
		pos_labels.put(POS.ADVERB, "ADVERB");
		pos_labels.put(POS.NOUN, "NOUN");
		pos_labels.put(POS.VERB, "VERB");
	}

	public static ISynsetID getISynsetIDFromString(String concept_key) {
		//retrieving an ISynset from the key
		int offset = Integer.parseInt(concept_key.split("-")[1]); //get the offset.
		char pos = concept_key.split("-")[2].charAt(0); //get the Part Of Speech.
		SynsetID synset_id = new SynsetID(offset, POS.getPartOfSpeech(pos));
		return synset_id;
	}

	public static String getWords(ISynset synset) {
		//get the words.
		StringBuffer sb = new StringBuffer();
		List<IWord> words = synset.getWords();
		sb.append("{ ");
		sb.append(words.get(0).getLemma());
		for (int i = 1; i < words.size(); ++i) {
			sb.append(" , " + words.get(i).getLemma());
		}
		sb.append(" }");
		String synset_words = sb.toString();
		return synset_words;
	}

	public static String getSynsetHTMLDescription(ISynset synset) {
		StringBuffer sb = new StringBuffer();
		sb.append("<html><body>");

		sb.append("<h1>");
		sb.append(synset.getID().toString());
		sb.append("</h1>");

		sb.append(WNUtils.getWords(synset));
		sb.append("<br/>");
		sb.append(synset.getGloss());

		for (Pointer p : WNUtils.getPointers()) {
			List<ISynsetID> related = synset.getRelatedSynsets((IPointer) p);

			if (related.size() != 0) {
				sb.append("<h2>");
				sb.append(p.toString());
				sb.append("</h2>");
				sb.append("<ul>");
				for (ISynsetID synset_id : related) {
					sb.append("<li>");
					sb.append(synset_id
							+ ": "
							+ WNUtils.getWords(wn_jwi_dictionary
									.getSynset(synset_id)));
					sb.append("</li>");
				}
				sb.append("</ul>");
			}
		}

		sb.append("</body></html>");

		return sb.toString();
	}
}
