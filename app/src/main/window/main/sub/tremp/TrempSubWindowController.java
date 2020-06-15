package main.window.main.sub.tremp;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.window.main.MainWindowController;
import main.window.main.sub.rating.newRatingController;
import transpool.logic.traffic.item.TrempRequest;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class TrempSubWindowController {

    private MainWindowController mainController;
    private SimpleBooleanProperty lastSelectedIsAssigned;

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
    private ChoiceBox<String> directChoiceBox;


    @FXML
    void onClickCreateNewTrempBtn(ActionEvent event) throws IOException {
        this.mainController.createNewTremp();
    }

    @FXML
    void onClickRankRiderBtn(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        stage.setResizable(false);
        URL resource = getClass().getResource("../rating/ratingWindow.fxml");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(resource);
        Parent root = loader.load();
        newRatingController controller = loader.getController();
        controller.setMainController(this);
        controller.setStage(stage);
        Scene scene = new Scene(root, 461, 241);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void onClickMatchRideBtn(ActionEvent event) {

        if(this.trempsVisibleInView.get(this.trempsListView.getSelectionModel().getSelectedIndex()) != null)
        {

        }

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
                lastSelectedIsAssigned.set(false);
            if (selectedTremp.rankedAssignedRide())
                lastSelectedIsAssigned.set(true);


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

    public TrempSubWindowController(){
        lastSelectedIsAssigned = new SimpleBooleanProperty(false);
    }

    @FXML
    public void initialize() {
        directChoiceBox.getItems().add("YES");
        directChoiceBox.getItems().add("NO");
        RankRiderBtn.disableProperty().bind(lastSelectedIsAssigned.not());
        matchRideBtn.disableProperty().bind(lastSelectedIsAssigned);

    }

}
