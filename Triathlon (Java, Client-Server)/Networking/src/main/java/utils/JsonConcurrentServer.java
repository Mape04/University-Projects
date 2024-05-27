package utils;

import Interfaces.IService;
import jsonprotocol.ClientJsonWorker;

import java.net.Socket;

public class JsonConcurrentServer extends AbsConcurrentServer {
    private IService chatServer;

    public JsonConcurrentServer(int port, IService chatServer) {
        super(port);
        this.chatServer = chatServer;
        System.out.println("Chat- JsonConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        ClientJsonWorker worker = new ClientJsonWorker(chatServer, client);

        Thread tw = new Thread(worker);
        return tw;
    }
}
