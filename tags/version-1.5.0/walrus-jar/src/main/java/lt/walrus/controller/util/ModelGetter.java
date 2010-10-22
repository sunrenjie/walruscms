package lt.walrus.controller.util;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import lt.walrus.model.Site;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.webflow.context.ExternalContext;

/**
 * Helper class to provide webflow context with the model that is created in MVC
 * controllers
 */
public class ModelGetter {

	@Autowired
	private SiteResolver siteResolver;
	@Autowired
	private ModelMaker modelMaker;

	/**
	 * @return model for view
	 */
	public HashMap<String, Object> getModel(ExternalContext context) {
		return modelMaker.makeModel((HttpServletRequest) context.getNativeRequest());
	}

	/**
	 * @return site specific to request
	 */
	public Site getSite(ExternalContext context) {
		return siteResolver.getSite((HttpServletRequest) context.getNativeRequest());
	}

	public void setSiteResolver(SiteResolver siteResolver) {
		this.siteResolver = siteResolver;
	}

	public void setModelMaker(ModelMaker modelMaker) {
		this.modelMaker = modelMaker;
	}
}
