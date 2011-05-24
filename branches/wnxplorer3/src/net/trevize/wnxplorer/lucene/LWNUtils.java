package net.trevize.wnxplorer.lucene;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

public class LWNUtils {

	/*
	 * synset data
	 */
	public static final String SYNSET_DATA_ID = "SYNSET_DATA_ID";
	public static final String SYNSET_DATA_POS = "SYNSET_DATA_POS";
	public static final String SYNSET_DATA_WORD_LEMMA = "SYNSET_DATA_WORD_LEMMA";
	public static final String SYNSET_DATA_GLOSS = "SYNSET_DATA_GLOSS";
	public static final String SYNSET_DATA_SENSE_NUMBER_FOR_WORD = "SYNSET_DATA_SENSE_NUMBER_FOR_WORD";

	private static String lucene_index_path;

	public static String getLucene_index_path() {
		return lucene_index_path;
	}

	public static void setLucene_index_path(String lucene_index_path) {
		LWNUtils.lucene_index_path = lucene_index_path;
	}

	private static Searcher searcher;

	public static Searcher getSearcher() {
		if (searcher == null) {
			initSearcher();
		}
		return searcher;
	}

	private static void initSearcher() {
		if (lucene_index_path == null) {
			return;
		}
		try {
			searcher = new IndexSearcher(new SimpleFSDirectory(new File(
					lucene_index_path)));
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getWords(Document document) {
		StringBuffer sb = new StringBuffer();
		Field[] fields = document.getFields(SYNSET_DATA_WORD_LEMMA);
		for (int i = 0; i < fields.length; ++i) {
			sb.append(fields[i].stringValue());
			if (i == fields.length - 1) {
				sb.append(",");
			}
		}
		return sb.toString();
	}

	public static Document getDocument(String key) {
		QueryParser query_parser = new QueryParser(Version.LUCENE_30,
				SYNSET_DATA_ID, new KeywordAnalyzer());

		TopDocs topdocs = null;
		try {
			Query query = query_parser.parse(key);
			topdocs = searcher.search(query, 1);
		} catch (ParseException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		if (topdocs != null && topdocs.totalHits > 1) {
			Document document = null;
			try {
				document = searcher.doc(topdocs.scoreDocs[0].doc);
			} catch (CorruptIndexException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return document;
		} else {
			return null;
		}
	}

}
