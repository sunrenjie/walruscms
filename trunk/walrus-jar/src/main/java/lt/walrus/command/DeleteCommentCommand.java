package lt.walrus.command;

import java.util.HashMap;

import lt.walrus.model.Comment;
import lt.walrus.service.WalrusService;

import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxResponseImpl;
import org.springmodules.xt.ajax.action.ExecuteJavascriptFunctionAction;

public class DeleteCommentCommand extends Command {

	private static final long serialVersionUID = 1L;
	private WalrusService service;
	private Comment comment;
	private int commentIndex;

	public DeleteCommentCommand(WalrusService service, Comment comment) {
		this.service = service;
		this.comment = comment;
		this.commentIndex = comment.getRubric().getComments().indexOf(comment);
	}
	
	@Override
	public AjaxResponse execute() {
		service.delete(comment);
		return makeReloadResponse();
	}

	private AjaxResponse makeReloadResponse() {
		AjaxResponseImpl response = new AjaxResponseImpl("UTF-8");
		response.addAction(new ExecuteJavascriptFunctionAction("reload", new HashMap<String, Object>()));
		return response;
	}

	@Override
	public AjaxResponse redo() {
		return execute();
	}

	@Override
	public AjaxResponse undo() {
		service.addComment(comment, commentIndex);
		return makeReloadResponse();
	}

}
