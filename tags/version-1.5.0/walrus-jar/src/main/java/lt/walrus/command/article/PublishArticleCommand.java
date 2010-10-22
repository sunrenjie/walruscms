package lt.walrus.command.article;

import java.util.HashMap;

import lt.walrus.model.Rubric;
import lt.walrus.service.RubricService;

import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxResponseImpl;
import org.springmodules.xt.ajax.action.ExecuteJavascriptFunctionAction;

public class PublishArticleCommand extends AbstractArticleCommand {
	private static final long serialVersionUID = -8561841271725644235L;

	protected Rubric context;
	protected RubricService service;

    public PublishArticleCommand(final RubricService service, Rubric article) {
        context = article;
        this.service = service;
    }

    @Override
    public AjaxResponse execute() {
        if(null != context) {
            context.setOnline(true);
            service.save(context);
        }
        AjaxResponse r = new AjaxResponseImpl("UTF-8");
        return addChangeMenuAction(r);
    }

    public AjaxResponse undo() {
        if(null != context) {
            context.setOnline(false);
            service.save(context);
        }
        AjaxResponse r = new AjaxResponseImpl("UTF-8");
        return addChangeMenuAction(r);
    }

	protected AjaxResponse addChangeMenuAction(AjaxResponse response) {
		response.addAction(new ExecuteJavascriptFunctionAction("reloadMenu", new HashMap<String, Object>()));
		return response;
	}

    public AjaxResponse redo() {
        return execute();
    }
    
}
