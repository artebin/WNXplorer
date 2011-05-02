package net.trevize.knetvis;

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

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * PopupPointerButton.java - Oct 19, 2010
 */

public class KNetPopupSemanticRelationButton extends JToggleButton implements
		ActionListener, ChangeListener {

	private static final String ACTION_COMMAND_SELECT_ALL_SEMANTIC_RELATION = "ACTION_COMMAND_SELECT_ALL_SEMANTIC_RELATION";
	private static final String ACTION_COMMAND_SELECT_NONE_SEMANTIC_RELATION = "ACTION_COMMAND_SELECT_NONE_SEMANTIC_RELATION";

	private AbstractAction show_popup_action = new AbstractAction("",
			new ImageIcon(KNetVisProperties
					.getIcon_path_semantic_relation_selector())) {
		public void actionPerformed(ActionEvent e) {
			JToggleButton b = (JToggleButton) e.getSource();
			if (!b.isSelected() && popup != null) {
				popup.hide();
				popup = null;
			} else {
				if (!(semantic_relation_list.getPreferredSize().height > scrollpane
						.getViewport().getPreferredSize().height)) {
					scrollpane.getViewport().setPreferredSize(
							semantic_relation_list.getPreferredSize());
					semantic_relation_list_panel.setPreferredSize(scrollpane
							.getPreferredSize());
				}

				Dimension panelSize = semantic_relation_list_panel
						.getPreferredSize();

				//right-align it with the popup button, so it pops up just above it.
				Point location = new Point(0 - panelSize.width
						+ getPreferredSize().width, 0 - panelSize.height);
				SwingUtilities.convertPointToScreen(location,
						KNetPopupSemanticRelationButton.this);

				popup = PopupFactory.getSharedInstance().getPopup(
						KNetPopupSemanticRelationButton.this,
						semantic_relation_list_panel, location.x, location.y);

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

	private KNetGraph knetgraph;
	private KNetGraphViewer knetgraph_viewer;
	private Popup popup;
	private JScrollPane scrollpane;
	private HashMap<KNetSemanticRelation, JCheckBox> semantic_relation_checkboxes;
	private JPanel semantic_relation_list_panel;
	private JList semantic_relation_list;
	private JCheckBox show_all_vertices_checkbox;

	public KNetPopupSemanticRelationButton(KNetGraph knetgraph,
			KNetGraphViewer knetgraph_viewer) {
		this.knetgraph = knetgraph;
		this.knetgraph_viewer = knetgraph_viewer;

		setMargin(new Insets(0, 0, 0, 0));

		/* construct the popup for the semantic relation list **************************/

		semantic_relation_list_panel = new JPanel();
		semantic_relation_list_panel.setLayout(new BoxLayout(
				semantic_relation_list_panel, BoxLayout.Y_AXIS));

		/* a tool panel *******************************************************/

		JPanel tool_panel = new JPanel();
		tool_panel.setLayout(new BoxLayout(tool_panel, BoxLayout.X_AXIS));
		JButton select_all_button = new JButton("all");
		select_all_button
				.setActionCommand(ACTION_COMMAND_SELECT_ALL_SEMANTIC_RELATION);
		select_all_button.addActionListener(this);
		tool_panel.add(select_all_button);

		JButton select_none_button = new JButton("none");
		select_none_button
				.setActionCommand(ACTION_COMMAND_SELECT_NONE_SEMANTIC_RELATION);
		select_none_button.addActionListener(this);
		tool_panel.add(select_none_button);

		tool_panel.add(Box.createHorizontalGlue());

		show_all_vertices_checkbox = new JCheckBox("show all vertices");
		show_all_vertices_checkbox.setSelected(true);
		show_all_vertices_checkbox.addChangeListener(this);
		tool_panel.add(show_all_vertices_checkbox);
		semantic_relation_list_panel.add(tool_panel);

		/* the list of pointers types *****************************************/
		semantic_relation_list = new JList();
		semantic_relation_list.setCellRenderer(new JCheckboxCellRenderer());
		DefaultListModel model = new DefaultListModel();
		semantic_relation_checkboxes = new HashMap<KNetSemanticRelation, JCheckBox>();

		for (KNetSemanticRelation semrel : knetgraph
				.getSemanticRelationReference().getSemanticRelationList()) {
			String semrel_name = semrel.getLabel();
			JCheckBox semrel_checkbox = new JCheckBox(semrel_name);
			semrel_checkbox.addChangeListener(this);
			semrel_checkbox.setOpaque(true);
			semrel_checkbox.setBackground(semrel.getColor());
			semrel_checkbox.setSelected(true); //by default all pointers are displayed whatever the types.
			model.addElement(semrel_checkbox);
			semantic_relation_checkboxes.put(semrel, semrel_checkbox);
		}

		semantic_relation_list.setModel(model);

		/* add a mouse listener to handle changing selection ******************/

		semantic_relation_list.addMouseListener(new MouseAdapter() {
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
		scrollpane.setViewportView(semantic_relation_list);
		semantic_relation_list_panel.add(scrollpane);

		setAction(show_popup_action);

		/* push the rendering filter initial state to the KNetGraph *************/
		knetgraph.setRenderingFilter(getSelectedPointersTypes(),
				show_all_vertices_checkbox.isSelected());
	}

	public HashMap<KNetSemanticRelation, Boolean> getSelectedPointersTypes() {
		HashMap<KNetSemanticRelation, Boolean> semrel_list = new HashMap<KNetSemanticRelation, Boolean>();
		for (KNetSemanticRelation semrel : semantic_relation_checkboxes
				.keySet()) {
			if (semantic_relation_checkboxes.get(semrel).isSelected()) {
				semrel_list.put(semrel, true);
			} else {
				semrel_list.put(semrel, false);
			}
		}
		return semrel_list;
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

		if (action_command.equals(ACTION_COMMAND_SELECT_ALL_SEMANTIC_RELATION)) {
			for (JCheckBox checkbox : semantic_relation_checkboxes.values()) {
				checkbox.setSelected(true);
			}
		}

		else

		if (action_command.equals(ACTION_COMMAND_SELECT_NONE_SEMANTIC_RELATION)) {
			for (JCheckBox checkbox : semantic_relation_checkboxes.values()) {
				checkbox.setSelected(false);
			}
		}

		if (semantic_relation_list_panel.isVisible()) {
			semantic_relation_list_panel.repaint();
		}

		knetgraph.setRenderingFilter(getSelectedPointersTypes(),
				show_all_vertices_checkbox.isSelected());
		knetgraph.applyRenderingFilter();
		knetgraph_viewer.refreshViews();
	}

	/***************************************************************************
	 * implementation of ChangeListener.
	 **************************************************************************/

	@Override
	public void stateChanged(ChangeEvent e) {
		knetgraph.setRenderingFilter(getSelectedPointersTypes(),
				show_all_vertices_checkbox.isSelected());
		knetgraph.applyRenderingFilter();
		knetgraph_viewer.refreshViews();
	}

}