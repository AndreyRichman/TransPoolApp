package classes;

import exception.*;
import interfaces.UIHandler;
import requests.classes.*;
import requests.enums.RequestType;
import requests.interfaces.UserRequest;

import javax.management.InstanceAlreadyExistsException;
import javax.xml.bind.JAXBException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static requests.enums.RequestType.LOAD_XML_FILE;

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
        boolean xmlLoadedCond = false;
        boolean treampsAddedCond = false;

        while(!exit) {
            UserRequest req = uiHandler.getRequestFromUser();
            processRequest(req, xmlLoadedCond, treampsAddedCond);
        }
    }

    private void processRequest(UserRequest request, boolean xmlLoadedCond, boolean treampsAddedCond){
        RequestType requestType = request.getRequestType();

        switch (requestType){
            case LOAD_XML_FILE:
                if(!xmlLoadedCond){
                    loadContentFromXMLFile((LoadXMLRequest)request);
                    xmlLoadedCond = true;
                }
                else {
                    String errorMsg = "XML has already loaded to system";
                    uiHandler.showErrorMsg(errorMsg);
                }
                break;
            case NEW_TREMP:
                if (xmlLoadedCond){
                    addNewTrempRequest(uiHandler.getRequestForNewTremp((NewTrempRequest) request));
                    treampsAddedCond = true;
                }
                else {
                    String errorMsg = "XML is NOT loaded to system yet";
                    uiHandler.showErrorMsg(errorMsg);
                }
                break;
            case NEW_RIDE:
                if(xmlLoadedCond)
                    addNewRide((NewRideRequest) request);
                else {
                    String errorMsg = "XML is NOT loaded to system yet";
                    uiHandler.showErrorMsg(errorMsg);
                }
                break;
            case GET_STATUS_OF_RIDES:
                if(xmlLoadedCond)
                    showStatusOfRides((GetStatusOfRidesRequest) request);
                else {
                    String errorMsg = "XML is NOT loaded to system yet";
                    uiHandler.showErrorMsg(errorMsg);
                }
                break;
            case GET_STATUS_OF_TREMPS:
                if (xmlLoadedCond && treampsAddedCond)
                    showStatusOfTremps((GetStatusOfTrempsRequest) request);
                else{
                    if(!xmlLoadedCond){
                        String errorMsg = "XML is NOT loaded to system yet";
                        uiHandler.showErrorMsg(errorMsg);
                    }
                    if(!treampsAddedCond){
                        String errorMsg = "No tremps in the system yet";
                        uiHandler.showErrorMsg(errorMsg);
                    }
                }
                break;
            case MATCH_TREMP_TO_RIDE:
                if (xmlLoadedCond && treampsAddedCond)
                    tryMatchTrempToRide((TryMatchTrempToRideRequest) request);
                else{
                    if(!xmlLoadedCond){
                        String errorMsg = "XML is NOT loaded to system yet";
                        uiHandler.showErrorMsg(errorMsg);
                    }
                    if(!treampsAddedCond){
                        String errorMsg = "No tremps in the system yet";
                        uiHandler.showErrorMsg(errorMsg);
                    }
                }
                break;
            case EXIT:
                exitApp();
                break;
        }

    }

    private void loadContentFromXMLFile(LoadXMLRequest request){

        try {
            logicHandler.loadXMLFile(request.getFileDirectory());
        } catch (JAXBException e) {
            String errorMsg = "Failed to read XML file";
            uiHandler.showErrorMsg(errorMsg);
        } catch (InvalidFileTypeException e) {
            String errorMsg = "File type is " + e.getFileType() + " and not .xml type" ;
            uiHandler.showErrorMsg(errorMsg);
        } catch (NoFileFoundInPathException e) {
            String errorMsg = "Cant file type in path";
            uiHandler.showErrorMsg(errorMsg);
        } catch (InvalidMapBoundariesException e) {
            String errorMsg = "Invalid map boundaries: " + e.getWidth() + "," + e.getLength();
            uiHandler.showErrorMsg(errorMsg);
        } catch (StationNameAlreadyExistsException e) {
            String errorMsg = "Two stations with the same name";
            uiHandler.showErrorMsg(errorMsg);
        } catch (InstanceAlreadyExistsException e) {
            String errorMsg = "?";
            uiHandler.showErrorMsg(errorMsg);
        } catch (StationCoordinateoutOfBoundriesException e) {
            String errorMsg = "Station Coordinate out Of Boundaries";
            uiHandler.showErrorMsg(errorMsg);
        } catch (StationAlreadyExistInCoordinateException e) {
            String errorMsg = "Station Already Exist In Coordinate ";
            uiHandler.showErrorMsg(errorMsg);
        } catch (StationNotFoundException e) {
            String errorMsg = "Cant create new road, station not found ";
            uiHandler.showErrorMsg(errorMsg);
        } catch (NoRoadBetweenStationsException e) {
            String errorMsg = "No Road Between " + e.getFromStation() + "to " + e.getToStation();
            uiHandler.showErrorMsg(errorMsg);
        }


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
            uiHandler.showErrorMsg(errorMsg);
        } catch (NoPathExistBetweenStationsException e){
          String errorMsg = "No path found between " + request.getFromStation() + " and " + request.getToStation();
            uiHandler.showErrorMsg(errorMsg);
        }

    }

    private void addNewRide(NewRideRequest request) {
        //TODO leave for later stage
    }

    private void showStatusOfRides(GetStatusOfRidesRequest request) {
        uiHandler.showOutput(createSummaryOfAllRides());
    }

    private String createSummaryOfAllRides() {
        StringBuilder out = new StringBuilder("Summary of all Rides in the system:" + System.lineSeparator());

        for(Ride ride: logicHandler.getAllRides()){
            out.append(createDescriptionOfRide(ride));
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
        try {
            TrempRequest chosenTrempRequest = getDesiredTrempRequestForMatching();
            int maxNumberOfOptions = getMaxNumberOfTrempChanges();
            List<Ride.SubRide> chosenRides = getDesiredRidesForTrempRequest(chosenTrempRequest, maxNumberOfOptions);
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

    private List<Ride.SubRide> getDesiredRidesForTrempRequest(TrempRequest trempRequest, int optionsLimit)
            throws NoRidesWereFoundForTrempRequestException{

        List<List<Ride.SubRide>> allTremps = logicHandler.getAllPossibleTrempsForTrempRequest(trempRequest);
        if (allTremps == null || allTremps.size() == 0)
            throw new NoRidesWereFoundForTrempRequestException();

        allTremps = allTremps.stream().limit(optionsLimit).collect(Collectors.toList());

        List<String> availableRidesOptions  = createSummaryOfAllTrempOptions(allTremps);
        String title = "The following Rides are available for the Selected Tremp Request:";
        int desiredRidesOption = uiHandler.showOptionsAndGetUserSelection(title, availableRidesOptions);

        return allTremps.get(desiredRidesOption);
    }

    private int getMaxNumberOfTrempChanges(){
        return uiHandler.getNumberForString("Enter Max number of Ride options:");
    }
    private List<String> createSummaryOfAllTrempOptions(List<List<Ride.SubRide>> trempOptions){
        return trempOptions.stream().map(this::createDescriptionOfTrempOption).collect(Collectors.toList());
    }

    private String createDescriptionOfTrempOption(List<Ride.SubRide> trempOption){
        StringBuilder out = new StringBuilder();
        out.append(String.format("Total Connections: %d", trempOption.size() - 1));

        for (Ride.SubRide subRide : trempOption) {
            out.append(getSubRideSummary(subRide));
            out.append('-' * 20);
        }
        out.append(String.join(System.lineSeparator(),
                String.format("Estimated arrival time: %s", trempOption.get(trempOption.size() - 1).getArrivalTime()),
                String.format("Average Fuel usage: %.2f", trempOption.stream().mapToDouble(Ride.SubRide::getAverageFuelUsage).average().getAsDouble()),
                String.format("Total Cost: %.2f", trempOption.stream().mapToDouble(Ride.SubRide::getTotalCost).sum())
                )
        );

        return out.toString();
    }

    private String getSubRideSummary(Ride.SubRide subRide){
        StringBuilder out = new StringBuilder();
        out.append(String.join(System.lineSeparator(),
                String.format("Ride ID: %d", subRide.getOriginalRide().getID()),
                String.format("Ride Owner: %s", subRide.getOriginalRide().getRideOwner().getName()),
                String.format("Tremp cost : %.2f", subRide.getTotalCost()),
                String.format("Route: %s", subRide.getAllStations().stream().map(Station::getName).collect(Collectors.joining(" -> ")))
                )
        );
//        subRide.selectedPartsOfRide.forEach(part ->
//           out.append(String.format("%s -> %s", part.getRoad().getStartStation().getName(), part.getRoad().getEndStation().getName()))
//        );

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
        return String.join(System.lineSeparator(),
                String.format("Request User: %s", trempRequest.getUser().getName()),
                String.format("Stations: %s --> %s", trempRequest.getStartStation().getName(), trempRequest.getEndStation().getName()),
                String.format("Depart Time: %s", trempRequest.getDepartTime().format(DateTimeFormatter.ISO_TIME))
        );
    }

    private void exitApp(){
        exit = true;
    }
}
