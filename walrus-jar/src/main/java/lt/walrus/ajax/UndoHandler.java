package lt.walrus.ajax;

import org.springmodules.xt.ajax.AjaxEvent;
import org.springmodules.xt.ajax.AjaxResponse;

public class UndoHandler extends AbstractWalrusAjaxHandler {
    
    public AjaxResponse undo(AjaxEvent e) {
        return commandManager.undo();
    }

    public AjaxResponse redo(AjaxEvent e) {
        return commandManager.redo();
    }
}
