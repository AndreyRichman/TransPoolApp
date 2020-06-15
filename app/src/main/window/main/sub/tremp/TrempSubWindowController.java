package main.window.main.sub.tremp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import main.window.main.MainWindowController;
import transpool.logic.traffic.item.TrempRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TrempSubWindowController {

    MainWindowController mainController;
    private boolean lastSelectedIsAssigned = false;

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
    private Button createNewTrempBtn;

    @FXML
    private Button RankRiderBtn;

    @FXML
    private Button matchRideBtn;

    @FXML
    private TextField maxOfffersTextField;

    @FXML
    private ChoiceBox<?> directChoiceBox;


    @FXML
    void onClickCreateNewTrempBtn(ActionEvent event) throws IOException {
        this.mainController.createNewTremp();
    }

    @FXML
    void onClickRankRiderBtn(ActionEvent event) {

    }

    @FXML
    void onClickMatchRideBtn(ActionEvent event) {


        //TODO either open rank window or show tremp options in rides list:
        // lastSelectedIsAssigned = true -> rank window
        // lastSelectedIsAssigned = false -> show tremp options
    }

    @FXML
    void onClickShowTrempDetails(ActionEvent event) {
    }


    @FXML
    void onTrempSelected(MouseEvent event) {
        if (this.mainController != null){
            int index = this.trempsListView.getSelectionModel().getSelectedIndex();
            TrempRequest selectedTremp = this.trempsVisibleInView.get(index);
            this.mainController.switchLiveMapOff();
            this.mainController.updateMapWithTrempRequest(selectedTremp);

            if (selectedTremp.isNotAssignedToRides())
            {
                lastSelectedIsAssigned = false;
                this.matchRideBtn.setVisible(true);
                this.matchRideBtn.setText("Find a Match!");
            }
            else if (selectedTremp.rankedAssignedRide()){
                this.matchRideBtn.setVisible(false);
                lastSelectedIsAssigned = true;
            } else {
                lastSelectedIsAssigned = true;
                this.matchRideBtn.setVisible(true);
                this.matchRideBtn.setText("Rank your tremp!");
            }
            initTrempsLabel(selectedTremp);
            //TODO: add functionality according to status
//            this.mainController.updateMapRoadsByRides(new LinkedList<Ride>(){{add(selectedTremp);}});
        }


    }

    private void initTrempsLabel(TrempRequest tremp) {
        userValue.setText(tremp.getUser().getName());
        wantToValue.setText(tremp.getSchedule().getDesiredTimeType().toString());
        atTimeValue.setText(String.format(tremp.getSchedule().getDesiredDateTimeAccordingToTimeType().getHour() +":"+tremp.getSchedule().getDesiredDateTimeAccordingToTimeType().getMinute()));
        if(tremp.isNotAssignedToRides())
            statusValue.setText(String.format("NOT Assinged"));
        else
            statusValue.setText(String.format("Assigned" ));
        //tremp.getSelectedRide().getSubRides().get(0).getOriginalRide().getID()
    }

    public void setMainController(MainWindowController mainController) {
        this.mainController = mainController;
    }

    private Map<Integer, TrempRequest> trempsVisibleInView;

    public void updateTrempsList(){
        this.trempsVisibleInView = new HashMap<>();

        this.trempsListView.getItems().clear();
        int index = 0;
        for (TrempRequest tremp: this.mainController.getAllTrempRequests()) {
               String trempRepr = String.format("%d - %s", tremp.getID(), tremp.getUser().getName());
               this.trempsListView.getItems().add(trempRepr);
               this.trempsVisibleInView.put(index++, tremp);
        }
    }

    public void clearSelection() {
        this.trempsListView.getSelectionModel().clearSelection();
    }

    public void clear() {
        this.trempsListView.getItems().clear();
    }
}
