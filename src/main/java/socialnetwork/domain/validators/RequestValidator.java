package socialnetwork.domain.validators;

import socialnetwork.domain.FriendRequest;

public class RequestValidator implements Validator<FriendRequest> {
    @Override
    public void validate(FriendRequest entity) throws ValidationException {
        if(entity.getId() <= 0)
            throw  new IDException("Invalid ID!\n");
    }
}
