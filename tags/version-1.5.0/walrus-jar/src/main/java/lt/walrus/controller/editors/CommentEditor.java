package lt.walrus.controller.editors;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;

import lt.walrus.model.Rubric;
import lt.walrus.service.RubricService;

public class CommentEditor extends PropertyEditorSupport implements PropertyEditor {

	private RubricService rubricService;

	public CommentEditor(RubricService service) {
		this.rubricService = service;
	}

	@Override
	public void setAsText(String rubricId) throws IllegalArgumentException {
		Rubric r = rubricService.get(Long.valueOf(rubricId));
		if (null != r) {
			setValue(r);
		} else {
			throw new IllegalArgumentException("Could not find rubric: " + rubricId);
		}
	}

	@Override
	public String getAsText() {
		Rubric r = (Rubric) getValue();
		return (r != null ? String.valueOf(r.getId()) : "");
	}
}
