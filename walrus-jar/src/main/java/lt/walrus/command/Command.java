package lt.walrus.command;

import java.io.Serializable;

import org.springmodules.xt.ajax.AjaxResponse;

public abstract class Command implements Undoable, Serializable {
	protected static transient org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Command.class);

	public abstract AjaxResponse execute();

	public String getExecuteMessage() {
		return getClass().getName() + ".execute";
	}

	public String getUndoMessage() {
		return getClass().getName() + ".undo";
	}

	public String getRedoMessage() {
		return getExecuteMessage();
	}

	public String getUndoTitle() {
		return getClass().getName() + ".undoTitle";
	}

	public String getRedoTitle() {
		return getClass().getName() + ".redoTitle";
	}
}