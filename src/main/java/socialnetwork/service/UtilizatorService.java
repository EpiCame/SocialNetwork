package socialnetwork.service;

import socialnetwork.domain.Prietenie;
import socialnetwork.domain.Utilizator;
import socialnetwork.domain.validators.UtilizatorValidator;
import socialnetwork.domain.validators.ValidationException;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.Repository;
import socialnetwork.repository.file.AbstractFileRepository;
import socialnetwork.repository.file.UtilizatorFile;

import java.util.List;

public class UtilizatorService  {
    private AbstractFileRepository<Long, Utilizator> repo;

    public UtilizatorService(AbstractFileRepository<Long, Utilizator> repo) {
        this.repo = repo;
    }

    public Utilizator extractUser(List<String> args){
        return this.repo.extractEntity(args);
    }

    public Utilizator addUtilizator(Utilizator user) {
        return repo.save(user);
    }

    public Iterable<Utilizator> getAll(){
        return repo.findAll();
    }

    public Utilizator deleteUtilizator(Long ID) {
        return this.repo.delete(ID);
    }

    public Utilizator findUtilizator(Long ID){
        return this.repo.findOne(ID);
    }

    public boolean makeFriendship(Utilizator firstUser, Utilizator secondUser){
        if(firstUser != null && secondUser!=null) {
            firstUser.addFriend(secondUser);
            secondUser.addFriend(firstUser);
            return true;
        }
        return false;
    }

    public boolean deleteFriendship(Utilizator firstUser , Utilizator secondUser ) {
        if(firstUser != null && secondUser!=null) {
            firstUser.deleteFriend(secondUser);
            secondUser.deleteFriend(firstUser);
            return true;
        }
        return false;
    }

    ///TO DO: add other methods
}
