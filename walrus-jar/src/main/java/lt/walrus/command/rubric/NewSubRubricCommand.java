package lt.walrus.command.rubric;

import lt.walrus.model.Rubric;
import lt.walrus.service.RubricService;

import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxResponseImpl;

public class NewSubRubricCommand extends AbstractRubricCommand {
	private static final long serialVersionUID = 9167553109355598781L;
	protected String name;
	Rubric rubricCreated;

	public NewSubRubricCommand(final RubricService service, Rubric currRubric1, String name1) {
		super(service, currRubric1);
		name = name1.trim();
	}

	@Override
	public AjaxResponse execute() {
		rubricCreated = createRubric();
		logger.debug("NewSubRubricCommand.execute: rubric created: " + rubricCreated.getTitle());
		//currRubric.getChildren().size()
		context.addRubric(rubricCreated, 0);
		AjaxResponse r = new AjaxResponseImpl("UTF-8");
		addActionAfterExecute(r);
		return r;
	}

	public AjaxResponse redo() {
		rubricCreated.setId(0);
		context.addRubric(rubricCreated, 0);
		AjaxResponse r = new AjaxResponseImpl("UTF-8");
		addActionAfterRedo(r);
		return r;
	}

	public AjaxResponse undo() {
		context.delete(rubricCreated);
		AjaxResponse r = new AjaxResponseImpl("UTF-8");
		addActionAfterUndo(r);
		return r;
	}

	protected void addActionAfterRedo(AjaxResponse r) {
		addActionAfterUndo(r);
	}

	protected Rubric createRubric() {
		Rubric r = new Rubric(name);
		r.setParent(currRubric);
		return r;
	}
}
