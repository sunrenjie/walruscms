package lt.walrus.model;

import java.io.Serializable;

/**
 * A specific box that can contain one image.
 */
public class ImageBox extends Box implements Serializable {
	private static final long serialVersionUID = 368379052383078787L;
	
	private String image;
	
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	
	public ImageBox clone() {
		ImageBox b = new ImageBox();
		b.setImage(image);
		return b;
	}
}
