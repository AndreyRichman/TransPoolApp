package classes;

import exception.*;
import interfaces.UIHandler;
import requests.classes.*;
import requests.enums.RequestType;
import requests.interfaces.UserRequest;

import java.time.LocalTime;
import java.util.stream.Collectors;

public class App {
    UIHandler uiHandler;
    LogicHandler logicHandler;
    Boolean exit;

    public App() {
        uiHandler = new ConsoleUI();
        logicHandler = new LogicHandler();

        exit = false;

    }

    public void run(){
        while(!exit) {
            UserRequest req = uiHandler.getRequestFromUser();
            processRequest(req);
        }
    }

    private void processRequest(UserRequest request){
        RequestType requestType = request.getRequestType();

        switch (requestType){
            case LOAD_XML_FILE:
                loadContentFromXMLFile((LoadXMLRequest)request);
                break;
            case NEW_TREMP:
                addNewTrempRequest((NewTrempRequest) request);
                break;
            case NEW_RIDE:
                addNewRide((NewRideRequest) request);
                break;
            case GET_STATUS_OF_RIDES:
                showStatusOfRides((GetStatusOfRidesRequest) request);
                break;
            case GET_STATUS_OF_TREMPS:
                showStatusOfTremps((GetStatusOfTrempsRequest) request);
                break;
            case MATCH_TREMP_TO_RIDE:
                MatchTrempToRIde((MatchTrempToRideRequest) request);
                break;
            case SHOW_UN_MATCHED_TREMP_REQUESTS:
                showAllUnMatchedTrempRequests((ShowUnMatchedTrempReqRequest) request);
                break;
            case GET_POSSIBLE_RIDES_FOR_TREMP_REQUEST:
                showPossibleRidesForTrempRequest((GetPossibleRidesForTrempRequest) request);
            case EXIT:
                exitApp();
                break;
        }

    }

    private void loadContentFromXMLFile(LoadXMLRequest request){
        //TODO: catch errors here
        String directory = request.getFileDirectory();
        logicHandler.loadXMLFile(directory);

    }

    private void addNewTrempRequest(NewTrempRequest request) {
        //TODO: add any other fields from user
        //TODO: Need to display to user the statoins in UI?
        try {
            Station from = logicHandler.getStationFromName(request.getFromStation());
            Station to = logicHandler.getStationFromName(request.getToStation());
            TrempRequest newTrempRequest = logicHandler.createNewEmptyTrempRequest(from, to);

            LocalTime departTime = LocalTime.parse(request.getDepartTime());
            newTrempRequest.setDepartTime(departTime);

            User user = logicHandler.getUserByName(request.getUserName());
            newTrempRequest.setUser(user);

            logicHandler.addTrempRequest(newTrempRequest);

        } catch (StationNotFoundException e) {
            String errorMsg = "Station not found: " + e.getStationName();
            uiHandler.showOutput(errorMsg);
        } catch (NoPathExistBetweenStationsException e){
          String errorMsg = "No path found between " + request.getFromStation() + " and " + request.getToStation();
            uiHandler.showOutput(errorMsg);
        }

    }

    private void addNewRide(NewRideRequest request) {
        //TODO leave for later stage
    }


    private void MatchTrempToRIde(MatchTrempToRideRequest request) {
        int trempRequestID = request.getTrempRequestID();
        int rideID = request.getRideID();
        try{
            Ride ride = logicHandler.getRideById(rideID);
            TrempRequest trempRequest = logicHandler.getTrempRequestById(trempRequestID);

            ride.assignTrempRequest(trempRequest);
        }
        catch (RideNotExistsException e){}
        catch (TrempRequestNotExist e) {}
        catch (RideNotContainsRouteException e) {}

    }


    private void showStatusOfRides(GetStatusOfRidesRequest request) {
        String summaryOfRides = createSummaryOfAllRides();
        uiHandler.showOutput(summaryOfRides);
    }

    private String createSummaryOfAllRides() {
        //TODO: add all relevant information
        StringBuilder out = new StringBuilder("Summary of all Rides in the system:" + System.lineSeparator());
        for(Ride ride: logicHandler.getAllRides()){
            String rideSummary = String.join(System.lineSeparator(),
                    String.format("Ride ID: %d", ride.getID()),
                    String.format("Stations: %s", ride.getAllStations()
                            .stream()
                            .map(Station::getName)
                            .collect(Collectors.joining(" -> ")), System.lineSeparator()
                    )
                    //String.format("Ride price: %d", ride.getTotalCostOfRide())
                    //String.format("Ride departure time: %d", ride.getDeptTime()),
                    //String.format("Ride arrival time: %d", ride.getArrTime()),
                    //String.format("Ride trempists capacity: %d", )
            );
            out.append(rideSummary);

        }
        return out.toString();
    }
    private void showStatusOfTremps(GetStatusOfTrempsRequest request) {
        String summaryOfAllTremps = getSummaryOfAllTrempRequests();
        uiHandler.showOutput(summaryOfAllTremps);
    }

    private String getSummaryOfAllTrempRequests(){
        //TODO: add all relevant information
        StringBuilder out = new StringBuilder("Summary of all Tremp Requests in the system:" + System.lineSeparator());
        for(TrempRequest tremp: logicHandler.getAllTrempRequests()){
            String trempSummary = String.join(System.lineSeparator(),

                    String.format("Tremp ID: %d", tremp.getID()),
                    String.format("Stations: %s --> %s", tremp.getStartStation().getName(), tremp.getEndStation().getName())
            );

            out.append(trempSummary);
        }
        return out.toString();
    }

    private void showPossibleRidesForTrempRequest(GetPossibleRidesForTrempRequest request) {
    }

    private void showAllUnMatchedTrempRequests(ShowUnMatchedTrempReqRequest request) {
        StringBuilder out = new StringBuilder("All Non-Matched Rides:" + System.lineSeparator());

        for(TrempRequest tremp: logicHandler.getAllNonMatchedTrempRequests()){
            String trempSummary = String.join(System.lineSeparator(),

                    String.format("Tremp ID: %d", tremp.getID()),
                    String.format("Stations: %s --> %s", tremp.getStartStation().getName(), tremp.getEndStation().getName())
            );

            out.append(trempSummary);
        }
        uiHandler.showOutput(out.toString());   //TODO: send limited options to UI
    }

    private void exitApp(){
        exit = true;
    }
}
