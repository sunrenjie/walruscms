package lt.walrus.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lt.walrus.command.DeleteSiteCommand;
import lt.walrus.model.Site;

import org.springframework.web.servlet.ModelAndView;

public class DeleteSiteController extends RubricController {
	public final static String PAR_DELETE_SITE = "deleteSite";

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (null != request.getParameter(PAR_DELETE_SITE)) {
			Site site = getSite(request);
			if (null != site) {
				try {
					commandManager.execute(new DeleteSiteCommand(service, site));
				} catch (Exception ex) {
					logger.error("Deleting site (" + request.getRequestURL() + "): ", ex);
				}
			} else {
				logger.error("No site to delete: " + request.getRequestURL());
			}
		}
		return new ModelAndView("redirect:" + getFullContextPath(request) + "/cms/index");
	}
}
