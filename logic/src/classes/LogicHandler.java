package classes;

import enums.DesiredTimeType;
import exception.*;
import jaxb.schema.generated.Path;
import jaxb.schema.generated.Stop;
import jaxb.schema.generated.TransPool;
import jaxb.schema.generated.TransPoolTrip;
import javax.management.InstanceAlreadyExistsException;
import javax.xml.bind.JAXBException;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;
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

    public void loadXMLFile(String pathToFile) throws FaildLoadingXMLFileException,
            InvalidMapBoundariesException, StationNameAlreadyExistsException, InstanceAlreadyExistsException,
            StationCoordinateoutOfBoundriesException, StationAlreadyExistInCoordinateException, NoRoadBetweenStationsException {

        TransPool transPool = (new XMLHandler(pathToFile)).LoadXML();

        initWorldMap(transPool);
        initRides(transPool);
    }

    private void initRoads(TransPool transPool) throws FaildLoadingXMLFileException  {

        for (Path path : transPool.getMapDescriptor().getPaths().getPath()) {

            Road toFromRoad = new Road(getStationFromName(path.getFrom()), getStationFromName(path.getTo()));
            toFromRoad.setFuelUsagePerKilometer(path.getFuelConsumption());
            toFromRoad.setLengthInKM(path.getLength());
            toFromRoad.setMaxSpeed(path.getSpeedLimit());
            try {
                map.addNewRoad(toFromRoad);
            } catch (InstanceAlreadyExistsException e) {
                throw new FaildLoadingXMLFileException("Road from" + path.getFrom() + " to " +path.getTo() + " Already Exists ");
            }

            toFromRoad.getStartStation().addRoadFromCurrentStation(toFromRoad);

            if (!path.isOneWay())
            {
                Road fromToRoad = new Road(getStationFromName(path.getTo()), getStationFromName(path.getFrom()));
                fromToRoad.setFuelUsagePerKilometer(path.getFuelConsumption());
                fromToRoad.setLengthInKM(path.getLength());
                fromToRoad.setMaxSpeed(path.getSpeedLimit());
                try {
                    map.addNewRoad(fromToRoad);
                } catch (InstanceAlreadyExistsException e) {
                    throw new FaildLoadingXMLFileException("Road from" + path.getTo() + " to " +path.getFrom() + " Already Exists ");
                }

                fromToRoad.getStartStation().addRoadFromCurrentStation(fromToRoad);
            }
        }
    }

    private void initStations(TransPool transPool) throws FaildLoadingXMLFileException {

        for (Stop stop : transPool.getMapDescriptor().getStops().getStop()) {
            try {
                map.addNewStation(new Station(new Coordinate(stop.getX(),stop.getY()),stop.getName()));
            } catch (InstanceAlreadyExistsException e) {
                throw new FaildLoadingXMLFileException("Station:" + stop.getName() + "already exists");
            } catch (StationNameAlreadyExistsException e) {
                throw new FaildLoadingXMLFileException("Station name:" + e.getStation().getName() + "already exists");
            } catch (StationAlreadyExistInCoordinateException e) {
                throw new FaildLoadingXMLFileException("Station name:" + e.getStation().getName() + "already exists in coords" + "(" + e.getStation().getCoordinate().getX() +"," +e.getStation().getCoordinate().getY() + ")");
            } catch (StationCoordinateoutOfBoundriesException e) {
                throw new FaildLoadingXMLFileException("Station name:" + e.getStation().getName() + "coords out of Boundries" + "(" + e.getStation().getCoordinate().getX() +"," +e.getStation().getCoordinate().getY() + ")");
            }
        }
    }

    private void initRides(TransPool transPool) throws FaildLoadingXMLFileException {

        for (TransPoolTrip ride : transPool.getPlannedTrips().getTransPoolTrip()) {

            List<String> roadListStringNames = Arrays.asList(ride.getRoute().getPath().split("\\s*(,)\\s*"));

            Ride newRide = null;
            try {
                newRide = createRideFromRoads(new User(ride.getOwner()), map.getRoadsFromStationsNames(roadListStringNames), ride.getCapacity());
            } catch (NoRoadBetweenStationsException e) {
                throw new FaildLoadingXMLFileException("No road between:" + e.getFromStation() + "to" + e.getToStation());
            }
            newRide.setPricePerKilometer(ride.getPPK());
                newRide.setSchedule(ride.getScheduling().getHourStart(),ride.getScheduling().getDayStart() ,ride.getScheduling().getRecurrences());
                trafficManager.addRide(newRide);
        }
    }

    private void initWorldMap(TransPool transPool) throws InvalidMapBoundariesException, FaildLoadingXMLFileException {

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

    public List<List<SubRide>> getAllPossibleTrempsForTrempRequest(TrempRequest trempRequest){
        Station start = trempRequest.getStartStation();
        Station end = trempRequest.getEndStation();
        int maxNumberOfConnections = trempRequest.getMaxNumberOfConnections();

        //TODO:pass all possible routes from start to end(using world Map) - or make sure Start can check this
        List<List<SubRide>> relevantByRouteOptions = trafficManager.getRideOptions(maxNumberOfConnections, start, end);
        relevantByRouteOptions.sort(Comparator.comparingInt(List::size));

        relevantByRouteOptions.forEach(lstOfSubrides -> {
            System.out.println(String.format("Available time: %s", lstOfSubrides.get(0).getDepartTime()));
        }
        );

        Function<List<SubRide>, LocalTime> rideCorrectTimeGetter = trempRequest.getDesiredTimeType() == DesiredTimeType.DEPART ?
                subRideList -> subRideList.get(0).getDepartTime() :  subRideList -> subRideList.get(subRideList.size() - 1).getArrivalTime();

        return relevantByRouteOptions.stream()
                .filter(lstOfSubRides -> rideCorrectTimeGetter.apply(lstOfSubRides).equals(trempRequest.getDesiredTime()))
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

    public List<Road> getRoadsFromStationsNames(List<String> stationNames) throws NoRoadBetweenStationsException {
        return this.map.getRoadsFromStationsNames(stationNames);
    }
}
