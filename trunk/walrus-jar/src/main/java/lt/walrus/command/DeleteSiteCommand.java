package lt.walrus.command;

import java.util.HashMap;

import lt.walrus.model.Site;
import lt.walrus.service.SiteService;

import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxResponseImpl;
import org.springmodules.xt.ajax.action.ExecuteJavascriptFunctionAction;

public class DeleteSiteCommand extends Command {
	private static final long serialVersionUID = 4213392827509364753L;
	private SiteService service;
	private Site site;

	public DeleteSiteCommand(SiteService service, Site site) {
		this.service = service;
		this.site = site;
	}

	@Override
	public AjaxResponse execute() {
		service.delete(site);
		AjaxResponse r = new AjaxResponseImpl("UTF-8");
		r.addAction(new ExecuteJavascriptFunctionAction("siteDeleted", new HashMap<String, Object>()));
		return r;
	}

	public AjaxResponse redo() {
		return execute();
	}

	public AjaxResponse undo() {
		service.add(site);
		AjaxResponse r = new AjaxResponseImpl("UTF-8");
		r.addAction(new ExecuteJavascriptFunctionAction("siteRestored", new HashMap<String, Object>()));
		return r;
	}

	public void setService(SiteService service) {
		this.service = service;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

}
