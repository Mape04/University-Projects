package Controller;

import Domain.Challenge;
import Domain.Participant;
import Domain.Referee;
import Domain.Result;
import Interfaces.IService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.converter.IntegerStringConverter;
import ro.mpp2024.ServicesException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ParticipantListController {

    private static final Logger logger = Logger.getLogger(ParticipantListController.class.getName());

    ClientController clientController;

    private String username;

    private IService service;
    private Collection<Result> allResults; // Variable to store all results

    @FXML
    private TableView<Participant> tableView;

    @FXML
    private TableView<Participant> challengeTable;

    @FXML
    private TableColumn<Participant, String> nameColumn;

    @FXML
    private TableColumn<Participant, String> ssnColumn;

    @FXML
    private TableColumn<Participant, Integer> pointsColumn;

    @FXML
    private TextField ssnField;

    @FXML
    private TextField pointsField;

    @FXML
    private Label userLabel;

    public void setClientController(ClientController clientController) {
        this.clientController = clientController;
    }

    public void setService(IService service) throws ServicesException {
        this.service = service;
        // Retrieve all results once and store them in allResults variable
        allResults = service.getAllResults();
        initializeTableView();
    }

    public void setUsername(String username) throws ServicesException {
        userLabel.setText("User: " + username);
        this.username = username;
        initializeChallengeTable(service.getRefereeByName(username).getChallenge().getName());
    }

    private void initializeTableView() throws ServicesException {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ssnColumn.setCellValueFactory(new PropertyValueFactory<>("ssn"));

        pointsColumn.setCellValueFactory(cellData -> {
            Participant participant = cellData.getValue();
            // Filter results by participant and calculate total points
            int totalPoints = allResults.stream()
                    .filter(result -> result.getParticipant().equals(participant))
                    .mapToInt(Result::getPoints)
                    .sum();
            return new SimpleIntegerProperty(totalPoints).asObject();
        });

        pointsColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        ObservableList<Participant> participants = FXCollections.observableArrayList(service.getAllParticipants());
        tableView.setItems(participants);

        nameColumn.setSortType(TableColumn.SortType.ASCENDING);
        tableView.getSortOrder().add(nameColumn);
        tableView.sort();

        logger.info("TableView initialized with " + participants.size() + " participants.");
    }

    @FXML
    void handleAdd(ActionEvent event) throws ServicesException {
        String ssn = ssnField.getText().trim();
        if (ssn.isEmpty()) {
            logger.warning("SSN field is empty");
            return;
        }

        Participant participant = service.findBySSN(ssn);
        if (participant == null) {
            logger.warning("Participant not found with SSN: " + ssn);
            return;
        }

        Referee referee = service.getRefereeByName(userLabel.getText().replace("User: ", ""));
        Challenge challenge = referee.getChallenge();

        if (challenge == null) {
            logger.warning("No challenge found for referee");
            return;
        }

        boolean hasResult = allResults.stream()
                .anyMatch(result -> result.getParticipant().equals(participant) && result.getChallenge().equals(challenge));

        if (hasResult) {
            logger.warning("Participant already has a result for this challenge");
            return;
        }

        int points = Integer.parseInt(pointsField.getText().trim());
        service.addResult(new Result(participant, challenge, points));

        allResults = service.getAllResults();
        clearFields();
        initializeChallengeTable(challenge.getName());
        initializeTableView();
    }

    @FXML
    void handleLogout(ActionEvent event) throws ServicesException{

        // Close all open stages (windows)
        Stage primaryStage = (Stage) userLabel.getScene().getWindow();
        primaryStage.close();

        // Iterate through all stages and close them
        for (Stage stage : Stage.getWindows().stream().filter(Window::isShowing).map(window -> (Stage) window).collect(Collectors.toList())) {
            stage.close();
        }

        clientController.logout(service.getRefereeByName(username));
    }

    private void clearFields() {
        ssnField.clear();
        pointsField.clear();
    }

    private void initializeChallengeTable(String challengeName) {
        TableColumn<Participant, String> challengeNameColumn = new TableColumn<>("Name");
        TableColumn<Participant, String> ssnColumn = new TableColumn<>("SSN");
        TableColumn<Participant, Integer> pointsColumn = new TableColumn<>("Points");

        challengeNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ssnColumn.setCellValueFactory(new PropertyValueFactory<>("ssn"));

        pointsColumn.setCellValueFactory(cellData -> {
            Participant participant = cellData.getValue();
            int totalPoints = calculateTotalPoints(participant);
            return new SimpleIntegerProperty(totalPoints).asObject();
        });

        pointsColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        List<Participant> participantsWithPoints = new ArrayList<>();

        for (Result result : allResults) {
            if (result.getChallenge() != null && result.getChallenge().getName().equals(challengeName)) {
                Participant participant = result.getParticipant();
                if (!participantsWithPoints.contains(participant)) {
                    participantsWithPoints.add(participant);
                }
            }
        }

        Comparator<Participant> pointsComparator = Comparator.comparingInt(this::calculateTotalPoints).reversed();

        participantsWithPoints.sort(pointsComparator);

        ObservableList<Participant> sortedParticipants = FXCollections.observableArrayList(participantsWithPoints);

        challengeTable.setItems(sortedParticipants);

        challengeTable.getColumns().clear();
        challengeTable.getColumns().addAll(challengeNameColumn, ssnColumn, pointsColumn);

        pointsColumn.setSortType(TableColumn.SortType.DESCENDING);
        challengeTable.getSortOrder().add(pointsColumn);
        challengeTable.sort();

        logger.info("ChallengeTable initialized with " + participantsWithPoints.size() + " participants who obtained points in the challenge: " + challengeName);
    }

    private int calculateTotalPoints(Participant participant) {
        int totalPoints = 0;
        for (Result result : allResults) {
            if (result.getParticipant().equals(participant)) {
                totalPoints += result.getPoints();
            }
        }
        return totalPoints;
    }
}
