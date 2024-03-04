package Domain;

public interface IEntityFactory<T extends Entity> {
    String toString(T object);
    T createEntity(String line);
}