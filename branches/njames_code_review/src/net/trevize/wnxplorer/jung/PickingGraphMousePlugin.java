package net.trevize.wnxplorer.jung;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;

import javax.swing.SwingUtilities;

import net.trevize.wnxplorer.explorer.Explorer;
import net.trevize.wnxplorer.jwi.WNUtils;
import edu.mit.jwi.item.ISynset;
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
				explorer.getWngraph_panel().developNode(v);
			}
		}
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

				//retrieve an ISynset from the SynsetVertex.
				ISynset synset = Explorer.wn_jwi_dictionary.getSynset(WNUtils
						.getISynsetIDFromString(v.getSynset_id()));

				//set the new content of the synset_info_panel.
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
				explorer.getWngraph_panel().setLast_clicked_vertex(v);
				
				//refresh the views
				explorer.refreshViews();
			}//end vertex not null and vertex is not the last_clicked_vertex.
		}//end pickSupport is not null.
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

}
