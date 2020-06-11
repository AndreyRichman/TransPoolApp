package main.window.newride;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.window.main.MainWindowController;
import transpool.logic.handler.LogicHandler;
import transpool.logic.map.structure.Station;
import transpool.ui.request.type.NewRideRequest;
import transpool.ui.request.type.NewTrempRequest;

import java.awt.event.MouseEvent;
import java.util.stream.Collectors;

public class newRideController {

    private Stage stage;
    MainWindowController mainController;
    private LogicHandler logicHandler;
    private NewRideRequest request;
    private ObservableList stationsNames;
    private ObservableList path;


    @FXML
    private Button createBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private Label Reputabel;

    @FXML
    private TextField userNameTextField;

    @FXML
    private ChoiceBox<String> addStationChoiceBox;

    @FXML
    private ChoiceBox<String> removeStationChoiceBox;

    @FXML
    private Spinner<Integer> daySpinner;

    @FXML
    private ChoiceBox<?> hourChoiceBox;

    @FXML
    private ChoiceBox<?> minutesChoiceBox;

    @FXML
    private TextField ppkTextField;

    @FXML
    private TextField capacityTextField;

    @FXML
    private ChoiceBox<?> reputabelChoiceBox;

    @FXML
    void onClickCancelButton(ActionEvent event) {
        stage.close();
    }

    @FXML
    void onClickCreateBtn(ActionEvent event) {

    }

    @FXML
    void onFromStationSelected(MouseEvent event) {

    }

    @FXML
    void onToStationSelected(MouseEvent event) {

    }

    public newRideController(){
        request = new NewRideRequest();
        addStationChoiceBox = new ChoiceBox<>();
        removeStationChoiceBox = new ChoiceBox<>();
        stationsNames = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        Platform.runLater(this::initStations);

        initdaySpinner();

        initHourAndMin();
    }

    private void initHourAndMin() {
        ObservableList min = FXCollections.observableArrayList();
        ObservableList hour = FXCollections.observableArrayList();

        for(int i=5; i<=60; i+=5) {min.add(i);}
        for(int i=1; i<=24; i++) {hour.add(i);}

        hourChoiceBox.getItems().addAll(hour);
        minutesChoiceBox.getItems().addAll(min);
    }

    private void initdaySpinner() {
        SpinnerValueFactory<Integer> spinerFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,100,1);
        this.daySpinner.setValueFactory(spinerFactory);
    }

    private void initStations() {
        stationsNames.addAll(logicHandler.getAllStations().stream().map(Station::getName).collect(Collectors.toList()));
        this.addStationChoiceBox.getItems().addAll(stationsNames);
        this.removeStationChoiceBox.getItems().addAll(stationsNames);
    }

    public void setLogicHandler(LogicHandler logicHandler) { this.logicHandler = logicHandler;  }

    public void setStage(Stage stage) { this.stage = stage;  }

    public void setMainController(MainWindowController mainController) { this.mainController = mainController;  }

}
