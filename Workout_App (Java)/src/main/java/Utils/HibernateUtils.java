package Utils;

import Domain.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtils {

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null || sessionFactory.isClosed()) {
            sessionFactory = createNewSessionFactory();
        }
        return sessionFactory;
    }

    //#TODO: COPY PASTA HURTS THE SOUL IF NOT UNDERSTAND THING
    private static  SessionFactory createNewSessionFactory(){
        sessionFactory = new Configuration()
                .addAnnotatedClass(Exercise.class)
                .addAnnotatedClass(ExerciseType.class)
                .addAnnotatedClass(Goal.class)
                .addAnnotatedClass(AppUser.class)
                .addAnnotatedClass(Workout.class)
                .buildSessionFactory();
        return sessionFactory;
    }

    public static void closeSessionFactory(){
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
