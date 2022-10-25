package socialnetwork.repository.memory;

import socialnetwork.domain.Entity;
import socialnetwork.domain.validators.ExistentObjectException;
import socialnetwork.domain.validators.NonExistentObjectException;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.Repository;

import java.util.HashMap;
import java.util.Map;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID,E> {

    private Validator<E> validator;
    Map<ID,E> entities;

    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities = new HashMap<ID,E>();
    }

    @Override
    public E findOne(ID id){
        if (id==null)
            throw new IllegalArgumentException("Id-ul nu poate fi nul!");
        return entities.get(id);
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public E save(E entity) {
        if (entity==null)
            throw new IllegalArgumentException("Entitatea nu poate fi nula!");
        validator.validate(entity);
        if(entities.get(entity.getId()) != null) {
            throw new ExistentObjectException("Aceasta Entitate exista deja!");
        }
        else entities.put(entity.getId(),entity);
        return null;
    }

    @Override
    public E delete(ID id) {
        if (id==null)
            throw new IllegalArgumentException("Id-ul nu poate fi nul!");
        if(entities.get(id) == null) {
            throw new NonExistentObjectException("Aceasta Entitate nu exista!");
        }
        entities.remove(id);
        return null;
    }

    @Override
    public E update(E entity) {

        if(entity == null)
            throw new IllegalArgumentException("Entitatea nu poate fi nula!");
        validator.validate(entity);

        //entities.put(entity.getId(),entity);

        if(entities.get(entity.getId()) == null) {
           throw new NonExistentObjectException("Aceasta Entitate nu exista!"); //
        }
        entities.put(entity.getId(),entity);

        return null;

    }

    @Override
    public Iterable<E> findPage(int offset, int limit) {
        return null;
    }

}
