package Main;

import Domain.*;
import Repository.*;
import Service.Service;
import UI.UI;
import Utils.RepositoryExceptions;

import java.util.Objects;

public class Main {
    public static void main(String[] args) throws RepositoryExceptions {

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
        UI ui = new UI(service);
        ui.run();

    }
}
