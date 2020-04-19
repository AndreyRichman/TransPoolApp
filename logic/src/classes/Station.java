package classes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Station {

    private Coordinate coordinate;
    private String name;
    private Map<Station, Road> stationsFromCurrent2Roads;
    private Map<Station, Road> stationsToCurrent2Roads;

    private Set<Station> allStationReachableFromThisStation = null;

    public Station(Coordinate coordinate, String name) {
        this.coordinate = coordinate;
        this.name = name;

        this.stationsFromCurrent2Roads = new HashMap<>();
        this.stationsToCurrent2Roads = new HashMap<>();
    }

    public Road getRoadToStation(Station station){
        return stationsFromCurrent2Roads.get(station);
    }

    public void addRoadFromCurrentStation(Road road) {
        // A , input:A->B :  A.roadsFrom += (B, A->B)   , B.roadTo += (A, A->B)
        //TODO: add validation for start_station == current Station
        //TODO: support two side road
        Station thisStation = road.getStartStation();
        Station targetStation = road.getEndStation();
        this.stationsFromCurrent2Roads.put(targetStation, road);
        targetStation.stationsToCurrent2Roads.put(thisStation, road);
    }

    private Set<Station> getAllReachableStations(){
        Set<Station> allReachableStation = new HashSet<>(this.stationsFromCurrent2Roads.keySet());
        this.stationsFromCurrent2Roads.keySet().forEach((s)-> allReachableStation.addAll(s.getAllReachableStations()));

        return allReachableStation;
    }

    private void loadAllReachableStations(){
        this.allStationReachableFromThisStation = this.getAllReachableStations();
    }

    public Boolean canReachStation(Station otherStation){
        if (this.allStationReachableFromThisStation == null){
            loadAllReachableStations();
        }

        return this.allStationReachableFromThisStation.contains(otherStation);
    }



}
