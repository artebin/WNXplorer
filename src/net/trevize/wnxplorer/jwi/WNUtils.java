package net.trevize.wnxplorer.jwi;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.trevize.tinker.X11Colors2;
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
			String color_name = X11Colors2.getX11_colors().keySet().toArray(
					new String[] {})[color_index];
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

	public static ISynsetID getISynsetIDFromString(String synset_id_string) {
		//retrieving an ISynset from the vertex.
		int offset = Integer.parseInt(synset_id_string.split("-")[1]); //get the offset.
		char pos = synset_id_string.split("-")[2].charAt(0); //get the Part Of Speech.
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

	public static String getSynsetHTMLDescription(IDictionary dict,
			ISynset synset) {
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
					sb.append(synset_id + ": "
							+ WNUtils.getWords(dict.getSynset(synset_id)));
					sb.append("</li>");
				}
				sb.append("</ul>");
			}
		}

		sb.append("</body></html>");

		return sb.toString();
	}

}
