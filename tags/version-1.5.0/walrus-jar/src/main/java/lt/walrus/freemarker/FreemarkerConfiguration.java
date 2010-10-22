package lt.walrus.freemarker;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

import javax.servlet.ServletContext;

import org.springframework.util.StringUtils;

import freemarker.cache.TemplateCache;
import freemarker.cache.WebappTemplateLoader;
import freemarker.template.Template;


public class FreemarkerConfiguration extends freemarker.template.Configuration {

	HashMap<String, TemplateCache> templateCaches = new HashMap<String, TemplateCache>();
	HashMap<String, String> paths = new HashMap<String, String>();
	
	protected org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());
	
	public boolean hasTemplateCacheForHost(String serverName) {
		return templateCaches.containsKey(serverName);
	}
	
	public String getPathForHost(String serverName) {
		return paths.get(serverName);
	}
	
	public void replaceTemplateLoader(ServletContext servletContext, String hostName, String templatePath) {
		addTemplateLoader(servletContext, hostName, templatePath);
	}

	public void addTemplateLoader(ServletContext servletContext, String hostName, String templatePath) {
		if(null != templatePath && StringUtils.hasLength(templatePath)) {
			try {
				WebappTemplateLoader loader = new WebappTemplateLoader(servletContext, templatePath);
				
		        TemplateCache cache = new TemplateCache(loader);
		        cache.setDelay(0);
		        cache.setConfiguration(this);
		        cache.setLocalizedLookup(true);
		        templateCaches.put(hostName, cache);
		        paths.put(hostName, templatePath);
		        logger.debug("we've put a new templateCache for " + hostName + " with path: " + templatePath);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public Template getTemplate(String name, Locale locale, String encoding, boolean parse) throws IOException {
		Template result = null;
		logger.debug("getting template for path: " + name);
		logger.debug("current thread id: " + Thread.currentThread().getId());
		logger.debug("currentHost is: " + getCurrentHost());
		TemplateCache cache = templateCaches.get(getCurrentHost());
		if(null != cache) {
			result = cache.getTemplate(name, locale, encoding, parse);
		} else {
			logger.debug("no cache for " + getCurrentHost());
		}
		if (null == result){
			logger.debug("couldn't get template, falling back to default template");
			result = super.getTemplate(name, locale, encoding, parse);
		}
		
		if (result == null) {
            throw new FileNotFoundException("Template " + name + " not found.");
        }
		
		return result;
	}

	private ThreadLocal<String> currentHost = new ThreadLocal<String>();
	
	private String getCurrentHost() {
		return currentHost.get();
	}
	
	public void setCurrentHost(String serverName) {
		currentHost.set(serverName);
	}

	public void clearCurrentHost() {
		currentHost.remove();
	}
}
