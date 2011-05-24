package net.trevize.wnxplorer.lucene;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.ListCellRenderer;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;

import net.trevize.wnxplorer.lucene.knetvis.LWNResource;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * PopupPOSButton.java - Oct 27, 2010
 */

public class PopupPOSButton extends JToggleButton {

	private AbstractAction show_popup_action = new AbstractAction("POS") {
		public void actionPerformed(ActionEvent e) {
			JToggleButton b = (JToggleButton) e.getSource();
			if (!b.isSelected() && popup != null) {
				popup.hide();
				popup = null;
			} else {
				if (!(pos_jlist.getPreferredSize().height > scrollpane
						.getViewport().getPreferredSize().height)) {
					scrollpane.getViewport().setPreferredSize(
							pos_jlist.getPreferredSize());
					pos_list_panel.setPreferredSize(scrollpane
							.getPreferredSize());
				}

				Dimension panelSize = pos_list_panel.getPreferredSize();

				//right-align it with the POS button, so it pops up just above it.
				Point location = new Point(0 - panelSize.width
						+ getPreferredSize().width, getPreferredSize().height);
				SwingUtilities.convertPointToScreen(location,
						PopupPOSButton.this);

				popup = PopupFactory.getSharedInstance().getPopup(
						PopupPOSButton.this, pos_list_panel, location.x,
						location.y);

				popup.show();
			}
		}
	};

	private class JCheckboxCellRenderer implements ListCellRenderer {
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			JCheckBox checkbox = (JCheckBox) value;

			checkbox.setEnabled(isEnabled());
			checkbox.setFont(getFont());
			checkbox.setFocusPainted(false);
			checkbox.setBorderPainted(true);

			return checkbox;
		}
	}

	private Popup popup;
	private JScrollPane scrollpane;
	private HashMap<String, JCheckBox> pos_checkboxes;
	private JPanel pos_list_panel;
	private JList pos_jlist;

	public PopupPOSButton() {
		setMargin(new Insets(0, 0, 0, 0));

		/*
		 * Construct the popup for the list of the semantic relations.
		 */
		pos_list_panel = new JPanel();
		pos_list_panel
				.setLayout(new BoxLayout(pos_list_panel, BoxLayout.Y_AXIS));

		pos_jlist = new JList();

		pos_jlist.setCellRenderer(new JCheckboxCellRenderer());

		DefaultListModel model = new DefaultListModel();
		pos_checkboxes = new HashMap<String, JCheckBox>();
		for (String pos : LWNResource.getPOS()) {
			JCheckBox pos_checkbox = new JCheckBox(pos);
			pos_checkbox.setSelected(true); //by default the search is performed in all POS.
			model.addElement(pos_checkbox);
			pos_checkboxes.put(pos, pos_checkbox);
		}

		pos_jlist.setModel(model);

		//add a mouse listener to handle changing selection.
		pos_jlist.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
				JList list = (JList) event.getSource();

				//get index of item clicked.
				int index = list.locationToIndex(event.getPoint());
				JCheckBox item = (JCheckBox) list.getModel()
						.getElementAt(index);

				//toggle selected state.
				item.setSelected(!item.isSelected());

				//repaint cell.
				list.repaint(list.getCellBounds(index, index));
			}
		});

		scrollpane = new JScrollPane();
		scrollpane.setViewportView(pos_jlist);
		pos_list_panel.add(scrollpane);

		setAction(show_popup_action);
	}

	public ArrayList<String> getSelectedPOS() {
		ArrayList<String> pos_list = new ArrayList<String>();
		for (String pos : pos_checkboxes.keySet()) {
			if (pos_checkboxes.get(pos).isSelected()) {
				pos_list.add(pos);
			}
		}
		return pos_list;
	}

	public void hidePopup() {
		if (popup != null) {
			popup.hide();
			setSelected(false);
		}
	}

}
