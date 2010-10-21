package lt.walrus.ajax;

import java.util.HashMap;

import lt.walrus.model.Site;
import lt.walrus.service.BoxService;
import lt.walrus.service.RubricService;
import lt.walrus.service.SiteService;
import lt.walrus.service.SlideService;
import lt.walrus.undo.CommandManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.support.RequestContext;
import org.springmodules.xt.ajax.AbstractAjaxHandler;
import org.springmodules.xt.ajax.AjaxEvent;
import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxResponseImpl;
import org.springmodules.xt.ajax.action.ExecuteJavascriptFunctionAction;

public class AbstractWalrusAjaxHandler extends AbstractAjaxHandler {

	@Autowired
	protected RubricService service;
	@Autowired
    protected CommandManager commandManager;
	@Autowired
	protected SiteService siteService;
	@Autowired
	protected BoxService boxService;
	@Autowired
	protected SlideService slideService;

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public void setCommandManager(CommandManager commandManager1) {
        this.commandManager = commandManager1;
    }

	public RubricService getService() {
		return service;
	}

	public void setService(RubricService service) {
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
		return siteService.getSite(host, language);
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

	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}

	public SiteService getSiteService() {
		return siteService;
	}

	public void setBoxService(BoxService boxService) {
		this.boxService = boxService;
	}

	public BoxService getBoxService() {
		return boxService;
	}

	public void setSlideService(SlideService slideService) {
		this.slideService = slideService;
	}

	public SlideService getSlideService() {
		return slideService;
	}

}
