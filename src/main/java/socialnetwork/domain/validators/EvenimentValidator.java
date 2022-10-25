package socialnetwork.domain.validators;

import socialnetwork.domain.Eveniment;

public class EvenimentValidator implements Validator<Eveniment>{
    /**
     *  valideaza un Eveniment
     *  @param entity
     *      entity nu poate avea campurile null
     * @throws ValidationException
     *                   daca un camp este null.
     */
    @Override
    public void validate(Eveniment entity) throws ValidationException {
        if(entity.getId()==null)
            throw new ValidationException("Id-ul nu poate fi vid!");
        if(entity.getId()<0)
            throw new ValidationException("Id-ul nu poate fi negativ!");
        if(entity.getNume()==null)
            throw new ValidationException("Numele nu poate fi vid!");
        if(entity.getNume()=="")
            throw new ValidationException("Numele nu poate fi vid!");
        if(entity.getData()==null)
            throw new ValidationException("Data nu poate fi vida!");
    }
}
