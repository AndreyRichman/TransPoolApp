package main.window.main.sub.map;

import com.fxgraph.graph.Graph;
import com.fxgraph.graph.Model;
import com.fxgraph.graph.PannableCanvas;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
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

import java.util.*;
import java.util.stream.Collectors;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;



public class DynamicMapController {

    private final int MIN_BOARD_SCALE = 10;
    private final int MAX_BOARD_SCALE = 100;
    private int currentBoardScale = 30;

    MainWindowController mainController;
    private Graph graphMap;
    private CoordinatesManager coordinatesManager;
    private StationManager stationManager;
    private List<ArrowedEdge>  allEdges;
    private List<ArrowedEdge>  markedEdges;

    Map<Road, ArrowedEdge> road2Edge;


    @FXML
    private ScrollPane mapScrollPane;

    @FXML
    private Button zoomPlusButton;

    @FXML
    private Button zoomMinusButton;

    @FXML
    private TextField timeNumberToAddTextBox;

    @FXML
    private ChoiceBox<?> addTimeTypeChooseBox;

    @FXML
    private Button updateMapBtn;

    @FXML
    void onTimeTypeSelected(MouseEvent event) {

    }
    @FXML
    void onClickUpdateMapBtn(ActionEvent event) {

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
        updateEdgesWithClass(this.allEdges, "road-line", "transparent-arrow-head");
//        updateEdgesUIStyle(this.allEdges);

        this.graphMap.endUpdate();
//        graph.layout(new MapGridLayout(coordinatesManager, stationManager));
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

    private void updateEdgesUIStyle(List<ArrowedEdge> edges) {
        Platform.runLater(() -> {
            edges.forEach(edge -> {
                edge.getLine().getStyleClass().clear();
                edge.getLine().getStyleClass().add("road-line");
                //edge.getText().getStyleClass().add("edge-text");
            });
        });
    }

    private void updateEdgesWithSelectedStyle(List<ArrowedEdge> specialEdges) {
        if (this.markedEdges != null) {
            updateEdgesWithClass(this.markedEdges, "road-line", "transparent-arrow-head");
            //updateEdgesUIStyle(this.allEdges);
        }
            //updateEdgesUIStyle(this.markedEdges);

        this.markedEdges = specialEdges;

        updateEdgesWithClass(specialEdges, "selected-road-line", "red-arrow-head");
//        Platform.runLater(() -> {
//            specialEdges.forEach(edge -> {
//                edge.getLine().getStyleClass().clear();
//                edge.getLine().getStyleClass().add("selected-road-line");
//                //edge.getText().getStyleClass().add("edge-text");    //TODO show number of trempists
//            });
//        });
    }

    private void updateEdgesWithClass(List<ArrowedEdge> specialEdges, String lineStyleClass, String arrowStyleClass){
        Platform.runLater(() -> {
            specialEdges.forEach(edge -> {
                edge.getLine().getStyleClass().clear();
                edge.getLine().getStyleClass().add(lineStyleClass);
                if (arrowStyleClass != null) {
                    edge.getArrowHead().getStyleClass().clear();
                    edge.getArrowHead().getStyleClass().add(arrowStyleClass);
                }

            });
        });
    }

    private StationManager loadStations(Model graphModel, List<Station> allStations) {
        StationManager stationManager = new StationManager(StationNode::new);

        allStations.forEach(station -> {
            StationNode stationNode = stationManager.getOrCreate(station.getCoordinate().getX(), station.getCoordinate().getY());
            stationNode.setName(station.getName());
            graphModel.addCell(stationNode);

            stationNode.setDetailsSupplier(() -> {
                List<String> trips = new ArrayList<>();
//                trips.add("Mosh");
//                trips.add("Menash");
//                trips.add("Tikva");
//                trips.add("Mazal");
                return new StationDetailsDTO(trips);
            });
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

    public void updateTextOfEdge(ArrowedEdge edgeToUpdate, String setText){
        edgeToUpdate.textProperty().set(setText);
    }


    public void markRoads(List<Road> roadsToMark){
        List<ArrowedEdge>  edgesToMark = roadsToMark.stream()
                .map(rode -> this.road2Edge.get(rode))
                .collect(Collectors.toList());
        ;//createEdges(this.coordinatesManager, roadsToMark);
        updateEdgesWithSelectedStyle(edgesToMark);

    }

    public void hideRoads(List<Road> roadsToHide){
        List<ArrowedEdge>  edgesToHide = roadsToHide.stream()
                .map(rode -> this.road2Edge.get(rode))
                .collect(Collectors.toList());
        updateEdgesWithClass(edgesToHide, "hidden-road", "transparent-arrow-head");
    }
}
