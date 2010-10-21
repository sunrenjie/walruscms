package lt.walrus.command;

import lt.walrus.model.Slide;
import lt.walrus.service.SlideService;

import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.action.ReplaceContentAction;
import org.springmodules.xt.ajax.component.SimpleText;

public class SaveSlideBodyCommand extends AbstractFieldCommand<Slide> {
	private static final long serialVersionUID = 1288566666356946393L;

	public SaveSlideBodyCommand(SlideService service, Slide context1, String text) {
		super(service, context1, text);
	}

	@Override
	protected String getPreviousValueFromContext(Slide context1) {
		return context1.getBody();
	}

	@Override
	protected void setValueToContext(String val) {
		context.setBody(val);
	}
	
	@Override
	protected void addActionAfterExecute(AjaxResponse r) {
		r.addAction(new ReplaceContentAction("slide_body_" + ((Slide) context).getId(), new SimpleText(((Slide) context).getBody())));
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
	
	@Override
	public String getExecuteMessage() {
		return "Skaidrės turinys pakeistas";
	}
	
	@Override
	public String getUndoMessage() {
		return "Skaidrės turinys sugrąžintas";
	}

}
