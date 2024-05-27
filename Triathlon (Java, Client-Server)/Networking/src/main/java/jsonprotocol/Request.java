package jsonprotocol;

import dto.ChallengeDTO;
import dto.ParticipantDTO;
import dto.RefereeDTO;
import dto.ResultDTO;

import java.util.Arrays;

public class Request {
    private RefereeDTO refereeDTO;

    private ParticipantDTO participantDTO;

    private ChallengeDTO challengeDTO;

    private ResultDTO resultDTO;

    private RequestType type;

    public Request(){}

    public RefereeDTO getRefereeDTO() {
        return refereeDTO;
    }

    public void setRefereeDTO(RefereeDTO refereeDTO) {
        this.refereeDTO = refereeDTO;
    }

    public ParticipantDTO getParticipantDTO() {
        return participantDTO;
    }

    public void setParticipantDTO(ParticipantDTO participantDTO) {
        this.participantDTO = participantDTO;
    }

    public ChallengeDTO getChallengeDTO() {
        return challengeDTO;
    }

    public void setChallengeDTO(ChallengeDTO challengeDTO) {
        this.challengeDTO = challengeDTO;
    }

    public ResultDTO getResultDTO() {
        return resultDTO;
    }

    public void setResultDTO(ResultDTO resultDTO) {
        this.resultDTO = resultDTO;
    }

    public RequestType getType() {
        return type;
    }

    public void setType(RequestType type) {
        this.type = type;
    }


    @Override
    public String toString() {
        return "Request{" +
                "refereeDTO=" + refereeDTO +
                ", participantDTO=" + participantDTO +
                ", challengeDTO=" + challengeDTO +
                ", resultDTO=" + resultDTO +
                ", type=" + type +
                '}';
    }
}