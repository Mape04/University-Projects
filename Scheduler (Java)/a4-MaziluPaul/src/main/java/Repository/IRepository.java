package Repository;

import Domain.Entity;
import Utils.ObjectNotFoundException;
import Utils.RepositoryExceptions;

import java.util.ArrayList;

public interface IRepository<T extends Entity> {
    void add(T entity) throws RepositoryExceptions;

    void delete(T entity) throws RepositoryExceptions;

    void  update(T entity) throws RepositoryExceptions;

    Entity getById(int id);

    ArrayList<T> getAll();
}
