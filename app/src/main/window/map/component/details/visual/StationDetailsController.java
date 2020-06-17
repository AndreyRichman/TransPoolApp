package main.window.map.component.details.visual;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import main.window.map.component.details.StationDetailsDTO;

public class StationDetailsController {

    private final String STATION_NAME_FORMAT = "%s ( %s ; %s)";
    private StationDetailsDTO data;

    @FXML
    private Label stationNameLabel;

    @FXML
    private ListView<String> RidersListVIew;

    @FXML
    private ListView<String> RidersonTrempistsListVIew;

    @FXML
    private ListView<String> offTrempistsListVIew;


    public void setData(StationDetailsDTO data) {
        this.data = data;
    }

    public StationDetailsController(){
        RidersListVIew = new ListView<>();
        RidersonTrempistsListVIew = new ListView<>();
        offTrempistsListVIew = new ListView<>();
    }

    public void setStationNameLabel() {
        stationNameLabel.setText(String.format(STATION_NAME_FORMAT, data.getName(), String.valueOf(data.getX()), String.valueOf(data.getY())));
    }

    public void setRidersListVIew(ListView<String> ridersListVIew) {
        this.RidersListVIew.getItems().addAll(data.getDrives());
    }

    public void setOffTrempistsListVIew(ListView<String> offTrempistsListVIew) {
        this.offTrempistsListVIew = offTrempistsListVIew;
    }

    public void setRidersonTrempistsListVIew(ListView<String> ridersonTrempistsListVIew) {
        this.RidersonTrempistsListVIew = ridersonTrempistsListVIew;
    }
}
