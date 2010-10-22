package lt.walrus.freemarker;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lt.walrus.service.ITemplatePathResolver;
import freemarker.template.Configuration;

public class FreemarkerServlet extends freemarker.ext.servlet.FreemarkerServlet {
	private static final long serialVersionUID = 4319152077840083199L;
	protected org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());

	@Override
	protected boolean preprocessRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.debug("preprocessing request for host: " + request.getServerName());
		String templatePath = getTemplatePathResolver().resolvePathForHost(request);
		logger.debug("tempalte path for it: " + templatePath);
		if(null != templatePath) {
			if(!getConfiguration().hasTemplateCacheForHost(request.getServerName())) {
				logger.debug("we don't have template cache for this host, adding " + templatePath);
				getConfiguration().addTemplateLoader(getServletContext(), request.getServerName(), templatePath);
			} else if(!getConfiguration().getPathForHost(request.getServerName()).equals(templatePath)) {
				logger.debug("we have template cache, but the template path has changed, replacing");
				getConfiguration().replaceTemplateLoader(getServletContext(), request.getServerName(), templatePath);
			}
			logger.debug("setting currentHost for current thread: " + Thread.currentThread().getId());
			getConfiguration().setCurrentHost(request.getServerName());
		} else {
			logger.debug("we don't know special template for this host, fallback to default");
		}
		return super.preprocessRequest(request, response);
	}
	
	private ITemplatePathResolver getTemplatePathResolver() {
		return (ITemplatePathResolver) getServletContext().getAttribute(ITemplatePathResolver.KEY);
	}

	@Override
	protected FreemarkerConfiguration getConfiguration() {
		return (FreemarkerConfiguration) super.getConfiguration();
	}
	
	@Override
	protected Configuration createConfiguration() {
		return new FreemarkerConfiguration();
	}
}
