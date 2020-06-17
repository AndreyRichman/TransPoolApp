package main.window.map.component.details.visual;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import main.window.map.component.details.StationDetailsDTO;

public class StationDetailsController {

    private final String STATION_NAME_FORMAT = "%s ( %s ; %s )";
    private StationDetailsDTO data;

    @FXML
    private Label stationNameLabel;

    @FXML
    private ListView<String> RidersListVIew;

    @FXML
    private ListView<String> onTrempistsListVIew;

    @FXML
    private ListView<String> offTrempistsListVIew;


    public void setData(StationDetailsDTO data) {
        stationNameLabel.setText(String.format(STATION_NAME_FORMAT, data.getName(), String.valueOf(data.getX()), String.valueOf(data.getY())));
        this.RidersListVIew.getItems().addAll(data.getDrives());
        this.onTrempistsListVIew.getItems().addAll(data.getOnTremp());
        this.offTrempistsListVIew.getItems().addAll(data.getOffTremp());

    }

    public StationDetailsController(){
        RidersListVIew = new ListView<>();
        onTrempistsListVIew = new ListView<>();
        offTrempistsListVIew = new ListView<>();
    }



}
