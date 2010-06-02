package net.trevize.wordnet.jwi;

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
 * JWIWNSearcher.java - Mar 25, 2010
 */

public class JWIWNSearcher {

	private IDictionary dict;
	private WordnetStemmer stemmer;
	private ArrayList<ISynset> results;

	public JWIWNSearcher(IDictionary dict) {
		this.dict = dict;
		stemmer = new WordnetStemmer(dict);
	}

	public void search(POS pos, String queryString) {
		//calling the stemmer.
		List<String> stems = stemmer.findStems(queryString);

		ArrayList<IWordID> all_word_id = new ArrayList<IWordID>();
		for (String stem : stems) {
			IIndexWord index_word = dict.getIndexWord(stem, POS.NOUN);
			if (index_word != null) {
				all_word_id.addAll(index_word.getWordIDs());
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
