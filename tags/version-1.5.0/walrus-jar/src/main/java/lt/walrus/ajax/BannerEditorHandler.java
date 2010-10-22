package lt.walrus.ajax;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import lt.walrus.controller.BannerEditorCommand;
import lt.walrus.model.Banner;
import lt.walrus.model.BannerBox;
import lt.walrus.model.Site;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springmodules.xt.ajax.AjaxEvent;
import org.springmodules.xt.ajax.AjaxResponse;
import org.springmodules.xt.ajax.AjaxResponseImpl;
import org.springmodules.xt.ajax.AjaxSubmitEvent;
import org.springmodules.xt.ajax.action.ExecuteJavascriptFunctionAction;

public class BannerEditorHandler extends UploadHandler {

	public AjaxResponse deleteBanner(AjaxEvent e) {
		AjaxResponse r = new AjaxResponseImpl("UTF-8");
		Site site = siteResolver.getSite(e);
		BannerBox box = (BannerBox) site.getBox(e.getParameters().get("boxId"));
		if(null!=box) {
			Banner banner = box.getBanner(Integer.valueOf(e.getParameters().get("bannerId")));
			if(null != banner) {
				fileService.deleteFile(banner.getBanner());
				box.getBanners().remove(banner);
				boxService.deleteBanner(banner);
				boxService.save(box);
				Banner b = box.getRandomBanner();
				if(null != b) {
					return makeChangeBannerResponse(r, box.getBoxId(), getBaseUrl(e) + b.getBanner(), b.getUrl(), e.getHttpRequest());
				} else {
					return makeChangeBannerResponse(r, box.getBoxId(), "", "", e.getHttpRequest());
				}
			} else {
				return AjaxErrorMaker.addErrorMessage(r, "Can't delete banner because banner is not fount in bannerbox " + e.getParameters().get("boxId")
						+ " with id: "
						+ e.getParameters().get("bannerId"));
			}
		} else {
			return AjaxErrorMaker.addErrorMessage(r, "Can't delete banner because banner box not fount with id: " + e.getParameters().get("boxId"));
		}
	}
	
	public AjaxResponse addBanner(AjaxSubmitEvent e) {
		BannerEditorCommand c = (BannerEditorCommand) e.getCommandObject();
		AjaxResponse r = new AjaxResponseImpl("UTF-8");

		if (null != c) {
			MultipartFile file = c.getFile();

			Site site = siteResolver.getSite(e);
			BannerBox box = (BannerBox) site.getBox(c.getBoxId());

			if (null == box) {
				return AjaxErrorMaker.addErrorMessage(r, "Nekorektiška užklausa. (Nerandam bannerBoxo su boxId " + c.getBoxId() + ")");
			}

			if (null != file && file.getSize() > 0 && StringUtils.hasText(c.getUrl())) {
				if (!fileService.isImage(file)) {
					return AjaxErrorMaker.addErrorMessage(r, "Jūs atsiuntėte blogo formato failą. Galima siųsti tik JPG, GIF ir PNG piešinėlių failus.");
				}

				String newFileName;
				try {
					newFileName = fileService.putFileToPlace(file);
				} catch (Exception ex) {
					return AjaxErrorMaker.addErrorMessage(r, "Can't copy file, check 'walrus.files.directory' in file /WEB-INF/classes/walrus.properties: "
							+ ex);
				}

				Banner b = new Banner();
				b.setBanner(getFileUrl() + "/" + newFileName);
				//b.setBanner(newFileName); // TODO i db saugoti tik failu pavadinimus, be fileUrl
				b.setUrl(c.getUrl());

				box.getBanners().add(b);
				
				boxService.save(box);
				
				return makeChangeBannerResponse(r, box.getBoxId(), getBaseUrl(e) + b.getBanner(), b.getUrl(), e.getHttpRequest());
			} else {
				return AjaxErrorMaker.addErrorMessage(r, "Can't add a new banner because image or URL is not specified.");
			}
		}

		return AjaxErrorMaker.addErrorMessage(r, "Incorrect request. (no BannerEditorCommand)");
	}

	public AjaxResponse updateBannerUrl(AjaxSubmitEvent e) {
		BannerEditorCommand c = (BannerEditorCommand) e.getCommandObject();
		AjaxResponse r = new AjaxResponseImpl("UTF-8");

		if (null != c) {
			Site site = siteResolver.getSite(e);
			BannerBox box = (BannerBox) site.getBox(c.getBoxId());

			if (null == box) {
				return AjaxErrorMaker.addErrorMessage(r, "Nekorektiška užklausa. (Nerandam bannerBoxo su boxId " + c.getBoxId() + ")");
			}

			if (StringUtils.hasText(c.getUrl())) {
				Banner b = box.getBanner(c.getBannerId());
				if (null != b) {
					b.setUrl(c.getUrl());

					MultipartFile file = c.getFile();
					if (null != file && file.getSize() > 0) {
						if (!fileService.isImage(file)) {
							return AjaxErrorMaker
									.addErrorMessage(r, "Jūs atsiuntėte blogo formato failą. Galima siųsti tik JPG, GIF ir PNG piešinėlių failus.");
						}
						String newFileName;
						try {
							newFileName = fileService.putFileToPlace(file);
							b.setBanner(getFileUrl() + "/" + newFileName);
						} catch (Exception ex) {
							return AjaxErrorMaker.addErrorMessage(r,
									"Can't copy file, check 'walrus.files.directory' in file /WEB-INF/classes/walrus.properties: " + ex);
						}
					}
					boxService.save(box);
					return makeChangeBannerResponse(r, box.getBoxId(), getBaseUrl(e) + b.getBanner(), b.getUrl(), e.getHttpRequest());
				} else {
					return AjaxErrorMaker.addErrorMessage(r, "Can't replace banner because wrong banner id was specified: " + c.getBannerId());
				}
			} else {
				return AjaxErrorMaker.addErrorMessage(r, "Can't replace banner because URL was not specified.");
			}
		}

		return AjaxErrorMaker.addErrorMessage(r, "Incorrect request. (no BannerEditorCommand)");
	}
	
	private AjaxResponse makeChangeBannerResponse(AjaxResponse r, String boxId, String banner, String url, HttpServletRequest request) {
		HashMap<String, Object> options = new HashMap<String, Object>();
		options.put("boxId", boxId);
		options.put("banner", banner);
		options.put("link", url);
		r.addAction(new ExecuteJavascriptFunctionAction("showNewBanner", options));
		return r;
	}
}
