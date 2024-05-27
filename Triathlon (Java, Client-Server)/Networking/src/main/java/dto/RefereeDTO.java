package dto;

import Domain.Challenge;

import java.io.Serializable;

public class RefereeDTO implements Serializable {
    private int Id;
    private String Name;
    private String Password;
    private Challenge Challenge;

    public RefereeDTO(int id, String name, String password, Challenge challenge) {
        this.Id = id;
        this.Name = name;
        this.Password = password;
        this.Challenge = challenge;
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
    public String toString() {
        return "RefereeDTO{" +
                "Name='" + Name + '\'' +
                ", Password='" + Password + '\'' +
                ", Challenge=" + Challenge +
                '}';
    }
}
