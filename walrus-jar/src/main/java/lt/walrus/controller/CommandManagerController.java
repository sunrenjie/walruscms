package lt.walrus.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

public class CommandManagerController extends RubricController {
    
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse arg1) throws Exception {
        HashMap<String, Object> model = new HashMap<String, Object>();
        model.put("commandManager", commandManager);

        return new ModelAndView("_commandManager", "model", model);
    }
}
