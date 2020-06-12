package main.window.map.example;

import com.fxgraph.graph.Graph;
import com.fxgraph.graph.Model;
import com.fxgraph.graph.PannableCanvas;
import main.window.map.component.road.ArrowedEdge;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import main.window.map.component.coordinate.CoordinateNode;
import main.window.map.component.coordinate.CoordinatesManager;
import main.window.map.component.details.StationDetailsDTO;
import main.window.map.component.station.StationManager;
import main.window.map.component.station.StationNode;
import main.window.map.layout.MapGridLayout;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SampleMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Graph graphMap = new Graph();
        createMap(graphMap);

        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("main.fxml");
        fxmlLoader.setLocation(url);
        GridPane root = fxmlLoader.load(url.openStream());

        final Scene scene = new Scene(root, 700, 400);

        ScrollPane scrollPane = (ScrollPane) scene.lookup("#scrollpaneContainer");
        PannableCanvas canvas = graphMap.getCanvas();
        scrollPane.setContent(canvas);

        primaryStage.setScene(scene);
        primaryStage.show();

        Platform.runLater(() -> {
            graphMap.getUseViewportGestures().set(false);
            graphMap.getUseNodeGestures().set(false);
        });

    }

    // Creating the graph is based on FXGraph 3rd party library.
    // See: https://github.com/sirolf2009/fxgraph for more details
    private void createMap(Graph graph) {

        final Model model = graph.getModel();
        graph.beginUpdate();

        StationManager sm = createStations(model);
        CoordinatesManager cm = createCoordinates(model);
        createEdges(model, cm);

        graph.endUpdate();

        graph.layout(new MapGridLayout(cm, sm, 0));
    }

    private StationManager createStations(Model model) {
        StationManager sm = new StationManager(StationNode::new);

        StationNode station = sm.getOrCreate(2, 2);
        station.setName("This is a test for long string");
        station.setDetailsSupplier(() -> {
            List<String> trips = new ArrayList<>();
            trips.add("Mosh");
            trips.add("Menash");
            return new StationDetailsDTO(trips);
        });
        model.addCell(station);

        station = sm.getOrCreate(5, 5);
        station.setName("B");
        station.setDetailsSupplier(() -> {
            List<String> trips = new ArrayList<>();
            return new StationDetailsDTO(trips);
        });
        model.addCell(station);

        station = sm.getOrCreate(7, 9);
        station.setName("C");
        station.setDetailsSupplier(() -> {
            List<String> trips = new ArrayList<>();
            trips.add("Mosh");
            trips.add("Menash");
            trips.add("Tikva");
            trips.add("Mazal");
            return new StationDetailsDTO(trips);
        });
        model.addCell(station);

        station = sm.getOrCreate(4, 6);
        station.setName("D");
        station.setDetailsSupplier(() -> {
            List<String> trips = new ArrayList<>();
            trips.add("Mazal");
            return new StationDetailsDTO(trips);
        });
        model.addCell(station);

        return sm;
    }

    private CoordinatesManager createCoordinates(Model model) {

        CoordinatesManager cm = new CoordinatesManager(CoordinateNode::new);

//        for (int i=0; i<5; i++) {
//            for (int j = 0; j < 5; j++) {
//                model.addCell(cm.getOrCreate(i+1, j+1));
//            }
//        }

        return cm;
    }

    private void createEdges(Model model, CoordinatesManager cm) {
        ArrowedEdge e13 = new ArrowedEdge(cm.getOrCreate(2,2), cm.getOrCreate(1,4));
        e13.textProperty().set("L: 7 ; FC: 4");
        model.addEdge(e13); // 1-3

        ArrowedEdge e34 = new ArrowedEdge(cm.getOrCreate(7,9), cm.getOrCreate(4,6));
        e34.textProperty().set("L: 12 ; FC: 14");
        model.addEdge(e34); // 3-4

        ArrowedEdge e23 = new ArrowedEdge(cm.getOrCreate(5,5), cm.getOrCreate(7,9));
        e23.textProperty().set("L: 4 ; FC: 10");
        model.addEdge(e23); // 2-3

        Platform.runLater(() -> {
            e13.getLine().getStyleClass().add("line1");
            e13.getText().getStyleClass().add("edge-text");

            e34.getLine().getStyleClass().add("line2");
            e34.getText().getStyleClass().add("edge-text");

            e23.getLine().getStyleClass().add("line3");
            e23.getText().getStyleClass().add("edge-text");

            //moveAllEdgesToTheFront(graph);
        });

    }

    private void moveAllEdgesToTheFront(Graph graph) {

        List<Node> onlyEdges = new ArrayList<>();

        // finds all edge nodes and remove them from the beginning of list
        ObservableList<Node> nodes = graph.getCanvas().getChildren();
        while (nodes.get(0).getClass().getSimpleName().equals("EdgeGraphic")) {
            onlyEdges.add(nodes.remove(0));
        }

        // adds them as last ones
        nodes.addAll(onlyEdges);
    }
}
