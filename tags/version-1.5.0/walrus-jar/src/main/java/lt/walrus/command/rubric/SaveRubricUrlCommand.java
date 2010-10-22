package lt.walrus.command.rubric;

import lt.walrus.command.AbstractFieldCommand;
import lt.walrus.model.Rubric;
import lt.walrus.service.RubricService;

public class SaveRubricUrlCommand extends AbstractFieldCommand {
	private static final long serialVersionUID = 7258078586214572992L;

	public SaveRubricUrlCommand(RubricService service, Rubric rubric, String text) {
		super(service, rubric, text);
	}

	@Override
	protected String getPreviousValueFromContext(Object context1) {
		return ((Rubric) context1).getUrl();
	}

	@Override
	protected void setValueToContext(String val) {
		((Rubric) context).setUrl(val);
	}
}
