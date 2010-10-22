package lt.walrus.ajax;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springmodules.xt.ajax.AjaxInterceptor;
import org.springmodules.xt.ajax.support.NoMatchingHandlerException;
import org.springmodules.xt.ajax.support.UnsupportedEventException;

/**
 * Public Ajax interceptorius, skirtas viešoms ajax užklausoms (pvz slaptažodžio priminimui)
 * Pasižymi tuo, kad užmuša standartinius AjaxInterceptoriaus metamus exceptionus tam, 
 * kad būtų galima turėti kelis AjaxInterceptorius - pvz vieną public ir kitą secured ir 
 * kad po to, kai public nerado kaip apdoroti ajax requestą, tai duotų pabandyt ir kitam interceptoriui 
 * @author mic
 *
 */
public class UnsecuredAjaxInterceptor extends AjaxInterceptor {
	protected transient org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		try {
			logger.debug("UnsecuredAjaxInterceptor.preHandle: ");
			return super.preHandle(request, response, handler);
		} catch (UnsupportedEventException e) {
			logger.debug("UnsecuredAjaxInterceptor.preHandle: EXPECTED: " + e.getMessage());
			return true;
		} catch (NoMatchingHandlerException e) {
			logger.debug("UnsecuredAjaxInterceptor.preHandle: EXPECTED: " + e.getMessage());
			return true;
		} catch (Exception e) {
			logger.debug("UnsecuredAjaxInterceptor.preHandle: EXPECTED3: " + e.getMessage());
			return true;
		}
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		logger.debug("UnsecuredAjaxInterceptor.postHandle: model and view: " + modelAndView);
		if(response.isCommitted()) {
			return;
		}
		try {
			logger.debug("UnsecuredAjaxInterceptor.postHandle: ");
			super.postHandle(request, response, handler, modelAndView);
			logger.debug("UnsecuredAjaxInterceptor.postHandle: after handling: " + modelAndView);
		} catch (UnsupportedEventException e) {
			logger.debug("UnsecuredAjaxInterceptor.preHandle: EXPECTED1: " + e.getMessage());
		} catch (NoMatchingHandlerException e) {
			logger.debug("UnsecuredAjaxInterceptor.preHandle: EXPECTED2: " + e.getMessage());
		} catch (Exception e) {
			logger.debug("UnsecuredAjaxInterceptor.preHandle: EXPECTED3: " + e.getMessage());
		}
	}
}
