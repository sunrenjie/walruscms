package lt.walrus.ajax;
/*
 * Copyright 2006 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.web.servlet.ModelAndView;
import org.springmodules.xt.ajax.AjaxAction;

/**
 * Ajax action for redirecting to a given URL.
 *
 * @author Sergio Bossa
 */
public class WalrusRedirectAction implements AjaxAction {
    
    private static final long serialVersionUID = 26L;
    
    private static final String OPEN = "<redirect>";
    private static final String CLOSE = "</redirect>";
    
    private String url;
    private Map<String, String> model;

	/**
	 * Construct the action.
	 * 
	 * @param url
	 *            The redirect url.
	 * @param model
	 *            The view model as a
	 *            {@link org.springframework.web.servlet.ModelAndView} object,
	 *            containing parameters to pass to the new view.
	 */
	public WalrusRedirectAction(String url, ModelAndView model) {
        this.url = url;
		if (model != null && model.getModel() != null) {
			this.model = new HashMap<String, String>();
			model.addAllObjects(model.getModel());
        }
        else {
            this.model = new HashMap<String, String>(1);
        }
    }
    
    /** 
     * Construct the action.
     * @param url The redirect url.
     * @param model The view model as a map object, containing parameters to pass to the new view. 
     */
    public WalrusRedirectAction(String url, Map<String, String> model) {
        this.url = url;
        if (model != null) {
            this.model = model;
        }
        else {
            this.model= new HashMap<String, String>(1);
        }
    }
    
    public String render() {
    	String redirectUrl = "";
        try {
            redirectUrl = this.appendQueryProperties(this.model, "UTF-8");
        }
        catch(UnsupportedEncodingException ex) {
            // FIXME : Unexpected ....
            throw new RuntimeException("Unexpected", ex);
        }
        StringBuilder response = new StringBuilder();
        response.append(OPEN)
        .append("<content>")
        .append("<target ")
        .append("url=").append('"').append(redirectUrl).append('"')
        .append("/>")
        .append("</content>")
        .append(CLOSE);
        return response.toString();
    }
    
    protected transient org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());
    
    /**
     * Append query properties to the redirect URL.
     * Stringifies, URL-encodes and formats model attributes as query properties.
     */
    @SuppressWarnings("unchecked")
	protected String appendQueryProperties(Map<String, String> model, String encodingScheme)
    throws UnsupportedEncodingException {
        // if there are not already some parameters, we need a ?
    	StringBuilder redirectUrl = new StringBuilder(this.url);
    	
        boolean first = (this.url.toString().indexOf('?') < 0);
        Iterator<Map.Entry<String, String>> entries = model.entrySet().iterator();
        logger.debug("WalrusRedirectAction.appendQueryProperties: url BEFORE: " + redirectUrl.toString());
        logger.debug("WalrusRedirectAction.appendQueryProperties: entries size: " + model.entrySet().size());
        while (entries.hasNext()) {
            if (first) {
                redirectUrl.append('?');
                first = false;
            }
            else {
                redirectUrl.append("&amp;");
            }
            Map.Entry entry = (Map.Entry) entries.next();
            String encodedKey = URLEncoder.encode(entry.getKey().toString(), encodingScheme);
            String encodedValue = (entry.getValue() != null ? URLEncoder.encode(entry.getValue().toString(), encodingScheme) : "");
            logger.debug("WalrusRedirectAction.appendQueryProperties: adding pair: " + encodedKey + "=" + encodedValue);
            redirectUrl.append(encodedKey).append('=').append(encodedValue);
        }
        logger.debug("WalrusRedirectAction.appendQueryProperties: url AFTER: " + redirectUrl.toString());
        return redirectUrl.toString();
    }
}
