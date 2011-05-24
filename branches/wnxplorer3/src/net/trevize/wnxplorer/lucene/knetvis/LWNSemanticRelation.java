package net.trevize.wnxplorer.lucene.knetvis;

import java.awt.Color;

import net.trevize.knetvis.KNetSemanticRelation;

public class LWNSemanticRelation extends KNetSemanticRelation {

	private String key;
	private String short_label;
	private String label;
	private String tooltip_text;
	private String short_description;
	private String description;
	private String full_description;
	private Color color;
	private LWNSemanticRelation opposite;

	public String getShort_label() {
		return short_label;
	}

	public void setShort_label(String short_label) {
		this.short_label = short_label;
	}

	public String getTooltip_text() {
		return tooltip_text;
	}

	public void setTooltip_text(String tooltip_text) {
		this.tooltip_text = tooltip_text;
	}

	public String getShort_description() {
		return short_description;
	}

	public void setShort_description(String short_description) {
		this.short_description = short_description;
	}

	public String getFull_description() {
		return full_description;
	}

	public void setFull_description(String full_description) {
		this.full_description = full_description;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void setOpposite(LWNSemanticRelation opposite) {
		this.opposite = opposite;
	}

	/***************************************************************************
	 * implementation of KNetSemanticRelation
	 **************************************************************************/

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public String getShortLabel() {
		return short_label;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public String getTooltipText() {
		return tooltip_text;
	}

	@Override
	public String getShortDescription() {
		return short_description;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getFullDescription() {
		return full_description;
	}

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public KNetSemanticRelation getOpposite() {
		return opposite;
	}

	@Override
	public boolean isSymmetric() {
		return opposite == this;
	}

}
