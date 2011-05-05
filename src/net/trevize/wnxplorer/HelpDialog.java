package net.trevize.wnxplorer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.xhtmlrenderer.simple.FSScrollPane;
import org.xhtmlrenderer.simple.XHTMLPanel;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * HelpDialog.java - Oct 29, 2010
 */

public class HelpDialog extends JDialog implements WindowListener,
		HyperlinkListener {

	private JPanel main_panel;
	private XHTMLPanel xhtml_panel;
	private FSScrollPane scrollpane;
	private JButton close_button;

	public HelpDialog(JComponent parent) {
		setModalityType(ModalityType.APPLICATION_MODAL);
		setTitle("WNXplorer help");
		init();
		setIconImage(new ImageIcon(WNXplorerProperties.getWnxplorer_icon_path())
				.getImage());
		setSize(768, 512);
		setLocationRelativeTo(parent);
	}

	private void init() {
		//setting swing components.
		main_panel = new JPanel();
		setLayout(new BorderLayout());
		add(main_panel, BorderLayout.CENTER);
		main_panel.setLayout(new BorderLayout());
		main_panel.setBorder(new EmptyBorder(10, 10, 10, 10));

		scrollpane = new FSScrollPane();
		main_panel.add(scrollpane, BorderLayout.CENTER);

		//remove the ugly border of the scrollpane viewport.
		//		Border empty = new EmptyBorder(0, 0, 0, 0);
		//		scrollpane.setViewportBorder(empty);
		//		scrollpane.getHorizontalScrollBar().setBorder(empty);
		//		scrollpane.getVerticalScrollBar().setBorder(empty);

		XHTMLPanel xhtml_panel = new XHTMLPanel();
		xhtml_panel
				.setDocument("file:////home/nicolas/dev/WNXplorer/workspace/WNXplorer/gfx/Help.html");

		scrollpane.setViewportView(xhtml_panel);

		JPanel panel0 = new JPanel();
		panel0.setBorder(new EmptyBorder(10, 0, 0, 0));
		panel0.setLayout(new BoxLayout(panel0, BoxLayout.X_AXIS));
		panel0.add(Box.createGlue());
		close_button = new JButton("Close");
		close_button.setIcon(new ImageIcon(WNXplorerProperties
				.getIcon_path_close()));
		close_button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		panel0.add(close_button);
		panel0.add(Box.createGlue());
		main_panel.add(panel0, BorderLayout.SOUTH);
	}

	/***************************************************************************
	 * implementation of WindowListener.
	 **************************************************************************/

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		setVisible(false);
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	/***************************************************************************
	 * implementation of HyperlinkListener.
	 **************************************************************************/

	@Override
	public void hyperlinkUpdate(HyperlinkEvent e) {
		if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
			System.out.println("link clicked");
			System.out.println(e.getDescription());

			try {
				java.awt.Desktop.getDesktop().browse(
						new URI(e.getDescription()));
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (URISyntaxException e1) {
				e1.printStackTrace();
			}
		}
	}

}
