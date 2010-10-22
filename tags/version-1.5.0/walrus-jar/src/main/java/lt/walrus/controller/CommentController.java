package lt.walrus.controller;

import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lt.walrus.controller.editors.CommentEditor;
import lt.walrus.controller.util.ModelMaker;
import lt.walrus.model.Comment;
import lt.walrus.model.Rubric;
import lt.walrus.service.CommentService;
import lt.walrus.service.RubricService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractCommandController;

// @Controller("commentController")
public class CommentController extends AbstractCommandController {
	@Autowired
	private RubricService rubricService;
	@Autowired
	private CommentService commentService;
	@Autowired
	private ModelMaker modelMaker;

	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
		super.initBinder(request, binder);
		binder.registerCustomEditor(Rubric.class, "rubric", new CommentEditor(rubricService));
	}

	@Override
	protected ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

		ModelMap model = modelMaker.makeModel(request);
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
		commentService.add(comment);
		return mav;
	}

	public void setRubricService(RubricService service) {
		this.rubricService = service;
	}

	public void setCommentService(CommentService commentService) {
		this.commentService = commentService;
	}

	public void setModelMaker(ModelMaker modelMaker) {
		this.modelMaker = modelMaker;
	}
}
