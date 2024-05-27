import Interfaces.IService;
//
//import Repository.DBRepositories.ChallengeDBRepository;
//import Repository.DBRepositories.ParticipantDBRepository;
//import Repository.DBRepositories.RefereeDBRepository;
//import Repository.DBRepositories.ResultDBRepository;


import Repository.DBRepositories.ChallengeDBRepository;
import Repository.DBRepositories.ParticipantDBRepository;
import Repository.DBRepositories.RefereeDBRepository;
import Repository.DBRepositories.ResultDBRepository;
import Repository.HRepositories.ChallengeHRepository;
import Repository.HRepositories.ParticipantHRepository;
import Repository.HRepositories.RefereeHRepository;
import Repository.HRepositories.ResultHRepository;


import Service.Service;
import utils.AbstractServer;
import utils.JsonConcurrentServer;
import utils.ServerException;

import java.io.IOException;
import java.util.Properties;

public class StartJsonServer {
    private static int defaultPort = 55555;

    public static void main(String[] args) {
        Properties serverProps = new Properties();
        try {
            serverProps.load(StartJsonServer.class.getResourceAsStream("server.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find chatserver.properties " + e);
            return;
        }

        ParticipantDBRepository participantDBRepository = new ParticipantDBRepository(serverProps);
        RefereeDBRepository refereeDBRepository = new RefereeDBRepository(serverProps);
        ChallengeDBRepository challengeDBRepository = new ChallengeDBRepository(serverProps);
        ResultDBRepository resultDBRepository = new ResultDBRepository(serverProps);

        IService chatServerImpl = new Service(participantDBRepository, challengeDBRepository, refereeDBRepository, resultDBRepository);
        int chatServerPort = defaultPort;
        try {
            chatServerPort = Integer.parseInt(serverProps.getProperty("server.port"));
        } catch (NumberFormatException nef) {
            System.err.println("Wrong  Port Number" + nef.getMessage());
            System.err.println("Using default port " + defaultPort);
        }
        System.out.println("Starting server on port: " + chatServerPort);
        AbstractServer server = new JsonConcurrentServer(chatServerPort, chatServerImpl);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        }
    }
}
