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
            String chosenRequestNumSTR = getInput();

            if (checkRequestIsValid(chosenRequestNumSTR)){
                requestIsValid = true;
                request = getRelevantRequest(chosenRequestNumSTR);
            }
            else {
                showErrorMessage();
            }
        }
        while (!requestIsValid);

        return request;
    }

    private void showErrorMessage(){
        //TODO: print some error message
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

    private boolean checkRequestIsValid(String chosenRequest){
        //TODO: validation - check that input is in kyes of Map of functions
        return true;
    }


    private UserRequest getRelevantRequest(String chosenRequest)
    {
        Map<String,Pair <RequestType, Supplier<UserRequest>>> stringNumToRequest = new HashMap<>();

        stringNumToRequest.put("1",(new Pair <> (LOAD_XML_FILE,this::getLoadXMLRequest)));
        stringNumToRequest.put("2",(new Pair <> (NEW_TREMP, this::getRequestForNewRide)));
        stringNumToRequest.put("3",(new Pair <> (GET_STATUS_OF_RIDES, this::getStatusOfRidesRequest)));
        stringNumToRequest.put("4",(new Pair <> (GET_STATUS_OF_TREMPS, this::getStatusOfTrempsRequest)));
        stringNumToRequest.put("5",(new Pair <> (MATCH_TREMP_TO_RIDE, this::getMatchTrempToRideRequest)));
        stringNumToRequest.put("6",(new Pair <> (EXIT, this::getExitRequest)));

        return stringNumToRequest.get(chosenRequest).getValue().get();
    }

    private ExitRequest getExitRequest(){
        return new ExitRequest();
    }
    private UserRequest getMatchTrempToRideRequest(){
        MatchTrempToRideRequest newRequest = new MatchTrempToRideRequest();
        int trempID = 1;  //TODO: get from User
        int rideID = 400; //TODO: get from User
        newRequest.setTrempRequestID(trempID);
        newRequest.setRideID(rideID);

        return newRequest;
    }
    private UserRequest getStatusOfTrempsRequest(){
        return new GetStatusOfTrempsRequest();
    }
    private UserRequest getStatusOfRidesRequest(){
        return new GetStatusOfRidesRequest();
    }

    private UserRequest getLoadXMLRequest(){
        String fileDirectory = "C://....";
        LoadXMLRequest newRequest = new LoadXMLRequest();
        newRequest.setFileDirectory(fileDirectory);

        return newRequest;
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
