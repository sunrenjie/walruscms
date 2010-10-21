package lt.walrus.command.article;

import lt.walrus.model.Rubric;
import lt.walrus.service.RubricService;

import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxResponseImpl;

public class UnpublishArticleCommand extends PublishArticleCommand {
	private static final long serialVersionUID = -6336418338178916019L;

	public UnpublishArticleCommand(final RubricService service, Rubric article) {
        super(service, article);
    }
    
    @Override
    public AjaxResponse execute() {
        context.setOnline(false);
        service.save(context);
        AjaxResponse r = new AjaxResponseImpl("UTF-8");
        return addChangeMenuAction(r);
    }
    
    @Override
    public AjaxResponse undo() {
        context.setOnline(true);
        service.save(context);
        AjaxResponse r = new AjaxResponseImpl("UTF-8");
        return addChangeMenuAction(r);
    }
}
