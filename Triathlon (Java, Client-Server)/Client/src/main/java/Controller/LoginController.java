package Controller;

import Domain.Referee;
import Interfaces.IService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2024.ServicesException;

import java.io.IOException;

public class LoginController {

    private static final Logger logger = LogManager.getLogger();

    private Stage primaryStage;
    private IService service;

    private ClientController clientController;

    public void setService(IService service) {
        this.service = service;
    }

    public void setClientController(ClientController clientController){
        this.clientController = clientController;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            clientController.login(username, password);
            openTableView(username);
        } catch (ServicesException e) {
            throw new RuntimeException(e);
        }
    }

    private void openTableView(String username) throws ServicesException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../Views/Triathlon.fxml"));
            Parent root = fxmlLoader.load();

            ParticipantListController controller = fxmlLoader.getController();
            controller.setService(service);
            controller.setUsername(username);
            controller.setClientController(clientController);

            Stage tableViewStage = new Stage();
            tableViewStage.setScene(new Scene(root, 800, 600));
            tableViewStage.setTitle("Participant List");
            tableViewStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Error opening TableView: " + e.getMessage());
        }
    }
    //TODO: implement observers XD

}

