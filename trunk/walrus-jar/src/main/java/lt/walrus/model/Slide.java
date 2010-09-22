package lt.walrus.model;

import java.io.Serializable;
import java.util.UUID;

public class Slide implements Serializable, Comparable<Slide> {
	private static final long serialVersionUID = 759730663422523688L;
	
	private String id;
	private String title;
	private String body;
	private int orderno;

	public Slide() {
		id = UUID.randomUUID().toString();
		body = "Nauja skaidrė. Spauskite čia ir redaguokite.";
		title = "Nauja skaidrė. Spauskite čia ir redaguokite.";
		orderno = 0;
	}
	
	@Override
	public String toString() {
		return "SLIDE[" + id + ", " + orderno + ", " + title +  "]";
	}
	
	public Slide(String slideId) {
		this();
		setId(slideId);
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getBody() {
		return body;
	}
	
	public Slide clone() {
		Slide ret = new Slide();
		ret.setId(getId());
		ret.setTitle(getTitle());
		ret.setBody(getBody());
		ret.setOrderno(orderno);
		
		return ret;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Slide other = (Slide) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public void setOrderno(int order) {
		this.orderno = order;
	}

	public int getOrderno() {
		return orderno;
	}

	public int compareTo(Slide o) {
		if (null == o) {
			return 1;
		}
		
		return orderno - o.getOrderno();
	}

}
