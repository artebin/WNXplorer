package net.trevize.wnxplorer.jung;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * SynsetVertex.java - Mar 25, 2010
 */

public class SynsetVertex {

	private String synset_id;
	private String pos;
	private String short_label;
	private String synset_words;

	public SynsetVertex(String synset_id, String pos, String synset_words,
			String short_label) {
		this.synset_id = synset_id;
		this.pos = pos;
		this.synset_words = synset_words;
		this.short_label = short_label;
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

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

}
