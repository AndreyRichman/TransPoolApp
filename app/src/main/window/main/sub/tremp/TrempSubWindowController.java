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
import javafx.scene.paint.Color;
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
    private Label maxOfferLabel;


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

        if(validations()){

            maxOfferLabel.setTextFill(Color.WHITE);
            maxOfffersTextField.setPromptText("");

            int index = this.trempsListView.getSelectionModel().getSelectedIndex();
            TrempRequest selectedTremp = this.trempsVisibleInView.get(index);

            if (this.directChoiceBox.getSelectionModel().getSelectedIndex() == 0)
                selectedTremp.setMaxNumberOfConnections(0);
            else
                selectedTremp.setMaxNumberOfConnections(5);

            int maxOffers = this.maxOfffersTextField.getText().equals("") ? 5 : Integer.parseInt(this.maxOfffersTextField.getText());

            this.mainController.showTremps(selectedTremp, maxOffers);
        }

    }

    public void updateNoTrempsAvailable(){
        this.matchRideBtn.setText("No Options");
        this.matchRideBtn.setMinWidth(240);
    }

    public void updateMatchText(){
        this.matchRideBtn.setMinWidth(50);
        this.matchRideBtn.setText("Match");
    }

    private boolean validations() {
        if (Integer.parseInt(this.maxOfffersTextField.getText() )< 1) {
            maxOfferLabel.setTextFill(Color.RED);
            maxOfffersTextField.setPromptText("number > 0");
            return false;
        }
        return true;
    }

    @FXML
    void onClickShowTrempDetails(ActionEvent event) {
    }


    @FXML
    void onTrempSelected(MouseEvent event) {

        RankRiderBtn.setVisible(true);
        matchRideBtn.setVisible(true);

        updateMatchText();
        updateAccordingToSelectedTremp();
    }

    public void updateAccordingToSelectedTremp(){
        if (this.mainController != null && !this.trempsListView.getSelectionModel().isEmpty()){
            int index = this.trempsListView.getSelectionModel().getSelectedIndex();
            TrempRequest selectedTremp = this.trempsVisibleInView.get(index);


            this.mainController.switchLiveMapOff();
            this.mainController.updateMapWithTrempRequest(selectedTremp);

            if (selectedTremp.isNotAssignedToRides())
                lastSelectedIsAssigned.set(false);
            if (selectedTremp.rankedAssignedRide())
                lastSelectedIsAssigned.set(true);


            initTrempsLabel(selectedTremp);
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

    public TrempRequest getSelectedTrempRequest(){
        int index = this.trempsListView.getSelectionModel().getSelectedIndex();
        return this.trempsVisibleInView.get(index);
    }

    public void setMainController(MainWindowController mainController) {
        this.mainController = mainController;
    }

    private Map<Integer, TrempRequest> trempsVisibleInView;

    public void updateTrempsList(){
        createNewTrempBtn.setVisible(true);
        RankRiderBtn.setVisible(false);
        matchRideBtn.setVisible(false);

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
        userValue.setText("");
        wantToValue.setText("");
        atTimeValue.setText("");
        statusValue.setText("");
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
        RankRiderBtn.setVisible(false);
        matchRideBtn.setVisible(false);
        createNewTrempBtn.setVisible(false);

    }

}
