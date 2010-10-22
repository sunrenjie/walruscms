package lt.walrus.command.article;

import lt.walrus.model.Rubric;
import lt.walrus.service.RubricService;

/**
 * @author mic
 * 
 */
public class SaveArticleBodyCommand extends AbstractArcticleFieldCommand {
	private static final long serialVersionUID = 5158255060233763844L;

	public SaveArticleBodyCommand(final RubricService service, Rubric context1, String text) {
        super(service, context1, text);
    }

    @Override
    protected String getPreviousValueFromContext(Object context1) {
        return ((Rubric) context1).getBody();
    }

    @Override
    protected void setValueToContext(String val) {
        ((Rubric) context).setBody(val);
    }
}
