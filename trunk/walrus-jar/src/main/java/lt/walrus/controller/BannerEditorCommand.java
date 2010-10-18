package lt.walrus.controller;

import org.springframework.web.multipart.MultipartFile;

public class BannerEditorCommand {
	protected org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());
	
    private MultipartFile file;
    private String url;
    private String boxId;
	private long bannerId;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file1) {
        this.file = file1;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url1) {
        this.url = url1;
    }

    @Override
    public String toString() {
    	if(null != file) {
    		return "BOX: " + boxId + " url:" + url + " file:" + file.getName();
    	} else {
    		return "BOX: " + boxId + " url:" + url + " bannerId:" + bannerId;
    	}
    }

	public String getBoxId() {
		return boxId;
	}

	public void setBoxId(String boxId) {
		this.boxId = boxId;
	}

	public long getBannerId() {
		return bannerId;
	}

	public void setBannerId(long bannerId) {
		this.bannerId = bannerId;
	}

}
