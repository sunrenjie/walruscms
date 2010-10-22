package lt.walrus.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lt.walrus.undo.CommandManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

public class CommandManagerController extends AbstractController {
	@Autowired
	private CommandManager commandManager;
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse arg1) throws Exception {
        HashMap<String, Object> model = new HashMap<String, Object>();
		model.put("commandManager", commandManager);

        return new ModelAndView("_commandManager", "model", model);
    }

	public void setCommandManager(CommandManager commandManager) {
		this.commandManager = commandManager;
	}
}
