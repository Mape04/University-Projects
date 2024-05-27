package jsonprotocol;

import Interfaces.IObserver;
import Interfaces.IService;
import Domain.Challenge;
import Domain.Participant;
import Domain.Referee;
import Domain.Result;
import com.google.gson.*;
import dto.*;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

public class ServicesJsonProxy implements IService {
    private String host;
    private int port;
    private Gson gsonFormatter;
    private Socket connection;
    private BufferedReader input;
    private PrintWriter output;
    private IObserver client;
    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public ServicesJsonProxy(String host, int port) {
        this.host = host;
        this.port = port;
        gsonFormatter = new Gson();
//        gsonFormatter = new GsonBuilder()
//                .registerTypeAdapter(RequestType.class, new EnumOrdinalTypeAdapter<>(RequestType.class))
//                .registerTypeAdapter(java.sql.Date.class, new SqlDateAdapter())
//                .create();
        qresponses = new LinkedBlockingQueue<>();
    }

    public class SqlDateAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {
        private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        @Override
        public JsonElement serialize(Date src, Type typeOfSrc, com.google.gson.JsonSerializationContext context) {
            return new JsonPrimitive(formatter.format(src));
        }

        @Override
        public Date deserialize(JsonElement json, Type typeOfT, com.google.gson.JsonDeserializationContext context) throws JsonParseException {
            try {
                return new Date(formatter.parse(json.getAsString()).getTime());
            } catch (ParseException e) {
                throw new JsonParseException(e);
            }
        }
    }

    public class EnumOrdinalTypeAdapter<E extends Enum<E>> implements JsonSerializer<E> {

        private final Class<E> eClass;

        public EnumOrdinalTypeAdapter(Class<E> eClass) {
            this.eClass = eClass;
        }

        @Override
        public JsonElement serialize(E src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.ordinal());
        }
    }

    private void initializeConnection() throws ServicesException {
        try {
            //gsonFormatter = new Gson();
            connection = new Socket(host, port);
            output = new PrintWriter(connection.getOutputStream());
            output.flush();
            input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            finished = false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection() {
        finished = true;
        try {
            input.close();
            output.close();
            connection.close();
            client = null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void handleUpdate(Response response) {

    }

    private boolean isUpdate(Response response) {
        return false;
    }

    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    String responseLine = input.readLine();
                    System.out.println("response received " + responseLine);
                    Response response = gsonFormatter.fromJson(responseLine, Response.class);
                    if (isUpdate(response)) {
                        handleUpdate(response);
                    } else {

                        try {
                            qresponses.put(response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error " + e);
                }
            }
        }
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }

    private void sendRequest(Request request) throws ServicesException {
        String reqLine = gsonFormatter.toJson(request);
        try {
            output.println(reqLine);
            output.flush();
        } catch (Exception e) {
            throw new ServicesException("Error sending object " + e);
        }

    }

    private Response readResponse(){
        Response response = null;
        try {

            response = qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }


    public void login(Referee referee, IObserver client) throws ServicesException {
        initializeConnection();

        Request req = JsonProtocolUtils.createLoginRequest(referee);
        sendRequest(req);
        Response response = readResponse();
        if (response.getResponseType() == ResponseType.OK) {
            this.client = client;
            return;
        }
        if (response.getResponseType() == ResponseType.ERROR) {
            String err = response.getErrorMessage();
            ;
            closeConnection();
            throw new ServicesException(err);
        }
    }

    @Override
    public void logout(Referee referee, IObserver client) throws ServicesException {
        Request req = JsonProtocolUtils.createLogoutRequest(referee);
        sendRequest(req);
        Response response = readResponse();
        closeConnection();
        if (response.getResponseType() == ResponseType.ERROR) {
            String err = response.getErrorMessage();//data().toString();
            throw new ServicesException(err);
        }
    }

    @Override
    public void addChallenge(Challenge challenge) {
    }

    @Override
    public Collection<Challenge> getAllChallenges() {
        return null;
    }

    @Override
    public Challenge findByName(String name) {
        return null;
    }

    @Override
    public Collection<Challenge> getChallengesByName(String name) {
        return null;
    }

    @Override
    public int getTotalPointsById(Integer id) {
        return 0;
    }

    @Override
    public void addParticipant(String name, String ssn) {

    }

    @Override
    public void deleteParticipant(int id) {

    }

    @Override
    public void updateParticipant(int id, String newName, String newSSN) {

    }

    @Override
    public int getTotalPointsBySsn(String ssn) throws ServicesException {
        int totalPoints = 0;
        Request request = JsonProtocolUtils.createGetTotalPointsRequest(new Participant(null, ssn));
        sendRequest(request);

        Response response = readResponse();

        if (response.getResponseType() == ResponseType.ERROR) {
            String errorMessage = response.getErrorMessage();
            throw new ServicesException(errorMessage); // Throw exception if response indicates error
        }

        totalPoints = response.getResultDTO().getPoints();

        return totalPoints;
    }

    @Override
    public Collection<Participant> getAllParticipants() throws ServicesException {
        Collection<Participant> participants = new ArrayList<>(); // Initialize a collection to hold participants

        Request request = JsonProtocolUtils.createAllParticipantsRequest(); // Create request to fetch all participants
        sendRequest(request); // Send the request to the server

        Response response = readResponse(); // Read the response from the server

        if (response.getResponseType() == ResponseType.ERROR) {
            String errorMessage = response.getErrorMessage();
            throw new ServicesException(errorMessage); // Throw exception if response indicates error
        }

        ParticipantDTO[] participantDTOs = response.getParticipantDTOS(); // Get participant DTOs from response
        participants = List.of(DTOUtils.getFromDTO(participantDTOs)); // Convert DTOs to Participant objects

        return participants; // Return the collection of participants
    }

    @Override
    public Collection<Result> getAllResults() throws ServicesException {
        Collection<Result> results = new ArrayList<>();
        Request request = JsonProtocolUtils.createGetAllResultsRequest();
        sendRequest(request);

        Response response = readResponse();

        if (response.getResponseType() == ResponseType.ERROR) {
            String errorMessage = response.getErrorMessage();
            throw new ServicesException(errorMessage); // Throw exception if response indicates error
        }

        ResultDTO[] resultDTOS = response.getResultDTOS();
        results = List.of(DTOUtils.getFromDTO(resultDTOS));

        return results;
    }



    @Override
    public Referee getRefereeByName(String name) throws ServicesException {
        Referee referee = new Referee(name, null, null);
        referee.setId(-1);

        Request request = JsonProtocolUtils.createGetRefereeByNameRequest(referee);
        sendRequest(request);

        Response response = readResponse();

        if (response.getResponseType() == ResponseType.ERROR) {
            String errorMessage = response.getErrorMessage();
            throw new ServicesException(errorMessage); // Throw exception if response indicates error
        }

        referee = DTOUtils.getFromDTO(response.getRefereeDTO());
        return referee;
    }

    @Override
    public Participant findBySSN(String ssn) throws ServicesException {
        Participant participant = new Participant(null, ssn);
        participant.setId(-1);
        Request request = JsonProtocolUtils.createGetParticipantBySsnRequest(participant);
        sendRequest(request);

        Response response = readResponse();

        if (response.getResponseType() == ResponseType.ERROR) {
            String errorMessage = response.getErrorMessage();
            throw new ServicesException(errorMessage); // Throw exception if response indicates error
        }

        participant = DTOUtils.getFromDTO(response.getParticipantDTO());
        return participant;

    }

    @Override
    public void addResult(Result result) throws ServicesException {
        Request request = JsonProtocolUtils.createAddResultRequest(result);
        sendRequest(request);

        Response response = readResponse();

        if (response.getResponseType() == ResponseType.ERROR) {
            String errorMessage = response.getErrorMessage();
            throw new ServicesException(errorMessage); // Throw exception if response indicates error
        }


    }

    @Override
    public Result getResult(int participantId, int challengeId) {
        return null;
    }


    @Override
    public boolean hasResult(Participant participant, Challenge challenge) {
        return false;
    }


}
