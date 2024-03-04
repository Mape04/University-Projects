package Service;

import Domain.Appointment;
import Domain.Patient;
import Repository.IRepository;
import Utils.RepositoryExceptions;
import Utils.Validators;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class Service {
    Validators validators = new Validators();

    IRepository<Patient> patientRepo;
    IRepository<Appointment> appointmentRepo;

    public Service(IRepository<Patient> patientRepo, IRepository<Appointment> appointmentRepo) {
        this.patientRepo = patientRepo;
        this.appointmentRepo = appointmentRepo;
    }

    public Patient makePatient(int id, String lastName, String firstName, int age) {
        return new Patient(id, lastName, firstName, age);
    }

    public Appointment makeAppointment(int id, Patient patient, String date, String hour, String purpose) {
        return new Appointment(id, patient, date, hour, purpose);
    }

    public void addPatient(int id, String lastName, String firstName, int age) throws RepositoryExceptions {
        validators.validatePacientId(id, getAllPatients());
        Patient patient = makePatient(id, lastName, firstName, age);
        patientRepo.add(patient);
    }

    public void addAppointment(int id, int id_patient, String date, String hour, String purpose) throws RepositoryExceptions {
        validators.validateProgramareId(id, getAllAppointments());
        validators.checkIfPacientExists(id_patient, getAllPatients());
        Patient patient = (Patient) patientRepo.getById(id_patient);
        Appointment appointment = makeAppointment(id, patient, date, hour, purpose);
        appointmentRepo.add(appointment);
    }

    public void deletePatient(int id) throws RepositoryExceptions {
        validators.checkIfPacientExists(id, getAllPatients());
        Patient patient = (Patient) patientRepo.getById(id);
        ArrayList<Integer> id_appointments = new ArrayList<>();

        for (Appointment appointment : appointmentRepo.getAll()) {
            if (appointment.getPatient().getId() == patient.getId()) {
                id_appointments.add(appointment.getId());
            }
        }

        for (Integer idp : id_appointments) {
            Appointment appointment = (Appointment) appointmentRepo.getById(idp);
            appointmentRepo.delete(appointment);
        }
        patientRepo.delete(patient);
    }

    public void deleteAppointment(int id) throws RepositoryExceptions {
        validators.checkIfProgramareExists(id, getAllAppointments());
        Appointment appointment = (Appointment) appointmentRepo.getById(id);
        appointmentRepo.delete(appointment);
    }

    public void updatePatient(int id, String lastName, String firstName, int age) throws RepositoryExceptions {
        validators.checkIfPacientExists(id, getAllPatients());
        Patient patient = (Patient) patientRepo.getById(id);
        patient.setLastName(lastName);
        patient.setFirstName(firstName);
        patient.setAge(age);
        patientRepo.update(patient);

        ArrayList<Integer> id_appointments = new ArrayList<>();
        for (Appointment appointment : appointmentRepo.getAll()) {
            if (appointment.getPatient().getId() == patient.getId()) {
                id_appointments.add(appointment.getId());
            }
        }
        for (Integer idp : id_appointments) {
            Appointment appointment = (Appointment) appointmentRepo.getById(idp);
            appointment.setPatient(patient);
        }

    }

    public void updateAppointment(int id, int id_patient, String date, String hour, String purpose) throws RepositoryExceptions {
        validators.checkIfProgramareExists(id, getAllAppointments());
        Patient patient = (Patient) patientRepo.getById(id_patient);
        Appointment appointment = (Appointment) appointmentRepo.getById(id);
        appointment.setPatient(patient);
        appointment.setDate(date);
        appointment.setHour(hour);
        appointment.setPurpose(purpose);
        appointmentRepo.update(appointment);
    }

    public ArrayList<Patient> getAllPatients() {
        return patientRepo.getAll();
    }

    public ArrayList<Appointment> getAllAppointments() {
        return appointmentRepo.getAll();
    }


    public void raportNumarProgramari() {
        List<Appointment> programari = getAllAppointments();
        // Gruparea programarilor dupa pacienți
        Map<Patient, Long> numarProgramariPePacient = programari.stream()
                .collect(Collectors.groupingBy(Appointment::getPatient, Collectors.counting()));

        // Sortarea rezultatelor in ordine descrescatoare
        numarProgramariPePacient.entrySet().stream()
                .sorted((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue()))
                .forEach(entry -> {
                    Patient patient = entry.getKey();
                    Long numarProgramari = entry.getValue();
                    System.out.println("Patient: " + patient.getLastName() + " " + patient.getFirstName() +
                            ", Numar de programari: " + numarProgramari);
                });
    }


    public void raportNumarProgramariPeLuna() {
        List<Appointment> programari = getAllAppointments();
        // Definirea unui format pentru parsarea datelor
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");

        // Gruparea programarilor dupa luna
        Map<Month, Long> numarProgramariPeLuna = programari.stream()
                .collect(Collectors.groupingBy(programare -> {
                    LocalDate dataProgramare = LocalDate.parse(programare.getDate(), formatter);
                    return dataProgramare.getMonth();
                }, Collectors.counting()));

        // Sortarea rezultatelor în ordine descrescatoare
        numarProgramariPeLuna.entrySet().stream()
                .sorted((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue()))
                .forEach(entry -> {
                    Month luna = entry.getKey();
                    Long numarProgramari = entry.getValue();
                    String numeLuna = luna.getDisplayName(TextStyle.FULL, Locale.getDefault());
                    System.out.println("Luna: " + numeLuna + ", Numar total de programari: " + numarProgramari);
                });
    }


    public void raportZileTrecuteDeLaUltimaProgramare() {
        List<Appointment> programari = getAllAppointments();
        // Definirea unui format pentru parsarea datelor
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");

        // Gruparea programarilor dupa pacient
        Map<Patient, LocalDate> ultimaProgramarePentruPacient = programari.stream()
                .collect(Collectors.groupingBy(Appointment::getPatient,
                        Collectors.collectingAndThen(
                                Collectors.maxBy((p1, p2) -> {
                                    LocalDate d1 = LocalDate.parse(p1.getDate(), formatter);
                                    LocalDate d2 = LocalDate.parse(p2.getDate(), formatter);
                                    return d1.compareTo(d2);
                                }),
                                opt -> opt.map(programare -> LocalDate.parse(programare.getDate(), formatter))
                                        .orElse(LocalDate.of(2000, 1, 1))
                        )
                ));

        // Calcularea numarului de zile trecute de la ultima programare si sortarea rezultatelor
        ultimaProgramarePentruPacient.entrySet().stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .forEach(entry -> {
                    Patient patient = entry.getKey();
                    LocalDate ultimaProgramare = entry.getValue();
                    long zileTrecute = ultimaProgramare.until(LocalDate.now()).getDays();
                    System.out.println("Patient: " + patient.getLastName() + " " + patient.getFirstName() +
                            ", Ultima programare: " + ultimaProgramare.format(formatter) +
                            ", Zile trecute: " + zileTrecute);
                });
    }


    public void raportCeleMaiAglomerateLuni() {
        List<Appointment> programari = getAllAppointments();
        // Definirea unui format pentru parsarea datelor
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");

        // Gruparea programarilor după lună si numararea programarilor pentru fiecare luna
        Map<Month, Long> numarProgramariPeLuna = programari.stream()
                .collect(Collectors.groupingBy(programare -> {
                    LocalDate dataProgramare = LocalDate.parse(programare.getDate(), formatter);
                    return dataProgramare.getMonth();
                }, Collectors.counting()));

        // Sortarea rezultatelorin ordine descrescatoare
        numarProgramariPeLuna.entrySet().stream()
                .sorted((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue()))
                .forEach(entry -> {
                    Month luna = entry.getKey();
                    Long numarProgramari = entry.getValue();
                    String numeLuna = luna.getDisplayName(TextStyle.FULL, Locale.getDefault());
                    System.out.println("Luna: " + numeLuna + ", Numar total de programari: " + numarProgramari);
                });
    }
}
