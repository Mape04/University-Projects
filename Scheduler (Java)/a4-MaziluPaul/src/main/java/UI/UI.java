package UI;

import Domain.Appointment;
import Domain.Patient;
import Service.Service;
import Utils.RepositoryExceptions;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class UI {
    private final Service service;

    public UI(Service service) {
        this.service = service;
    }

    private static void printOptions() {
        System.out.println("""
                1. Add\s
                2. Delete\s
                3. Update\s
                4. ShowAll\s
                5. The number of appointments for each individual patient.\s
                6. Total number of appointments for each month of the year.\s
                7. The number of days since each patient's last appointment.\s
                8. The busiest months of the year.\s
                x. Exit""");
    }

    private void addPatient() throws RepositoryExceptions {
        Scanner scn = new Scanner(System.in);

        System.out.print("ID Patient: ");
        int id = scn.nextInt();

        System.out.print("Nume: ");
        String nume = scn.next();

        System.out.print("Prenume: ");
        String prenume = scn.next();

        System.out.print("Varsta: ");
        int varsta = scn.nextInt();

        service.addPatient(id, nume, prenume, varsta);
    }

    private void addAppointment() throws RepositoryExceptions {

        Scanner scn = new Scanner(System.in);
        System.out.print("ID Appointment: ");
        int id = scn.nextInt();

        System.out.print("ID Patient: ");
        int id_pacient = scn.nextInt();

        System.out.print("Data: ");
        String data = scn.next();

        System.out.print("Ora: ");
        String ora = scn.next();

        System.out.print("Scopul: ");
        String scopul = scn.next();

        service.addAppointment(id, id_pacient, data, ora, scopul);
    }

    private int readId() {
        Scanner scn = new Scanner(System.in);

        System.out.print("ID: ");
        return scn.nextInt();
    }

    private void deletePatient() throws RepositoryExceptions {
        int id = readId();
        service.deletePatient(id);
    }

    private void deleteAppointment() throws RepositoryExceptions {
        int id = readId();
        service.deleteAppointment(id);
    }

    private void updatePatient() throws RepositoryExceptions {
        Scanner scn = new Scanner(System.in);

        System.out.print("ID Patient: ");
        int id = scn.nextInt();

        System.out.print("Nume: ");
        String nume = scn.next();

        System.out.print("Prenume: ");
        String prenume = scn.next();

        System.out.print("Varsta: ");
        int varsta = scn.nextInt();

        service.updatePatient(id, nume, prenume, varsta);
    }

    private void updateAppointment() throws RepositoryExceptions {
        Scanner scn = new Scanner(System.in);
        System.out.print("ID Appointment: ");
        int id = scn.nextInt();

        System.out.print("ID Patient: ");
        int id_pacient = scn.nextInt();

        System.out.print("Data: ");
        String data = scn.next();

        System.out.print("Ora: ");
        String ora = scn.next();

        System.out.print("Scopul: ");
        String scopul = scn.next();

        service.updateAppointment(id, id_pacient, data, ora, scopul);
    }

    private void getAllPatients() {
        ArrayList<Patient> all = this.service.getAllPatients();
        for (Patient patient : all) {
            System.out.println(patient.toString());
        }
    }

    private void getAllAppointments() {
        ArrayList<Appointment> all = this.service.getAllAppointments();
        for (Appointment appointment : all) {
            System.out.println(appointment.toString());
        }
    }

    private void add() throws RepositoryExceptions {
        Scanner scn = new Scanner(System.in);
        System.out.print("""
                1. Patient \s
                2. Appointment \n""");

        System.out.print("Option: ");
        String option = scn.nextLine();

        if (Objects.equals(option, "1"))
            addPatient();
        if (Objects.equals(option, "2"))
            addAppointment();

    }

    private void delete() throws RepositoryExceptions {
        Scanner scn = new Scanner(System.in);
        System.out.print("""
                1. Patient \s
                2. Appointment \n""");
        System.out.print("Option: ");
        String option = scn.nextLine();

        if (Objects.equals(option, "1"))
            deletePatient();
        if (Objects.equals(option, "2"))
            deleteAppointment();

    }

    private void update() throws RepositoryExceptions {
        Scanner scn = new Scanner(System.in);
        System.out.print("""
                1. Patient \s
                2. Appointment \n""");

        System.out.print("Option: ");
        String option = scn.nextLine();

        if (Objects.equals(option, "1"))
            updatePatient();
        if (Objects.equals(option, "2"))
            updateAppointment();
    }

    private void getAll() {
        Scanner scn = new Scanner(System.in);
        System.out.print("""
                1. Pacienti \s
                2. Programari \n""");

        System.out.print("Option: ");
        String option = scn.nextLine();

        if (Objects.equals(option, "1"))
            getAllPatients();
        if (Objects.equals(option, "2"))
            getAllAppointments();

    }

    private void raportNrPro(){
        service.raportNumarProgramari();
    }

    private void raportNrProPeLuna(){
        service.raportNumarProgramariPeLuna();
    }

    private void raportZileTrecuteDeLaUltimaProgramare(){
        service.raportZileTrecuteDeLaUltimaProgramare();
    }

    private void raportCeleMaiAglomerateLuni(){
        service.raportCeleMaiAglomerateLuni();
    }

    public void run() {
        while (true) {
            try {
                printOptions();
                Scanner scn = new Scanner(System.in);
                System.out.print("Choose option: ");
                String option = scn.nextLine();

                switch (option) {
                    case "1":
                        add();
                        break;

                    case "2":
                        delete();
                        break;

                    case "3":
                        update();
                        break;

                    case "4":
                        getAll();
                        break;

                    case "5":
                        raportNrPro();
                        break;

                    case "6":
                        raportNrProPeLuna();
                        break;

                    case "7":
                        raportZileTrecuteDeLaUltimaProgramare();
                        break;

                    case "8":
                        raportCeleMaiAglomerateLuni();
                        break;
                    case "x":
                        System.exit(0);
                        break;
                }
            }catch(RepositoryExceptions e){
                System.out.println(e);
            }

        }
    }
}
