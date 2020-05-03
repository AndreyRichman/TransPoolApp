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
import java.util.*;
import java.util.stream.Collectors;

public class App {
    UIHandler uiHandler;
    LogicHandler logicHandler;
    Boolean exit;

    boolean xmlLoadedCond = false;
    boolean treampsAddedCond = false;

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

        String title = "Load XML file";
        uiHandler.showTitle(title);
        uiHandler.getXMLpath((LoadXMLRequest)request);
        try {

            logicHandler.loadXMLFile(request.getFileDirectory());
            uiHandler.showOutput("Xml file loaded successfully!");
        }  catch (InvalidMapBoundariesException e) {
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
        } catch (FaildLoadingXMLFileException e) {
            uiHandler.showErrorMsg(e.getReason());
        }


    }


    private void addNewRide(NewRideRequest request) {
        //TODO leave for later stage
    }

    private void showStatusOfRides(GetStatusOfRidesRequest request) {
        uiHandler.showOutput(createSummaryOfAllRides());
    }

    private String createSummaryOfAllRides() {

        StringBuilder out = new StringBuilder(System.lineSeparator());

        String title = "All Ride in the system";
        uiHandler.showTitle(title);

        for(Ride ride: logicHandler.getAllRides()){
            out.append(createDescriptionOfRide(ride));
        }
        return out.toString();
    }

    private String createDescriptionOfRide(Ride ride){

        return String.join(System.lineSeparator(),
                String.format("Ride ID: %d", ride.getID()),
                String.format("Dept time: %d:%d ", ride.getSchedule().getHour(), ride.getSchedule().getMin() ),
                //String.format("Arr time: %.2f ", ride.getTotalTimeOfRide() + ride.getSchedule().getHour() + ride.getSchedule().getMin() ), //TODO: adds minuts to ride total cost of time
                String.format("Stations: %s", ride.getAllStations()
                        .stream()
                        .map(Station::getName)
                        .collect(Collectors.joining(" -> "))),
                String.format("%s", ride.isTrempsAssignToRide()? getDescriptionOfPartOfRide(ride.getPartOfRide()) : "No tremps assigned to this ride"),
                System.lineSeparator()
        );
    }

    private List<String> getTrempistsId(List<Trempist> trempists) {
        return trempists.stream().map(Trempist::getUser).map(User::getName).collect(Collectors.toList());
    }

    private List<String> getDescriptionOfPartOfRide(List<PartOfRide> lpride)
    {
        return lpride.stream().filter(p -> !p.getTrempistsManager().getAllTrempists().isEmpty()).map(this::createDescriptionOfPartOfRide).collect(Collectors.toList());
    }

    private String createDescriptionOfPartOfRide(PartOfRide pride){
        return String.join(System.lineSeparator(),
                String.format("Stop: [ %s ]", pride.getRoad().getStartStation().getName()),
                String.format("Trempists ID: [ %s ]", String.join(pride.getTrempistsManager().getAllTrempists().stream().map(Trempist::getUser).map(User::getName).collect(Collectors.joining(" , ")))),
                String.format("Add trempists names: [ %s ]", String.join(pride.getTrempistsManager().getJustJoinedTrempists().stream().map(Trempist::getUser).map(User::getName).collect(Collectors.joining(" , ")))),
                String.format("Remove trempists names: [ %s ] ", String.join(pride.getTrempistsManager().getLeavingTrempists().stream().map(Trempist::getUser).map(User::getName).collect(Collectors.joining(" , ")))));
    }

    private void showStatusOfTremps(GetStatusOfTrempsRequest request) {
        List<TrempRequest> allTrempRequests = logicHandler.getAllTrempRequests();
        String title = "All Tremp Requests";
        uiHandler.showTitle(title);

        if (allTrempRequests.size() > 0){
            List<String> summaryOfAllTremps = getSummaryOfAllTrempRequests(allTrempRequests);
            uiHandler.showOutput(String.join(System.lineSeparator(), summaryOfAllTremps));
        } else
            uiHandler.showOutput( String.join("", Collections.nCopies(10, " "))+ "No Rides found in the System");
    }

    private List<String> getSummaryOfAllTrempRequests(List<TrempRequest> allTrempRequests){
        return allTrempRequests.stream().map(this::createDescriptionOfTrempRequest).collect(Collectors.toList());
    }

    private String createDescriptionOfTrempRequest(TrempRequest trempRequest){
        String trempRequestDescription = String.join(System.lineSeparator(),
                String.format("Request ID: %d", trempRequest.getID()),
                String.format("Request User: %s", trempRequest.getUser().getName()),
                String.format("Stations: [ %s ] --> [ %s ]", trempRequest.getStartStation().getName(), trempRequest.getEndStation().getName()),
                String.format("Desired departure Time: %s", trempRequest.getDepartTime().format(DateTimeFormatter.ofPattern("HH:mm"))),
                String.format("Status: %s",
                        trempRequest.isNotAssignedToRides()? "Not Assigned to any Ride" : "Assigned to Ride")
        );
        if (!trempRequest.isNotAssignedToRides()){
            String assignedTrempsDescriptions = this.createDescriptionOfTrempOption(trempRequest.getSubRides());
            trempRequestDescription = String.join(System.lineSeparator() + "Assigned Tremp:" + System.lineSeparator(),
                    trempRequestDescription,
                    assignedTrempsDescriptions
            );
        }

        return trempRequestDescription;
    }

    private void tryMatchTrempToRide(TryMatchTrempToRideRequest request) {
        try {
            TrempRequest chosenTrempRequest = getDesiredTrempRequestForMatching();
            int maxNumberOfOptions = getMaxNumberOfTrempChanges();
            List<SubRide> chosenRides = getDesiredRidesForTrempRequest(chosenTrempRequest, maxNumberOfOptions);
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
        } catch (ActionAbortedException ignore){}


    }

    private TrempRequest getDesiredTrempRequestForMatching()
            throws NotFoundTrempRequestsWithoutMatchException, ActionAbortedException{
        List<TrempRequest> trempRequests = logicHandler.getAllNonMatchedTrempRequests();
        if (trempRequests == null || trempRequests.size() == 0)
            throw new NotFoundTrempRequestsWithoutMatchException();

        List<String> trempRequestsStrOptions = getAllUnMatchedTrempRequestStrOptions(trempRequests);
        String title = "The following Tremp Requests are not assigned to any rides, select one to match a ride to it:";
        int chosenTrempRequest = uiHandler.showOptionsAndGetUserSelection(title, trempRequestsStrOptions);

        if(chosenTrempRequest == -1)
            throw new ActionAbortedException();
        return trempRequests.get(chosenTrempRequest);
    }

    private List<SubRide> getDesiredRidesForTrempRequest(TrempRequest trempRequest, int optionsLimit)
            throws NoRidesWereFoundForTrempRequestException, ActionAbortedException {

        List<List<SubRide>> allTremps = logicHandler.getAllPossibleTrempsForTrempRequest(trempRequest);
        if (allTremps == null || allTremps.size() == 0)
            throw new NoRidesWereFoundForTrempRequestException();

        allTremps = allTremps.stream().limit(optionsLimit).collect(Collectors.toList());

        String title = "The following Rides are available for the Selected Tremp Request:";
        List<String> availableRidesOptions  = createSummaryOfAllTrempOptions(allTremps);
        int desiredRidesOption = uiHandler.showOptionsAndGetUserSelection(title, availableRidesOptions);

        if (desiredRidesOption == -1)
            throw new ActionAbortedException();

        return allTremps.get(desiredRidesOption);
    }

    private int getMaxNumberOfTrempChanges(){
        return uiHandler.getNumberForString("Enter Max number of Ride options:");
    }
    private List<String> createSummaryOfAllTrempOptions(List<List<SubRide>> trempOptions){
        return trempOptions.stream()
                .map(this::createDescriptionOfTrempOption)
                .collect(Collectors.toList());
    }

    private String createDescriptionOfTrempOption(List<SubRide> trempOption){
        StringBuilder out = new StringBuilder();
        String trempOptionDelimiter = String.join("", Collections.nCopies(5, " "));
        String trempPartsDelimiter = String.join("", Collections.nCopies(10, " "));
        out.append(String.join(System.lineSeparator() + trempOptionDelimiter,
                "",
                String.format("Estimated arrival time: %s", trempOption.get(trempOption.size() - 1).getArrivalTime()),
                String.format("Average Fuel usage: %.2f", trempOption.stream().mapToDouble(SubRide::getAverageFuelUsage).average().getAsDouble()),
                String.format("Total Distance: %.1f km", trempOption.stream().mapToDouble(SubRide::getTotalDistance).sum()),
                String.format("Total Cost: %.2f", trempOption.stream().mapToDouble(SubRide::getTotalCost).sum()),
                String.format("Total Connections: %d", trempOption.size() - 1),
                String.format("Tremp parts: %s", System.lineSeparator())
                )
        );

        List<String> summaryOfAllTrempOptionParts = createSummaryOfAllTrempOptionParts(trempOption, trempPartsDelimiter);
        out.append(
                String.join(trempPartsDelimiter + trempPartsDelimiter + "+",
                        summaryOfAllTrempOptionParts)
        );

        return out.toString();
    }

    private List<String> createSummaryOfAllTrempOptionParts(List<SubRide> trempOptionParts, String trempPartsDelimiter){
        List<String> summary = new ArrayList<>(trempOptionParts.size());

        for (SubRide subRide : trempOptionParts) {
            summary.add(String.format("%s%s", trempPartsDelimiter,
                    String.join(System.lineSeparator() + trempPartsDelimiter,getSubRideSummary(subRide))));
        }

        return summary;
    }

    private List<String> getSubRideSummary(SubRide subRide){
        return new ArrayList<>(
                Arrays.asList(
                        String.format("Ride ID: %d", subRide.getOriginalRide().getID()),
                        String.format("Ride Owner: %s", subRide.getOriginalRide().getRideOwner().getName()),
                        String.format("Tremp cost : %.2f", subRide.getTotalCost()),
                        String.format("Route: %s", subRide.getAllStations().stream().map(Station::getName).collect(Collectors.joining(" -> ")))
                )
        );
    }

    private void connectTrempRequestToSubRides(TrempRequest trempRequest, List<SubRide> subRides){
        subRides.forEach(subRide -> {
            subRide.applyTrempistToAllPartsOfRide(trempRequest.getUser());
            trempRequest.addSubRide(subRide);
        });
    }

    private List<String> getAllUnMatchedTrempRequestStrOptions(List<TrempRequest> trempRequests) {
        return trempRequests.stream().map(this::createDescriptionOfTrempRequest).collect(Collectors.toList());
    }

    private void addNewTrempRequest(NewTrempRequest request) {
        try {
            TrempRequest newTrempRequest = createNewTrempRequest();
            logicHandler.addTrempRequest(newTrempRequest);
            uiHandler.showOutput("Your Tremp request was submitted successfully.");
        } catch (NoPathExistBetweenStationsException e) {
            uiHandler.showErrorMsg("No path found between selected stations.");
        }

    }
    private TrempRequest createNewTrempRequest() throws NoPathExistBetweenStationsException {
        Station fromStation = getStartStationFromUser();
        Station toStation = getEndStationFromUserBasedOnFromStation(fromStation);
        TrempRequest newTrempRequest = logicHandler.createNewEmptyTrempRequest(fromStation, toStation);

        String userName = uiHandler.getStringForQuestion("Enter Your Name:");
        newTrempRequest.setUser(logicHandler.getUserByName(userName));

        LocalTime departTime = uiHandler.getTimeFromUser("Enter Depart time in HH:MM format: ");
        newTrempRequest.setDepartTime(departTime);

        return newTrempRequest;
    }

    private Station getStartStationFromUser(){
        String title = "Select Depart Station:";
        return getSelectionOfStationFromUser(title, logicHandler.getAllStations());
    }

    private Station getEndStationFromUserBasedOnFromStation(Station fromStation){
        String title = "Select Arrive Station:";
        return getSelectionOfStationFromUser(title, new ArrayList<>(fromStation.getAllReachableStations()));
    }

    private Station getSelectionOfStationFromUser(String title, List<Station> stationOptions){
        List<String> stationsNames = stationOptions.stream().map(Station::getName).collect(Collectors.toList());
        int desiredStationIndex = uiHandler.showOptionsAndGetUserSelection(title, stationsNames);

        return stationOptions.get(desiredStationIndex);
    }


    private void exitApp(){
        exit = true;
    }
}
