package techbrain.wikibot.beans;

/**
 * Created by andrea on 17/12/17.
 */

public class MessageElement {
    MessageType type;
    String value;

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
}
