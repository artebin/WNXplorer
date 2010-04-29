package net.trevize.wordnet.jwi;

import org.apache.lucene.document.Document;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * JWIWNResult.java - Mar 31, 2010
 */

public class JWIWNResult {

	private Document doc;
	private float score;

	public JWIWNResult(Document doc, float score) {
		this.doc = doc;
		this.score = score;
	}

	public Document getDoc() {
		return doc;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

}
