//package HBRepositories;
//
//import Domain.ExerciseType;
//import Interfaces.IExerciseTypeRepository;
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
//public class ExerciseTypeHBRepository implements IExerciseTypeRepository {
//    private static final Logger logger = LogManager.getLogger();
//
//    public ExerciseTypeHBRepository() {
//    }
//
//    @Override
//    public void add(ExerciseType element) {
//        logger.traceEntry("Adding ExerciseType: {}", element);
//        try(Session session = HibernateUtils.getSessionFactory().openSession()) {
//            Transaction tx = session.beginTransaction();
//            session.persist(element);
//            tx.commit();
//        }catch (Exception e) {
//            logger.error(e);
//            System.out.println("Error adding ExerciseType: " + e);
//        }
//    }
//
//    @Override
//    public void delete(ExerciseType element) {
//        logger.traceEntry("Deleting ExerciseType: {}", element);
//        try(Session session = HibernateUtils.getSessionFactory().openSession()) {
//            Transaction tx = session.beginTransaction();
//            session.delete(element);
//            tx.commit();
//        }catch (Exception e) {
//            logger.error(e);
//            System.out.println("Error deleting ExerciseType: " + e);
//        }
//        logger.traceExit();
//    }
//
//    @Override
//    public void update(ExerciseType element, Integer id) {
//        logger.traceEntry("Updating ExerciseType: {}", element);
//        try(Session session = HibernateUtils.getSessionFactory().openSession()) {
//            Transaction tx = session.beginTransaction();
//            ExerciseType exerciseType = (ExerciseType) session.get(ExerciseType.class, id);
//            if(exerciseType != null) {
//                exerciseType.setName(element.getName());
//                exerciseType.setCategory(element.getCategory());
//                exerciseType.setDescription(element.getDescription());
//                session.update(exerciseType);
//                tx.commit();
//            }
//        }
//    }
//
//    @Override
//    public ExerciseType getById(Integer id) {
//        logger.traceEntry("Getting ExerciseType: {}", id);
//        ExerciseType exerciseType = null;
//        try(Session session = HibernateUtils.getSessionFactory().openSession()) {
//            exerciseType = (ExerciseType) session.get(ExerciseType.class, id);
//        }catch (Exception e) {
//            logger.error(e);
//        }
//        logger.traceExit(exerciseType);
//        return exerciseType;
//    }
//
//    @Override
//    public Iterable<ExerciseType> findAll() {
//        logger.traceEntry("Finding all ExerciseType");
//        List<ExerciseType> exerciseTypes = null;
//        try(Session session = HibernateUtils.getSessionFactory().openSession()) {
//            exerciseTypes = (List<ExerciseType>) session.createQuery("from ExerciseType").list();
//        }catch (Exception e) {
//            logger.error(e);
//        }
//        logger.traceExit(exerciseTypes);
//        return exerciseTypes;
//    }
//
//    @Override
//    public Collection<ExerciseType> getAll() {
//        logger.traceEntry("Finding all ExerciseType");
//        List<ExerciseType> exerciseTypes = null;
//        try(Session session = HibernateUtils.getSessionFactory().openSession()) {
//            exerciseTypes = session.createQuery("from ExerciseType ", ExerciseType.class).list();
//        }catch (Exception e) {
//            logger.error(e);
//        }
//        logger.traceExit(exerciseTypes);
//        return exerciseTypes;
//    }
//}
//
