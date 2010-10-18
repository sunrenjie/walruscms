package lt.walrus.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;

/**
 * The main content holder. The website is composed of Rubric tree.
 */
public class Rubric implements Serializable {
	private static final long serialVersionUID = 1L;
	protected static transient org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Rubric.class);

	/**
	 * The rubric display mode
	 */
	public enum Mode {
		/**
		 * Children of rubric are not displayed
		 */
		NONE,
		/**
		 * Children of rubric are displayed in simple list
		 */
		SIMPLE_LIST,
		/**
		 * Children of rubric are displayed in a list where child abstracts are
		 * visible
		 */
		EXPANDED_LIST,
		/**
		 * Children are displayed in blog mode - full child body is visible
		 */
		BLOG
	}

	/**
	 * Id of rubric
	 */
	private long id;
	/**
	 * Parent rubric (might be null for root rubric)
	 */
	private Rubric parent;
	/**
	 * Child rubrics
	 */
	List<Rubric> children;
	/**
	 * Title of rubric
	 */
	private String title;

	/**
	 * Abstract of rubric
	 */
	private String abstr = "";
	/**
	 * Text body of rubric
	 */
	private String body = "";
	/**
	 * The date string
	 */
	private String date = "";
	/**
	 * Is rubric available for visitors to see (depending on time related
	 * factors)
	 */
	private boolean online = false;

	/**
	 * When false, time restrictions visibleFrom visibleTo apply
	 */
	private boolean visibleForever;
	/**
	 * Date rubric is visible to visitors from (when visibleForever == false)
	 */
	private Date visibleFrom;
	/**
	 * Date rubric is visible to visitors to (when visibleForever == false)
	 */
	private Date visibleTo;

	/**
	 * Is rubric a leaf - ie true when rubric can not have children
	 */
	private boolean leaf = false;

	/**
	 * Rubric display mode
	 */
	private Mode mode = Mode.SIMPLE_LIST;
	/**
	 * Order no in parent child list
	 */
	private int orderno;
	/**
	 * Special url string for rubric
	 */
	private String url;

	/**
	 * Comments to the rubric
	 */
	private List<Comment> comments;
	/**
	 * Is commenting turned on for rubric
	 */
	private boolean commentsAllowed = true;

	public boolean isCommentsAllowed() {
		return commentsAllowed;
	}

	public void setCommentsAllowed(boolean commentsAllowed) {
		this.commentsAllowed = commentsAllowed;
	}

	public Rubric() {
		children = new ArrayList<Rubric>();

		date = new SimpleDateFormat("yyyy-MM-dd").format(GregorianCalendar.getInstance().getTime());
		visibleForever = true;
		visibleFrom = GregorianCalendar.getInstance().getTime();
		visibleTo = DateUtils.addDays(visibleFrom, 180);

		parent = null;
	}

	public Rubric(String title1) {
		this();
		setTitle(title1);
	}

	public Rubric(Rubric parent1, String title1) {
		this(title1);
		this.parent = parent1;
		if (null != parent) {
			if (null != parent.getChildren()) {
				parent.getChildren().add(this);
			}
		}
	}

	public Rubric(Rubric parent1, String title1, Mode mode1) {
		this(parent1, title1);
		setMode(mode1);
	}

	public Rubric(String title1, Mode mode1) {
		this(title1);
		setMode(mode1);
	}

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode1) {
		this.mode = mode1;
	}

	public Rubric getParent() {
		return parent;
	}

	public void setParent(Rubric parent1) {
		if (parent == parent1)
			return;
		this.parent = parent1;
	}

	/**
	 * Adds a child to rubric at specified index. If the specified child already
	 * exists, no action is taken. Shifts children currently at index position
	 * and all subsequent elements right.
	 * 
	 * @param child
	 * @param index
	 */
	public void addChild(Rubric child, int index) {
		logger.debug("Rubric.addChild: " + child.getTitle() + " to " + getTitle() + " to index: " + index);
		if (!children.contains(child)) {
			if (null != child.getParent()) {
				child.getParent().deleteChild(child);
			}
			children.add(index, child);
			child.setParent(this);
			logger.debug("Rubric.addChild: " + child.getTitle() + " parent is: " + child.getParent().getTitle());
		} else {
			logger.debug("Rubric.addChild: NOPE, already contains...");
		}
	}

	public List<Rubric> getChildren() {
		return children;
	}

	public void setChildren(List<Rubric> children1) {
		this.children = children1;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title1) {
		this.title = title1;
	}

	public int getOrderno() {
		return this.orderno;
	}

	public void setOrderno(int orderno1) {
		this.orderno = orderno1;
	}

	public long getId() {
		return id;
	}

	public void deleteChild(Rubric r) {
		logger.debug("Rubric.deleteChild: " + r.getTitle() + " from " + getTitle());
		children.remove(r);
	}

	public boolean hasChild(Rubric rubric) {
		return children.contains(rubric);
	}

	public void setId(long id1) {
		this.id = id1;
	}

	@Override
	public String toString() {
		return "Rubric[" + id + ", " + title + "]";
	}

	/**
	 * @return count all descendants (children and children of children etc) of
	 *         rubric
	 */
	public int countChildren() {
		int ret = children.size();

		for (Iterator<Rubric> i = children.iterator(); i.hasNext();) {
			ret += i.next().countChildren();
		}

		return ret;
	}

	/**
	 * @return true if any child is archived
	 */
	public boolean hasArchive() {
		for (Iterator<Rubric> i = children.iterator(); i.hasNext();) {
			if (i.next().isArchived()) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @return true if rubric is visibleForever or visible according to date
	 *         restrictions
	 */
	public boolean isVisible() {
		Date now = Calendar.getInstance().getTime();
		return visibleForever || (now.equals(visibleFrom) || now.after(visibleFrom)) && (now.equals(visibleTo) || now.before(visibleTo));
	}

	/**
	 * @return true if rubric is not visibleForever and will become visible in
	 *         future
	 */
	public boolean isPending() {
		return !visibleForever && Calendar.getInstance().getTime().before(visibleFrom);
	}

	/**
	 * @return true if rubric is not visibleForever and was visible by date
	 *         restrictins in past
	 */
	public boolean isArchived() {
		return !visibleForever && Calendar.getInstance().getTime().after(visibleTo);
	}

	/**
	 * @return true if rubric has children that are visible and online
	 */
	public boolean hasVisibleOnlineArticles() {
		for (Iterator<Rubric> i = children.iterator(); i.hasNext();) {
			Rubric article = i.next();
			if (article.isVisible() && article.isOnline()) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Iterates on all descendants and sets order number for every child. Used
	 * by site initialisation from XML. Pereina per vaikų sąrašą ir kiekvienam
	 * vaikui priskiria jo orderno pagal jo indeksą sąraše. Čia reikalinga, kai
	 * saitas yra pakeliamas iš XMLo pirmą kartą ir visų rubrikų orderno būna 0,
	 * tai taip ir paseivina į DB
	 */
	public void initChildrenOrderNos() {
		for (int i = 0; i < children.size(); i++) {
			children.get(i).setOrderno(i);
			children.get(i).initChildrenOrderNos();
		}
	}

	public String getAbstr() {
		return abstr;
	}

	public void setAbstr(String abstr) {
		this.abstr = abstr;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public Date getRubricDate() {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		
		try {
			return fmt.parse(getDate());
		} catch (ParseException e) {
			return null;
		}
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public boolean isVisibleForever() {
		return visibleForever;
	}

	public void setVisibleForever(boolean visibleForever) {
		this.visibleForever = visibleForever;
	}

	public Date getVisibleFrom() {
		return visibleFrom;
	}

	public void setVisibleFrom(Date visibleFrom) {
		this.visibleFrom = visibleFrom;
	}

	public Date getVisibleTo() {
		return visibleTo;
	}

	public void setVisibleTo(Date visibleTo) {
		this.visibleTo = visibleTo;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public Rubric clone() {
		Rubric r = new Rubric();
		r.setAbstr(abstr);
		r.setBody(body);
		List<Rubric> l = new ArrayList<Rubric>();
		for (Rubric rubric : children) {
			Rubric clone = rubric.clone();
			clone.setParent(r);
			l.add(clone);
		}
		r.setChildren(l);
		r.setDate(date);
		r.setLeaf(leaf);
		r.setMode(mode);
		r.setOnline(online);
		r.setOrderno(orderno);
		r.setParent(null);
		r.setTitle(title);
		r.setUrl(url);
		r.setVisibleForever(visibleForever);
		r.setVisibleFrom(visibleFrom);
		r.setVisibleTo(visibleTo);
		return r;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	
	public List<Rubric> getAllChildren() {
		List<Rubric> ret = new ArrayList<Rubric>();
		
		ret.addAll(getChildren());
		for (Rubric child: getChildren()) {
			ret.addAll(child.getAllChildren());
		}
		
		return ret;
	}
	
	/**
	 * return all children of this rubric and their children and their children's children..., sorted by date
	 * @param asc -- when true, old items are first, when false new items are first ,)
	 * @return
	 */
	public List<Rubric> getAllChildrenInChronoOrder(boolean asc) {
		List<Rubric> ret = getAllChildren();
		if (asc) {
			Collections.sort(ret, new RubricDateComparator());
		} else {
			Collections.sort(ret, new RubricDateDescComparator());
		}
		
		return ret;
	}
}

