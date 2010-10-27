package net.trevize.wnxplorer.jung;

import edu.mit.jwi.item.Pointer;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * PointerEdge.java - Mar 25, 2010
 */

public class PointerEdge {

	private Pointer pointer_type;

	public PointerEdge(Pointer pointer_type) {
		this.pointer_type = pointer_type;
	}

	public Pointer getPointer_type() {
		return pointer_type;
	}

	public void setPointer_type(Pointer pointerType) {
		pointer_type = pointerType;
	}

}
