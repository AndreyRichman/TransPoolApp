package classes;

import exception.NoPathExistBetweenStationsException;
import exception.StationNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogicHandler {
    WorldMap map;
    List<Ride> rides;
    List<TrempRequest> trempRequests;
    Map<String, User> usersNameToObject;

    public LogicHandler() {
        trempRequests = new ArrayList<>();
        usersNameToObject = new HashMap<>();
    }

    public void loadXMLFile(String pathToFile){
        XMLHandler loadXML = new XMLHandler(pathToFile);
        map = loadXML.initWorldMap();
        loadXML.initStations();

        //rides = loadXML.initRides();

    }

    public Ride createNewEmptyRide(User rideOwner, List<Road> roads, int capacity){
        Ride newRide = Ride.createRideFromRoads(rideOwner, roads, capacity);

//        this.rides.add(newRide);  moved to a seperate func

        return newRide;
    }
    public void addRide(Ride rideToAdd){
        this.rides.add(rideToAdd);
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
        trempRequests.add(trempRequest);
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
        return this.rides;
    }

    public List<TrempRequest> getAllTrempRequests(){
        return this.trempRequests;
    }

}
