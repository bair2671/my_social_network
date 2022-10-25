package socialnetwork.domain.validators;

import socialnetwork.domain.Message;
import socialnetwork.domain.Utilizator;

import java.util.regex.Pattern;

public class MessageValidator implements Validator<Message>{
    /**
     *  valideaza un Message
     *  @param entity
     *      entity nu poate avea campurile null
     * @throws ValidationException
     *                   daca un camp este null.
     */
    @Override
    public void validate(Message entity) throws ValidationException {
        if(entity.getId()==null)
            throw new ValidationException("Id-ul nu poate fi vid!");
        if(entity.getId()<0)
            throw new ValidationException("Id-ul nu poate fi negativ!");
        if(entity.getFrom()==null)
            throw new ValidationException("Numele expeditorului nu poate fi vid!");
        if(entity.getTo()==null || entity.getTo().isEmpty())
            throw new ValidationException("Lista destinatarilor nu poate fi vida!");
        if(entity.getMessage()==null)
            throw new ValidationException("Textul mesajului nu poate fi vid!");
        if(entity.getData()==null)
            throw new ValidationException("Data nu poate fi vida!");
        entity.getTo().forEach(x->{
            if(x.getId().equals(entity.getFrom().getId()))
                throw new  ValidationException("Un utilizator nu isi poate trimite singur mesaj!");
        });
    }
}
