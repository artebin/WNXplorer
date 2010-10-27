package net.trevize.wnxplorer.explorer;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * PopupSemanticRelationButton.java - Oct 19, 2010
 */

public class PopupSemanticRelationButton extends JToggleButton {

	private Popup semanticRelationPopup;
	private JScrollPane jsp;

	private AbstractAction semanticRelationAction = new AbstractAction("",
			new ImageIcon("./gfx/semantic_web_32.png")) {
		public void actionPerformed(ActionEvent e) {
			JToggleButton b = (JToggleButton) e.getSource();
			if (!b.isSelected() && semanticRelationPopup != null) {
				semanticRelationPopup.hide();
				semanticRelationPopup = null;
			} else {
				if (!(semanticRelationList.getPreferredSize().height > jsp
						.getViewport().getPreferredSize().height)) {
					jsp.getViewport().setPreferredSize(
							semanticRelationList.getPreferredSize());
					semanticRelationPanel.setPreferredSize(jsp
							.getPreferredSize());
				}

				Dimension panelSize = semanticRelationPanel.getPreferredSize();

				// Right-align it with the volume button, so it pops up just above it
				Point location = new Point(0 - panelSize.width
						+ getPreferredSize().width, 0 - panelSize.height);
				SwingUtilities.convertPointToScreen(location,
						PopupSemanticRelationButton.this);

				semanticRelationPopup = PopupFactory.getSharedInstance()
						.getPopup(PopupSemanticRelationButton.this,
								semanticRelationPanel, location.x, location.y);

				semanticRelationPopup.show();
			}
		}
	};

	private class JCheckboxCellRenderer implements ListCellRenderer {
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			JCheckBox checkbox = (JCheckBox) value;

			//			checkbox.setBackground(isSelected ? getSelectionBackground()
			//					: getBackground());
			//
			//			checkbox.setForeground(isSelected ? getSelectionForeground()
			//					: getForeground());

			checkbox.setEnabled(isEnabled());
			checkbox.setFont(getFont());
			checkbox.setFocusPainted(false);
			checkbox.setBorderPainted(true);

			//			checkbox
			//					.setBorder(isSelected ? UIManager
			//							.getBorder("List.focusCellHighlightBorder")
			//							: noFocusBorder);

			return checkbox;
		}
	}


	private JPanel semanticRelationPanel;
	private JList semanticRelationList;

	public PopupSemanticRelationButton() {
		setMargin(new Insets(0, 0, 0, 0));

		/*
		 * Construct the popup for the list of the semantic relations.
		 */
		semanticRelationPanel = new JPanel();
		semanticRelationPanel.setLayout(new BoxLayout(semanticRelationPanel,
				BoxLayout.Y_AXIS));

		semanticRelationList = new JList();

		semanticRelationList.setCellRenderer(new JCheckboxCellRenderer());

		DefaultListModel model = new DefaultListModel();
		model.addElement(new JCheckBox("hypernymie"));
		model.addElement(new JCheckBox("hyponymie"));
		model.addElement(new JCheckBox("meronymie"));
		model.addElement(new JCheckBox("holonymie"));
		model.addElement(new JCheckBox("antonymie"));
		model.addElement(new JCheckBox("partonymie"));
		model.addElement(new JCheckBox("troponymie"));

		semanticRelationList.setModel(model);

		// Add a mouse listener to handle changing selection

		semanticRelationList.addMouseListener(new MouseAdapter() {
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

		jsp = new JScrollPane();
		jsp.setViewportView(semanticRelationList);
		semanticRelationPanel.add(jsp);

		setAction(semanticRelationAction);
	}

}