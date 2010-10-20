package lt.walrus.ajax;

import org.springmodules.xt.ajax.AjaxEvent;

public class EditedEntity {
	private String entity;
	private String field;
	private String id;
	private String elementId;

	public EditedEntity(AjaxEvent e) {
		elementId = e.getElementId().trim();

		if (elementId.split("_").length >= 3) {
			entity = elementId.split("_")[0];
			field = elementId.split("_")[1];
			id = elementId.split("_")[2];
		}
	}

	public boolean isEntity(String entityName) {
		return entityName.equals(entity);
	}

	public boolean isField(String fieldName) {
		return fieldName.equals(field);
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "[entity: " + entity + " field: " + field + " id: " + id + "]";
	}

	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
	}
}
