package lt.walrus.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lt.walrus.command.DeleteSiteCommand;
import lt.walrus.controller.util.SiteResolver;
import lt.walrus.model.Site;
import lt.walrus.service.SiteService;
import lt.walrus.undo.CommandManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

@Controller("deleteSiteController")
public class DeleteSiteController extends AbstractController {
	public final static String PAR_DELETE_SITE = "deleteSite";
	@Autowired
	private SiteResolver siteResolver;
	@Autowired
	private CommandManager commandManager;
	@Autowired
	private SiteService siteService;

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (null != request.getParameter(PAR_DELETE_SITE)) {
			Site site = siteResolver.getSite(request);
			if (null != site) {
				try {
					commandManager.execute(new DeleteSiteCommand(siteService, site));
				} catch (Exception ex) {
					logger.error("Deleting site (" + request.getRequestURL() + "): ", ex);
				}
			} else {
				logger.error("No site to delete: " + request.getRequestURL());
			}
		}
		return new ModelAndView("redirect:" + SiteResolver.getFullContextPath(request) + "/cms/index");
	}

	public void setSiteResolver(SiteResolver siteResolver) {
		this.siteResolver = siteResolver;
	}

	public void setCommandManager(CommandManager commandManager) {
		this.commandManager = commandManager;
	}

	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}
}
