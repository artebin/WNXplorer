package net.trevize.knetvis;

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

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import net.trevize.knetvis.controler.KNetPickingGraphMousePlugin;
import net.trevize.knetvis.controler.KNetPopupGraphMousePlugin;
import net.trevize.knetvis.controler.KNetSatelliteVisualizationViewerMouseControler;
import net.trevize.knetvis.controler.KNetVisualizationViewerMouseControler;
import net.trevize.knetvis.renderer.KNetEdgeDrawPaintTransformer;
import net.trevize.knetvis.renderer.KNetEdgeLabelTranformer;
import net.trevize.knetvis.renderer.KNetVertexLabelTransformer;
import net.trevize.knetvis.renderer.KNetVertexShapeSizeAspectTransformer;
import net.trevize.knetvis.renderer.KNetVertexStrokeHighlight;
import net.trevize.knetvis.renderer.KNetVertexTooltip;
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

public class KNetGraphViewerImplementation implements KNetGraphViewer,
		MouseListener, KeyListener, ActionListener {

	public static final String ACTION_COMMAND_MODE = "ACTION_COMMAND_MODE";
	public static final String ACTION_COMMAND_RESTART_LAYOUT = "ACTION_COMMAND_RESTART_LAYOUT";

	private KNetGraph knetgraph;

	private VisualizationViewer<KNetVertex, KNetEdge> vv1;
	private SatelliteVisualizationViewer<KNetVertex, KNetEdge> vv2;
	private Layout<KNetVertex, KNetEdge> layout;

	private GraphZoomScrollPane scrollpane;
	private DefaultModalGraphMouse<KNetVertex, KNetEdge> gm;

	private KNetVertex selected_vertex;

	private JPanel graph_view_panel;
	private StatusBar status_bar;
	private JCheckBox picking_mode_checkbox;
	private JButton restart_layout_button;
	private KNetPopupSemanticRelationButton semantic_relation_selector_button;

	private JPanel satellite_view_panel;

	private KNetConceptDescriptionPanel concept_description_panel;

	private KNetGraphInfoPanel graph_info_panel;

	public KNetGraphViewerImplementation(KNetGraph knetgraph) {
		this.knetgraph = knetgraph;
		init();
	}

	private void init() {
		graph_view_panel = new JPanel();
		graph_view_panel.setLayout(new BorderLayout());

		//setting the panel BorderLayout.CENTER
		layout = new FRLayout2<KNetVertex, KNetEdge>(
				knetgraph.getFilteredGraph());

		//create the two views
		vv1 = new VisualizationViewer<KNetVertex, KNetEdge>(layout);
		vv2 = new SatelliteVisualizationViewer<KNetVertex, KNetEdge>(vv1);

		//initialize the views
		initGraphView();
		initSatelliteView();

		//initialize the popup panel for graph nodes
		gm = new DefaultModalGraphMouse<KNetVertex, KNetEdge>();
		gm.add(new KNetPopupGraphMousePlugin(knetgraph, this));
		vv1.setGraphMouse(gm);

		KNetPickingGraphMousePlugin picking_plugin = new KNetPickingGraphMousePlugin(
				this);
		gm.add(picking_plugin);

		//add the graph view in a jscrollpane and this jscrollpane in the view panel
		scrollpane = new GraphZoomScrollPane(vv1);
		graph_view_panel.add(scrollpane, BorderLayout.CENTER);

		//initialize the status bar
		status_bar = new StatusBar();
		status_bar.setBorder(new EmptyBorder(4, 4, 4, 4));

		picking_mode_checkbox = new JCheckBox("picking mode");
		picking_mode_checkbox.setActionCommand(ACTION_COMMAND_MODE);
		picking_mode_checkbox.addActionListener(this);
		status_bar.addComponent("mode", picking_mode_checkbox);

		restart_layout_button = new JButton("Restart layout");
		restart_layout_button.setActionCommand(ACTION_COMMAND_RESTART_LAYOUT);
		restart_layout_button.addActionListener(this);
		status_bar.addComponent("auto layout", restart_layout_button);

		semantic_relation_selector_button = new KNetPopupSemanticRelationButton(
				knetgraph, this);
		status_bar.addComponent("PopupPointerButton",
				semantic_relation_selector_button);

		//add the status bar to the main panel
		graph_view_panel.add(status_bar, BorderLayout.SOUTH);

		concept_description_panel = new KNetConceptDescriptionPanel(this);

		graph_info_panel = new KNetGraphInfoPanel(this);
	}

	private void initGraphView() {
		//initialize the SatelliteVisualizationViewer
		KNetVisualizationViewerMouseControler vv_mouse_pan = new KNetVisualizationViewerMouseControler(
				vv1);
		vv1.addMouseListener(vv_mouse_pan);
		vv1.addMouseMotionListener(vv_mouse_pan);

		//setting the PickedVertexPaintTransformer.
		PickableVertexPaintTransformer<KNetVertex> vpt = new PickableVertexPaintTransformer<KNetVertex>(
				vv1.getPickedVertexState(), Color.WHITE, Color.YELLOW);
		vv1.getRenderContext().setVertexFillPaintTransformer(vpt);

		//setting the PickedEdgePaintTransformer.
		PickableEdgePaintTransformer<KNetEdge> vet = new PickableEdgePaintTransformer<KNetEdge>(
				vv1.getPickedEdgeState(), Color.GRAY, Color.YELLOW);
		vv1.getRenderContext().setEdgeDrawPaintTransformer(vet);
		vv1.getRenderContext().setArrowDrawPaintTransformer(vet);

		//setting the EdgeShapeTransformer.
		vv1.getRenderContext().setEdgeShapeTransformer(
				new EdgeShape.Line<KNetVertex, KNetEdge>());

		vv1.getRenderContext().setEdgeDrawPaintTransformer(
				new KNetEdgeDrawPaintTransformer());

		//setting the Arrow transformer.
		//vv.getRenderContext().setArrowFillPaintTransformer(new PointerEdgeDrawPaintTransformer());
		//vv.getRenderContext().setArrowDrawPaintTransformer(new PointerEdgeDrawPaintTransformer());
		vv1.getRenderer()
				.getEdgeRenderer()
				.setEdgeArrowRenderingSupport(
						new BasicEdgeArrowRenderingSupport());

		//setting the VertexStrokeTransformer.
		vv1.getRenderContext().setVertexStrokeTransformer(
				new KNetVertexStrokeHighlight<KNetVertex, KNetEdge>(this));

		//setting the label renderer.
		/*
		SynsetVertexLabelRenderer<SynsetVertex> vertex_label_renderer = new SynsetVertexLabelRenderer<SynsetVertex>(
				Color.YELLOW);
		vertex_label_renderer.setBackgroundColor(Color.BLACK);
		vertex_label_renderer.setForegroundColor(Color.WHITE);
		vv.getRenderContext().setVertexLabelRenderer(vertex_label_renderer);
		 */

		vv1.getRenderContext().setVertexLabelTransformer(
				new KNetVertexLabelTransformer());
		vv1.getRenderer().getVertexLabelRenderer()
				.setPosition(Renderer.VertexLabel.Position.E);

		vv1.getRenderContext().setEdgeLabelTransformer(
				new KNetEdgeLabelTranformer());

		vv1.setVertexToolTipTransformer(new KNetVertexTooltip<String>());

		vv1.setBackground(Color.WHITE);
		vv1.addMouseListener(this);
		vv1.addKeyListener(this);
	}

	private void initSatelliteView() {
		satellite_view_panel = new JPanel();
		satellite_view_panel.setLayout(new BorderLayout());

		//initialize the SatelliteVisualizationViewer
		KNetSatelliteVisualizationViewerMouseControler svv_mouse_pan = new KNetSatelliteVisualizationViewerMouseControler(
				vv2);
		vv2.addMouseListener(svv_mouse_pan);
		vv2.addMouseMotionListener(svv_mouse_pan);
		vv2.addMouseWheelListener(svv_mouse_pan);

		vv2.getRenderContext().setVertexShapeTransformer(
				new KNetVertexShapeSizeAspectTransformer<KNetVertex, KNetEdge>(
						knetgraph.getFilteredGraph()));

		PickableVertexPaintTransformer<KNetVertex> vpt = new PickableVertexPaintTransformer<KNetVertex>(
				vv1.getPickedVertexState(), Color.WHITE, Color.YELLOW);
		vv2.getRenderContext().setVertexFillPaintTransformer(vpt);

		vv2.getRenderContext().setEdgeShapeTransformer(
				new EdgeShape.Line<KNetVertex, KNetEdge>());

		vv2.getRenderContext().setVertexStrokeTransformer(
				new KNetVertexStrokeHighlight<KNetVertex, KNetEdge>(this));

		ScalingControl vv2Scaler = new CrossoverScalingControl();
		vv2.scaleToLayout(vv2Scaler);

		satellite_view_panel.add(vv2, BorderLayout.CENTER);
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
			layout = new FRLayout2<KNetVertex, KNetEdge>(
					knetgraph.getFilteredGraph());
			vv1.setGraphLayout(layout);
			refreshViews();
		}
	}

	/***************************************************************************
	 * getters and setters.
	 **************************************************************************/

	@Override
	public KNetGraph getKNetGraph() {
		return knetgraph;
	}

	@Override
	public JPanel getGraphViewPanel() {
		return graph_view_panel;
	}

	@Override
	public JPanel getSatelliteViewPanel() {
		return satellite_view_panel;
	}

	@Override
	public VisualizationViewer<KNetVertex, KNetEdge> getVisualizationViewer() {
		return vv1;
	}

	@Override
	public SatelliteVisualizationViewer<KNetVertex, KNetEdge> getSatelliteVisualizationViewer() {
		return vv2;
	}

	@Override
	public Layout<KNetVertex, KNetEdge> getLayout() {
		return layout;
	}

	@Override
	public KNetPopupSemanticRelationButton getPopupSemanticRelationButton() {
		return semantic_relation_selector_button;
	}

	@Override
	public KNetConceptDescriptionPanel getConceptDescriptionPanel() {
		return concept_description_panel;
	}

	@Override
	public KNetGraphInfoPanel getGraphInfoPanel() {
		return graph_info_panel;
	}

	@Override
	public void fireGraphStructureChanged() {
		if (graph_info_panel != null) {
			graph_info_panel.updateContent();
		}
	}

	@Override
	public void refreshViews() {
		graph_view_panel.repaint();
		satellite_view_panel.repaint();
	}

	@Override
	public void centerViewsOnConcept(KNetConcept concept) {
		KNetVertex vertex = knetgraph.getVerticesIndex().get(concept.getKey());
		centerViewsOnVertex(vertex);
	}

	@Override
	public void centerViewsOnVertex(KNetVertex vertex) {
		//the following location have sense in the space of the layout
		Point2D v_location = layout.transform(vertex);
		Point2D vv1_center_location = vv1.getRenderContext()
				.getMultiLayerTransformer()
				.inverseTransform(Layer.LAYOUT, vv1.getCenter());

		double scale = vv1.getRenderContext().getMultiLayerTransformer()
				.getTransformer(Layer.VIEW).getScale();

		vv1.getRenderContext()
				.getMultiLayerTransformer()
				.getTransformer(Layer.LAYOUT)
				.translate(
						-(v_location.getX() - vv1_center_location.getX()) * 1
								/ scale,
						-(v_location.getY() - vv1_center_location.getY()) * 1
								/ scale);

		refreshViews();
	}

	@Override
	public void selectConcept(KNetConcept concept) {
		KNetVertex vertex = knetgraph.getVerticesIndex().get(concept.getKey());
		selectVertex(vertex);
	}

	@Override
	public void selectVertex(KNetVertex vertex) {
		//retrieve the concept from the KNetVertex
		KNetConcept knetconcept = vertex.getConcept();

		//set the new content of the KNetConceptDescriptionPanel
		concept_description_panel.updateContent(knetconcept);

		/*
		 * put the scrollbars value of the scrollpane that contained the JeditorPane
		 * to 0. 
		 * Because of the JEditorPane, this has to be done in a separate thread.
		 */
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				concept_description_panel.getScrollpane()
						.getVerticalScrollBar().setValue(0);
				concept_description_panel.getScrollpane()
						.getHorizontalScrollBar().setValue(0);
			}
		});

		/*
		 * update the last_clicked_vertex field and repaint the graph
		 */
		selected_vertex = vertex;

		//refresh the views
		refreshViews();
	}

	@Override
	public void developConcept(KNetConcept concept) {
		KNetVertex vertex = knetgraph.getVerticesIndex().get(concept.getKey());
		developVertex(vertex);
	}

	@Override
	public void developVertex(KNetVertex vertex) {
		//we lock the vertices before develop the pointed node
		Iterator<KNetVertex> vertex_iter = knetgraph.getFilteredGraph()
				.getVertices().iterator();
		while (vertex_iter.hasNext()) {
			layout.lock(vertex_iter.next(), true);
		}

		//augment the graph
		knetgraph.augmentGraphWithNeighborRing(vertex);

		//re-initialize the layout
		layout.initialize();

		//unlock the vertices
		vertex_iter = knetgraph.getFilteredGraph().getVertices().iterator();
		while (vertex_iter.hasNext()) {
			layout.lock(vertex_iter.next(), false);
		}

		fireGraphStructureChanged();

		//refresh the views
		refreshViews();
	}

	@Override
	public KNetVertex getSelectedvertex() {
		return selected_vertex;
	}

	@Override
	public StatusBar getStatusBar() {
		return status_bar;
	}

}
