package socialnetwork.domain.validators;

public interface Validator<T> {
    /**
     *  valideaza o entitate
     *  @param entity
     *      entity nu poate avea campurile null
     * @throws ValidationException
     *      daca un camp este null.
     */
    void validate(T entity) throws ValidationException;
}