package socialnetwork.service;

import socialnetwork.domain.Message;
import socialnetwork.domain.Utilizator;
import socialnetwork.repository.file.AbstractFileRepository;

import java.util.List;

public class MessageService {
    private AbstractFileRepository<Long, Message> repo;

    public MessageService(AbstractFileRepository<Long, Message> repo) {
        this.repo = repo;
    }

    public Message extractMessage(List<String> args){
        return this.repo.extractEntity(args);
    }

    public Message addMessage(Message msg) {
        return repo.save(msg);
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

}
