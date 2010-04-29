package net.trevize.wordnet;

import java.util.ArrayList;
import java.util.List;

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

	public static String getWords(ISynset synset) {
		//get the words.
		StringBuffer sb = new StringBuffer();
		List<IWord> words = synset.getWords();
		sb.append("{ ");
		sb.append(words.get(0).getLemma());
		for (int i = 1; i < words.size(); ++i) {
			sb.append(" , " + words.get(i).getLemma());
		}
		sb.append(" a}");
		String synset_words = sb.toString();
		return synset_words;
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

	public static ISynsetID getISynsetIDFromString(String synset_id_string) {
		//retrieving an ISynset from the vertex.
		int offset = Integer.parseInt(synset_id_string.split("-")[1]);
		char pos = synset_id_string.split("-")[2].charAt(0);
		SynsetID synset_id = new SynsetID(offset, POS.getPartOfSpeech(pos));
		return synset_id;
	}

	public static String getSynsetHTMLDescription(IDictionary dict, ISynset synset) {
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
