package lt.walrus.ajax;

import lt.walrus.undo.CommandManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springmodules.xt.ajax.AbstractAjaxHandler;
import org.springmodules.xt.ajax.AjaxEvent;
import org.springmodules.xt.ajax.AjaxResponse;

public class UndoHandler extends AbstractAjaxHandler {
	@Autowired
	private CommandManager commandManager;

    public AjaxResponse undo(AjaxEvent e) {
		return commandManager.undo();
    }

    public AjaxResponse redo(AjaxEvent e) {
		return commandManager.redo();
    }

	public void setCommandManager(CommandManager commandManager) {
		this.commandManager = commandManager;
	}
}
