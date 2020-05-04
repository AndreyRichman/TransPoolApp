package exception;

import main.java.com.TransPool.logic.map.structure.Station;

public class StationAlreadyExistInCoordinateException extends Exception {

    private Station station;

    public StationAlreadyExistInCoordinateException(Station station){
        this.station = station;
    }

    public Station getStation() {
        return station;
    }
}
