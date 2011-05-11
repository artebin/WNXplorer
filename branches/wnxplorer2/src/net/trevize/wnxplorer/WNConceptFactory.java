package net.trevize.wnxplorer;

import net.trevize.knetvis.KNetConcept;
import net.trevize.knetvis.KNetConceptFactory;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.ISynsetID;

public class WNConceptFactory extends KNetConceptFactory {

	@Override
	public KNetConcept getKNetConcept(String key) {
		ISynsetID synset_id = WNUtils.getISynsetIDFromString(key);
		ISynset synset = WNUtils.getWN_JWI_dictionary().getSynset(synset_id);
		WNConcept concept = new WNConcept(synset);
		return concept;
	}

}
