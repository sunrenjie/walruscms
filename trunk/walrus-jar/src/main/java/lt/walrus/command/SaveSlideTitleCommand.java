package lt.walrus.command;

import lt.walrus.model.Slide;
import lt.walrus.service.WalrusService;

import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.action.ReplaceContentAction;
import org.springmodules.xt.ajax.action.SetAttributeAction;
import org.springmodules.xt.ajax.component.SimpleText;

public class SaveSlideTitleCommand extends SaveSlideBodyCommand {
	private static final long serialVersionUID = 529133824523199313L;

	public SaveSlideTitleCommand(WalrusService service, Slide context1, String text) {
		super(service, context1, text);
	}

	protected String getPreviousValueFromContext(Object context1) {
		return ((Slide) context1).getTitle();
	}

	@Override
	protected void setValueToContext(String val) {
		((Slide) context).setTitle(val);
		
	}
	
	@Override
	protected String getFieldName() {
		return "title";
	}
	
	protected void addActionAfterExecute(AjaxResponse r) {
		String slideId = ((Slide) context).getId();
		String slideTitle = ((Slide) context).getTitle();
		r.addAction(new ReplaceContentAction("slide_title_" + slideId, new SimpleText(slideTitle)));
		r.addAction(new ReplaceContentAction("slide_shortcut_" + slideId, new SimpleText(slideTitle)));
		r.addAction(new SetAttributeAction("slide_body_" + slideId, "title", slideTitle));
	}
	
	@Override
	public String getExecuteMessage() {
		return "Skaidrės pavadinimas pakeistas į '" + newValue + "'";
	}
	
	@Override
	public String getUndoMessage() {
		return "Skaidrės pavadinimas sugrąžintas į '" + previousValue + "'";
	}
}
