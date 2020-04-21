package classes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Station {

    private Coordinate coordinate;
    private String name;
    private Map<Station, Road> stationsFromCurrent;
    private Map<Station, Road> stationsToCurrent;
    private Set<Station> allReachableStationFromThisStation;

    public Station(Coordinate sationCoordinate, String stationName) {
        coordinate = sationCoordinate;
        name = stationName;
        stationsFromCurrent = new HashMap<>();
        stationsToCurrent = new HashMap<>();
        allReachableStationFromThisStation =  new HashSet<>();
    }

    public Road getRoadToStation(Station station){
        return stationsFromCurrent.get(station);
    }

    public void addRoadFromCurrentStation(Road road) {
        // A , input:A->B :  A.roadsFrom += (B, A->B)   , B.roadTo += (A, A->B)
        //TODO: add validation for start_station == current Station
        //TODO: support two side road לא אמרנו שנעשה את זה פשוט לכל כיוון?
        Station fromStation = road.getStartStation();
        Station totStation = road.getEndStation();

        stationsFromCurrent.put(totStation, road);
        allReachableStationFromThisStation.add(totStation);
        totStation.stationsToCurrent.put(fromStation, road);
    }

    public Boolean canReachStation(Station station){
        return allReachableStationFromThisStation.contains(station);
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }
}
