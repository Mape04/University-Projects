//package HBRepositories;
//
//import Domain.Exercise;
//import Interfaces.IExerciseRepository;
//import Utils.HibernateUtils;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.hibernate.Session;
//import org.hibernate.Transaction;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Component;
//
//import java.sql.Connection;
//import java.util.Collection;
//import java.util.List;
//import java.util.Properties;
//
//@Component
//public class ExerciseHBRepository implements IExerciseRepository {
//
//    private static final Logger logger = LogManager.getLogger();
//
//    public ExerciseHBRepository() {
//    }
//
//    @Override
//    public void add(Exercise element) {
//        logger.traceEntry("Adding Exercise: {}", element);
//        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
//            Transaction tx = session.beginTransaction();
//            session.merge(element);
//            tx.commit();
//        } catch (Exception e) {
//            logger.error(e);
//            System.out.println("Error adding Exercise: " + e);
//        }
//        logger.traceExit();
//    }
//
//    @Override
//    public void delete(Exercise element) {
//        logger.traceEntry("Deleting Exercise: {}", element);
//        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
//            Transaction tx = session.beginTransaction();
//            session.delete(element);
//            tx.commit();
//        } catch (Exception e) {
//            logger.error(e);
//            System.out.println("Error deleting Exercise: " + e);
//        }
//        logger.traceExit();
//    }
//
//    @Override
//    public void update(Exercise element, Integer id) {
//        logger.traceEntry("Updating Exercise: {}", element);
//        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
//            Transaction tx = session.beginTransaction();
//            Exercise exercise = session.get(Exercise.class, id);
//            if(exercise != null) {
//                exercise.setExerciseType_id(element.getExerciseType_id());
//                exercise.setSets(element.getSets());
//                exercise.setReps(element.getReps());
//                exercise.setWeight(element.getWeight());
//                session.update(exercise);
//                tx.commit();
//            }else {
//                logger.warn("Exercise with id {} not found", id);
//            }
//        }catch (Exception e) {
//            logger.error("Error updating Exercise: {}", e);
//        }
//        logger.traceExit();
//    }
//
//    @Override
//    public Exercise getById(Integer id) {
//        logger.traceEntry("Getting Exercise: {}", id);
//        Exercise exercise = null;
//        try(Session session = HibernateUtils.getSessionFactory().openSession()) {
//            exercise = session.get(Exercise.class, id);
//        }catch (Exception e) {
//            logger.error(e);
//            System.out.println("Error retrieving Exercise: " + e);
//        }
//        logger.traceExit(exercise);
//        return exercise;
//    }
//
//    @Override
//    public Iterable<Exercise> findAll() {
//        logger.traceEntry("Getting Exercises");
//        List<Exercise> exercises = null;
//        try(Session session = HibernateUtils.getSessionFactory().openSession()) {
//            exercises = session.createQuery("from Exercise").list();
//        }catch (Exception e) {
//            logger.error(e);
//            System.out.println("Error finding Exercises: " + e);
//        }
//        logger.traceExit(exercises);
//        return exercises;
//    }
//
//    @Override
//    public Collection<Exercise> getAll() {
//        logger.traceEntry("Getting Exercises");
//        List<Exercise> exercises = null;
//        try(Session session = HibernateUtils.getSessionFactory().openSession()) {
//            exercises = session.createQuery("from Exercise").list();
//        }catch (Exception e) {
//            logger.error("Error retrieving Exercises: {}", e);
//        }
//        logger.traceExit(exercises);
//        return exercises;
//    }
//}
