package net.trevize.wnxplorer.explorer;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;

import net.trevize.wnxplorer.jung.PointerEdge;
import net.trevize.wnxplorer.jung.SynsetVertex;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.control.SatelliteVisualizationViewer;

public class SatelliteVisualizationViewerMousePan implements MouseListener,
		MouseMotionListener {

	private SatelliteVisualizationViewer<SynsetVertex, PointerEdge> vv;
	private Point drag_point;

	public SatelliteVisualizationViewerMousePan(
			SatelliteVisualizationViewer<SynsetVertex, PointerEdge> vv) {
		this.vv = vv;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (drag_point != null) {
			Point2D p0 = vv.getRenderContext().getMultiLayerTransformer()
					.transform(Layer.LAYOUT, drag_point);
			Point2D p1 = vv.getRenderContext().getMultiLayerTransformer()
					.transform(Layer.LAYOUT, e.getPoint());

			double delta_x = p1.getX() - p0.getX();
			double delta_y = p1.getY() - p0.getY();
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

}
