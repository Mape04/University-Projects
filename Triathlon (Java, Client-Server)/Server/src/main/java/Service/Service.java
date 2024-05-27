package Service;

import Domain.Challenge;
import Domain.Participant;
import Domain.Referee;
import Domain.Result;
import Interfaces.IObserver;
import Interfaces.IService;
import Repository.Interfaces.IChallengeRepository;
import Repository.Interfaces.IParticipantRepository;
import Repository.Interfaces.IRefereeRepository;
import Repository.Interfaces.IResultRepository;
import ro.mpp2024.ServicesException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Service implements IService {
    private final IParticipantRepository participantRepository;
    private final IChallengeRepository challengeRepository;
    private final IRefereeRepository refereeRepository;
    private final IResultRepository resultRepository;
    private Map<Integer, IObserver> loggedClients;

    public Service(IParticipantRepository participantRepository, IChallengeRepository challengeRepository, IRefereeRepository refereeRepository, IResultRepository resultRepository) {
        this.participantRepository = participantRepository;
        this.challengeRepository = challengeRepository;
        this.refereeRepository = refereeRepository;
        this.resultRepository = resultRepository;
        loggedClients = new ConcurrentHashMap<>();
    }

    @Override
    public void login(Referee referee, IObserver client) throws ServicesException {
        Referee refereeR = refereeRepository.findByNameAndPassword(referee.getName(), referee.getPassword());
        if (refereeR != null) {
            if (loggedClients.get(refereeR.getId()) != null)
                throw new ServicesException("Referee already logged in.");
            loggedClients.put(refereeR.getId(), client);
        } else
            throw new ServicesException("Authentication failed.");
    }

    @Override
    public void logout(Referee referee, IObserver client) throws ServicesException {
        IObserver localClient = loggedClients.remove(referee.getId());
        if (localClient == null)
            throw new ServicesException("User " + referee.getId() + " is not logged in.");
    }

    public void addChallenge(Challenge challenge) {
        challengeRepository.add(challenge);
    }

    public Collection<Challenge> getAllChallenges() {
        return challengeRepository.getAll();
    }

    public Challenge findByName(String name) {
        return this.challengeRepository.findByName(name);
    }

    public Collection<Challenge> getChallengesByName(String name) {
        Collection<Challenge> challenges = getAllChallenges();
        Collection<Challenge> newChallenges = new ArrayList<>();
        for (Challenge challenge : challenges) {
            newChallenges.add(findByName(challenge.getName()));
        }
        return newChallenges;
    }

    public int getTotalPointsById(Integer id) {
        return participantRepository.getTotalPointsById(id);
    }

    public void addParticipant(String name, String ssn) {
        Participant participant = new Participant(name, ssn);
        participantRepository.add(participant);
    }

    public void deleteParticipant(int id) {
        Participant participant = participantRepository.findById(id);
        if (participant != null) {
            participantRepository.delete(participant);
        } else {
            System.out.println("Participant not found with ID: " + id);
        }
    }

    public void updateParticipant(int id, String newName, String newSSN) {
        Participant participant = participantRepository.findById(id);
        if (participant != null) {
            participant.setName(newName);
            participant.setSsn(newSSN);
            participantRepository.update(participant, id);
        } else {
            System.out.println("Participant not found with ID: " + id);
        }
    }

    public Collection<Participant> getAllParticipants() {
        return participantRepository.getAll();
    }

    public Participant findBySSN(String ssn) {
        return this.participantRepository.findBySSN(ssn);
    }

    public Referee getRefereeByName(String name) {
        return refereeRepository.getRefereeByName(name);
    }


    public void addResult(Result result) {
        this.resultRepository.add(result);
    }

    public Result getResult(int participantId, int challengeId) {
        return this.resultRepository.getResult(participantId, challengeId);
    }

    public Collection<Result> getAllResults() {
        return this.resultRepository.getAll();
    }

    public boolean hasResult(Participant participant, Challenge challenge) {
        return this.resultRepository.hasResult(participant, challenge);
    }

    @Override
    public int getTotalPointsBySsn(String ssn) throws ServicesException {
        return this.resultRepository.getTotalPointsBySsn(ssn);
    }



}
