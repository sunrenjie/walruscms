package lt.walrus.command;

import lt.walrus.service.SaveService;

import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxResponseImpl;

public abstract class AbstractFieldCommand<T> extends Command {
	private static final long serialVersionUID = 1L;
	protected T context;
    protected String newValue;
    protected String previousValue;
	protected SaveService<T> service;

    protected abstract String getPreviousValueFromContext(T context1);
    protected abstract void setValueToContext(String val);

	public AbstractFieldCommand(final SaveService<T> service, T context1, String text) {
    	this.service = service;
        context = context1;
        newValue = text;
        previousValue = getPreviousValueFromContext(context); 
    }
    
    @Override
    public AjaxResponse execute() {
        setValueToContext(newValue);
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

}
