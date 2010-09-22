package lt.walrus.ajax;

import java.util.HashMap;

import org.springmodules.xt.ajax.AjaxEvent;
import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxResponseImpl;
import org.springmodules.xt.ajax.action.ExecuteJavascriptFunctionAction;

public class ResetVisitStatisticsHandler extends AbstractWalrusAjaxHandler {
	public AjaxResponse resetVisitStatistics(AjaxEvent e) {
		AjaxResponse r = new AjaxResponseImpl("UTF-8");
		
		r.addAction(new ExecuteJavascriptFunctionAction("reload", new HashMap<String, Object>()));
		return r;
	}
}
