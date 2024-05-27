package Interfaces;

import Domain.Challenge;
import Domain.Participant;
import Domain.Referee;
import Domain.Result;
import ro.mpp2024.ServicesException;

import java.util.Collection;

public interface IService {
    void addChallenge(Challenge challenge);
    Collection<Challenge> getAllChallenges();
    Challenge findByName(String name);
    Collection<Challenge> getChallengesByName(String name);
    int getTotalPointsById(Integer id);
    void addParticipant(String name, String ssn);
    void deleteParticipant(int id);
    void updateParticipant(int id, String newName, String newSSN);
    Collection<Participant> getAllParticipants() throws ServicesException;
    Participant findBySSN(String ssn) throws ServicesException;
    Referee getRefereeByName(String name) throws ServicesException;
    void addResult(Result result) throws ServicesException;
    Result getResult(int participantId, int challengeId);
    Collection<Result> getAllResults() throws ServicesException;
    boolean hasResult(Participant participant, Challenge challenge);
    int getTotalPointsBySsn(String ssn) throws ServicesException;

    void login(Referee referee, IObserver client) throws ServicesException;
    void logout(Referee referee, IObserver client) throws ServicesException;


}
