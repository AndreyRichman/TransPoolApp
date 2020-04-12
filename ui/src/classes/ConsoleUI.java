package classes;

import interfaces.UIHandler;
import requests.classes.*;
import requests.enums.RequestType;
import requests.interfaces.UserRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
            }
        }
        while (!requestIsValid);

        return request;
    }

    @Override
    public void showOptions() {
        String optionsScreen =
                "1. Load XML File " +
                "2. Ask for new tremp " +
                "3. Show status of all avaible riders " +
                "4. Show status of all avaible temp requsts" +
                "5. Find a match " +
                "6. Exit";
    }

    @Override
    public String getInput() {
        String input = this.inputReader.nextLine();
        return input;
    }

    @Override
    public void showOutput(String outputMsg) {

        System.out.println(outputMsg);
    }

    private RequestType getRequestType(String chosenRequest){

        return RequestType.values()[Integer.parseInt(chosenRequest)];
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
