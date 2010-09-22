package lt.walrus.controller.editors;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorSupport;

import lt.walrus.model.Rubric;
import lt.walrus.service.WalrusService;

public class CommentEditor extends PropertyEditorSupport implements PropertyEditor {

	private WalrusService service;

	public CommentEditor(WalrusService service) {
		this.service = service;
	}

	@Override
	public void setAsText(String rubricId) throws IllegalArgumentException {
		Rubric r = service.getRubric(rubricId);
		if (null != r) {
			setValue(r);
		} else {
			throw new IllegalArgumentException("Could not find rubric: " + rubricId);
		}
	}

	@Override
	public String getAsText() {
		Rubric r = (Rubric) getValue();
		return (r != null ? r.getId() : "");
	}
}
