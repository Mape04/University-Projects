package Repository.DBRepositories;

import Domain.Challenge;
import Domain.Participant;
import Domain.Result;
import Repository.Interfaces.IResultRepository;
import Repository.Utils.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class ResultDBRepository implements IResultRepository {
    private JdbcUtils dbUtils;

    private static final Logger logger = LogManager.getLogger();

    public ResultDBRepository(Properties properties){
        logger.info("Initializing ResultDBRepository with properties: {} ", properties);
        dbUtils = new JdbcUtils(properties);
    }

    @Override
    public void add(Result elem) {
        logger.traceEntry("saving task {} ", elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("insert into Results (ID_Participant, ID_Challenge, Points) values (?,?,?)")) {
            preStmt.setInt(1, elem.getParticipant().getId());
            preStmt.setInt(2, elem.getChallenge().getId());
            preStmt.setInt(3, elem.getPoints());
            int resultSet = preStmt.executeUpdate();
            logger.trace("Saved {} instances", resultSet);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Result elem) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("DELETE FROM Results WHERE ID = ?")) {
            preStmt.setInt(1, elem.getId());
            int rowsDeleted = preStmt.executeUpdate();
            if (rowsDeleted > 0) {
                logger.info("Result deleted successfully: " + elem);
            } else {
                logger.warn("Result with ID " + elem.getId() + " not found in the database.");
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit();
    }


    @Override
    public void update(Result elem, Integer id) {
        logger.traceEntry("updating result with Id {} with details: {}", id, elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("UPDATE Results SET ID_Participant=?, ID_Challenge=?, Points=? WHERE ID=?")) {
            preStmt.setInt(1, elem.getParticipant().getId());
            preStmt.setInt(2, elem.getChallenge().getId());
            preStmt.setInt(3, elem.getPoints());
            preStmt.setInt(4, id); // Set the ID to identify the row to update
            int resultSet = preStmt.executeUpdate();
            if (resultSet > 0) {
                logger.trace("Updated {} instance(s)", resultSet);
            } else {
                logger.trace("No instance updated");
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error updating result: " + ex);
        }
        logger.traceExit();
    }


    @Override
    public Result findById(Integer id) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        Result result = null;
        try (PreparedStatement preStmt = con.prepareStatement("select * from Results where ID = ?")) {
            preStmt.setInt(1, id);
            try (ResultSet resultSet = preStmt.executeQuery()) {
                if (resultSet.next()) {

                    Integer id_participant = resultSet.getInt("ID_Participant");
                    logger.traceEntry();
                    Participant participant = null;
                    try (PreparedStatement stmt = con.prepareStatement("select * from Participants where ID = ?")) {
                        stmt.setInt(1, id_participant);
                        try (ResultSet resultSet1 = stmt.executeQuery()) {
                            if (resultSet1.next()) {
                                String name = resultSet1.getString("Name");
                                String ssn = resultSet1.getString("SSN");
                                participant = new Participant(name, ssn);
                                participant.setId(id_participant);
                            }
                        }
                    } catch (SQLException e) {
                        logger.error(e);
                        System.err.println("Error DB " + e);
                    }
                    logger.traceExit();


                    Integer id_challenge = resultSet.getInt("ID_Challenge");
                    logger.traceEntry();
                    Challenge challenge = null;
                    try (PreparedStatement preStmt1 = con.prepareStatement("select * from Challenges where ID = ?")) {
                        preStmt1.setInt(1, id_challenge);
                        try (ResultSet resultSet2 = preStmt1.executeQuery()) {
                            if (resultSet2.next()) {
                                String dateString = resultSet2.getString("Date");
                                Date date = Date.valueOf(dateString);
                                String name = resultSet2.getString("Name");
                                challenge = new Challenge(name, date);
                                challenge.setId(id_challenge);
                            }
                        }
                    } catch (SQLException e) {
                        logger.error(e);
                        System.err.println("Error DB " + e);
                    }
                    logger.traceExit();

                    Integer points = resultSet.getInt("Points");
                    result = new Result(participant, challenge, points);
                    result.setId(id);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit();
        return result;
    }

    @Override
    public Iterable<Result> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();

        List<Result> results = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Results")) {
            try (ResultSet resultSet = preStmt.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("ID");

                    int id_participant = resultSet.getInt("ID_Participant");
                    logger.traceEntry();
                    Participant participant = null;
                    try (PreparedStatement stmt = con.prepareStatement("select * from Participants where ID = ?")) {
                        stmt.setInt(1, id_participant);
                        try (ResultSet resultSet1 = stmt.executeQuery()) {
                            if (resultSet1.next()) {
                                String name = resultSet1.getString("Name");
                                String ssn = resultSet1.getString("SSN");
                                participant = new Participant(name, ssn);
                                participant.setId(id_participant);
                            }
                        }
                    } catch (SQLException e) {
                        logger.error(e);
                        System.err.println("Error DB " + e);
                    }
                    logger.traceExit();

                    int id_challenge = resultSet.getInt("ID_Challenge");
                    logger.traceEntry();
                    Challenge challenge = null;
                    try (PreparedStatement preStmt1 = con.prepareStatement("select * from Challenges where ID = ?")) {
                        preStmt1.setInt(1, id_challenge);
                        try (ResultSet resultSet2 = preStmt1.executeQuery()) {
                            if (resultSet2.next()) {
                                String dateString = resultSet2.getString("Date");
                                Date date = Date.valueOf(dateString);
                                String name = resultSet2.getString("Name");
                                challenge = new Challenge(name, date);
                                challenge.setId(id_challenge);
                            }
                        }
                    } catch (SQLException e) {
                        logger.error(e);
                        System.err.println("Error DB " + e);
                    }
                    logger.traceExit();

                    int points = resultSet.getInt("Points");

                    Result result = new Result(participant, challenge, points);
                    result.setId(id);
                    results.add(result);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit();
        return results;
    }

    @Override
    public Collection<Result> getAll() {
        Iterable<Result> results = findAll();
        return (Collection<Result>) results;
    }

    @Override
    public Result getResult(int participantId, int challengeId) {
        Participant participant = getParticipantById(participantId);
        Challenge challenge = getChallengeById(challengeId);
        if (participant != null && challenge != null) {
            try (Connection con = dbUtils.getConnection();
                 PreparedStatement preStmt = con.prepareStatement("SELECT Points FROM Results WHERE ID_Participant = ? AND ID_Challenge = ?")) {
                preStmt.setInt(1, participantId);
                preStmt.setInt(2, challengeId);
                try (ResultSet resultSet = preStmt.executeQuery()) {
                    if (resultSet.next()) {
                        int points = resultSet.getInt("Points");
                        return new Result(participant, challenge, points);
                    }
                }
            } catch (SQLException e) {
                logger.error("Error retrieving result: " + e.getMessage());
            }
        }
        return null;
    }

    private Participant getParticipantById(int id) {
        try (Connection con = dbUtils.getConnection();
             PreparedStatement preStmt = con.prepareStatement("SELECT Name FROM Participants WHERE ID = ?")) {
            preStmt.setInt(1, id);
            try (ResultSet resultSet = preStmt.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("Name");
                    String ssn = resultSet.getString("SSN");
                    return new Participant(name, ssn);
                }
            }
        } catch (SQLException e) {
            logger.error("Error retrieving participant: " + e.getMessage());
        }
        return null;
    }

    private Challenge getChallengeById(int id) {
        try (Connection con = dbUtils.getConnection();
             PreparedStatement preStmt = con.prepareStatement("SELECT Name FROM Challenges WHERE ID = ?")) {
            preStmt.setInt(1, id);
            try (ResultSet resultSet = preStmt.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("Name");
                    String dateString = resultSet.getString("Date");
                    Date date = Date.valueOf(dateString);
                    return new Challenge(name, date);
                }
            }
        } catch (SQLException e) {
            logger.error("Error retrieving challenge: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean hasResult(Participant participant, Challenge challenge) {
        logger.traceEntry();
        boolean resultExists = false;
        try (Connection con = dbUtils.getConnection();
             PreparedStatement preStmt = con.prepareStatement("SELECT COUNT(*) FROM Results WHERE ID_Participant = ? AND ID_Challenge = ?")) {
            preStmt.setInt(1, participant.getId());
            preStmt.setInt(2, challenge.getId());
            try (ResultSet resultSet = preStmt.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    resultExists = count > 0;
                }
            }
        } catch (SQLException e) {
            logger.error("Error checking if result exists: " + e.getMessage());
        }
        logger.traceExit();
        return resultExists;
    }

    @Override
    public int getTotalPointsBySsn(String ssn) {
        int totalPoints = 0;
        try (Connection con = dbUtils.getConnection()) {
            // Step 1: Find participant by SSN and retrieve all associated results
            String sql = "SELECT P.ID as ParticipantID, P.Name as ParticipantName, R.ID_Challenge, R.Points " +
                    "FROM Participants P " +
                    "JOIN Results R ON P.ID = R.ID_Participant " +
                    "WHERE P.SSN = ?";
            try (PreparedStatement preStmt = con.prepareStatement(sql)) {
                preStmt.setString(1, ssn);
                try (ResultSet resultSet = preStmt.executeQuery()) {
                    // Initialize participant information
                    Participant participant = null;
                    List<Result> results = new ArrayList<>();

                    // Process each row in the result set
                    while (resultSet.next()) {
                        if (participant == null) {
                            // Create participant if not already initialized
                            String participantName = resultSet.getString("ParticipantName");
                            int participantId = resultSet.getInt("ParticipantID");
                            participant = new Participant(participantName, ssn);
                            participant.setId(participantId);
                        }

                        // Retrieve challenge and points from the result set
                        int challengeId = resultSet.getInt("ID_Challenge");
                        int points = resultSet.getInt("Points");

                        // Fetch challenge details
                        Challenge challenge = getChallengeById(con, challengeId);

                        if (challenge != null) {
                            // Create Result instance and add to results list
                            Result result = new Result(participant, challenge, points);
                            results.add(result);
                        }
                    }

                    // Calculate total points from all results
                    for (Result result : results) {
                        totalPoints += result.getPoints();
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error retrieving total points for participant with SSN " + ssn + ": " + e.getMessage());
        }
        return totalPoints;
    }

    // Helper method to fetch challenge details by ID
    private Challenge getChallengeById(Connection con, int challengeId) throws SQLException {
        Challenge challenge = null;
        String sql = "SELECT * FROM Challenges WHERE ID = ?";
        try (PreparedStatement preStmt = con.prepareStatement(sql)) {
            preStmt.setInt(1, challengeId);
            try (ResultSet resultSet = preStmt.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("Name");

                    String dateString = resultSet.getString("Date");
                    Date date = Date.valueOf(dateString);

                    challenge = new Challenge(name, date);
                    challenge.setId(challengeId);
                }
            }
        }
        return challenge;
    }





}
