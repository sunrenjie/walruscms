package lt.walrus.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lt.walrus.ajax.SortHandler;
import lt.walrus.model.Box;
import lt.walrus.model.Comment;
import lt.walrus.model.Rubric;
import lt.walrus.model.RubricBox;
import lt.walrus.model.Site;
import lt.walrus.service.RubricService;
import lt.walrus.service.SiteService;
import lt.walrus.service.TemplateManager;
import lt.walrus.undo.CommandManager;
import lt.walrus.utils.WalrusSecurity;

import org.springframework.aop.framework.Advised;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.support.RequestContext;

@Controller("rubricController")
public class RubricController extends AbstractController {

	public static final String PARAM_ARTICLE_ID = "articleId";
	public static final String PARAM_RUBRIC_ID = "rubricId";
	public static final String DEFAULT_STATIC_SERVLET_PATH = "/static";
	public static final String TREE_PATH = "/tree";
	public static final String PARAM_CREATE_SITE = "createSite";
	
	public static final String ATTR_CURRENT_RUBRIC = "currentRubric"; 

	private String staticServletPath = DEFAULT_STATIC_SERVLET_PATH;
	@Autowired
	protected RubricService service;

	protected org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());

	@Autowired
	protected CommandManager commandManager;
	@Autowired
	private TemplateManager templateManager;
	@Autowired
	protected SiteService siteService;
	@Autowired
	private String fileUrl;

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		if (null == getSite(request)) {
			return new ModelAndView("noSite");
		}
		ModelMap model = makeModel(request);
		if (null == model.get(ATTR_CURRENT_RUBRIC)) {
			return new ModelAndView("redirect:" + getFullContextPath(request) + "/cms/404");
		}

		if (!getCurrentRubric(request).isOnline() && !WalrusSecurity.loggedOnUserHasAdminRole()) {
			return new ModelAndView("redirect:" + getFullContextPath(request) + "/cms/404");
		}

		mav.addObject("model", model);
		Comment c = new Comment();
		c.setRubric((Rubric) model.get(ATTR_CURRENT_RUBRIC));
		mav.addObject("comment", c);
		return mav;
	}

	public ModelMap makeModel(HttpServletRequest request) {
		Site site = getSite(request);
		ModelMap model = new ModelMap();
		if (null == site) {
			return model;
		}
		model.addAttribute("site", site);
		model.addAttribute("sites", siteService.getSites(getLanguage(request)));
		try {
			// šito beprasmiško veiksmo reikia, kad proxis pasigautų ir
			// suwirintų commandManagerio priklausomybes
			commandManager.getMessages();
			// dedamės į modelį ne CommandManagerio proxį, o tikrąjį objektą,
			// kad WebFlow galėtų jį serializuoti
			model.addAttribute("commandManager", ((Advised) commandManager).getTargetSource().getTarget());
		} catch (Exception e) {
			logger.error("While unwrapping CommandManager proxy: ", e);
		}

		model.addAttribute(ATTR_CURRENT_RUBRIC, getCurrentRubric(request));
		if (WalrusSecurity.loggedOnUserHasAdminRole()) {
			model.addAttribute("isAdmin", true);
		}

		model.addAttribute("isArchive", "1".equals(request.getParameter("archive")));
		model.addAttribute("fullContextPath", getFullContextPath(request));
		model.addAttribute("contextPath", request.getContextPath());
		model.addAttribute("serverPort", request.getServerPort());
		model.addAttribute("requestURL", request.getRequestURL());
		model.addAttribute("queryString", request.getQueryString());
		model.addAttribute("servletPath", request.getServletPath());
		model.addAttribute("fileUrl", getFileUrl());
		if (null != request.getParameter(SortHandler.IS_TREE_PARAM) || TREE_PATH.equals(request.getPathInfo())) {
			model.addAttribute("pathInfo", TREE_PATH);
		} else {
			model.addAttribute("pathInfo", request.getPathInfo());
		}
		model.addAttribute("language", getLanguage(request));
		model.addAttribute("staticServletPath", getStaticServletPath());

		List<Rubric> boxRubrics = new ArrayList<Rubric>();
		for (Box b : site.getBoxes()) {
			if (b instanceof RubricBox) {
				boxRubrics.add(((RubricBox) b).getRubric());
			}
		}
		model.addAttribute("boxRubrics", boxRubrics);
		model.addAttribute("comment", new Comment());

		model.addAttribute("templateManager", templateManager);
		return model;
	}

	protected String getFullContextPath(HttpServletRequest request) {
		String port = (80 == request.getServerPort()) ? "" : (":" + request.getServerPort());
		return request.getScheme() + "://" + request.getServerName() + port + request.getContextPath();
	}

	private Rubric getCurrentRubric(HttpServletRequest request) {
		Rubric currRubric = getSite(request).getRootRubric();

		if (null != request.getParameter(PARAM_RUBRIC_ID)) {
			currRubric = service.get(Long.valueOf(request.getParameter(PARAM_RUBRIC_ID)));
		} else if (request.getServletPath().equals(getStaticServletPath())) {
			String pagePermalink = request.getRequestURL().substring(
					request.getRequestURL().indexOf(getStaticServletPath()) + getStaticServletPath().length() + 1);
			try {
				pagePermalink = URLDecoder.decode(pagePermalink, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				logger.error("while decoging page url: ", e);
			}
			logger.debug("\n\n\nARTICLE URL: " + pagePermalink + "\n\n\n\n");
			currRubric = service.getRubric(getSite(request), pagePermalink);
		}
		return currRubric;
	}

	public Site getSite(HttpServletRequest request) {
		return siteService.getSite(request.getServerName(), getLanguage(request), null != request.getParameter(PARAM_CREATE_SITE));
	}

	protected String getLanguage(HttpServletRequest request) {
		String language = (new RequestContext(request)).getLocale().getLanguage();
		return language;
	}

	public CommandManager getCommandManager() {
		return commandManager;
	}

	public void setCommandManager(CommandManager commandManager1) {
		this.commandManager = commandManager1;
	}

	public RubricService getService() {
		return service;
	}

	public void setService(RubricService service) {
		this.service = service;
	}

	public void setStaticServletPath(String staticServletPath) {
		this.staticServletPath = staticServletPath;
	}

	public String getStaticServletPath() {
		return staticServletPath;
	}

	public void setTemplateManager(TemplateManager templateManager) {
		this.templateManager = templateManager;
	}

	public TemplateManager getTemplateManager() {
		return templateManager;
	}

	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}

	public SiteService getSiteService() {
		return siteService;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getFileUrl() {
		return fileUrl;
	}

}
