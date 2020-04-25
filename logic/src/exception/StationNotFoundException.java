package exception;

public class StationNotFoundException extends Exception {
    String stationName;

    public StationNotFoundException(String stationName) {
        this.stationName = stationName;
    }

    public String getStationName() {
        return stationName;
    }
}
