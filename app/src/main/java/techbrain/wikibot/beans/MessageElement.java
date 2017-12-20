package techbrain.wikibot.beans;

/**
 * Created by andrea on 17/12/17.
 */

public class MessageElement {

    MessageType type;
    String value;
    String remoteImageUrl;
    String localImageFilePath;
    String previewText;
    String previewTextHtml;

    public MessageElement(MessageType type, String value) {
        this.type = type;
        this.value = value;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
}
