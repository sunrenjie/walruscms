package lt.walrus.service.log;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class UsageReportCommand implements Serializable {
	private static final long serialVersionUID = -250604548391607043L;
	public UsageReportCommand(){
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -1);
		
		endDate = Calendar.getInstance().getTime();
		startDate = c.getTime();
	}
	
	Date startDate;
	Date endDate;
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
