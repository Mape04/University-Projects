package dto;

import java.io.Serializable;

public class ParticipantDTO implements Serializable {
    private String Name;
    private String Ssn;

    private int Id;

    public ParticipantDTO(int id, String name, String ssn) {
        this.Id = id;
        this.Name = name;
        this.Ssn = ssn;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getSsn() {
        return Ssn;
    }

    public void setSsn(String ssn) {
        this.Ssn = ssn;
    }

    @Override
    public String toString() {
        return "ParticipantDTO{" +
                "Name='" + Name + '\'' +
                ", Ssn='" + Ssn + '\'' +
                '}';
    }
}
