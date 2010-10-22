package lt.walrus.ajax;

import java.io.File;
import java.util.HashMap;

import lt.walrus.controller.UploadCommand;
import lt.walrus.controller.util.SiteResolver;
import lt.walrus.service.BoxService;
import lt.walrus.service.FileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springmodules.xt.ajax.AbstractAjaxHandler;
import org.springmodules.xt.ajax.AjaxEvent;
import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxResponseImpl;
import org.springmodules.xt.ajax.AjaxSubmitEvent;
import org.springmodules.xt.ajax.action.ExecuteJavascriptFunctionAction;
import org.springmodules.xt.ajax.action.SetAttributeAction;

public class UploadHandler extends AbstractAjaxHandler {
	protected org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());

	@Autowired
	protected FileService fileService;
	protected File destDir;
	protected int thumbSize = 100;
	@Autowired
	protected String fileUrl;
	@Autowired
	protected SiteResolver siteResolver;

	@Autowired
	protected BoxService boxService;

	public AjaxResponse uploadMedia(AjaxSubmitEvent e) {
		return uploadFile(e);
	}
	
	public AjaxResponse uploadFile(AjaxSubmitEvent e) {
		UploadCommand c = (UploadCommand) e.getCommandObject();
		AjaxResponse r = new AjaxResponseImpl("UTF-8");
		if (null != c) {
			MultipartFile file = c.getFile();
			return actionNoop(r, file, getFileBaseUrl(e));
		}
		return r;
	}
	
	public AjaxResponse deleteFile(AjaxEvent e) {
		AjaxResponse r = new AjaxResponseImpl("UTF-8");
		String fileName = e.getParameters().get("fileName");
		logger.debug("triname failą: ".concat(fileName));

		fileService.delete(fileName);
		
		HashMap<String, String> model = new HashMap<String, String>();
		model.put("type", e.getParameters().get("type"));
		r.addAction(new WalrusRedirectAction("list", model));

		return r;
	}

	protected AjaxResponse actionNoop(AjaxResponse r, MultipartFile file, String fileBaseUrl) {
		try {
			String newFileName = fileService.putFileToPlace(file);
			r.addAction(new SetAttributeAction("src", "value", fileBaseUrl + newFileName));
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("url", fileBaseUrl + newFileName);
			r.addAction(new ExecuteJavascriptFunctionAction("fileUploaded", params));
		} catch (Exception ex) {
			return AjaxErrorMaker.addErrorMessage(r, "Can't copy file, check 'walrus.files.directory' in file /WEB-INF/classes/walrus.properties: " + ex);
		}
		return r;
	}

	protected String getFileBaseUrl(AjaxSubmitEvent e) {
		return "http://" + e.getHttpRequest().getServerName() + ":" + e.getHttpRequest().getServerPort() + e.getHttpRequest().getContextPath()
				+ fileUrl + "/";
	}

	protected String getBaseUrl(AjaxSubmitEvent e) {
		return "http://" + e.getHttpRequest().getServerName() + ":" + e.getHttpRequest().getServerPort() + e.getHttpRequest().getContextPath();
	}
	
	protected String getBaseUrl(AjaxEvent e) {
		return "http://" + e.getHttpRequest().getServerName() + ":" + e.getHttpRequest().getServerPort() + e.getHttpRequest().getContextPath();
	}
	
	public void setFileService(FileService fileService) {
		this.fileService = fileService;
	}

	public File getDestDir() {
		return destDir;
	}

	public void setDestDir(File destDir) {
		this.destDir = destDir;
	}

	public int getThumbSize() {
		return thumbSize;
	}

	public void setThumbSize(int thumbSize) {
		this.thumbSize = thumbSize;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public void setBoxService(BoxService boxService) {
		this.boxService = boxService;
	}

	public void setSiteResolver(SiteResolver siteResolver) {
		this.siteResolver = siteResolver;
	}
}
