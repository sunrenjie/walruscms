package lt.walrus.model;

import java.io.Serializable;

/**
 * Box represents a static area in layout that is hardcoded in template. All
 * boxes that are referenced in tempates must be created beforehand in XML site
 * definition before the first site initialization. Content in a box might be
 * fully editable, but currently there is no way to add or remove boxes
 * dynamically.
 */
public class Box implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Unique Id of the box
	 */
	private long id;
	/**
	 * Id by which box is referenced in template
	 */
	private String boxId;

	public String getBoxId() {
		return boxId;
	}

	public void setBoxId(String boxId) {
		this.boxId = boxId;
	}

	public Box() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public Box clone() {
		Box b = new Box();
		b.setBoxId(boxId);
		return b;
	}
}
