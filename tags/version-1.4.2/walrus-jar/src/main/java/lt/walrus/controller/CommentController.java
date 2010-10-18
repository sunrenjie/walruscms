package lt.walrus.controller;

import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lt.walrus.controller.editors.CommentEditor;
import lt.walrus.model.Comment;
import lt.walrus.model.Rubric;
import lt.walrus.service.WalrusService;

import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractCommandController;

public class CommentController extends AbstractCommandController {

	private WalrusService service;
	private RubricController rubricController;

	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		super.initBinder(request, binder);
		binder.registerCustomEditor(Rubric.class, "rubric", new CommentEditor(service));
	}

	@Override
	protected ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

		ModelMap model = rubricController.makeModel(request);
		ModelAndView mav = new ModelAndView();
		mav.addObject("model", model);
		Comment comment = (Comment) command;
		mav.addObject("comment", comment);

		if (errors.hasErrors()) {
			mav.addAllObjects(errors.getModel());
			mav.setViewName("commentForm");
			return mav;
		}

		if (null == comment.getRubric().getComments()) {
			comment.getRubric().setComments(new ArrayList<Comment>());
		}
		comment.setDate(Calendar.getInstance().getTime());
		service.addComment(comment, 0);
		return mav;
	}

	public WalrusService getService() {
		return service;
	}

	public void setService(WalrusService service) {
		this.service = service;
	}

	public RubricController getRubricController() {
		return rubricController;
	}

	public void setRubricController(RubricController rubricController) {
		this.rubricController = rubricController;
	}

}
