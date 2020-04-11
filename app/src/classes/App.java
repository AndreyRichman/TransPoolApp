package classes;

import interfaces.UIHandler;
import requests.classes.ExitRequest;
import requests.classes.LoadXMLRequest;
import requests.enums.RequestType;
import requests.interfaces.UserRequest;

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
        };
    }

    private void processRequest(UserRequest req){
        RequestType requestType = req.getRequestType();

        switch (requestType){
            case LOAD_XML_FILE:
                logicHandler.loadXMLFile((((LoadXMLRequest)req).getFileDirectory());
                break;
            case NEW_TREMP:
                addNewTremp(req);
                break;
            case NEW_RIDE:
                addNewRide(req);
                break;
            case GET_STATUS_OF_RIDES:

                break;
            case GET_STATUS_OF_TREMPS:

                break;
            case MATCH_TREMP_TO_RIDE:

                break;
            case EXIT:
                exitApp();
                break;
            case INVALID:

                break;
        }

    }

    private void addNewRide(UserRequest req) {

    }

    private void addNewTremp(UserRequest req) {

    }

    private void exitApp(){

        exit = true;
    }


//    private void addNewRide(NewRideRequest req){
//        Ride r = new Ride();
//
//        r.from = req.from;
//        r.to = req.to;
//        r.userName = req.name;
//
//        logic.addNewRide(r);
//    }
//    private void loadXMLFile(LoadXMLRequest request){
//        logic.loadXMLFile(request.fileDirectory);
//    }

//    //from, to
//    {
//        "from": "...",
//        "to": "..."
//    }
//    request.type = CREATE_NEW
//    //option2 -- dicitonary
//    from = dict.get("from");
//    to = dict.get("to");
//
//    //option3 -- class
//    obj.
//    from = obj.from;
//    to = obj.to;

//    private void showStatusOfAllRides(){
//        List<Ride> rides = logic.getAllRides();
//
//        uiHandler.showOutput(output);
//    }
}
