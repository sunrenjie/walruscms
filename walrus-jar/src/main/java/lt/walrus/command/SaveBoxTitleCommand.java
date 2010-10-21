package lt.walrus.command;

import lt.walrus.model.Box;
import lt.walrus.model.TextBox;
import lt.walrus.service.BoxService;

import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.action.ReplaceContentAction;
import org.springmodules.xt.ajax.component.SimpleText;

public class SaveBoxTitleCommand extends SaveBoxBodyCommand {
	private static final long serialVersionUID = 529133824523199310L;

	public SaveBoxTitleCommand(BoxService service, TextBox context1, String text) {
		super(service, context1, text);
	}

	protected String getPreviousValueFromContext(Box context1) {
		return ((TextBox) context1).getTitle();
	}

	@Override
	protected void setValueToContext(String val) {
		((TextBox) context).setTitle(val);
		
	}
	
	@Override
	protected String getFieldName() {
		return "title";
	}
	
	protected void addActionAfterExecute(AjaxResponse r) {
		r.addAction(new ReplaceContentAction("box_" + getFieldName() + "_" + ((Box) context).getBoxId(), new SimpleText(((TextBox) context).getTitle())));
	}
}
