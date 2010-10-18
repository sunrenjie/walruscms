package lt.walrus.controller;

import org.springframework.web.multipart.MultipartFile;

public class UploadCommand {
    private MultipartFile file;
    private String action;
    private String articleId;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file1) {
        this.file = file1;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action1) {
        this.action = action1;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId1) {
        this.articleId = articleId1;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "articleId: " + articleId + " action:" + action + " file:" + file.getName();
    }
}
