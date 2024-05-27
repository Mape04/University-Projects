package Repository.HRepositories;

import Domain.Challenge;
import Domain.Participant;
import Domain.Result;
import Repository.Interfaces.IResultRepository;
import Repository.Utils.HibernateUtils;
import Repository.Utils.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class ResultHRepository implements IResultRepository {
    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public ResultHRepository(Properties props) {
        logger.traceEntry("Initializing ResultHRepository with properties: {}", props);
        this.dbUtils = new JdbcUtils(props);
    }

    @Override
    public void add(Result elem) {
        logger.traceEntry("Adding result: {}", elem);
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(elem);
            transaction.commit();
        } catch (Exception e) {
            logger.error("Error adding result: {}", e.getMessage());
        }
        logger.traceExit();
    }

    @Override
    public void delete(Result elem) {
        logger.traceEntry("Deleting result: {}", elem);
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.delete(elem);
            transaction.commit();
        } catch (Exception e) {
            logger.error("Error deleting result: {}", e.getMessage());
        }
        logger.traceExit();
    }

    @Override
    public void update(Result elem, Integer id) {
        logger.traceEntry("Updating result with id: {}", id);
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Result result = session.get(Result.class, id);
            if (result != null) {
                result.setParticipant(elem.getParticipant());
                result.setChallenge(elem.getChallenge());
                result.setPoints(elem.getPoints());
                session.update(result);
                transaction.commit();
            } else {
                logger.warn("No result found with id: {}", id);
            }
        } catch (Exception e) {
            logger.error("Error updating result: {}", e.getMessage());
        }
        logger.traceExit();
    }

    @Override
    public Result findById(Integer id) {
        logger.traceEntry("Finding result by id: {}", id);
        Result result = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            result = session.get(Result.class, id);
        } catch (Exception e) {
            logger.error("Error finding result by id: {}", e.getMessage());
        }
        logger.traceExit(result);
        return result;
    }

    @Override
    public Iterable<Result> findAll() {
        logger.traceEntry("Finding all results");
        List<Result> results = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            results = session.createQuery("FROM Result", Result.class).list();
        } catch (Exception e) {
            logger.error("Error finding all results: {}", e.getMessage());
        }
        logger.traceExit(results);
        return results;
    }

    @Override
    public Collection<Result> getAll() {
        logger.traceEntry("Getting all results");
        List<Result> results = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            results = session.createQuery("FROM Result", Result.class).list();
        } catch (Exception e) {
            logger.error("Error getting all results: {}", e.getMessage());
        }
        logger.traceExit(results);
        return results;
    }

    @Override
    public Result getResult(int participantId, int challengeId) {
        logger.traceEntry("Getting result for participantId: {} and challengeId: {}", participantId, challengeId);
        Result result = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Participant participant = session.find(Participant.class, participantId);
            Challenge challenge = session.find(Challenge.class, challengeId);
            if (participant != null && challenge != null) {
                result = session.createQuery("FROM Result WHERE Participant = :participant AND Challenge = :challenge", Result.class)
                        .setParameter("participant", participant)
                        .setParameter("challenge", challenge)
                        .uniqueResult();
            }
        } catch (Exception e) {
            logger.error("Error getting result: {}", e.getMessage());
        }
        logger.traceExit(result);
        return result;
    }

    @Override
    public boolean hasResult(Participant participant, Challenge challenge) {
        logger.traceEntry("Checking if participant {} has result for challenge {}", participant, challenge);
        boolean hasResult = false;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Long count = session.createQuery("SELECT count(r) FROM Result r WHERE r.Participant = :participant AND r.Challenge = :challenge", Long.class)
                    .setParameter("participant", participant)
                    .setParameter("challenge", challenge)
                    .uniqueResult();
            hasResult = count != null && count > 0;
        } catch (Exception e) {
            logger.error("Error checking result: {}", e.getMessage());
        }
        logger.traceExit(hasResult);
        return hasResult;
    }

    @Override
    public int getTotalPointsBySsn(String ssn) {
        logger.traceEntry("Getting total points by SSN: {}", ssn);
        int totalPoints = 0;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Long sum = session.createQuery("SELECT sum(r.Points) FROM Result r WHERE r.Participant.ssn = :ssn", Long.class)
                    .setParameter("ssn", ssn)
                    .uniqueResult();
            totalPoints = sum != null ? sum.intValue() : 0;
        } catch (Exception e) {
            logger.error("Error getting total points: {}", e.getMessage());
        }
        logger.traceExit(totalPoints);
        return totalPoints;
    }
}
