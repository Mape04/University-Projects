package Controller;

import Domain.Challenge;
import Domain.Referee;
import Interfaces.IObserver;
import Interfaces.IService;
import ro.mpp2024.ServicesException;

import java.sql.Date;

public class ClientController implements IObserver {
    private IService server;

    private Referee refereeL;

    public ClientController(IService server) {
        this.server = server;
    }

    public void login(String name, String password) throws ServicesException {
        refereeL = new Referee(name, password, new Challenge("", new Date(System.currentTimeMillis())));
        refereeL.setId(-1);
        server.login(refereeL, this);
    }

    public void logout(Referee refereeByName) {
        try {
            server.logout(refereeByName, this);


        } catch (ServicesException e) {
            System.out.println("Logout error " + e);
        }
    }

}

