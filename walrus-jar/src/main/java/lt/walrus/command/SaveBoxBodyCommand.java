package lt.walrus.command;

import lt.walrus.model.Box;
import lt.walrus.model.TextBox;
import lt.walrus.service.BoxService;

import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.action.ReplaceContentAction;
import org.springmodules.xt.ajax.component.SimpleText;

public class SaveBoxBodyCommand extends AbstractFieldCommand<Box> {
	private static final long serialVersionUID = 1288566666356946392L;

	public SaveBoxBodyCommand(BoxService service, TextBox context1, String text) {
		super(service, context1, text);
	}

	@Override
	protected String getPreviousValueFromContext(Box context1) {
		return ((TextBox) context1).getBody();
	}

	@Override
	protected void setValueToContext(String val) {
		((TextBox) context).setBody(val);

	}
	
	@Override
	protected void addActionAfterExecute(AjaxResponse r) {
		r.addAction(new ReplaceContentAction("box_" + getFieldName() + "_" + ((Box) context).getBoxId(), new SimpleText(((TextBox) context).getBody())));
	}
	
	protected String getFieldName() {
		return "body";
	}

	@Override
	protected void addActionAfterRedo(AjaxResponse r) {
		addActionAfterExecute(r);
	}

	@Override
	protected void addActionAfterUndo(AjaxResponse r) {
		addActionAfterExecute(r);
	}

}
