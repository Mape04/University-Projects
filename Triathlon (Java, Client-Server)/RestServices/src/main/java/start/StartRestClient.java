package start;

import Domain.Challenge;
import Domain.Referee;
import client.NewRefereesClient;
import controller.ServiceException;
import org.springframework.web.client.RestClientException;

import java.sql.Date;
import java.time.LocalDate;


public class StartRestClient {
    private final static NewRefereesClient refereesClient = new NewRefereesClient();

    public static void main(String[] args) {

        Challenge challenge = new Challenge("Test", Date.valueOf(LocalDate.now()));
        Referee userT=new Referee("test2024nou","133",challenge);
        try{
            System.out.println("Adding a new user "+userT);
            show(()-> System.out.println(refereesClient.create(userT)));
            System.out.println("\nPrinting all users ...");
            show(()->{
                Referee[] res=refereesClient.getAll();
                for(Referee u:res){
                    System.out.println(u.getId()+": "+u.getName());
                }
            });
        }catch(RestClientException ex){
            System.out.println("Exception ... "+ex.getMessage());
        }

        System.out.println("\nInfo for user with id=1");
        show(()-> System.out.println(refereesClient.getById("1")));

        System.out.println("\nDeleting user with id="+userT.getId());
        //show(()-> refereesClient.delete(userT.getId()));
    }

    private static void show(Runnable task) {
        try {
            task.run();
        } catch (ServiceException e) {
            System.out.println("Service exception"+ e);
        }
    }

}
