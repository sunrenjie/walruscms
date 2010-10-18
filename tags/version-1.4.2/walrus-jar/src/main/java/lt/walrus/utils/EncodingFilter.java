/*
 * Created on 2007.7.9
 *
 */
package lt.walrus.utils;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @author Dmitry Shenk
 */
public class EncodingFilter implements Filter{
    protected org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());
    private String encoding;
    
    public void init(FilterConfig filterCfg) 
	throws ServletException
	{
    	encoding = filterCfg.getInitParameter("encoding");
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
		throws IOException, ServletException
	{
	    if(encoding != null)
	    {
	        request.setCharacterEncoding(encoding);
            response.setCharacterEncoding("UTF-8");
	    }
	    chain.doFilter(request, response);
        if(encoding != null)
        {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            logger.debug("EncodingFilter.doFilter: setting resp encoding: " + response.getCharacterEncoding());
        }
	}
	
	public void destroy() 
	{
		 
	}
}
