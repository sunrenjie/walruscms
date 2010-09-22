package lt.walrus.model;

import java.io.Serializable;
import java.util.UUID;

/**
 * A Banner for BannerBox
 */
public class Banner implements Serializable {
	private static final long serialVersionUID = 1245957677160247953L;

	String id;
	/**
	 * Path to file on the filesystem
	 */
	private String banner;
	/**
	 * Url the banner leads to
	 */
	private String url;

	public Banner() {
		id = UUID.randomUUID().toString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBanner() {
		return banner;
	}

	public void setBanner(String banner) {
		this.banner = banner;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public Banner clone() {
		Banner b = new Banner();
		b.setBanner(banner);
		b.setUrl(url);
		return b;
	}
}
