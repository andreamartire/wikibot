package techbrain.wikibot.beans;

import android.provider.BaseColumns;

import java.util.Calendar;
import java.util.Date;

public class MessageElement {

    Long id;

    MessageType messageType;
    String messageValue;
    Date creationDate;

    String remoteImageUrl;
    String localImageFilePath;
    String previewText;
    String previewTextHtml;

    int previewDone;

    /* Inner class that defines the table contents */
    public static class MessageElementEntry implements BaseColumns {
        public static final String TABLE_NAME = "message_element";
        public static final String COLUMN_NAME_MESSAGE_TYPE = "message_type";
        public static final String COLUMN_NAME_MESSAGE_VALUE = "message_value";
        public static final String COLUMN_NAME_REMOTE_IMAGE_URL = "remote_image_url";
        public static final String COLUMN_NAME_LOCAL_IMAGE_FILE_PATH = "local_image_file_path";
        public static final String COLUMN_NAME_PREVIEW_TEXT = "preview_text";
        public static final String COLUMN_NAME_PREVIEW_TEXT_HTML = "preview_text_html";
        public static final String COLUMN_NAME_PREVIEW_DONE = "preview_done";
        public static final String COLUMN_NAME_CREATION_DATE = "creation_date";
    }

    public MessageElement(){
        this.creationDate = Calendar.getInstance().getTime();
    }

    public MessageElement(MessageType messageType, String messageValue) {
        this.messageType = messageType;
        this.messageValue = messageValue;
        this.creationDate = Calendar.getInstance().getTime();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPreviewDone() {
        return previewDone;
    }

    public void setPreviewDone(int previewDone) {
        this.previewDone = previewDone;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getMessageValue() {
        return messageValue;
    }

    public void setMessageValue(String messageValue) {
        this.messageValue = messageValue;
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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
