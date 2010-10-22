package lt.walrus.view;

import javax.servlet.http.HttpServletRequest;

import lt.walrus.controller.BannerListController;
import lt.walrus.controller.RubricController;
import lt.walrus.controller.util.SiteResolver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.view.DefaultRequestToViewNameTranslator;

public class RequestParamToViewNameTranslator extends DefaultRequestToViewNameTranslator {

	public static final String VIEW_INDEX = "index";
	public static final String VIEW_RUBRIC = "rubric";
	public static final String VIEW_MENU = "menu";
	public static final String VIEW_RSS = "rss";
	public static final String VIEW_ATOM = "atom";
	public static final String PARAM_MENU = "menu";
	public static final String VIEW_BANNER_LIST = "bannerList";
	public static final String PARAM_RSS = "rss";
	public static final String PARAM_ATOM = "atom";
	public static final String VIEW_RUBRIC_LIST = "rubricList";
	public static final String PARAM_RUBRIC_LIST = "rubricList";
	public static final String PARAM_404 = "404";
	public static final String VIEW_404 = "404";
	public static final String VIEW_NAV_PATH = "navpathreload";
	public static final String PARAM_NAV_PATH = "navpathreload";
	public static final String VIEW_XML = "xml";
	public static final String PARAM_XML = "xml";

	public static final String VIEW_FEATURES = "features";
	public static final String PARAM_FEATURES = "features";
	private static final String PARAM_ADD_COMMENT = "addComment";
	private static final String VIEW_COMMENT_ADDED = "commentAdded";

	private String defaultViewName = VIEW_INDEX;

	@Autowired
	private SiteResolver siteResolver;

	@Override
	public String getViewName(HttpServletRequest request) {
		if (null != request.getParameter(RubricController.PARAM_RUBRIC_ID) && null == request.getParameter(PARAM_MENU)
				&& null == request.getParameter(PARAM_RSS) && null == request.getParameter(PARAM_RUBRIC_LIST) && null == request.getParameter(PARAM_NAV_PATH)) {
			return VIEW_RUBRIC;
		} else if (null != request.getParameter(PARAM_MENU)) {
			return VIEW_MENU;
		} else if (null != request.getParameter(PARAM_RUBRIC_LIST)) {
			return VIEW_RUBRIC_LIST;
		} else if (null != request.getParameter(PARAM_NAV_PATH)) {
			return VIEW_NAV_PATH;
		} else if (null != request.getParameter(PARAM_RSS)) {
			return VIEW_RSS;
		} else if (null != request.getParameter(PARAM_ATOM)) {
			return VIEW_ATOM;
		} else if (null != request.getParameter(BannerListController.PARAM_BOXID)) {
			return VIEW_BANNER_LIST;
		} else if (request.getServletPath().equals(siteResolver.getStaticServletPath())) {// TODO
																								// extract
																								// to
																								// some
																								// config
																								// class
			return VIEW_RUBRIC;
		} else if (request.getRequestURI().endsWith(PARAM_404)) {
			return VIEW_404;
		} else if (request.getRequestURI().endsWith(PARAM_404)) {
			return VIEW_404;
		} else if (request.getRequestURI().endsWith(PARAM_FEATURES)) {
			return VIEW_FEATURES;
		} else if (request.getRequestURI().endsWith(PARAM_ADD_COMMENT)) {
			return VIEW_COMMENT_ADDED;
		} else if (request.getRequestURI().endsWith(PARAM_XML)) {
			return VIEW_XML;
		} else {
			return getDefaultViewName();
		}
	}

	public String getDefaultViewName() {
		return defaultViewName;
	}

	public void setDefaultViewName(String defaultViewName) {
		this.defaultViewName = defaultViewName;
	}

	public void setSiteResolver(SiteResolver siteResolver) {
		this.siteResolver = siteResolver;
	}
}
