package classes;

import exception.*;
import jaxb.schema.generated.Path;
import jaxb.schema.generated.Stop;
import jaxb.schema.generated.TransPool;
import jaxb.schema.generated.TransPoolTrip;
import javax.management.InstanceAlreadyExistsException;
import javax.xml.bind.JAXBException;
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

    public void loadXMLFile(String pathToFile) throws JAXBException, InvalidFileTypeException, NoFileFoundInPathException,
            InvalidMapBoundariesException, StationNameAlreadyExistsException, InstanceAlreadyExistsException,
            StationCoordinateoutOfBoundriesException, StationAlreadyExistInCoordinateException, NoRoadBetweenStationsException {

        TransPool transPool = (new XMLHandler(pathToFile)).LoadXML();

        initWorldMap(transPool);
        initRides(transPool);
    }

    private void initRoads(TransPool transPool) throws StationNotFoundException, InstanceAlreadyExistsException {

        for (Path path : transPool.getMapDescriptor().getPaths().getPath()) {

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
        }
    }

    private void initStations(TransPool transPool) throws StationNameAlreadyExistsException, InstanceAlreadyExistsException,
            StationCoordinateoutOfBoundriesException, StationAlreadyExistInCoordinateException {

        for (Stop stop : transPool.getMapDescriptor().getStops().getStop()) {
            map.addNewStation(new Station(new Coordinate(stop.getX(),stop.getY()),stop.getName()));
        }
    }

    private void initRides(TransPool transPool) throws NoRoadBetweenStationsException {
        for (TransPoolTrip ride : transPool.getPlannedTrips().getTransPoolTrip()) {

            List<String> roadListStringNames = Arrays.asList(ride.getRoute().getPath().split("\\s*(,)\\s*"));

                Ride newRide = createRideFromRoads(new User(ride.getOwner()), map.getRoadsFromStationsNames(roadListStringNames), ride.getCapacity());
                newRide.setPricePerKilometer(ride.getPPK());
                newRide.setSchedule(ride.getScheduling().getHourStart(),ride.getScheduling().getDayStart() ,ride.getScheduling().getRecurrences());
                trafficManager.addRide(newRide);


        }
    }

    private void initWorldMap(TransPool transPool) throws InvalidMapBoundariesException, StationNameAlreadyExistsException, InstanceAlreadyExistsException,
            StationCoordinateoutOfBoundriesException, StationAlreadyExistInCoordinateException, StationNotFoundException {

        int width = transPool.getMapDescriptor().getMapBoundries().getWidth();
        int Length = transPool.getMapDescriptor().getMapBoundries().getLength();

        if(!mapOutOfBoundaries(width,Length))
            throw new InvalidMapBoundariesException(width,Length);

        map = new WorldMap(width,Length);

        initStations(transPool);
        initRoads(transPool);
    }

    private boolean mapOutOfBoundaries(int width, int height) throws InvalidMapBoundariesException {
        if((width < 6 || width > 100) || (height < 6 || height > 100))
            return false;
        return true;
    }

    public Ride createNewEmptyRide(User rideOwner, List<Road> roads, int capacity){
        Ride newRide = createRideFromRoads(rideOwner, roads, capacity);
      
        return newRide;
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

    public void addRide(Ride rideToAdd){
        this.trafficManager.addRide(rideToAdd);
    }

    public TrempRequest createNewEmptyTrempRequest(Station start, Station end) throws NoPathExistBetweenStationsException{

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
            usersNameToObject.put(name, new User(name));
        }
        return usersNameToObject.get(name);
    }

    public List<Ride> getAllRides(){
        return this.trafficManager.getAllRides();
    }

    //public List<String> getAllRideAsString(){
    //    List<String> s = getAllRides()
    //                .stream()
    //                .flatMap(ride -> Stream.of(ride.getAllStations())).flatMap(Collection::stream).map(Station::getName).collect(Collectors.toList());
    //   return s;
    //}

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

    public List<Station> getAllStations(){
        return this.map.getAllStations();
    }
}
