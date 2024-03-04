package com.example.a4mazilupaul;

import Domain.Appointment;
import Domain.Patient;
import Domain.PatientFactory;
import Domain.AppointmentFactory;
import Main.Settings;
import Repository.*;
import Service.Service;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {
    TextField idPatientTextField = new TextField();
    TextField numePatientTextField = new TextField();
    TextField firstNamePatientTextField = new TextField();
    TextField agePatientTextField = new TextField();

    TextField idAppointmentTextField = new TextField();
    TextField idPatientAppointmentTextField = new TextField();
    TextField dateAppointmentTextField = new TextField();
    TextField hourAppointmentTextField = new TextField();
    TextField purposeAppointmentTextFie = new TextField();

    @Override
    public void start(Stage stage) throws IOException {
        IRepository<Patient> patientRepository = null;
        IRepository<Appointment> appointmentRepository = null;
        Settings settings = Settings.getInstance();

        if (Objects.equals(settings.getRepoType(), "memory")) {
            patientRepository = new MemoryRepository<>();
            appointmentRepository = new MemoryRepository<>();
        }

        if (Objects.equals(settings.getRepoType(), "text")) {
            patientRepository = new TextFileRepository<>(settings.getRepoFile1(), new PatientFactory());
            appointmentRepository = new TextFileRepository<>(settings.getRepoFile2(), new AppointmentFactory());
        }

        if (Objects.equals(settings.getRepoType(), "binary")) {
            patientRepository = new BinaryFileRepository<>(settings.getRepoFile1());
            appointmentRepository = new BinaryFileRepository<>(settings.getRepoFile2());
        }

        if(Objects.equals(settings.getRepoType(), "database")){
            patientRepository = new PatientDbRepository();
            appointmentRepository = new AppointmentDbRepository();
        }

        Service service = new Service(patientRepository, appointmentRepository);

        HBox mainHorizontalBox = new HBox();
        mainHorizontalBox.setPadding(new Insets(10));

        VBox patientsVerticalBox = new VBox();
        patientsVerticalBox.setPadding(new Insets(10));
        mainHorizontalBox.getChildren().add(patientsVerticalBox);

        ObservableList<Patient> patients = FXCollections.observableArrayList(service.getAllPatients());
        ObservableList<Appointment> appointments = FXCollections.observableArrayList(service.getAllAppointments());

        ListView<Patient> patientsListView = new ListView<>();
        patientsListView.setPrefHeight(700);
        patientsListView.setPrefWidth(500);
        patientsListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Patient patient = patientsListView.getSelectionModel().getSelectedItem();
                idPatientTextField.setText(Integer.toString(patient.getId()));
                numePatientTextField.setText(patient.getLastName());
                firstNamePatientTextField.setText(patient.getFirstName());
                agePatientTextField.setText(Integer.toString(patient.getAge()));
            }
        });
        patientsVerticalBox.getChildren().add(patientsListView);


        GridPane patientsGridPane = new GridPane();

        Label idPacientLabel = new Label();
        idPacientLabel.setText("ID: ");
        idPacientLabel.setPadding(new Insets(10, 5, 10, 0));

        Label numePacientLable = new Label();
        numePacientLable.setText("Last name: ");
        numePacientLable.setPadding(new Insets(10, 5, 10, 0));

        Label prenumePacientLable = new Label();
        prenumePacientLable.setText("First name: ");
        prenumePacientLable.setPadding(new Insets(10, 5, 10, 0));

        Label varstaPacientLable = new Label();
        varstaPacientLable.setText("Age: ");
        varstaPacientLable.setPadding(new Insets(10, 5, 10, 0));

        patientsGridPane.add(idPacientLabel, 0, 0);
        patientsGridPane.add(idPatientTextField, 1, 0);

        patientsGridPane.add(numePacientLable, 0, 1);
        patientsGridPane.add(numePatientTextField, 1, 1);

        patientsGridPane.add(prenumePacientLable, 0, 2);
        patientsGridPane.add(firstNamePatientTextField, 1, 2);

        patientsGridPane.add(varstaPacientLable, 0, 3);
        patientsGridPane.add(agePatientTextField, 1, 3);

        patientsVerticalBox.getChildren().add(patientsGridPane);

        HBox patientButtonsHorizontalBox = new HBox();
        patientsVerticalBox.getChildren().add(patientButtonsHorizontalBox);

        Button addPatientButton = new Button("Add patient");
        addPatientButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    int id = Integer.parseInt(idPatientTextField.getText());
                    String lastName = numePatientTextField.getText();
                    String fistName = firstNamePatientTextField.getText();
                    int age = Integer.parseInt(agePatientTextField.getText());

                    service.addPatient(id, lastName, fistName, age);
                    patients.setAll(service.getAllPatients());
                } catch (Exception e) {
                    Alert errorPopUp = new Alert(Alert.AlertType.ERROR);
                    errorPopUp.setTitle("Error");
                    errorPopUp.setContentText(e.getMessage());
                    errorPopUp.show();
                }
            }
        });
        patientButtonsHorizontalBox.getChildren().add(addPatientButton);


        Button deletePatientButton = new Button("Delete patient");
        deletePatientButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    int id = Integer.parseInt(idPatientTextField.getText());

                    service.deletePatient(id);
                    patients.setAll(service.getAllPatients());
                    appointments.setAll(service.getAllAppointments());
                } catch (Exception e) {
                    Alert errorPopUp = new Alert(Alert.AlertType.ERROR);
                    errorPopUp.setTitle("Error");
                    errorPopUp.setContentText(e.getMessage());
                    errorPopUp.show();
                }
            }
        });
        patientButtonsHorizontalBox.getChildren().add(deletePatientButton);

        Button updatePatientButton = new Button("Update patient");
        updatePatientButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    int id = Integer.parseInt(idPatientTextField.getText());
                    String lastName = numePatientTextField.getText();
                    String firstName = firstNamePatientTextField.getText();
                    int age = Integer.parseInt(agePatientTextField.getText());

                    service.updatePatient(id, lastName, firstName, age);
                    patients.setAll(service.getAllPatients());
                } catch (Exception e) {
                    Alert errorPopUp = new Alert(Alert.AlertType.ERROR);
                    errorPopUp.setTitle("Error");
                    errorPopUp.setContentText(e.getMessage());
                    errorPopUp.show();
                }
            }
        });
        patientButtonsHorizontalBox.getChildren().add(updatePatientButton);


        /// ---------------- GUI Programari ----------------------
        VBox appointmentsVerticalBox = new VBox();
        appointmentsVerticalBox.setPadding(new Insets(10));
        mainHorizontalBox.getChildren().add(appointmentsVerticalBox);

        ListView<Appointment> appointmentsListView = new ListView<>();
        appointmentsListView.setPrefHeight(700);
        appointmentsListView.setPrefWidth(800);
        appointmentsListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Appointment appointment = appointmentsListView.getSelectionModel().getSelectedItem();
                idAppointmentTextField.setText(Integer.toString(appointment.getId()));
                idPatientAppointmentTextField.setText(Integer.toString(appointment.getPatient().getId()));
                dateAppointmentTextField.setText(appointment.getDate());
                hourAppointmentTextField.setText(appointment.getHour());
                purposeAppointmentTextFie.setText(appointment.getPurpose());
            }
        });
        appointmentsVerticalBox.getChildren().add(appointmentsListView);


        GridPane appointmentsGridPane = new GridPane();

        Label idProgramareLabel = new Label();
        idProgramareLabel.setText("ID: ");
        idProgramareLabel.setPadding(new Insets(10, 5, 10, 0));

        Label idPacientProgramareLable = new Label();
        idPacientProgramareLable.setText("ID patient: ");
        idPacientProgramareLable.setPadding(new Insets(10, 5, 10, 0));

        Label dataProgramareLable = new Label();
        dataProgramareLable.setText("Date: ");
        dataProgramareLable.setPadding(new Insets(10, 5, 10, 0));

        Label oraProgramareLable = new Label();
        oraProgramareLable.setText("Hour: ");
        oraProgramareLable.setPadding(new Insets(10, 5, 10, 0));

        Label scopProgramareLable = new Label();
        scopProgramareLable.setText("Purpose: ");
        scopProgramareLable.setPadding(new Insets(10, 5, 10, 0));

        appointmentsGridPane.add(idProgramareLabel, 0, 0);
        appointmentsGridPane.add(idAppointmentTextField, 1, 0);

        appointmentsGridPane.add(idPacientProgramareLable, 0, 1);
        appointmentsGridPane.add(idPatientAppointmentTextField, 1, 1);

        appointmentsGridPane.add(dataProgramareLable, 0, 2);
        appointmentsGridPane.add(dateAppointmentTextField, 1, 2);

        appointmentsGridPane.add(oraProgramareLable, 0, 3);
        appointmentsGridPane.add(hourAppointmentTextField, 1, 3);

        appointmentsGridPane.add(scopProgramareLable, 0, 4);
        appointmentsGridPane.add(purposeAppointmentTextFie, 1, 4);

        appointmentsVerticalBox.getChildren().add(appointmentsGridPane);

        HBox appointmentButtonsHorizontalBox = new HBox();
        appointmentsVerticalBox.getChildren().add(appointmentButtonsHorizontalBox);

        Button addAppointmentButton = new Button("Add appointment");
        addAppointmentButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    int id = Integer.parseInt(idAppointmentTextField.getText());
                    int id_patient = Integer.parseInt(idPatientAppointmentTextField.getText());
                    String date = dateAppointmentTextField.getText();
                    String hour = hourAppointmentTextField.getText();
                    String purpose = purposeAppointmentTextFie.getText();

                    service.addAppointment(id, id_patient, date, hour, purpose);
                    appointments.setAll(service.getAllAppointments());
                } catch (Exception e) {
                    Alert errorPopUp = new Alert(Alert.AlertType.ERROR);
                    errorPopUp.setTitle("Error");
                    errorPopUp.setContentText(e.getMessage());
                    errorPopUp.show();
                }
            }
        });
        appointmentButtonsHorizontalBox.getChildren().add(addAppointmentButton);


        Button deleteAppointmentButton = new Button("Delete appointment");
        deleteAppointmentButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    int id = Integer.parseInt(idAppointmentTextField.getText());

                    service.deleteAppointment(id);
                    appointments.setAll(service.getAllAppointments());
                } catch (Exception e) {
                    Alert errorPopUp = new Alert(Alert.AlertType.ERROR);
                    errorPopUp.setTitle("Error");
                    errorPopUp.setContentText(e.getMessage());
                    errorPopUp.show();
                }
            }
        });
        appointmentButtonsHorizontalBox.getChildren().add(deleteAppointmentButton);


        Button updateAppointmentButton = new Button("Update appointment");
        updateAppointmentButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    int id = Integer.parseInt(idAppointmentTextField.getText());
                    int id_patient = Integer.parseInt(idPatientAppointmentTextField.getText());
                    String date = dateAppointmentTextField.getText();
                    String hour = hourAppointmentTextField.getText();
                    String purpose = purposeAppointmentTextFie.getText();

                    service.updateAppointment(id, id_patient, date, hour, purpose);
                    appointments.setAll(service.getAllAppointments());
                } catch (Exception e) {
                    Alert errorPopUp = new Alert(Alert.AlertType.ERROR);
                    errorPopUp.setTitle("Error");
                    errorPopUp.setContentText(e.getMessage());
                    errorPopUp.show();
                }
            }
        });
        appointmentButtonsHorizontalBox.getChildren().add(updateAppointmentButton);


        patientsListView.setItems(patients);
        appointmentsListView.setItems(appointments);
        Scene scene = new Scene(mainHorizontalBox, 1350, 700);
        stage.setTitle("Application");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}