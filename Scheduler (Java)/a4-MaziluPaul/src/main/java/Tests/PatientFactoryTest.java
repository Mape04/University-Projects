package Tests;

import Domain.Patient;
import Domain.PatientFactory;
import org.junit.jupiter.api.Test;

public class PatientFactoryTest {
    @Test
    public void testPacientFactory(){
        PatientFactory patientFactory = new PatientFactory();
        Patient patient = new Patient(1,"one","one",1);
        Patient patient1 = patientFactory.createEntity("1,one,one,1");
        assert patient.equals(patient1);

        assert (patientFactory.toString(patient)).equals("1,one,one,1");
    }
}
