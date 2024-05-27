package start;


import Domain.Challenge;
import Domain.Referee;
import client.RefereesClient;
import controller.ServiceException;
import org.springframework.web.client.RestClientException;

import java.sql.Date;
import java.time.LocalDate;

public class StartRestTemplateClient {
    private final static RefereesClient usersClient=new RefereesClient();
    public static void main(String[] args) {
        try{


            System.out.println("\n  Printing all users ...");
            show(()->{
                Referee[] res=usersClient.getAll();
                for(Referee u:res){
                    System.out.println(u.getId()+": "+u.getName());
                }
            });
        }catch(RestClientException ex){
            System.out.println("Exception ... "+ex.getMessage());
        }

        System.out.println("\n  Info for user with id=1");
        show(()-> System.out.println(usersClient.getById(1)));
    }



    private static void show(Runnable task) {
        try {
            task.run();
        } catch (ServiceException e) {
            System.out.println("Service exception"+ e);
        }
    }
}
