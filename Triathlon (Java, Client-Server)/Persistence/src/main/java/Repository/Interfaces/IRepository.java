package Repository.Interfaces;

import Domain.Identifiable;

import java.util.Collection;

public interface IRepository<T extends Identifiable<Tid>, Tid> {
    void add(T elem);
    void delete(T elem);
    void update(T elem, Tid id);
    T findById(Tid id);
    Iterable<T> findAll();
    Collection<T> getAll();

}
