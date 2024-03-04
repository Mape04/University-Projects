package Repository;

import Domain.Patient;
import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.*;

public class PatientDbRepository extends MemoryRepository<Patient> implements IDbRepository<Patient> {
    private static String JDBC_URL = "jdbc:sqlite:database.db";

    private Connection connection;

    public PatientDbRepository() {
        openConnection();
        createTable();
        generateAndSaveEntities();
        //initTable();
    }

    public void openConnection() {
        SQLiteDataSource ds = new SQLiteDataSource();
        ds.setUrl(JDBC_URL);

        try {
            if (connection == null || connection.isClosed()) {
                connection = ds.getConnection();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void createTable() {
        try (final Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS patients(id int , lastName varchar(400), firstName varchar(400), age int);");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void initTable() {
        List<Patient> patients = new ArrayList<>();
        patients.add(new Patient(1, "Ion", "Pop", 23));
        patients.add(new Patient(2, "Maria", "Ionescu", 25));
        patients.add(new Patient(3, "IRadu", "Verdea", 30));
        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO patients values (?,?,?,?);")) {
            for (Patient p : patients) {
                stmt.setInt(1, p.getId());
                stmt.setString(2, p.getLastName());
                stmt.setString(3, p.getFirstName());
                stmt.setInt(4, p.getAge());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Patient> getAll() {
        ArrayList<Patient> patients = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * from patients;")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Patient p = new Patient(rs.getInt(1), rs.getString(2),rs.getString(3), rs.getInt(4));
                patients.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return patients;
    }

    @Override
    public void add(Patient p) {
        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO patients values (?,?,?,?);")) {
            stmt.setInt(1, p.getId());
            stmt.setString(2, p.getLastName());
            stmt.setString(3, p.getFirstName());
            stmt.setInt(4, p.getAge());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Patient p) {
        try (PreparedStatement stmt = connection.prepareStatement("UPDATE patients SET lastName=?, firstName=?, age=? WHERE id=?;")) {
            stmt.setString(1, p.getLastName());
            stmt.setString(2, p.getFirstName());
            stmt.setInt(3, p.getAge());
            stmt.setInt(4, p.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Patient p) {
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM patients WHERE id=?;")) {
            stmt.setInt(1, p.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Patient getById(int id) {
        Patient patient = null;
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM patients WHERE id = ?;")) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    patient = new Patient(rs.getInt("id"), rs.getString("lastName"), rs.getString("firstName"), rs.getInt("age"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patient;
    }


    private static void generateAndSaveEntities() {
        try (Connection connection = DriverManager.getConnection(JDBC_URL)) {
            generateAndSavePatients(connection);;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void generateAndSavePatients(Connection connection) throws SQLException {
        String insertPatientQuery = "INSERT INTO patients (id, lastName, firstName, age) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertPatientQuery)) {
            Set<Integer> generatedIds = new HashSet<>();

            for (int i = 0; i < 10; i++) {
                int id;
                do {
                    id = new Random().nextInt(1000) + 1;
                } while (id == 0 || !generatedIds.add(id));

                String lastName = generateRandomName();
                String firstName = generateRandomName();
                int age = generateRandomAge();

                preparedStatement.setInt(1, id);
                preparedStatement.setString(2, lastName);
                preparedStatement.setString(3, firstName);
                preparedStatement.setInt(4, age);

                preparedStatement.executeUpdate();
            }
        }
    }


    private static String generateRandomName() {
        List<String> names = List.of("John", "Jane", "Michael", "Emma", "Daniel", "Olivia", "David", "Sophia");
        Random random = new Random();
        return names.get(random.nextInt(names.size()));
    }

    private static int generateRandomAge() {
        return new Random().nextInt(40) + 18;
    }
}

