package Repository;

import Domain.Entity;
import Domain.IEntityFactory;
import Utils.DuplicateObjectException;
import Utils.RepositoryExceptions;

import java.io.*;
import java.util.Scanner;

public class TextFileRepository<T extends Entity> extends MemoryRepository<T> {
    private String fileName;

    private IEntityFactory<T> entityFactory;

    public TextFileRepository(String fileName, IEntityFactory<T> entityFactory) {
        this.fileName = fileName;
        this.entityFactory = entityFactory;

        try {
            loadFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void add(T entity) throws RepositoryExceptions {
        try {
            super.add(entity);
        } catch (DuplicateObjectException e){
            throw new RepositoryExceptions(e.getMessage());
        }
        try {
            saveFile();
        } catch (IOException e) {
            throw new RuntimeException("Error saving object!", e);
        }
    }

    @Override
    public void delete(T entity) throws RepositoryExceptions {
        super.delete(entity);
        try{
            saveFile();
        } catch (IOException e) {
            throw new RuntimeException("Error saving object!", e);
        }
    }

    @Override
    public void update(T entity) throws RepositoryExceptions {
        super.update(entity);
        try{
            saveFile();
        } catch (IOException e) {
            throw new RuntimeException("Error saving object!", e);
        }
    }

    private void loadFile() throws IOException {
        // delete whatever is in the repo's memory list
        entities.clear();

        File file = new File(fileName);

        // Check if the file exists
        if (!file.exists()) {
            // You can create the file if it doesn't exist
            file.createNewFile();
            return; // No need to read further if the file is new
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            while (line != null && !line.isEmpty()) {
                entities.add(entityFactory.createEntity(line));
                line = br.readLine();
            }
        }
    }


    private void saveFile() throws IOException {
        try (FileWriter fw = new FileWriter(fileName)) {
            for (T object : entities) {
                fw.write(entityFactory.toString(object));
                fw.write("\r\n");
            }
        }
    }
}
