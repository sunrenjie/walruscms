package lt.walrus.command.rubric;

import java.util.HashMap;

import lt.walrus.ajax.WalrusRedirectAction;
import lt.walrus.command.Command;
import lt.walrus.model.Rubric;
import lt.walrus.model.Rubric.Mode;
import lt.walrus.service.RubricService;

import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxResponseImpl;

public class ChangeRubricModeCommand extends Command {

    private Mode newMode;
    private Mode previousMode;
    private Rubric currRubric;
	private RubricService service;

    public ChangeRubricModeCommand(RubricService service1, Rubric currRubric1, Mode mode1) {
    	service = service1;
        currRubric = currRubric1;
        newMode = mode1;
        previousMode = currRubric1.getMode();
    }

    @Override
    public AjaxResponse execute() {
        currRubric.setMode(newMode);
        service.save(currRubric);
        return addReloadAction();
    }

    private AjaxResponse addReloadAction() {
        AjaxResponse response = new AjaxResponseImpl("UTF-8");
        HashMap<String, String> p = new HashMap<String, String>();
        if(null != currRubric) {
			p.put("rubricId", String.valueOf(currRubric.getId()));
        }
        response.addAction(new WalrusRedirectAction("index", p));        
        return response;
    }

    public AjaxResponse redo() {
        return execute();
    }

    public AjaxResponse undo() {
        currRubric.setMode(previousMode);
        service.save(currRubric);
        return addReloadAction();
    }
}
