package classes;

import interfaces.UIHandler;
import requests.classes.*;
import requests.enums.RequestType;
import requests.interfaces.UserRequest;
import java.util.*;

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
               request = createRelevantRequest(requestType);
               //request = createRelevantRequestViaMapDIrecttly(chosenAction);
               showOutput(request);
          }
        }
        while (!requestIsValid);

        return request;
    }
/*
    private UserRequest createRelevantRequestViaMapDIrecttly(String chosenAction) {

        Map<String, UserRequest> reqType = new HashMap<>();

        reqType.put("1", new LoadXMLRequest());
        reqType.put("2", getRequestForNewRide());
        reqType.put("3", GetStatusOfRidesRequest::new);
        reqType.put("4", GetStatusOfTrempsRequest::new);
        reqType.put("5", MatchTrempToRideRequest::new);
        reqType.put("6", ExitRequest::new);

        return reqType.get(chosenAction);
    }
*/
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
    public void showOutput(Object outputMsg) {
        System.out.println(outputMsg);
    }

    private Map<String, RequestType> createMapRequest()
    {
        Map<String, RequestType> reqType = new HashMap<>();
        reqType.put("1",LOAD_XML_FILE);
        reqType.put("2",NEW_TREMP);
        reqType.put("3",GET_STATUS_OF_RIDES);
        reqType.put("4",GET_STATUS_OF_TREMPS);
        reqType.put("5",MATCH_TREMP_TO_RIDE);
        reqType.put("6",EXIT);

        return reqType;
    }

    private RequestType getRequestType(String chosenRequest){

        Map<String, RequestType> reqType = createMapRequest();

        return reqType.get(chosenRequest);
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
