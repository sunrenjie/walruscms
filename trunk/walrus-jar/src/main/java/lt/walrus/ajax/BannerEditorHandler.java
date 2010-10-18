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
		Site site = getSite(e);
		BannerBox box = (BannerBox) site.getBox(e.getParameters().get("boxId"));
		if(null!=box) {
			Banner banner = box.getBanner(Integer.valueOf(e.getParameters().get("bannerId")));
			if(null != banner) {
				fileService.deleteFile(banner.getBanner());
				box.getBanners().remove(banner);
				service.deleteBanner(banner);
				service.save(box);
				Banner b = box.getRandomBanner();
				if(null != b) {
					return makeChangeBannerResponse(r, box.getBoxId(), getBaseUrl(e) + b.getBanner(), b.getUrl(), e.getHttpRequest());
				} else {
					return makeChangeBannerResponse(r, box.getBoxId(), "", "", e.getHttpRequest());
				}
			} else {
				return addErrorMessage(r, "Nepavyko ištrinti banerio, nes nepavyko rasti banerio bannerboxe " + e.getParameters().get("boxId") + " su id: " + e.getParameters().get("bannerId"));
			}
		} else {
			return addErrorMessage(r, "Nepavyko ištrinti banerio, nes nepavyko rasti bannerBoxo su id: " + e.getParameters().get("boxId"));
		}
	}
	
	public AjaxResponse addBanner(AjaxSubmitEvent e) {
		BannerEditorCommand c = (BannerEditorCommand) e.getCommandObject();
		AjaxResponse r = new AjaxResponseImpl("UTF-8");

		if (null != c) {
			MultipartFile file = c.getFile();

			Site site = getSite(e);
			BannerBox box = (BannerBox) site.getBox(c.getBoxId());

			if (null == box) {
				return addErrorMessage(r, "Nekorektiška užklausa. (Nerandam bannerBoxo su boxId " + c.getBoxId() + ")");
			}

			if (null != file && file.getSize() > 0 && StringUtils.hasText(c.getUrl())) {
				if (!fileService.isImage(file)) {
					return addErrorMessage(r, "Jūs atsiuntėte blogo formato failą. Galima siųsti tik JPG, GIF ir PNG piešinėlių failus.");
				}

				String newFileName;
				try {
					newFileName = fileService.putFileToPlace(file);
				} catch (Exception ex) {
					return addErrorMessage(r, "Nepavyko nukopijuoti failo, patikrinkite svetainės konfigūracijos parametrus: " + ex);
				}

				Banner b = new Banner();
				b.setBanner(getFileUrl() + "/" + newFileName);
				//b.setBanner(newFileName); // TODO i db saugoti tik failu pavadinimus, be fileUrl
				b.setUrl(c.getUrl());

				box.getBanners().add(b);
				
				service.save(box);
				
				return makeChangeBannerResponse(r, box.getBoxId(), getBaseUrl(e) + b.getBanner(), b.getUrl(), e.getHttpRequest());
			} else {
				return addErrorMessage(r, "Nepavyko pridėti naujo banerio, nes nenurodėte piešinėlio arba adreso.");
			}
		}

		return addErrorMessage(r, "Nekorektiška užklausa. (no BannerEditorCommand)");
	}

	public AjaxResponse updateBannerUrl(AjaxSubmitEvent e) {
		BannerEditorCommand c = (BannerEditorCommand) e.getCommandObject();
		AjaxResponse r = new AjaxResponseImpl("UTF-8");

		if (null != c) {
			Site site = getSite(e);
			BannerBox box = (BannerBox) site.getBox(c.getBoxId());

			if (null == box) {
				return addErrorMessage(r, "Nekorektiška užklausa. (Nerandam bannerBoxo su boxId " + c.getBoxId() + ")");
			}

			if (StringUtils.hasText(c.getUrl())) {
				Banner b = box.getBanner(c.getBannerId());
				if (null != b) {
					b.setUrl(c.getUrl());

					MultipartFile file = c.getFile();
					if (null != file && file.getSize() > 0) {
						if (!fileService.isImage(file)) {
							return addErrorMessage(r, "Jūs atsiuntėte blogo formato failą. Galima siųsti tik JPG, GIF ir PNG piešinėlių failus.");
						}
						String newFileName;
						try {
							newFileName = fileService.putFileToPlace(file);
							b.setBanner(getFileUrl() + "/" + newFileName);
						} catch (Exception ex) {
							return addErrorMessage(r, "Nepavyko nukopijuoti failo, patikrinkite svetainės konfigūracijos parametrus: " + ex);
						}
					}
					service.save(box);
					return makeChangeBannerResponse(r, box.getBoxId(), getBaseUrl(e) + b.getBanner(), b.getUrl(), e.getHttpRequest());
				} else {
					return addErrorMessage(r, "Nepavyko pakeisti banerio, nes nurodytas blogas banerio id - " + c.getBannerId());
				}
			} else {
				return addErrorMessage(r, "Nepavyko pakeisti banerio, nes nenurodėte url adreso.");
			}
		}

		return addErrorMessage(r, "Nekorektiška užklausa. (no BannerEditorCommand)");
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
