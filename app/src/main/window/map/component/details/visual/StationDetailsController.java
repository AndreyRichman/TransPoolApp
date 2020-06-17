package main.window.map.component.details.visual;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import main.window.map.component.details.StationDetailsDTO;

public class StationDetailsController {

    private final String STATION_NAME_FORMAT = "%s ( %s ; %s)";

    @FXML private Label stationNameLabel;
    @FXML private Label visitingTripsLabel;
    @FXML private ListView<String> RidersListVIew;

    public void setData(StationDetailsDTO data) {
        RidersListVIew = new ListView<>();
        stationNameLabel.setText(String.format(STATION_NAME_FORMAT, data.getName(), String.valueOf(data.getX()), String.valueOf(data.getY())));
        visitingTripsLabel.setText(data.getDrives().toString());
        RidersListVIew.getItems().addAll(data.getDrives());
    }



}
