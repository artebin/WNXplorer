package net.trevize.wnxplorer.jung;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;

import net.trevize.wnxplorer.explorer.Explorer;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.visualization.VisualizationViewer;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * PickingGraphMousePlugin.java - Mar 30, 2010
 */

public class PickingGraphMousePlugin
		extends
		edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin<SynsetVertex, PointerEdge>
		implements MouseListener {

	private Explorer explorer;

	public PickingGraphMousePlugin(Explorer explorer) {
		this.explorer = explorer;
	}

	/***************************************************************************
	 * implementation of MouseListener.
	 **************************************************************************/

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			//get the VisualizationViewer.
			VisualizationViewer<SynsetVertex, PointerEdge> vv = (VisualizationViewer<SynsetVertex, PointerEdge>) e
					.getSource();
			Point2D p = e.getPoint();

			GraphElementAccessor<SynsetVertex, PointerEdge> pickSupport = vv
					.getPickSupport();

			if (pickSupport != null) {
				SynsetVertex v = pickSupport.getVertex(explorer
						.getWngraph_panel().getLayout(), p.getX(), p.getY());
				if (e.getButton() == MouseEvent.BUTTON1 && v != null) {
					explorer.getWngraph_panel().developNode(v);
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
		VisualizationViewer<SynsetVertex, PointerEdge> vv = (VisualizationViewer<SynsetVertex, PointerEdge>) e
				.getSource();
		Point2D p = e.getPoint();

		GraphElementAccessor<SynsetVertex, PointerEdge> pickSupport = vv
				.getPickSupport();

		if (pickSupport != null) {
			SynsetVertex v = pickSupport.getVertex(explorer.getWngraph_panel()
					.getLayout(), p.getX(), p.getY());

			if (e.getButton() == MouseEvent.BUTTON1
					&& v != null
					&& !v.getSynset_id().equals(
							explorer.getSynset_info_panel().getSynset_id())) {
				explorer.getWngraph_panel().selectNode(v);
			}//end vertex not null and vertex is not the last_clicked_vertex.
		}//end pickSupport is not null.
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

}
