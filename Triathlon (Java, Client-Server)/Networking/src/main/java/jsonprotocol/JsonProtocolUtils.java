package jsonprotocol;

import Domain.Participant;
import Domain.Referee;
import Domain.Result;
import dto.DTOUtils;
import dto.ParticipantDTO;


public class JsonProtocolUtils {
    public static Request createLoginRequest(Referee referee) {
        Request req = new Request();
        req.setType(RequestType.LOGIN);
        req.setRefereeDTO(DTOUtils.getDTO(referee));
        return req;
    }

    public static Request createLogoutRequest(Referee referee) {
        Request req = new Request();
        req.setType(RequestType.LOGOUT);
        req.setRefereeDTO(DTOUtils.getDTO(referee));
        return req;
    }

    public static Response createErrorResponse(String errorMessage) {
        Response resp = new Response();
        resp.setResponseType(ResponseType.ERROR);
        resp.setErrorMessage(errorMessage);
        return resp;
    }

    public static Response createOkResponse() {
        Response resp = new Response();
        resp.setResponseType(ResponseType.OK);
        return resp;
    }

    public static Request createAllParticipantsRequest() {
        Request req = new Request();
        req.setType(RequestType.GET_ALL_PARTICIPANTS);
        return req;
    }

    public static Response createAllParticipantsResponse(Participant[] participants) {
        Response resp = new Response();
        resp.setResponseType(ResponseType.GET_ALL_PARTICIPANTS);
        resp.setParticipantDTOS(DTOUtils.getDTO(participants));
        return resp;
    }

    public static Response createGetTotalPointsResponse(int points) {
        Response resp = new Response();
        resp.setResponseType(ResponseType.GET_TOTAL_POINTS_BY_ID);
        resp.setResultDTO(DTOUtils.getDTO(new Result(null, null, points)));
        return resp;
    }

    public static Request createGetTotalPointsRequest(Participant participant) {
        Request req = new Request();
        req.setType(RequestType.GET_TOTAL_POINTS_BY_ID);
        req.setParticipantDTO(DTOUtils.getDTO(participant));
        return req;
    }

    public static Request createGetRefereeByNameRequest(Referee referee) {
        Request req = new Request();
        req.setType(RequestType.GET_REFEREE_BY_NAME);
        req.setRefereeDTO(DTOUtils.getDTO(referee));
        return req;
    }

    public static Response createGetRefereeByNameResponse(Referee referee) {
        Response resp = new Response();
        resp.setResponseType(ResponseType.GET_REFEREE_BY_NAME);
        resp.setRefereeDTO(DTOUtils.getDTO(referee));
        return resp;
    }

    public static Request createGetAllResultsRequest() {
        Request req = new Request();
        req.setType(RequestType.GET_ALL_RESULTS);
        return req;
    }

    public static Response createGetAllResultsResponse(Result[] results) {
        Response resp = new Response();
        resp.setResponseType(ResponseType.GET_ALL_PARTICIPANTS);
        resp.setResultDTOS(DTOUtils.getDTO(results));
        return resp;
    }

    public static Request createGetParticipantBySsnRequest(Participant participant) {
        Request req = new Request();
        req.setType(RequestType.GET_PARTICIPANT_BY_SSN);
        req.setParticipantDTO(DTOUtils.getDTO(participant));
        return req;
    }

    public static Response createGetParticipantBySsnResponse(Participant participant) {
        Response resp = new Response();
        resp.setResponseType(ResponseType.GET_PARTICIPANT_BY_SSN);
        resp.setParticipantDTO(DTOUtils.getDTO(participant));
        return resp;
    }

    public static Request createAddResultRequest(Result result) {
        Request req = new Request();
        req.setType(RequestType.ADD_RESULTS);
        req.setResultDTO(DTOUtils.getDTO(result));
        return req;
    }

    public static Response createAddResultResponse() {
        Response resp = new Response();
        resp.setResponseType(ResponseType.ADD_RESULTS);
        return resp;
    }
}

