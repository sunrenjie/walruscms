package lt.walrus.ajax;

import java.lang.Object;
import java.lang.String;
import java.util.HashMap;


import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxResponseImpl;
import org.springmodules.xt.ajax.action.ExecuteJavascriptFunctionAction;

public class AjaxErrorMaker {
	public static AjaxResponse addErrorMessage(AjaxResponse r, String message) {
		HashMap<String, Object> p = new HashMap<String, Object>();
		p.put("msg", message);
		r.addAction(new ExecuteJavascriptFunctionAction("displayError", p));
		return r;
	}

	public static AjaxResponse makeErrorResponse(String message) {
		AjaxResponse response = new AjaxResponseImpl("UTF-8");
		addErrorMessage(response, message);
		return response;
	}

	public static AjaxResponse makeErrorResponse(String message, EditedEntity entity, String oldValue) {
		AjaxResponse response = AjaxErrorMaker.makeErrorResponse(message);
		HashMap<String, Object> p = new HashMap<String, Object>();
		p.put("elementId", entity.getElementId());
		p.put("value", oldValue == null ? "" : oldValue);
		response.addAction(new ExecuteJavascriptFunctionAction("restoreValue", p));
		return response;
	}
}
