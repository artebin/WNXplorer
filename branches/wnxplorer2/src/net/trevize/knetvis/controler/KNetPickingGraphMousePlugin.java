package net.trevize.knetvis.controler;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;

import net.trevize.knetvis.KNetEdge;
import net.trevize.knetvis.KNetGraphViewer;
import net.trevize.knetvis.KNetVertex;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.visualization.VisualizationViewer;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * PickingGraphMousePlugin.java - Mar 30, 2010
 */

public class KNetPickingGraphMousePlugin
		extends
		edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin<KNetVertex, KNetEdge>
		implements MouseListener {

	private KNetGraphViewer knetgraph_viewer;

	public KNetPickingGraphMousePlugin(KNetGraphViewer knetgraph_viewer) {
		this.knetgraph_viewer = knetgraph_viewer;
	}

	/***************************************************************************
	 * implementation of MouseListener.
	 **************************************************************************/

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			//get the VisualizationViewer.
			VisualizationViewer<KNetVertex, KNetEdge> vv = (VisualizationViewer<KNetVertex, KNetEdge>) e
					.getSource();
			Point2D p = e.getPoint();

			GraphElementAccessor<KNetVertex, KNetEdge> pickSupport = vv
					.getPickSupport();

			if (pickSupport != null) {
				KNetVertex v = pickSupport.getVertex(
						knetgraph_viewer.getLayout(), p.getX(), p.getY());
				if (e.getButton() == MouseEvent.BUTTON1 && v != null) {
					knetgraph_viewer.developVertex(v);
				}
			}//end pickSupport is not null.
		}//end it is a double click.
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		//get the VisualizationViewer.
		VisualizationViewer<KNetVertex, KNetEdge> vv = (VisualizationViewer<KNetVertex, KNetEdge>) e
				.getSource();
		Point2D p = e.getPoint();

		GraphElementAccessor<KNetVertex, KNetEdge> pickSupport = vv
				.getPickSupport();

		if (pickSupport != null) {
			KNetVertex vertex = pickSupport.getVertex(
					knetgraph_viewer.getLayout(), p.getX(), p.getY());

			if (e.getButton() == MouseEvent.BUTTON1
					&& vertex != null
					&& vertex.getConcept() != knetgraph_viewer
							.getConceptDescriptionPanel()
							.getDescribed_knetconcept()) {
				knetgraph_viewer.selectVertex(vertex);
			}//end vertex not null and vertex is not the last_clicked_vertex.
		}//end pickSupport is not null.
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

}
