package main.window.newride;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.window.main.MainWindowController;
import transpool.logic.handler.LogicHandler;
import transpool.logic.map.structure.Station;
import transpool.ui.request.type.NewRideRequest;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class newRideController {

    private Stage stage;
    MainWindowController mainController;
    private LogicHandler logicHandler;
    private NewRideRequest request;
    private ObservableList stationsNames;
    private List<String> path;
    private SimpleStringProperty selectedFileProperty;


    @FXML
    private Button createBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private TextArea pathTextArea;

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
    private Label pathLabel;

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

    public void updateStationList(){
        this.addStationChoiceBox.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) ->
        {
            path.add(newValue);
            selectedFileProperty.set(String.join("->", path));
        });

        this.removeStationChoiceBox.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) ->
        {
            path.remove(newValue);
            selectedFileProperty.set(String.join("->", path));
        });

    }

    public newRideController(){
        request = new NewRideRequest();
        addStationChoiceBox = new ChoiceBox<>();
        removeStationChoiceBox = new ChoiceBox<>();
        stationsNames = FXCollections.observableArrayList();
        selectedFileProperty = new SimpleStringProperty();
        path = new ArrayList<>();
    }

    @FXML
    public void initialize() {
        Platform.runLater(this::initStations);

        pathLabel.textProperty().bind(selectedFileProperty);

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

        updateStationList();
    }

    public void setLogicHandler(LogicHandler logicHandler) { this.logicHandler = logicHandler;  }

    public void setStage(Stage stage) { this.stage = stage;  }

    public void setMainController(MainWindowController mainController) { this.mainController = mainController;  }

}
