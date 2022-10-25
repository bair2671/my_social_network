package socialnetwork.domain.validators;

import socialnetwork.domain.CerereDePrietenie;
import socialnetwork.domain.Message;

public class CerereDePrietenieValidator implements Validator<CerereDePrietenie>{
    /**
     *  valideaza o CerereDePrietenie
     *  @param entity
     *      entity nu poate avea campurile null
     * @throws ValidationException
     *                   daca un camp este null.
     */
    @Override
    public void validate(CerereDePrietenie entity) throws ValidationException {
        if(entity.getId()==null)
            throw new ValidationException("Id-ul nu poate fi vid!");
        if(entity.getId()<0)
            throw new ValidationException("Id-ul nu poate fi negativ!");
        if(entity.getIdExpeditor()==null)
            throw new ValidationException("Expeditorul nu poate fi vid!");
        if(entity.getIdDestinatar()==null)
            throw new ValidationException("Destinatarul nu poate fi vid!");
        if(entity.getData()==null)
            throw new ValidationException("Data nu poate fi vida!");
    }
}
