package classes;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class WorldMap {

    private final int width;
    private final int height;
    List<Station> allStations;
    List<Road> allRoads;

    public WorldMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.allStations = new ArrayList<>();
        this.allRoads = new ArrayList<>();
    }

    public void addNewStation(Station station) throws InstanceAlreadyExistsException{
        validateStation(station);
        this.allStations.add(station);
    }
    private void validateStation(Station station) throws InstanceAlreadyExistsException{

        if (!coordinateInBoundaries(station.getCoordinate())) {
            throw new IndexOutOfBoundsException(station.getCoordinate().toString());
        }

        if (this.allStations.contains(station)) {
            throw new InstanceAlreadyExistsException(station.toString());
        }
    }

    private boolean coordinateInBoundaries(Coordinate coord){
        return 0 <= coord.getX() &&
                coord.getX() < this.width &&
                0 <= coord.getY() &&
                coord.getY() < this.height;
    }

    public void addNewRoad(Road road) throws InstanceAlreadyExistsException, InstanceNotFoundException{
        validateNewRoad(road);
        this.allRoads.add(road);
    }

    private void validateNewRoad(Road road) throws InstanceAlreadyExistsException, InstanceNotFoundException{
        Station start = road.getStartStation();
        Station end = road.getEndStation();

        if (this.allRoads.contains(road))
            throw new InstanceAlreadyExistsException(road.toString());

        if (!this.allStations.contains(start))
            throw new InstanceNotFoundException(start.toString());

        if (!this.allStations.contains(end)) {
            throw new InstanceNotFoundException(end.toString());
        }

    }
}
