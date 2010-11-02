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
		if ((e.getModifiers() & KeyEvent.ALT_MASK) != 0
				&& e.getKeyCode() == KeyEvent.VK_S) {
			//System.out.println("tab0");
			explorer.getTabbedpane().setSelectedIndex(0);
		}

		else

		if ((e.getModifiers() & KeyEvent.ALT_MASK) != 0
				&& e.getKeyCode() == KeyEvent.VK_I) {
			//System.out.println("tab1");
			explorer.getTabbedpane().setSelectedIndex(1);
		}

		else

		if ((e.getModifiers() & KeyEvent.ALT_MASK) != 0
				&& e.getKeyCode() == KeyEvent.VK_G) {
			//System.out.println("tab2");
			explorer.getTabbedpane().setSelectedIndex(2);
		}

		else

		if ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0
				&& e.getKeyCode() == KeyEvent.VK_C) {
			//System.out.println("clear");
			explorer.clearGraphView();
		}
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

}
