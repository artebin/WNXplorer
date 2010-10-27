package net.trevize.wnxplorer.explorer;

import java.awt.AWTEvent;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * GlobalKeyListener.java - Feb 3, 2010
 */

public class GlobalKeyListener implements AWTEventListener {

	private Explorer explorer;

	public GlobalKeyListener(Explorer explorer) {
		this.explorer = explorer;
	}

	public void eventDispatched(AWTEvent e) {
		switch (e.getID()) {
		case KeyEvent.KEY_PRESSED:
			keyPressed((KeyEvent) e);
			break;
		case KeyEvent.KEY_RELEASED:
			keyReleased((KeyEvent) e);
			break;
		case KeyEvent.KEY_TYPED:
			keyTyped((KeyEvent) e);
			break;
		}
	}

	public void keyPressed(KeyEvent e) {
		if (e.isAltDown() && e.getKeyChar() == '&') {
			//System.out.println("tab0");
			explorer.getJtp().setSelectedIndex(0);
		}

		else

		if (e.isAltDown() && e.getKeyChar() == 'é') {
			//System.out.println("tab1");
			explorer.getJtp().setSelectedIndex(1);
		}

		else

		if (e.isAltDown() && e.getKeyChar() == '"') {
			//System.out.println("tab2");
			explorer.getJtp().setSelectedIndex(2);
		}

		else

		if (e.isAltDown() && e.getKeyChar() == 'c') {
			//System.out.println("clear");
			explorer.clearView();
		}
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

}
