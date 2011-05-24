package net.trevize.wnxplorer.lucene;

import java.util.List;

import net.trevize.wnxplorer.lucene.knetvis.LWNResource;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.Pointer;

public class LWNDocumentFactory {

	public static Document createDocument(ISynset synset) {
		//create a new Lucene document
		Document doc = new Document();

		//Field.Store.YES for set on the indexing
		//Field.Index.NOT_ANALYZED for not tokenizing the field

		doc.add(new Field(LWNUtils.SYNSET_DATA_ID, synset.getID().toString(),
				Field.Store.YES, Field.Index.NOT_ANALYZED));

		doc.add(new Field(LWNUtils.SYNSET_DATA_POS, synset.getPOS().name(),
				Field.Store.YES, Field.Index.NOT_ANALYZED));

		List<IWord> word_list = synset.getWords();
		for (IWord word : word_list) {
			doc.add(new Field(LWNUtils.SYNSET_DATA_WORD_LEMMA, word.getLemma(),
					Field.Store.YES, Field.Index.NOT_ANALYZED));
		}

		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.ALSO_SEE)) {
			doc.add(new Field(LWNResource.SEMANTIC_RELATION_KEY_ALSO_SEE,
					related_synset_id.toString(), Field.Store.YES,
					Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.ANTONYM)) {
			doc.add(new Field(LWNResource.SEMANTIC_RELATION_KEY_ANTONYM,
					related_synset_id.toString(), Field.Store.YES,
					Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.ATTRIBUTE)) {
			doc.add(new Field(LWNResource.SEMANTIC_RELATION_KEY_ATTRIBUTE,
					related_synset_id.toString(), Field.Store.YES,
					Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.CAUSE)) {
			doc.add(new Field(LWNResource.SEMANTIC_RELATION_KEY_CAUSE,
					related_synset_id.toString(), Field.Store.YES,
					Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.DERIVATIONALLY_RELATED)) {
			doc.add(new Field(
					LWNResource.SEMANTIC_RELATION_KEY_DERIVATIONALLY_RELATED,
					related_synset_id.toString(), Field.Store.YES,
					Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.DERIVED_FROM_ADJ)) {
			doc.add(new Field(
					LWNResource.SEMANTIC_RELATION_KEY_DERIVED_FROM_ADJ,
					related_synset_id.toString(), Field.Store.YES,
					Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.ENTAILMENT)) {
			doc.add(new Field(LWNResource.SEMANTIC_RELATION_KEY_ENTAILMENT,
					related_synset_id.toString(), Field.Store.YES,
					Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.HOLONYM_MEMBER)) {
			doc.add(new Field(LWNResource.SEMANTIC_RELATION_KEY_HOLONYM_MEMBER,
					related_synset_id.toString(), Field.Store.YES,
					Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.HOLONYM_PART)) {
			doc.add(new Field(LWNResource.SEMANTIC_RELATION_KEY_HOLONYM_PART,
					related_synset_id.toString(), Field.Store.YES,
					Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.HOLONYM_SUBSTANCE)) {
			doc.add(new Field(
					LWNResource.SEMANTIC_RELATION_KEY_HOLONYM_SUBSTANCE,
					related_synset_id.toString(), Field.Store.YES,
					Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.HYPERNYM)) {
			doc.add(new Field(LWNResource.SEMANTIC_RELATION_KEY_HYPERNYM,
					related_synset_id.toString(), Field.Store.YES,
					Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.HYPERNYM_INSTANCE)) {
			doc.add(new Field(
					LWNResource.SEMANTIC_RELATION_KEY_HYPERNYM_INSTANCE,
					related_synset_id.toString(), Field.Store.YES,
					Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.HYPONYM)) {
			doc.add(new Field(LWNResource.SEMANTIC_RELATION_KEY_HYPONYM,
					related_synset_id.toString(), Field.Store.YES,
					Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.HYPONYM_INSTANCE)) {
			doc.add(new Field(
					LWNResource.SEMANTIC_RELATION_KEY_HYPONYM_INSTANCE,
					related_synset_id.toString(), Field.Store.YES,
					Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.MERONYM_MEMBER)) {
			doc.add(new Field(LWNResource.SEMANTIC_RELATION_KEY_MERONYM_MEMBER,
					related_synset_id.toString(), Field.Store.YES,
					Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.MERONYM_PART)) {
			doc.add(new Field(LWNResource.SEMANTIC_RELATION_KEY_MERONYM_PART,
					related_synset_id.toString(), Field.Store.YES,
					Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.MERONYM_SUBSTANCE)) {
			doc.add(new Field(
					LWNResource.SEMANTIC_RELATION_KEY_MERONYM_SUBSTANCE,
					related_synset_id.toString(), Field.Store.YES,
					Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.PARTICIPLE)) {
			doc.add(new Field(LWNResource.SEMANTIC_RELATION_KEY_PARTICIPLE,
					related_synset_id.toString(), Field.Store.YES,
					Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.PERTAINYM)) {
			doc.add(new Field(LWNResource.SEMANTIC_RELATION_KEY_PERTAINYM,
					related_synset_id.toString(), Field.Store.YES,
					Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.REGION)) {
			doc.add(new Field(LWNResource.SEMANTIC_RELATION_KEY_REGION,
					related_synset_id.toString(), Field.Store.YES,
					Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.REGION_MEMBER)) {
			doc.add(new Field(LWNResource.SEMANTIC_RELATION_KEY_REGION_MEMBER,
					related_synset_id.toString(), Field.Store.YES,
					Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.SIMILAR_TO)) {
			doc.add(new Field(LWNResource.SEMANTIC_RELATION_KEY_SIMILAR_TO,
					related_synset_id.toString(), Field.Store.YES,
					Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.TOPIC)) {
			doc.add(new Field(LWNResource.SEMANTIC_RELATION_KEY_TOPIC,
					related_synset_id.toString(), Field.Store.YES,
					Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.TOPIC_MEMBER)) {
			doc.add(new Field(LWNResource.SEMANTIC_RELATION_KEY_TOPIC_MEMBER,
					related_synset_id.toString(), Field.Store.YES,
					Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.USAGE)) {
			doc.add(new Field(LWNResource.SEMANTIC_RELATION_KEY_USAGE,
					related_synset_id.toString(), Field.Store.YES,
					Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.USAGE_MEMBER)) {
			doc.add(new Field(LWNResource.SEMANTIC_RELATION_KEY_USAGE_MEMBER,
					related_synset_id.toString(), Field.Store.YES,
					Field.Index.NOT_ANALYZED));
		}
		for (ISynsetID related_synset_id : synset
				.getRelatedSynsets(Pointer.VERB_GROUP)) {
			doc.add(new Field(LWNResource.SEMANTIC_RELATION_KEY_VERB_GROUP,
					related_synset_id.toString(), Field.Store.YES,
					Field.Index.NOT_ANALYZED));
		}

		doc.add(new Field(LWNUtils.SYNSET_DATA_GLOSS, synset.getGloss(),
				Field.Store.YES, Field.Index.ANALYZED));

		return doc;
	}

}
