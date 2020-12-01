package socialnetwork.domain;

import java.time.LocalDateTime;

public class FriendRequest extends Entity<Long> {
    private Utilizator firstUser;
    private String firstUserFirstName;
    private String secondUserFirstName;
    private Utilizator secondUser;
    private Status status = Status.pending;
    private LocalDateTime date;

    public FriendRequest(Utilizator firstUser, Utilizator secondUser, Status status) {
        this.firstUser = firstUser;
        this.secondUser = secondUser;
        this.status = status;
        this.date = LocalDateTime.now();
    }

    public FriendRequest(Utilizator firstUser, Utilizator secondUser) {
        this.firstUser = firstUser;
        this.secondUser = secondUser;
        this.status = Status.pending;
    }

    public Utilizator getFirstUser() {
        return firstUser;
    }

    public void setFirstUser(Utilizator firstUser) {
        this.firstUser = firstUser;
    }

    public Utilizator getSecondUser() {
        return secondUser;
    }

    public void setSecondUser(Utilizator secondUser) {
        this.secondUser = secondUser;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getFirstUserFirstName() {
        return this.firstUser.getFirstName();
    }

    public String getSecondUserFirstName() {
        return this.secondUser.getFirstName();
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "From:" + firstUser.getLastName() +
                ", To:" + secondUser.getLastName() +
                ", Status:" + status;
    }
}
