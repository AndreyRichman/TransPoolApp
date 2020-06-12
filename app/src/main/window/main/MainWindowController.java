package main.window.main;

        import exception.FaildLoadingXMLFileException;
        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.fxml.FXMLLoader;
        import javafx.scene.Parent;
        import javafx.scene.Scene;
        import javafx.scene.control.Button;
        import javafx.scene.layout.GridPane;
        import javafx.scene.layout.Pane;
        import javafx.stage.Modality;
        import javafx.stage.Stage;
        import main.window.main.sub.map.DynamicMapController;
        import main.window.main.sub.ride.RideSubWindowController;
        import main.window.main.sub.tremp.TrempSubWindowController;
        import main.window.newride.newRideController;
        import main.window.newtremp.CreateTrempController;
        import main.window.newxmlload.newXmlLoadController;
        import transpool.logic.handler.LogicHandler;
        import transpool.logic.map.structure.Road;
        import transpool.logic.traffic.item.PartOfRide;
        import transpool.logic.traffic.item.Ride;
        import transpool.logic.traffic.item.TrempRequest;

        import java.io.IOException;
        import java.net.URL;
        import java.util.LinkedList;
        import java.util.List;
        import java.util.stream.Collectors;


public class MainWindowController {

    private LogicHandler logicHandler;
    private Stage primaryStage;

    @FXML private Pane rideComponent;
    @FXML private Pane trempComponent;
    @FXML private Pane mapComponent;
    @FXML private RideSubWindowController rideComponentController;
    @FXML private TrempSubWindowController trempComponentController;
    @FXML private DynamicMapController mapComponentController;

    @FXML
    private Button loadXmlBtn;

    @FXML
    private Button newBtn;

    @FXML
    private Button newBtn1;

    @FXML
    private Button matchBtn;

    @FXML
    private Button matchBtn1;

    @FXML
    void onLoadXmlBtnClick(ActionEvent event) throws IOException {
        loadXmlFile();
    }

    public void loadXmlFile() throws IOException {
        Stage stage = new Stage();
        stage.setResizable(false);
        URL resource = getClass().getResource("../newxmlload/newXmlLoadWindow.fxml");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(resource);
        Parent root = loader.load();
        newXmlLoadController controller = loader.getController();
        controller.setMainController(this);
        controller.setStage(stage);
        controller.setLogicHandler(logicHandler);
        Scene scene = new Scene(root, 400, 390);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.show();
    }


    public void createNewRide() throws IOException{
        Stage stage = new Stage();
        stage.setResizable(false);
        URL resource = getClass().getResource("../newride/newRideWindow.fxml");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(resource);
        Parent root = loader.load();
        newRideController controller = loader.getController();
        controller.setMainController(this);
        controller.setStage(stage);
        controller.setLogicHandler(logicHandler);
        Scene scene = new Scene(root, 500, 700);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void onQuitBtnClick(ActionEvent event) { primaryStage.close(); }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setLogic(LogicHandler logicHandler) { this.logicHandler = logicHandler;  }

    @FXML
    public void initialize(){
        if (rideComponentController != null && this.trempComponentController != null && mapComponentController != null){
            this.rideComponentController.setMainController(this);
            this.trempComponentController.setMainController(this);
            this.mapComponentController.setMainController(this);
        }
    }

    public void updateMap(){
        mapComponentController.initVisualMap(this.logicHandler.getMap());
    }

    public void updateRidesList(){
        this.rideComponentController.updateRidesList();
    }

    public void updateMapWithRide(Ride ride){
        List<Road> roadsToMark = ride.getPartsOfRide().stream().map(PartOfRide::getRoad).collect(Collectors.toList());
        this.mapComponentController.markRoads(roadsToMark); //markRoads()

        List<Road> roadsToHide = new LinkedList<>();
        for (Road toHide: this.logicHandler.getAllRoads()){
            for (Road toMark: roadsToMark){
                if (toHide.sharesOppositeStations(toMark))
                    roadsToHide.add(toHide);
            }
        }

        this.mapComponentController.hideRoads(roadsToHide);
    }

    public void updateTrempsList(){
        this.trempComponentController.updateRidesList();
    }

    public void createNewTremp() throws IOException {
        Stage stage = new Stage();
        stage.setResizable(false);
        URL resource = getClass().getResource("../newtremp/newTrempWindow.fxml");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(resource);
        Parent root = loader.load();
        CreateTrempController controller = loader.getController();
        controller.setMainController(this);
        controller.setStage(stage);
        controller.setLogicHandler(logicHandler);
        Scene scene = new Scene(root, 400, 390);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.show();
    }




    public List<Ride> getAllRides() {
        return this.logicHandler.getAllRides();
    }

    public List<TrempRequest> getAllTrempRequests(){
        return this.logicHandler.getAllTrempRequests();
    }
}
