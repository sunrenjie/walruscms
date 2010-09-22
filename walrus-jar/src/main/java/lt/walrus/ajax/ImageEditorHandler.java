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
			Site site = getSite(e);
			ImageBox box = (ImageBox) site.getBox(c.getBoxId());

			if (null == box) {
				return addErrorMessage(r, "Nekorektiška užklausa. (Nerandam ImageBox'o su boxId=" + c.getBoxId() + ")");
			}
			if (file.getSize() > 0) {
				if (!fileService.isImage(file)) {
					return addErrorMessage(r, "Jūs atsiuntėte blogo formato failą. Galima siųsti tik JPG, GIF ir PNG piešinėlių failus.");
				}
				String newFileName;
				try {
					newFileName = fileService.putFileToPlace(file);
				} catch (Exception ex) {
					return addErrorMessage(r, "Nepavyko nukopijuoti failo, patikrinkite svetainės konfigūracijos parametrus: " + ex);
				}
				box.setImage(getFileUrl() + "/" + newFileName);
				service.save(box);
				return makeChangeImageResponse(r, box.getBoxId(), getFileBaseUrl(e) + box.getImage());
			} else {
				fileService.deleteFile(box.getImage());
				box.setImage("");
				service.save(box);
				return makeChangeImageResponse(r, box.getBoxId(), "");
			}
		}
		return addErrorMessage(r, "Nekorektiška užklausa. (ImageEditorHandler.updateImage(): no BannerEditorCommand)");
	}

	private AjaxResponse makeChangeImageResponse(AjaxResponse r, String boxId, String image) {
		HashMap<String, Object> options = new HashMap<String, Object>();
		options.put("boxId", boxId);
		options.put("image", image);
		r.addAction(new ExecuteJavascriptFunctionAction("showNewImage", options));
		return r;
	}
}
