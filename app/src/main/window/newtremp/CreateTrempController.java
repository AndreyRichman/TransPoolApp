package main.window.newtremp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import main.window.main.MainWindowController;
import transpool.ui.request.type.NewTrempRequest;

import java.util.ArrayList;
import java.util.List;

public class CreateTrempController {

    MainWindowController mainController;

    @FXML
    private Button createBtn;

    @FXML
    private Button cancelBtn;

    @FXML
    private TextField userNameTextField;

    @FXML
    private ChoiceBox<?> fromStationChoiceBox;

    @FXML
    private ChoiceBox<?> toStationChoiceBox;

    @FXML
    void onClickCancelButton(ActionEvent event) {

    }

    @FXML
    void onClickCreateBtn(ActionEvent event) {
        NewTrempRequest request = new NewTrempRequest();
        request.setUserName(userNameTextField.getText());

        this.mainController.addNewTrempFromRequest(request);
    }

    @FXML
    void onFromStationSelected(MouseEvent event) {

    }

    @FXML
    void onToStationSelected(MouseEvent event) {

    }

    public void setMainController(MainWindowController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void initialize() {
        List<String> stationsNames = new ArrayList<>();
        stationsNames.add("Test1");
        stationsNames.add("Test2");
        ObservableList options = FXCollections.observableList(stationsNames);

        this.fromStationChoiceBox.setItems(options);
    }

}







