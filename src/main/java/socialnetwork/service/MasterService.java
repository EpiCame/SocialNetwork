package socialnetwork.service;

import controller.ConversationController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import socialnetwork.domain.*;
import utils.events.FriendshipChangeEvent;
import utils.events.MessageChangeEvent;
import utils.events.RequestChangeEvent;
import utils.observer.Observable;
import utils.observer.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class MasterService {
    private UtilizatorService userService = null;
    private PrietenieService friendService = null;
    private MessageService messageService = null;
    private FriendReqService requestService = null;
    private Retea network = null;

    public MasterService(UtilizatorService userService, PrietenieService friendService, MessageService messageService, FriendReqService reqService) {
        this.userService = userService;
        this.friendService = friendService;
        this.messageService = messageService;
        this.requestService = reqService;
        addAllFriendships();
        completeMessages();
        completeRequests();
        network = Retea.getInstance(this.userService.getAll());
    }

    private void completeRequests(){
        Iterable<FriendRequest> requests = this.requestService.getAll();
        for(FriendRequest request : requests){
            Utilizator user1 = this.userService.findUtilizator(request.getFirstUser().getId());
            request.setFirstUser(user1);
            Utilizator user2 = this.userService.findUtilizator(request.getSecondUser().getId());
            request.setSecondUser(user2);
            if(request.getStatus()== Status.approved)
            {
                Prietenie friendship = new Prietenie();
                friendship.setId(new Tuple<>(user1.getId(), user2.getId()));
                if(user1!=user2 && userService.makeFriendship(user1,user2))
                    this.friendService.addPrietenie(friendship);
            }
        }
    }

    private void completeMessages(){
        Iterable<Message> messages = this.messageService.getAll();
        for(Message m : messages){
            m.setFrom(this.userService.findUtilizator(m.getFrom().getId()));
            List<Utilizator> utilizators = new ArrayList<>();
            for(int i = 0 ; i < m.getTo().size(); i++){
                Utilizator user = m.getTo().get(i);
                utilizators.add(this.userService.findUtilizator(user.getId()));
            }
            m.setTo(utilizators);
        }
    }



    public Utilizator findUserById(Long id){
        return this.userService.findUtilizator(id);
    }
    /**
     *
     * @return the list of users
     */
    public Iterable<Utilizator> getAllUsers(){
        return this.userService.getAll();
    }

    /**
     *
     * @return the list of friendships
     */
    public Iterable<Prietenie> getAllFriendships(){
        return this.friendService.getAll();
    }

    /**
     * add all the friendships to each user list
     */
    private void addAllFriendships(){
        for(Prietenie elem : this.friendService.getAll()){
            Tuple<Long, Long> id = elem.getId();
            Utilizator first = this.userService.findUtilizator(id.getLeft());
            Utilizator second = this.userService.findUtilizator(id.getRight());
            this.userService.makeFriendship(first, second);
        }
    }

    public List<Prietenie> getFriendshipsOfUser(Utilizator user){
        long id = user.getId();
        Iterable<Prietenie> friendships = this.getAllFriendships();
        ObservableList<Prietenie> items = FXCollections.observableArrayList();
        List<Prietenie> friendsList = new ArrayList<>();
        friendships.forEach(friendsList::add);
        return friendsList.stream().filter(x->{if(x.getId().getLeft()==id) return true;
        if(x.getId().getRight()==id){
            Long left = x.getId().getLeft();
            x.getId().setLeft(x.getId().getRight());
            x.getId().setRight(left);
            return true;
        }
        return false;
        })
                .collect(Collectors.toList());
    }

    /**
     * adds an user to the network and repository
     * @param args
     * @return the user if it's added
     */
    public Utilizator addUser(List<String> args){
        Utilizator user = this.userService.addUtilizator(this.userService.extractUser(args));
        this.network.addUser(user);
        return user;
    }

    /**
     * deletes the user and his friendships
     * @param argsList
     * @return the deleted user
     */
    public Utilizator deleteUser(List<String> argsList){
        Long ID = null;
        try{
            ID = Long.parseLong(argsList.get(0));
        }
        catch(NumberFormatException ignored){
            System.out.println("Invalid ID!\n");
        }
        Utilizator user = this.userService.findUtilizator(ID);
        this.network.deleteUser(user);
        List<String> auxList = new ArrayList<>(argsList);
        List<Utilizator> friends = user.getFriends();
        if(friends!=null)
            for(int i=0; i<friends.size(); ++i) {
                auxList.add(friends.get(i).getId().toString());
                --i;
                this.deleteFriendship(auxList);
                auxList.remove(1);
            }
        return this.userService.deleteUtilizator(ID);
    }

    /**
     * adds a friendship
     * @param args
     * @return the added friendship
     */
    public Prietenie addFriendship(List<String> args){
        Long firstID = null;
        Long secondID = null;
        try{
            firstID = Long.parseLong(args.get(0));
            secondID = Long.parseLong(args.get(1));
        }
        catch(NumberFormatException ignored){
            System.out.println("Invalid ID!\n");
        }
        Utilizator firstUser = this.userService.findUtilizator(firstID);
        Utilizator secondUser = this.userService.findUtilizator(secondID);
        Prietenie friendship = this.friendService.extractPrietenie(args);
        if(firstUser!=secondUser && this.userService.makeFriendship(firstUser, secondUser))
            return this.friendService.addPrietenie(friendship);
        return null;
    }
    public Prietenie deleteFriendship(List<String> args){
        Long firstID = null;
        Long secondID = null;
        try{
            firstID = Long.parseLong(args.get(0));
            secondID = Long.parseLong(args.get(1));
        }
        catch(NumberFormatException ignored){
            System.out.println("Invalid ID!\n");
        }
        Utilizator firstUser = this.userService.findUtilizator(firstID);
        Utilizator secondUser = this.userService.findUtilizator(secondID);
        Prietenie friendship = null;
        if(firstUser!=secondUser && this.userService.deleteFriendship(firstUser,secondUser)) {
            friendship = this.friendService.deletePrietenie(firstID, secondID);
        }
        return friendship;
    }
    public int printNumberOfCommunities(){
        return this.network.numberOfConnectedComponents();
    }

    public List<String> printMostSociableCommunity(){
        return this.network.longestConnectedComponent();
    }

    public Message sendAMessage(Long msgID, Long fromID, List<Long> toList, String message) {
        List<Utilizator> utlist = new ArrayList<>();
        Utilizator fromUser = this.userService.findUtilizator(fromID);
        if(fromUser == null)
            return new Message(null,null,null,null);
        Utilizator user = null;
        for(Long l : toList) {
            user = this.userService.findUtilizator(l);
            if(user!=null && user!=fromUser)
                utlist.add(user);
        }
        if(toList.isEmpty())
            return new Message(null,null,null,null);
        Message msg = new Message(fromUser, utlist,null, message);
        msg.setId(msgID);
        return this.messageService.addMessage(msg);
    }

    public Iterable<Message> getAllMessages() {
        return this.messageService.getAll();
    }


    public Message replyToAMessage(Long replyID, Long msgId, Long userID, String replyMessage) {
        Message message = this.messageService.findMessage(msgId);
        if(message == null) {
            System.out.println("The message does not exist!\n");
            return new Message(null,null,null,"");
        }
        Utilizator user = this.userService.findUtilizator(userID);
        if(user == null) {
            System.out.println("The user does not exist!\n");
            return new Message(null,null,null, "");
        }
        List<Utilizator> toList = new ArrayList<>();
        toList.add(message.getFrom());
        Message reply = new Message(user, toList, null, replyMessage);
        reply.setId(replyID);
        Message msg = this.messageService.addMessage(reply);
        if(msg == null)
            message.addReplyMessage(reply);
        return msg;
    }

    public List<Message> getConversation(Long firstID, Long secondID) {
        Utilizator firstUser = this.userService.findUtilizator(firstID);
        Utilizator secondUser = this.userService.findUtilizator(secondID);
        List<Message> list = new ArrayList<>();
        if(firstUser==null || secondUser==null || firstID.equals(secondID))
            return list;
        Iterable<Message> messages = this.messageService.getAll();
        for(Message msg : messages) {
            List<Long> ids = new ArrayList<>();
            for(int i = 0 ; i < msg.getTo().size(); i++)
                ids.add(msg.getTo().get(i).getId());
            if ((msg.getFrom().getId().equals(firstID) && ids.contains(secondID)) ||
                    (msg.getFrom().getId().equals(secondID) && ids.contains(firstID))) {
                list.add(msg);
            }
        }
        return list;
    }

    public long getMaxRequestID(){
        return FriendReqService.getMaxID();
    }

    public FriendRequest sendFriendRequest(Long id, Long firstID, Long secondID) {
        if(firstID.equals(secondID))
            return new FriendRequest(null, null, Status.pending);
        Utilizator firstUser = this.userService.findUtilizator(firstID);
        Utilizator secondUser = this.userService.findUtilizator(secondID);
        if(firstUser==null || secondUser==null)
            return new FriendRequest(null, null, Status.pending);
        FriendRequest request = new FriendRequest(firstUser,secondUser,Status.pending);
        request.setId(id);
        return this.requestService.addRequest(request);
    }

    public Iterable<FriendRequest> getAllRequests() {
        return this.requestService.getAll();
    }

    public List<FriendRequest> getRequestsBySender(Utilizator user){
        List<FriendRequest> retList = new ArrayList<>();
        this.requestService.getAll().forEach(retList::add);
        return retList.stream().filter(x-> x.getFirstUser()==user).collect(Collectors.toList());
    }


    public List<FriendRequest> getRequestsByReceiver(Utilizator user) {
        List<FriendRequest> retList = new ArrayList<>();
        this.requestService.getAll().forEach(retList::add);
        return retList.stream().filter(x-> x.getSecondUser()==user).collect(Collectors.toList());
    }

    public FriendRequest replyToARequest(Long id, Status status) {
        FriendRequest request = this.requestService.findRequest(id);
        if(request == null || request.getStatus()!=Status.pending)
            return new FriendRequest(null,null, null);
        request.setStatus(status);
        Utilizator firstUser = request.getFirstUser();
        Utilizator secondUser = request.getSecondUser();
        Prietenie friendship = new Prietenie();
        friendship.setId(new Tuple<>(firstUser.getId(),secondUser.getId()));
        this.requestService.updateRequest(request);
        if(status == Status.approved && firstUser!=secondUser && this.userService.makeFriendship(firstUser, secondUser))
           if(this.friendService.addPrietenie(friendship)==null)
                return null;
           else
               return request;
        return null;
    }

    public void addFriendObserver(Observer<FriendshipChangeEvent> e){
        this.friendService.addObserver(e);
    }

    public void addRequestObserver(Observer<RequestChangeEvent> e){this.requestService.addObserver(e);}

    public FriendRequest deleteRequest(FriendRequest request) {
        return this.requestService.deleteRequest(request.getId());
    }

    public void addMessageObserver(Observer<MessageChangeEvent> e) {
        this.messageService.addObserver(e);
    }
}

