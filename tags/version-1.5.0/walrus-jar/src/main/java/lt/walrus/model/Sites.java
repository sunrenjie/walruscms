package lt.walrus.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.springframework.util.StringUtils;

/**
 * {@link Site} hashmap holder, returns Site by id, by language and hostname
 */
public class Sites implements Serializable {
	private static final long serialVersionUID = -3977959673145802869L;
	
	private HashMap<String, Site> sites;
	
	public Sites () {
		sites = new HashMap<String, Site>();
	}

	public Collection<Site> getSites() {
		return sites.values();
	}

	public void setSites(HashMap<String, Site> sites) {
		this.sites = sites;
	}

	public Set<String> keySet() {
		return this.sites.keySet();
	}
	
	private Site get(String key) {
		if (!StringUtils.hasText(key)) {
			return null;
		}
		return sites.get(key);
	}

	public Site get(String host, String language) {
		if(null != get(host + language)) {
			return get(host + language);
		}
		if(host.startsWith("www.")) {
			if(null != get(host.substring(4) + language)) {
				return get(host.substring(4) + language);
			}
		} else {
			if (null != get("www." + host + language)) {
				return get("www." + host + language);
			}
		}
		return null;
	}

	
	public void put(String key, Site s) {
		sites.put(key, s);
	}

	public void clear() {
		sites.clear();
	}
	
	public int size() {
		if(null != sites) {
			return sites.size();
		} else {
			return 0;
		}
	}

	public boolean isEmpty() {
		return null != sites && sites.isEmpty();
	}

	public List<Site> getList(String language) {
		ArrayList<Site> ret = new ArrayList<Site>();
		for (Site site : sites.values()) {
			if (language.equals(site.getLanguage())) {
				ret.add(site);
			}
		}
		Collections.sort(ret);
		return ret;
	}

	public void remove(String key) {
		sites.remove(key);
	}

	public Site getSiteById(long id) {
		for (Site site : sites.values()) {
			if (id == site.getId()) {
				return site;
			}
		}
		return null;
	}
}
