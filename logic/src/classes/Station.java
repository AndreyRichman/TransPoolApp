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

    public Station(Coordinate coordinate, String name) {
        this.coordinate = coordinate;
        this.name = name;
        this.stationsFromCurrent = new HashMap<>();
        this.stationsToCurrent = new HashMap<>();
        this.allReachableStationFromThisStation =  new HashSet<>();
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

        this.stationsFromCurrent.put(totStation, road);
        this.allReachableStationFromThisStation.add(totStation);
        totStation.stationsToCurrent.put(fromStation, road);
    }

    public Boolean canReachStation(Station station){
        //if (this.allReachableStationFromThisStation == null){
       //     loadAllReachableStations();
       // }

        return this.allReachableStationFromThisStation.contains(station);
    }

    /*
    private void loadAllReachableStations(){
        this.allReachableStationFromThisStation = this.getAllReachableStations();
    }

    private Set<Station> getAllReachableStations(){
        Set<Station> allReachableStation = new HashSet<>(this.stationsFromCurrent.keySet());
        this.stationsFromCurrent.keySet().forEach((s)-> allReachableStation.addAll(s.getAllReachableStations()));

        return allReachableStation;
    }

 */

    public Coordinate getCoordinate() {
        return coordinate;
    }
}
