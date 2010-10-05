package lt.walrus.service;

import javax.servlet.http.HttpServletRequest;

public interface ITemplatePathResolver {

	public static final String KEY = "lt.walrus.servlet.ITemplatePathResolver";

	/**
	 * Returns template path for the site, associated with given request
	 */
	public abstract String resolvePathForHost(HttpServletRequest request);

}