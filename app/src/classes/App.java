package classes;

import interfaces.UIHandler;
import requests.classes.ExitRequest;
import requests.classes.LoadXMLRequest;
import requests.enums.RequestType;
import requests.interfaces.UserRequest;

import java.util.ArrayList;

public class App {
    UIHandler uiHandler;
    LogicHandler logicHandler;
    Boolean exit;

    public App() {
        uiHandler = new ConsoleUI();
        exit = false;

        uiHandler.showOutput("App created successfully!");
    }
    public void run(){
        uiHandler.showOutput("App is Running!");
        while(!exit) {
            UserRequest req = uiHandler.getRequestFromUser();
            processRequest(req);
        }
    }

    private void processRequest(UserRequest req){
        RequestType requestType = req.getRequestType();

        switch (requestType){
            case LOAD_XML_FILE:
                logicHandler.loadXMLFile((((LoadXMLRequest)req).getFileDirectory()));
                break;
            case NEW_TREMP:
                addNewTremp(req);
                break;
            case NEW_RIDE:
                addNewRide(req);
                break;
            case GET_STATUS_OF_RIDES:
                getStatusOfRides();
                break;
            case GET_STATUS_OF_TREMPS:
                getStatusOfTremps();
                break;
            case MATCH_TREMP_TO_RIDE:
                MatchTrempToRIde();
                break;
            case EXIT:
                exitApp();
                break;
        }

    }

    private void MatchTrempToRIde() {

    }

    private ArrayList<Ride> getStatusOfRides() {

        return null;
    }

    private ArrayList<Tremp> getStatusOfTremps() {

        return null;
    }

    private void addNewRide(UserRequest req) {

    }

    private void addNewTremp(UserRequest req) {
        Tremp newTrempist = new Tremp();
        newTrempist.
        //TODO: Need to display to user the avaible rides
    }

    private void exitApp(){

        exit = true;
    }
}
