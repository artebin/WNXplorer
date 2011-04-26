package net.trevize.wnxplorer.explorer;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import net.trevize.wnxplorer.jung.PointerEdge;
import net.trevize.wnxplorer.jung.SynsetVertex;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.control.SatelliteVisualizationViewer;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * SatelliteVisualizationViewerMousePan.java - Apr 26, 2011
 */

public class SatelliteVisualizationViewerMouseControler implements
		MouseListener, MouseMotionListener, MouseWheelListener {

	private SatelliteVisualizationViewer<SynsetVertex, PointerEdge> vv;
	private Point drag_point;
	private double zoom_factor = 0.1;

	public SatelliteVisualizationViewerMouseControler(
			SatelliteVisualizationViewer<SynsetVertex, PointerEdge> vv) {
		this.vv = vv;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (drag_point != null) {
			double delta_x = e.getPoint().getX() - drag_point.getX();
			double delta_y = e.getPoint().getY() - drag_point.getY();

			double scale = vv.getRenderContext().getMultiLayerTransformer()
					.getTransformer(Layer.VIEW).getScale();

			vv.getRenderContext().getMultiLayerTransformer()
					.getTransformer(Layer.LAYOUT)
					.translate(delta_x * (1 / scale), delta_y * (1 / scale));

			drag_point = e.getPoint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

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
		if (e.getButton() == MouseEvent.BUTTON2) {
			drag_point = e.getPoint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON2) {
			drag_point = null;
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.getModifiersEx() == MouseEvent.CTRL_DOWN_MASK) {
			vv.getRenderContext()
					.getMultiLayerTransformer()
					.getTransformer(Layer.LAYOUT)
					.scale(1 + (e.getWheelRotation() * zoom_factor),
							1 + (e.getWheelRotation() * zoom_factor),
							vv.getCenter());
		}
	}
}
