package net.trevize.wordnet.explorer.main;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.trevize.wordnet.explorer.Explorer;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * Main.java - Mar 24, 2010
 */

public class Main {

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

		Explorer explorer = new Explorer();
	}

}
