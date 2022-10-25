package socialnetwork.repository.database;

import socialnetwork.domain.Entity;
import socialnetwork.domain.validators.Validator;
import socialnetwork.repository.Repository;

public abstract class AbstractDbRepository<ID, E extends Entity<ID>> implements Repository<ID,E> {

    protected String url;
    protected String username;
    protected String password;
    protected Validator<E> validator;
    protected String table;

    public AbstractDbRepository(String url, String username, String password, Validator<E> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public abstract E findOne(ID id);

    @Override
    public abstract Iterable<E> findAll();

    @Override
    public abstract E save(E entity);

    @Override
    public abstract E delete(ID id);

    @Override
    public abstract E update(E entity);

    @Override
    public abstract Iterable<E> findPage(int offset,int limit);
}
