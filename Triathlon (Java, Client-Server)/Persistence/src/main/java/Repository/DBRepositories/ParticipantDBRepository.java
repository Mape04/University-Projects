package Repository.DBRepositories;

import Domain.Participant;
import Repository.Interfaces.IParticipantRepository;
import Repository.Utils.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;


public class ParticipantDBRepository implements IParticipantRepository {

    private JdbcUtils dbUtils;

    private static final Logger logger = LogManager.getLogger();

    public ParticipantDBRepository(Properties properties){
        logger.info("Initializing RefereeDBRepository with properties: {} ", properties);
        dbUtils = new JdbcUtils(properties);
    }

    @Override
    public void add(Participant elem) {
        logger.traceEntry("saving task {} ", elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("insert into Participants (Name, SSN) values (?,?)")) {
            preStmt.setString(1, elem.getName());
            preStmt.setString(2, elem.getSsn());
            int result = preStmt.executeUpdate();
            logger.trace("Saved {} instances", result);
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Participant elem) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("DELETE FROM Participants WHERE ID = ?")) {
            preStmt.setInt(1, elem.getId());
            int rowsDeleted = preStmt.executeUpdate();
            if (rowsDeleted > 0) {
                logger.info("Participant deleted successfully: " + elem);
            } else {
                logger.warn("Participant with ID " + elem.getId() + " not found in the database.");
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit();
    }

    @Override
    public int getTotalPointsById(Integer id){
        logger.traceEntry("Getting all points for participant with Id {}", id);
        Connection con = dbUtils.getConnection();
        int points = -1;
        try (PreparedStatement preStmt = con.prepareStatement("SELECT SUM(Points) AS TotalPoints FROM Results WHERE ID_Participant = ?")) {
            preStmt.setInt(1, id);
            try (ResultSet resultSet = preStmt.executeQuery()) {
                if (resultSet.next()) {
                    points = resultSet.getInt("TotalPoints");
                } else {
                    logger.warn("No points found for participant with Id {}", id);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error getting all points for participant: " + ex);
        }
        return points;
    }

    @Override
    public Participant findBySSN(String ssn) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        Participant participant = null;
        try (PreparedStatement preStmt = con.prepareStatement("select * from Participants where SSN = ?")) {
            preStmt.setString(1, ssn);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("ID");
                    String name = result.getString("Name");
                    participant = new Participant(name, ssn);
                    participant.setId(id);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit();
        return participant;
    }

    @Override
    public void update(Participant elem, Integer id) {
        logger.traceEntry("updating car with Id {} with details: {}", id, elem);
        Connection con = dbUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("update Participants set Name=?, SSN=? where ID=?")) {
            preStmt.setString(1, elem.getName());
            preStmt.setString(2, elem.getSsn());
            preStmt.setInt(3, id);
            int result = preStmt.executeUpdate();
            if (result > 0) {
                logger.trace("Updated {} instance(s)", result);
            } else {
                logger.trace("No instance updated");
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error updating participant: " + ex);
        }
        logger.traceExit();
    }

    @Override
    public Participant findById(Integer id) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();
        Participant participant = null;
        try (PreparedStatement preStmt = con.prepareStatement("select * from Participants where ID = ?")) {
            preStmt.setInt(1, id);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    String name = result.getString("Name");
                    String ssn = result.getString("SSN");
                    participant = new Participant(name, ssn);
                    participant.setId(id);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit();
        return participant;
    }

    @Override
    public Iterable<Participant> findAll() {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();

        List<Participant> participants = new ArrayList<>();
        try (PreparedStatement preStmt = con.prepareStatement("select * from Participants")) {
            try (ResultSet result = preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("ID");
                    String name = result.getString("Name");
                    String ssn = result.getString("SSN");
                    Participant participant = new Participant(name, ssn);
                    participant.setId(id);
                    participants.add(participant);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.err.println("Error DB " + e);
        }
        logger.traceExit();
        return participants;
    }

    @Override
    public Collection<Participant> getAll() {
        Iterable<Participant> participants = findAll();
        return (Collection<Participant>) participants;
    }
}
