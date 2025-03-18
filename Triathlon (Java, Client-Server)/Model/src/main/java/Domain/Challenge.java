package Domain;


import jakarta.persistence.Column;
import jakarta.persistence.Table;

import java.sql.Date;
import java.util.Objects;

@jakarta.persistence.Entity
@Table(name = "Challenges")
public class Challenge extends Entity<Integer>{

    @Column(name = "Date")
    private Date Date;
    private String Name;
    public Challenge(String name, Date date) {
        this.Name = name;
        this.Date = date;
    }

    public Challenge() {

    }

    public Date getDate() {
        return Date;
    }

    public void setDate(Date date) {
        this.Date = date;
    }

    public String getName(){ return Name;}

    public void setName(String name){ this.Name = name;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Challenge challenge)) return false;
        return Objects.equals(Date, challenge.Date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Date);
    }

    @Override
    public String toString() {
        return "Domain.Challenge{" +
                "Id=" + Id +
                ", Name=" + Name +
                ", Date=" + Date +
                '}';
    }
}
