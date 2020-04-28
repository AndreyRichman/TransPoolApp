package classes;

import exception.*;
import jaxb.schema.generated.Path;
import jaxb.schema.generated.Stop;
import jaxb.schema.generated.TransPool;
import jaxb.schema.generated.TransPoolTrip;
import javax.management.InstanceAlreadyExistsException;
import java.util.*;
import java.util.stream.Collectors;

import static classes.Ride.createRideFromRoads;

public class LogicHandler {
    private WorldMap map;
//    List<Ride> rides;
//    List<TrempRequest> trempRequests;
    private TrafficManager trafficManager;
    private Map<String, User> usersNameToObject;

    public LogicHandler() {
        trafficManager = new TrafficManager();
//        trempRequests = new ArrayList<>();
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
                Road road = new Road(getStationFromName(path.getFrom()), getStationFromName(path.getTo()));
                road.setFuelUsagePerKilometer(path.getFuelConsumption());
                road.setLengthInKM(path.getLength());
                road.setMaxSpeed(path.getSpeedLimit());

                map.addNewRoad(road);
            } catch (InstanceAlreadyExistsException e) {
                e.printStackTrace();
            } catch (StationNotFoundException e) {
                e.printStackTrace();
            }
            System.out.println("from: " + path.getFrom() + " to: " + path.getTo());
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
            System.out.println(stop.getName());
        }
    }

    private void initRides(TransPool transPool) {
        for (TransPoolTrip ride : transPool.getPlannedTrips().getTransPoolTrip()) {
            String routes = ride.getRoute().getPath();
            List<String> roadListStringNames = Arrays.asList(routes.split(","));
            List<Road> roads = null;
            try {
                roads = map.getRoadsFromStationsNames(roadListStringNames);
            } catch (NoRoadBetweenStationsException e) {
                e.printStackTrace();
            }
            Ride test = (createRideFromRoads(new User(ride.getOwner()), roads, ride.getCapacity()));
            System.out.println(test);
            trafficManager.addRide(createRideFromRoads(new User(ride.getOwner()), roads, ride.getCapacity()));
            //createRideFromRoads(ride.getOwner(),ride.getRoute(),)
        }
    }

    private void initWorldMap(TransPool transPool) {
        map = new WorldMap(transPool.getMapDescriptor().getMapBoundries().getWidth(),transPool.getMapDescriptor().getMapBoundries().getLength());

        initStations(transPool);
        initRoads(transPool);


    }



    public Ride createNewEmptyRide(User rideOwner, List<Road> roads, int capacity){
        Ride newRide = createRideFromRoads(rideOwner, roads, capacity);

//        this.rides.add(newRide);  moved to a seperate func

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
//        trempRequests.add(newTrempRequest); moved to a seperate func
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
