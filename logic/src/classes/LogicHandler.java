package classes;

import enums.Recurrences;
import exception.*;
import jaxb.schema.generated.Path;
import jaxb.schema.generated.Stop;
import jaxb.schema.generated.TransPool;
import jaxb.schema.generated.TransPoolTrip;
import javax.management.InstanceAlreadyExistsException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static classes.Ride.createRideFromRoads;

public class LogicHandler {
    private WorldMap map;
    private TrafficManager trafficManager;
    private Map<String, User> usersNameToObject;

    public LogicHandler() {
        trafficManager = new TrafficManager();
        usersNameToObject = new HashMap<>();
    }

    public void loadXMLFile(String pathToFile){
        XMLHandler loadXML = new XMLHandler(pathToFile);
        TransPool transPool = loadXML.LoadXML();

        initWorldMap(transPool);
        initRides(transPool);

    }

    private void initRoads(TransPool transPool) {

        for (Path path : transPool.getMapDescriptor().getPaths().getPath()) {

            try {

                Road toFromRoad = new Road(getStationFromName(path.getFrom()), getStationFromName(path.getTo()));
                toFromRoad.setFuelUsagePerKilometer(path.getFuelConsumption());
                toFromRoad.setLengthInKM(path.getLength());
                toFromRoad.setMaxSpeed(path.getSpeedLimit());
                map.addNewRoad(toFromRoad);
                toFromRoad.getStartStation().addRoadFromCurrentStation(toFromRoad);

                if (!path.isOneWay())
                {
                    Road fromToRoad = new Road(getStationFromName(path.getTo()), getStationFromName(path.getFrom()));
                    fromToRoad.setFuelUsagePerKilometer(path.getFuelConsumption());
                    fromToRoad.setLengthInKM(path.getLength());
                    fromToRoad.setMaxSpeed(path.getSpeedLimit());
                    map.addNewRoad(fromToRoad);
                    fromToRoad.getStartStation().addRoadFromCurrentStation(fromToRoad);
                }

            } catch (InstanceAlreadyExistsException e) {
                e.printStackTrace();
            } catch (StationNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void initStations(TransPool transPool) {

        for (Stop stop : transPool.getMapDescriptor().getStops().getStop()) {
            try {
                map.addNewStation(new Station(new Coordinate(stop.getX(),stop.getY()),stop.getName()));
            } catch (InstanceAlreadyExistsException e) {
                e.printStackTrace();
            } catch (StationNameAlreadyExistsException e) {
                e.printStackTrace();
            } catch (StationAlreadyExistInCoordinateException e) {
                e.printStackTrace();
            } catch (StationCoordinateoutOfBoundriesException e) {
                e.printStackTrace();
            }
        }
    }

    private void initRides(TransPool transPool) {
        for (TransPoolTrip ride : transPool.getPlannedTrips().getTransPoolTrip()) {

            List<String> roadListStringNames = Arrays.asList(ride.getRoute().getPath().split("\\s*(,)\\s*"));

            try {
                Ride newRide = createRideFromRoads(new User(ride.getOwner()), map.getRoadsFromStationsNames(roadListStringNames), ride.getCapacity());
                newRide.setPricePerKilometer(ride.getPPK());
                newRide.setSchedule(ride.getScheduling().getHourStart(),ride.getScheduling().getDayStart() ,ride.getScheduling().getRecurrences());
                trafficManager.addRide(newRide);
            } catch (NoRoadBetweenStationsException e) {
                e.printStackTrace();
            }

        }
    }

    private void initWorldMap(TransPool transPool) {

        map = new WorldMap(transPool.getMapDescriptor().getMapBoundries().getWidth(),transPool.getMapDescriptor().getMapBoundries().getLength());

        initStations(transPool);
        initRoads(transPool);

    }

    public Ride createNewEmptyRide(User rideOwner, List<Road> roads, int capacity){
        Ride newRide = createRideFromRoads(rideOwner, roads, capacity);

        return newRide;
    }
    public void addRide(Ride rideToAdd){
        this.trafficManager.addRide(rideToAdd);
    }

    public TrempRequest createNewEmptyTrempRequest(Station start, Station end) throws NoPathExistBetweenStationsException{
        //TODO: validate path is exist
        if (!start.canReachStation(end)){
            throw new NoPathExistBetweenStationsException();
        }
        TrempRequest newTrempRequest = new TrempRequest(start, end);
        return newTrempRequest;
    }

    public void addTrempRequest(TrempRequest trempRequest){
        this.trafficManager.addTrempRequest(trempRequest);
    }

    public Station getStationFromName(String name) throws StationNotFoundException {
        return this.map.getStationByName(name);
    }

    public User getUserByName(String name){
        if (!usersNameToObject.containsKey(name)) {
            User user = new User(name);
        }
        return usersNameToObject.get(name);
    }

    public List<Ride> getAllRides(){
        return this.trafficManager.getRides();
    }

    //public List<String> getAllRideAsString(){
    //    List<String> s = getAllRides()
    //                .stream()
    //                .flatMap(ride -> Stream.of(ride.getAllStations())).flatMap(Collection::stream).map(Station::getName).collect(Collectors.toList());
    //   return s;
    //}

    public List<TrempRequest> getAllTrempRequests(){
        return this.trafficManager.getTrempRequests();
    }

    public List<TrempRequest> getAllNonMatchedTrempRequests(){
        return getAllTrempRequests().stream()
                .filter(TrempRequest::isNotAssignedToRides)
                .collect(Collectors.toList());
    }


    public TrempRequest getTrempRequestById(int trempID) throws TrempRequestNotExist{
        return this.trafficManager.getTrempRequestById(trempID);
    }

    public Ride getRideById(int rideID) throws RideNotExistsException{
        return this.trafficManager.getRideByID(rideID);
    }
}
