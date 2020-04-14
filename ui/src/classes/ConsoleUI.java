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
import java.util.stream.Stream;

import static requests.enums.RequestType.*;

public class ConsoleUI implements UIHandler {

    Scanner inputReader;
    Map<String,Pair <String, Supplier<UserRequest>>> mapRequestToInt;

    public ConsoleUI() {
        this.inputReader = new Scanner(System.in);
        mapRequestToInt = getEnumMap();
    }

    @Override
    public UserRequest getRequestFromUser(){
        UserRequest request = null;
        boolean requestIsValid = false;

        do {
            showOptions();
            String chosenAction = getInput();
            requestIsValid = checkRequestIsValid(chosenAction);
            if (requestIsValid) {
                request = getRelevantRequest(chosenAction);
            }
        }
        while (!requestIsValid);

        return request;
    }

    @Override
    public void showOptions() {
        mapRequestToInt.forEach((k,v) -> System.out.println(v.getKey()));
    }

    @Override
    public String getInput() {
        return this.inputReader.nextLine();
    }

    @Override
    public void showOutput(String outputMsg) {
        System.out.println(outputMsg);
    }

    private boolean checkRequestIsValid(String chosenRequest) {
        return mapRequestToInt.containsKey(chosenRequest);
    }

    private  Map<String,Pair <String, Supplier<UserRequest>>> getEnumMap(){

        Map<String,Pair <String, Supplier<UserRequest>>> mapRequestToInt = new HashMap<>();

        mapRequestToInt.put("1",(new Pair <> ("1. Load XML File ",LoadXMLRequest::new)));
        mapRequestToInt.put("2",(new Pair <> ("2. Ask for new tremp ", this::getRequestForNewTremp)));
        mapRequestToInt.put("3",(new Pair <> ("3. Show status of all avaible riders ", GetStatusOfRidesRequest::new)));
        mapRequestToInt.put("4",(new Pair <> ("4. Show status of all avaible temp requsts", GetStatusOfTrempsRequest::new)));
        mapRequestToInt.put("5",(new Pair <> ("5. Find a match ", MatchTrempToRideRequest::new)));
        mapRequestToInt.put("6",(new Pair <> ("6. Exit", ExitRequest::new)));

        return mapRequestToInt;
    }

    private UserRequest getRelevantRequest(String chosenRequest)
    {
        return mapRequestToInt.get(chosenRequest).getValue().get();
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
