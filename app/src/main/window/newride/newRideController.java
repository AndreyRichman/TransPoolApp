package main.window.newride;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.window.main.MainWindowController;
import transpool.logic.handler.LogicHandler;

import java.awt.event.MouseEvent;

public class newRideController {

    private Stage stage;
    MainWindowController mainController;
    private LogicHandler logicHandler;

    @FXML
    private Button createBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private Label Reputabel;

    @FXML
    private TextField userNameTextField;

    @FXML
    private ChoiceBox<?> addStationChoiceBox;

    @FXML
    private ChoiceBox<?> removeStationChoiceBox;

    @FXML
    private Spinner<?> daySpinner;

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

    public void setLogicHandler(LogicHandler logicHandler) { this.logicHandler = logicHandler;  }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setMainController(MainWindowController mainController) { this.mainController = mainController;  }

}
