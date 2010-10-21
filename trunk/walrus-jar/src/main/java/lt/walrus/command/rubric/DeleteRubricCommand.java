package lt.walrus.command.rubric;

import java.util.HashMap;

import lt.walrus.ajax.WalrusRedirectAction;
import lt.walrus.model.Rubric;
import lt.walrus.service.RubricService;

import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxResponseImpl;
import org.springmodules.xt.ajax.action.ExecuteJavascriptFunctionAction;

public class DeleteRubricCommand extends AbstractRubricCommand {
	private static final long serialVersionUID = -1923172305580932873L;

	private Rubric delRubric;
	private int rubricIndex;

	public DeleteRubricCommand(final RubricService service, Rubric currRubric1, Rubric delRubric1) {
		super(service, currRubric1);
		delRubric = delRubric1;
	}

	@Override
	public AjaxResponse execute() {
		rubricIndex = delRubric.getParent().getChildren().indexOf(delRubric);
		if (rubricIndex < 0) {
			rubricIndex = 0;
		}
		context.delete(delRubric);
		AjaxResponse r = new AjaxResponseImpl("UTF-8");
		if (null != currRubric && currRubric.getId() == delRubric.getId()) {
			HashMap<String, String> p = new HashMap<String, String>();
			if (null != delRubric.getParent() && null != delRubric.getParent().getParent()) {
				p.put("rubricId", String.valueOf(delRubric.getParent().getId()));
			}
			r.addAction(new WalrusRedirectAction("index", p));
		} else {
			addActionAfterExecute(r);
		}
		
		return r;
	}

	public AjaxResponse undo() {
		delRubric.setId(0);
		context.addRubric(delRubric, rubricIndex);
		AjaxResponse r = new AjaxResponseImpl("UTF-8");
		r.addAction(new ExecuteJavascriptFunctionAction("reloadMenu", new HashMap<String, Object>()));
		return r;
	}

	public AjaxResponse redo() {
		return execute();
	}
}
