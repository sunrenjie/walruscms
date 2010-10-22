package lt.walrus.controller.util;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import lt.walrus.ajax.SortHandler;
import lt.walrus.controller.RubricController;
import lt.walrus.model.Box;
import lt.walrus.model.Comment;
import lt.walrus.model.Rubric;
import lt.walrus.model.RubricBox;
import lt.walrus.model.Site;
import lt.walrus.service.SiteService;
import lt.walrus.service.TemplateManager;
import lt.walrus.undo.CommandManager;
import lt.walrus.utils.WalrusSecurity;

import org.springframework.aop.framework.Advised;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;

public class ModelMaker {
	protected org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());

	@Autowired
	private TemplateManager templateManager;
	@Autowired
	protected SiteService siteService;
	@Autowired
	protected SiteResolver siteResolver;
	@Autowired
	protected CommandManager commandManager;
	@Resource(name = "fileUrl")
	private String fileUrl;

	public ModelMap makeModel(HttpServletRequest request) {
		Site site = siteResolver.getSite(request);
		ModelMap model = new ModelMap();
		if (null == site) {
			return model;
		}
		model.addAttribute("site", site);
		model.addAttribute("sites", siteService.getSites(siteResolver.getLanguage(request)));
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

		model.addAttribute(RubricController.ATTR_CURRENT_RUBRIC, siteResolver.getCurrentRubric(request));
		if (WalrusSecurity.loggedOnUserHasAdminRole()) {
			model.addAttribute("isAdmin", true);
		}

		model.addAttribute("isArchive", "1".equals(request.getParameter("archive")));
		model.addAttribute("fullContextPath", SiteResolver.getFullContextPath(request));
		model.addAttribute("contextPath", request.getContextPath());
		model.addAttribute("serverPort", request.getServerPort());
		model.addAttribute("requestURL", request.getRequestURL());
		model.addAttribute("queryString", request.getQueryString());
		model.addAttribute("servletPath", request.getServletPath());
		model.addAttribute("fileUrl", getFileUrl());
		if (null != request.getParameter(SortHandler.IS_TREE_PARAM) || RubricController.TREE_PATH.equals(request.getPathInfo())) {
			model.addAttribute("pathInfo", RubricController.TREE_PATH);
		} else {
			model.addAttribute("pathInfo", request.getPathInfo());
		}
		model.addAttribute("language", siteResolver.getLanguage(request));
		model.addAttribute("staticServletPath", siteResolver.getStaticServletPath());

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

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setSiteResolver(SiteResolver siteResolver) {
		this.siteResolver = siteResolver;
	}

	public void setTemplateManager(TemplateManager templateManager) {
		this.templateManager = templateManager;
	}

	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}

	public void setCommandManager(CommandManager commandManager) {
		this.commandManager = commandManager;
	}
}
