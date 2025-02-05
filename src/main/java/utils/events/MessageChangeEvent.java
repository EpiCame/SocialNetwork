package utils.events;

import socialnetwork.domain.Message;
import socialnetwork.domain.Prietenie;

public class MessageChangeEvent implements Event {
    private ChangeEventType type;
    private Message data, oldData;

    public MessageChangeEvent(ChangeEventType type, Message data) {
        this.type = type;
        this.data = data;
    }
    public MessageChangeEvent(ChangeEventType type, Message data, Message oldData) {
        this.type = type;
        this.data = data;
        this.oldData=oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Message getData() {
        return data;
    }

    public Message getOldData() {
        return oldData;
    }
}