package socialnetwork.domain.validators;

import socialnetwork.domain.Message;

public class MessageValidator implements Validator<Message> {
    @Override
    public void validate(Message entity) throws ValidationException {
        if(entity.getId()<0)
            throw new IDException("Invalid ID!\n");
        if(entity.getMessage().isEmpty())
            throw new MessageException("Invalid message!\n");
    }
}
