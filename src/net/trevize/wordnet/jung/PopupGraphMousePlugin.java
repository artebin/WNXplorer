package net.trevize.wordnet.jung;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;

import net.trevize.wordnet.explorer.WNGraph;
import net.trevize.wordnet.explorer.WNGraphPanel;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractPopupGraphMousePlugin;

/**
 * A GraphMousePlugin that offers popup menu support.
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * PopupGraphMousePlugin.java - Mar 23, 2010
 */

public class PopupGraphMousePlugin extends AbstractPopupGraphMousePlugin
		implements MouseListener {

	private WNGraph wngraph;
	private WNGraphPanel wngraphp;

	public PopupGraphMousePlugin(WNGraph wngraph, WNGraphPanel wngraphp) {
		this(MouseEvent.BUTTON3_MASK);
		this.wngraph = wngraph;
		this.wngraphp = wngraphp;
	}

	public PopupGraphMousePlugin(int modifiers) {
		super(modifiers);
	}

	protected void handlePopup(MouseEvent e) {
		final VisualizationViewer<SynsetVertex, SemanticRelationEdge> vv = (VisualizationViewer<SynsetVertex, SemanticRelationEdge>) e
				.getSource();
		Point2D p = e.getPoint();

		GraphElementAccessor<SynsetVertex, SemanticRelationEdge> pickSupport = vv
				.getPickSupport();
		if (pickSupport != null) {
			final SynsetVertex v = pickSupport.getVertex(wngraphp.getLayout(),
					p.getX(), p.getY());

			if (v != null) {
				JPopupMenu popup = new JPopupMenu();

				popup.add(new AbstractAction("Develop") {
					public void actionPerformed(ActionEvent e) {
						//we lock the vertices before develop the pointed node.
						Iterator<SynsetVertex> vertex_iter = wngraph.getG()
								.getVertices().iterator();
						while (vertex_iter.hasNext()) {
							wngraphp.getLayout().lock(vertex_iter.next(), true);
						}

						//develop the pointed node.
						wngraph.develop(v);

						//re-initialize the layout.
						wngraphp.getLayout().initialize();

						//unlock the vertices.
						vertex_iter = wngraph.getG().getVertices().iterator();
						while (vertex_iter.hasNext()) {
							wngraphp.getLayout()
									.lock(vertex_iter.next(), false);
						}

						//repaint the VisualizationViewer.
						wngraphp.getVv().repaint();
					}
				});

				popup.add(new AbstractAction("Select 1-neighborhood ring") {
					public void actionPerformed(ActionEvent e) {
						Iterator<SynsetVertex> vertex_iter = wngraph.getG()
								.getNeighbors(v).iterator();
						while (vertex_iter.hasNext()) {
							wngraphp.getVv().getPickedVertexState().pick(
									vertex_iter.next(), true);
						}
					}
				});

				popup.show(vv, e.getX(), e.getY());
			}

			else {
				final SemanticRelationEdge edge = pickSupport.getEdge(wngraphp
						.getLayout(), p.getX(), p.getY());
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
		}
	}

}
