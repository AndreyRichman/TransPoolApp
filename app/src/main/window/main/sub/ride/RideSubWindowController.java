package main.window.main.sub.ride;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import main.window.main.MainWindowController;
import transpool.logic.traffic.item.Ride;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RideSubWindowController {

    MainWindowController mainController;
    Map<Integer, Ride> ridesVisibleInView;

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

    @FXML
    private ListView<String> ridesListView;


    @FXML
    private Button addNewRideBtn;

    @FXML
    void onAddNewRideBtnClick(ActionEvent event) throws IOException {
        this.mainController.createNewRide();

    }
    public void setMainController(MainWindowController mainController) {
        this.mainController = mainController;
    }

    public void updateRidesList(){
        this.ridesVisibleInView = new HashMap<>();
        int index = 0;
        List<Ride> allRides = this.mainController.getAllRides();
        for(Ride ride : allRides) {
            String toSHow = String.format("%d - %s",
                        ride.getID(),
                        ride.getRideOwner().getUser().getName());
            this.ridesListView.getItems().add(toSHow);
            ridesVisibleInView.put(index++, ride);
        }

//        List<String> ridesRepresentations = allRides.stream()
//                .map(ride -> String.format("%d - %s",
//                        ride.getID(),
//                        ride.getRideOwner().getUser().getName()))
//                .collect(Collectors.toList());
//
//        this.ridesListView.getItems().addAll(ridesRepresentations);
    }

    @FXML
    void onRideSelected(MouseEvent event) {
        if (this.mainController != null){
            int index = this.ridesListView.getSelectionModel().getSelectedIndex();
            Ride selectedRide = this.ridesVisibleInView.get(index);
            this.mainController.switchLiveMapOff();
            this.mainController.updateMapRoadsByRides(new LinkedList<Ride>(){{add(selectedRide);}});
        }
    }

    public void clearSelection() {
        this.ridesListView.getSelectionModel().clearSelection();
    }
}
