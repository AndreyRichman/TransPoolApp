package main.window.map.component.details.visual;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import main.window.map.component.details.StationDetailsDTO;

public class StationDetailsController {

    private final String STATION_NAME_FORMAT = "%s ( %s ; %s)";

    @FXML private Label stationNameLabel;

    @FXML private Label visitingTripsLabel;

    @FXML
    private ListView<String> JoinRideListView;

    @FXML
    private ListView<String> dropRideListView;

    @FXML
    private ListView<String> passingRideListView;

    @FXML
    private Label stationName;

    public StationDetailsController(){
        JoinRideListView = new ListView<>();
        dropRideListView = new ListView<>();
        passingRideListView = new ListView<>();
    }


    public void setData(StationDetailsDTO data) {
        stationNameLabel.setText(String.format(STATION_NAME_FORMAT, data.getName(), String.valueOf(data.getX()), String.valueOf(data.getY())));

    }

}
