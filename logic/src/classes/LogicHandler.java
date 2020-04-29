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
            String routes = "...";
            List<Road> roads;
            List<String> elephantList = Arrays.asList(routes.split(","));
            //createRideFromRoads(ride.getOwner(),ride.getRoute(),)
        }
    }

    private void initWorldMap(TransPool transPool) {
        map = new WorldMap(transPool.getMapDescriptor().getMapBoundries().getWidth(),transPool.getMapDescriptor().getMapBoundries().getLength());

        initStations(transPool);
        initRoads(transPool);
        initRides(transPool);
    }

    public List<List<Ride.SubRide>> getAllPossibleTrempsForTrempRequest(TrempRequest trempRequest){
        Station start = trempRequest.getStartStation();
        Station end = trempRequest.getEndStation();
        int maxNumberOfConnections = trempRequest.getMaxNumberOfConnections();

        //TODO:pass all possible routes from start to end(using world Map) - or make sure Start can check this
        List<List<Ride.SubRide>> relevantByRouteOptions = trafficManager.getRideOptions(maxNumberOfConnections, start, end);
        relevantByRouteOptions.sort(Comparator.comparingInt(List::size));

        return relevantByRouteOptions.stream()
                .filter(lstOfSubRides -> lstOfSubRides.get(0).selectedPartsOfRide.get(0)
                        .getStartTime().equals(trempRequest.getDepartTime()))
                .collect(Collectors.toList());

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
            usersNameToObject.put(name, new User(name));
        }
        return usersNameToObject.get(name);
    }

    public List<Ride> getAllRides(){
        return this.trafficManager.getAllRides();
    }

    public List<TrempRequest> getAllTrempRequests(){
        return this.trafficManager.getAllTrempRequests();
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
