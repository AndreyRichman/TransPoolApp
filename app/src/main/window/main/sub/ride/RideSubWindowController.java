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
    private Label rideDuration;

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
        this.ridesListView.getItems().clear();
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
            initLabels(selectedRide);
            this.mainController.switchLiveMapOff();
            if (selectedRide != null)
                this.mainController.updateMapRoadsByRides(new LinkedList<Ride>(){{add(selectedRide);}});
        }
    }

    private void initLabels(Ride ride) {
        userValue.setText(ride.getRideOwner().getUser().getName());
        departValue.setText(String.valueOf(ride.getSchedule().getStartTime()));
        arriveValue.setText(String.valueOf(ride.getSchedule().getEndTime()));
        ppkValue.setText(String.valueOf(ride.getPricePerKilometer()));
        fuelValue.setText(String.valueOf(ride.getAverageFuelUsage()));
    }

    public void clear(){
        this.ridesListView.getItems().clear();
    }

    public void clearSelection() {
        this.ridesListView.getSelectionModel().clearSelection();
    }
}
