package lt.walrus.command.rubric;

import java.util.HashMap;

import lt.walrus.ajax.WalrusRedirectAction;
import lt.walrus.command.Command;
import lt.walrus.model.Rubric;
import lt.walrus.service.RubricService;

import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.action.ExecuteJavascriptFunctionAction;

public abstract class AbstractRubricCommand extends Command {

	protected Rubric currRubric;
	protected RubricService context;

	public AbstractRubricCommand(final RubricService service, Rubric currRubric1) {
		context = service;
		currRubric = currRubric1;
	}

	protected void addActionAfterExecute(AjaxResponse r) {
		addChangeMenuAction(r, currRubric);
		addChangeRubricListAction(r, currRubric);
	}

	protected void addChangeMenuAction(AjaxResponse response, Rubric currRubric1) {
		response.addAction(new ExecuteJavascriptFunctionAction("reloadMenu", new HashMap<String, Object>()));
	}

	protected void addChangeRubricListAction(AjaxResponse response, Rubric currRubric1) {
		response.addAction(new ExecuteJavascriptFunctionAction("reloadRubricList", new HashMap<String, Object>()));
	}
	
	protected void addActionAfterUndo(AjaxResponse r) {
		HashMap<String, String> p = new HashMap<String, String>();
		if (null != currRubric) {
			p.put("rubricId", String.valueOf(currRubric.getId()));
		}
		r.addAction(new WalrusRedirectAction("index", p));
	}
}
