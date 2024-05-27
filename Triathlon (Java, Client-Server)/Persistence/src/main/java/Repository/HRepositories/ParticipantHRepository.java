package Repository.HRepositories;

import Domain.Participant;
import Repository.Interfaces.IParticipantRepository;
import Repository.Utils.HibernateUtils;
import Repository.Utils.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class ParticipantHRepository implements IParticipantRepository {

    private JdbcUtils dbUtils;

    private static final Logger logger = LogManager.getLogger();

    public ParticipantHRepository(Properties props) {
        logger.traceEntry("Initializing ParticipantHRepository with properties: {} ", props);
        this.dbUtils = new JdbcUtils(props);
    }

    @Override
    public int getTotalPointsById(Integer id) {
        logger.traceEntry("Getting total points for participant with id: {} ", id);
        int totalPoints = 0;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            totalPoints = session.createQuery("SELECT SUM(r.Points) FROM Result r WHERE r.Participant.id = :id", Long.class)
                    .setParameter("id", id)
                    .uniqueResult()
                    .intValue();
        } catch (Exception e) {
            logger.error("Error getting total points for participant: {}", e.getMessage());
        }
        logger.traceExit(totalPoints);
        return totalPoints;
    }

    @Override
    public Participant findBySSN(String ssn) {
        logger.traceEntry("Finding participant by SSN: {} ", ssn);
        Participant participant = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            participant = session.createQuery("FROM Participant WHERE ssn = :ssn", Participant.class)
                    .setParameter("ssn", ssn)
                    .uniqueResult();
        } catch (Exception e) {
            logger.error("Error finding participant by SSN: {}", e.getMessage());
        }
        logger.traceExit(participant);
        return participant;
    }

    @Override
    public void add(Participant elem) {
        logger.traceEntry("Adding participant: {} ", elem);
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(elem);
            transaction.commit();
        } catch (Exception e) {
            logger.error("Error adding participant: {}", e.getMessage());
        }
        logger.traceExit();
    }

    @Override
    public void delete(Participant elem) {
        logger.traceEntry("Deleting participant: {} ", elem);
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.delete(elem);
            transaction.commit();
        } catch (Exception e) {
            logger.error("Error deleting participant: {}", e.getMessage());
        }
        logger.traceExit();
    }

    @Override
    public void update(Participant elem, Integer id) {
        logger.traceEntry("Updating participant with id: {} ", id);
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Participant participant = session.get(Participant.class, id);
            if (participant != null) {
                participant.setName(elem.getName());
                participant.setSsn(elem.getSsn());
                session.update(participant);
                transaction.commit();
            } else {
                logger.warn("No participant found with id: {}", id);
            }
        } catch (Exception e) {
            logger.error("Error updating participant: {}", e.getMessage());
        }
        logger.traceExit();
    }

    @Override
    public Participant findById(Integer id) {
        logger.traceEntry("Finding participant by id: {} ", id);
        Participant participant = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            participant = session.get(Participant.class, id);
        } catch (Exception e) {
            logger.error("Error finding participant by id: {}", e.getMessage());
        }
        logger.traceExit(participant);
        return participant;
    }

    @Override
    public Iterable<Participant> findAll() {
        logger.traceEntry("Finding all participants");
        List<Participant> participants = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            participants = session.createQuery("FROM Participant", Participant.class).list();
        } catch (Exception e) {
            logger.error("Error finding all participants: {}", e.getMessage());
        }
        logger.traceExit(participants);
        return participants;
    }

    @Override
    public Collection<Participant> getAll() {
        logger.traceEntry("Getting all participants");
        List<Participant> participants = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            participants = session.createQuery("FROM Participant", Participant.class).list();
        } catch (Exception e) {
            logger.error("Error getting all participants: {}", e.getMessage());
        }
        logger.traceExit(participants);
        return participants;
    }
}
