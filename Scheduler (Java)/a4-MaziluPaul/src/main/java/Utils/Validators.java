package Utils;

import Domain.Appointment;
import Domain.Patient;

import java.util.ArrayList;

public class Validators {
    // Ne uitam daca deja exista un id in lista de pacineti si aruncam exceptie
    public void validatePacientId(int id, ArrayList<Patient> patients) throws DuplicateObjectException {
        for(Patient patient : patients){
            if(patient.getId() == id)
                throw new DuplicateObjectException("ID Patient deja existent!\n");
        }
    }
    public void validateProgramareId(int id, ArrayList<Appointment> appointments) throws DuplicateObjectException {
        for(Appointment appointment : appointments){
            if(appointment.getId() == id)
                throw new DuplicateObjectException("ID Appointment deja existent!\n");
        }
    }

    //Ne uitam daca exista pacientul in lista de pacienti
    public void checkIfPacientExists(int id, ArrayList<Patient> patients) throws ObjectNotFoundException {
        boolean ok = false;
        for(Patient patient : patients){
            if (patient.getId() == id) {
                ok = true;
                break;
            }
        }
        if(!ok){
            throw new ObjectNotFoundException("Patient inexistent!\n");
        }
    }

    public void checkIfProgramareExists(int id, ArrayList<Appointment> appointments) throws ObjectNotFoundException {
        boolean ok = false;
        for(Appointment appointment : appointments){
            if (appointment.getId() == id) {
                ok = true;
                break;
            }
        }
        if(!ok){
            throw new ObjectNotFoundException("Appointment inexistenta!\n");
        }
    }
}
