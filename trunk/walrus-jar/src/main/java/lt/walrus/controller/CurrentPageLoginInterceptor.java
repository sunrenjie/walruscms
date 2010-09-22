package lt.walrus.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.security.ui.AbstractProcessingFilter;
import org.springframework.security.ui.savedrequest.SavedRequest;
import org.springframework.security.util.PortResolver;
import org.springframework.security.util.PortResolverImpl;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class CurrentPageLoginInterceptor extends HandlerInterceptorAdapter {

	protected Logger logger = Logger.getLogger(getClass());

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		request.getSession(true).setAttribute(
				AbstractProcessingFilter.SPRING_SECURITY_SAVED_REQUEST_KEY, defineReferer(request));

		return super.preHandle(request, response, handler);
	}

	private SavedRequest defineReferer(HttpServletRequest request) {
		String referer = request.getHeader("Referer");
		if (!StringUtils.hasText(referer)) {
			referer = request.getContextPath();
		}
		logger.debug("Came here from: " + referer);
		return new SavedReferrerRequest(request, new PortResolverImpl(), referer);
	}

}

class SavedReferrerRequest extends SavedRequest {
	private static final long serialVersionUID = 1L;
	
	String refererUrl;
	
	public SavedReferrerRequest(HttpServletRequest req, PortResolver prtRslv, String ref) {
		super(req, prtRslv);
		refererUrl = ref;
	}
	
	@Override
	public String getFullRequestUrl() {
		return refererUrl;
	}
	
	@Override
	public String getRequestUrl() {
		return refererUrl;
	}
	
	@Override
	public String getRequestURL() {
		return refererUrl;
	}
}
