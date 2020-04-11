package classes;

import interfaces.UIHandler;
import requests.classes.ExitRequest;
import requests.classes.LoadXMLRequest;
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
            case EXIT:
                userRequest = getRequestForExit();
                break;
        }

        return userRequest;
    }

    private RequestType getRequestType(String chosenRequest){
        //TODO: add function 'TryParse(string)' to RequestType class
        return RequestType.NEW_RIDE;
    }
    private UserRequest getRequestForExit(){
        return new ExitRequest();
    }
//
//    private NewRideRequest getRequestForNewRide(){
//        NewRideRequest req = new NewRideRequest();
//        showOutput("From Which Station?");
//        req.from = getInput();
//        showOutput("To Which Station?");
//        req.to = getInput();
//        showOutput("What your name?");
//        req.name = getInput();
//

//        return req;

//    }
}
