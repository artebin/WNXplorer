package net.trevize.wnxplorer.explorer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
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
import net.trevize.wnxplorer.jwi.WNUtils;
import edu.mit.jwi.item.ISynset;
import edu.uci.ics.jung.algorithms.layout.FRLayout2;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.Layer;
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
	public static final String ACTION_COMMAND_SATVIEW_GO_N = "ACTION_COMMAND_SATVIEW_GO_N";
	public static final String ACTION_COMMAND_SATVIEW_GO_E = "ACTION_COMMAND_SATVIEW_GO_E";
	public static final String ACTION_COMMAND_SATVIEW_GO_S = "ACTION_COMMAND_SATVIEW_GO_S";
	public static final String ACTION_COMMAND_SATVIEW_GO_W = "ACTION_COMMAND_SATVIEW_GO_W";
	public static final String ACTION_COMMAND_SATVIEW_ZOOM_IN = "ACTION_COMMAND_SATVIEW_ZOOM_IN";
	public static final String ACTION_COMMAND_SATVIEW_ZOOM_OUT = "ACTION_COMMAND_SATVIEW_ZOOM_OUT";

	private Explorer explorer;

	//for jung.
	private VisualizationViewer<SynsetVertex, PointerEdge> vv1;
	private SatelliteVisualizationViewer<SynsetVertex, PointerEdge> vv2;
	private Layout<SynsetVertex, PointerEdge> layout;

	private GraphZoomScrollPane scrollpane;
	private DefaultModalGraphMouse<SynsetVertex, PointerEdge> gm;

	private SynsetVertex last_clicked_vertex;

	//classical swing components for the StatusBar.
	private JPanel main_panel;
	private JPanel satellite_view_panel;
	private JCheckBox picking_mode_checkbox;
	private JButton restart_layout_button;
	private PopupPointerButton pointer_selector_button;
	private JButton help_button;
	private HelpDialog help_dialog;

	public WNGraphPanel(Explorer explorer) {
		this.explorer = explorer;

		init();
	}

	public void init() {
		main_panel = new JPanel();
		main_panel.setLayout(new BorderLayout());

		//setting the panel BorderLayout.CENTER
		layout = new FRLayout2<SynsetVertex, PointerEdge>(explorer.getWngraph()
				.getG());

		//create the two views
		vv1 = new VisualizationViewer<SynsetVertex, PointerEdge>(layout);
		vv2 = new SatelliteVisualizationViewer<SynsetVertex, PointerEdge>(vv1);

		//initialize the views
		initGraphView();
		initSatelliteView();

		//initialize the popup panel for graph nodes
		gm = new DefaultModalGraphMouse<SynsetVertex, PointerEdge>();
		gm.add(new PopupGraphMousePlugin(explorer.getWngraph(), this));
		vv1.setGraphMouse(gm);

		//add the graph view in a jscrollpane and this jscrollpane in the view panel
		scrollpane = new GraphZoomScrollPane(vv1);
		main_panel.add(scrollpane, BorderLayout.CENTER);

		//initialize the status bar
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

		pointer_selector_button = new PopupPointerButton(explorer.getWngraph(),
				this);
		status_bar.addComponent("PopupPointerButton", pointer_selector_button);

		help_button = new JButton(new ImageIcon(
				WNXplorerProperties.getIcon_path_help()));
		help_button.setActionCommand(ACTION_COMMAND_HELP);
		help_button.addActionListener(this);
		help_dialog = new HelpDialog(main_panel);
		status_bar.addComponent("help button", help_button);

		//add the status bar to the main panel
		main_panel.add(status_bar, BorderLayout.SOUTH);
	}

	private void initGraphView() {
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
								explorer.getWngraph().getG()));

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

		vv1.setVertexToolTipTransformer(new SynsetVertexTooltip<String>());

		vv1.setBackground(Color.WHITE);
		vv1.addMouseListener(this);
		vv1.addKeyListener(this);
	}

	private void initSatelliteView() {
		satellite_view_panel = new JPanel();
		satellite_view_panel.setLayout(new BorderLayout());

		//initialize the SatelliteVisualizationViewer
		vv2.getRenderContext()
				.setVertexShapeTransformer(
						new SynsetVertexShapeSizeAspectTransformer<SynsetVertex, PointerEdge>(
								explorer.getWngraph().getG()));

		PickableVertexPaintTransformer<SynsetVertex> vpt = new PickableVertexPaintTransformer<SynsetVertex>(
				vv1.getPickedVertexState(), Color.WHITE, Color.YELLOW);
		vv2.getRenderContext().setVertexFillPaintTransformer(vpt);

		vv2.getRenderContext().setEdgeShapeTransformer(
				new EdgeShape.Line<SynsetVertex, PointerEdge>());

		vv2.getRenderContext().setVertexStrokeTransformer(
				new VertexStrokeHighlight<SynsetVertex, PointerEdge>(this));

		ScalingControl vv2Scaler = new CrossoverScalingControl();
		vv2.scaleToLayout(vv2Scaler);

		satellite_view_panel.add(vv2, BorderLayout.CENTER);

		//initialize the satellite view toolbar
		JPanel p = new JPanel();
		p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));

		JButton b_satview_go_n = new JButton("N");
		b_satview_go_n.setActionCommand(ACTION_COMMAND_SATVIEW_GO_N);
		b_satview_go_n.addActionListener(this);

		JButton b_satview_go_e = new JButton("E");
		b_satview_go_e.setActionCommand(ACTION_COMMAND_SATVIEW_GO_E);
		b_satview_go_e.addActionListener(this);

		JButton b_satview_go_s = new JButton("S");
		b_satview_go_s.setActionCommand(ACTION_COMMAND_SATVIEW_GO_S);
		b_satview_go_s.addActionListener(this);

		JButton b_satview_go_w = new JButton("W");
		b_satview_go_w.setActionCommand(ACTION_COMMAND_SATVIEW_GO_W);
		b_satview_go_w.addActionListener(this);

		JButton b_zi = new JButton("+");
		b_zi.setActionCommand(ACTION_COMMAND_SATVIEW_ZOOM_IN);
		b_zi.addActionListener(this);

		JButton b_zo = new JButton("-");
		b_zo.setActionCommand(ACTION_COMMAND_SATVIEW_ZOOM_OUT);
		b_zo.addActionListener(this);

		p.add(b_satview_go_n);
		p.add(b_satview_go_e);
		p.add(b_satview_go_s);
		p.add(b_satview_go_w);
		p.add(b_zi);
		p.add(b_zo);

		satellite_view_panel.add(p, BorderLayout.SOUTH);
	}

	/**
	 * This method is called by the Explorer class when the others components of
	 * are ready (SynsetInfoPanel for instance)
	 * @param picking_plugin
	 */
	public void addPickingPlugin(
			edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin picking_plugin) {
		gm.add(picking_plugin);
	}

	public void refreshViews() {
		main_panel.repaint();
		satellite_view_panel.repaint();
	}

	public void centerViewsOnVertex(SynsetVertex v) {
		//the following location have sense in the space of the layout
		Point2D v_location = layout.transform(v);
		Point2D vv1_center_location = vv1.getRenderContext()
				.getMultiLayerTransformer()
				.inverseTransform(Layer.LAYOUT, vv1.getCenter());

		vv1.getRenderContext()
				.getMultiLayerTransformer()
				.getTransformer(Layer.LAYOUT)
				.translate(-(v_location.getX() - vv1_center_location.getX()),
						-(v_location.getY() - vv1_center_location.getY()));

		refreshViews();
	}

	public void selectNode(SynsetVertex v) {
		//retrieve an ISynset from the SynsetVertex
		ISynset synset = Explorer.wn_jwi_dictionary.getSynset(WNUtils
				.getISynsetIDFromString(v.getSynset_id()));

		//set the new content of the SynsetInfoPanel.
		explorer.getSynset_info_panel().updateContent(synset);

		/*
		 * set the synset_id in the synset_info_panel.
		 * (this field is used for not displaying several times the same
		 * synset_info_panel because of several clicks over the same node.
		 */
		explorer.getSynset_info_panel().setSynset_id(v.getSynset_id());

		/*
		 * put the scrollbars value of the scrollpane that contained the JeditorPane
		 * to 0. 
		 * Because of the JEditorPane, this has to be done in a separate thread.
		 */
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				explorer.getSynset_info_panel().getScrollpane()
						.getVerticalScrollBar().setValue(0);
				explorer.getSynset_info_panel().getScrollpane()
						.getHorizontalScrollBar().setValue(0);
			}
		});

		/*
		 * update the last_clicked_vertex field and repaint the graph
		 */
		setLast_clicked_vertex(v);

		//refresh the views
		refreshViews();
	}

	public void developNode(SynsetVertex v) {
		//we lock the vertices before develop the pointed node
		Iterator<SynsetVertex> vertex_iter = explorer.getWngraph().getG()
				.getVertices().iterator();
		while (vertex_iter.hasNext()) {
			layout.lock(vertex_iter.next(), true);
		}

		//augment the graph
		explorer.getWngraph().augmentGraphWithNodeNeighborsRing(v);

		//re-initialize the layout
		layout.initialize();

		//unlock the vertices
		vertex_iter = explorer.getWngraph().getG().getVertices().iterator();
		while (vertex_iter.hasNext()) {
			layout.lock(vertex_iter.next(), false);
		}

		//refresh the views
		refreshViews();
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
			layout = new FRLayout2<SynsetVertex, PointerEdge>(explorer
					.getWngraph().getG());
			vv1.setGraphLayout(layout);
		}

		else

		if (action_command.equals(ACTION_COMMAND_HELP)) {
			help_dialog.setVisible(true);
		}

		else

		if (action_command.equals(ACTION_COMMAND_SATVIEW_GO_N)) {
			vv2.getRenderContext().getMultiLayerTransformer()
					.getTransformer(Layer.LAYOUT).translate(0, 10);
		}

		else

		if (action_command.equals(ACTION_COMMAND_SATVIEW_GO_E)) {
			vv2.getRenderContext().getMultiLayerTransformer()
					.getTransformer(Layer.LAYOUT).translate(-10, 0);
		}

		else

		if (action_command.equals(ACTION_COMMAND_SATVIEW_GO_S)) {
			vv2.getRenderContext().getMultiLayerTransformer()
					.getTransformer(Layer.LAYOUT).translate(0, -10);
		}

		else

		if (action_command.equals(ACTION_COMMAND_SATVIEW_GO_W)) {
			vv2.getRenderContext().getMultiLayerTransformer()
					.getTransformer(Layer.LAYOUT).translate(10, 0);
		}

		else

		if (action_command.equals(ACTION_COMMAND_SATVIEW_ZOOM_IN)) {
			vv2.getRenderContext().getMultiLayerTransformer()
					.getTransformer(Layer.LAYOUT)
					.scale(1.5, 1.5, vv2.getCenter());
		}

		else

		if (action_command.equals(ACTION_COMMAND_SATVIEW_ZOOM_OUT)) {
			vv2.getRenderContext().getMultiLayerTransformer()
					.getTransformer(Layer.LAYOUT)
					.scale(.5, .5, vv2.getCenter());
		}
	}

	/***************************************************************************
	 * getters and setters.
	 **************************************************************************/

	public JPanel getMainPanel() {
		return main_panel;
	}

	public JPanel getSatellite_view_panel() {
		return satellite_view_panel;
	}

	public VisualizationViewer<SynsetVertex, PointerEdge> getVisualizationViewer() {
		return vv1;
	}

	public SatelliteVisualizationViewer<SynsetVertex, PointerEdge> getSatelliteVisualizationViewer() {
		return vv2;
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

}
