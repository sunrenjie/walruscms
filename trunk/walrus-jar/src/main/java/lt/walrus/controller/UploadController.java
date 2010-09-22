package lt.walrus.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springmodules.xt.ajax.web.servlet.AjaxModelAndView;

public class UploadController extends SimpleFormController {
	protected org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());

	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {

		UploadCommand bean = (UploadCommand) command;

		MultipartFile file = bean.getFile();
		if (file == null) {
			logger.debug("UploadController.onSubmit: FILE IZ NULLL!!!!");
		} else {
			logger.debug("UploadController.onSubmit: FILENAME: " + file.getOriginalFilename());
		}

		ModelMap model = new ModelMap();
		model.addAttribute("contextPath", request.getContextPath());
		
		return new AjaxModelAndView("upload", errors, model);	
	}
}
