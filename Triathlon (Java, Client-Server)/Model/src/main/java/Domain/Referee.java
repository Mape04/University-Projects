package Domain;


import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.util.Objects;

@jakarta.persistence.Entity
@Table(name = "Referees")
public class Referee extends Entity<Integer> {

    private String Name;
    private String Password;
    @OneToOne
    @JoinColumn(name = "ID_Challenge")
    private Challenge Challenge;

    public Referee(String name, String password, Challenge challenge) {
        this.Name = name;
        this.Password = password;
        this.Challenge = challenge;
    }

    public Referee() {

    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public Challenge getChallenge() {
        return Challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.Challenge = challenge;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Referee referee)) return false;
        return Objects.equals(Name, referee.Name) && Objects.equals(Password, referee.Password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Name, Password);
    }

    @Override
    public String toString() {
        return "Domain.Referee{" +
                "Id=" + Id +
                ", Name='" + Name + '\'' +
                ", Password='" + Password + '\'' +
                '}';
    }
}
