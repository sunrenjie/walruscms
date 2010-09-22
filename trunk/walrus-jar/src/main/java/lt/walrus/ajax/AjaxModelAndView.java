package lt.walrus.ajax;

import org.springframework.validation.BindException;

public class AjaxModelAndView extends org.springmodules.xt.ajax.web.servlet.AjaxModelAndView{

	public AjaxModelAndView(String viewName, BindException errors) {
		super(viewName, errors);
	}
	
	@Override
	public boolean isEmpty() {
		return (getView() == null && getViewName() == null && (getModelInternal() == null || getModelInternal().isEmpty())); 
	}
}
