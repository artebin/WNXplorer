package net.trevize.wnxplorer.explorer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * HelpDialog.java - Oct 29, 2010
 */

public class HelpDialog extends JDialog implements WindowListener,
		HyperlinkListener {

	private static final String STYLESHEET_FILEPATH = "./gfx/style.css";

	private JPanel main_panel;
	private JScrollPane scrollpane;
	private JEditorPane editorp;
	private String help_text_content;
	private JButton close_button;

	public HelpDialog(JComponent main_component) {
		setModalityType(ModalityType.APPLICATION_MODAL);
		setTitle("Load dataset");
		init();
		setSize(512, 512);
		setLocationRelativeTo(main_component);
	}

	private void init() {
		//reading the help HTML file.
		try {
			FileReader fr = new FileReader(new File("./gfx/Help.html"));
			BufferedReader br = new BufferedReader(fr);

			StringBuffer sb = new StringBuffer();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				line = br.readLine();
			}

			help_text_content = sb.toString();

			br.close();
			fr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		//setting swing components.
		main_panel = new JPanel();
		setLayout(new BorderLayout());
		add(main_panel, BorderLayout.CENTER);
		main_panel.setLayout(new BorderLayout());
		main_panel.setBorder(new EmptyBorder(10, 10, 10, 10));

		scrollpane = new JScrollPane();
		main_panel.add(scrollpane, BorderLayout.CENTER);

		//remove the ugly border of the scrollpane viewport.
		Border empty = new EmptyBorder(0, 0, 0, 0);
		scrollpane.setViewportBorder(empty);
		scrollpane.getHorizontalScrollBar().setBorder(empty);
		scrollpane.getVerticalScrollBar().setBorder(empty);

		editorp = new JEditorPane();
		editorp.addHyperlinkListener(this);
		HTMLEditorKit kit = new HTMLEditorKit();
		StyleSheet ss = new StyleSheet();

		//loading the stylesheet.
		StringBuffer sb = null;
		try {
			FileReader fr = new FileReader(STYLESHEET_FILEPATH);
			BufferedReader br = new BufferedReader(fr);
			sb = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
			fr.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		ss.addRule(sb.toString());
		kit.getStyleSheet().addStyleSheet(ss);
		editorp.setEditorKit(kit);
		editorp.setEditable(false);
		scrollpane.setViewportView(editorp);

		editorp.setText(help_text_content);

		JPanel panel0 = new JPanel();
		panel0.setLayout(new BoxLayout(panel0, BoxLayout.X_AXIS));
		panel0.add(Box.createGlue());
		close_button = new JButton("Close");
		close_button.setIcon(new ImageIcon("./gfx/gtk_close.png"));
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
