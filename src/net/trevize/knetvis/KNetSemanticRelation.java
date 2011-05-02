package net.trevize.knetvis;

import java.awt.Color;

public abstract class KNetSemanticRelation {

	public abstract String getKey();

	public abstract String getShortLabel();

	public abstract String getLabel();

	public abstract String getTooltipText();

	public abstract String getShortDescription();

	public abstract String getDescription();

	public abstract String getFullDescription();

	public abstract Color getColor();

	public abstract KNetSemanticRelation getOpposite();

}
