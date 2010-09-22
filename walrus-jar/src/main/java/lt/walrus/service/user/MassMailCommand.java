package lt.walrus.service.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lt.walrus.model.WalrusUser;

public class MassMailCommand implements Serializable{
	private static final long serialVersionUID = -4281011210420749179L;
	List<WalrusUser> users;
	String subject;
	String text;
	
	public MassMailCommand() {
		users = new ArrayList<WalrusUser>();
	}
	
	public List<WalrusUser> getUsers() {
		return users;
	}
	public void setUsers(List<WalrusUser> users) {
		this.users = users;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
}
