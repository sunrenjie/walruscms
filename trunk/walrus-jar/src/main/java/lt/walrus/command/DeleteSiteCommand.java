package lt.walrus.command;

import java.util.HashMap;

import lt.walrus.model.Site;
import lt.walrus.service.WalrusService;

import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxResponseImpl;
import org.springmodules.xt.ajax.action.ExecuteJavascriptFunctionAction;

public class DeleteSiteCommand extends Command {
	private static final long serialVersionUID = 4213392827509364753L;
	private WalrusService service;
	private Site site;

	public DeleteSiteCommand(WalrusService service, Site site) {
		this.service = service;
		this.site = site;
	}

	@Override
	public AjaxResponse execute() {
		service.deleteSite(site);
		AjaxResponse r = new AjaxResponseImpl("UTF-8");
		r.addAction(new ExecuteJavascriptFunctionAction("siteDeleted", new HashMap<String, Object>()));
		return r;
	}

	public AjaxResponse redo() {
		return execute();
	}

	public AjaxResponse undo() {
		service.addSite(site);
		AjaxResponse r = new AjaxResponseImpl("UTF-8");
		r.addAction(new ExecuteJavascriptFunctionAction("siteRestored", new HashMap<String, Object>()));
		return r;
	}

	public WalrusService getService() {
		return service;
	}

	public void setService(WalrusService service) {
		this.service = service;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

}
