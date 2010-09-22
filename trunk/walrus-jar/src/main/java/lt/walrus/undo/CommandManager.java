package lt.walrus.undo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import lt.walrus.command.Command;

import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.action.ExecuteJavascriptFunctionAction;

public class CommandManager implements Serializable {
	private static final long serialVersionUID = -7566851051024644115L;
	protected static transient org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(CommandManager.class);
	
    ArrayList<Command> commandHistory = new ArrayList<Command>();
    int lastCommandIndex = -1;
    private String lastMessage = "";
    
    protected static transient ResourceBundleMessageSource messages;
    
    public AjaxResponse undo() {
        if(canUndo()) {
            AjaxResponse r = commandHistory.get(lastCommandIndex).undo();
            addCommandMessage(r, commandHistory.get(lastCommandIndex).getUndoMessage());
            lastCommandIndex--;
            return r;
        } else {
            return null;
        }
    }

    public String getUndoTitle() {
        return getMessage(commandHistory.get(lastCommandIndex).getUndoTitle());
    }

    public AjaxResponse execute(Command c) {
        clearRemainingHistory();
        commandHistory.add(c);
        lastCommandIndex = commandHistory.size() - 1;
        AjaxResponse r = c.execute();
        addCommandMessage(r, c.getExecuteMessage());
        return r;
    }
    
    private void clearRemainingHistory() {
        for(int i = commandHistory.size()-1; i > lastCommandIndex ; i--) {
            commandHistory.remove(i);
        }
    }

    public AjaxResponse redo() {
        if(canRedo()) {
            lastCommandIndex++;
            AjaxResponse r = commandHistory.get(lastCommandIndex).redo();
            addCommandMessage(r, commandHistory.get(lastCommandIndex).getRedoMessage());
            return r;
        } else {
            return null;
        }
    }
    
    public String getRedoTitle() {
        return getMessage(commandHistory.get(lastCommandIndex+1).getRedoTitle());
    }
    public boolean canRedo() {
        return !commandHistory.isEmpty() && lastCommandIndex < commandHistory.size()-1;
    }

    public boolean canUndo() {
        return !commandHistory.isEmpty() && lastCommandIndex >= 0;
    }

    private void addCommandMessage(AjaxResponse r, String messageKey) {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("msg", storeLastMessage(messageKey));
        r.addAction(new ExecuteJavascriptFunctionAction("setCommandMsg", params));
        if(willNotReload(r)) {
            r.addAction(new ExecuteJavascriptFunctionAction("reloadCommandManager", new HashMap<String, Object>()));
        }
    }

    private String storeLastMessage(String messageKey) {
        lastMessage = getMessage(messageKey);
        return lastMessage;
    }

    private String getMessage(String messageKey) {
    	String message;
		try {
			message = messages.getMessage(messageKey, null, null);
		} catch (NoSuchMessageException e) {
			message = messageKey;
		}
		return message;
    }
    
    private boolean willNotReload(AjaxResponse r) {
        return !r.render().contains("<redirect>") && !r.render().contains("reload();");
    }
    
    public String getLastMessage() {
        return lastMessage;
    }
    
    public void clearLastMessage() {
        lastMessage = "";
    }

    public ResourceBundleMessageSource getMessages() {
        return messages;
    }

    public void setMessages(ResourceBundleMessageSource messages1) {
        CommandManager.messages = messages1;
    }
}
