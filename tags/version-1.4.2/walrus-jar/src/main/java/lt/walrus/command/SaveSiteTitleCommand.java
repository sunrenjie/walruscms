package lt.walrus.command;

import lt.walrus.model.Site;
import lt.walrus.service.WalrusService;

public class SaveSiteTitleCommand extends AbstractFieldCommand {
	private static final long serialVersionUID = -71676701228181864L;

	public SaveSiteTitleCommand(WalrusService service, Object context1, String text) {
		super(service, context1, text);
	}

	@Override
	protected String getPreviousValueFromContext(Object context1) {
		return ((Site) context1).getTitle();
	}

	@Override
	protected void setValueToContext(String val) {
		((Site) context).setTitle(val);
	}

}
