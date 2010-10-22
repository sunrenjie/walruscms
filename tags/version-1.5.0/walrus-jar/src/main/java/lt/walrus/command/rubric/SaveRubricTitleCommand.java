package lt.walrus.command.rubric;

import java.util.HashMap;

import lt.walrus.ajax.WalrusRedirectAction;
import lt.walrus.command.AbstractFieldCommand;
import lt.walrus.model.Rubric;
import lt.walrus.service.RubricService;

import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.action.ExecuteJavascriptFunctionAction;

public class SaveRubricTitleCommand extends AbstractFieldCommand {
	private static final long serialVersionUID = 6704856206476639722L;

	public SaveRubricTitleCommand(final RubricService service, Rubric context1, String text) {
		super(service, context1, text);
	}

	@Override
	protected String getPreviousValueFromContext(Object context1) {
		return ((Rubric) context1).getTitle();
	}

	@Override
	protected void setValueToContext(String val) {
		((Rubric) context).setTitle(val);
	}

	@Override
	protected void addActionAfterExecute(AjaxResponse r) {
		//addChangeMenuAction(r, (Rubric) context);
		r.addAction(new ExecuteJavascriptFunctionAction("saveRubricTitleCallback", new HashMap<String, Object>()));
	}

	@Override
	protected void addActionAfterUndo(AjaxResponse r) {
		addRedirectToRubricAction(r);
	}

	@Override
	protected void addActionAfterRedo(AjaxResponse r) {
		addRedirectToRubricAction(r);
	}

	private void addRedirectToRubricAction(AjaxResponse r) {
		HashMap<String, String> p = new HashMap<String, String>();
		p.put("rubricId", String.valueOf(((Rubric) context).getId()));
		r.addAction(new WalrusRedirectAction("index", p));
	}

	protected void addChangeMenuAction(AjaxResponse response, Rubric currRubric) {
		response.addAction(new ExecuteJavascriptFunctionAction("reloadMenu", new HashMap<String, Object>()));
	}
}
