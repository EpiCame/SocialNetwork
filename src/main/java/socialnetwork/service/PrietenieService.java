package socialnetwork.service;

import socialnetwork.domain.Prietenie;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.Utilizator;
import socialnetwork.repository.file.AbstractFileRepository;
import utils.events.ChangeEventType;
import utils.events.FriendshipChangeEvent;
import utils.observer.Observable;
import utils.observer.Observer;

import java.util.ArrayList;
import java.util.List;

public class PrietenieService implements Observable<FriendshipChangeEvent> {
    private AbstractFileRepository<Tuple<Long,Long>, Prietenie> repo;

    public PrietenieService(AbstractFileRepository<Tuple<Long, Long>, Prietenie> repo) {
        this.repo = repo;
    }

    public Prietenie extractPrietenie(List<String> args){
        return this.repo.extractEntity(args);
    }
    public Prietenie addPrietenie(Prietenie prietenie) {
        Prietenie friendship =  repo.save(prietenie);
        if(friendship==null)
            notifyObservers(new FriendshipChangeEvent(ChangeEventType.ADD, null));
        return friendship;
    }

    public Iterable<Prietenie> getAll(){
        return repo.findAll();
    }

    public Prietenie deletePrietenie(Long firstID, Long secondID) {
        Tuple<Long,Long> ID = new Tuple<>(firstID,secondID);
        Prietenie friendship = this.repo.delete(ID);
        if(friendship==null)
            notifyObservers(new FriendshipChangeEvent(ChangeEventType.DELETE, null));
        return friendship;
    }
    private List<Observer<FriendshipChangeEvent>> observers = new ArrayList<>();
    @Override
    public void addObserver(Observer<FriendshipChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<FriendshipChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(FriendshipChangeEvent t) {
        observers.forEach(x->x.update(t));
    }
}
