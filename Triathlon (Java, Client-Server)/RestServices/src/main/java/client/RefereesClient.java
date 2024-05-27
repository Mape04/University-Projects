package client;

import Domain.Referee;
import controller.ServiceException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Callable;

public class RefereesClient {
    public static final String URL = "http://localhost:8080/triathlon/referees";

    private RestTemplate restTemplate = new RestTemplate();

    private <T> T execute(Callable<T> callable) {
        try {
            return callable.call();
        } catch (ResourceAccessException | HttpClientErrorException e) { // server down, resource exception
            throw new ServiceException(e);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    public Referee[] getAll() {
        return execute(() -> restTemplate.getForObject(URL, Referee[].class));
    }

    public Referee getById(int id) {
        return execute(() -> restTemplate.getForObject(String.format("%s/%d", URL, id), Referee.class));
    }

    public Referee create(Referee user) {
        return execute(() -> restTemplate.postForObject(URL, user, Referee.class));
    }

    public void update(Referee user) {
        execute(() -> {
            restTemplate.put(String.format("%s/%s", URL, user.getId()), user);
            return null;
        });
    }

    public void delete(String id) {
        execute(() -> {
            restTemplate.delete(String.format("%s/%s", URL, id));
            return null;
        });
    }

}
