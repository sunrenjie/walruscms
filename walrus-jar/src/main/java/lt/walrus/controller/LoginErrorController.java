package lt.walrus.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

@Controller("loginErrorController")
public class LoginErrorController extends AbstractController {

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("login_error", Boolean.TRUE);

		return new ModelAndView("login", model);
	}
}
