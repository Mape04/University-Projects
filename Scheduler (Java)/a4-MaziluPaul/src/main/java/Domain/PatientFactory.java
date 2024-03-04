package Domain;

public class PatientFactory implements IEntityFactory<Patient>{

    @Override
    public String toString(Patient patient) {
        return patient.getId() + "," + patient.getLastName() + "," + patient.getFirstName() + "," + patient.getAge();
    }

    @Override
    public Patient createEntity(String line) {
        int id = Integer.parseInt(line.split(",")[0]);
        String nume = line.split(",")[1];
        String prenume = line.split(",")[2];
        int varsta = Integer.parseInt(line.split(",")[3]);

        return new Patient(id, nume, prenume, varsta);
    }
}
