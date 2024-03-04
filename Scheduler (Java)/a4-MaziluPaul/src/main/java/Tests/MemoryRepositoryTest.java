package Tests;

import Domain.Patient;
import Repository.MemoryRepository;
import Utils.DuplicateObjectException;
import Utils.ObjectNotFoundException;
import Utils.RepositoryExceptions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class MemoryRepositoryTest {
    MemoryRepository<Patient> repository = new MemoryRepository<>();

    @Test
    public void testAdd() throws RepositoryExceptions {
        repository.add(new Patient(1, "one", "one", 1));
        repository.add(new Patient(2, "two", "two", 2));
        repository.add(new Patient(3, "three", "three", 3));

        assert 3 == repository.getAll().size();

        assertThrows(IllegalArgumentException.class, () -> repository.add(null));
        assertThrows(DuplicateObjectException.class, () -> repository.add(new Patient(3, "three", "three", 3)));
    }

    @Test
    public void testUpdate() throws RepositoryExceptions {
        repository.add(new Patient(1, "one", "one", 1));
        Patient patient = new Patient(1, "four", "four", 4);
        repository.update(patient);

        assertThrows(IllegalArgumentException.class, () -> repository.update(null));
        assertThrows(ObjectNotFoundException.class, () -> repository.update(new Patient(4, "four", "four", 4)));

        assert repository.getById(1).equals(patient);
    }

    @Test
    public void testDelete() throws RepositoryExceptions {
        repository.add(new Patient(1, "one", "one", 1));
        repository.add(new Patient(2, "two", "two", 2));
        repository.add(new Patient(3, "three", "three", 3));

        repository.delete(new Patient(2, "two", "two", 2));

        assertThrows(IllegalArgumentException.class, () -> repository.delete(null));

        assertThrows(ObjectNotFoundException.class, () -> repository.delete(new Patient(2, "two", "two", 2)));

        assert repository.getById(1).equals(new Patient(1, "one", "one", 1));
        assert repository.getById(3).equals(new Patient(3, "three", "three", 3));

        assert 2 == repository.getAll().size();
    }
}
