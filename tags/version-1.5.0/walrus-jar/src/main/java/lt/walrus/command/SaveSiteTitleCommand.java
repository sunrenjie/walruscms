package lt.walrus.command;

import lt.walrus.model.Site;
import lt.walrus.service.SiteService;

public class SaveSiteTitleCommand extends AbstractFieldCommand<Site> {
	private static final long serialVersionUID = -71676701228181864L;

	public SaveSiteTitleCommand(SiteService service, Site context1, String text) {
		super(service, context1, text);
	}

	@Override
	protected String getPreviousValueFromContext(Site context1) {
		return ((Site) context1).getTitle();
	}

	@Override
	protected void setValueToContext(String val) {
		((Site) context).setTitle(val);
	}

}
