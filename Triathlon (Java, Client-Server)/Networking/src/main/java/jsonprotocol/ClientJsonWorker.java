package jsonprotocol;


import Domain.Participant;
import Domain.Referee;
import Domain.Result;
import Interfaces.IObserver;
import Interfaces.IService;
import com.google.gson.*;
import dto.DTOUtils;
import dto.ParticipantDTO;
import dto.RefereeDTO;
import dto.ResultDTO;
import ro.mpp2024.ServicesException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ClientJsonWorker implements Runnable, IObserver {
    private IService server;
    private Socket connection;

    private BufferedReader input;
    private PrintWriter output;
    private Gson gsonFormatter;
    private volatile boolean connected;

    public ClientJsonWorker(IService server, Socket connection) {
        this.server = server;
        this.connection = connection;
        gsonFormatter = new GsonBuilder()
                .registerTypeAdapter(Date.class, new SqlDateAdapter())
                .create();
        try {
            output = new PrintWriter(connection.getOutputStream());
            input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class SqlDateAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {
        private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        @Override
        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(dateFormat.format(src));
        }

        @Override
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return new Date(dateFormat.parse(json.getAsString()).getTime());
            } catch (ParseException e) {
                try {
                    // Try parsing with an alternative date format
                    return new Date(new SimpleDateFormat("MMM d, yyyy").parse(json.getAsString()).getTime());
                } catch (ParseException ex) {
                    throw new JsonParseException("Unparseable date: " + json.getAsString(), ex);
                }
            }
        }
    }

    private static Response okResponse = JsonProtocolUtils.createOkResponse();

    private Response handleRequest(Request request) throws ServicesException {
        Response response = null;
        if (request.getType() == RequestType.LOGIN) {
            System.out.println("Login request ..." + request.getType());
            RefereeDTO refereeDTO = request.getRefereeDTO();
            Referee user = DTOUtils.getFromDTO(refereeDTO);
            try {
                server.login(user, this);
                return okResponse;
            } catch (ServicesException e) {
                connected = false;
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        if (request.getType() == RequestType.LOGOUT) {
            System.out.println("Logout request");

            RefereeDTO refereeDTO = request.getRefereeDTO();
            Referee user = DTOUtils.getFromDTO(refereeDTO);
            try {
                server.logout(user, this);
                connected = false;
                return okResponse;

            } catch (ServicesException e) {
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }

        if(request.getType() == RequestType.GET_ALL_PARTICIPANTS){
            System.out.println("GettingAllParticipants ...");
            try{
                Participant[] participants = server.getAllParticipants().toArray(new Participant[0]);
                response = JsonProtocolUtils.createAllParticipantsResponse(participants);
            }catch (ServicesException e){
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }

        if(request.getType() == RequestType.GET_TOTAL_POINTS_BY_ID){
            System.out.println("Getting total points by ID ...");
            ParticipantDTO participantDTO = request.getParticipantDTO();
            String ssn = participantDTO.getSsn();
            int points = server.getTotalPointsBySsn(ssn);
            response = JsonProtocolUtils.createGetTotalPointsResponse(points);
        }

        if(request.getType() == RequestType.GET_REFEREE_BY_NAME){
            System.out.println("Getting referee by Name ...");
            RefereeDTO refereeDTO = request.getRefereeDTO();
            Referee referee = server.getRefereeByName(refereeDTO.getName());
            response = JsonProtocolUtils.createGetRefereeByNameResponse(referee);
        }

        if(request.getType() == RequestType.GET_ALL_RESULTS){
            System.out.println("Getting all Results ...");
            try{
                Result[] results = server.getAllResults().toArray(new Result[0]);
                response = JsonProtocolUtils.createGetAllResultsResponse(results);
            }catch (ServicesException e){
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }

        if(request.getType() == RequestType.GET_PARTICIPANT_BY_SSN){
            System.out.println("Getting participant by SSN ...");
            ParticipantDTO participantDTO = request.getParticipantDTO();
            String ssn = participantDTO.getSsn();
            Participant participant = server.findBySSN(ssn);
            response = JsonProtocolUtils.createGetParticipantBySsnResponse(participant);
        }

        if(request.getType() == RequestType.ADD_RESULTS){
            System.out.println("Adding results ...");
            ResultDTO resultDTO = request.getResultDTO();
            server.addResult(DTOUtils.getFromDTO(resultDTO));
            response = JsonProtocolUtils.createAddResultResponse();
        }
        return response;
    }

    private void sendResponse(Response response) throws IOException {
        String responseLine = gsonFormatter.toJson(response);
        System.out.println("sending response " + responseLine);
        synchronized (output) {
            output.println(responseLine);
            output.flush();
        }
    }

    public void run() {
        while (connected) {
            try {
                String requestLine = input.readLine();
                Request request = gsonFormatter.fromJson(requestLine, Request.class);
                Response response = handleRequest(request);
                if (response != null) {
                    sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ServicesException e) {
                throw new RuntimeException(e);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error " + e);
        }
    }
}
