package net.trevize.wnxplorer;

import java.util.ArrayList;
import java.util.List;

import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.IIndexWord;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.IWordID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.morph.WordnetStemmer;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * Searcher.java - Mar 25, 2010
 */

public class Searcher {

	private IDictionary dict;
	private WordnetStemmer stemmer;
	private ArrayList<ISynset> results;

	public Searcher(IDictionary dict) {
		this.dict = dict;
		stemmer = new WordnetStemmer(dict);
	}

	public void search(List<POS> pos_list, String queryString) {
		//calling the stemmer.
		List<String> stems = stemmer.findStems(queryString, null);

		ArrayList<IWordID> all_word_id = new ArrayList<IWordID>();

		for (POS pos : pos_list) {
			for (String stem : stems) {
				IIndexWord index_word = dict.getIndexWord(stem, pos);
				if (index_word != null) {
					all_word_id.addAll(index_word.getWordIDs());
				}
			}
		}

		results = new ArrayList<ISynset>();
		for (IWordID word_id : all_word_id) {
			results.add(dict.getSynset(word_id.getSynsetID()));
		}

		//display number of results.
		System.out.println(results.size() + " results.");

	}

	/***************************************************************************
	 * getters and setters.
	 **************************************************************************/

	public IDictionary getDict() {
		return dict;
	}

	public void setDict(IDictionary dict) {
		this.dict = dict;
	}

	public WordnetStemmer getStemmer() {
		return stemmer;
	}

	public void setStemmer(WordnetStemmer stemmer) {
		this.stemmer = stemmer;
	}

	public ArrayList<ISynset> getResults() {
		return results;
	}

	public void setResults(ArrayList<ISynset> results) {
		this.results = results;
	}

}
