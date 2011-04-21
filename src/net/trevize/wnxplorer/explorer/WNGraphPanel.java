package net.trevize.wnxplorer.explorer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.trevize.wnxplorer.explorer.dialogs.HelpDialog;
import net.trevize.wnxplorer.jung.PointerEdge;
import net.trevize.wnxplorer.jung.PointerEdgeDrawPaintTransformer;
import net.trevize.wnxplorer.jung.PointerEdgeLabelTranformer;
import net.trevize.wnxplorer.jung.PopupGraphMousePlugin;
import net.trevize.wnxplorer.jung.SynsetVertex;
import net.trevize.wnxplorer.jung.SynsetVertexLabelTransformer;
import net.trevize.wnxplorer.jung.SynsetVertexShapeSizeAspectTransformer;
import net.trevize.wnxplorer.jung.SynsetVertexTooltip;
import net.trevize.wnxplorer.jung.VertexStrokeHighlight;
import edu.uci.ics.jung.algorithms.layout.FRLayout2;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse.Mode;
import edu.uci.ics.jung.visualization.control.SatelliteVisualizationViewer;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.PickableEdgePaintTransformer;
import edu.uci.ics.jung.visualization.decorators.PickableVertexPaintTransformer;
import edu.uci.ics.jung.visualization.renderers.BasicEdgeArrowRenderingSupport;
import edu.uci.ics.jung.visualization.renderers.Renderer;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * WNGraphPanel.java - Mar 24, 2010
 */

public class WNGraphPanel implements MouseListener, KeyListener, ActionListener {

	public static final String ACTION_COMMAND_MODE = "ACTION_COMMAND_MODE";
	public static final String ACTION_COMMAND_RESTART_LAYOUT = "ACTION_COMMAND_RESTART_LAYOUT";
	public static final String ACTION_COMMAND_HELP = "ACTION_COMMAND_HELP";

	private WNGraph wngraph;

	//for jung.
	private DirectedSparseMultigraph<SynsetVertex, PointerEdge> g;
	private VisualizationViewer<SynsetVertex, PointerEdge> vv1;
	private SatelliteVisualizationViewer<SynsetVertex, PointerEdge> vv2;
	private Layout<SynsetVertex, PointerEdge> layout;

	private GraphZoomScrollPane scrollpane;
	private DefaultModalGraphMouse<SynsetVertex, PointerEdge> gm;

	private SynsetVertex last_clicked_vertex;

	//classical swing components for the StatusBar.
	private JPanel main_panel;
	private JCheckBox picking_mode_checkbox;
	private JButton restart_layout_button;
	private PopupPointerButton pointer_selector_button;
	private JButton help_button;
	private HelpDialog help_dialog;

	public WNGraphPanel(WNGraph wngraph) {
		this.wngraph = wngraph;
		this.g = wngraph.getG();

		init();
	}

	public void init() {
		main_panel = new JPanel();
		main_panel.setLayout(new BorderLayout());

		//setting the panel BorderLayout.CENTER
		layout = new FRLayout2<SynsetVertex, PointerEdge>(g);

		// create one model that both views will share
		VisualizationModel<SynsetVertex, PointerEdge> vm = new DefaultVisualizationModel<SynsetVertex, PointerEdge>(
				layout);

		vv1 = new VisualizationViewer<SynsetVertex, PointerEdge>(layout);
		vv2 = new SatelliteVisualizationViewer<SynsetVertex, PointerEdge>(vv1);

		vv2.getRenderContext().setEdgeDrawPaintTransformer(
				new PickableEdgePaintTransformer<PointerEdge>(vv2
						.getPickedEdgeState(), Color.black, Color.cyan));
		vv2.getRenderContext().setVertexFillPaintTransformer(
				new PickableVertexPaintTransformer<SynsetVertex>(vv2
						.getPickedVertexState(), Color.red, Color.yellow));

		ScalingControl vv2Scaler = new CrossoverScalingControl();
		vv2.scaleToLayout(vv2Scaler);

		//setting the PickedVertexPaintTransformer.
		PickableVertexPaintTransformer<SynsetVertex> vpt = new PickableVertexPaintTransformer<SynsetVertex>(
				vv1.getPickedVertexState(), Color.WHITE, Color.YELLOW);
		vv1.getRenderContext().setVertexFillPaintTransformer(vpt);

		//setting the PickedEdgePaintTransformer.
		PickableEdgePaintTransformer<PointerEdge> vet = new PickableEdgePaintTransformer<PointerEdge>(
				vv1.getPickedEdgeState(), Color.GRAY, Color.YELLOW);
		vv1.getRenderContext().setEdgeDrawPaintTransformer(vet);
		vv1.getRenderContext().setArrowDrawPaintTransformer(vet);

		//setting the VertexShapeTransformer.
		vv1.getRenderContext()
				.setVertexShapeTransformer(
						new SynsetVertexShapeSizeAspectTransformer<SynsetVertex, PointerEdge>(
								g));

		//setting the EdgeShapeTransformer.
		vv1.getRenderContext().setEdgeShapeTransformer(
				new EdgeShape.Line<SynsetVertex, PointerEdge>());

		vv1.getRenderContext().setEdgeDrawPaintTransformer(
				new PointerEdgeDrawPaintTransformer());

		//setting the Arrow transformer.
		//vv.getRenderContext().setArrowFillPaintTransformer(new PointerEdgeDrawPaintTransformer());
		//vv.getRenderContext().setArrowDrawPaintTransformer(new PointerEdgeDrawPaintTransformer());
		vv1.getRenderer()
				.getEdgeRenderer()
				.setEdgeArrowRenderingSupport(
						new BasicEdgeArrowRenderingSupport());

		//setting the VertexStrokeTransformer.
		vv1.getRenderContext().setVertexStrokeTransformer(
				new VertexStrokeHighlight<SynsetVertex, PointerEdge>(this));

		//setting the label renderer.
		/*
		SynsetVertexLabelRenderer<SynsetVertex> vertex_label_renderer = new SynsetVertexLabelRenderer<SynsetVertex>(
				Color.YELLOW);
		vertex_label_renderer.setBackgroundColor(Color.BLACK);
		vertex_label_renderer.setForegroundColor(Color.WHITE);
		vv.getRenderContext().setVertexLabelRenderer(vertex_label_renderer);
		 */

		vv1.getRenderContext().setVertexLabelTransformer(
				new SynsetVertexLabelTransformer());
		vv1.getRenderer().getVertexLabelRenderer()
				.setPosition(Renderer.VertexLabel.Position.E);

		vv1.getRenderContext().setEdgeLabelTransformer(
				new PointerEdgeLabelTranformer());

		scrollpane = new GraphZoomScrollPane(vv1);
		gm = new DefaultModalGraphMouse<SynsetVertex, PointerEdge>();
		gm.add(new PopupGraphMousePlugin(wngraph, this));

		vv1.setGraphMouse(gm);

		vv1.setVertexToolTipTransformer(new SynsetVertexTooltip<String>());

		vv1.setBackground(Color.WHITE);
		vv1.addMouseListener(this);
		vv1.addKeyListener(this);

		main_panel.add(scrollpane, BorderLayout.CENTER);

		//setting the panel BorderLayout.SOUTH (i.e. the status bar).
		StatusBar status_bar = new StatusBar();
		status_bar.setBorder(new EmptyBorder(4, 4, 4, 4));

		picking_mode_checkbox = new JCheckBox("picking mode");
		picking_mode_checkbox.setActionCommand(ACTION_COMMAND_MODE);
		picking_mode_checkbox.addActionListener(this);
		status_bar.addComponent("mode", picking_mode_checkbox);

		restart_layout_button = new JButton("Restart layout");
		restart_layout_button.setActionCommand(ACTION_COMMAND_RESTART_LAYOUT);
		restart_layout_button.addActionListener(this);
		status_bar.addComponent("auto layout", restart_layout_button);

		pointer_selector_button = new PopupPointerButton(wngraph, this);
		status_bar.addComponent("PopupPointerButton", pointer_selector_button);

		help_button = new JButton(new ImageIcon(
				"./gfx/org/freedesktop/tango/help-browser.png"));
		help_button.setActionCommand(ACTION_COMMAND_HELP);
		help_button.addActionListener(this);
		help_dialog = new HelpDialog(main_panel);
		status_bar.addComponent("help button", help_button);

		main_panel.add(status_bar, BorderLayout.SOUTH);
	}

	public JPanel getPanel() {
		return main_panel;
	}

	public void addPickingPlugin(
			edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin picking_plugin) {
		gm.add(picking_plugin);
	}

	/***************************************************************************
	 * implementation of KeyListener.
	 **************************************************************************/

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getKeyChar() == 'p') {
			picking_mode_checkbox.setSelected(!picking_mode_checkbox
					.isSelected());
			if (gm != null) { //if a GraphZoomScrollPane has ever been instantiated.
				if (picking_mode_checkbox.isSelected()) {
					gm.setMode(Mode.PICKING);
				} else {
					gm.setMode(Mode.TRANSFORMING);
				}
			}
		}

		else

		if (e.isAltDown() && e.getKeyChar() == 'l') {
			System.out.println("reset layout");
			layout.reset();
		}
	}

	/***************************************************************************
	 * implementation of MouseListener.
	 **************************************************************************/

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getSource() == vv1) {
			vv1.requestFocus();
		}
	}

	/***************************************************************************
	 * implementation of ActionListener.
	 **************************************************************************/

	@Override
	public void actionPerformed(ActionEvent e) {
		String action_command = e.getActionCommand();

		if (action_command.equals(ACTION_COMMAND_MODE)) {
			if (gm != null) { //if a GraphZoomScrollPane has ever been instantiated.
				if (picking_mode_checkbox.isSelected()) {
					gm.setMode(Mode.PICKING);
				} else {
					gm.setMode(Mode.TRANSFORMING);
				}
			}
		}

		else

		if (action_command.equals(ACTION_COMMAND_RESTART_LAYOUT)) {
			layout = new FRLayout2<SynsetVertex, PointerEdge>(g);
			vv1.setGraphLayout(layout);
		}

		else

		if (action_command.equals(ACTION_COMMAND_HELP)) {
			help_dialog.setVisible(true);
		}
	}

	/***************************************************************************
	 * getters and setters.
	 **************************************************************************/

	public VisualizationViewer<SynsetVertex, PointerEdge> getVisualizationViewer() {
		return vv1;
	}

	public Layout<SynsetVertex, PointerEdge> getLayout() {
		return layout;
	}

	public SynsetVertex getLast_clicked_vertex() {
		return last_clicked_vertex;
	}

	public void setLast_clicked_vertex(SynsetVertex lastClickedVertex) {
		last_clicked_vertex = lastClickedVertex;
	}

	public DefaultModalGraphMouse<SynsetVertex, PointerEdge> getGm() {
		return gm;
	}

	public PopupPointerButton getPointer_selector_button() {
		return pointer_selector_button;
	}

	public SatelliteVisualizationViewer<SynsetVertex, PointerEdge> getSatelliteVisualizationViewer() {
		return vv2;
	}

}
