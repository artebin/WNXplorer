package net.trevize.wnxplorer.knetvis;

import java.awt.Color;

import net.trevize.knetvis.KNetSemanticRelation;
import net.trevize.wnxplorer.WNUtils;
import edu.mit.jwi.item.Pointer;

public class WNSemanticRelation extends KNetSemanticRelation {

	private Pointer pointer;

	private String key;
	private String short_label;
	private String label;
	private String tooltip_text;
	private String short_description;
	private String description;
	private String full_description;
	private Color color;
	private WNSemanticRelation opposite;

	public WNSemanticRelation(Pointer pointer) {
		this.pointer = pointer;

		key = pointer.getName();
		short_label = pointer.getName();
		label = pointer.getName();
		tooltip_text = pointer.getName();
		short_description = pointer.getName();
		description = pointer.getName();
		full_description = pointer.getName();
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void setOpposite(WNSemanticRelation semrel) {
		this.opposite = semrel;
	}

	public Pointer getPointer() {
		return pointer;
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
		return WNUtils.isSymmetric(pointer);
	}

}
