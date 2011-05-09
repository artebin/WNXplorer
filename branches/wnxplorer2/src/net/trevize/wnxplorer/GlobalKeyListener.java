package net.trevize.wnxplorer;

import java.awt.AWTEvent;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Calendar;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * GlobalKeyListener.java - Feb 3, 2010
 */

public class GlobalKeyListener implements AWTEventListener {

	private Explorer explorer;
	private boolean fullscreen;

	public GlobalKeyListener(Explorer explorer) {
		this.explorer = explorer;
		fullscreen = false;
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
		}

		else

		if ((e.getModifiers() & KeyEvent.ALT_MASK) != 0
				&& e.getKeyCode() == KeyEvent.VK_I) {
		}

		else

		if ((e.getModifiers() & KeyEvent.ALT_MASK) != 0
				&& e.getKeyCode() == KeyEvent.VK_G) {
		}

		else

		if ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0
				&& e.getKeyCode() == KeyEvent.VK_C) {
			explorer.clearGraphView();
		}

		else

		if ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0
				&& e.getKeyCode() == KeyEvent.VK_S) {
			explorer.writeJPEGImage(new File("./"
					+ Calendar.getInstance().getTimeInMillis() + ".jpg"));
		}

		else

		if (e.getKeyCode() == KeyEvent.VK_F11) {
			//determine if full-screen mode is supported directly. 
			GraphicsEnvironment ge = GraphicsEnvironment
					.getLocalGraphicsEnvironment();
			GraphicsDevice gs = ge.getDefaultScreenDevice();
			if (gs.isFullScreenSupported()) {
				if (!fullscreen) {
					gs.setFullScreenWindow(explorer.getMain_frame());
					fullscreen = true;
				} else {
					gs.setFullScreenWindow(null);
					fullscreen = false;
				}
			}
		}
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

}
