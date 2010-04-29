package net.trevize.wordnet;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.POS;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * ListingSynsets.java - Mar 25, 2010
 */

public class ListingSynsets {

	public static void main(String[] args) {
		String wordnet_path = "/home/nicolas/WordNet/WordNet-3.0/dict";
		URL url = null;
		try {
			url = new URL("file", null, wordnet_path);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		IDictionary dict = new Dictionary(url);
		dict.open();

		//		Iterator<IIndexWord> indexword_iter = dict
		//				.getIndexWordIterator(POS.NOUN);
		//		try {
		//			FileWriter fw = new FileWriter("./indexwords.txt");
		//			BufferedWriter bw = new BufferedWriter(fw);
		//			while (indexword_iter.hasNext()) {
		//				IIndexWord indexword = indexword_iter.next();
		//				//System.out.println(indexword.getLemma());
		//				bw.write(indexword.getLemma() + "\n");
		//			}
		//			bw.close();
		//			fw.close();
		//		} catch (IOException e) {
		//			e.printStackTrace();
		//		}

		Iterator<ISynset> synset_iter = dict.getSynsetIterator(POS.NOUN);
		try {
			FileWriter fw = new FileWriter("./synsets.txt");
			BufferedWriter bw = new BufferedWriter(fw);
			while (synset_iter.hasNext()) {
				ISynset synset = synset_iter.next();
				StringBuffer sb = new StringBuffer();
				sb.append(synset.getID() + ",");
				List<IWord> words = synset.getWords();
				for (int i = 0; i < words.size(); ++i) {
					sb.append(words.get(i).getLemma());
					if (i != words.size() - 1) {
						sb.append(" ");
					}
				}
				sb.append("," + synset.getGloss());
				bw.write(sb.toString() + "\n");
			}
			bw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
