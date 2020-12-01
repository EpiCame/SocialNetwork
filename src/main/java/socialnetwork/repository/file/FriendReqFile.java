package socialnetwork.repository.file;

import socialnetwork.domain.FriendRequest;
import socialnetwork.domain.Status;
import socialnetwork.domain.Utilizator;
import socialnetwork.domain.validators.Validator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FriendReqFile extends AbstractFileRepository<Long, FriendRequest> {
    public FriendReqFile(String fileName, Validator<FriendRequest> validator) {
        super(fileName, validator);
    }

    @Override
    public FriendRequest extractEntity(List<String> attributes) {
        Long ID = Long.parseLong(attributes.get(0));
        Long firstID = Long.parseLong(attributes.get(1));
        Long secondID = Long.parseLong(attributes.get(2));
        Status status = Status.pending;
        if(attributes.get(3).equals("approved"))
            status = Status.approved;
        else if(attributes.get(3).equals("rejected"))
            status = Status.rejected;
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime date = LocalDateTime.parse(attributes.get(4), formatter);
        Utilizator firstUser = new Utilizator("","");
        Utilizator secondUser = new Utilizator("","");
        firstUser.setId(firstID);
        secondUser.setId(secondID);
        FriendRequest request = new FriendRequest(firstUser,secondUser,status);
        request.setId(ID);
        request.setDate(date);
        return request;
    }

    @Override
    protected String createEntityAsString(FriendRequest entity) {
        return entity.getId()+";"+entity.getFirstUser().getId()+";"+entity.getSecondUser().getId()+";"+entity.getStatus()+";"+entity.getDate();
    }


}
