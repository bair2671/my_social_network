package socialnetwork.domain.validators;

import socialnetwork.domain.UtilizatorEveniment;

public class UtilizatorEvenimentValidator implements Validator<UtilizatorEveniment>{
    /**
     *  valideaza un UtilizatorEveniment
     *  @param entity
     *      entity nu poate avea campurile null
     * @throws ValidationException
     *                   daca un camp este null.
     */
    @Override
    public void validate(UtilizatorEveniment entity) throws ValidationException {
        if(entity.getId()==null)
            throw new ValidationException("Id-ul nu poate fi vid!");
        if(entity.getId().getLeft()==null)
            throw new ValidationException("Id-ul utilizatorului nu poate fi vid!");
        if(entity.getId().getRight()==null)
            throw new ValidationException("Id-ul evenimetului nu poate fi vid!");
    }
}
