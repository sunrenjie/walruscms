package lt.walrus.ajax;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lt.walrus.utils.WalrusSecurity;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;
import org.springmodules.xt.ajax.AjaxAction;
import org.springmodules.xt.ajax.AjaxInterceptor;
import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxResponseImpl;
import org.springmodules.xt.ajax.action.RedirectAction;
import org.springmodules.xt.ajax.util.AjaxResponseSender;

public class SecuredAjaxInterceptor extends AjaxInterceptor {

	protected Logger logger = Logger.getLogger(getClass());

	private boolean urlCondition(String targetUrl) {
		return targetUrl.contains("cms/upload") || targetUrl.contains("cms/editbanner") || targetUrl.contains("cms/savefield");
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		String targetUrl = ((request.getRequestURL()).toString()).toLowerCase();
		if (isAjaxRequest(request)) {
			if (isAuthorized(request)) {
				try {
					logger.debug("SecuredAjaxInterceptor.postHandle: modelAndView before handling: " + modelAndView);
					super.postHandle(request, response, handler, modelAndView);
					logger.debug("SecuredAjaxInterceptor.postHandle: modelAndView after handling: " + modelAndView);
					logger.debug("SecuredAjaxInterceptor.postHandle: clearing modekAndView");
					modelAndView.clear();
					logger.debug("SecuredAjaxInterceptor.postHandle: modelAndView after clearing: " + modelAndView);
					logger.debug("SecuredAjaxInterceptor.postHandle: modelAndView is cleared: " + modelAndView.wasCleared());
				} catch (Exception e) {
					logger.error("SecuredAjaxInterceptor.postHandle: " + e.getMessage(), e);
				}
			} else {
				//redirectToLogin(request, response, modelAndView);
				logger.debug("SecuredAjaxInterceptor.postHandle: redirecting to index 1");
				redirectToIndex(request, response, modelAndView); // TODO apgalvoti i kur redirektinti ajax'ines uzklausas po logino
			}
		} else {
			if(urlCondition(targetUrl)) {
				logger.debug("SecuredAjaxInterceptor.postHandle: redirecting to index 2");
				redirectToIndex(request, response, modelAndView);
			}
		}
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		logger.debug("SecuredAjaxInterceptor.preHandle: ");
		String targetUrl = ((request.getRequestURL()).toString()).toLowerCase();
		if (isAjaxRequest(request)) {
			if (isAuthorized(request)) {
				return super.preHandle(request, response, handler);
			} else {
				//redirectToLogin(request, response, null);
				redirectToIndex(request, response, null); // TODO apgalvoti i kur redirektinti ajax'ines uzklausas po logino
				return false;
			}
		} else {
			if(urlCondition(targetUrl)) {
				redirectToIndex(request, response, null);
				return false;
			}
			return true;
		}
	}
	
	private boolean isAuthorized(HttpServletRequest request) {
		return WalrusSecurity.loggedOnUserHasAdminRole();
	}

	@Override
	public boolean isAjaxRequest(HttpServletRequest request) {
		return	! WebUtils.isIncludeRequest(request) &&
				super.isAjaxRequest(request);
	}
	
    private void redirectToLogin(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws IOException {
        // Creating Ajax redirect action:
        AjaxResponse ajaxResponse = new AjaxResponseImpl();
        AjaxAction ajaxAction = new RedirectAction(new StringBuilder(request.getContextPath()).append("/cms/login").toString(), modelAndView);
        ajaxResponse.addAction(ajaxAction);
        // Need to clear the ModelAndView because we are handling the response by ourselves:
        if (null != modelAndView) {
        	modelAndView.clear();
        }
        AjaxResponseSender.sendResponse(response, ajaxResponse);
    }
    
    private void redirectToIndex(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) throws IOException {
        // Creating Ajax redirect action:
        AjaxResponse ajaxResponse = new AjaxResponseImpl();
        AjaxAction ajaxAction = new RedirectAction(new StringBuilder(request.getContextPath()).append("/cms/index").toString(), modelAndView);
        ajaxResponse.addAction(ajaxAction);
        // Need to clear the ModelAndView because we are handling the response by ourselves:
        if (null != modelAndView) {
        	modelAndView.clear();
        }
        AjaxResponseSender.sendResponse(response, ajaxResponse);
    }
}
