package lt.walrus.ajax;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import lt.walrus.command.DeleteCommentCommand;
import lt.walrus.command.DeleteSiteCommand;
import lt.walrus.command.article.PublishArticleCommand;
import lt.walrus.command.article.UnpublishArticleCommand;
import lt.walrus.command.rubric.ChangeRubricModeCommand;
import lt.walrus.command.rubric.DeleteRubricCommand;
import lt.walrus.command.rubric.NewSubRubricCommand;
import lt.walrus.model.Box;
import lt.walrus.model.Rubric;
import lt.walrus.model.RubricBox;
import lt.walrus.model.Site;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.security.context.SecurityContextHolder;
import org.springmodules.xt.ajax.AjaxEvent;
import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxResponseImpl;
import org.springmodules.xt.ajax.action.ExecuteJavascriptFunctionAction;

public class RubricActionsHandler extends SaveFieldHandler {

	public AjaxResponse setRubricMode(AjaxEvent e) {
		Rubric currRubric = getCurrRubric(e);
		try {
			return commandManager.execute(new ChangeRubricModeCommand(service, currRubric, Enum.valueOf(Rubric.Mode.class, e.getParameters().get("mode"))));
		}catch (Exception ex) {
			return makeErrorResponse("Jei matote šį klaidos pranešimą, reiškia programuotojai padarė klaidą. Nedelsiant praneškite programuotojams, kokiu būdu jūs gavote šią klaidą. " + ex.getMessage());
		}
	}

	public AjaxResponse newSubRubric(AjaxEvent e) {
		Rubric currRubric = getCurrRubric(e);
		
		try {
			return commandManager.execute(new NewSubRubricCommand(service, currRubric, e.getParameters().get("text")));
		}catch (Exception ex) {
			return makeErrorResponse("Jei matote šį klaidos pranešimą, reiškia programuotojai padarė klaidą. Nedelsiant praneškite programuotojams, kokiu būdu jūs gavote šią klaidą. " + ex.getMessage());
		}
	}

	public AjaxResponse deleteRubric(AjaxEvent e) {
		Rubric currRubric = getCurrRubric(e);
		Rubric delRubric = service.getRubric(e.getParameters().get("deleteRubricId"));
		
		for (Box b : getSite(e).getBoxes()) {
			if (b instanceof RubricBox) {
				if (((RubricBox) b).getRubric().equals(delRubric)) {
					return makeErrorResponse("Negalite trinti šios rubrikos, nes ji yra susieta su struktūriniu svetainės elementu - puslapio \"dėžute\".");
				}
			}
		}
		
		if (null != delRubric) {
			try {
				return commandManager.execute(new DeleteRubricCommand(service, currRubric, delRubric));
			}catch (Exception ex) {
				return makeErrorResponse("Jei matote šį klaidos pranešimą, reiškia programuotojai padarė klaidą. Nedelsiant praneškite programuotojams, kokiu būdu jūs gavote šią klaidą. " + ex.getMessage());
			}
		} else {
			return makeNoopResponse();
		}
	}
	
	public AjaxResponse deleteComment(AjaxEvent e) {
		return commandManager.execute(new DeleteCommentCommand(service, service.getComment(e.getParameters().get("commentId"))));
	}
	
	public AjaxResponse deleteSite(AjaxEvent e) {
		Site site = service.getSiteById(e.getParameters().get("siteId"));
		if (null != site) {
			try {
				return commandManager.execute(new DeleteSiteCommand(service, site));
			}catch (Exception ex) {
				return makeErrorResponse("Jei matote šį klaidos pranešimą, reiškia programuotojai padarė klaidą. Nedelsiant praneškite programuotojams, kokiu būdu jūs gavote šią klaidą. " + ex.getMessage());
			}
		} else {
			return makeNoopResponse();
		}
	}

    public AjaxResponse publishArticle(AjaxEvent e) {
		try {
			return commandManager.execute(new PublishArticleCommand(service, getRubric(e)));
		}catch (Exception ex) {
			return makeErrorResponse("Jei matote šį klaidos pranešimą, reiškia programuotojai padarė klaidą. Nedelsiant praneškite programuotojams, kokiu būdu jūs gavote šią klaidą. " + ex.getMessage());
		}
    }

    private Rubric getRubric(AjaxEvent e) {
    	String rubricId = e.getParameters().get("rubricId");
        Rubric rubric = service.getRubric(rubricId);
        return rubric;
	}

	public AjaxResponse unpublishArticle(AjaxEvent e) {
		try {
			return commandManager.execute(new UnpublishArticleCommand(service, getRubric(e)));
		}catch (Exception ex) {
			return makeErrorResponse("Jei matote šį klaidos pranešimą, reiškia programuotojai padarė klaidą. Nedelsiant praneškite programuotojams, kokiu būdu jūs gavote šią klaidą. " + ex.getMessage());
		}
    }

	public AjaxResponse setLeaf(AjaxEvent e) {
		Rubric rubric = getRubric(e);
		rubric.setLeaf("true".equals(e.getParameters().get("leaf")));
		service.save(rubric);
		return makeExecuteJavascriptResponse("reloadMenu");
	}

	private AjaxResponse makeExecuteJavascriptResponse(String function) {
		AjaxResponseImpl response = new AjaxResponseImpl("UTF-8");
		addCommandExecutedAction(response);
		response.addAction(new ExecuteJavascriptFunctionAction(function, new HashMap<String, Object>()));
		return response;
	}
	
	public AjaxResponse setCommentsAllowed(AjaxEvent e) {
		Rubric rubric = getRubric(e);
		rubric.setCommentsAllowed("true".equals(e.getParameters().get("commentsAllowed")));
		service.save(rubric);
		return makeExecuteJavascriptResponse("reload");
	}

	private void addCommandExecutedAction(AjaxResponseImpl response) {
		response.addAction(new ExecuteJavascriptFunctionAction("commandExecuted", new HashMap<String, Object>()));
	}
	
	public AjaxResponse setVisibleForever(AjaxEvent e) {
		Rubric rubric = getRubric(e);
		rubric.setVisibleForever("true".equals(e.getParameters().get("visible")));
		service.save(rubric);
		AjaxResponseImpl response = new AjaxResponseImpl("UTF-8");
		addCommandExecutedAction(response);
		return response;
	}

	private Date parseDate(AjaxEvent e) throws ParseException {
		return DateUtils.parseDate(e.getParameters().get("date"), new String[] {"yyyy-MM-dd"});
	}
	
	public AjaxResponse setVisibleFrom(AjaxEvent e) {
		AjaxResponseImpl response = new AjaxResponseImpl("UTF-8");
		try {
			Rubric rubric = getRubric(e);
			Date from = parseDate(e);
			
			if (datesAreFine(from, rubric.getVisibleTo())) {
				rubric.setVisibleFrom(from);
				service.save(rubric);
			} else {
				AjaxResponse r = addErrorMessage(response, "Straipsnio matomumo pradžios data vėlesnė nei pabaigos data!");
				HashMap<String, Object> args = new HashMap<String, Object>();
				args.put("fieldId", "visibleFrom_" + rubric.getId());
				args.put("date", new SimpleDateFormat("yyyy-MM-dd").format(rubric.getVisibleFrom()));
				r.addAction(new ExecuteJavascriptFunctionAction("restoreDate", args));

				return r;
			}
			
		} catch (ParseException e1) {
			logger.warn("while parsing start date: ", e1);
			return addErrorMessage(response, "Nekorektiška straipsnio matomumo pradžios data");
		}
		addCommandExecutedAction(response);
		return response;
	}

	public AjaxResponse setVisibleTo(AjaxEvent e) {
		AjaxResponseImpl response = new AjaxResponseImpl("UTF-8");
		try {
			Rubric rubric = getRubric(e);
			Date to = parseDate(e);
			
			if (datesAreFine(rubric.getVisibleFrom(), to)) {
				rubric.setVisibleTo(to);
				service.save(rubric);
			} else {
				AjaxResponse r = addErrorMessage(response, "Straipsnio matomumo pabaigos data ankstesnė nei pradžios data!");
				HashMap<String, Object> args = new HashMap<String, Object>();
				args.put("fieldId", "visibleTo_" + rubric.getId());
				args.put("date", new SimpleDateFormat("yyyy-MM-dd").format(rubric.getVisibleTo()));
				r.addAction(new ExecuteJavascriptFunctionAction("restoreDate", args));
				return r;
			}
		} catch (ParseException e1) {
			logger.warn("while parsing end date: ", e1);
			return addErrorMessage(response, "Nekorektiška straipsnio matomumo pabaigos data");
		}
		addCommandExecutedAction(response);
		return response;
	}
	
    private boolean datesAreFine(Date from, Date to) {
    	return 	(null != from && null != to) &&
    			(from.before(to) || from.equals(to));
    }

	
	private AjaxResponse makeNoopResponse() {
		AjaxResponse r = new AjaxResponseImpl();
		r.addAction(new ExecuteJavascriptFunctionAction("noop", new HashMap<String, Object>()));
		return null;
	}

	private Rubric getCurrRubric(AjaxEvent e) {
		Rubric currRubric = service.getRubric(e.getParameters().get("currentRubricId"));
		return currRubric;
	}
}
