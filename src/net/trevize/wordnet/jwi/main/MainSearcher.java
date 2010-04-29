package net.trevize.wordnet.jwi.main;

import java.awt.BorderLayout;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.trevize.wordnet.explorer.Configuration;
import net.trevize.wordnet.jwi.JWIWNSearcher;
import net.trevize.wordnet.jwi.resultspanel.JWIWNResultsPanel;
import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.POS;

public class MainSearcher {

	public static void main(String[] args) {
		// setting the system look and feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		//for JWI.
		IDictionary dict;

		String wordnet_path = Configuration.getWN_PATH();
		URL url = null;
		try {
			url = new URL("file", null, wordnet_path);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		dict = new Dictionary(url);
		dict.open();

		JWIWNSearcher searcher = new JWIWNSearcher(dict);
		JFrame f = new JFrame("JWIWNResultsPanel");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(1024, 512);
		f.setLocationRelativeTo(null);

		searcher.search(POS.NOUN, "drive");

		JWIWNResultsPanel p = new JWIWNResultsPanel(dict, searcher.getResults());
		p.retrieveResults(0, 42);

		f.getContentPane().setLayout(new BorderLayout());
		f.getContentPane().add(p.getView(), BorderLayout.CENTER);

		f.setVisible(true);
	}
}
