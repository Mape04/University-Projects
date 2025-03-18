package Repository.DBRepositories;

import Domain.Challenge;
import Domain.Referee;
import Repository.Interfaces.IRefereeRepository;

import Repository.Utils.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;


@Component
public class RefereeDBRepository implements IRefereeRepository {
    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    @Autowired
    public RefereeDBRepository(@Qualifier("dbProperties") Properties properties) {
        logger.info("Initializing RefereeDBRepository with properties: {}", properties);
        dbUtils = new JdbcUtils(properties);
    }

    @Override
    public void add(Referee elem) {
        logger.traceEntry("Saving referee: {}", elem);
        try (Connection con = dbUtils.getConnection();
             PreparedStatement preStmt = con.prepareStatement("INSERT INTO Referees (Name, Password, ID_Challenge) VALUES (?, ?, ?)")) {
            preStmt.setString(1, elem.getName());
            preStmt.setString(2, elem.getPassword());
            preStmt.setInt(3, elem.getChallenge().getId());
            int result = preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error adding referee to database: " + ex.getMessage());
        }
        logger.traceExit();
    }

    @Override
    public void delete(Referee elem) {
        logger.traceEntry("Deleting referee: {}", elem);
        try (Connection con = dbUtils.getConnection();
             PreparedStatement preStmt = con.prepareStatement("DELETE FROM Referees WHERE ID = ?")) {
            preStmt.setInt(1, elem.getId());
            int rowsDeleted = preStmt.executeUpdate();
            if (rowsDeleted > 0) {
                logger.info("Referee deleted successfully: " + elem);
            } else {
                logger.warn("Referee with ID " + elem.getId() + " not found in the database.");
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error deleting referee from database: " + e.getMessage());
        }
        logger.traceExit();
    }

    @Override
    public void update(Referee elem, Integer id) {
        logger.traceEntry("Updating referee with ID {}: {}", id, elem);
        try (Connection con = dbUtils.getConnection();
             PreparedStatement preStmt = con.prepareStatement("UPDATE Referees SET Name=?, Password=?, ID_Challenge=? WHERE ID=?")) {
            preStmt.setString(1, elem.getName());
            preStmt.setString(2, elem.getPassword());
            preStmt.setInt(3, elem.getChallenge().getId());
            preStmt.setInt(4, id);
            int result = preStmt.executeUpdate();
            logger.trace("Updated {} instance(s)", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error updating referee in database: " + ex.getMessage());
        }
        logger.traceExit();
    }

    @Override
    public Referee findById(Integer id) {
        logger.traceEntry("Finding referee by ID: {}", id);
        Referee referee = null;
        try (Connection con = dbUtils.getConnection();
             PreparedStatement preStmt = con.prepareStatement("SELECT r.*, c.Name AS ChallengeName, c.Date AS ChallengeDate FROM Referees r JOIN Challenges c ON r.ID_Challenge = c.ID WHERE r.ID = ?")) {
            preStmt.setInt(1, id);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    String name = result.getString("Name");
                    String password = result.getString("Password");
                    int challengeId = result.getInt("ID_Challenge");
                    String challengeName = result.getString("ChallengeName");
                    String challengeDateString = result.getString("ChallengeDate");
                    Date challengeDate = Date.valueOf(challengeDateString);
                    Challenge challenge = new Challenge(challengeName, challengeDate);
                    challenge.setId(challengeId);
                    referee = new Referee(name, password, challenge);
                    referee.setId(id);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error finding referee in database: " + e.getMessage());
        }
        logger.traceExit();
        return referee;
    }

    @Override
    public Iterable<Referee> findAll() {
        logger.traceEntry("Finding all referees");
        List<Referee> referees = new ArrayList<>();
        try (Connection con = dbUtils.getConnection();
             PreparedStatement preStmt = con.prepareStatement("SELECT r.*, c.Name AS ChallengeName, c.Date AS ChallengeDate FROM Referees r JOIN Challenges c ON r.ID_Challenge = c.ID")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("ID");
                    String name = result.getString("Name");
                    String password = result.getString("Password");
                    int challengeId = result.getInt("ID_Challenge");
                    String challengeName = result.getString("ChallengeName");
                    String challengeDateString = result.getString("ChallengeDate");
                    Date challengeDate = Date.valueOf(challengeDateString);
                    Challenge challenge = new Challenge(challengeName, challengeDate);
                    challenge.setId(challengeId);
                    Referee referee = new Referee(name, password, challenge);
                    referee.setId(id);
                    referees.add(referee);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error finding all referees in database: " + e.getMessage());
        }
        logger.traceExit();
        return referees;
    }

    @Override
    public Collection<Referee> getAll() {
        Iterable<Referee> referees = findAll();
        return (Collection<Referee>) referees;
    }

    @Override
    public Referee getRefereeByName(String name) {
        logger.traceEntry("Finding referee by name: {}", name);
        Referee referee = null;
        try (Connection con = dbUtils.getConnection();
             PreparedStatement preStmt = con.prepareStatement("SELECT r.*, c.Name AS ChallengeName, c.Date AS ChallengeDate FROM Referees r JOIN Challenges c ON r.ID_Challenge = c.ID WHERE r.Name = ?")) {
            preStmt.setString(1, name);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("ID");
                    String password = result.getString("Password");
                    int challengeId = result.getInt("ID_Challenge");
                    String challengeName = result.getString("ChallengeName");
                    String challengeDateString = result.getString("ChallengeDate");
                    Date challengeDate = Date.valueOf(challengeDateString);
                    Challenge challenge = new Challenge(challengeName, challengeDate);
                    challenge.setId(challengeId);
                    referee = new Referee(name, password, challenge);
                    referee.setId(id);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error finding referee by name in database: " + e.getMessage());
        }
        logger.traceExit();
        return referee;
    }

    @Override
    public Referee findByNameAndPassword(String name, String password) {
        logger.traceEntry("Finding referee by name and password: {}", name);
        Referee referee = null;
        try (Connection con = dbUtils.getConnection();
             PreparedStatement preStmt = con.prepareStatement("SELECT r.*, c.Name AS ChallengeName, c.Date AS ChallengeDate FROM Referees r JOIN Challenges c ON r.ID_Challenge = c.ID WHERE r.Name = ? AND r.Password = ?")) {
            preStmt.setString(1, name);
            preStmt.setString(2, password);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("ID");
                    String refereeName = result.getString("Name");
                    String challengeName = result.getString("ChallengeName");
                    String challengeDateString = result.getString("ChallengeDate");
                    Date challengeDate = Date.valueOf(challengeDateString);
                    Challenge challenge = new Challenge(challengeName, challengeDate);
                    challenge.setId(result.getInt("ID_Challenge"));
                    referee = new Referee(refereeName, password, challenge);
                    referee.setId(id);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error finding referee by name and password in database: " + e.getMessage());
        }
        logger.traceExit();
        return referee;
    }

}
