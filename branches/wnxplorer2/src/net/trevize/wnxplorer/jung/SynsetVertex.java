package net.trevize.wnxplorer.jung;

import net.trevize.wnxplorer.jwi.WNUtils;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.POS;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * SynsetVertex.java - Mar 25, 2010
 */

public class SynsetVertex {

	private ISynset isynset;
	private String synset_id;
	private POS pos;
	private String short_label;
	private String synset_words;

	public SynsetVertex(ISynset isynset) {
		this.isynset = isynset;
		synset_id = isynset.getID().toString();
		pos = isynset.getPOS();
		synset_words = WNUtils.getWords(isynset);
		short_label = isynset.getWords().get(0).getLemma();
	}

	public String getSynset_id() {
		return synset_id;
	}

	public void setSynset_id(String synsetId) {
		synset_id = synsetId;
	}

	public String getShort_label() {
		return short_label;
	}

	public void setShort_label(String shortLabel) {
		short_label = shortLabel;
	}

	public String getSynset_words() {
		return synset_words;
	}

	public void setSynset_words(String synsetWords) {
		synset_words = synsetWords;
	}

	public POS getPOS() {
		return pos;
	}

	public void setPOS(POS pos) {
		this.pos = pos;
	}

	public ISynset getIsynset() {
		return isynset;
	}

	public void setIsynset(ISynset isynset) {
		this.isynset = isynset;
	}

}
