package net.trevize.knetvis;

import javax.swing.JPanel;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.SatelliteVisualizationViewer;

public interface KNetGraphViewer {

	public KNetGraph getKNetGraph();

	public void fireGraphStructureChanged();

	public void refreshViews();

	public void centerViewsOnConcept(KNetConcept concept);

	public void centerViewsOnVertex(KNetVertex vertex);

	public void selectConcept(KNetConcept concept);

	public void selectVertex(KNetVertex vertex);

	public KNetVertex getSelectedvertex();

	public void developConcept(KNetConcept concept);

	public void developVertex(KNetVertex vertex);

	public JPanel getGraphViewPanel();

	public VisualizationViewer<KNetVertex, KNetEdge> getVisualizationViewer();

	public SatelliteVisualizationViewer<KNetVertex, KNetEdge> getSatelliteVisualizationViewer();

	public Layout<KNetVertex, KNetEdge> getLayout();

	public KNetPopupSemanticRelationButton getPopupSemanticRelationButton();

	public StatusBar getStatusBar();

	public JPanel getSatelliteViewPanel();

	public KNetConceptDescriptionPanel getConceptDescriptionPanel();

	public KNetGraphInfoPanel getGraphInfoPanel();

}
