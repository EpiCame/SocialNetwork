package socialnetwork.domain.validators;

import socialnetwork.domain.Prietenie;
import socialnetwork.domain.Tuple;

public class PrietenieValidator implements Validator<Prietenie> {

     /**
     *
     * @param entity
     * @throws ValidationException
     */
    @Override
    public void validate(Prietenie entity) throws ValidationException {
        Tuple<Long,Long> id = entity.getId();
        if(id.getLeft()<=0 || id.getRight()<=0)
            throw  new FriendshipException("Invalid friendship!\n");
    }
}
