package Repository.HRepositories;

import Domain.Referee;
import Repository.Interfaces.IRefereeRepository;
import Repository.Utils.HibernateUtils;
import Repository.Utils.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class RefereeHRepository implements IRefereeRepository {
    private JdbcUtils dbUtils;
    private static final Logger logger = LogManager.getLogger();

    public RefereeHRepository(Properties props) {
        logger.traceEntry("Initializing RefereeHRepository with properties: {}", props);
        this.dbUtils = new JdbcUtils(props);
    }

    @Override
    public Referee getRefereeByName(String name) {
        logger.traceEntry("Finding referee by name: {}", name);
        Referee referee = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            referee = session.createQuery("FROM Referee WHERE Name = :name", Referee.class)
                    .setParameter("name", name)
                    .uniqueResult();
        } catch (Exception e) {
            logger.error("Error finding referee by name: {}", e.getMessage());
        }
        logger.traceExit(referee);
        return referee;
    }

    @Override
    public Referee findByNameAndPassword(String name, String password) {
        logger.traceEntry("Finding referee by name and password");
        Referee referee = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            referee = session.createQuery("FROM Referee WHERE Name = :name AND Password = :password", Referee.class)
                    .setParameter("name", name)
                    .setParameter("password", password)
                    .uniqueResult();
        } catch (Exception e) {
            logger.error("Error finding referee by name and password: {}", e.getMessage());
        }
        logger.traceExit(referee);
        return referee;
    }

    @Override
    public void add(Referee elem) {
        logger.traceEntry("Adding referee: {}", elem);
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(elem);
            transaction.commit();
        } catch (Exception e) {
            logger.error("Error adding referee: {}", e.getMessage());
        }
        logger.traceExit();
    }

    @Override
    public void delete(Referee elem) {
        logger.traceEntry("Deleting referee: {}", elem);
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.delete(elem);
            transaction.commit();
        } catch (Exception e) {
            logger.error("Error deleting referee: {}", e.getMessage());
        }
        logger.traceExit();
    }

    @Override
    public void update(Referee elem, Integer id) {
        logger.traceEntry("Updating referee with id: {}", id);
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Referee referee = session.get(Referee.class, id);
            if (referee != null) {
                referee.setName(elem.getName());
                referee.setPassword(elem.getPassword());
                referee.setChallenge(elem.getChallenge());
                session.update(referee);
                transaction.commit();
            } else {
                logger.warn("No referee found with id: {}", id);
            }
        } catch (Exception e) {
            logger.error("Error updating referee: {}", e.getMessage());
        }
        logger.traceExit();
    }

    @Override
    public Referee findById(Integer id) {
        logger.traceEntry("Finding referee by id: {}", id);
        Referee referee = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            referee = session.get(Referee.class, id);
        } catch (Exception e) {
            logger.error("Error finding referee by id: {}", e.getMessage());
        }
        logger.traceExit(referee);
        return referee;
    }

    @Override
    public Iterable<Referee> findAll() {
        logger.traceEntry("Finding all referees");
        List<Referee> referees = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            referees = session.createQuery("FROM Referee", Referee.class).list();
        } catch (Exception e) {
            logger.error("Error finding all referees: {}", e.getMessage());
        }
        logger.traceExit(referees);
        return referees;
    }

    @Override
    public Collection<Referee> getAll() {
        logger.traceEntry("Getting all referees");
        List<Referee> referees = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            referees = session.createQuery("FROM Referee", Referee.class).list();
        } catch (Exception e) {
            logger.error("Error getting all referees: {}", e.getMessage());
        }
        logger.traceExit(referees);
        return referees;
    }
}
