//package HBRepositories;
//
//import Domain.AppUser;
//import Interfaces.IUserRepository;
//import Utils.HibernateUtils;
//import org.apache.logging.log4j.Logger;
//import org.apache.logging.log4j.LogManager;
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
//public class UserHBRepository implements IUserRepository {
//
//
//    private static final Logger logger = LogManager.getLogger(UserHBRepository.class);
//
//    public UserHBRepository() {
//    }
//
//    @Override
//    public void add(AppUser element) {
//        logger.traceEntry("Adding AppUser: {}", element);
//        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
//            Transaction tx = session.beginTransaction();
//            session.merge(element); //in loc de persist ca altfel pusca
//            tx.commit();
//        } catch (Exception e) {
//            logger.error(e);
//            System.out.println("Error adding AppUser: " + e);
//        }
//        logger.traceExit();
//    }
//
//    @Override
//    public void delete(AppUser element) {
//        logger.traceEntry("Deleting AppUser: {}", element);
//        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
//            Transaction tx = session.beginTransaction();
//            session.delete(element);
//            tx.commit();
//        } catch (Exception e) {
//            logger.error(e);
//            System.out.println("Error deleting AppUser: " + e);
//        }
//        logger.traceExit();
//    }
//
//    @Override
//    public void update(AppUser element, Integer id) {
//        logger.traceEntry("Updating Exercise: {}", element);
//        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
//            Transaction tx = session.beginTransaction();
//            AppUser appUser = session.get(AppUser.class, id);
//            if(appUser != null) {
//                appUser.setEmail(element.getEmail());
//                appUser.setGoal_id(element.getGoal_id());
//                appUser.setUsername(element.getUsername());
//                appUser.setWorkout_id(element.getWorkout_id());
//                appUser.setDateOfBirth(element.getDateOfBirth());
//                appUser.setPassword(element.getPassword());
//                session.update(appUser);
//                tx.commit();
//            }else {
//                logger.warn("AppUser with id {} not found", id);
//            }
//        }catch (Exception e) {
//            logger.error("Error updating AppUser: {}", e);
//        }
//        logger.traceExit();
//    }
//
//    @Override
//    public AppUser getById(Integer id) {
//        logger.traceEntry("Getting Exercise: {}", id);
//        AppUser appUser = null;
//        try(Session session = HibernateUtils.getSessionFactory().openSession()) {
//            appUser = session.get(AppUser.class, id);
//        }catch (Exception e) {
//            logger.error(e);
//            System.out.println("Error retrieving Exercise: " + e);
//        }
//        logger.traceExit(appUser);
//        return appUser;
//    }
//
//    @Override
//    public Iterable<AppUser> findAll() {
//        logger.traceEntry("Getting Users");
//        List<AppUser> appUsers = null;
//        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
//            // Use quotes around the table name "AppUser" to avoid conflicts
//            appUsers = session.createQuery("from AppUser", AppUser.class).list();
//        } catch (Exception e) {
//            logger.error(e);
//            System.out.println("Error finding Users: " + e);
//        }
//        logger.traceExit(appUsers);
//        return appUsers;
//    }
//
//    @Override
//    public Collection<AppUser> getAll() {
//        logger.traceEntry("Getting Users");
//        List<AppUser> appUsers = null;
//        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
//            appUsers = session.createQuery("from AppUser" , AppUser.class).list();
//
//        } catch (Exception e) {
//            logger.error(e);
//            System.out.println("Error finding Users: " + e);
//        }
//        logger.traceExit(appUsers);
//        return appUsers;
//    }
//
//}
//
