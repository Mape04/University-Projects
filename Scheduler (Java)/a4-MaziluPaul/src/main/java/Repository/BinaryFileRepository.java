package Repository;

import Domain.Entity;
import Utils.RepositoryExceptions;

import java.io.*;

public class BinaryFileRepository<T extends Entity> extends MemoryRepository<T> {
    private final String fileName;

    public BinaryFileRepository(String fileName) {
        this.fileName = fileName;
        try {
            loadFile();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadFile() throws IOException, ClassNotFoundException {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            Object entity;
            while (true) {
                try {
                    entity = inputStream.readObject();
                    super.add((T) entity);
                } catch (EOFException eofException) {
                    break;
                }
            }
        } catch (Exception e) {

        }
    }

    private void saveFile() throws IOException {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream((fileName)))) {
            for (T entity : super.getAll()) {
                outputStream.writeObject(entity);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void add(T entity) throws RepositoryExceptions {
        super.add(entity);
        try{
            saveFile();
        } catch (IOException e) {
            throw new RuntimeException("Error saving file " + e.getMessage(), e);
        }

    }

    @Override
    public void delete(T entity) throws RepositoryExceptions {
        super.delete(entity);
        try {
            saveFile();
        } catch (IOException e) {
            throw new RuntimeException("Error saving file " + e.getMessage(), e);
        }
    }

    @Override
    public void update(T entity) throws RepositoryExceptions {
        super.update(entity);
        try {
            saveFile();
        } catch (IOException e) {
            throw new RuntimeException("Error saving file " + e.getMessage(), e);
        }
    }
}

