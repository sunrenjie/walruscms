package lt.walrus.model;

import java.io.Serializable;

/**
 * Simple text box that has a title and a body
 */
public class TextBox extends Box implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Title of the box
	 */
	private String title;
	/**
	 * Body of the box
	 */
	private String body;

	public TextBox() {
		super();
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
	
	public TextBox clone() {
		TextBox b = new TextBox();
		b.setBoxId(getBoxId());
		b.setBody(body);
		b.setTitle(title);
		return b;
	}
}
