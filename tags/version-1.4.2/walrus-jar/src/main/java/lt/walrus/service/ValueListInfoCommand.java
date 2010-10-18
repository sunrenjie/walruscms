package lt.walrus.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.mlw.vlh.ValueListInfo;

/**
 * A command used for communication with value list
 */
public class ValueListInfoCommand implements Serializable {
	private static final long serialVersionUID = 6563070148982048756L;
	/**
	 * Id of chesen item
	 */
	private String itemId;
	/**
	 * Items per page
	 */
	private String pagingNumberPer;
	/**
	 * Current page
	 */
	private String pagingPage;
	/**
	 * Column by which list is sorted
	 */
	private String sortColumn;
	/**
	 * Sort direction
	 */
	private String sortDirection;
	/**
	 * List of selected records. ids of selected items are in the list.
	 */
	private List<String> selectedIds = new ArrayList<String>();

	public ValueListInfoCommand() {
		// EMPTY
	}
	
	public boolean isSelected(String id) {
		return selectedIds.contains(id);
	}
	
	public void toggle(String id) {
		if(isSelected(id)) {
			selectedIds.remove(id);
		} else {
			selectedIds.add(id);
		}
	}

	public String getPagingNumberPer() {
		return pagingNumberPer;
	}

	public void setPagingNumberPer(String pagingNumberPer) {
		this.pagingNumberPer = pagingNumberPer;
	}

	public String getPagingPage() {
		return pagingPage;
	}

	public void setPagingPage(String pagingPage) {
		this.pagingPage = pagingPage;
	}

	public String getSortColumn() {
		return sortColumn;
	}

	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}

	public String getSortDirection() {
		return sortDirection;
	}

	public void setSortDirection(String sortDirection) {
		this.sortDirection = sortDirection;
	}

	public ValueListInfo getValueListInfo() {
		return new ValueListInfo(getValueListInfoParameters());
	}

	protected Map<String, Object> getValueListInfoParameters() {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(ValueListInfo.SORT_COLUMN, getSortColumn());
		parameters.put(ValueListInfo.PAGING_NUMBER_PER, getPagingNumberPer());
		parameters.put(ValueListInfo.PAGING_PAGE, getPagingPage());
		parameters.put(ValueListInfo.SORT_DIRECTION, getSortDirection());
		return parameters;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public List<String> getSelectedIds() {
		return selectedIds;
	}

	public void setSelectedIds(List<String> selectedIds) {
		this.selectedIds = selectedIds;
	}
}
