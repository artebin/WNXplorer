package net.trevize.knetvis.controler;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;

import net.trevize.knetvis.KNetEdge;
import net.trevize.knetvis.KNetVertex;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.control.SatelliteVisualizationViewer;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * SatelliteVisualizationViewerMousePan.java - Apr 26, 2011
 */

public class KNetSatelliteVisualizationViewerMouseControler implements
		MouseListener, MouseMotionListener, MouseWheelListener {

	private SatelliteVisualizationViewer<KNetVertex, KNetEdge> vv;
	private Point drag_point;
	private double zoom_factor = 0.1;

	public KNetSatelliteVisualizationViewerMouseControler(
			SatelliteVisualizationViewer<KNetVertex, KNetEdge> vv) {
		this.vv = vv;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (drag_point != null) {
			Point2D l_drag_point = vv.getRenderContext()
					.getMultiLayerTransformer()
					.inverseTransform(Layer.LAYOUT, drag_point);

			Point2D l_e_point = vv.getRenderContext()
					.getMultiLayerTransformer()
					.inverseTransform(Layer.LAYOUT, e.getPoint());

			double delta_x = l_e_point.getX() - l_drag_point.getX();
			double delta_y = l_e_point.getY() - l_drag_point.getY();

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
