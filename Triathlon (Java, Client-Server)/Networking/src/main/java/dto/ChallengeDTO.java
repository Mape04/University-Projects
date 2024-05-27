package dto;

import java.io.Serializable;
import java.sql.Date;

public class ChallengeDTO implements Serializable {
    private String Name;
    private Date Date;

    public ChallengeDTO(String name, Date date) {
        this.Name = name;
        this.Date = date;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public Date getDate() {
        return Date;
    }

    public void setDate(Date date) {
        this.Date = date;
    }

    @Override
    public String toString() {
        return "ChallengeDTO{" +
                "Name='" + Name + '\'' +
                ", Date=" + Date +
                '}';
    }
}
