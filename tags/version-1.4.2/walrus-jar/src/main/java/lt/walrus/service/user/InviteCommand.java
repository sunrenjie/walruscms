package lt.walrus.service.user;

import java.io.Serializable;

public class InviteCommand implements Serializable {
	private static final long serialVersionUID = -5873332899691296624L;
	private String emails;
	private String comment;

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getEmails() {
		return emails;
	}

	public void setEmails(String emails) {
		this.emails = emails;
	}
}
