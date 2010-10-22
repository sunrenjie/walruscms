package lt.walrus.utils;

import org.springframework.web.servlet.mvc.multiaction.PropertiesMethodNameResolver;

public class DefaultPropertiesMethodNameResolver extends PropertiesMethodNameResolver {

	private String defaultMethod = null;

	@Override
	public String getHandlerMethodNameForUrlPath(String urlPath) {
		String ret = super.getHandlerMethodNameForUrlPath(urlPath);
		if (null == ret) {
			return getDefaultMethod();
		} else {
			return ret;
		}
	}

	public void setDefaultMethod(String defaultMethod) {
		this.defaultMethod = defaultMethod;
	}

	public String getDefaultMethod() {
		return defaultMethod;
	}
}