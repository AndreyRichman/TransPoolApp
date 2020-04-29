package classes;

import exception.*;
import interfaces.UIHandler;
import requests.classes.*;
import requests.enums.RequestType;
import requests.interfaces.UserRequest;

import java.time.LocalTime;
import java.util.List;
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
                tryMatchTrempToRide((TryMatchTrempToRideRequest) request);
                break;
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

    private void showStatusOfRides(GetStatusOfRidesRequest request) {
        String summaryOfRides = createSummaryOfAllRides();
        uiHandler.showOutput(summaryOfRides);
    }

    private String createSummaryOfAllRides() {
        StringBuilder out = new StringBuilder("Summary of all Rides in the system:" + System.lineSeparator());
        for(Ride ride: logicHandler.getAllRides()){

            String rideDescription = createDescriptionOfRide(ride);
            out.append(rideDescription);
        }
        return out.toString();
    }

    private String createDescriptionOfRide(Ride ride){
        //TODO: add all relevant information
        return String.join(System.lineSeparator(),
                String.format("Ride ID: %d", ride.getID()),
                String.format("Stations: %s", ride.getAllStations()
                        .stream()
                        .map(Station::getName)
                        .collect(Collectors.joining(" -> "))
                )
        );
    }
    private void showStatusOfTremps(GetStatusOfTrempsRequest request) {
        List<TrempRequest> allTrempRequests = logicHandler.getAllTrempRequests();
        List<String> summaryOfAllTremps = getSummaryOfAllTrempRequests(allTrempRequests);
        uiHandler.showOutput(String.join(System.lineSeparator(), summaryOfAllTremps));
    }

    private List<String> getSummaryOfAllTrempRequests(List<TrempRequest> allTrempRequests){
        return allTrempRequests.stream().map(this::createDescriptionOfTrempRequest).collect(Collectors.toList());
    }

    private void tryMatchTrempToRide(TryMatchTrempToRideRequest request) {
        TrempRequest chosenTrempRequest = null;
        try {
            chosenTrempRequest = getDesiredTrempRequestForMatching();
            List<Ride.SubRide> chosenRides = getDesiredRidesForTrempRequest(chosenTrempRequest);
            connectTrempRequestToSubRides(chosenTrempRequest, chosenRides);
            uiHandler.showOutput("Tremp request was assigned to Ride succesfully!");
        }
        catch (NotFoundTrempRequestsWithoutMatchException e) {
            uiHandler.showOutput("Not found any Tremp requests without a match!");

        } catch (NoRidesWereFoundForTrempRequestException e) {
            uiHandler.showOutput("No availble Rides for the selected tremp request");
            boolean tryAgain = uiHandler.getYesNoAnswerForQuestion("Would you like to select a different Tremp Request?");
            if (tryAgain)
                tryMatchTrempToRide(request);
        }


    }

    private TrempRequest getDesiredTrempRequestForMatching() throws NotFoundTrempRequestsWithoutMatchException{
        List<TrempRequest> trempRequests = logicHandler.getAllNonMatchedTrempRequests();
        if (trempRequests == null || trempRequests.size() == 0)
            throw new NotFoundTrempRequestsWithoutMatchException();

        List<String> trempRequestsStrOptions = getAllUnMatchedTrempRequestStrOptions(trempRequests);
        String title = "The following Tremp Requests are not assigned to any rides, select one to match a ride to it:";
        int chosenTrempRequest = uiHandler.showOptionsAndGetUserSelection(title, trempRequestsStrOptions);

        return trempRequests.get(chosenTrempRequest);
    }

    private List<Ride.SubRide> getDesiredRidesForTrempRequest(TrempRequest trempRequest) throws NoRidesWereFoundForTrempRequestException{

        List<List<Ride.SubRide>> allTremps = logicHandler.getAllPossibleTrempsForTrempRequest(trempRequest);
        if (allTremps == null || allTremps.size() == 0)
            throw new NoRidesWereFoundForTrempRequestException();

        List<String> availableRidesOptions  = createSummaryOfAllTrempOptions(allTremps);
        String title = "The following Rides are available for the Selected Tremp Request:";
        int desiredRidesOption = uiHandler.showOptionsAndGetUserSelection(title, availableRidesOptions);

        return allTremps.get(desiredRidesOption);
    }

    private List<String> createSummaryOfAllTrempOptions(List<List<Ride.SubRide>> trempOptions){
        return trempOptions.stream().map(this::createDescriptionOfTrempOption).collect(Collectors.toList());
    }

    private String createDescriptionOfTrempOption(List<Ride.SubRide> trempOption){
        StringBuilder out = new StringBuilder();
        out.append(String.join(System.lineSeparator(),
                String.format("Total Connections: %d", trempOption.size() - 1)
                )
        );
        for (Ride.SubRide subRide : trempOption) {
            out.append(getSubRideSummary(subRide));
            out.append('-' * 20);
        }
        return out.toString();
    }

    private String getSubRideSummary(Ride.SubRide subRide){
        StringBuilder out = new StringBuilder();
        subRide.selectedPartsOfRide.forEach(part ->
           out.append(String.format("%s -> %s", part.getRoad().getStartStation().getName(), part.getRoad().getEndStation().getName()))
        );

        return out.toString();
    }
    private void connectTrempRequestToSubRides(TrempRequest trempRequest, List<Ride.SubRide> subRides){
        subRides.forEach(subRide -> {
            subRide.applyTrempistToAllPartsOfRide(trempRequest.getUser());
            trempRequest.addSubRide(subRide);
        });
    }

    private List<String> getAllUnMatchedTrempRequestStrOptions(List<TrempRequest> trempRequests) {
        return trempRequests.stream().map(this::createDescriptionOfTrempRequest).collect(Collectors.toList());
    }

    private String createDescriptionOfTrempRequest(TrempRequest trempRequest){
        //TODO: add all relevant information
        return String.join(System.lineSeparator(),
                String.format("Tremp ID: %d", trempRequest.getID()),
                String.format("Stations: %s --> %s", trempRequest.getStartStation().getName(), trempRequest.getEndStation().getName())
        );
    }

    private void exitApp(){
        exit = true;
    }
}
