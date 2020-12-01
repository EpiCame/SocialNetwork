package socialnetwork.repository.file;

import socialnetwork.domain.Entity;
import socialnetwork.domain.Prietenie;
import socialnetwork.domain.Tuple;
import socialnetwork.domain.Utilizator;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.RepositoryException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PrietenieFile extends AbstractFileRepository<Tuple<Long,Long>, Prietenie> {

    public PrietenieFile(String fileName, Validator<Prietenie> validator) {
        super(fileName, validator);
    }

    /**
     * extracts a friendship entity
     * @param attributes
     * @return a friendship
     */
    @Override
    public Prietenie extractEntity(List<String> attributes) {
        if(attributes.size()!=3 && attributes.size()!=2)
            throw new RepositoryException("Invalid arguments!\n");
        long firstId, secondId;
        LocalDateTime date=null;
        try{
            firstId = Long.parseLong(attributes.get(0));
            secondId = Long.parseLong(attributes.get(1));
            if(attributes.size()==3) {
                DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                date = LocalDateTime.parse(attributes.get(2), formatter);
            }
        }
        catch(NumberFormatException e){
            return null;
        }
        Prietenie prietenie = new Prietenie();
        if(attributes.size()==3) prietenie.setDate(date);
        prietenie.setId(new Tuple<>(firstId,secondId));
        return prietenie;
    }

    @Override
    protected String createEntityAsString(Prietenie entity) {
        return entity.getId().toString() + ";" + entity.getDate();
    }
}
