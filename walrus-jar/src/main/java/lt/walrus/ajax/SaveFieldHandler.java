package lt.walrus.ajax;

import java.util.HashMap;

import lt.walrus.command.SaveBoxBodyCommand;
import lt.walrus.command.SaveBoxTitleCommand;
import lt.walrus.command.SaveSiteTitleCommand;
import lt.walrus.command.SaveSlideBodyCommand;
import lt.walrus.command.SaveSlideOrderCommand;
import lt.walrus.command.SaveSlideTitleCommand;
import lt.walrus.command.article.SaveArticleAbstractCommand;
import lt.walrus.command.article.SaveArticleBodyCommand;
import lt.walrus.command.article.SaveArticleDateCommand;
import lt.walrus.command.rubric.SaveRubricTitleCommand;
import lt.walrus.command.rubric.SaveRubricUrlCommand;
import lt.walrus.controller.SaveFieldCommand;
import lt.walrus.model.Rubric;
import lt.walrus.model.Slide;
import lt.walrus.model.SlideshowBox;
import lt.walrus.model.TextBox;

import org.springmodules.xt.ajax.AjaxEvent;
import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxResponseImpl;
import org.springmodules.xt.ajax.AjaxSubmitEvent;
import org.springmodules.xt.ajax.action.ExecuteJavascriptFunctionAction;

public class SaveFieldHandler extends AbstractWalrusAjaxHandler {
	protected org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());

	public AjaxResponse saveBody(AjaxSubmitEvent e) {
		SaveFieldCommand c = (SaveFieldCommand) e.getCommandObject();
		return saveFieldSrsly(e, c.getText());
	}

	public AjaxResponse saveField(AjaxEvent e) {
		return saveFieldSrsly(e, e.getParameters().get("text"));
	}

	protected AjaxResponse saveFieldSrsly(AjaxEvent e, String newValue) {
		AjaxResponse response = null;
		EditedEntity entity = new EditedEntity(e);

		logger.debug("entity:" + entity);

		try {
			if (entity.isEntity("rubric")) {
				if (entity.isField("title")) {
					response = commandManager.execute(new SaveRubricTitleCommand(service, service.getRubric(Long.valueOf(entity.getId())), newValue));
				} else if (entity.isField("body")) {
					response = commandManager.execute(new SaveArticleBodyCommand(service, service.getRubric(Long.valueOf(entity.getId())), newValue));
				} else if (entity.isField("abstract")) {
					response = commandManager.execute(new SaveArticleAbstractCommand(service, service.getRubric(Long.valueOf(entity.getId())), newValue));
				} else if (entity.isField("date")) {
					response = commandManager.execute(new SaveArticleDateCommand(service, service.getRubric(Long.valueOf(entity.getId())), newValue));
				} else if (entity.isField("url")) {
					Rubric rubric = service.getRubric(Long.valueOf(entity.getId()));
					if ("".equals(newValue.trim())) {
						newValue = null;
					} else {
						Rubric existent = service.getRubric(getSite(e), newValue);
						if (null != existent && existent.getId() != rubric.getId()) {
							return makeErrorResponse("This static URL is already assigned to rubric \"" + existent.getTitle() + "\"", entity, ("".equals(rubric
									.getUrl())
									|| null == rubric.getUrl() ? "Click here" : rubric.getUrl()));
						}
					}
					response = commandManager.execute(new SaveRubricUrlCommand(service, rubric, newValue));
				}
			} else if (entity.isEntity("box")) {
				if (entity.isField("title")) {
					response = commandManager.execute(new SaveBoxTitleCommand(service, (TextBox) getSite(e).getBox(entity.getId()), newValue));
				} else if (entity.isField("body")) {
					response = commandManager.execute(new SaveBoxBodyCommand(service, (TextBox) getSite(e).getBox(entity.getId()), newValue));
				}
			} else if (entity.isEntity("site")) {
				if (entity.isField("title")) {
					response = commandManager.execute(new SaveSiteTitleCommand(service, getSite(e), newValue));
				}
			} else if (entity.isEntity("slide")) {
				SlideshowBox slideshow = getSite(e).findSlideshow(Long.valueOf(entity.getId()));
				Slide slide = slideshow.getSlide(Long.valueOf(entity.getId()));
				if (entity.isField("body")) {
					response = commandManager.execute(new SaveSlideBodyCommand(service, slide, newValue));
				} else if (entity.isField("title")) {
					response = commandManager.execute(new SaveSlideTitleCommand(service, slide, newValue));
				} else if (entity.isField("shortcut")) {
					response = commandManager.execute(new SaveSlideOrderCommand(service, slideshow, slide, newValue));
				} else {
					logger.warn("UNKNOWN SLIDE OPERATION: " + entity);
				}
			}
		} catch (Exception ex) {
			response = makeErrorResponse(
					"Jei matote šį klaidos pranešimą, reiškia programuotojai padarė klaidą. Nedelsiant praneškite programuotojams, kokiu būdu jūs gavote šią klaidą. "
							+ ex.getMessage(), entity, "nesėkmė :(");
		}

		if (null == response) {
			response = makeErrorResponse("Nežinau, kaip išsaugoti esybės '" + entity.getEntity() + "' lauką '" + entity.getField() + "'");
		}
		return response;
	}

	protected AjaxResponse makeErrorResponse(String message, EditedEntity entity, String oldValue) {
		AjaxResponse response = makeErrorResponse(message);
		HashMap<String, Object> p = new HashMap<String, Object>();
		p.put("elementId", entity.getElementId());
		p.put("value", oldValue == null ? "" : oldValue);
		response.addAction(new ExecuteJavascriptFunctionAction("restoreValue", p));
		return response;
	}

	@Override
	protected AjaxResponse makeErrorResponse(String message) {
		AjaxResponse response = new AjaxResponseImpl("UTF-8");
		HashMap<String, Object> p = new HashMap<String, Object>();
		p.put("msg", message);
		response.addAction(new ExecuteJavascriptFunctionAction("displayError", p));
		return response;
	}

}
