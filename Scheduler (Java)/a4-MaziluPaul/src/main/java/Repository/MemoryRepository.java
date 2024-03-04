package Repository;

import Domain.Entity;
import Utils.DuplicateObjectException;
import Utils.ObjectNotFoundException;
import Utils.RepositoryExceptions;

import java.util.ArrayList;

public class MemoryRepository<T extends Entity> extends AbstractRepository<T>{
    @Override
    public void add(T entity) throws RepositoryExceptions {
        if(entity == null){
            throw new IllegalArgumentException();
        }
        if(getById(entity.getId()) != null){
            throw new DuplicateObjectException("Cannot duplicate repository entities!");
        }
        entities.add(entity);
    }

    @Override
    public void delete(T entity) throws RepositoryExceptions {
        if(entity == null)
            throw new IllegalArgumentException();
        if(getById(entity.getId()) == null)
            throw new ObjectNotFoundException("Entity doesn't exist!");
        entities.remove(entity);
    }

    @Override
    public void update(T entity) throws RepositoryExceptions{
        if(entity == null)
            throw new IllegalArgumentException();

        if(getById(entity.getId()) == null)
            throw new ObjectNotFoundException("Entity doesn't exist!");

        for (int i = 0; i < entities.size(); i++) {
            if (entities.get(i).getId() == entity.getId())
                entities.set(i, entity);
        }
    }

    @Override
    public T getById(int id) {
        for (T entity : entities) {
            if (entity.getId() == id) {
                return entity;
            }
        }
        return null;
    }

    @Override
    public ArrayList<T> getAll() {
        return new ArrayList<T>(entities);
    }
}
