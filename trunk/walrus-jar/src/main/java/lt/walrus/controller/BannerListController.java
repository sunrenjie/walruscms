package lt.walrus.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lt.walrus.controller.util.SiteResolver;
import lt.walrus.model.BannerBox;
import lt.walrus.model.Box;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * Returns a list of banners of particular banner box for editing
 */
public class BannerListController extends AbstractController {

	public static final String PARAM_BOXID = "boxId";
	@Autowired
	private SiteResolver siteResolver;

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		if(null != request.getParameter(BannerListController.PARAM_BOXID)) {
			Box box = siteResolver.getSite(request).getBox(request.getParameter(BannerListController.PARAM_BOXID));
			if(null != box) {
				BannerBox bannerBox = (BannerBox) box;
				// mav.addAllObjects(makeModel(request));
				mav.addObject("contextPath", request.getContextPath());
				mav.addObject("banners", bannerBox.getBanners());
				mav.addObject("boxId", request.getParameter(BannerListController.PARAM_BOXID));
			}
		}
		return mav;
	}

	public void setSiteResolver(SiteResolver siteResolver) {
		this.siteResolver = siteResolver;
	}
}
