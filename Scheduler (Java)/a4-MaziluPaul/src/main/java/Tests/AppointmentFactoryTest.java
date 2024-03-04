package Tests;

import Domain.Appointment;
import Domain.Patient;
import Domain.AppointmentFactory;
import org.junit.jupiter.api.Test;

public class AppointmentFactoryTest {
    @Test
    public void testProgramareFactory(){
        AppointmentFactory appointmentFactory = new AppointmentFactory();
        Patient patient = new Patient(1,"one","one", 1);
        Appointment appointment = new Appointment(1, patient, "one","one","one");
        Appointment appointment1 = appointmentFactory.createEntity("1,1,one,one,1,one,one,one");
        assert appointment1.equals(appointment);

        assert appointmentFactory.toString(appointment).equals("1,1,one,one,1,one,one,one");
    }
}
