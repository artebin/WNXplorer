package net.trevize.wnxplorer.explorer;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.util.regex.Pattern;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.border.MatteBorder;

import net.trevize.FSExplorer.FSTree.FSTree;
import net.trevize.gui.FSExplorer.FSE.FSEEvent;
import net.trevize.gui.FSExplorer.FSE.FSEListener;
import net.trevize.gui.layout.CellStyle;
import net.trevize.gui.layout.XGridBag;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * GetWordNetPathDialog.java - Oct 29, 2010
 */

public class GetWordNetPathDialog extends JDialog implements FSEListener {

	private FSTree fstree;

	public GetWordNetPathDialog(JComponent main_component) {
		setModalityType(ModalityType.APPLICATION_MODAL);
		setTitle("WordNet installation path");
		init();
		setSize(512, 512);
		setLocationRelativeTo(main_component);
	}

	private void init() {
		setLayout(new GridBagLayout());
		XGridBag xgb = new XGridBag(this);

		JLabel header = new JLabel();
		header.setOpaque(true);
		header.setBackground(Color.WHITE);
		header.setBorder(new MatteBorder(0, 15, 0, 0, Color.BLUE));
		header
				.setText("<html><body><h1>WordNet installation path</h1><p>WNXplorer needs to know where is installed WordNet, indicate here the path to the <b><code>dict</code></b> directory of WordNet.</p>"
						+ "<p>This path will be recorded in the WNXplorer.properties file, the next times you launch WNXplorer, I will not ask you again for it.</p>");
		CellStyle style0 = new CellStyle(1., 0., GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);
		xgb.add(header, style0, 0, 0, 1, 2);

		String file_filter = "(^[^\\.]$|^[^\\.].*[^~]$)";
		String dir_filter = "^[^\\.].*$";
		fstree = new FSTree(File.listRoots()[0], Pattern.compile(file_filter),
				Pattern.compile(dir_filter), true);
		fstree.addFSEListener(this);
		CellStyle style1 = new CellStyle(1., 1., GridBagConstraints.NORTHWEST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		xgb.add(fstree, style1, 1, 0, 1, 2);
	}

	/***************************************************************************
	 * implementation of FSEListener
	 **************************************************************************/

	@Override
	public void focusChanged(FSEEvent e) {
	}

	@Override
	public void open(FSEEvent e) {
		WNXplorerProperties.setWN_PATH(e.getSelectedFile().getPath());
		setVisible(false);
	}

	@Override
	public void pathChanged(FSEEvent e) {
	}

	@Override
	public void selectionChanged(FSEEvent e) {
	}

}
