package lt.walrus.service;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import lt.walrus.controller.RubricController;
import lt.walrus.model.Site;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.context.ServletContextAware;

import com.mysql.jdbc.StringUtils;

public class TemplateManager implements ServletContextAware, InitializingBean, ITemplatePathResolver{
	public static final String TEMPLATE_PATH = "/WEB-INF/templates/";
	File destDir;
	private ServletContext servletContext;
	private RubricController rubricController;
	protected org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());
	
	public List<String> getTemplates() {
		List<String> templates = new ArrayList<String>();
		
		String realPath = servletContext.getRealPath(TEMPLATE_PATH);
		logger.debug("\n\n\n\n real path:" + realPath);
		File templatesDir = new File(realPath);
		logger.debug(" templatesDir: " + templatesDir);
		
		if(null != realPath && templatesDir.exists()) {
			File[] subDirectories = templatesDir.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return new File(dir, name).isDirectory();
				}
			});
			if(null != subDirectories && subDirectories.length > 0) {
				for (File file : subDirectories) {
					templates.add(file.getName());
				}
			}
		}
		
		return templates;
	}

	public boolean isCorrectTemplatePath(String template) {
		File tpl = new File(servletContext.getRealPath(TEMPLATE_PATH) + File.separator + template);
		return tpl.exists() && tpl.canRead() && tpl.isDirectory();
	}
	
	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}
	
	public ServletContext getServletContext() {
		return servletContext;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		getServletContext().setAttribute(KEY, this);
	}

	public void setRubricController(RubricController rubricController) {
		this.rubricController = rubricController;
	}

	public RubricController getRubricController() {
		return rubricController;
	}
	
	public String resolvePathForHost(HttpServletRequest request) {
		Site s = rubricController.getSite(request);
		if(null == s || null == s.getTemplatePath() || StringUtils.isEmptyOrWhitespaceOnly(s.getTemplatePath())) {
			return null;
		}
		return TemplateManager.TEMPLATE_PATH + s.getTemplatePath();
	}
}
