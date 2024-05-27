package Repository.HRepositories;

import Domain.Challenge;
import Repository.Interfaces.IChallengeRepository;
import Repository.Utils.HibernateUtils;
import Repository.Utils.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class ChallengeHRepository implements IChallengeRepository {
    private JdbcUtils dbUtils;

    private static final Logger logger = LogManager.getLogger();

    public ChallengeHRepository(Properties props) {
        logger.info("Initializing ChallengeHRepository with properties : {} ", props);
        this.dbUtils = new JdbcUtils(props);
    }

    @Override
    public Challenge findByName(String name) {
        logger.traceEntry("Finding challenge by name {} ", name);
        Challenge challenge = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            challenge = session.createQuery("FROM Challenge WHERE name = :name", Challenge.class)
                    .setParameter("name", name)
                    .uniqueResult();
        } catch (Exception e) {
            logger.error("Error finding challenge by name: {}", e.getMessage());
        }
        logger.traceExit(challenge);
        return challenge;
    }

    @Override
    public void add(Challenge elem) {
        logger.traceEntry("Adding challenge {} ", elem);
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(elem);
            transaction.commit();
        } catch (Exception e) {
            logger.error("Error adding challenge: {}", e.getMessage());
        }
        logger.traceExit();
    }

    @Override
    public void delete(Challenge elem) {
        logger.traceEntry("Deleting challenge {} ", elem);
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.delete(elem);
            transaction.commit();
        } catch (Exception e) {
            logger.error("Error deleting challenge: {}", e.getMessage());
        }
        logger.traceExit();
    }

    @Override
    public void update(Challenge elem, Integer id) {
        logger.traceEntry("Updating challenge with id {} ", id);
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Challenge challenge = session.get(Challenge.class, id);
            if (challenge != null) {
                challenge.setName(elem.getName());
                challenge.setDate(elem.getDate());
                session.update(challenge);
                transaction.commit();
            } else {
                logger.warn("No challenge found with id {}", id);
            }
        } catch (Exception e) {
            logger.error("Error updating challenge: {}", e.getMessage());
        }
        logger.traceExit();
    }

    @Override
    public Challenge findById(Integer id) {
        logger.traceEntry("Finding challenge by id {} ", id);
        Challenge challenge = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            challenge = session.get(Challenge.class, id);
        } catch (Exception e) {
            logger.error("Error finding challenge by id: {}", e.getMessage());
        }
        logger.traceExit(challenge);
        return challenge;
    }

    @Override
    public Iterable<Challenge> findAll() {
        logger.traceEntry("Finding all challenges");
        List<Challenge> challenges = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            challenges = session.createQuery("FROM Challenge", Challenge.class).list();
        } catch (Exception e) {
            logger.error("Error finding all challenges: {}", e.getMessage());
        }
        logger.traceExit(challenges);
        return challenges;
    }

    @Override
    public Collection<Challenge> getAll() {
        logger.traceEntry("Getting all challenges");
        List<Challenge> challenges = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            challenges = session.createQuery("FROM Challenge", Challenge.class).list();
        } catch (Exception e) {
            logger.error("Error getting all challenges: {}", e.getMessage());
        }
        logger.traceExit(challenges);
        return challenges;
    }
}
