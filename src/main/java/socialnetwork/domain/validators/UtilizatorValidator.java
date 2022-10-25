package socialnetwork.domain.validators;

import socialnetwork.domain.Utilizator;

import java.util.regex.Pattern;

public class UtilizatorValidator implements Validator<Utilizator> {

    /**
     *  valideaza un Utilizator
     *  @param entity
     *      entity nu poate avea campurile null
     * @throws ValidationException
     *                   daca un camp este null.
     */
    @Override
    public void validate(Utilizator entity) throws ValidationException {
        if(entity.getId()==null)
            throw new ValidationException("Id-ul nu poate fi vid!");
        if(entity.getId()<0)
            throw new ValidationException("Id-ul nu poate fi negativ!");
        if(entity.getFirstName()==null)
            throw new ValidationException("Primul nume nu poate fi vid!");
        if(entity.getLastName()==null)
            throw new ValidationException("Al doilea nume nu poate fi vid!");
        if (!Pattern.matches("[a-zA-Z]+",entity.getFirstName()))
            throw new ValidationException("Prenumele trebuie sa contina doar litere!");
        if (!Pattern.matches("[a-zA-Z]+",entity.getLastName()))
            throw new ValidationException("Numele trebuie sa contina doar litere!");
        if(entity.getPassword()==null)
            throw new ValidationException("Parola nu poate fi vida!");
        if(entity.getPassword().length()<3)
            throw new ValidationException("Parola nu poate avea mai putin de 3 caractere!");
        if(entity.getUsername()==null)
            throw new ValidationException("Username-ul nu poate fi vid!");
        if(entity.getUsername().length()<3)
            throw new ValidationException("Username-ul nu poate avea mai putin de 3 caractere!");
    }
}
