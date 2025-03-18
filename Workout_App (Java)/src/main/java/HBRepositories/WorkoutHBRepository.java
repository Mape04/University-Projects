//package HBRepositories;
//
//import Domain.Exercise;
//import Domain.Workout;
//import Interfaces.IWorkoutRepository;
//import Utils.HibernateUtils;
//import jakarta.transaction.Transactional;
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
//public class WorkoutHBRepository implements IWorkoutRepository {
//
//
//    private static final Logger logger = LogManager.getLogger();
//
//    public WorkoutHBRepository() {
//    }
//
//    @Override
//    public void add(Workout workout) {
//        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
//            Transaction tx = session.beginTransaction();
//
//            // Persist the workout
//            session.persist(workout);
//
//            // Insert into the join table manually
//            for (Exercise exercise : workout.getExercises()) {
//                session.createNativeQuery("INSERT INTO Workout_Exercise (workout_id, exercise_id) VALUES (:workoutId, :exerciseId)")
//                        .setParameter("workoutId", workout.getId())
//                        .setParameter("exerciseId", exercise.getId())
//                        .executeUpdate();
//            }
//
//            tx.commit();
//        } catch (Exception e) {
//            logger.error("Error adding workout: ", e);
//        }
//    }
//
//    @Override
//    public void delete(Workout workout) {
//        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
//            Transaction tx = session.beginTransaction();
//
//            Workout existingWorkout = session.get(Workout.class, workout.getId());
//            if (existingWorkout != null) {
//                // Clear relationships in the join table
//                existingWorkout.getExercises().clear();
//                session.update(existingWorkout);
//
//                // Delete the workout
//                session.delete(existingWorkout);
//            }
//
//            tx.commit();
//        } catch (Exception e) {
//            logger.error("Error deleting workout: ", e);
//        }
//    }
//
//
//    @Override
//    public void update(Workout workout, Integer id) {
//        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
//            Transaction tx = session.beginTransaction();
//
//            Workout existingWorkout = session.get(Workout.class, id);
//            if (existingWorkout != null) {
//                // Update workout details
//                existingWorkout.setDate(workout.getDate());
//                existingWorkout.setNotes(workout.getNotes());
//                existingWorkout.setCompletionPercentage(workout.getCompletionPercentage());
//
//                // Update the exercises
//                existingWorkout.getExercises().clear();
//                for (Exercise exercise : workout.getExercises()) {
//                    Exercise existingExercise = session.get(Exercise.class, exercise.getId());
//                    if (existingExercise == null) {
//                        session.persist(exercise); // Persist new exercise if necessary
//                    } else {
//                        existingWorkout.addExercise(existingExercise);
//                    }
//                }
//
//                session.update(existingWorkout);
//                tx.commit();
//            } else {
//                logger.warn("Workout with ID {} not found", id);
//            }
//        } catch (Exception e) {
//            logger.error("Error updating workout: ", e);
//        }
//    }
//
//
//
//    @Override
//    public Workout getById(Integer id) {
//        logger.traceEntry("Getting Workout: {}", id);
//        Workout workout = null;
//        try(Session session = HibernateUtils.getSessionFactory().openSession()) {
//            workout = session.get(Workout.class, id);
//        }catch (Exception e) {
//            logger.error(e);
//            System.out.println("Error retrieving Workout: " + e);
//        }
//        logger.traceExit(workout);
//        return workout;
//    }
//
//    @Override
//    public Iterable<Workout> findAll() {
//        logger.traceEntry("Getting Workouts");
//        List<Workout> workouts = null;
//        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
//            // Fetch workouts along with their exercises using JOIN FETCH
//            workouts = session.createQuery("SELECT DISTINCT w FROM Workout w LEFT JOIN FETCH w.exercises", Workout.class).list();
//        } catch (Exception e) {
//            logger.error("Error finding Workouts", e);
//            System.out.println("Error finding Workouts: " + e.getMessage());
//        }
//        logger.traceExit(workouts);
//        return workouts;
//    }
//
//    @Override
//    public Collection<Workout> getAll() {
//        logger.traceEntry("Getting Workouts");
//        List<Workout> workouts = null;
//        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
//            workouts = session.createQuery("from Workout", Workout.class).list();
//        } catch (Exception e) {
//            logger.error("Error finding Workouts: {}", e);
//            System.out.println("Error finding Workouts: " + e);
//        }
//        logger.traceExit(workouts);
//        return workouts;
//    }
//
//}
