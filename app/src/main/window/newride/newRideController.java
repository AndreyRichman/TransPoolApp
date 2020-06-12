package main.window.newride;

import enums.RepeatType;
import exception.NoRoadBetweenStationsException;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.window.main.MainWindowController;
import transpool.logic.handler.LogicHandler;
import transpool.logic.map.structure.Road;
import transpool.logic.map.structure.Station;
import transpool.logic.traffic.item.Ride;
import transpool.logic.user.User;
import transpool.ui.request.type.NewRideRequest;
import java.awt.event.MouseEvent;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class newRideController {

    private Stage stage;
    MainWindowController mainController;
    private LogicHandler logicHandler;
    private NewRideRequest request;
    private ObservableList stationsNames;
    private List<String> stationsNamesWithRoad;
    private List<String> path;
    private String currentStation;
    private SimpleStringProperty pathProperty;


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
    private ChoiceBox<Integer> hourChoiceBox;

    @FXML
    private ChoiceBox<Integer> minutesChoiceBox;

    @FXML
    private TextField ppkTextField;

    @FXML
    private TextField capacityTextField;

    @FXML
    private ChoiceBox<RepeatType> reputabelChoiceBox;

    @FXML
    private Label pathLabel;

    @FXML
    void onClickCancelButton(ActionEvent event) {
        stage.close();
    }

    @FXML
    void onClickCreateBtn(ActionEvent event) throws NoRoadBetweenStationsException {

        initNewRideRequest();

        logicHandler.addRide(createNewRideFromRequest(request));

    }

    private void initNewRideRequest() {
        request.setCarCapacity(Integer.parseInt(capacityTextField.getText()));
        request.setPricePerKilometer(Integer.parseInt(ppkTextField.getText()));
        request.setStations(path);
        request.setUserName(userNameTextField.getText());
        request.setRepeatType(reputabelChoiceBox.getValue());
        request.setStartTime(LocalTime.of(hourChoiceBox.getValue(), minutesChoiceBox.getValue()));
    }

    @FXML
    void onFromStationSelected(MouseEvent event) {

    }

    @FXML
    void onToStationSelected(MouseEvent event) {

    }

    private Ride createNewRideFromRequest(NewRideRequest request) throws NoRoadBetweenStationsException {
        List<Road> roads = logicHandler.getRoadsFromStationsNames(request.getStations());
        User user = logicHandler.getUserByName(request.getUserName());
        Ride newRide = logicHandler.createNewEmptyRide(user, roads, request.getCarCapacity());

        //TODO: get day from user (default = 1)
        newRide.setSchedule(request.getStartTime(), request.getDay(), request.getRepeatType());
        newRide.setPricePerKilometer(request.getPricePerKilometer());

        return newRide;
    }

    public void addStationChoiceBoxListener(){
        // this is that when you choose a station the choicebox will delete all the stations without a road

        this.addStationChoiceBox.getSelectionModel().selectedItemProperty().addListener((v, oldStation, newStation) ->
        {

            //set Path label
            path.add(newStation);
            pathProperty.set(String.join("->", path));

            //get all the stations with road from newStation
            //TODO: I NEED YOUR HELP HERE. need to get all station( as List<String> ) with road from newStation
            //stationsNamesWithRoad.addAll(logicHandler.getRoadsFromStationsNames(Arrays.asList(newStation)).stream().map(Road::getEndStation).map(Station::getName).collect(Collectors.toList()));

            this.addStationChoiceBox.getItems().clear();
            this.addStationChoiceBox.getItems().addAll(stationsNamesWithRoad);
        });

        this.removeStationChoiceBox.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) ->
        {
            path.remove(newValue);
            pathProperty.set(String.join("->", path));
        });

    }

    public newRideController(){
        request = new NewRideRequest();
        addStationChoiceBox = new ChoiceBox<>();
        removeStationChoiceBox = new ChoiceBox<>();
        stationsNames = FXCollections.observableArrayList();
        stationsNamesWithRoad = new ArrayList<>();
        pathProperty = new SimpleStringProperty();
        path = new ArrayList<>();
    }

    @FXML
    public void initialize() {
        Platform.runLater(this::initStations);

        pathLabel.textProperty().bind(pathProperty);

        initdaySpinner();

        initHourAndMin();

        initRepeat();
    }

    private void initRepeat() {

        reputabelChoiceBox.getItems().add(RepeatType.ONE_TIME);
        reputabelChoiceBox.getItems().add(RepeatType.DAILY);
        reputabelChoiceBox.getItems().add(RepeatType.BIDAIILY);
        reputabelChoiceBox.getItems().add(RepeatType.WEEKLY);
        reputabelChoiceBox.getItems().add(RepeatType.MONTHLY);

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

        addStationChoiceBoxListener();
    }

    public void setLogicHandler(LogicHandler logicHandler) { this.logicHandler = logicHandler;  }

    public void setStage(Stage stage) { this.stage = stage;  }

    public void setMainController(MainWindowController mainController) { this.mainController = mainController;  }

}
