package lt.walrus.command;

import org.springmodules.xt.ajax.AjaxResponse;

public interface Undoable {
    AjaxResponse undo();
    AjaxResponse redo();
}
