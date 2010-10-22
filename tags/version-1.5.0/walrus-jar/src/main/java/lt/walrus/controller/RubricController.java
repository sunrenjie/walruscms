package lt.walrus.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lt.walrus.controller.util.ModelMaker;
import lt.walrus.controller.util.SiteResolver;
import lt.walrus.model.Comment;
import lt.walrus.model.Rubric;
import lt.walrus.utils.WalrusSecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

@Controller("rubricController")
public class RubricController extends AbstractController {
	protected org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());

	public static final String PARAM_ARTICLE_ID = "articleId";
	public static final String PARAM_RUBRIC_ID = "rubricId";
	public static final String DEFAULT_STATIC_SERVLET_PATH = "/static";
	public static final String TREE_PATH = "/tree";
	public static final String PARAM_CREATE_SITE = "createSite";
	
	public static final String ATTR_CURRENT_RUBRIC = "currentRubric"; 

	@Autowired
	private SiteResolver siteResolver;
	@Autowired
	protected ModelMaker modelMaker;

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		logger.debug("handling request: " + SiteResolver.getFullContextPath(request));
		logger.debug("servlet path is: " + request.getServletPath());

		if (null == siteResolver.getSite(request)) {
			return new ModelAndView("noSite");
		}
		ModelMap model = modelMaker.makeModel(request);
		if (null == model.get(ATTR_CURRENT_RUBRIC)) {
			logger.debug("redirecting to 404 because could not resolve the currentRubric");
			return new ModelAndView("redirect:" + SiteResolver.getFullContextPath(request) + "/cms/404");
		}

		if (!siteResolver.getCurrentRubric(request).isOnline() && !WalrusSecurity.loggedOnUserHasAdminRole()) {
			logger.debug("redirecting because current rubric is offline: " + siteResolver.getCurrentRubric(request));
			return new ModelAndView("redirect:" + SiteResolver.getFullContextPath(request) + "/cms/404");
		}

		mav.addObject("model", model);
		Comment c = new Comment();
		c.setRubric((Rubric) model.get(ATTR_CURRENT_RUBRIC));
		mav.addObject("comment", c);
		return mav;
	}

	public void setSiteResolver(SiteResolver siteResolver) {
		this.siteResolver = siteResolver;
	}

	public void setModelMaker(ModelMaker modelMaker) {
		this.modelMaker = modelMaker;
	}
}
