package lt.walrus.ajax;

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
import lt.walrus.controller.util.SiteResolver;
import lt.walrus.model.Rubric;
import lt.walrus.model.Slide;
import lt.walrus.model.SlideshowBox;
import lt.walrus.model.TextBox;
import lt.walrus.service.BoxService;
import lt.walrus.service.RubricService;
import lt.walrus.service.SiteService;
import lt.walrus.service.SlideService;
import lt.walrus.undo.CommandManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springmodules.xt.ajax.AbstractAjaxHandler;
import org.springmodules.xt.ajax.AjaxEvent;
import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxSubmitEvent;

public class SaveFieldHandler extends AbstractAjaxHandler {
	protected org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());

	@Autowired
	protected CommandManager commandManager;
	@Autowired
	protected RubricService rubricService;
	@Autowired
	protected SiteResolver siteResolver;
	@Autowired
	protected BoxService boxService;
	@Autowired
	protected SiteService siteService;
	@Autowired
	protected SlideService slideService;

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
					response = commandManager.execute(new SaveRubricTitleCommand(rubricService, rubricService.get(Long.valueOf(entity.getId())), newValue));
				} else if (entity.isField("body")) {
					response = commandManager.execute(new SaveArticleBodyCommand(rubricService, rubricService.get(Long.valueOf(entity.getId())), newValue));
				} else if (entity.isField("abstract")) {
					response = commandManager.execute(new SaveArticleAbstractCommand(rubricService, rubricService.get(Long.valueOf(entity.getId())), newValue));
				} else if (entity.isField("date")) {
					response = commandManager.execute(new SaveArticleDateCommand(rubricService, rubricService.get(Long.valueOf(entity.getId())), newValue));
				} else if (entity.isField("url")) {
					Rubric rubric = rubricService.get(Long.valueOf(entity.getId()));
					if ("".equals(newValue.trim())) {
						newValue = null;
					} else {
						Rubric existent = rubricService.getRubric(siteResolver.getSite(e), newValue);
						if (null != existent && existent.getId() != rubric.getId()) {
							return AjaxErrorMaker.makeErrorResponse("This static URL is already assigned to rubric \"" + existent.getTitle() + "\"", entity,
									("".equals(rubric
									.getUrl())
									|| null == rubric.getUrl() ? "Click here" : rubric.getUrl()));
						}
					}
					response = commandManager.execute(new SaveRubricUrlCommand(rubricService, rubric, newValue));
				}
			} else if (entity.isEntity("box")) {
				if (entity.isField("title")) {
					response = commandManager.execute(new SaveBoxTitleCommand(boxService, (TextBox) siteResolver.getSite(e).getBox(entity.getId()), newValue));
				} else if (entity.isField("body")) {
					response = commandManager.execute(new SaveBoxBodyCommand(boxService, (TextBox) siteResolver.getSite(e).getBox(entity.getId()), newValue));
				}
			} else if (entity.isEntity("site")) {
				if (entity.isField("title")) {
					response = commandManager.execute(new SaveSiteTitleCommand(siteService, siteResolver.getSite(e), newValue));
				}
			} else if (entity.isEntity("slide")) {
				SlideshowBox slideshow = siteResolver.getSite(e).findSlideshow(Long.valueOf(entity.getId()));
				Slide slide = slideshow.getSlide(Long.valueOf(entity.getId()));
				if (entity.isField("body")) {
					response = commandManager.execute(new SaveSlideBodyCommand(slideService, slide, newValue));
				} else if (entity.isField("title")) {
					response = commandManager.execute(new SaveSlideTitleCommand(slideService, slide, newValue));
				} else if (entity.isField("shortcut")) {
					response = commandManager.execute(new SaveSlideOrderCommand(slideService, boxService, slideshow, slide, newValue));
				} else {
					logger.warn("UNKNOWN SLIDE OPERATION: " + entity);
				}
			}
		} catch (Exception ex) {
			response = AjaxErrorMaker.makeErrorResponse(
					"Jei matote šį klaidos pranešimą, reiškia programuotojai padarė klaidą. Nedelsiant praneškite programuotojams, kokiu būdu jūs gavote šią klaidą. "
							+ ex.getMessage(), entity, "nesėkmė :(");
		}

		if (null == response) {
			response = AjaxErrorMaker.makeErrorResponse("Nežinau, kaip išsaugoti esybės '" + entity.getEntity() + "' lauką '" + entity.getField() + "'");
		}
		return response;
	}

	public void setCommandManager(CommandManager commandManager) {
		this.commandManager = commandManager;
	}

	public void setRubricService(RubricService rubricService) {
		this.rubricService = rubricService;
	}

	public void setSiteResolver(SiteResolver siteResolver) {
		this.siteResolver = siteResolver;
	}

	public void setBoxService(BoxService boxService) {
		this.boxService = boxService;
	}

	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}

	public void setSlideService(SlideService slideService) {
		this.slideService = slideService;
	}
}
