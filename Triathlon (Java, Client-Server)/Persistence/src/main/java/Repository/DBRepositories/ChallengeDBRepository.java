package Repository.DBRepositories;

import Domain.Challenge;
import Repository.Interfaces.IChallengeRepository;

import Repository.Utils.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;


public class ChallengeDBRepository implements IChallengeRepository {
    private JdbcUtils dbUtils;

    private static final Logger logger = LogManager.getLogger();

    public ChallengeDBRepository(Properties properties){
        logger.info("Initializing ChallengeDBRepository with properties: {} ", properties);
        dbUtils = new JdbcUtils(properties);
    }

    @Override
    public void add(Challenge elem) {
        logger.traceEntry("saving task {} ", elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("insert into Challenges (Name, Date) values (?, ?)")) {
            preStmt.setString(1, elem.getName());
            preStmt.setDate(2, new Date(elem.getDate().getTime()));
            int result = preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Challenge elem) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("DELETE FROM Challenges WHERE ID = ?")) {
            preStmt.setInt(1, elem.getId());
            int rowsDeleted = preStmt.executeUpdate();
            if (rowsDeleted > 0) {
                logger.info("Challenge deleted successfully: " + elem);
            } else {
                logger.warn("Challenge with ID " + elem.getId() + " not found in the database.");
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit();
    }

    @Override
    public void update(Challenge elem, Integer id) {
        logger.traceEntry("updating challenge with Id {} with details: {}", id, elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("update Challenges set Name=?, Date=? where ID=?")) {
            preStmt.setString(1, elem.getName());
            preStmt.setDate(2, new Date(elem.getDate().getTime()));
            preStmt.setInt(3, id);
            int result = preStmt.executeUpdate();
            if (result > 0) {
                logger.trace("Updated {} instance(s)", result);
            } else {
                logger.trace("No instance updated");
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error updating challenge: " + ex);
        }
        logger.traceExit();
    }

    @Override
    public Challenge findById(Integer id) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        Challenge challenge = null;
        try (PreparedStatement preStmt = con.prepareStatement("select * from Challenges where ID = ?")) {
            preStmt.setInt(1, id);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    String dateString = result.getString("Date");
                    Date date = Date.valueOf(dateString);
                    String name = result.getString("Name");
                    challenge = new Challenge(name, date);
                    challenge.setId(id);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit();
        return challenge;
    }

    @Override
    public Iterable<Challenge> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();

        List<Challenge> challenges = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Challenges")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("ID");
                    String dateString = result.getString("Date");
                    Date date = Date.valueOf(dateString);
                    String name = result.getString("Name");
                    Challenge challenge = new Challenge(name, date);
                    challenge.setId(id);
                    challenges.add(challenge);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit();
        return challenges;
    }

    @Override
    public Collection<Challenge> getAll() {
        Iterable<Challenge> challenges = findAll();
        return (Collection<Challenge>) challenges;
    }

    public Challenge findByName(String name) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        Challenge challenge = null;
        try (PreparedStatement preStmt = con.prepareStatement("select * from Challenges where Name = ?")) {
            preStmt.setString(1, name);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("ID");
                    String dateString = result.getString("Date");
                    Date date = Date.valueOf(dateString);
                    challenge = new Challenge(name, date);
                    challenge.setId(id);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit();
        return challenge;
    }
}
