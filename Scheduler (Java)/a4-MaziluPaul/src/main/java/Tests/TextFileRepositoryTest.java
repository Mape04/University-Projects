package Tests;

import Domain.Patient;
import Domain.PatientFactory;
import Repository.TextFileRepository;
import Utils.RepositoryExceptions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TextFileRepositoryTest {
    private static final String TEST_FILE_PATH = "test.txt";
    private TextFileRepository<Patient> repository;

    @BeforeEach
    void setUp() throws RepositoryExceptions {
        // Initialize the repository and create an initial file for testing
        repository = new TextFileRepository<Patient>(TEST_FILE_PATH, new PatientFactory());
        repository.add(new Patient(1, "one", "one", 1));
        repository.add(new Patient(2, "two", "two", 2));
    }

    @AfterEach
    void tearDown() throws IOException {
        // Delete the test file after each test
        Files.deleteIfExists(Path.of(TEST_FILE_PATH));
    }

    @Test
    public void testAdd() throws RepositoryExceptions {
        Patient patient = new Patient(3, "three", "three", 3);
        repository.add(patient);
        assert 3 == repository.getAll().size();

        Patient patient1 = new Patient(3, "three", "three", 3);
        assertThrows(RepositoryExceptions.class, () -> repository.add(patient1));
    }

    @Test
    public void testDelete() throws RepositoryExceptions {
        repository.delete(new Patient(2, "two", "two", 2));
        assert 1 == repository.getAll().size();
        assert repository.getAll().get(0).equals(new Patient(1, "one", "one", 1));
    }

    @Test
    public void testUpdate() throws RepositoryExceptions {
        Patient patient3 = new Patient(2, "four", "four", 4);
        repository.update(patient3);
        assert repository.getAll().get(1).equals(patient3);

    }

    @Test
    void testLoadFileFromExistingFile() throws IOException {
        // Create a test file with some content
        createTestFile("1,John,Doe,1\n2,Jane,Doe,2\n3,Bob,Smith,3");

        repository = new TextFileRepository<>(TEST_FILE_PATH, new PatientFactory());

        // Ensure that entities are loaded from the existing file
        List<Patient> entities = repository.getAll();
        assertEquals(3, entities.size(), "Incorrect number of entities loaded from file");
    }

    @Test
    void testLoadFileFromNonExistingFile() throws IOException {
        // Ensure the test file does not exist initially
        File file = new File(TEST_FILE_PATH);
        if (file.exists()) {
            file.delete();
        }

        repository = new TextFileRepository<>(TEST_FILE_PATH, new PatientFactory());

        // Ensure that no entities are loaded from the non-existing file
        List<Patient> entities = repository.getAll();
        assertTrue(entities.isEmpty(), "Entities should be empty for a non-existing file");
    }

    // Helper method to create a test file with the given content
    private void createTestFile(String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_FILE_PATH))) {
            writer.write(content);
        }
    }
}
