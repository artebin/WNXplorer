package net.trevize.wnxplorer.explorer;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.ListCellRenderer;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;

import net.trevize.wnxplorer.jwi.WNUtils;
import edu.mit.jwi.item.Pointer;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * PopupPointerButton.java - Oct 19, 2010
 */

public class PopupPointerButton extends JToggleButton {

	private AbstractAction show_popup_action = new AbstractAction("",
			new ImageIcon("./gfx/semantic_web_32.png")) {
		public void actionPerformed(ActionEvent e) {
			JToggleButton b = (JToggleButton) e.getSource();
			if (!b.isSelected() && popup != null) {
				popup.hide();
				popup = null;
			} else {
				if (!(pointer_list.getPreferredSize().height > scrollpane
						.getViewport().getPreferredSize().height)) {
					scrollpane.getViewport().setPreferredSize(
							pointer_list.getPreferredSize());
					pointer_list_panel.setPreferredSize(scrollpane
							.getPreferredSize());
				}

				Dimension panelSize = pointer_list_panel.getPreferredSize();

				//right-align it with the volume button, so it pops up just above it.
				Point location = new Point(0 - panelSize.width
						+ getPreferredSize().width, 0 - panelSize.height);
				SwingUtilities.convertPointToScreen(location,
						PopupPointerButton.this);

				popup = PopupFactory.getSharedInstance().getPopup(
						PopupPointerButton.this, pointer_list_panel,
						location.x, location.y);

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
	private HashMap<Pointer, JCheckBox> pointer_checkboxes;
	private JPanel pointer_list_panel;
	private JList pointer_list;

	public PopupPointerButton() {
		setMargin(new Insets(0, 0, 0, 0));

		/*
		 * Construct the popup for the pointers list.
		 */
		pointer_list_panel = new JPanel();
		pointer_list_panel.setLayout(new BoxLayout(pointer_list_panel,
				BoxLayout.Y_AXIS));

		pointer_list = new JList();

		pointer_list.setCellRenderer(new JCheckboxCellRenderer());

		DefaultListModel model = new DefaultListModel();
		pointer_checkboxes = new HashMap<Pointer, JCheckBox>();
		for (Pointer pointer : WNUtils.getPointers()) {
			String pointer_name = pointer.getName();
			JCheckBox pointer_checkbox = new JCheckBox(pointer_name);
			pointer_checkbox.setOpaque(true);
			pointer_checkbox.setBackground(WNUtils.getPointersColor().get(
					pointer));
			pointer_checkbox.setSelected(true); //by default all pointers are displayed whatever the types.
			model.addElement(pointer_checkbox);
			pointer_checkboxes.put(pointer, pointer_checkbox);
		}

		pointer_list.setModel(model);

		// Add a mouse listener to handle changing selection

		pointer_list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
				JList list = (JList) event.getSource();

				// Get index of item clicked
				int index = list.locationToIndex(event.getPoint());
				JCheckBox item = (JCheckBox) list.getModel()
						.getElementAt(index);

				// Toggle selected state
				item.setSelected(!item.isSelected());

				// Repaint cell
				list.repaint(list.getCellBounds(index, index));
			}
		});

		scrollpane = new JScrollPane();
		scrollpane.setViewportView(pointer_list);
		pointer_list_panel.add(scrollpane);

		setAction(show_popup_action);
	}

}