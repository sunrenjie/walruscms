package lt.walrus.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxResponseImpl;
import org.springmodules.xt.ajax.action.ExecuteJavascriptFunctionAction;
import org.springmodules.xt.ajax.util.AjaxResponseSender;

/**
 * @author mic
 * 
 */
public class SizeLimitExceededExceptionHandler implements HandlerExceptionResolver {
    protected org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object arg2, Exception ex) {
    	
		AjaxResponse r = new AjaxResponseImpl();
		
		logger.error("SizeLimitExceededExceptionHandler.resolveException: " + ex.getMessage(), ex);
		
        HashMap<String, Object> p = new HashMap<String, Object>();
        p.put("msg", "exception: " + ex.getMessage());
        r.addAction(new ExecuteJavascriptFunctionAction("displayError", p));
        
        try {
			AjaxResponseSender.sendResponse(response, r);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
        ModelAndView mv = new ModelAndView();
        mv.clear();
        return mv;
    }

}
