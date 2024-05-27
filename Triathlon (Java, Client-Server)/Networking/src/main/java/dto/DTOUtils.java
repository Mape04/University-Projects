package dto;

import Domain.Challenge;
import Domain.Participant;
import Domain.Referee;
import Domain.Result;

public class DTOUtils {
    public static Referee getFromDTO(RefereeDTO refereeDTO) {
        int id = refereeDTO.getId();
        String name = refereeDTO.getName();
        String password = refereeDTO.getPassword();
        Challenge challenge = refereeDTO.getChallenge();
        Referee referee =  new Referee(name, password, challenge);
        referee.setId(id);
        return referee;

    }

    public static RefereeDTO getDTO(Referee referee) {
        int id = referee.getId();
        String name = referee.getName();
        String password = referee.getPassword();
        Challenge challenge = referee.getChallenge();
        return new RefereeDTO(id, name, password, challenge);
    }

    public static Result getFromDTO(ResultDTO resultDTO){
        Participant participant = resultDTO.getParticipant();
        Challenge challenge = resultDTO.getChallenge();
        int points = resultDTO.getPoints();
        return new Result(participant, challenge, points);
    }

    public static ResultDTO getDTO(Result result){
        Participant participant = result.getParticipant();
        Challenge challenge = result.getChallenge();
        int points = result.getPoints();
        return new ResultDTO(participant, challenge, points);
    }

    public static ResultDTO[] getDTO(Result[] results) {
        ResultDTO[] frDTO = new ResultDTO[results.length];
        for (int i = 0; i < results.length; i++)
            frDTO[i] = getDTO(results[i]);
        return frDTO;
    }

    public static Result[] getFromDTO(ResultDTO[] results) {
        Result[] friends = new Result[results.length];
        for (int i = 0; i < results.length; i++) {
            friends[i] = getFromDTO(results[i]);
        }
        return friends;
    }



    public static Participant getFromDTO(ParticipantDTO participantDTO){
        int id = participantDTO.getId();
        String name = participantDTO.getName();
        String ssn = participantDTO.getSsn();
        Participant participant = new Participant(name, ssn);
        participant.setId(id);
        return participant;
    }

    public static ParticipantDTO getDTO(Participant participant){
        int id = participant.getId();
        String name = participant.getName();
        String ssn = participant.getSsn();
        return new ParticipantDTO(id, name, ssn);
    }

    public static ParticipantDTO[] getDTO(Participant[] participants) {
        ParticipantDTO[] frDTO = new ParticipantDTO[participants.length];
        for (int i = 0; i < participants.length; i++)
            frDTO[i] = getDTO(participants[i]);
        return frDTO;
    }

    public static Participant[] getFromDTO(ParticipantDTO[] participants) {
        Participant[] friends = new Participant[participants.length];
        for (int i = 0; i < participants.length; i++) {
            friends[i] = getFromDTO(participants[i]);
        }
        return friends;
    }
}
