package main.java.com.TransPool.logic.traffic;

import exception.RideNotExistsException;
import exception.TrempRequestNotExist;
import main.java.com.TransPool.logic.map.structure.Station;
import main.java.com.TransPool.logic.traffic.item.SubRide;
import main.java.com.TransPool.logic.traffic.item.Ride;
import main.java.com.TransPool.logic.traffic.item.TrempRequest;

import java.util.*;
import java.util.stream.Collectors;


public class TrafficManager {


    List<Ride> allRides;
    List<TrempRequest> allTrempRequests;

    Map<Integer, Ride> mapIdToRide;
    Map<Integer, TrempRequest> mapIdToTrempReuqest;

    public TrafficManager() {

        this.allRides = new ArrayList<>();
        this.allTrempRequests = new ArrayList<>();
        this.mapIdToRide = new HashMap<>();
        this.mapIdToTrempReuqest = new HashMap<>();
    }

    public List<Ride> getAllRides() {
        return allRides;
    }

    public Ride getRideByID(int idOfRide) throws RideNotExistsException {
        if (!this.mapIdToRide.containsKey(idOfRide)){
            throw new RideNotExistsException(idOfRide);
        }

        return this.mapIdToRide.get(idOfRide);
    }

    public void addRide(Ride ride){
        this.allRides.add(ride);
        this.mapIdToRide.put(ride.getID(), ride);
    }

    public List<TrempRequest> getAllTrempRequests() {
        return allTrempRequests;
    }

    public void addTrempRequest(TrempRequest trempRequest){
        this.allTrempRequests.add(trempRequest);
        this.mapIdToTrempReuqest.put(trempRequest.getID(), trempRequest);
    }

    public TrempRequest getTrempRequestById(int idOfTrempRequest) throws TrempRequestNotExist{
        if (!this.mapIdToTrempReuqest.containsKey(idOfTrempRequest))
            throw new TrempRequestNotExist(idOfTrempRequest);

        return this.mapIdToTrempReuqest.get(idOfTrempRequest);
    }

    private List<List<SubRide>> getRideOptionsWithNoConnections(Station from, Station to){
        List<List<SubRide>> noConnectionOptions = new LinkedList<>();
        this.allRides.stream()
                .filter((ride -> ride.containsValidRoute(from, to)))
                .forEach(ride -> {
                    ArrayList<SubRide> subRidesOption = new ArrayList<>();
                    subRidesOption.add(ride.getSubRide(from, to));
                    noConnectionOptions.add(subRidesOption);
                });

        return noConnectionOptions;
    }

    private List<List<SubRide>> getRideOptionsWithConnections(int maxConnections, Station from, Station to){
        List<List<SubRide>> rideOptions = new LinkedList<>();
        List<Ride> relevantRides = this.allRides.stream()
                .filter((ride)-> !ride.containsValidRoute(from, to)).collect(Collectors.toList());

        //TODO: write algorithm to groups of rides and filter by max connections

        return rideOptions;
    }
    public List<List<SubRide>> getRideOptions(int maxConnections, Station from, Station to){
        List<List<SubRide>> allRideOptions = new ArrayList<>();

        allRideOptions.addAll(getRideOptionsWithNoConnections(from, to));
        allRideOptions.addAll(getRideOptionsWithConnections(maxConnections, from, to));

        return allRideOptions;
    }
}
