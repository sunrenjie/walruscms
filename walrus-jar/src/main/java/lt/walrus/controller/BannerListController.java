package lt.walrus.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lt.walrus.model.BannerBox;
import lt.walrus.model.Box;

import org.springframework.web.servlet.ModelAndView;

public class BannerListController extends RubricController {

	public static final String PARAM_BOXID = "boxId";

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		if(null != request.getParameter(BannerListController.PARAM_BOXID)) {
			Box box = getSite(request).getBox(request.getParameter(BannerListController.PARAM_BOXID));
			if(null != box) {
				BannerBox bannerBox = (BannerBox) box;
				mav.addAllObjects(makeModel(request));
				mav.addObject("banners", bannerBox.getBanners());
				mav.addObject("boxId", request.getParameter(BannerListController.PARAM_BOXID));
			}
		}
		return mav;
	}

}
