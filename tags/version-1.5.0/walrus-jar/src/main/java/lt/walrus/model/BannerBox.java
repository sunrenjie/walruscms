package lt.walrus.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Banner box contains several banners. On every display of the box different
 * Banner is chosen randomly
 */
public class BannerBox extends Box implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * List of banners
	 */
	List<Banner> banners = new ArrayList<Banner>();

	public List<Banner> getBanners() {
		return banners;
	}

	public void setBanners(List<Banner> banners) {
		this.banners = banners;
	}
	
	public Banner getRandomBanner() {
		if(null != banners && banners.size() > 0) {
			if(1 == banners.size()) {
				return banners.get(0);
			}
			return banners.get((int) Math.round((Math.random() * (double) (banners.size() - 1))));
		} else {
			return null;
		}
	}

	public Banner getBanner(long bannerId) {
		for (Banner b : getBanners()) {
			if (b.getId() == bannerId) {
				return b;
			}
		}
		return null;
	}
	
	public void updateBannerUrl(long bannerId, String url) {
		for (Banner b : getBanners()) {
			if (b.getId() == bannerId) {
				b.setUrl(url);
			}
		}
	}
	
	public BannerBox clone() {
		BannerBox b = new BannerBox();
		b.setBoxId(getBoxId());
		List<Banner> clonedBanners = new ArrayList<Banner>();
		for (Banner banner : banners) {
			clonedBanners.add(banner.clone());
		}
		b.setBanners(clonedBanners);
		return b;
	}
}
