package lt.walrus.command.article;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import lt.walrus.model.Rubric;
import lt.walrus.service.RubricService;

import org.springframework.util.StringUtils;
import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxResponseImpl;
import org.springmodules.xt.ajax.action.ExecuteJavascriptFunctionAction;

public class SaveArticleDateCommand extends AbstractArcticleFieldCommand {

	public SaveArticleDateCommand(RubricService service, Rubric context1, String text) {
		super(service, context1, text);
	}

	@Override
	protected String getPreviousValueFromContext(Object context1) {
		return ((Rubric) context).getDate();
	}

	@Override
	protected void setValueToContext(String val) {
		((Rubric) context).setDate(val);
	}
	public static final DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
	
    @Override
    public AjaxResponse execute() {
    	boolean isDate = false;
    	try {
			fmt.parse(newValue);
			isDate = true;
		} catch (ParseException e) {
			logger.debug(e);
		}
		
		AjaxResponse r = new AjaxResponseImpl("UTF-8");
		// priimam korektišką datą arba tuščią reikšmę
		if (isDate || ! StringUtils.hasText(newValue)) {
	        setValueToContext(newValue);
	        service.save(context);
	
	        addActionAfterExecute(r);
	        return r;
		} else {
			HashMap<String,Object> params = new HashMap<String, Object>();
			params.put("msg", "Nekorektiška data!");
			r.addAction(new ExecuteJavascriptFunctionAction("displayError", params));
			addActionAfterUndo(r);
			
			return r;
		}
    }
	
}
