package lt.walrus.ajax;

import java.util.HashMap;

import lt.walrus.controller.BannerEditorCommand;
import lt.walrus.model.ImageBox;
import lt.walrus.model.Site;

import org.springframework.web.multipart.MultipartFile;
import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxResponseImpl;
import org.springmodules.xt.ajax.AjaxSubmitEvent;
import org.springmodules.xt.ajax.action.ExecuteJavascriptFunctionAction;

public class ImageEditorHandler extends UploadHandler {

	public AjaxResponse updateImage(AjaxSubmitEvent e) {
		AjaxResponse r = new AjaxResponseImpl("UTF-8");

		BannerEditorCommand c = (BannerEditorCommand) e.getCommandObject();
		logger.debug("ImageEditorHandler.updateImage(): COMMAND: " + c);
		
		if (null != c) {
			MultipartFile file = c.getFile();
			Site site = siteResolver.getSite(e);
			ImageBox box = (ImageBox) site.getBox(c.getBoxId());

			if (null == box) {
				return AjaxErrorMaker.addErrorMessage(r, "Incorrect request. (Can't find imabe box with boxId=" + c.getBoxId() + ")");
			}
			if (file.getSize() > 0) {
				if (!fileService.isImage(file)) {
					return AjaxErrorMaker.addErrorMessage(r, "Can't process the format of your file. Only JPG, GIF and PNG images are allowed.");
				}
				String newFileName;
				try {
					newFileName = fileService.putFileToPlace(file);
				} catch (Exception ex) {
					return AjaxErrorMaker.addErrorMessage(r, "Can't copy file, check 'walrus.files.directory' in file /WEB-INF/classes/walrus.properties: "
							+ ex);
				}
				box.setImage(getFileUrl() + "/" + newFileName);
				boxService.save(box);
				return makeChangeImageResponse(r, box.getBoxId(), getFileBaseUrl(e) + box.getImage());
			} else {
				fileService.deleteFile(box.getImage());
				box.setImage("");
				boxService.save(box);
				return makeChangeImageResponse(r, box.getBoxId(), "");
			}
		}
		return AjaxErrorMaker.addErrorMessage(r, "Incorrect request. (ImageEditorHandler.updateImage(): no BannerEditorCommand)");
	}

	private AjaxResponse makeChangeImageResponse(AjaxResponse r, String boxId, String image) {
		HashMap<String, Object> options = new HashMap<String, Object>();
		options.put("boxId", boxId);
		options.put("image", image);
		r.addAction(new ExecuteJavascriptFunctionAction("showNewImage", options));
		return r;
	}
}
