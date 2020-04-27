package classes;

import exception.StationAlreadyExistInCoordinate;
import exception.StationCoordinateoutOfBoundriesException;
import exception.StationNameAlreadyExistsException;
import exception.StationNotFoundException;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorldMap {

    private final int width;
    private final int height;
    List<Station> allStations;
    List<Road> allRoads;
    Map<String, Station> stationName2Object;
    Map<Coordinate, String> coordinateStationNameMap;

    public WorldMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.allStations = new ArrayList<>();
        this.allRoads = new ArrayList<>();
        this.stationName2Object = new HashMap<>();
        this.coordinateStationNameMap = new HashMap<>();
    }
    public List<Station> getAllStations(){
        return allStations;
    }

    public void addNewStation(Station station)
            throws InstanceAlreadyExistsException, StationNameAlreadyExistsException, StationAlreadyExistInCoordinate, StationCoordinateoutOfBoundriesException {
        validateStation(station);
        this.allStations.add(station);
        this.stationName2Object.put(station.getName(), station);
        this.coordinateStationNameMap.put(station.getCoordinate(), station.getName());
    }

    public Station getStationByName(String name) throws StationNotFoundException{
        Station station = this.stationName2Object.get(name);
        if (station == null)
            throw new StationNotFoundException(name);

        return station;
    }

    private void validateStation(Station station)
            throws InstanceAlreadyExistsException, StationNameAlreadyExistsException, StationAlreadyExistInCoordinate {

        if (!coordinateInBoundaries(station.getCoordinate())) {
            throw new IndexOutOfBoundsException(station.getCoordinate().toString());
        }

        if (this.allStations.contains(station)) {
            throw new InstanceAlreadyExistsException(station.toString());
        }

        if (this.stationName2Object.containsKey(station.getName()))
            throw new StationNameAlreadyExistsException();

        if (this.coordinateStationNameMap.containsKey(station.getCoordinate())) {
            throw new StationAlreadyExistInCoordinate();
        }
    }

    private boolean coordinateInBoundaries(Coordinate coord){
        return 0 <= coord.getX() &&
                coord.getX() < this.width &&
                0 <= coord.getY() &&
                coord.getY() < this.height;
    }

    public void addNewRoad(Road road) throws InstanceAlreadyExistsException, StationNotFoundException{
        validateNewRoad(road);
        this.allRoads.add(road);
    }

    private void validateNewRoad(Road road) throws InstanceAlreadyExistsException, StationNotFoundException{
        Station start = road.getStartStation();
        Station end = road.getEndStation();

        if (this.allRoads.contains(road))
            throw new InstanceAlreadyExistsException(road.toString());

        if (!this.allStations.contains(start))
            throw new StationNotFoundException(start.getName());

        if (!this.allStations.contains(end)) {
            throw new StationNotFoundException(end.getName());
        }

    }
}
