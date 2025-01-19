//package HBRepositories;
//
//import Domain.Goal;
//import Interfaces.IGoalRepository;
//import Utils.HibernateUtils;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.hibernate.Session;
//import org.hibernate.Transaction;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Component;
//
//import java.util.Collection;
//import java.util.List;
//import java.util.Properties;
//
//@Component
//public class GoalHBRepository implements IGoalRepository {
//
//    private static final Logger logger = LogManager.getLogger();
//
//    public GoalHBRepository() {
//    }
//
//    @Override
//    public void add(Goal element) {
//        logger.trace("Adding goal: {}", element);
//        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
//            Transaction tx = session.beginTransaction();
//            session.persist(element);
//            tx.commit();
//        } catch (Exception e) {
//            logger.error(e);
//            System.out.println("Error adding goal: " + e);
//        }
//        logger.traceExit();
//    }
//
//    @Override
//    public void delete(Goal element) {
//        logger.traceEntry("Deleting goal: {}", element);
//        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
//            Transaction tx = session.beginTransaction();
//            session.delete(element);
//            tx.commit();
//        } catch (Exception e) {
//            logger.error(e);
//            System.out.println("Error deleting goal: " + e);
//        }
//    }
//
//    @Override
//    public void update(Goal element, Integer id) {
//        logger.traceEntry("Updating goal: {}", element);
//        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
//            Transaction tx = session.beginTransaction();
//            Goal goal = (Goal) session.get(Goal.class, id);
//            if (goal != null) {
//                goal.setDeadline(element.getDeadline());
//                goal.setIsAchieved(element.getIsAchieved());
//                goal.setCreationDate(element.getCreationDate());
//            }
//        }
//    }
//
//    @Override
//    public Goal getById(Integer id) {
//        logger.traceEntry("Getting goal: {}", id);
//        Goal goal = null;
//        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
//            goal = session.get(Goal.class, id);
//        } catch (Exception e) {
//            logger.error(e);
//            System.out.println("Error retrieving goal: " + e);
//        }
//        return null;
//    }
//
//    @Override
//    public Iterable<Goal> findAll() {
//        logger.traceEntry("Finding all goals");
//        List<Goal> goals = null;
//        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
//            goals = session.createQuery("from Goal").list();
//        } catch (Exception e) {
//            logger.error(e);
//            System.out.println("Error finding all goals: " + e);
//        }
//        return goals;
//    }
//
//    @Override
//    public Collection<Goal> getAll() {
//        logger.traceEntry("Finding all goals");
//        List<Goal> goals = null;
//        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
//            goals = session.createQuery("from Goal").list();
//        } catch (Exception e) {
//            logger.error(e);
//            System.out.println("Error finding all goals: " + e);
//        }
//        return goals;
//    }
//}
