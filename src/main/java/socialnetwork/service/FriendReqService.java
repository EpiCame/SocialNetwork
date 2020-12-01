package socialnetwork.service;

import socialnetwork.domain.FriendRequest;
import socialnetwork.domain.Message;
import socialnetwork.repository.file.AbstractFileRepository;
import utils.events.ChangeEventType;
import utils.events.RequestChangeEvent;
import utils.observer.Observable;
import utils.observer.Observer;

import java.util.ArrayList;
import java.util.List;

public class FriendReqService implements Observable<RequestChangeEvent> {
    private AbstractFileRepository<Long, FriendRequest> repo;
    private static long maxID;

    public FriendReqService(AbstractFileRepository<Long, FriendRequest> repo) {
        this.repo = repo;
        maxID = 0;
        for(FriendRequest x : repo.findAll())
            maxID = Math.max(maxID,x.getId());
        maxID+=1;
    }
    public FriendRequest extractRequest(List<String> args){
        return this.repo.extractEntity(args);
    }

    public FriendRequest addRequest(FriendRequest req) {
        FriendRequest request =  repo.save(req);
        if(request==null) {
            notifyObservers(new RequestChangeEvent(ChangeEventType.ADD, request));
            if(req.getId()>=maxID)
                maxID = req.getId()+1;
        }
        return request;
    }

    public static long getMaxID() {
        return maxID;
    }

    public Iterable<FriendRequest> getAll(){
        return repo.findAll();
    }



    public FriendRequest deleteRequest(Long ID) {
        FriendRequest request =  this.repo.delete(ID);
        if(request == null)
            notifyObservers(new RequestChangeEvent(ChangeEventType.DELETE, request));
        return request;
    }

    public FriendRequest findRequest(Long ID){
        return this.repo.findOne(ID);
    }

    public FriendRequest updateRequest(FriendRequest request) {
        FriendRequest request1 =  this.repo.update(request);
        if(request1==null)
            notifyObservers(new RequestChangeEvent(ChangeEventType.UPDATE, request));
        return request1;
    }

    private List<Observer<RequestChangeEvent>> observers = new ArrayList<>();
    @Override
    public void addObserver(Observer<RequestChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<RequestChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(RequestChangeEvent t) {
        this.observers.forEach(x->x.update(t));
    }
}
