package lt.walrus.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.util.StringUtils;

/**
 * The Site - the holder of Rubric and Box content. One WalrusCMS installation
 * allows to have several sites that differ by host name and language. I.e. you
 * can have different site for every language and every virtual host.
 */
public class Site implements Serializable, Comparable<Site> {
	private static final long serialVersionUID = 1L;
	protected static transient org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Site.class);

	/**
	 * Site Id
	 */
	private long id;
	/**
	 * Root rubric of site rubric tree
	 */
	private Rubric rootRubric;
	/**
	 * Language code of the site
	 */
	private String language;
	/**
	 * Hostname by which the site is accessible
	 */
	private String host;

	private String hostAliases;
	/**
	 * Title of the site
	 */
	private String title;
	/**
	 * The list of the boxes managed by the site
	 */
	private List<Box> boxes;
	/**
	 * Is site restricted and accessible only to visitors allowed by
	 * {@link IpAddressFilter}
	 */
	private boolean restricted = false;
	/**
	 * Index of site in site list
	 */
	private int index;
	/**
	 * Path of the site template directory
	 */
	private String templatePath = "default";
	
	public Site() {
	}

	public Site(Rubric root) {
		this();
		rootRubric = root;
	}

	public Rubric getRootRubric() {
		return rootRubric;
	}

	public void setRootRubric(Rubric rootRubric1) {
		this.rootRubric = rootRubric1;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public List<Box> getBoxes() {
		return boxes;
	}

	public void setBoxes(List<Box> boxes) {
		this.boxes = boxes;
	}

	public Box getBox(String boxId) {
		if (!StringUtils.hasText(boxId)) {
			return null;
		}
		for (Iterator<Box> i = boxes.iterator(); i.hasNext();) {
			Box box = i.next();
			if (null != box && boxId.trim().equals(box.getBoxId().trim())) {
				return box;
			}
		}
		return null;
	}

	public SlideshowBox findSlideshow(long slideId) {
		for (Box box : boxes) {
			if (!(box instanceof SlideshowBox)) {
				continue;
			}

			if (((SlideshowBox) box).hasSlide(slideId)) {
				return (SlideshowBox) box;
			}
		}

		return null;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}
	
	public Site clone() {
		Site s = new Site();
		s.setBoxes(boxes);
		s.setHost(host);
		s.setLanguage(language);
		s.setRootRubric(rootRubric.clone());
		List<Box> clonedBoxes = new ArrayList<Box>();

		for (Box box : boxes) {
			Box clone = box.clone();
			if (clone instanceof RubricBox) {
				((RubricBox) clone).setRubric(s.getRootRubric());
			}
			clonedBoxes.add(clone);
		}
		return s;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isRestricted() {
		return restricted;
	}

	public void setRestricted(boolean restricted) {
		this.restricted = restricted;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public int compareTo(Site s) {
		return this.getIndex() - s.getIndex();
	}

	public String getHostAliases() {
		return hostAliases;
	}

	public void setHostAliases(String hostAliases) {
		this.hostAliases = hostAliases;
	}

	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

	public String getTemplatePath() {
		return templatePath;
	}

	public boolean hasAliases() {
		return StringUtils.hasText(getHostAliases()) && getHostAliases().split(",").length > 0;
	}
}
