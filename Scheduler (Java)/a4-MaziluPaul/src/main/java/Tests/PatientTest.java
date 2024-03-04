package Tests;

import Domain.Patient;
import org.junit.jupiter.api.Test;

public class PatientTest {
    @Test
    public void testPacient(){
        Patient patient = new Patient(1,"one","one",1);
        assert patient.getId() == 1;
        assert "one".equals(patient.getLastName());
        assert "one".equals(patient.getFirstName());
        assert patient.getAge() == 1;

        patient.setLastName("two");
        patient.setFirstName("two");
        patient.setAge(2);

        assert "two".equals(patient.getLastName());
        assert "two".equals(patient.getFirstName());
        assert patient.getAge() == 2;

        assert "Patient{id=1, nume='two', prenume='two', varsta=2}".equals(patient.toString());
    }
}
