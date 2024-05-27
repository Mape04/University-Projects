package dto;

import Domain.Challenge;
import Domain.Participant;

import java.io.Serializable;

public class ResultDTO implements Serializable {
    private Participant Participant;
    private Challenge Challenge;
    private int Points;

    public ResultDTO(Participant participant, Challenge challenge, int points) {
        this.Participant = participant;
        this.Challenge = challenge;
        this.Points = points;
    }

    public Participant getParticipant() {
        return Participant;
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
    public String toString() {
        return "ResultDTO{" +
                "Participant=" + Participant +
                ", Challenge=" + Challenge +
                ", Points=" + Points +
                '}';
    }
}
