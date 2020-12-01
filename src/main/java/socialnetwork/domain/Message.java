package socialnetwork.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Message extends Entity<Long>{
    private Utilizator from;
    private List<Utilizator> to;
    private String message;
    private LocalDateTime date;
    private List<Message> reply = null;

    public Message(Utilizator from, List<Utilizator> to, LocalDateTime date, String message) {
        this.from = from;
        this.to = to;
        this.message = message;
        if(date == null)
            this.date = LocalDateTime.now();
        else
            this.date = date;
    }

    public void addReplyMessage(Message replyMessage){
        if(this.reply == null)
            this.reply = new ArrayList<>();
        this.reply.add(replyMessage);
    }

    public Utilizator getFrom() {
        return from;
    }

    public void setFrom(Utilizator from) {
        this.from = from;
    }

    public List<Utilizator> getTo() {
        return to;
    }

    public void setTo(List<Utilizator> to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public List<Message> getReply() {
        return reply;
    }

    public void setReply(List<Message> reply) {
        this.reply = reply;
    }

    @Override
    public String toString() {
        return "Message{" +
                "from=" + from +
                ", to=" + to +
                ", message='" + message + '\'' +
                ", date=" + date +
                ", reply=" + reply +
                '}';
    }
}
