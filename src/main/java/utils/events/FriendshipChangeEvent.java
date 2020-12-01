package utils.events;

import socialnetwork.domain.Prietenie;

public class FriendshipChangeEvent implements Event {
    private ChangeEventType type;
    private Prietenie data, oldData;

    public FriendshipChangeEvent(ChangeEventType type, Prietenie data) {
        this.type = type;
        this.data = data;
    }
    public FriendshipChangeEvent(ChangeEventType type, Prietenie data, Prietenie oldData) {
        this.type = type;
        this.data = data;
        this.oldData=oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Prietenie getData() {
        return data;
    }

    public Prietenie getOldData() {
        return oldData;
    }
}