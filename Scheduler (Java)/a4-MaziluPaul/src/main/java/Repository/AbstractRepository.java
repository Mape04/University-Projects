package Repository;

import Domain.Entity;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRepository<T extends Entity> implements IRepository<T> {
    protected ArrayList<T> entities = new ArrayList<>();
}
