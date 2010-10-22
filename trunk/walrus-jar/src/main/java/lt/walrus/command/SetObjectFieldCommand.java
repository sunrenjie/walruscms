package lt.walrus.command;

import lt.walrus.service.SaveService;

import org.springframework.beans.DirectFieldAccessor;
import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxResponseImpl;

public class SetObjectFieldCommand<T> extends Command {
	private static final long serialVersionUID = 1469670978108055985L;
	
	private String field;
	protected T context;
	protected Object newValue;
	protected Object previousValue;
	protected SaveService<T> service;
	protected DirectFieldAccessor accessor;
	private String messageBase;

	/**
	 * @param service -
	 *            CRUDService servisas, kuris moka seivinti
	 * @param context1 -
	 *            Objektas, kurio laukus mes čia redaguojame
	 * @param field1 -
	 *            lauko pavadinimas
	 * @param text -
	 *            nauja lauko reikšmė
	 * @param messageBase -
	 *            pranešimo ape veiksmą rakto (message.properties) pradžia, prie
	 *            kurios bus prikabinta [lauko vardas].execute, undo, undoTitle,
	 *            redoTitle
	 */
	public SetObjectFieldCommand(SaveService<T> service, T context1, String field1, Object text, String messageBase) {
		this.service = service;
		context = context1;
		newValue = text;
		field = field1;
		accessor = new DirectFieldAccessor(context);
		previousValue = getPreviousValueFromContext();
		this.messageBase = messageBase;
	}

	protected Object getPreviousValueFromContext() {
		return accessor.getPropertyValue(getField());
	}

	protected void setValueToContext(Object val) {
		accessor.setPropertyValue(getField(), val);
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	@Override
	public AjaxResponse execute() {
		logger.debug("prev value: " + accessor.getPropertyValue(getField()) + " setting new value: " + newValue + " to field " + getField());

		setValueToContext(newValue);
		logger.debug("value set: " + accessor.getPropertyValue(getField()));
		service.save(context);

		AjaxResponse r = new AjaxResponseImpl("UTF-8");
		addActionAfterExecute(r);
		return r;
	}

	protected void addActionAfterExecute(AjaxResponse r) {
	}

	protected void addActionAfterUndo(AjaxResponse r) {
	}

	protected void addActionAfterRedo(AjaxResponse r) {
	}

	public AjaxResponse redo() {
		AjaxResponse r = execute();
		addActionAfterRedo(r);
		return r;
	}

	public AjaxResponse undo() {
		setValueToContext(previousValue);
		service.save(context);
		AjaxResponse r = new AjaxResponseImpl("UTF-8");
		addActionAfterUndo(r);
		return r;
	}

	public Object getContext() {
		return context;
	}

	public void setContext(T context) {
		this.context = context;
	}

	public void setService(SaveService<T> service) {
		this.service = service;
	}

	public final String getExecuteMessage() {
		return messageBase + "." + field + ".execute";
	}

	public String getUndoMessage() {
		return messageBase + "." + field + ".undo";
	}

	public String getRedoMessage() {
		return getExecuteMessage();
	}

	public String getUndoTitle() {
		return messageBase + "." + field + ".undoTitle";
	}

	public String getRedoTitle() {
		return messageBase + "." + field + ".redoTitle";
	}
}
