package lt.walrus.command.article;

import lt.walrus.model.Rubric;
import lt.walrus.service.RubricService;

public class SaveArticleAbstractCommand extends AbstractArcticleFieldCommand {
	private static final long serialVersionUID = 6110290063222094123L;


	public SaveArticleAbstractCommand(final RubricService service, Rubric context1, String text) {
        super(service, context1, text);
    }

    @Override
    protected String getPreviousValueFromContext(Object context1) {
        return ((Rubric) context1).getAbstr();
    }

    @Override
    protected void setValueToContext(String val) {
        ((Rubric) context).setAbstr(val);
    }
}
