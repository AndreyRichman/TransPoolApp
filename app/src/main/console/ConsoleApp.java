package main.console;

import enums.DesiredTimeType;
import exception.*;
import transpool.ui.interfaces.UIHandler;
import transpool.logic.handler.LogicHandler;
import transpool.logic.map.structure.Road;
import transpool.logic.map.structure.Station;
import transpool.logic.traffic.item.*;
import transpool.logic.user.Trempist;
import transpool.logic.user.User;
import transpool.ui.console.ConsoleUI;
import transpool.ui.request.enums.RequestType;
import transpool.ui.interfaces.UserRequest;
import transpool.ui.request.type.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static transpool.ui.request.enums.RequestType.EXIT;
import static transpool.ui.request.enums.RequestType.LOAD_XML_FILE;

public class ConsoleApp implements Runnable {
    public static final int ABORT_INDEX = -1;
    public static final String LIST_ITEMS_SEPARATOR = System.lineSeparator()
            + String.join("", Collections.nCopies(40, "="))
            + System.lineSeparator();
    public static final String TREMP_OPTION_DELIMITER = String.join("", Collections.nCopies(5, " "));
    public static final String TREMP_PARTS_DELIMITER = String.join("", Collections.nCopies(10, " "));


    private UIHandler uiHandler;
    private LogicHandler logicHandler;
    private boolean exit;
    private boolean xmlLoadedCond = false;

    public ConsoleApp() {
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

    private void processRequest(UserRequest request) {
        if(request.getRequestType().equals(LOAD_XML_FILE)){
            loadContentFromXMLFile((LoadXMLRequest)request);
            xmlLoadedCond = true;
        }
        else if(request.getRequestType().equals(EXIT)) {
            exitApp();
        }
        else {
            if (xmlLoadedCond)
                processLogicRequest(request);
            else
                uiHandler.showOutput("Xml file must be load to system first!");
        }
    }


    private void processLogicRequest(UserRequest request){
        RequestType requestType = request.getRequestType();

        switch (requestType){
            case LOAD_XML_FILE:
                loadContentFromXMLFile((LoadXMLRequest)request);
                break;
            case NEW_TREMP:
                addNewTrempRequest((NewTrempRequest)request);
                break;
            case NEW_RIDE:
                addNewRide((NewRideRequest)request);
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
        String path = uiHandler.getStringForQuestion("Please enter full path of XML file:");
        request.setFileDirectory(path);

        try {
            loadXMLFileFromRequest(request);
            uiHandler.showOutput("Xml file loaded successfully!");
        }  catch (FaildLoadingXMLFileException e) {
            uiHandler.showOutput(e.getReason());
        }
    }

    private void loadXMLFileFromRequest(LoadXMLRequest request) throws FaildLoadingXMLFileException {
        logicHandler.loadXMLFile(request.getFileDirectory());
    }

    private void showStatusOfRides(GetStatusOfRidesRequest request) {
        uiHandler.showOutput(createSummaryOfAllRides());
    }

    private String createSummaryOfAllRides() {

        StringBuilder out = new StringBuilder();

        String title = "All Ride in the system";
        uiHandler.showTitle(title);

        for(Ride ride: logicHandler.getAllRides()){
            out.append(createDescriptionOfRide(ride));
        }
        return out.toString();
    }

    private String createDescriptionOfRide(Ride ride){
        String description = String.join(System.lineSeparator(),
                String.format("Ride ID: %d", ride.getID()),
                String.format("Depart Time: %s", ride.getDepartTime()),
                String.format("Estimated Arrive Time: %s", ride.getArriveTime()),
                String.format("Stations: %s", ride.getAllStations()
                        .stream()
                        .map(Station::getName)
                        .collect(Collectors.joining(" -> "))),
                String.format("Status: %s", ride.isTrempsAssignToRide()?
                        "Tremps assigned to this ride": "No Tremps assigned to this ride"),
                System.lineSeparator()
        );

        if (ride.isTrempsAssignToRide())
            description = String.format("%s%s%s",description, System.lineSeparator(),
                    String.join(System.lineSeparator(), getDescriptionOfPartOfRide(ride.getPartOfRide())));

        return description;
    }

    private List<String> getTrempistsId(List<Trempist> trempists) {
        return trempists.stream().map(Trempist::getUser).map(User::getName).collect(Collectors.toList());
    }

    private List<String> getDescriptionOfPartOfRide(List<PartOfRide> lpride)
    {
        return lpride.stream().filter(p -> !p.getTrempistsManager().getAllTrempists().isEmpty())
                .map(this::createDescriptionOfPartOfRide).collect(Collectors.toList());
    }

    private String createDescriptionOfPartOfRide(PartOfRide pride){
        String fromStation = pride.getRoad().getStartStation().getName();
        String toStation = pride.getRoad().getEndStation().getName();
        String startTime = pride.getStartTime().toString();
        String endTime = pride.getEndTime().toString();
        int freePlaces = pride.getTotalCapacity();
        String allTrempistsIDS = String.format("Trempists ID: [ %s ]", (pride.getTrempistsManager().getAllTrempists().stream().map(Trempist::getUser).map(User::getID).map(Object::toString).collect(Collectors.joining(", "))));
        String trempistsJoining = pride.getTrempistsManager().getJustJoinedTrempists().stream().map(Trempist::getUser).map(user -> String.format("%d-%s", user.getID(), user.getName())).collect(Collectors.joining(", "));
        String trempistsLeaving = pride.getTrempistsManager().getLeavingTrempists().stream().map(Trempist::getUser).map(user -> String.format("%d-%s", user.getID(), user.getName())).collect(Collectors.joining(", "));

        String trempistsNames = pride.getTrempistsManager().getAllTrempists().stream().map(Trempist::getUser).map(
                user -> String.format("%d-%s", user.getID(), user.getName())).collect(Collectors.joining(", "));

        return String.join(System.lineSeparator(),
                String.join(" ",
                        String.format("Free Places: %d.", freePlaces),
                        String.format("All Trempists IDs: %s", allTrempistsIDS)
                ),
                String.join(", ",
                        String.format("From Station: %s", fromStation),
                        String.format("Leaving at: %s", startTime),
                        String.format("Picking up: %s", trempistsJoining)
                ),
                " | ",
                "\\|/",
                String.join(", ",
                        String.format("To Station: %s", toStation),
                        String.format("Arriving at: %s", endTime),
                        String.format("Leaving in Station: %s", trempistsLeaving)
                ),
                LIST_ITEMS_SEPARATOR
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

        String title = "Match For Tremp Request";
        uiHandler.showTitle(title);

        try {
            request.setTrempRequestID(getDesiredTrempRequestForMatching());
            request.setMaxNumberOfOptions(getMaxNumberOfTrempChanges());

            matchTrempToRideFromRequest(request);
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
        catch (TrempRequestNotExist trempRequestNotExist) {
            uiHandler.showOutput("Tremp request not found");
        }


    }

    private void matchTrempToRideFromRequest(TryMatchTrempToRideRequest request) throws TrempRequestNotExist, NoRidesWereFoundForTrempRequestException, ActionAbortedException {
        TrempRequest chosenTrempRequest = logicHandler.getTrempRequestById(request.getTrempRequestID());
        int maxNumberOfOptions = request.getMaxNumberOfOptions();

        RideForTremp chosenRides = getDesiredRidesForTrempRequest(chosenTrempRequest, maxNumberOfOptions);
        connectTrempRequestToSubRides(chosenTrempRequest, chosenRides);
    }

    private int getDesiredTrempRequestForMatching()
            throws NotFoundTrempRequestsWithoutMatchException, ActionAbortedException{
        List<TrempRequest> trempRequests = logicHandler.getAllNonMatchedTrempRequests();
        if (listIsEmpty(trempRequests))
            throw new NotFoundTrempRequestsWithoutMatchException();

        List<String> trempRequestsStrOptions = getAllUnMatchedTrempRequestStrOptions(trempRequests);
        String title = "The following Tremp Requests are not assigned to any rides, select one to match a ride to it:";
        int chosenTrempRequest = uiHandler.showOptionsAndGetUserSelection(title, trempRequestsStrOptions);

        if(chosenTrempRequest == ABORT_INDEX)
            throw new ActionAbortedException();

        return trempRequests.get(chosenTrempRequest).getID();
    }

    private RideForTremp getDesiredRidesForTrempRequest(TrempRequest trempRequest, int optionsLimit)
            throws NoRidesWereFoundForTrempRequestException, ActionAbortedException {

        List<RideForTremp> allTremps = logicHandler.getAllPossibleTrempsForTrempRequest(trempRequest);
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
    private List<String> createSummaryOfAllTrempOptions(List<RideForTremp> trempOptions){
        return trempOptions.stream()
                .map(this::createDescriptionOfTrempOption)
                .collect(Collectors.toList());
    }

    private String createDescriptionOfTrempOption(RideForTremp trempOption){
        StringBuilder out = new StringBuilder();

        out.append(String.join(System.lineSeparator() + TREMP_OPTION_DELIMITER,
                "",
                String.format("Depart time: %s", trempOption.getDepartTime()),
                String.format("Estimated Arrive time: %s", trempOption.getArriveTime()),
                String.format("Average Fuel usage: %.2f", trempOption.getAverageFuelUsage()),
                String.format("Total Distance: %.1f km", trempOption.getTotalDistance()),
                String.format("Total Cost: %.2f", trempOption.getTotalCost()),
                String.format("Total Connections: %d", trempOption.getNumOfConnections()),
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

    private List<String> createSummaryOfAllTrempOptionParts(RideForTremp trempOptionParts){
        List<String> summary = new ArrayList<>(trempOptionParts.getNumOfParts());

        for (SubRide subRide : trempOptionParts.getSubRides()) {
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
    private void connectTrempRequestToSubRides(TrempRequest trempRequest, RideForTremp ridesToAssign){
        trempRequest.assignRides(ridesToAssign);
        ridesToAssign.assignTrempRequest(trempRequest);
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
            String assignedTrempsDescriptions = this.createDescriptionOfTrempOption(trempRequest.getSelectedRide());
            trempRequestDescription = String.join(System.lineSeparator() + "Assigned Tremp:" + System.lineSeparator(),
                    trempRequestDescription,
                    assignedTrempsDescriptions
                    );
        }

        return trempRequestDescription;
    }

    private void addNewTrempRequest(NewTrempRequest request) {
        try {
            String fromStation = getStartStationFromUser();
            request.setFromStation(fromStation);
            request.setToStation(getEndStationFromUserBasedOnFromStation(fromStation));
            request.setUserName(uiHandler.getStringForQuestion("Enter Your Name:"));
            String desiredTimeType = getDesiredTypeOfTimeFromUser();
            request.setDesiredTimeType(desiredTimeType);
            request.setChosenTime(uiHandler.getTimeFromUser(String.format("Enter %s time in HH:MM format: ", desiredTimeType)).toString());

            addNewTrempFromRequest(request);

        } catch (NoPathExistBetweenStationsException e) {
            uiHandler.showOutput("No path found between selected stations.");
        } catch (ActionAbortedException ignore){}

    }

    private void addNewTrempFromRequest(NewTrempRequest request) throws NoPathExistBetweenStationsException {
        Station fromStation = logicHandler.getStationFromName(request.getFromStation());
        Station toStation = logicHandler.getStationFromName(request.getToStation());

        TrempRequest newTrempRequest = logicHandler.createNewEmptyTrempRequest(fromStation, toStation);
        newTrempRequest.setUser(logicHandler.getUserByName(request.getUserName()));
        DesiredTimeType desiredTimeType = DesiredTimeType.valueOf(request.getDesiredTimeType());
        newTrempRequest.setDesiredTimeType(desiredTimeType);

        LocalTime desiredTime = LocalTime.parse(request.getChosenTime());
        newTrempRequest.setDesiredTime(desiredTime);

        logicHandler.addTrempRequest(newTrempRequest);
    }

    private String getDesiredTypeOfTimeFromUser(){
        String title = "Do you wish to Depart or Arrive at specific Time?";
        List<DesiredTimeType> options = Arrays.asList(DesiredTimeType.values());
        List<String> optionsNames = options.stream().map(Enum::name).collect(Collectors.toList());
        int selectedIndex = uiHandler.showOptionsAndGetUserSelection(title, optionsNames);

        return options.get(selectedIndex).name();
    }

    private String getStartStationFromUser() throws ActionAbortedException {
        String title = "Select Depart Station:";
        return getSelectionOfStationFromUser(title, logicHandler.getAllStations());
    }

    private String getEndStationFromUserBasedOnFromStation(String fromStationName) throws ActionAbortedException {
        String title = "Select Arrive Station:";
        Station fromStation = logicHandler.getStationFromName(fromStationName);
        Set<Station> allReachableStation = fromStation.getAllReachableStations();
        allReachableStation.remove(fromStation);

        return getSelectionOfStationFromUser(title, new ArrayList<>(allReachableStation));
    }

    private String getSelectionOfStationFromUser(String title, List<Station> stationOptions) throws ActionAbortedException {
        List<String> stationsNames = stationOptions.stream().map(Station::getName).collect(Collectors.toList());
        int desiredStationIndex = uiHandler.showOptionsAndGetUserSelection(title, stationsNames);

        if (desiredStationIndex == ABORT_INDEX)
            throw new ActionAbortedException();

        return stationOptions.get(desiredStationIndex).getName();
    }

    private void addNewRide(NewRideRequest request) {

        String title = "New Ride";
        uiHandler.showTitle(title);

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

        if (selectedStationsNames.size() < 2 ){
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

            if(stationsMenu.size() == 0)
                routeEnded = true;
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
        uiHandler.showOutput("Exiting the System. Hope you had a great ride and to see you again");
        exit = true;
    }
}
