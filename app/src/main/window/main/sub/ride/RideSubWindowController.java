package main.window.main.sub.ride;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import main.window.main.MainWindowController;

public class RideSubWindowController {

    MainWindowController mainController;

    @FXML
    private Label userTitle;

    @FXML
    private Label departTitle;

    @FXML
    private Label ArriveTitle;

    @FXML
    private Label ppkTitle;

    @FXML
    private Label fuelTitle;

    @FXML
    private Label userValue;

    @FXML
    private Label departValue;

    @FXML
    private Label arriveValue;

    @FXML
    private Label ppkValue;

    @FXML
    private Label fuelValue;

    public void setMainController(MainWindowController mainController) {
        this.mainController = mainController;
    }
}
