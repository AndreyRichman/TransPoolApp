package main.window.newtremp;

import enums.DesiredTimeType;
import exception.NoPathExistBetweenStationsException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import main.window.main.MainWindowController;
import transpool.logic.handler.LogicHandler;
import transpool.logic.map.structure.Station;
import transpool.logic.traffic.item.TrempRequest;
import transpool.ui.request.type.NewTrempRequest;

import java.time.LocalTime;
import java.util.stream.Collectors;


public class CreateTrempController {

    private MainWindowController mainController;
    private LogicHandler logicHandler;
    private NewTrempRequest request;
    private ObservableList stationsNames;
    private Stage stage;

    @FXML
    private Button createBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private TextField userNameTextField;

    @FXML
    private ChoiceBox<String> fromStationChoiceBox;

    @FXML
    private ChoiceBox<String> toStationChoiceBox;

    @FXML
    private Spinner<Integer> daySpinner;

    @FXML
    private ChoiceBox<Integer> hourChoiceBox;

    @FXML
    private ChoiceBox<Integer> minutesChoiceBox;

    @FXML
    private ChoiceBox<String> searchByChoiceBox;

    @FXML
    private TextField timeDeviation;

    @FXML
    private Label timeDeviationLabel;

    @FXML
    void onClickCancelButton(ActionEvent event) {
        stage.close();
    }

    @FXML
    void onClickCreateBtn(ActionEvent event) {
        //set request attributes
        request.setUserName(userNameTextField.getText());
        String minutes = minutesChoiceBox.getValue() < 10 ? String.format("0%d", minutesChoiceBox.getValue()) : minutesChoiceBox.getValue().toString();
        String hours = hourChoiceBox.getValue() < 10 ? String.format("0%d", hourChoiceBox.getValue()) : hourChoiceBox.getValue().toString();
        request.setChosenTime(String.format("%s:%s", hours, minutes));
        request.setFromStation(fromStationChoiceBox.getValue());
        request.setToStation(toStationChoiceBox.getValue());
        request.setDepartDay(daySpinner.getValue());
        if (searchByChoiceBox.getValue().equals("DEPART")) {  request.setDesiredTimeType(DesiredTimeType.DEPART.toString()); }
        else { request.setDesiredTimeType(DesiredTimeType.ARRIVE.toString());  }
        if(vaildTimeDeviation()){
            timeDeviationLabel.setTextFill(Color.WHITE);
            addNewTrempFromRequest(request);
            this.mainController.updateTrempsList();
            this.stage.close();
        }
        else
        {
            timeDeviationLabel.setTextFill(Color.RED);
            timeDeviation.setText("");
            timeDeviation.setPromptText("please enter num > 0");
        }

    }

    private boolean vaildTimeDeviation() {
        if(!timeDeviation.getText().equals("")){
            if (Integer.parseInt(timeDeviation.getText()) > 0)
                return true;
        }
        return false;
    }

    @FXML
    void onFromStationSelected(MouseEvent event) {

    }

    @FXML
    void onToStationSelected(MouseEvent event) {

    }

    public CreateTrempController(){
        request = new NewTrempRequest();
        fromStationChoiceBox = new ChoiceBox<>();
        toStationChoiceBox = new ChoiceBox<>();
        searchByChoiceBox = new ChoiceBox<>();
        stationsNames = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        Platform.runLater(this::initStations);

        initdaySpinner();

        initHourAndMin();

        initChoiceBox();
    }

    private void initChoiceBox() {
        searchByChoiceBox.getItems().add("DEPART");
        searchByChoiceBox.getItems().add("ARRIVE");
    }

    private void initHourAndMin() {
        ObservableList min = FXCollections.observableArrayList();
        ObservableList hour = FXCollections.observableArrayList();

        for(int i=0; i<=60; i+=5) {min.add(i);}
        for(int i=0; i<=23; i++) {hour.add(i);}

        hourChoiceBox.getItems().addAll(hour);
        minutesChoiceBox.getItems().addAll(min);
    }

    private void initdaySpinner() {
        SpinnerValueFactory<Integer> spinerFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,100,1);
        this.daySpinner.setValueFactory(spinerFactory);
    }

    private void initStations() {
        stationsNames.addAll(logicHandler.getAllStations().stream().map(Station::getName).collect(Collectors.toList()));
        this.fromStationChoiceBox.getItems().addAll(stationsNames);
        this.toStationChoiceBox.getItems().addAll(stationsNames);
    }



    @FXML
    void onClickDepChoise(ActionEvent event) {

    }

    @FXML
    void onClickArrChoise(ActionEvent event) {

    }

    public void addNewTrempFromRequest(NewTrempRequest request)  {
        Station fromStation = logicHandler.getStationFromName(request.getFromStation());
        Station toStation = logicHandler.getStationFromName(request.getToStation());

        TrempRequest newTrempRequest = null;
        try {
            newTrempRequest = logicHandler.createNewEmptyTrempRequest(fromStation, toStation);

            newTrempRequest.setUser(logicHandler.getUserByName(request.getUserName()));
            DesiredTimeType desiredTimeType = DesiredTimeType.valueOf(request.getDesiredTimeType());

            LocalTime desiredTime = LocalTime.parse(request.getChosenTime());
            int onDay = request.getDepartDay();
            newTrempRequest.setDesiredDayAndTime(onDay, desiredTime, desiredTimeType);
            newTrempRequest.setMaxNumberOfConnections(Integer.parseInt(timeDeviation.getText()));
            logicHandler.addTrempRequest(newTrempRequest);

        }  catch (NoPathExistBetweenStationsException e) { e.printStackTrace();  }
    }

    public void setLogicHandler(LogicHandler logicHandler) { this.logicHandler = logicHandler;  }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setMainController(MainWindowController mainController) { this.mainController = mainController;  }

}







