package Tests;

import Domain.Appointment;
import Domain.Patient;
import org.junit.jupiter.api.Test;

public class AppointmentTest {
    @Test
    public void testProgramare(){
        Patient patient = new Patient(1,"one", "one", 1);
        Appointment appointment = new Appointment(1, patient, "one", "one", "one");

        assert appointment.getId() == 1;
        assert appointment.getPatient() == patient;
        assert "one".equals(appointment.getDate());
        assert "one".equals(appointment.getHour());
        assert "one".equals(appointment.getPurpose());

        Patient patient1 = new Patient(2, "two", "two", 2);
        appointment.setPatient(patient1);
        appointment.setDate("two");
        appointment.setHour("two");
        appointment.setPurpose("two");

        assert appointment.getPatient() == patient1;
        assert "two".equals(appointment.getDate());
        assert "two".equals(appointment.getHour());
        assert "two".equals(appointment.getPurpose());

        assert "Appointment{id=1, patient=Patient{id=2, nume='two', prenume='two', varsta=2}, data='two', ora='two', scop='two'}".equals(appointment.toString());
    }
}
