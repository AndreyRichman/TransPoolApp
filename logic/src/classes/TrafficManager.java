package classes;

import exception.RideNotExistsException;
import exception.TrempRequestNotExist;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TrafficManager {


    List<Ride> allRides;
    List<TrempRequest> allTrempRequests;

    Map<Integer, Ride> mapIdToRide;
    Map<Integer, TrempRequest> mapIdToTrempReuqest;


//    WorldMap worldMap;

    public TrafficManager() {
        this.allRides = new ArrayList<>();
        this.allTrempRequests = new ArrayList<>();
//        this.worldMap = worldMap;
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

    private List<List<Ride.SubRide>> getRideOptionsWithNoConnections(Station from, Station to){
        List<List<Ride.SubRide>> noConnectionOptions = new LinkedList<>();
        this.allRides.stream()
                .filter((ride -> ride.containsValidRoute(from, to)))
                .forEach(ride -> {
                    noConnectionOptions.add(new ArrayList<Ride.SubRide>(1){{
                        add(ride.getSubRide(from, to));
                    }});
                });

        return noConnectionOptions;
    }

    private List<List<Ride.SubRide>> getRideOptionsWithConnections(int maxConnections, Station from, Station to){
        List<List<Ride.SubRide>> rideOptions = new LinkedList<>();
        List<Ride> relevantRides = this.allRides.stream()
                .filter((ride)-> !ride.containsValidRoute(from, to)).collect(Collectors.toList());

        //TODO: write algorithm to groups of rides

        return rideOptions;
    }
    public List<List<Ride.SubRide>> getRideOptions(int maxConnections, Station from, Station to){
        List<List<Ride.SubRide>> allRideOptions = new ArrayList<>();

        allRideOptions.addAll(getRideOptionsWithNoConnections(from, to));
        allRideOptions.addAll(getRideOptionsWithConnections(maxConnections, from, to));

        return allRideOptions;
    }




}
