import Controller.ClientController;
import Controller.LoginController;
import Interfaces.IService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jsonprotocol.ServicesJsonProxy;

import java.io.IOException;
import java.util.Properties;

public class StartJsonClientFX extends Application {

    private static int defaultChatPort = 55555;
    private static String defaultServer = "localhost";

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("In start");

        // Load client properties
        Properties clientProps = new Properties();
        try {
            clientProps.load(StartJsonClientFX.class.getResourceAsStream("/client.properties"));
            System.out.println("Client properties set.");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find client.properties " + e);
            return;
        }

        // Get server IP and port from properties
        String serverIP = clientProps.getProperty("chat.server.host", defaultServer);
        int serverPort = defaultChatPort;
        try {
            serverPort = Integer.parseInt(clientProps.getProperty("chat.server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultChatPort);
        }

        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server Port " + serverPort);

        // Initialize the service using ServicesJsonProxy
        IService server = new ServicesJsonProxy(serverIP, serverPort);

        // Load the login screen
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Views/Login.fxml"));
        Parent root = loader.load();

        // Set the service in the LoginController
        LoginController ctrl = loader.getController();
        ctrl.setService(server);

        ClientController clientController = new ClientController(server);
        ctrl.setClientController(clientController);

        // Set up the primary stage
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
