package main.window.main.sub.ride;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import main.window.main.MainWindowController;
import transpool.logic.traffic.item.Ride;
import transpool.logic.traffic.item.RideForTremp;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RideSubWindowController {

    private MainWindowController mainController;
    private Map<Integer, Ride> ridesVisibleInView;
    private Map<Integer, RideForTremp> trempsVisibleInView;

    private boolean showingTremps = false;

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
    private Label mainTitle;

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
    private Button assignTrempBtn;

    @FXML
    private Button cancelTrempsBtn;
  
    @FXML
     private Label rideDuration;

    @FXML
    void onClickAssignTrempBtn(ActionEvent event) {
        this.mainController.assignSelectedTrempRequestToRide();
        updateAfterDoneWithTremps();
    }

    public RideForTremp getSelectedTremp(){
        int index = this.ridesListView.getSelectionModel().getSelectedIndex();
        return this.trempsVisibleInView.get(index);
    }

    @FXML
    void onClickCancelTrempBtn(ActionEvent event) {
        updateAfterDoneWithTremps();
    }

    private void updateAfterDoneWithTremps(){
        this.showingTremps = false;
        updateTrempButtons();
        updateMainTitle();
        updateNewButton();
        this.mainController.refreshTrempState();
//        updateRidesList();
    }

    public void showTremps(List<RideForTremp> tremps){
        this.showingTremps = true;
        updateMainTitle();
        updateTrempsList(tremps);
        updateNewButton();
    }




    @FXML
    void onAddNewRideBtnClick(ActionEvent event) throws IOException {
        this.mainController.createNewRide();

    }
    public void setMainController(MainWindowController mainController) {
        this.mainController = mainController;
    }

    public void updateRidesList(){
        updateAfterDoneWithTremps();
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
    }



    public void updateTrempsList(List<RideForTremp> tremps){
        this.trempsVisibleInView = new HashMap<>();
        int index = 0;
        this.ridesListView.getItems().clear();

        for(RideForTremp ride : tremps) {
            String toSHow = String.format("%d - %s",
                    ride.getID(),
                    String.join(",", ride.getDrivers()));
            this.ridesListView.getItems().add(toSHow);
            trempsVisibleInView.put(index++, ride);
        }
    }

    @FXML
    void onRideSelected(MouseEvent event) {
        if (this.mainController != null){
            updateTrempButtons();

            if (this.showingTremps)
                updateTrempInfo();
            else
                updateRideInfo();

        }
    }

    private void updateTrempInfo() {
        int index = this.ridesListView.getSelectionModel().getSelectedIndex();
        RideForTremp selectedRide = this.trempsVisibleInView.get(index);

        this.mainController.showRideForTremp(selectedRide);
    }

    private void updateRideInfo() {
        int index = this.ridesListView.getSelectionModel().getSelectedIndex();
        Ride selectedRide = this.ridesVisibleInView.get(index);
        initRideLabels(selectedRide);

        this.mainController.switchLiveMapOff();
        if (selectedRide != null)
            this.mainController.updateMapRoadsByRides(new LinkedList<Ride>(){{add(selectedRide);}});

        this.mainController.updateMatchBtn();
    }

    private void updateTrempButtons() {
        updateNewButton();
        if(this.showingTremps){
            this.assignTrempBtn.setVisible(true);
            this.cancelTrempsBtn.setVisible(true);
        } else{
            this.assignTrempBtn.setVisible(false);
            this.cancelTrempsBtn.setVisible(false);
        }
    }

    private void updateMainTitle(){
        if(this.showingTremps){
            this.mainTitle.setText("Choose A Tremp");
        } else
            this.mainTitle.setText("Rides");
    }
    private void updateNewButton() {
        if(this.showingTremps){
           this.addNewRideBtn.setDisable(true);
        } else
            this.addNewRideBtn.setDisable(false);

    }

    private void initRideLabels(Ride ride) {
        userValue.setText(ride.getRideOwner().getUser().getName());
        departValue.setText(String.valueOf(ride.getSchedule().getStartTime()));
        arriveValue.setText(String.valueOf(ride.getSchedule().getEndTime()));
        ppkValue.setText(String.valueOf(ride.getPricePerKilometer()));
        fuelValue.setText(String.valueOf(ride.getAverageFuelUsage()));
        rideDuration.setText(String.valueOf(ride.getTotalTimeOfRide()));
    }

    public void clear(){
        this.ridesListView.getItems().clear();
    }

    public void clearSelection() {
        this.ridesListView.getSelectionModel().clearSelection();
    }

    public void showNoTrempsAvailableTitle() {
        this.mainTitle.setText("Sorry.... no matches for your request");
    }

    public void showRidesTitle() {
        this.mainTitle.setText("Rides");
    }
}
