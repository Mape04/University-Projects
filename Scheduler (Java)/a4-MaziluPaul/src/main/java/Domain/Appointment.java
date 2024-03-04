package Domain;

import java.util.Objects;

public class Appointment extends Entity {
    private static final long serialVersionUID = 1000L;
    private Patient patient;
    private String date;
    private String hour;
    private String purpose;
    public Appointment(int id, Patient patient, String date, String hour, String purpose) {
        super(id);
        this.patient = patient;
        this.date = date;
        this.hour = hour;
        this.purpose = purpose;
    }

    public Patient getPatient() {
        return patient;
    }
    public void setPatient(Patient patient) {
        this.patient = patient;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getHour() {
        return hour;
    }
    public void setHour(String hour) {
        this.hour = hour;
    }
    public String getPurpose() {
        return purpose;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", patient=" + patient +
                ", data='" + date + '\'' +
                ", hour='" + hour + '\'' +
                ", purpose='" + purpose + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Appointment appointment = (Appointment) o;
        return this.getId() == appointment.getId() && Objects.equals(this.patient, appointment.getPatient()) && Objects.equals(this.getDate(), appointment.getDate()) && Objects.equals(this.getHour(), appointment.getHour()) && Objects.equals(this.getPurpose(), appointment.getPurpose());
    }
}
