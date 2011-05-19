package net.trevize.wnxplorer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import net.trevize.wnxplorer.jwiknetvis.JWIUtils;

import edu.mit.jwi.item.IPointer;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.item.Pointer;
import edu.mit.jwi.item.SynsetID;

public class WordNetSandbox {

	public void hypernym_tree() {
		JWIUtils.setWN_dict_path(WNXplorerProperties.getWordnet_dict_path());

		ISynset synset_thing = JWIUtils.getWN_JWI_dictionary().getSynset(
				new SynsetID(1740, POS.NOUN));

		StringBuffer sb = new StringBuffer();
		hypernym_tree_rec(synset_thing, 0, sb);

		try {
			FileWriter fw = new FileWriter("./wn_hypernym_tree.txt");
			fw.write(sb.toString());
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void hypernym_tree_rec(ISynset synset, int level, StringBuffer sb) {
		Map<IPointer, List<ISynsetID>> related_synsets = synset.getRelatedMap();

		if (related_synsets != null
				&& related_synsets.get(Pointer.HYPONYM) != null) {

			for (ISynsetID synset_id : related_synsets.get(Pointer.HYPONYM)) {
				ISynset synset_child = JWIUtils.getWN_JWI_dictionary()
						.getSynset(synset_id);

				for (int i = 0; i < level; ++i) {
					sb.append(".");
				}

				sb.append(JWIUtils.getWords(synset_child));
				sb.append("\n");
				hypernym_tree_rec(synset_child, level + 1, sb);
			}

		}
	}

	public static void main(String[] args) {
		WordNetSandbox wn_sandbox = new WordNetSandbox();
		wn_sandbox.hypernym_tree();
	}

}
