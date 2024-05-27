package Domain;


import jakarta.persistence.Table;

import java.util.Objects;

@jakarta.persistence.Entity
@Table(name = "Participants")
public class Participant extends Entity<Integer>{

    private String Name;

    private String Ssn;

    public Participant(String name, String ssn) {
        this.Name = name;
        this.Ssn = ssn;
    }

    public Participant() {

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Participant that)) return false;
        return Objects.equals(Name, that.Name) && Objects.equals(Ssn, that.Ssn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Name, Ssn);
    }

    @Override
    public String toString() {
        return "Domain.Participant{" +
                "Id=" + Id +
                ", Name='" + Name + '\'' +
                ", Ssn='" + Ssn + '\'' +
                '}';
    }
}
