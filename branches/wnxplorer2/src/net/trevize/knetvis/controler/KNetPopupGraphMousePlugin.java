package net.trevize.knetvis.controler;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;

import net.trevize.knetvis.KNetEdge;
import net.trevize.knetvis.KNetGraph;
import net.trevize.knetvis.KNetGraphViewer;
import net.trevize.knetvis.KNetVertex;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractPopupGraphMousePlugin;

/**
 * A GraphMousePlugin that offers popup menu support.
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * PopupGraphMousePlugin.java - Mar 23, 2010
 */

public class KNetPopupGraphMousePlugin extends AbstractPopupGraphMousePlugin
		implements MouseListener {

	private KNetGraph knetgraph;
	private KNetGraphViewer knetgraph_viewer;

	public KNetPopupGraphMousePlugin(KNetGraph knetgraph,
			KNetGraphViewer knetgraph_viewer) {
		this(MouseEvent.BUTTON3_MASK);
		this.knetgraph = knetgraph;
		this.knetgraph_viewer = knetgraph_viewer;
	}

	public KNetPopupGraphMousePlugin(int modifiers) {
		super(modifiers);
	}

	protected void handlePopup(MouseEvent e) {
		final VisualizationViewer<KNetVertex, KNetEdge> vv = (VisualizationViewer<KNetVertex, KNetEdge>) e
				.getSource();
		Point2D p = e.getPoint();

		GraphElementAccessor<KNetVertex, KNetEdge> pickSupport = vv
				.getPickSupport();

		if (pickSupport != null) {
			final KNetVertex v = pickSupport.getVertex(
					knetgraph_viewer.getLayout(), p.getX(), p.getY());

			if (v != null) {
				//select the node


				JPopupMenu popup = new JPopupMenu();

				popup.add(new AbstractAction("Develop") {
					public void actionPerformed(ActionEvent e) {
						knetgraph_viewer.developVertex(v);
					}
				});

				popup.add(new AbstractAction("Select 1-neighborhood ring") {
					public void actionPerformed(ActionEvent e) {
						Iterator<KNetVertex> vertex_iter = knetgraph
								.getFilteredGraph().getNeighbors(v).iterator();
						while (vertex_iter.hasNext()) {
							knetgraph_viewer.getVisualizationViewer()
									.getPickedVertexState()
									.pick(vertex_iter.next(), true);
						}
					}
				});

				popup.show(vv, e.getX(), e.getY());
			}

			/*
			 * the following is for contextual popup menu of edges
			 */

			/*
			else {
				final PointerEdge edge = pickSupport.getEdge(
						wngraphp.getLayout(), p.getX(), p.getY());
				if (edge != null) {
					JPopupMenu popup = new JPopupMenu();
					popup.add(new AbstractAction(edge.toString()) {
						public void actionPerformed(ActionEvent e) {
							System.err.println("got " + edge);
						}
					});
					popup.show(vv, e.getX(), e.getY());
				}
			}
			*/

		}
	}

}
