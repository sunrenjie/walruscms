package lt.walrus.command.article;

import java.util.HashMap;

import lt.walrus.ajax.WalrusRedirectAction;
import lt.walrus.command.AbstractFieldCommand;
import lt.walrus.controller.RubricController;
import lt.walrus.model.Rubric;
import lt.walrus.service.RubricService;

import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.action.ExecuteJavascriptFunctionAction;

public abstract class AbstractArcticleFieldCommand extends AbstractFieldCommand {

	private static final long serialVersionUID = -4319437905850484544L;

	public AbstractArcticleFieldCommand(final RubricService service, Rubric context1, String text) {
        super(service, context1, text);
    }

    @Override
    protected void addActionAfterExecute(AjaxResponse r) {
        r.addAction(new ExecuteJavascriptFunctionAction("noop", new HashMap<String, Object>()));
    }
    
    @Override
    protected void addActionAfterUndo(AjaxResponse r) {
        addRedirectToArticleAction(r);
    }

    @Override
    protected void addActionAfterRedo(AjaxResponse r) {
        addRedirectToArticleAction(r);
    }

    private void addRedirectToArticleAction(AjaxResponse r) {
        HashMap<String, String> p = new HashMap<String, String>();
		p.put(RubricController.PARAM_RUBRIC_ID, String.valueOf(((Rubric) context).getId()));
        r.addAction(new WalrusRedirectAction("index", p));
    }
}
