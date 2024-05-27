package Domain;


import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.Objects;

@jakarta.persistence.Entity
@Table(name = "Results")
public class Result extends Entity<Integer>{

    @ManyToOne
    @JoinColumn(name = "ID_Participant")
    private Participant Participant;

    @ManyToOne
    @JoinColumn(name = "ID_Challenge")
    private Challenge Challenge;

    private int Points;
    public Result(Participant participant, Challenge challenge, int points) {
        this.Participant = participant;
        this.Challenge = challenge;
        this.Points = points;
    }

    public Result() {

    }

    public Participant getParticipant() {
        return this.Participant;
    }

    public void setParticipant(Participant participant) {
        this.Participant = participant;
    }

    public Challenge getChallenge() {
        return Challenge;
    }

    public void setChallenge(Challenge challenge) {
        this.Challenge = challenge;
    }

    public int getPoints() {
        return Points;
    }

    public void setPoints(int points) {
        this.Points = points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Result result)) return false;
        return Points == result.Points && Objects.equals(Participant, result.Participant) && Objects.equals(Challenge, result.Challenge);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Participant, Challenge, Points);
    }

    @Override
    public String toString() {
        return "Domain.Result{" +
                "Id=" + Id +
                ", Participant=" + Participant +
                ", Challenge=" + Challenge +
                ", Points=" + Points +
                '}';
    }
}
