package net.trevize.wnxplorer.lucene;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.SimpleFSDirectory;

import com.davidsoergel.conja.Function;
import com.davidsoergel.conja.Parallel;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.POS;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * LuceneWNIndexer.java - Mar 25, 2010
 */

public class LWNIndexer {

	public static void main(String[] args) {
		LWNIndexer indexer = new LWNIndexer(LWNIndexer.WHITESPACE_ANALYZER);
		String wn_dict_path = "/home/nicolas/WordNet/WordNet-3.0/WordNet-3.0/dict";
		String lucene_index_path = "./LuceneIndex";
		indexer.indexWordNetDatabase(wn_dict_path, lucene_index_path);
	}

	public static final String KEYWORD_ANALYZER = "KEYWORD_ANALYZER";
	public static final String WHITESPACE_ANALYZER = "WHITESPACE_ANALYZER";

	private IndexWriter index_writer;
	private Analyzer analyzer;

	private IDictionary dict;

	public LWNIndexer(String analyzer_name) {
		if (analyzer_name.equals(KEYWORD_ANALYZER)) {
			analyzer = new KeywordAnalyzer();
		}

		else

		if (analyzer_name.equals(WHITESPACE_ANALYZER)) {
			analyzer = new WhitespaceAnalyzer();
		}
	}

	public void indexWordNetDatabase(String wn_dict_path,
			String lucene_index_path) {
		//open a JWI dictionary
		try {
			dict = new Dictionary(new URL("file", null, wn_dict_path));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		dict.open();

		//create the directory for storing the index if that directory not exists
		//if it exists the index will be extended with new documents (take care for copy)
		boolean create;
		File f = new File(lucene_index_path);
		if (f.exists() && f.isDirectory()) {
			create = false;
		} else {
			create = true;
		}

		//initializing the index writer
		try {
			Directory fsdir = new SimpleFSDirectory(
					new File(lucene_index_path), null);
			index_writer = new IndexWriter(fsdir, analyzer, create,
					IndexWriter.MaxFieldLength.UNLIMITED);
		} catch (CorruptIndexException e1) {
			e1.printStackTrace();
		} catch (LockObtainFailedException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		_processWordNetDatabaseParallelized();

		try {
			index_writer.close();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void _processWordNetDatabase() {
		Iterator<ISynset> noun_synset_iter = dict.getSynsetIterator(POS.NOUN);
		try {
			while (noun_synset_iter.hasNext()) {
				ISynset synset = noun_synset_iter.next();
				Document synset_document = LWNDocumentFactory
						.createDocument(synset);
				index_writer.addDocument(synset_document);
				index_writer.optimize();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		Iterator<ISynset> verb_synset_iter = dict.getSynsetIterator(POS.VERB);
		try {
			while (verb_synset_iter.hasNext()) {
				ISynset synset = verb_synset_iter.next();
				Document synset_document = LWNDocumentFactory
						.createDocument(synset);
				index_writer.addDocument(synset_document);
				index_writer.optimize();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		Iterator<ISynset> adjective_synset_iter = dict
				.getSynsetIterator(POS.ADJECTIVE);
		try {
			while (adjective_synset_iter.hasNext()) {
				ISynset synset = adjective_synset_iter.next();
				Document synset_document = LWNDocumentFactory
						.createDocument(synset);
				index_writer.addDocument(synset_document);
				index_writer.optimize();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		Iterator<ISynset> adverb_synset_iter = dict
				.getSynsetIterator(POS.ADVERB);
		try {
			while (adverb_synset_iter.hasNext()) {
				ISynset synset = adverb_synset_iter.next();
				Document synset_document = LWNDocumentFactory
						.createDocument(synset);
				index_writer.addDocument(synset_document);
				index_writer.optimize();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static int counter = 0;

	private void _processWordNetDatabaseParallelized() {
		/*
		 * defining the function for the Parallel.forEach(...).
		 */
		Function<ISynset, Void> index_synset_function = new Function<ISynset, Void>() {
			public Void apply(ISynset synset) {
				System.out.println(++counter + " " + synset.getID().toString());
				Document synset_document = LWNDocumentFactory
						.createDocument(synset);
				try {
					index_writer.addDocument(synset_document);
				} catch (CorruptIndexException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}
		};

		/*
		 * launching the Parallel.forEach(...).
		 */
		System.out.println("indexing POS.NOUN ...");
		Iterator<ISynset> noun_synset_iter = dict.getSynsetIterator(POS.NOUN);
		Parallel.forEach(noun_synset_iter, index_synset_function);

		System.out.println("indexing POS.VERB ...");
		Iterator<ISynset> verb_synset_iter = dict.getSynsetIterator(POS.VERB);
		Parallel.forEach(verb_synset_iter, index_synset_function);

		System.out.println("indexing POS.ADJECTIVE ...");
		Iterator<ISynset> adjective_synset_iter = dict
				.getSynsetIterator(POS.ADJECTIVE);
		Parallel.forEach(adjective_synset_iter, index_synset_function);

		System.out.println("indexing POS.ADVERB ...");
		Iterator<ISynset> adverb_synset_iter = dict
				.getSynsetIterator(POS.ADVERB);
		Parallel.forEach(adverb_synset_iter, index_synset_function);

		try {
			index_writer.optimize();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Parallel.shutdown();
	}

}
