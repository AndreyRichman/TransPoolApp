package main.window.main.sub.tremp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import main.window.main.MainWindowController;
import transpool.logic.traffic.item.Ride;
import transpool.logic.traffic.item.TrempRequest;

import java.util.List;
import java.util.stream.Collectors;

public class TrempSubWindowController {

    MainWindowController mainController;
    @FXML
    private Label userTitle;

    @FXML
    private Label fromStationTitle;

    @FXML
    private Label toStationTitle;

    @FXML
    private Label wantToTitle;

    @FXML
    private Label atTimeTitle;

    @FXML
    private Label statusTitle;

    @FXML
    private Label userValue;

    @FXML
    private Label toStationValue;

    @FXML
    private Label wantToValue;

    @FXML
    private Label atTimeValue;

    @FXML
    private Label statusValue;

    @FXML
    private AnchorPane allTrempsScrollPane;

    @FXML
    private Button showTrempDetails;

    @FXML
    private ListView<String> trempsListView;

    @FXML
    void onClickShowTrempDetails(ActionEvent event) {
    }

    @FXML
    void onMouseClickedScrollPane(MouseEvent event) {

    }

    public void setMainController(MainWindowController mainController) {
        this.mainController = mainController;
    }

    public void updateRidesList(){
        List<TrempRequest> allTremps = this.mainController.getAllTrempRequests();
        List<String> trempRepr = allTremps.stream()
                .map(tremp -> String.format("%d - %s",
                        tremp.getID(),
                        tremp.getUser().getName()))
                .collect(Collectors.toList());

        this.trempsListView.getItems().addAll(trempRepr);
    }
}
