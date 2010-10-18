package lt.walrus.ajax;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springmodules.xt.ajax.AjaxExceptionHandler;
import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxResponseImpl;
import org.springmodules.xt.ajax.action.ExecuteJavascriptFunctionAction;

public class ImgToBigExceptionHandler implements AjaxExceptionHandler {

	public AjaxResponse handle(HttpServletRequest request, Exception ex) {
		AjaxResponse r = new AjaxResponseImpl();
		
        HashMap<String, Object> p = new HashMap<String, Object>();
        p.put("msg", "exception: " + ex.getMessage());
        r.addAction(new ExecuteJavascriptFunctionAction("displayError", p));
		
		return r;
	}

}
