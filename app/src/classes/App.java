package classes;

import enums.DesiredTimeType;
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
    public static final int ABORT_INDEX = -1;
    public static final String LIST_ITEMS_SEPARATOR = System.lineSeparator()
            + String.join("", Collections.nCopies(40, "="))
            + System.lineSeparator();
    public static final String TREMP_OPTION_DELIMITER = String.join("", Collections.nCopies(5, " "));
    public static final String TREMP_PARTS_DELIMITER = String.join("", Collections.nCopies(10, " "));


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
        String title = "All Tremp Requests";
        uiHandler.showTitle(title);

        if (allTrempRequests.size() > 0){
            List<String> summaryOfAllTremps = getSummaryOfAllTrempRequests(allTrempRequests);
            uiHandler.showOutput(String.join(LIST_ITEMS_SEPARATOR, summaryOfAllTremps));
        } else
            uiHandler.showOutput( String.join("", Collections.nCopies(10, " "))+ "No Rides found in the System");

    }

    private List<String> getSummaryOfAllTrempRequests(List<TrempRequest> allTrempRequests){
        return allTrempRequests.stream().map(this::createDescriptionOfTrempRequest).collect(Collectors.toList());
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
        if (listIsEmpty(trempRequests))
            throw new NotFoundTrempRequestsWithoutMatchException();

        List<String> trempRequestsStrOptions = getAllUnMatchedTrempRequestStrOptions(trempRequests);
        String title = "The following Tremp Requests are not assigned to any rides, select one to match a ride to it:";
        int chosenTrempRequest = uiHandler.showOptionsAndGetUserSelection(title, trempRequestsStrOptions);

        if(chosenTrempRequest == ABORT_INDEX)
            throw new ActionAbortedException();

        return trempRequests.get(chosenTrempRequest);
    }

    private List<SubRide> getDesiredRidesForTrempRequest(TrempRequest trempRequest, int optionsLimit)
            throws NoRidesWereFoundForTrempRequestException, ActionAbortedException {

        List<List<SubRide>> allTremps = logicHandler.getAllPossibleTrempsForTrempRequest(trempRequest);
        if (listIsEmpty(allTremps))
            throw new NoRidesWereFoundForTrempRequestException();

        allTremps = allTremps.stream().limit(optionsLimit).collect(Collectors.toList());

        String title = "The following Rides are available for the Selected Tremp Request:";
        List<String> availableRidesOptions  = createSummaryOfAllTrempOptions(allTremps);
        int desiredRidesOption = uiHandler.showOptionsAndGetUserSelection(title, availableRidesOptions);

        if (desiredRidesOption == ABORT_INDEX)
            throw new ActionAbortedException();

        return allTremps.get(desiredRidesOption);
    }

    private boolean listIsEmpty(List list){
        return list == null || list.size() == 0;
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

        out.append(String.join(System.lineSeparator() + TREMP_OPTION_DELIMITER,
                "",
                String.format("Depart time: %s", trempOption.get(0).getDepartTime()),
                String.format("Estimated Arrive time: %s", trempOption.get(trempOption.size() - 1).getArrivalTime()),
                String.format("Average Fuel usage: %.2f", trempOption.stream().mapToDouble(SubRide::getAverageFuelUsage).average().getAsDouble()),
                String.format("Total Distance: %.1f km", trempOption.stream().mapToDouble(SubRide::getTotalDistance).sum()),
                String.format("Total Cost: %.2f", trempOption.stream().mapToDouble(SubRide::getTotalCost).sum()),
                String.format("Total Connections: %d", trempOption.size() - 1),
                String.format("Tremp parts: %s", System.lineSeparator())
                )
        );

        List<String> summaryOfAllTrempOptionParts = createSummaryOfAllTrempOptionParts(trempOption);
        out.append(
                String.join(TREMP_PARTS_DELIMITER + TREMP_PARTS_DELIMITER + "+",
                        summaryOfAllTrempOptionParts)
        );

        return out.toString();
    }

    private List<String> createSummaryOfAllTrempOptionParts(List<SubRide> trempOptionParts){
        List<String> summary = new ArrayList<>(trempOptionParts.size());

        for (SubRide subRide : trempOptionParts) {
            summary.add(String.format("%s%s", TREMP_PARTS_DELIMITER,
                    String.join(System.lineSeparator() + TREMP_PARTS_DELIMITER,getSubRideSummary(subRide))));
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

    private String createDescriptionOfTrempRequest(TrempRequest trempRequest){
        String trempRequestDescription = String.join(System.lineSeparator(),
                String.format("Request ID: %d", trempRequest.getID()),
                String.format("Request User: %s", trempRequest.getUser().getName()),
                String.format("Stations: [ %s ] --> [ %s ]", trempRequest.getStartStation().getName(), trempRequest.getEndStation().getName()),
                String.format("Desired %s Time: %s", trempRequest.getDesiredTimeType().name(), trempRequest.getDesiredTime().format(DateTimeFormatter.ofPattern("HH:mm"))),
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

    private void addNewTrempRequest(NewTrempRequest request) {
        try {
            TrempRequest newTrempRequest = createNewTrempRequest();
            logicHandler.addTrempRequest(newTrempRequest);
            uiHandler.showOutput("Your Tremp request was submitted successfully.");

        } catch (NoPathExistBetweenStationsException e) {
            uiHandler.showErrorMsg("No path found between selected stations.");
        } catch (ActionAbortedException ignore){}

    }
    private TrempRequest createNewTrempRequest() throws NoPathExistBetweenStationsException, ActionAbortedException {
        Station fromStation = getStartStationFromUser();
        Station toStation = getEndStationFromUserBasedOnFromStation(fromStation);
        TrempRequest newTrempRequest = logicHandler.createNewEmptyTrempRequest(fromStation, toStation);

        String userName = uiHandler.getStringForQuestion("Enter Your Name:");
        newTrempRequest.setUser(logicHandler.getUserByName(userName));

        DesiredTimeType desiredTimeType = getDesiredTypeOfTimeFromUser();
        newTrempRequest.setDesiredTimeType(desiredTimeType);

        LocalTime desiredTime = uiHandler.getTimeFromUser(String.format("Enter %s time in HH:MM format: ", desiredTimeType.name()));
        newTrempRequest.setDesiredTime(desiredTime);

        return newTrempRequest;
    }

    private DesiredTimeType getDesiredTypeOfTimeFromUser(){
        String title = "Do you wish to Depart or Arrive at specific Time?";
        List<DesiredTimeType> options = Arrays.asList(DesiredTimeType.values());
        List<String> optionsNames = options.stream().map(Enum::name).collect(Collectors.toList());
        int selectedIndex = uiHandler.showOptionsAndGetUserSelection(title, optionsNames);

        return options.get(selectedIndex);
    }

    private Station getStartStationFromUser() throws ActionAbortedException {
        String title = "Select Depart Station:";
        return getSelectionOfStationFromUser(title, logicHandler.getAllStations());
    }

    private Station getEndStationFromUserBasedOnFromStation(Station fromStation) throws ActionAbortedException {
        String title = "Select Arrive Station:";
        Set<Station> allReachableStation = fromStation.getAllReachableStations();
        allReachableStation.remove(fromStation);

        return getSelectionOfStationFromUser(title, new ArrayList<>(allReachableStation));
    }

    private Station getSelectionOfStationFromUser(String title, List<Station> stationOptions) throws ActionAbortedException {
        List<String> stationsNames = stationOptions.stream().map(Station::getName).collect(Collectors.toList());
        int desiredStationIndex = uiHandler.showOptionsAndGetUserSelection(title, stationsNames);

        if (desiredStationIndex == ABORT_INDEX)
            throw new ActionAbortedException();

        return stationOptions.get(desiredStationIndex);
    }

    private void addNewRide(NewRideRequest request) {
        try {
            request.setStations(getRouteStationsForNewRide());
            request.setUserName(uiHandler.getStringForQuestion("Enter Your Name:"));
            request.setCarCapacity(uiHandler.getNumberForString("Enter Car Capacity:"));
            request.setPricePerKilometer(uiHandler.getNumberForString("Enter Price per Kilometer:"));
            request.setStartTime(uiHandler.getTimeFromUser("Enter Depart time in HH:MM format: "));

            Ride newRide = createNewRideFromRequest(request);
            logicHandler.addRide(newRide);
        } catch (ActionAbortedException | NoRoadBetweenStationsException ignore) {}
    }

    private List<String> getRouteStationsForNewRide() throws ActionAbortedException {
        List<String> selectedStationsNames;
        List<String> stationsMenu = logicHandler.getAllStations().stream()
                .map(Station::getName)
                .collect(Collectors.toList());

        uiHandler.showOutput("Please Select Your Route Stations, enter 'q' to end the route.");
        selectedStationsNames = getStationsNamesForNewRide(stationsMenu);

        if (selectedStationsNames.size() < 2){
            boolean tryAgain = uiHandler.getYesNoAnswerForQuestion("Incorrect Number Of Station (minimum 2), would you like to try again?");
            if (tryAgain)
                return getRouteStationsForNewRide();
            else
                throw new ActionAbortedException();
        }

        return selectedStationsNames;
    }

    private List<String> getStationsNamesForNewRide(List<String> stationsMenu){
        List<String> selectedStationsNames = new LinkedList<>();
        boolean routeEnded = false;

        while(!routeEnded){

            int selectedIndex = uiHandler.showOptionsAndGetUserSelection("Select Station:", stationsMenu);
            if (selectedIndex == ABORT_INDEX)
                routeEnded = true;
            else{
                String selectedStationName = stationsMenu.get(selectedIndex);
                selectedStationsNames.add(selectedStationName);
                stationsMenu = logicHandler.getStationFromName(selectedStationName)
                        .getStationsAccessedFromCurrentStation().stream()
                        .map(Station::getName)
                        .collect(Collectors.toList());
                stationsMenu.removeAll(selectedStationsNames);
            }
        }

        return selectedStationsNames;
    }

    private Ride createNewRideFromRequest(NewRideRequest request) throws NoRoadBetweenStationsException {
        List<Road> roads = logicHandler.getRoadsFromStationsNames(request.getStations());
        User user = logicHandler.getUserByName(request.getUserName());
        Ride newRide = logicHandler.createNewEmptyRide(user, roads, request.getCarCapacity());

        newRide.setStartTime(request.getStartTime());
        newRide.setPricePerKilometer(request.getPricePerKilometer());

        return newRide;
    }

    private void exitApp(){
        exit = true;
    }
}
