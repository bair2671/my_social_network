package socialnetwork.domain.validators;

import socialnetwork.domain.Prietenie;

public class PrietenieValidator implements Validator<Prietenie> {
@Override
/**
 *  valideaza o Prietenie
 *  @param entity
 *      entity nu poate avea campurile null
 * @throws ValidationException
 *                   daca un camp este null sau utilizatorii sunt identici
 *
 */
public void validate(Prietenie entity) throws ValidationException {
        if(entity.getId()==null)
            throw new ValidationException("Id-ul nu poate fi vid!");
        if(entity.getId().getLeft()==null || entity.getId().getRight()==null)
            throw new ValidationException("Id-ul unui utilizator nu poate fi vid!");
        if(entity.getId().getLeft().equals(entity.getId().getRight()))
            throw new ValidationException("Utilizatorii trebuie sa fie diferiti!");
        if(entity.getDate()==null)
            throw new ValidationException("Data nu poate fi vida!");
        }
}

