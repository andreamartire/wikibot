package techbrain.wikibot.beans;

import android.provider.BaseColumns;

import java.util.Calendar;
import java.util.Date;

public class Article {

    Long id;

    String url;

    String previewText;
    String previewTextHtml;

    String remoteImageUrl;
    String localImageFilePath;

    Date creationDate;

    /* Inner class that defines the table contents */
    public static class ArticleEntry implements BaseColumns {
        public static final String TABLE_NAME = "article";
        public static final String COLUMN_NAME_URL = "url";
        public static final String COLUMN_NAME_PREVIEW_TEXT = "preview_text";
        public static final String COLUMN_NAME_PREVIEW_TEXT_HTML = "preview_text_html";
        public static final String COLUMN_NAME_REMOTE_IMAGE_URL = "remote_image_url";
        public static final String COLUMN_NAME_LOCAL_IMAGE_FILE_PATH = "local_image_file_path";
        public static final String COLUMN_NAME_CREATION_DATE = "creation_date";
    }

    public Article(){
        this.creationDate = Calendar.getInstance().getTime();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPreviewText() {
        return previewText;
    }

    public void setPreviewText(String previewText) {
        this.previewText = previewText;
    }

    public String getPreviewTextHtml() {
        return previewTextHtml;
    }

    public void setPreviewTextHtml(String previewTextHtml) {
        this.previewTextHtml = previewTextHtml;
    }

    public String getRemoteImageUrl() {
        return remoteImageUrl;
    }

    public void setRemoteImageUrl(String remoteImageUrl) {
        this.remoteImageUrl = remoteImageUrl;
    }

    public String getLocalImageFilePath() {
        return localImageFilePath;
    }

    public void setLocalImageFilePath(String localImageFilePath) {
        this.localImageFilePath = localImageFilePath;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
