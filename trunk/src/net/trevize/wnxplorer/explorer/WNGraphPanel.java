package net.trevize.wnxplorer.explorer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import net.trevize.wnxplorer.jung.PopupGraphMousePlugin;
import net.trevize.wnxplorer.jung.SemanticRelationEdge;
import net.trevize.wnxplorer.jung.SemanticRelationEdgeLabelTranformer;
import net.trevize.wnxplorer.jung.SynsetVertex;
import net.trevize.wnxplorer.jung.SynsetVertexLabelRenderer;
import net.trevize.wnxplorer.jung.SynsetVertexLabelTransformer;
import net.trevize.wnxplorer.jung.SynsetVertexShapeSizeAspect;
import net.trevize.wnxplorer.jung.SynsetVertexTooltip;
import net.trevize.wnxplorer.jung.VertexStrokeHighlight;
import edu.uci.ics.jung.algorithms.layout.FRLayout2;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse.Mode;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.PickableEdgePaintTransformer;
import edu.uci.ics.jung.visualization.decorators.PickableVertexPaintTransformer;
import edu.uci.ics.jung.visualization.renderers.Renderer;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * WNGraphPanel.java - Mar 24, 2010
 */

public class WNGraphPanel implements MouseListener, KeyListener, ActionListener {

	public static final String ACTION_COMMAND_MODE = "ACTION_COMMAND_MODE";
	public static final String ACTION_RESTART_LAYOUT = "ACTION_RESTART_LAYOUT";

	private WNGraph wngraph;

	//for jung.
	private DirectedSparseMultigraph<SynsetVertex, SemanticRelationEdge> g;
	private VisualizationViewer<SynsetVertex, SemanticRelationEdge> vv;
	private Layout<SynsetVertex, SemanticRelationEdge> layout;

	private GraphZoomScrollPane scrollpane;
	private DefaultModalGraphMouse<SynsetVertex, SemanticRelationEdge> gm;

	private SynsetVertex last_clicked_vertex;

	//classical swing components.
	private JPanel p0;
	private JCheckBox cb0;
	private JButton b0;

	public WNGraphPanel(WNGraph wngraph) {
		this.wngraph = wngraph;
		this.g = wngraph.getG();

		init();
	}

	public void init() {
		p0 = new JPanel();
		p0.setLayout(new BorderLayout());

		//setting the panel BorderLayout.CENTER
		layout = new FRLayout2<SynsetVertex, SemanticRelationEdge>(g);
		vv = new VisualizationViewer<SynsetVertex, SemanticRelationEdge>(layout);

		//setting the PickedVertexPaintTransformer.
		PickableVertexPaintTransformer<SynsetVertex> vpt = new PickableVertexPaintTransformer<SynsetVertex>(
				vv.getPickedVertexState(), Color.WHITE, Color.YELLOW);
		vv.getRenderContext().setVertexFillPaintTransformer(vpt);

		//setting the PickedEdgePaintTransformer.
		PickableEdgePaintTransformer<SemanticRelationEdge> vet = new PickableEdgePaintTransformer<SemanticRelationEdge>(
				vv.getPickedEdgeState(), Color.GRAY, Color.YELLOW);
		vv.getRenderContext().setEdgeDrawPaintTransformer(vet);
		vv.getRenderContext().setArrowDrawPaintTransformer(vet);

		//setting the VertexShapeTransformer.
		vv
				.getRenderContext()
				.setVertexShapeTransformer(
						new SynsetVertexShapeSizeAspect<SynsetVertex, SemanticRelationEdge>(
								g));

		//setting the EdgeShapeTransformer.
		vv.getRenderContext().setEdgeShapeTransformer(
				new EdgeShape.Line<SynsetVertex, SemanticRelationEdge>());

		//setting the VertexStrokeTransformer.
		vv.getRenderContext().setVertexStrokeTransformer(
				new VertexStrokeHighlight<SynsetVertex, SemanticRelationEdge>(
						this));

		//setting the label renderer.
		SynsetVertexLabelRenderer<SynsetVertex> vertex_label_renderer = new SynsetVertexLabelRenderer<SynsetVertex>(
				Color.YELLOW);
		vertex_label_renderer.setBackgroundColor(Color.BLACK);
		vertex_label_renderer.setForegroundColor(Color.WHITE);
		vv.getRenderContext().setVertexLabelRenderer(vertex_label_renderer);

		vv.getRenderContext().setVertexLabelTransformer(
				new SynsetVertexLabelTransformer());
		vv.getRenderer().getVertexLabelRenderer().setPosition(
				Renderer.VertexLabel.Position.E);

		vv.getRenderContext().setEdgeLabelTransformer(
				new SemanticRelationEdgeLabelTranformer());

		scrollpane = new GraphZoomScrollPane(vv);
		gm = new DefaultModalGraphMouse<SynsetVertex, SemanticRelationEdge>();
		gm.add(new PopupGraphMousePlugin(wngraph, this));

		vv.setGraphMouse(gm);

		vv.setVertexToolTipTransformer(new SynsetVertexTooltip<String>());

		vv.setBackground(Color.WHITE);
		vv.addMouseListener(this);
		vv.addKeyListener(this);

		p0.add(scrollpane, BorderLayout.CENTER);

		//setting the panel BorderLayout.SOUTH (i.e. the status bar).
		StatusBar status_bar = new StatusBar();

		cb0 = new JCheckBox("picking mode");
		cb0.setActionCommand(ACTION_COMMAND_MODE);
		cb0.addActionListener(this);
		status_bar.addComponent("mode", cb0);

		b0 = new JButton("Restart layout");
		b0.setToolTipText("Reset layout");
		b0.setActionCommand(ACTION_RESTART_LAYOUT);
		b0.addActionListener(this);
		status_bar.addComponent("auto layout", b0);

		status_bar.addComponent("PopupPointerButton", new PopupPointerButton());
		
		p0.add(status_bar, BorderLayout.SOUTH);
	}

	public JPanel getPanel() {
		return p0;
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
			cb0.setSelected(!cb0.isSelected());
			if (gm != null) { //if a GraphZoomScrollPane has ever been instantiated.
				if (cb0.isSelected()) {
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
		if (e.getSource() == vv) {
			vv.requestFocus();
		}
	}

	/***************************************************************************
	 * implementation of ActionListener.
	 **************************************************************************/

	@Override
	public void actionPerformed(ActionEvent e) {
		String action_command = e.getActionCommand();

		if (action_command.equals(ACTION_COMMAND_MODE)) {
			System.out.println("ACTION_COMMAND_MODE");

			if (gm != null) { //if a GraphZoomScrollPane has ever been instantiated.
				if (cb0.isSelected()) {
					gm.setMode(Mode.PICKING);
				} else {
					gm.setMode(Mode.TRANSFORMING);
				}
			}
		}

		else

		if (action_command.equals(ACTION_RESTART_LAYOUT)) {
			System.out.println("ACTION_RESTART_LAYOUT");

			layout = new FRLayout2<SynsetVertex, SemanticRelationEdge>(g);
			vv.setGraphLayout(layout);
		}
	}

	/***************************************************************************
	 * getters and setters.
	 **************************************************************************/

	public VisualizationViewer<SynsetVertex, SemanticRelationEdge> getVv() {
		return vv;
	}

	public void setVv(VisualizationViewer<SynsetVertex, SemanticRelationEdge> vv) {
		this.vv = vv;
	}

	public Layout<SynsetVertex, SemanticRelationEdge> getLayout() {
		return layout;
	}

	public void setLayout(Layout<SynsetVertex, SemanticRelationEdge> layout) {
		this.layout = layout;
	}

	public SynsetVertex getLast_clicked_vertex() {
		return last_clicked_vertex;
	}

	public void setLast_clicked_vertex(SynsetVertex lastClickedVertex) {
		last_clicked_vertex = lastClickedVertex;
	}

	public DefaultModalGraphMouse<SynsetVertex, SemanticRelationEdge> getGm() {
		return gm;
	}

	public void setGm(
			DefaultModalGraphMouse<SynsetVertex, SemanticRelationEdge> gm) {
		this.gm = gm;
	}

}
