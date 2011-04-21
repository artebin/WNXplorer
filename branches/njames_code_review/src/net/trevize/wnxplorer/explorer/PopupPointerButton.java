package net.trevize.wnxplorer.explorer;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.ListCellRenderer;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.trevize.wnxplorer.jwi.WNUtils;
import edu.mit.jwi.item.Pointer;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * PopupPointerButton.java - Oct 19, 2010
 */

public class PopupPointerButton extends JToggleButton implements
		ActionListener, ChangeListener {

	private static final String ACTION_COMMAND_SELECT_ALL_POINTERS_TYPES = "ACTION_COMMAND_SELECT_ALL_POINTERS_TYPES";
	private static final String ACTION_COMMAND_SELECT_NONE_POINTERS_TYPES = "ACTION_COMMAND_SELECT_NONE_POINTERS_TYPES";

	private AbstractAction show_popup_action = new AbstractAction("",
			new ImageIcon(WNXplorerProperties.getIcon_path_pointer_selector())) {
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

	private WNGraph wngraph;
	private WNGraphPanel wngraph_panel;
	private Popup popup;
	private JScrollPane scrollpane;
	private HashMap<Pointer, JCheckBox> pointer_checkboxes;
	private JPanel pointer_list_panel;
	private JList pointer_list;
	private JCheckBox show_all_vertices_checkbox;

	public PopupPointerButton(WNGraph wngraph, WNGraphPanel wngraph_panel) {
		this.wngraph = wngraph;
		this.wngraph_panel = wngraph_panel;

		setMargin(new Insets(0, 0, 0, 0));

		/* construct the popup for the pointers list **************************/

		pointer_list_panel = new JPanel();
		pointer_list_panel.setLayout(new BoxLayout(pointer_list_panel,
				BoxLayout.Y_AXIS));

		/* a tool panel *******************************************************/

		JPanel tool_panel = new JPanel();
		tool_panel.setLayout(new BoxLayout(tool_panel, BoxLayout.X_AXIS));
		JButton select_all_button = new JButton("all");
		select_all_button
				.setActionCommand(ACTION_COMMAND_SELECT_ALL_POINTERS_TYPES);
		select_all_button.addActionListener(this);
		tool_panel.add(select_all_button);

		JButton select_none_button = new JButton("none");
		select_none_button
				.setActionCommand(ACTION_COMMAND_SELECT_NONE_POINTERS_TYPES);
		select_none_button.addActionListener(this);
		tool_panel.add(select_none_button);

		tool_panel.add(Box.createHorizontalGlue());

		show_all_vertices_checkbox = new JCheckBox("show all vertices");
		show_all_vertices_checkbox.setSelected(true);
		show_all_vertices_checkbox.addChangeListener(this);
		tool_panel.add(show_all_vertices_checkbox);
		pointer_list_panel.add(tool_panel);

		/* the list of pointers types *****************************************/
		pointer_list = new JList();

		pointer_list.setCellRenderer(new JCheckboxCellRenderer());

		DefaultListModel model = new DefaultListModel();
		pointer_checkboxes = new HashMap<Pointer, JCheckBox>();
		for (Pointer pointer : WNUtils.getPointers()) {
			String pointer_name = pointer.getName();
			JCheckBox pointer_checkbox = new JCheckBox(pointer_name);
			pointer_checkbox.addChangeListener(this);
			pointer_checkbox.setOpaque(true);
			pointer_checkbox.setBackground(WNUtils.getPointersColor().get(
					pointer));
			pointer_checkbox.setSelected(true); //by default all pointers are displayed whatever the types.
			model.addElement(pointer_checkbox);
			pointer_checkboxes.put(pointer, pointer_checkbox);
		}

		pointer_list.setModel(model);

		/* add a mouse listener to handle changing selection ******************/

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

		/* push the rendering filter initial state to the WNGraph *************/
		wngraph.setRenderingFilter(getSelectedPointersTypes(),
				show_all_vertices_checkbox.isSelected());
	}

	public HashMap<Pointer, Boolean> getSelectedPointersTypes() {
		HashMap<Pointer, Boolean> pointers_list = new HashMap<Pointer, Boolean>();
		for (Pointer pointer : pointer_checkboxes.keySet()) {
			if (pointer_checkboxes.get(pointer).isSelected()) {
				pointers_list.put(pointer, true);
			} else {
				pointers_list.put(pointer, false);
			}
		}
		return pointers_list;
	}

	public void hidePopup() {
		if (popup != null) {
			popup.hide();
			setSelected(false);
		}
	}

	/***************************************************************************
	 * implementation of ActionListener.
	 **************************************************************************/

	@Override
	public void actionPerformed(ActionEvent e) {
		String action_command = e.getActionCommand();

		if (action_command.equals(ACTION_COMMAND_SELECT_ALL_POINTERS_TYPES)) {
			for (JCheckBox checkbox : pointer_checkboxes.values()) {
				checkbox.setSelected(true);
			}
		}

		else

		if (action_command.equals(ACTION_COMMAND_SELECT_NONE_POINTERS_TYPES)) {
			for (JCheckBox checkbox : pointer_checkboxes.values()) {
				checkbox.setSelected(false);
			}
		}

		if (pointer_list_panel.isVisible()) {
			pointer_list_panel.repaint();
		}
		wngraph.setRenderingFilter(getSelectedPointersTypes(),
				show_all_vertices_checkbox.isSelected());
		wngraph.applyRenderingFilter();
		wngraph_panel.getMainPanel().repaint();
	}

	/***************************************************************************
	 * implementation of ChangeListener.
	 **************************************************************************/

	@Override
	public void stateChanged(ChangeEvent e) {
		wngraph.setRenderingFilter(getSelectedPointersTypes(),
				show_all_vertices_checkbox.isSelected());
		wngraph.applyRenderingFilter();
		wngraph_panel.getMainPanel().repaint();
	}

}