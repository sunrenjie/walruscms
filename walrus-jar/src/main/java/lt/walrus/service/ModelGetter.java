package lt.walrus.service;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import lt.walrus.controller.RubricController;
import lt.walrus.model.Site;

import org.springframework.webflow.context.ExternalContext;

/**
 * Helper class to provide webflow context with the model that is created in MVC
 * controllers
 */
public class ModelGetter {

	private RubricController rubricController;

	/**
	 * @return model for view
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> getModel(ExternalContext context) {
		return rubricController.makeModel((HttpServletRequest) context.getNativeRequest());
	}

	/**
	 * @return site specific to request
	 */
	public Site getSite(ExternalContext context) {
		return rubricController.getSite((HttpServletRequest) context.getNativeRequest());
	}
	
	public RubricController getRubricController() {
		return rubricController;
	}

	public void setRubricController(RubricController rubricController) {
		this.rubricController = rubricController;
	}  
}
