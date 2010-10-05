package lt.walrus.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lt.walrus.model.Site;
import lt.walrus.service.TemplateManager;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

public class TemplateController extends RubricController {
	private TemplateManager templateManager;
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Site s = getSite(request);
		String template = request.getParameter("template");
		if(null != template && StringUtils.hasText(template) && templateManager.isCorrectTemplatePath(template)) {
			s.setTemplatePath(template);
			service.save(s);
		}
		return new ModelAndView("redirect:" + getFullContextPath(request) + "/cms/index");
	}

	public TemplateManager getTemplateManager() {
		return templateManager;
	}

	public void setTemplateManager(TemplateManager templateManager) {
		this.templateManager = templateManager;
	}
}
