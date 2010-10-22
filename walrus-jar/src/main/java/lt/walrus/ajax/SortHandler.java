package lt.walrus.ajax;

import java.util.HashMap;

import lt.walrus.controller.util.SiteResolver;
import lt.walrus.model.Rubric;
import lt.walrus.service.RubricService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springmodules.xt.ajax.AbstractAjaxHandler;
import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxResponseImpl;
import org.springmodules.xt.ajax.AjaxSubmitEvent;
import org.springmodules.xt.ajax.action.ExecuteJavascriptFunctionAction;

public class SortHandler extends AbstractAjaxHandler {
	@Autowired
	protected SiteResolver siteResolver;
	@Autowired
	private RubricService rubricService;

	protected org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());
	public static final String IS_TREE_PARAM = "isTree";
	private int maxRubricTreeDepth = 3;
	
	public AjaxResponse saveSort(AjaxSubmitEvent e) {
		AjaxResponse r = new AjaxResponseImpl("UTF-8");
		HashMap<String, Object> hmap = new HashMap<String, Object>();

		String sortedJSON = e.getParameters().get("sorted");
		logger.debug("SortHandler.saveSort: json: " + sortedJSON);

		JSONObject a = JSONObject.fromString(sortedJSON);
		JSONArray tree = a.getJSONArray("tree");
		
		if (isTooDeep(tree, 0, maxRubricTreeDepth)) {
			r = AjaxErrorMaker.makeErrorResponse("Viršytas rubrikų gylis! Maksimalus leidžiamas rubrikų gylis yra " + (maxRubricTreeDepth + 1));
			r.addAction(new ExecuteJavascriptFunctionAction("setTimeout(function(){window.location.reload()}, 2000)", hmap));
			return r;
		}

		processJSONTree2(tree, siteResolver.getSite(e).getRootRubric());
		// currRubric.reorderChildren();
		rubricService.save(siteResolver.getSite(e).getRootRubric());
		hmap.put(IS_TREE_PARAM, "1");
		r.addAction(new ExecuteJavascriptFunctionAction("reloadMenu", hmap));

		return r;
	}
	
	private boolean isTooDeep(JSONArray items, int currDepth, int maxDepth) {
		if (currDepth > maxDepth) {
			return true;
		}
		boolean ret = false;
		for (int i = 0; i < items.length(); i++) {
			JSONObject o = items.getJSONObject(i);
			if (o.has("children")) {
				 ret = ret || isTooDeep(o.getJSONArray("children"), currDepth + 1, maxDepth);
			}
		}
		return ret;
	}

	private void processJSONTree2(JSONArray items, Rubric currRubric) {
		logger.debug("working in rubric: " + currRubric.getTitle());
		int rubricIndex = 0;

		for (int i = 0; i < items.length(); i++) {
			JSONObject o = items.getJSONObject(i);
			String id = o.getString("id");
			logger.debug("exmamining id: " + id);
			if (isRubric(id)) {
				Rubric rubric = rubricService.get(toWalrusId(id));
				if (!currRubric.hasChild(rubric)) {
					rubricService.moveSubrubricToRubric(currRubric, rubric, rubricIndex);
				} else {
					currRubric.getChildren().remove(rubric);
					currRubric.getChildren().add(rubricIndex, rubric);
					//rubric.setOrderno(rubricIndex);
				}
				if (o.has("children")) {
					processJSONTree2(o.getJSONArray("children"), rubric);
				}
				rubricIndex++;
			}
		}
	}

	/**
	 * @param id
	 * @return
	 */
	private long toWalrusId(String id) {
		return Long.valueOf(id.substring(2));
	}

	private boolean isRubric(String id) {
		return id.startsWith("r.");
	}

	public void setMaxRubricTreeDepth(int maxRubricTreeDepth) {
		this.maxRubricTreeDepth = maxRubricTreeDepth;
	}

	public int getMaxRubricTreeDepth() {
		return maxRubricTreeDepth;
	}

	public void setSiteResolver(SiteResolver siteResolver) {
		this.siteResolver = siteResolver;
	}

	public void setRubricService(RubricService rubricService) {
		this.rubricService = rubricService;
	}
}
