package lt.walrus.ajax;

import java.util.HashMap;

import lt.walrus.model.Site;
import lt.walrus.service.WalrusService;
import lt.walrus.undo.CommandManager;

import org.springframework.web.servlet.support.RequestContext;
import org.springmodules.xt.ajax.AbstractAjaxHandler;
import org.springmodules.xt.ajax.AjaxEvent;
import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxResponseImpl;
import org.springmodules.xt.ajax.action.ExecuteJavascriptFunctionAction;

public class AbstractWalrusAjaxHandler extends AbstractAjaxHandler {

    protected WalrusService service;
    protected CommandManager commandManager;

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public void setCommandManager(CommandManager commandManager1) {
        this.commandManager = commandManager1;
    }

	public WalrusService getService() {
		return service;
	}

	public void setService(WalrusService service) {
		this.service = service;
	}

	protected String getLanguage(AjaxEvent e) {
		String language = (new RequestContext(e.getHttpRequest())).getLocale().getLanguage();
		return language;
	}

	public static AjaxResponse addErrorMessage(AjaxResponse r, String message) {
		HashMap<String, Object> p = new HashMap<String, Object>();
		p.put("msg", message);
		r.addAction(new ExecuteJavascriptFunctionAction("displayError", p));
		return r;
	}

	protected Site getSite(AjaxEvent e) {
		String language = getLanguage(e);
		String host = getHost(e);
		return service.getSite(host, language);
	}

	private String getHost(AjaxEvent e) {
		return e.getHttpRequest().getServerName();
	}

	protected AjaxResponse makeErrorResponse(String message) {
		AjaxResponse response = new AjaxResponseImpl("UTF-8");
		HashMap<String, Object> p = new HashMap<String, Object>();
		p.put("msg", message);
		response.addAction(new ExecuteJavascriptFunctionAction("displayError", p));
		return response;
	}

}
