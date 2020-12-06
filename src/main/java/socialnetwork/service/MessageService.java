package socialnetwork.service;

import socialnetwork.domain.FriendRequest;
import socialnetwork.domain.Message;
import socialnetwork.domain.Utilizator;
import socialnetwork.repository.file.AbstractFileRepository;
import utils.events.ChangeEventType;
import utils.events.FriendshipChangeEvent;
import utils.events.MessageChangeEvent;
import utils.events.RequestChangeEvent;
import utils.observer.Observable;
import utils.observer.Observer;

import java.util.ArrayList;
import java.util.List;

public class MessageService implements Observable<MessageChangeEvent> {
    private AbstractFileRepository<Long, Message> repo;
    private static long maxID;

    public static long getMaxID() {
        return maxID;
    }

    public MessageService(AbstractFileRepository<Long, Message> repo) {
        this.repo = repo;
        maxID = 0;
        for(Message x : repo.findAll())
            maxID = Math.max(maxID,x.getId());
        maxID+=1;
    }

    public Message extractMessage(List<String> args){
        return this.repo.extractEntity(args);
    }

    public Message addMessage(Message msg) {
        Message message =  repo.save(msg);
        if(message==null) {
            notifyObservers(new MessageChangeEvent(ChangeEventType.ADD, message));
            if(msg.getId()>=maxID)
                maxID = msg.getId()+1;
        }
        return message;
    }

    public Iterable<Message> getAll(){
        return repo.findAll();
    }

    public Message deleteMessage(Long ID) {
        return this.repo.delete(ID);
    }

    public Message findMessage(Long ID){
        return this.repo.findOne(ID);
    }

    private List<Observer<MessageChangeEvent>> observers = new ArrayList<>();
    @Override
    public void addObserver(Observer<MessageChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<MessageChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(MessageChangeEvent t) {
        observers.forEach(x->x.update(t));
    }
}
