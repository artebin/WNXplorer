package net.trevize.knetvis;

import java.util.List;
import java.util.Map;

public abstract class KNetConcept {

	public abstract String getKey();

	public abstract String getShortLabel();

	public abstract String getLabel();

	public abstract String getTooltipText();

	public abstract String getShortDescription();

	public abstract String getDescription();

	public abstract String getFullDescription();

	public abstract Map<KNetSemanticRelation, List<KNetConcept>> getRelatedConcepts();

	public abstract List<KNetConcept> getRelatedConcepts(
			KNetSemanticRelation semantic_relation);

}
