package main.window.main.sub.map;

import com.fxgraph.graph.Graph;
import com.fxgraph.graph.Model;
import com.fxgraph.graph.PannableCanvas;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import main.window.main.MainWindowController;
import main.window.map.component.coordinate.CoordinateNode;
import main.window.map.component.coordinate.CoordinatesManager;
import main.window.map.component.details.StationDetailsDTO;
import main.window.map.component.road.ArrowedEdge;
import main.window.map.component.station.StationManager;
import main.window.map.component.station.StationNode;
import main.window.map.layout.MapGridLayout;
import transpool.logic.map.WorldMap;
import transpool.logic.map.structure.Road;
import transpool.logic.map.structure.Station;
import transpool.logic.time.Schedule;
import transpool.logic.traffic.item.PartOfRide;
import transpool.logic.traffic.item.Ride;
import transpool.logic.user.Trempist;
import transpool.logic.user.TrempistsManager;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


public class DynamicMapController {

    private final int MIN_BOARD_SCALE = 10;
    private final int MAX_BOARD_SCALE = 100;
    private int currentBoardScale = 25;

    private MainWindowController mainController;
    private Graph graphMap;
    private CoordinatesManager coordinatesManager;
    private StationManager stationManager;
    private List<ArrowedEdge>  allEdges;
    private List<ArrowedEdge> markedRedEdges;
    private List<ArrowedEdge> edgesWithText;
    private List<ArrowedEdge> markedBlueEdges;
    private Map<Road, ArrowedEdge> road2Edge;
    private String[] addSubOptions = {"Add(+)", "Sub(-)"};
    private int addSubIndex = 0;
    private String[] timeOptions = {"5 min", "30 min", "1 hour", "2 hours", "1 day"};
    private Integer[] timeMinutesOptions = {5, 30, 60, 120, 1440};
    private int timeOptionIndex = 0;
    private boolean isLiveMapOn = false;

    private LocalDateTime currentDateTime;



    @FXML
    private ScrollPane mapScrollPane;

    @FXML
    private Button zoomPlusButton;

    @FXML
    private Button zoomMinusButton;

    @FXML
    private Button liveToggleBtn;


    @FXML
    private Label dayTextField;

    @FXML
    private Button addSubBtn;

    @FXML
    private Button timeQuantityBtn;

    @FXML
    private Button applyTimeChangeBtn;

    @FXML
    private Label timeTextField;

    @FXML
    void onClickAddSubBtn(ActionEvent event) {
        updateAddSubBtn(this.addSubIndex + 1);
    }

    @FXML
    void onClickApplyTimeChangeBtn(ActionEvent event) {
        LocalDateTime newDateTime;
        if (this.addSubIndex == 0)  //add
            newDateTime =  this.currentDateTime.plusMinutes(this.timeMinutesOptions[this.timeOptionIndex]);
        else    //sub
            newDateTime = this.currentDateTime.minusMinutes(this.timeMinutesOptions[this.timeOptionIndex]);

        this.currentDateTime = newDateTime.isBefore(Schedule.minDateTime.plusDays(1)) ?
                Schedule.minDateTime.plusDays(1): newDateTime;

        updateDateTimeTextFieldsAccordingToTime(this.currentDateTime);
        this.mainController.switchLiveMapOn();
//        updateMapWithLiveTraffic();
    }

    private void updateDateTimeTextFieldsAccordingToTime(LocalDateTime dateTime){
        this.dayTextField.textProperty().set(String.format("%d", Schedule.getDayOfDateTime(dateTime)));
        this.timeTextField.textProperty().set(dateTime.toLocalTime().toString());
    }

    public List<String> getRidersName() {
        List<String> ridersNames = new ArrayList<>();

        int index = 0;
        List<Ride> allRides = this.mainController.getAllRides();
        for (Ride ride : allRides) {
            String toSHow = String.format("%d - %s",
                    ride.getID(),
                    ride.getRideOwner().getUser().getName());
            ridersNames.add(toSHow);
        }
        return ridersNames;
    }

    @FXML
    void onClickTimeQuantity(ActionEvent event) {
        updateTimeBtn(this.timeOptionIndex + 1);
    }

    @FXML
    void onClickLiveToggleBtn(ActionEvent event) {
        this.isLiveMapOn = !this.isLiveMapOn;
        applyLiveToggle();
    }

    public void applyLiveToggle(){
        if (this.isLiveMapOn)
            this.mainController.switchLiveMapOn();
        else
            this.mainController.switchLiveMapOff();
    }

    public void toggleLiveMapOn() {
        this.isLiveMapOn = true;
        this.liveToggleBtn.getStyleClass().clear();
        this.liveToggleBtn.getStyleClass().add("toggle-on");
        this.liveToggleBtn.getStyleClass().add("menu-toggle");
        updateMapWithLiveTraffic();
    }

    public void toggleLiveMapOff() {
        this.isLiveMapOn = false;
        this.liveToggleBtn.getStyleClass().clear();
        this.liveToggleBtn.getStyleClass().add("toggle-of");
        this.liveToggleBtn.getStyleClass().add("menu-toggle");

        updateMapWithLiveTraffic();
    }

    private void updateMapWithLiveTraffic() {
        if (this.isLiveMapOn)
            this.mainController.updateMapWithRidesRunningOn(this.currentDateTime);
        else {
            unMarkRedMarkedEdges();
            unMarkBlueMarkedEdges();
            removeTextFromEdges();
        }
    }

    public void clearMap(){
        unMarkRedMarkedEdges();
        unMarkBlueMarkedEdges();
        removeTextFromEdges();
        unMarkAllStations();

        this.mapScrollPane.setContent(null);
    }


    @FXML
    void onZoomMinusBtnClick(ActionEvent event) {
        updateVisualMapByScale(-5);
    }

    @FXML
    void onZoomPlusBtnClick(ActionEvent event) {
        updateVisualMapByScale(5);
    }

    public void setMainController(MainWindowController mainController) {
        this.mainController = mainController;

    }

    @FXML
    public void initialize(){
        updateAddSubBtn(0);
        updateTimeBtn(0);
        this.currentDateTime = Schedule.minDateTime.plusDays(1).plusHours(8);
        updateDateTimeTextFieldsAccordingToTime(this.currentDateTime);
        toggleLiveMapOff();
    }

    private void updateAddSubBtn(int index){
        index = index % this.addSubOptions.length;
        this.addSubBtn.setText(this.addSubOptions[index]);
        this.addSubIndex = index;
    }

    private void updateTimeBtn(int index){
        index = index % this.timeOptions.length;
        this.timeQuantityBtn.setText(this.timeOptions[index]);
        this.timeOptionIndex = index;
    }

    public void initVisualMap(WorldMap worldMap) {
        this.graphMap = new Graph();
        initializeMapWithLogicMap(worldMap);
        updateVisualMapByScale(0);
    }

    private void initializeMapWithLogicMap(WorldMap worldMap){
        Model graphModel = this.graphMap.getModel();
        this.graphMap.beginUpdate();
        this.stationManager = loadStations(graphModel, worldMap.getAllStations());
        this.coordinatesManager = new CoordinatesManager(CoordinateNode::new);// createCoordinates(graphModel);
        this.allEdges = createEdges(this.coordinatesManager, worldMap.getAllRoads());
        this.allEdges.forEach(graphModel::addEdge);
        showRegularLines(this.allEdges);
        hideArrow(this.allEdges);
        this.graphMap.endUpdate();
    }

    private void updateVisualMapByScale(int scaleDiff){
        int newScale = currentBoardScale + scaleDiff;
        newScale = newScale < MIN_BOARD_SCALE ? MIN_BOARD_SCALE :
                Math.min(newScale, MAX_BOARD_SCALE);

        this.currentBoardScale = newScale;
        this.graphMap.layout(new MapGridLayout(coordinatesManager, stationManager, newScale));
        PannableCanvas canvas = graphMap.getCanvas();
        this.mapScrollPane.setContent(canvas);
        Platform.runLater(() -> {
            graphMap.getUseViewportGestures().set(false);
            graphMap.getUseNodeGestures().set(false);
        });
    }

    private void updateEdgesWithSelectedStyle(List<ArrowedEdge> specialEdges) {
        if (this.markedRedEdges != null) {
            unMarkRedMarkedEdges();
            removeTextFromEdges();
        }

        this.markedRedEdges = specialEdges;
        markEdgesInRed(specialEdges);
        showArrow(specialEdges);
    }


    private void updateEdgesWithBlueColor(List<ArrowedEdge> edgesToBlue) {
        if (this.markedBlueEdges != null) {
            unMarkBlueMarkedEdges();
            removeTextFromEdges();
        }

        this.markedBlueEdges = edgesToBlue;
        markEdgesInBlue(edgesToBlue);
        showArrow(edgesToBlue); //showBlueArrow
    }

    private Map<Station, StationNode> allStationNodes;

    public void clearAllStationsData(){
        if (allStationNodes != null)
            this.allStationNodes.forEach((station, stationNode) -> stationNode.getStationDetailsDTO().clearAllLists());
    }

    public void updateStationsWindowWithPartOfRide(PartOfRide partOfRide, LocalDateTime dateTime){

//        rides.forEach(ride -> {
//            ride.getPartsOfRide().forEach(partOfRide -> {
        Station from = partOfRide.getRoad().getStartStation();
        Station to = partOfRide.getRoad().getEndStation();
        StationNode startStationNode = this.allStationNodes.get(from);
        StationNode endStationNode = this.allStationNodes.get(to);
        updateStartStationNodeWithTrempists(startStationNode, partOfRide, dateTime);
        updateEndStationNodeWithTrempists(endStationNode, partOfRide, dateTime);
//
//                //TO Change Existing Details:
//                StationDetailsDTO startExistingDetails = startStationNode.getStationDetailsDTO();
//                StationDetailsDTO endExistingDetails = endStationNode.getStationDetailsDTO();

//                List<String> allTrempists = startExistingDetails.getAllTrempists();
//                List<String> leavingTrempists = startExistingDetails.getOffTremp();
//                List<String> gettingOnTrempists = startExistingDetails.getOnTremp();
//
//                partOfRide.getTrempistsManager().getAllTrempists(Schedule.getDayOfDateTime(when)).forEach(
//                        trempist -> {
//                            if(!allTrempists.contains(trempist.getUser().getName()))
//                                allTrempists.add(trempist.getUser().getName());
//                            if(leavingTrempists.contains(trempist))
//                        }
//                );
//                existingDetails.setName("New Details");
//                existingDetails.setWhateverYouWant(ride); //You can Even Add ride

                //TO Replace existing details with new one
//                List<String> trips = new ArrayList<>();
//                StationDetailsDTO details = new StationDetailsDTO();
//                startStationNode.setStationDetailsDTO(details);

//            });
//        });
    }

//    private void updateEndStationNodeWithTrempists(StationNode endStationNode, PartOfRide partOfRide,
//                                                   LocalDateTime when) {
//        StationDetailsDTO startExistingDetails = endStationNode.getStationDetailsDTO();
//
//        List<String> allRiders = startExistingDetails.getAllRiders();
//        List<String> leavingTrempists = startExistingDetails.getOffTremp();
//        String rider = String.format("%s - ride %d", partOfRide.getRide().getRideOwner().getUser().getName(),
//                partOfRide.getRide().getID());
//
//        if(allRiders.contains(rider))
//            allRiders.add(rider);
//
//        partOfRide.getTrempistsManager().getAllTrempists(Schedule.getDayOfDateTime(when)).forEach(
//                trempist -> {
//                    String trempistName = String.format("%s - leaving %s", trempist.getUser().getName(), partOfRide.getRide().getID());
//
//                    if(!leavingTrempists.contains(trempistName))
//                        leavingTrempists.add(trempistName);
//                }
//        );
//    }

    private void updateEndStationNodeWithTrempists(StationNode endStationNode, PartOfRide partOfRide, LocalDateTime dateTime) {
        StationDetailsDTO endExistingDetails = endStationNode.getStationDetailsDTO();

        List<String> allRiders = endExistingDetails.getAllRiders();
        List<String> gettingOffTrempists = endExistingDetails.getOffTremp();

        String rider = String.format("%s - ride %d", partOfRide.getRide().getRideOwner().getUser().getName(), partOfRide.getRide().getID());

        if(!allRiders.contains(rider))
            allRiders.add(rider);

        TrempistsManager manager = partOfRide.getTrempistsManager();
        List<Trempist> allLeavingTrempists = dateTime == null? manager.getLeavingTrempistsAllDays()
                : manager.getLeavingTrempists(Schedule.getDayOfDateTime(dateTime));

        allLeavingTrempists.forEach(
                trempist -> {
                    String trempistName = String.format("%s - leave %s(day %d %s)",
                            trempist.getUser().getName(),
                            partOfRide.getRide().getID(),
                            Schedule.getDayOfDateTime(partOfRide.getSchedule().getEndDateTime()),
                            partOfRide.getSchedule().getEndTime());

                    if(!gettingOffTrempists.contains(trempistName))
                        gettingOffTrempists.add(trempistName);
                }
        );

        endExistingDetails.setAllRiders(allRiders);
        endExistingDetails.setOffTremp(gettingOffTrempists);
        endStationNode.setStationDetailsDTO(endExistingDetails);
    }

    private void updateStartStationNodeWithTrempists(StationNode startStationNode, PartOfRide partOfRide, LocalDateTime dateTime) {
        StationDetailsDTO startExistingDetails = startStationNode.getStationDetailsDTO();

        List<String> allRiders = startExistingDetails.getAllRiders();
        List<String> gettingOnTrempists = startExistingDetails.getOnTremp();

        String rider = String.format("%s - ride %d", partOfRide.getRide().getRideOwner().getUser().getName(), partOfRide.getRide().getID());

        if(!allRiders.contains(rider))
            allRiders.add(rider);

        TrempistsManager manager = partOfRide.getTrempistsManager();
        List<Trempist> allJoiningTrempists = dateTime == null? manager.getJustJoinedTrempistsAllDays()
                : manager.getJustJoinedTrempists(Schedule.getDayOfDateTime(dateTime));

        allJoiningTrempists.forEach(
                trempist -> {
                    String trempistName = String.format("%s - join %s(day %d %s)",
                            trempist.getUser().getName(),
                            partOfRide.getRide().getID(),
                            Schedule.getDayOfDateTime(partOfRide.getSchedule().getStartDateTime()),
                            partOfRide.getSchedule().getStartTime());

                    if(!gettingOnTrempists.contains(trempistName))
                        gettingOnTrempists.add(trempistName);
                }
        );

        startExistingDetails.setAllRiders(allRiders);
        startExistingDetails.setOnTremp(gettingOnTrempists);
        startStationNode.setStationDetailsDTO(startExistingDetails);
    }


    private StationManager loadStations(Model graphModel, List<Station> allStations) {
        StationManager stationManager = new StationManager(StationNode::new);
        allStationNodes = new HashMap<>();

        allStations.forEach(station -> {
            StationNode stationNode = stationManager.getOrCreate(station.getCoordinate().getX(), station.getCoordinate().getY());
            stationNode.setName(station.getName());
            graphModel.addCell(stationNode);

            allStationNodes.put(station, stationNode);

            stationNode.setStationDetailsDTO(new StationDetailsDTO());
//            stationNode.setDetailsSupplier(() -> {
//                StationDetailsDTO sta =  ;
////                sta.setDrives(getRidersName()); //needs to get riders on this station
////                sta.setOnTremp(getRidersName()); //needs to get on- Tremps on this station
////                sta.setOffTremp(getRidersName()); // needs to get off-tremp on this station
//                return sta;
//            });


        });

        return stationManager;
    }

    private List<ArrowedEdge> createEdges(CoordinatesManager coordinatesManager, List<Road> roads) {
        List<ArrowedEdge> edges = new LinkedList<>();
        this.road2Edge = new HashMap<>();
        roads.forEach(road -> {
            int fromX = road.getStartStation().getCoordinate().getX();
            int fromY = road.getStartStation().getCoordinate().getY();
            int toX = road.getEndStation().getCoordinate().getX();
            int toY = road.getEndStation().getCoordinate().getY();
            ArrowedEdge e = new ArrowedEdge(coordinatesManager.getOrCreate(fromX, fromY), coordinatesManager.getOrCreate(toX, toY));
//            e.textProperty().set(String.format("%s KM", road.getLengthInKM()));
            this.road2Edge.put(road, e);
            edges.add(e);


        });

        return edges;

    }


    public void markRoadsInRed(List<Road> roadsToMark){
        List<ArrowedEdge>  edgesToMark = roadsToMark.stream()
                .map(rode -> this.road2Edge.get(rode))
                .collect(Collectors.toList());
        updateEdgesWithSelectedStyle(edgesToMark);
    }

    public void markRoadsInBlue(List<Road> roadsToMark){
        List<ArrowedEdge>  edgesToMark = roadsToMark.stream()
                .map(rode -> this.road2Edge.get(rode))
                .collect(Collectors.toList());

        updateEdgesWithBlueColor(edgesToMark);
    }

    public void hideRoads(List<Road> roadsToHide){
        List<ArrowedEdge>  edgesToHide = roadsToHide.stream()
                .map(rode -> this.road2Edge.get(rode))
                .collect(Collectors.toList());
        hideArrow(edgesToHide);
        hideLines(edgesToHide);
    }

    public void unMarkRedMarkedEdges() {
        if (this.markedRedEdges != null) {
            hideArrow(this.markedRedEdges);
            showRegularLines(this.markedRedEdges);
            this.markedRedEdges = null;
        }
    }

    public void unMarkBlueMarkedEdges() {
        if (this.markedBlueEdges != null) {
            hideArrow(this.markedBlueEdges);
            showRegularLines(this.markedBlueEdges);
            this.markedBlueEdges = null;
        }
    }

    public void showArrow(List<ArrowedEdge> lines){
        Platform.runLater(() -> lines.forEach(line -> {
            line.getArrowHead().getStyleClass().clear();
            line.getArrowHead().getStyleClass().add("red-arrow-head");
        }));
    }

    public void showBlueArrow(List<ArrowedEdge> lines){
        Platform.runLater(() -> lines.forEach(line -> {
            line.getArrowHead().getStyleClass().clear();
            line.getArrowHead().getStyleClass().add("blue-arrow-head");
        }));
    }

    public void hideArrow(List<ArrowedEdge> lines){
        Platform.runLater(() -> lines.forEach(line -> {
            line.getArrowHead().getStyleClass().clear();
            line.getArrowHead().getStyleClass().add("transparent-arrow-head");
        }));
    }

    public void showRegularLines(List<ArrowedEdge> lines){
        Platform.runLater(() -> lines.forEach(line -> {
            line.getLine().getStyleClass().clear();
            line.getLine().getStyleClass().add("road-line");
        }));
    }
    public void markEdgesInRed(List<ArrowedEdge> lines){
        Platform.runLater(() -> lines.forEach(line -> {
            line.getLine().getStyleClass().clear();
            line.getLine().getStyleClass().add("red-road-line");
        }));
    }

    public void markEdgesInBlue(List<ArrowedEdge> lines){
        Platform.runLater(() -> lines.forEach(line -> {
            line.getLine().getStyleClass().clear();
            line.getLine().getStyleClass().add("blue-road-line");
        }));
    }

    public void hideLines(List<ArrowedEdge> lines){
        Platform.runLater(() -> lines.forEach(line -> {
            line.getLine().getStyleClass().clear();
            line.getLine().getStyleClass().add("hidden-road");
        }));
    }

    public void removeTextFromEdges(){
        if (this.edgesWithText != null) {
            this.edgesWithText.forEach(edge -> edge.textProperty().set(null));
            this.edgesWithText = null;
        }
    }
    public void updateEdgesWithTexts(Map<Road, String> roadsWithStatus) {
        roadsWithStatus.forEach(((road, s) -> setTextToEdge(this.road2Edge.get(road), s)));
    }

    private void setTextToEdge(ArrowedEdge edgeToUpdate, String s) {
        if (this.edgesWithText == null)
            this.edgesWithText = new LinkedList<>();

        edgeToUpdate.textProperty().set(s);
        this.edgesWithText.add(edgeToUpdate);
    }

    public void markStations(Station startStation, Station endStation) {
        markStation(startStation, "start-station");
        markStation(endStation, "end-station");
    }
    private List<Node> markedStations;

    private void markStation(Station station, String styleClass){
        StationNode stationNode = this.stationManager.getOrCreate(station.getCoordinate().getX(), station.getCoordinate().getY());
        Node node = stationNode.getRoot().getChildren().get(0);
        node.getStyleClass().clear();
        node.getStyleClass().add(styleClass);
        if(markedStations == null)
            markedStations = new LinkedList<>();

        this.markedStations.add(node);
    }

    private void unMarkNode(Node node){
        node.getStyleClass().clear();
        node.getStyleClass().add("station-circle");
    }
    public void unMarkAllStations(){
        if(this.markedStations != null) {
            this.markedStations.forEach(this::unMarkNode);
            this.markedStations = null;
        }

    }
}