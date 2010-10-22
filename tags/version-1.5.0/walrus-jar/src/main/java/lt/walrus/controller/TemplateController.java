package lt.walrus.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lt.walrus.controller.util.SiteResolver;
import lt.walrus.model.Site;
import lt.walrus.service.SiteService;
import lt.walrus.service.TemplateManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

@Controller
public class TemplateController extends AbstractController {
	@Autowired
	private TemplateManager templateManager;
	@Autowired
	private SiteResolver siteResolver;
	@Autowired
	private SiteService siteService;
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Site site = siteResolver.getSite(request);
		String template = request.getParameter("template");
		if(null != template && StringUtils.hasText(template) && templateManager.isCorrectTemplatePath(template)) {
			site.setTemplatePath(template);
			siteService.save(site);
		}
		return new ModelAndView("redirect:" + SiteResolver.getFullContextPath(request) + "/cms/index");
	}

	public void setTemplateManager(TemplateManager templateManager) {
		this.templateManager = templateManager;
	}

	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}

	public void setSiteResolver(SiteResolver siteResolver) {
		this.siteResolver = siteResolver;
	}
}
