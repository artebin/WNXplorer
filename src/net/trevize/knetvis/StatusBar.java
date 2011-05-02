package net.trevize.knetvis;

import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * StatusBar.java - Feb 15, 2010
 */

public class StatusBar extends JPanel {

	public HashMap<String, JComponent> getFields() {
		return fields;
	}

	public void setFields(HashMap<String, JComponent> fields) {
		this.fields = fields;
	}

	private HashMap<String, JComponent> fields;

	public StatusBar() {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		fields = new HashMap<String, JComponent>();
		//add(new JLabel(" "));
	}

	public void addComponent(String key, JComponent c) {
		fields.put(key, c);
		add(c);
		add(new JLabel(" | "));
	}

	public JComponent getComponent(String key) {
		return fields.get(key);
	}

}
