package socialnetwork.domain.validators;

import socialnetwork.domain.Utilizator;


public class UtilizatorValidator implements Validator<Utilizator> {

    /**
     *
     * @param entity
     * @throws ValidationException
     */
    @Override
    public void validate(Utilizator entity) throws ValidationException {
        //TODO: implement method validate
        if(entity.getId() <=0)
            throw  new IDException("Invalid Id!\n");
        if(entity.getFirstName().equals(""))
            throw  new FirstNameException("Invalid first name!\n");
        if(entity.getLastName().equals(""))
            throw  new LastNameException("Invalid last name!\n");
    }
}
