package Repository;

import Domain.Appointment;
import Domain.Patient;
import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AppointmentDbRepository extends MemoryRepository<Appointment> implements IDbRepository<Appointment> {
    private static final String JDBC_URL = "jdbc:sqlite:database.db";
    private Connection connection;

    public AppointmentDbRepository() {
        openConnection();
        createTable();
        generateAndSaveEntities();
        //initTable();
    }

    @Override
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

    @Override
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void createTable() {
        try (final Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS appointments(id int, id_patient int, date varchar(400), hour varchar(400), purpose varchar(400));");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initTable() {
        List<Appointment> appointments = new ArrayList<>();
        Patient patient1 = new Patient(1, "Ion", "Pop", 23);
        Patient patient2 = new Patient(2, "Maria", "Ionescu", 25);
        Patient patient3 = new Patient(3, "IRadu", "Verdea", 30);
        appointments.add(new Appointment(1, patient1, "2023-10-10", "10:00", "scop"));
        appointments.add(new Appointment(2, patient2, "2000-9-10", "11:00", "sco"));
        appointments.add(new Appointment(3, patient3, "2023-8-10", "12:00", "sc"));
        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO appointments values (?,?,?,?,?);")) {
            for (Appointment p : appointments) {
                stmt.setInt(1, p.getId());
                stmt.setInt(2, p.getPatient().getId());
                stmt.setString(3, p.getDate());
                stmt.setString(4, p.getHour());
                stmt.setString(5, p.getPurpose());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Appointment> getAll() {
        ArrayList<Appointment> appointments = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement("SELECT  * FROM appointments;")) {
            try (PreparedStatement stmt1 = connection.prepareStatement("SELECT  * FROM patients;")) {
                ResultSet rs1 = stmt1.executeQuery();
                while (rs1.next()) {
                    Patient patient = new Patient(rs1.getInt(1), rs1.getString(2), rs1.getString(3), rs1.getInt(4));
                    ResultSet rs = stmt.executeQuery();
                    while (rs.next()) {
                        if (patient.getId() == rs.getInt(2)) {
                            Appointment p = new Appointment(rs.getInt(1), patient, rs.getString(3), rs.getString(4), rs.getString(5));
                            appointments.add(p);
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    public void add(Appointment p) {
        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO appointments values (?,?,?,?,?);")) {
            stmt.setInt(1, p.getId());
            stmt.setInt(2, p.getPatient().getId());
            stmt.setString(3, p.getDate());
            stmt.setString(4, p.getHour());
            stmt.setString(5, p.getPurpose());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void update(Appointment p) {
        try (PreparedStatement stmt = connection.prepareStatement("UPDATE appointments SET id_patient=?, date=?, hour=?, purpose=? WHERE id=?;")) {
            stmt.setInt(1, p.getPatient().getId());
            stmt.setString(2, p.getDate());
            stmt.setString(3, p.getHour());
            stmt.setString(4, p.getPurpose());
            stmt.setInt(5, p.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Appointment p) {
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM appointments WHERE id=?;")) {
            stmt.setInt(1, p.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Appointment getById(int id) {
        Appointment appointment = null;
        Patient patient = null;

        try (PreparedStatement stmt1 = connection.prepareStatement("SELECT * FROM patients WHERE id = ?;")) {
            try {
                stmt1.setInt(1, id);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try (ResultSet rs1 = stmt1.executeQuery()) {
                if (rs1.next()) {
                    patient = new Patient(rs1.getInt("id"), rs1.getString("lastName"), rs1.getString("firstName"), rs1.getInt("age"));
                }
            } catch (SQLException e) {
                // Handle or log the exception
                e.printStackTrace();
            }

            try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM appointments WHERE id = ?;")) {
                stmt.setInt(1, id);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        appointment = new Appointment(rs.getInt("id"), patient, rs.getString("date"), rs.getString("hour"), rs.getString("purpose"));
                    }
                } catch (SQLException e) {
                    // Handle or log the exception
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                // Handle or log the exception
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointment;
    }

    private static void generateAndSaveEntities() {
        try (Connection connection = DriverManager.getConnection(JDBC_URL)) {
            generateAndSaveAppointments(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void generateAndSaveAppointments(Connection connection) throws SQLException {
        String insertAppointmentQuery = "INSERT INTO appointments (id, id_patient, date, hour, purpose) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertAppointmentQuery)) {
            // Get existing pacient IDs from the database
            Set<Integer> patientIds = getExistingPacientIds(connection);

            for (int i = 0; i < 10; i++) {
                int id;
                do {
                    id = new Random().nextInt(1000) + 1;
                } while (id == 0);

                int patientId = getRandomPatientId(patientIds);

                String date = generateRandomDate();
                String hour = generateRandomTime();
                String purpose = generateRandomPurpose();

                preparedStatement.setInt(1, id);
                preparedStatement.setInt(2, patientId);
                preparedStatement.setString(3, date);
                preparedStatement.setString(4, hour);
                preparedStatement.setString(5, purpose);

                preparedStatement.executeUpdate();
            }
        }
    }

    private static Set<Integer> getExistingPacientIds(Connection connection) throws SQLException {
        Set<Integer> patientIds = new HashSet<>();

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT id FROM patients");
            while (resultSet.next()) {
                patientIds.add(resultSet.getInt("id"));
            }
        }

        return patientIds;
    }

    private static int getRandomPatientId(Set<Integer> patientIds) {
        List<Integer> pacientIdsList = new ArrayList<>(patientIds);
        Random random = new Random();
        return pacientIdsList.get(random.nextInt(pacientIdsList.size()));
    }

    private static String generateRandomDate() {
        LocalDate startDate = LocalDate.of(2022, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);
        long startEpochDay = startDate.toEpochDay();
        long endEpochDay = endDate.toEpochDay();

        long randomDay = startEpochDay + (long) (Math.random() * (endEpochDay - startEpochDay + 1));
        LocalDate randomDate = LocalDate.ofEpochDay(randomDay);

        return randomDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    private static String generateRandomTime() {
        int randomHour = (int) (Math.random() * 24);
        int randomMinute = (int) (Math.random() * 60);

        return String.format("%02d:%02d", randomHour, randomMinute);
    }

    private static String generateRandomPurpose() {
        List<String> scopList = List.of("Consultatie", "Analize", "Tratament", "Vaccinare");
        Random random = new Random();
        return scopList.get(random.nextInt(scopList.size()));
    }

}


