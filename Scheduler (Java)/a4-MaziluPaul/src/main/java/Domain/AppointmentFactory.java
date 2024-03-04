package Domain;

public class AppointmentFactory implements IEntityFactory<Appointment> {
    @Override
    public String toString(Appointment object) {
        Patient patient = object.getPatient();
        return object.getId() + "," + patient.getId() + "," + patient.getLastName() + "," + patient.getFirstName() + "," + patient.getAge() + "," + object.getDate() + "," + object.getHour() + "," + object.getPurpose();
    }

    @Override
    public Appointment createEntity(String line) {
        int id = Integer.parseInt(line.split(",")[0]);
        int id_pacient = Integer.parseInt(line.split(",")[1]);
        String nume = line.split(",")[2];
        String prenume = line.split(",")[3];
        int varsta = Integer.parseInt(line.split(",")[4]);
        Patient patient = new Patient(id_pacient,nume,prenume,varsta);
        String data = line.split(",")[5];
        String ora = line.split(",")[6];
        String scop = line.split(",")[7];

        return new Appointment(id, patient, data, ora, scop);
    }
}
