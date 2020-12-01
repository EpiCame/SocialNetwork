package utils.events;

import socialnetwork.domain.FriendRequest;
import socialnetwork.domain.Prietenie;

public class RequestChangeEvent implements Event{
    private ChangeEventType type;
    private FriendRequest data, oldData;

    public RequestChangeEvent(ChangeEventType type, FriendRequest data) {
        this.type = type;
        this.data = data;
    }
    public RequestChangeEvent(ChangeEventType type, FriendRequest data, FriendRequest oldData) {
        this.type = type;
        this.data = data;
        this.oldData=oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public FriendRequest getData() {
        return data;
    }

    public FriendRequest getOldData() {
        return oldData;
    }
}
