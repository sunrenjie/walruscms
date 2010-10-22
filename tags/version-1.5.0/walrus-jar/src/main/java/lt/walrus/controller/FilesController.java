package lt.walrus.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lt.walrus.command.FileListCommand;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

@Controller("filesController")
public class FilesController extends MultiActionController {

	private File destDir = null;

	public ModelAndView listFiles(HttpServletRequest request, HttpServletResponse response, FileListCommand command) {
		HashMap<String, Object> model = new HashMap<String, Object>();
		
		logger.debug("DEST DIR: " + destDir.getAbsolutePath());
		
		FilenameFilter filter = (null != command && command.isImage()) ? new UploadedImageFilter() : new UploadedFileFilter();
		File[] fileList = destDir.listFiles(filter);
		List<File> list = new ArrayList<File>(Arrays.asList(fileList));

		Collections.sort(list);
		// Collections.sort(list, new Comparator<File>() {
		// public int compare(File o1, File o2) {
		// return (int) (o1.lastModified() - o2.lastModified());
		// }
		// });
		
		model.put("list", list);
		model.put("type", (null != command ? command.getType() : null));
		model.put("showFiles", command.isFull());
		
		return new ModelAndView("fileList", "model", model);
	}
	
	public void sendFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String uri = request.getRequestURI();
		if (! StringUtils.hasText(uri)) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		} else {
			uri = URLDecoder.decode(uri.substring(uri.lastIndexOf("/") + 1), "UTF-8");
			File res = new File(destDir.getPath() + File.separator + uri);

			if (! res.exists()) {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			} else if (0 == res.length()) {
				response.sendError(HttpServletResponse.SC_CONFLICT);
			} else if (res.isDirectory() || res.isHidden()) {
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
			} else {
				response.setContentType(this.getServletContext().getMimeType(res.getName()));
				response.setHeader("content-length",Long.toString(res.length()));
				//response.setHeader("Content-Disposition", "inline; document.fileName=\""+ res.getName() + "\"");
				response.setHeader("ETag", getETag(res));
				response.setHeader("Last-Modified", getLastModifiedHttp(res));
				try {
					IOUtils.copyLarge(new FileInputStream(res), response.getOutputStream());
				} catch (FileNotFoundException e) {
					logger.warn("Suddenly, the file is not found: ", e);
					response.sendError(HttpServletResponse.SC_NOT_FOUND);
				}
			}
		}
	}
	
	public File getDestDir() {
		return destDir;
	}

	public void setDestDir(File destDir1) {
		this.destDir = destDir1;
	}

    /**
     * Get the ETag associated with a file.
     *
     * @param resourceAttributes The resource information
     */
    protected String getETag(File item) {
        return "W/\"" + item.length() + "-" + item.lastModified() + "\"";
    }
    
    protected static final SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
    
    protected String getLastModifiedHttp(File item) {
    	Date d = new Date();
    	d.setTime(item.lastModified() * 1000);
    	String ret = "";
    	synchronized (format) {
			ret = format.format(d);
		}
    	return ret;
    }
    
    class UploadedFileFilter implements FilenameFilter {

		public boolean accept(File dir, String name) {
			if (! StringUtils.hasText(name)) {
				return false;
			}
			File test = new File(dir.getAbsoluteFile() + File.separator + name);
			return	null != test &&
					test.isFile() &&
					! test.isHidden() &&
					! test.isDirectory() &&
					test.canRead();
		}
    }
    
    class UploadedImageFilter extends UploadedFileFilter {
    	
    	@Override
    	public boolean accept(File dir, String name) {
    		return	super.accept(dir, name) &&
    				(name.endsWith(".jpg") || name.endsWith(".JPG")) ||
    				(name.endsWith(".png") || name.endsWith(".PNG")) ||
    				(name.endsWith(".gif") || name.endsWith(".GIF"));
    	}
    }

}
