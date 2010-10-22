package lt.walrus.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Comment for rubric
 */
public class Comment implements Serializable {
	private static final long serialVersionUID = -4117472004424399495L;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Comment other = (Comment) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}

	public Comment clone() throws CloneNotSupportedException {
		Comment c = new Comment();
		c.setBody(body);
		c.setDate(date);
		c.setEmail(email);
		// c.setId(id);
		c.setName(name);
		c.setRubric(rubric);
		c.setWebsite(website);
		return c;
	}
	private long id;

	/**
	 * Name of the commenter
	 */
	private String name;
	private String email;
	private String website;
	private String body;
	private Date date;
	private Rubric rubric;

	public Comment() {
		date = Calendar.getInstance().getTime();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Rubric getRubric() {
		return rubric;
	}

	public void setRubric(Rubric rubric) {
		this.rubric = rubric;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
