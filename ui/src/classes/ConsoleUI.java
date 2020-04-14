package classes;

import com.sun.javafx.collections.MappingChange;
import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
import interfaces.UIHandler;
import javafx.util.Pair;
import requests.classes.*;
import requests.enums.RequestType;
import requests.interfaces.UserRequest;

import java.util.*;
import java.util.function.Supplier;

import static requests.enums.RequestType.*;

public class ConsoleUI implements UIHandler {

    Scanner inputReader;

    public ConsoleUI() {
        this.inputReader = new Scanner(System.in);

    }

    @Override
    public UserRequest getRequestFromUser(){
        UserRequest request = null;
        boolean requestIsValid = false;

        do {
            showOptions();
            String chosenAction = getInput();
            RequestType requestType = getRequestType(chosenAction);
            if (requestType != RequestType.INVALID) {
                requestIsValid = true;
                //request = createRelevantRequest(requestType);
                request = getRelevantRequest(chosenAction);
            }
        }
        while (!requestIsValid);

        return request;
    }

    @Override
    public void showOptions() {
        String optionsScreen =
                "1. Load XML File " + System.lineSeparator() +
                "2. Ask for new tremp " + System.lineSeparator() +
                "3. Show status of all avaible riders " + System.lineSeparator() +
                "4. Show status of all avaible temp requsts" + System.lineSeparator() +
                "5. Find a match " + System.lineSeparator() +
                "6. Exit";

        showOutput(optionsScreen);
    }

    @Override
    public String getInput() {
        return this.inputReader.nextLine();
    }

    @Override
    public void showOutput(String outputMsg) {

        System.out.println(outputMsg);
    }

    private RequestType getRequestType(String chosenRequest){

        return RequestType.values()[Integer.parseInt(chosenRequest)];
    }


    private UserRequest getRelevantRequest(String chosenRequest)
    {
        Map<String,Pair <RequestType, Supplier<UserRequest>>> mapRequestToInt = new HashMap<>();

        mapRequestToInt.put("1",(new Pair <RequestType,Supplier<UserRequest>> (LOAD_XML_FILE,LoadXMLRequest::new)));
        mapRequestToInt.put("2",(new Pair <RequestType,Supplier<UserRequest>> (NEW_TREMP, (Supplier<UserRequest>)getRequestForNewTremp())));
        mapRequestToInt.put("3",(new Pair <RequestType,Supplier<UserRequest>> (GET_STATUS_OF_RIDES, GetStatusOfRidesRequest::new)));
        mapRequestToInt.put("4",(new Pair <RequestType,Supplier<UserRequest>> (GET_STATUS_OF_TREMPS, GetStatusOfTrempsRequest::new)));
        mapRequestToInt.put("5",(new Pair <RequestType,Supplier<UserRequest>> (MATCH_TREMP_TO_RIDE, MatchTrempToRideRequest::new)));
        mapRequestToInt.put("6",(new Pair <RequestType,Supplier<UserRequest>> (EXIT, ExitRequest::new)));

        return mapRequestToInt.get(chosenRequest).getValue().get();
    }

    private UserRequest createRelevantRequest(RequestType reqType){

        UserRequest userRequest = null;

        switch (reqType){
            case LOAD_XML_FILE:
                userRequest = new LoadXMLRequest();
                break;
            case NEW_TREMP:
                userRequest = getRequestForNewTremp();
                break;
            case NEW_RIDE:
                userRequest = getRequestForNewRide();
                break;
            case GET_STATUS_OF_RIDES:
                userRequest = new GetStatusOfRidesRequest();
                break;
            case GET_STATUS_OF_TREMPS:
                userRequest = new GetStatusOfTrempsRequest();
                break;
            case MATCH_TREMP_TO_RIDE:
                userRequest = new MatchTrempToRideRequest();
                break;
            case EXIT:
                userRequest = new ExitRequest();
                break;
        }

        return userRequest;
    }

    private UserRequest getRequestForNewTremp() {
        NewTrempRequest req = new NewTrempRequest();

        showOutput("What your name?");
        String userName = getInput();
        req.setUserName(userName);

        showOutput("what is your origen Station?");
        String stationFrom = getInput();
        req.setFromStation(stationFrom);

        showOutput("What is your destanation Station?");
        String stationTo = getInput();
        req.setToStation(stationTo);

        showOutput("departure time?");
        String departureTime = getInput();
        req.setDepartTime(departureTime);

        return req;
    }

    private NewRideRequest getRequestForNewRide(){

        return new NewRideRequest();
    }
}
