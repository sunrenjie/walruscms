package lt.walrus.command.article;

import java.util.HashMap;

import lt.walrus.ajax.WalrusRedirectAction;
import lt.walrus.command.Command;
import lt.walrus.controller.RubricController;
import lt.walrus.model.Rubric;

import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxResponseImpl;

public abstract class AbstractArticleCommand extends Command {
	private static final long serialVersionUID = -2863328143243689646L;

	public AbstractArticleCommand() {
    	// nothing
    }
    
    protected AjaxResponse addRedirectToArticleAction(Rubric article) {
        AjaxResponse response = new AjaxResponseImpl("UTF-8");
		HashMap<String, String> p = new HashMap<String, String>();
		p.put(RubricController.PARAM_RUBRIC_ID, String.valueOf(article.getId()));
        
        response.addAction(new WalrusRedirectAction("index", p));
        return response;
    }

}
