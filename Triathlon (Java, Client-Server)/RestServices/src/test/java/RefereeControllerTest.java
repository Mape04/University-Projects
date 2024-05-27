import Domain.Challenge;
import Domain.Referee;
import Repository.Interfaces.IRefereeRepository;
import controller.RefereeController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class RefereeControllerTest {

    @InjectMocks
    private RefereeController refereeController;

    @Mock
    private IRefereeRepository refereeRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAll() {
        Referee referee1 = new Referee("John", "password1", new Challenge("Challenge1", Date.valueOf(LocalDate.now())));
        Referee referee2 = new Referee("Jane", "password2", new Challenge("Challenge2", Date.valueOf(LocalDate.now())));
        List<Referee> referees = Arrays.asList(referee1, referee2);

        when(refereeRepository.getAll()).thenReturn(referees);

        Referee[] result = refereeController.getAll();

        assertEquals(2, result.length);
        assertEquals("John", result[0].getName());
        assertEquals("Jane", result[1].getName());
    }

    @Test
    public void testGetById() {
        Referee referee = new Referee("John", "password1", new Challenge("Challenge1", Date.valueOf(LocalDate.now())));
        referee.setId(1);

        when(refereeRepository.findById(1)).thenReturn(referee);

        ResponseEntity<?> response = refereeController.getById("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(referee, response.getBody());
    }

    @Test
    public void testGetByIdNotFound() {
        when(refereeRepository.findById(1)).thenReturn(null);

        ResponseEntity<?> response = refereeController.getById("1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCreate() {
        Referee referee = new Referee("John", "password1", new Challenge("Challenge1", Date.valueOf(LocalDate.now())));

        doNothing().when(refereeRepository).add(any(Referee.class));

        Referee result = refereeController.create(referee);

        assertNotNull(result);
        assertEquals("John", result.getName());
        verify(refereeRepository, times(1)).add(referee);
    }

    @Test
    public void testUpdate() {
        Referee referee = new Referee("John", "password1", new Challenge("Challenge1", Date.valueOf(LocalDate.now())));
        referee.setId(1);

        doNothing().when(refereeRepository).update(any(Referee.class), anyInt());

        Referee result = refereeController.update(referee);

        assertNotNull(result);
        assertEquals("John", result.getName());
        verify(refereeRepository, times(1)).update(referee, referee.getId());
    }

    @Test
    public void testDelete() {
        Referee referee = new Referee("John", "password1", new Challenge("Challenge1", Date.valueOf(LocalDate.now())));

        when(refereeRepository.getRefereeByName("John")).thenReturn(referee);
        doNothing().when(refereeRepository).delete(any(Referee.class));

        ResponseEntity<?> response = refereeController.delete("John");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(refereeRepository, times(1)).delete(referee);
    }
}
