package lt.walrus.controller.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import lt.walrus.controller.RubricController;
import lt.walrus.model.Rubric;
import lt.walrus.model.Site;
import lt.walrus.service.RubricService;
import lt.walrus.service.SiteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.support.RequestContext;
import org.springmodules.xt.ajax.AjaxEvent;

public class SiteResolver {
	protected org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());

	@Autowired
	private SiteService siteService;
	@Autowired
	private RubricService rubricService;

	// TODO refactor to config class
	@Resource(name = "staticServletPath")
	private String staticServletPath = RubricController.DEFAULT_STATIC_SERVLET_PATH;

	public Site getSite(HttpServletRequest request) {
		return siteService.getSite(request.getServerName(), getLanguage(request), null != request
				.getParameter(RubricController.PARAM_CREATE_SITE));
	}

	public Site getSite(AjaxEvent e) {
		String language = getLanguage(e);
		String host = e.getHttpRequest().getServerName();
		return siteService.getSite(host, language);
	}

	public String getLanguage(HttpServletRequest request) {
		String language = (new RequestContext(request)).getLocale().getLanguage();
		return language;
	}

	private String getLanguage(AjaxEvent e) {
		String language = (new RequestContext(e.getHttpRequest())).getLocale().getLanguage();
		return language;
	}

	public Rubric getCurrentRubric(HttpServletRequest request) {
		Rubric currRubric = getSite(request).getRootRubric();

		logger.debug("\n\nStsic servlet path is: " + getStaticServletPath());
		logger.debug("servlet path is: " + request.getServletPath());
		if (null != request.getParameter(RubricController.PARAM_RUBRIC_ID)) {
			currRubric = rubricService.get(Long.valueOf(request.getParameter(RubricController.PARAM_RUBRIC_ID)));
		} else if (request.getServletPath().equals(getStaticServletPath())) {
			String pagePermalink = request.getRequestURL().substring(
					request.getRequestURL().indexOf(getStaticServletPath()) + getStaticServletPath().length() + 1);
			try {
				pagePermalink = URLDecoder.decode(pagePermalink, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				logger.error("while decoging page url: ", e);
			}
			logger.debug("\n\n\nARTICLE URL: " + pagePermalink + "\n\n\n\n");
			currRubric = rubricService.getRubric(getSite(request), pagePermalink);
		}
		return currRubric;
	}

	public static String getFullContextPath(HttpServletRequest request) {
		String port = (80 == request.getServerPort()) ? "" : (":" + request.getServerPort());
		return request.getScheme() + "://" + request.getServerName() + port + request.getContextPath();
	}

	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}

	public void setStaticServletPath(String staticServletPath) {
		this.staticServletPath = staticServletPath;
	}

	public String getStaticServletPath() {
		return staticServletPath;
	}

	public void setRubricService(RubricService rubricService) {
		this.rubricService = rubricService;
	}
}
