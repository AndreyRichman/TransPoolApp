package classes;

import interfaces.UIHandler;
import requests.classes.ExitRequest;
import requests.classes.LoadXMLRequest;
import requests.classes.NewRideRequest;
import requests.enums.RequestType;
import requests.interfaces.UserRequest;

public class ConsoleUI implements UIHandler {
    @Override
    public void showOptions() {
    }

    @Override
    public String getInput() {

        return null;
    }

    @Override
    public void showOutput(String outputMsg) {

        System.out.println(outputMsg);
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
        }while (!requestIsValid);

        return request;

    }

    private UserRequest createRelevantRequest(RequestType reqType){
        UserRequest userRequest = null;

        switch (reqType){
            case LOAD_XML_FILE:
                userRequest = loadXMLFile();
                break;
            case NEW_TREMP:
                userRequest = getRequestForNewTremp();
                break;
            case NEW_RIDE:
                userRequest = getRequestForNewRide();
                break;
            case GET_STATUS_OF_RIDES:
                userRequest = getRequestForStatusOfRides();
                break;
            case GET_STATUS_OF_TREMPS:
                userRequest = getRequestForStatusOfTremps();
                break;
            case MATCH_TREMP_TO_RIDE:
                userRequest = getRequestForMatchTrempToRide();
                break;
            case EXIT:
                userRequest = getRequestForExit();
                break;
            case INVALID:
                userRequest = getInvalidReques();
                break;
        }

        return userRequest;
    }

    private UserRequest getInvalidReques() {

        return null;
    }

    private UserRequest loadXMLFile() {

        return null;
    }

    private UserRequest getRequestForMatchTrempToRide() {

        return null;
    }

    private UserRequest getRequestForStatusOfTremps() {

        return null;
    }

    private UserRequest getRequestForStatusOfRides() {

        return null;
    }

    private UserRequest getRequestForNewTremp() {

        return null;
    }

    private NewRideRequest getRequestForNewRide(){
        NewRideRequest req = new NewRideRequest();

        showOutput("From Which Station?");
        req.from = getInput();

        showOutput("To Which Station?");
        req.to = getInput();

        showOutput("What your name?");
        req.name = getInput();

        return req;
    }


    private RequestType getRequestType(String chosenRequest){

        return RequestType.valueOf(chosenRequest);
    }

    private UserRequest getRequestForExit(){

        return new ExitRequest();
    }


}
