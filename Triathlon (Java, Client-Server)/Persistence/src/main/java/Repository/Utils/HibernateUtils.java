package Repository.Utils;

import Domain.Challenge;
import Domain.Participant;
import Domain.Referee;
import Domain.Result;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtils {

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory(){
        if ((sessionFactory==null)||(sessionFactory.isClosed()))
            sessionFactory=createNewSessionFactory();
        return sessionFactory;
    }

    private static  SessionFactory createNewSessionFactory(){
        sessionFactory = new Configuration()
                .addAnnotatedClass(Challenge.class)
                .addAnnotatedClass(Participant.class)
                .addAnnotatedClass(Referee.class)
                .addAnnotatedClass(Result.class)
                .buildSessionFactory();
        return sessionFactory;
    }

    public static  void closeSessionFactory(){
        if (sessionFactory!=null)
            sessionFactory.close();
    }
}
