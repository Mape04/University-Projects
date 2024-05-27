package jsonprotocol;

import dto.ChallengeDTO;
import dto.ParticipantDTO;
import dto.RefereeDTO;
import dto.ResultDTO;

import java.io.Serializable;

public class Response implements Serializable {

    private String errorMessage;
    private RefereeDTO refereeDTO;

    private ParticipantDTO participantDTO;

    private ParticipantDTO[] participantDTOS;

    private ResultDTO[] resultDTOS;

    private ChallengeDTO challengeDTO;

    private ResultDTO resultDTO;

    private ResponseType responseType;

    private boolean hasResult;

    public boolean isHasResult() {
        return hasResult;
    }

    public void setHasResult(boolean hasResult) {
        this.hasResult = hasResult;
    }

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

    public ResponseType getResponseType() {
        return responseType;
    }

    public void setResponseType(ResponseType responseType) {
        this.responseType = responseType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ParticipantDTO[] getParticipantDTOS() {
        return participantDTOS;
    }

    public void setParticipantDTOS(ParticipantDTO[] participantDTOS) {
        this.participantDTOS = participantDTOS;
    }

    public ResultDTO[] getResultDTOS() {
        return resultDTOS;
    }

    public void setResultDTOS(ResultDTO[] resultDTOS) {
        this.resultDTOS = resultDTOS;
    }

    @Override
    public String toString() {
        return "Response{" +
                "refereeDTO=" + refereeDTO +
                ", participantDTO=" + participantDTO +
                ", challengeDTO=" + challengeDTO +
                ", resultDTO=" + resultDTO +
                ", responseType=" + responseType +
                '}';
    }

}