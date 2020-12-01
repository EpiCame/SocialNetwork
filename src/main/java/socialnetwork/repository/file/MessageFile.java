package socialnetwork.repository.file;

import socialnetwork.domain.Message;
import socialnetwork.domain.Utilizator;
import socialnetwork.domain.validators.Validator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MessageFile extends AbstractFileRepository<Long, Message> {
    public MessageFile(String fileName, Validator<Message> validator) {
        super(fileName, validator);
    }


    @Override
    public Message extractEntity(List<String> attributes) {
        Long msgID = Long.parseLong(attributes.get(0));
        Long fromID = Long.parseLong(attributes.get(1));
        Utilizator user = new Utilizator("", "");
        user.setId(fromID);
        String[] args = attributes.get(2).split(" ");
        List<Utilizator> toList = new ArrayList<>();
        Utilizator toUser;
        for(String str : args){
            Long ID = Long.parseLong(str);
            toUser = new Utilizator("", "");
            toUser.setId(ID);
            toList.add(toUser);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime date = LocalDateTime.parse(attributes.get(3), formatter);
        String msg = attributes.get(4);
        Message message = new Message(user, toList,date, msg);
        message.setId(msgID);
        return message;
    }


    @Override
    protected String createEntityAsString(Message entity) {
        Optional<String> to = entity.getTo().stream().map(x->x.getId().toString())
                .reduce((x,y)->{return x+" "+y;});
        String strTo = to.get();
        return entity.getId() + ";" + entity.getFrom().getId() + ";" +strTo+ ";" + entity.getDate() + ";" + entity.getMessage();
    }
}
