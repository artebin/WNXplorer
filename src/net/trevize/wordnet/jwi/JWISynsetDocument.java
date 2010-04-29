package net.trevize.wordnet.jwi;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.Pointer;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * JWISynsetDocument.java - Mar 25, 2010
 */

public class JWISynsetDocument {

	public static final String FIELD_SYNSET_TYPE_NOUN = "FIELD_SYNSET_TYPE_NOUN";
	public static final String FIELD_SYNSET_TYPE_VERB = "FIELD_SYNSET_TYPE_VERB";
	public static final String FIELD_SYNSET_TYPE_ADJECTIVE = "FIELD_SYNSET_TYPE_ADJECTIVE";
	public static final String FIELD_SYNSET_TYPE_ADVERB = "FIELD_SYNSET_TYPE_ADVERB";
	public static final String FIELD_SYNSET_TYPE_ADJECTIVE_SATELLITE = "FIELD_SYNSET_TYPE_ADJECTIVE_SATELLITE";

	public static final String FIELD_SYNSET_ID = "FIELD_SYNSET_ID";
	public static final String FIELD_SYNSET_TYPE = "FIELD_SYNSET_TYPE";
	public static final String FIELD_SYNSET_POS = "FIELD_SYNSET_POS";
	public static final String FIELD_SYNSET_WORD_LEMMA = "FIELD_SYNSET_WORD_LEMMA";

	public static final String FIELD_POINTER_ALSO_SEE = "FIELD_POINTER_ALSO_SEE";
	public static final String FIELD_POINTER_ANTONYM = "FIELD_POINTER_ANTONYM";
	public static final String FIELD_POINTER_ATTRIBUTE = "FIELD_POINTER_ATTRIBUTE";
	public static final String FIELD_POINTER_CAUSE = "FIELD_POINTER_CAUSE";
	public static final String FIELD_POINTER_DERIVATIONALLY_RELATED = "FIELD_POINTER_DERIVATIONALLY_RELATED";
	public static final String FIELD_POINTER_DERIVED_FROM_ADJ = "FIELD_POINTER_DERIVED_FROM_ADJ";
	public static final String FIELD_POINTER_ENTAILMENT = "FIELD_POINTER_ENTAILMENT";
	public static final String FIELD_POINTER_HOLONYM_MEMBER = "FIELD_POINTER_HOLONYM_MEMBER";
	public static final String FIELD_POINTER_HOLONYM_PART = "FIELD_POINTER_HOLONYM_PART";
	public static final String FIELD_POINTER_HOLONYM_SUBSTANCE = "FIELD_POINTER_HOLONYM_SUBSTANCE";
	public static final String FIELD_POINTER_HYPERNYM = "FIELD_POINTER_HYPERNYM";
	public static final String FIELD_POINTER_HYPERNYM_INSTANCE = "FIELD_POINTER_HYPERNYM_INSTANCE";
	public static final String FIELD_POINTER_HYPONYM = "FIELD_POINTER_HYPONYM";
	public static final String FIELD_POINTER_HYPONYM_INSTANCE = "FIELD_POINTER_HYPONYM_INSTANCE";
	public static final String FIELD_POINTER_MERONYM_MEMBER = "FIELD_POINTER_MERONYM_MEMBER";
	public static final String FIELD_POINTER_MERONYM_PART = "FIELD_POINTER_MERONYM_PART";
	public static final String FIELD_POINTER_MERONYM_SUBSTANCE = "FIELD_POINTER_MERONYM_SUBSTANCE";
	public static final String FIELD_POINTER_PARTICIPLE = "FIELD_POINTER_PARTICIPLE";
	public static final String FIELD_POINTER_PERTAINYM = "FIELD_POINTER_PERTAINYM";
	public static final String FIELD_POINTER_REGION = "FIELD_POINTER_REGION";
	public static final String FIELD_POINTER_REGION_MEMBER = "FIELD_POINTER_REGION_MEMBER";
	public static final String FIELD_POINTER_SIMILAR_TO = "FIELD_POINTER_SIMILAR_TO";
	public static final String FIELD_POINTER_TOPIC = "FIELD_POINTER_TOPIC";
	public static final String FIELD_POINTER_TOPIC_MEMBER = "FIELD_POINTER_TOPIC_MEMBER";
	public static final String FIELD_POINTER_USAGE = "FIELD_POINTER_USAGE";
	public static final String FIELD_POINTER_USAGE_MEMBER = "FIELD_POINTER_USAGE_MEMBER";
	public static final String FIELD_POINTER_VERB_GROUP = "FIELD_POINTER_VERB_GROUP";

	public static final String FIELD_SYNSET_GLOSS = "FIELD_SYNSET_GLOSS";

	public static final String FIELD_SYNSET_SENSE_NUMBER_FOR_WORD = "FIELD_SYNSET_SENSE_NUMBER_FOR_WORD";

	private static ArrayList<String> pointers;

	public static ArrayList<String> getPointers() {
		if (pointers == null) {
			pointers = new ArrayList<String>();
			pointers.add(FIELD_POINTER_ALSO_SEE);
			pointers.add(FIELD_POINTER_ANTONYM);
			pointers.add(FIELD_POINTER_ATTRIBUTE);
			pointers.add(FIELD_POINTER_CAUSE);
			pointers.add(FIELD_POINTER_DERIVATIONALLY_RELATED);
			pointers.add(FIELD_POINTER_DERIVED_FROM_ADJ);
			pointers.add(FIELD_POINTER_ENTAILMENT);
			pointers.add(FIELD_POINTER_HOLONYM_MEMBER);
			pointers.add(FIELD_POINTER_HOLONYM_PART);
			pointers.add(FIELD_POINTER_HOLONYM_SUBSTANCE);
			pointers.add(FIELD_POINTER_HYPERNYM);
			pointers.add(FIELD_POINTER_HYPERNYM_INSTANCE);
			pointers.add(FIELD_POINTER_HYPONYM);
			pointers.add(FIELD_POINTER_HYPONYM_INSTANCE);
			pointers.add(FIELD_POINTER_MERONYM_MEMBER);
			pointers.add(FIELD_POINTER_MERONYM_PART);
			pointers.add(FIELD_POINTER_MERONYM_SUBSTANCE);
			pointers.add(FIELD_POINTER_PARTICIPLE);
			pointers.add(FIELD_POINTER_PERTAINYM);
			pointers.add(FIELD_POINTER_REGION);
			pointers.add(FIELD_POINTER_REGION_MEMBER);
			pointers.add(FIELD_POINTER_SIMILAR_TO);
			pointers.add(FIELD_POINTER_TOPIC);
			pointers.add(FIELD_POINTER_TOPIC_MEMBER);
			pointers.add(FIELD_POINTER_USAGE);
			pointers.add(FIELD_POINTER_USAGE_MEMBER);
			pointers.add(FIELD_POINTER_VERB_GROUP);
		}

		return pointers;
	}

	public static Document createSynsetDocument(ISynset synset) {
		//create a new Lucene document.
		Document doc = new Document();

		//Field.Store.YES for set on the indexing
		//Field.Index.UN_TOKENIZED for not tokenizing the field

		//FIELD_SYNSET_ID.
		doc.add(new Field(FIELD_SYNSET_ID, synset.getID().toString(),
				Field.Store.YES, Field.Index.NOT_ANALYZED));

		//FIELD_SYNSET_TYPE.
		doc.add(new Field(FIELD_SYNSET_TYPE, "" + synset.getType(),
				Field.Store.YES, Field.Index.NOT_ANALYZED));

		//FIELD_SYNSET_POS.
		doc.add(new Field(FIELD_SYNSET_POS, synset.getPOS().name(),
				Field.Store.YES, Field.Index.NOT_ANALYZED));

		//FIELD_SYNSET_WORD_LEMMA
		List<IWord> word_list = synset.getWords();
		for (IWord word : word_list) {
			doc.add(new Field(FIELD_SYNSET_WORD_LEMMA, word.getLemma(),
					Field.Store.YES, Field.Index.NOT_ANALYZED));
		}

		//FIELD_RELATED_*
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.ALSO_SEE)) {
			doc.add(new Field(FIELD_POINTER_ALSO_SEE, related_synset_id
					.toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.ANTONYM)) {
			doc.add(new Field(FIELD_POINTER_ANTONYM, related_synset_id
					.toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.ATTRIBUTE)) {
			doc.add(new Field(FIELD_POINTER_ATTRIBUTE, related_synset_id
					.toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.CAUSE)) {
			doc.add(new Field(FIELD_POINTER_CAUSE,
					related_synset_id.toString(), Field.Store.YES,
					Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.DERIVATIONALLY_RELATED)) {
			doc.add(new Field(FIELD_POINTER_DERIVATIONALLY_RELATED,
					related_synset_id.toString(), Field.Store.YES,
					Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.DERIVED_FROM_ADJ)) {
			doc.add(new Field(FIELD_POINTER_DERIVED_FROM_ADJ, related_synset_id
					.toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.ENTAILMENT)) {
			doc.add(new Field(FIELD_POINTER_ENTAILMENT, related_synset_id
					.toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.HOLONYM_MEMBER)) {
			doc.add(new Field(FIELD_POINTER_HOLONYM_MEMBER, related_synset_id
					.toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.HOLONYM_PART)) {
			doc.add(new Field(FIELD_POINTER_HOLONYM_PART, related_synset_id
					.toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.HOLONYM_SUBSTANCE)) {
			doc.add(new Field(FIELD_POINTER_HOLONYM_SUBSTANCE,
					related_synset_id.toString(), Field.Store.YES,
					Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.HYPERNYM)) {
			doc.add(new Field(FIELD_POINTER_HYPERNYM, related_synset_id
					.toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.HYPERNYM_INSTANCE)) {
			doc.add(new Field(FIELD_POINTER_HYPERNYM_INSTANCE,
					related_synset_id.toString(), Field.Store.YES,
					Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.HYPONYM)) {
			doc.add(new Field(FIELD_POINTER_HYPONYM, related_synset_id
					.toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.HYPONYM_INSTANCE)) {
			doc.add(new Field(FIELD_POINTER_HYPONYM_INSTANCE, related_synset_id
					.toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.MERONYM_MEMBER)) {
			doc.add(new Field(FIELD_POINTER_MERONYM_MEMBER, related_synset_id
					.toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.MERONYM_PART)) {
			doc.add(new Field(FIELD_POINTER_MERONYM_PART, related_synset_id
					.toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.MERONYM_SUBSTANCE)) {
			doc.add(new Field(FIELD_POINTER_MERONYM_SUBSTANCE,
					related_synset_id.toString(), Field.Store.YES,
					Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.PARTICIPLE)) {
			doc.add(new Field(FIELD_POINTER_PARTICIPLE, related_synset_id
					.toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.PERTAINYM)) {
			doc.add(new Field(FIELD_POINTER_PERTAINYM, related_synset_id
					.toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.REGION)) {
			doc.add(new Field(FIELD_POINTER_REGION, related_synset_id
					.toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.REGION_MEMBER)) {
			doc.add(new Field(FIELD_POINTER_REGION_MEMBER, related_synset_id
					.toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.SIMILAR_TO)) {
			doc.add(new Field(FIELD_POINTER_SIMILAR_TO, related_synset_id
					.toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.TOPIC)) {
			doc.add(new Field(FIELD_POINTER_TOPIC,
					related_synset_id.toString(), Field.Store.YES,
					Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.TOPIC_MEMBER)) {
			doc.add(new Field(FIELD_POINTER_TOPIC_MEMBER, related_synset_id
					.toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.USAGE)) {
			doc.add(new Field(FIELD_POINTER_USAGE_MEMBER, related_synset_id
					.toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.VERB_GROUP)) {
			doc.add(new Field(FIELD_POINTER_VERB_GROUP, related_synset_id
					.toString(), Field.Store.YES, Field.Index.NOT_ANALYZED));
		}

		//FIELD_SYNSET_GLOSS.
		doc.add(new Field(FIELD_SYNSET_GLOSS, synset.getGloss(),
				Field.Store.YES, Field.Index.ANALYZED));

		//FIELD_SYNSET_SENSE_NUMBER_FOR_WORD.
		
		
		return doc;
	}

	public static String getDocumentWords(Document doc) {
		StringBuffer synset_words = new StringBuffer();
		Field[] fields = doc
				.getFields(JWISynsetDocument.FIELD_SYNSET_WORD_LEMMA);
		synset_words.append("{ ");
		synset_words.append(fields[0].stringValue());
		for (int i = 1; i < fields.length; ++i) {
			synset_words.append(" , " + fields[i].stringValue());
		}
		synset_words.append(" }");
		return synset_words.toString();
	}

	public static String getDocumentShortTextDescription(Document doc) {
		StringBuffer sb = new StringBuffer();

		sb.append(doc.getField(FIELD_SYNSET_ID).stringValue());
		sb.append("\n");

		sb.append(getDocumentWords(doc));
		sb.append("\n");
		sb.append(doc.getField(FIELD_SYNSET_GLOSS).stringValue());

		return sb.toString();
	}

	public static String getDocumentHTMLDescription(Document doc) {
		StringBuffer sb = new StringBuffer();

		sb.append("<h1>");
		sb.append(doc.getField(FIELD_SYNSET_ID).stringValue());
		sb.append("</h1>");

		sb.append(getDocumentWords(doc));
		sb.append("<br/>");
		sb.append(doc.getField(FIELD_SYNSET_GLOSS).stringValue());

		for (String p : getPointers()) {
			Field[] fields = doc.getFields(p);

			if (fields.length != 0) {
				sb.append("<h2>");
				sb.append(p.toString());
				sb.append("</h2>");
				sb.append("<ul>");
				for (Field field : fields) {
					sb.append("<li>");
					sb.append(field.toString());
					sb.append("</li>");
				}
				sb.append("</ul>");
			}
		}

		return sb.toString();
	}

}
