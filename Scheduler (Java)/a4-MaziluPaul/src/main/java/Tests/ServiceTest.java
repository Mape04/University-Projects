package Tests;

import Domain.Patient;
import Domain.Appointment;
import Repository.MemoryRepository;
import Service.Service;
import Utils.RepositoryExceptions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServiceTest {
    MemoryRepository<Patient> pacientMemoryRepository = new MemoryRepository<>();
    MemoryRepository<Appointment> programareMemoryRepository = new MemoryRepository<>();
    Service service = new Service(pacientMemoryRepository, programareMemoryRepository);
    @Test
    public void testAdd() throws RepositoryExceptions {
        service.addPatient(1,"one","one", 1);
        assert 1 == service.getAllPatients().size();

        service.addAppointment(1,1,"10/10/2010", "10:00", "scop");
        assert 1 == service.getAllAppointments().size();
    }
    @Test
    public void testDelete() throws RepositoryExceptions {
        service.addPatient(1,"one","one", 1);
        service.addPatient(2,"two","two", 2);
        service.deletePatient(1);

        assert 1 == service.getAllPatients().size();

        service.addPatient(1,"one","one", 1);
        service.addAppointment(1,1, "1","1","1");
        service.addAppointment(2,2, "2","2","2");
        service.deleteAppointment(2);

        assert 1 == service.getAllAppointments().size();

        service.deletePatient(1);

        assert service.getAllAppointments().isEmpty();
    }

    @Test
    public void testUpdate() throws RepositoryExceptions {
        service.addPatient(1,"one","one", 1);
        service.updatePatient(1,"1","1",1);

        assertEquals(service.getAllPatients().get(0), new Patient(1, "1", "1", 1));

        service.addPatient(2,"2","2",2);
        service.addAppointment(1,1,"10/10/2010", "10:00", "scop");
        service.updateAppointment(1,2, "1","1","1");

        assertEquals(service.getAllAppointments().get(0), new Appointment(1, new Patient(2, "2", "2", 2), "1", "1", "1"));

        service.updatePatient(2,"3","3",3);

        assertEquals(service.getAllAppointments().get(0).getPatient(), new Patient(2,"3","3",3));
    }
}
