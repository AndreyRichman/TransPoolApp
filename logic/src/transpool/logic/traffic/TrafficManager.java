package transpool.logic.traffic;

import exception.RideNotExistsException;
import exception.TrempRequestNotExist;
import transpool.logic.map.structure.Station;
import transpool.logic.traffic.item.RideForTremp;
import transpool.logic.traffic.item.SubRide;
import transpool.logic.traffic.item.Ride;
import transpool.logic.traffic.item.TrempRequest;

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

    private List<RideForTremp> getRideOptionsWithNoConnections(Station from, Station to, int onDay){
        List<RideForTremp> noConnectionOptions = new LinkedList<>();
        this.allRides.stream()
                .filter((ride -> ride.containsValidRoute(from, to, onDay)))
                .forEach(ride -> {
                    ArrayList<SubRide> subRidesOption = new ArrayList<>();
                    subRidesOption.add(ride.getSubRide(from, to));
                    noConnectionOptions.add(new RideForTremp(subRidesOption));
                });

        return noConnectionOptions;
    }

    private List<RideForTremp> getRideOptionsWithConnections(int maxConnections, Station from, Station to, int onDay){
        List<RideForTremp> rideOptions = new LinkedList<>();
        List<Ride> relevantRides = this.allRides.stream()
                .filter((ride)-> !ride.containsValidRoute(from, to, onDay)).collect(Collectors.toList());

        //TODO: write algorithm to groups of rides and filter by max connections

        return rideOptions;
    }
    public List<RideForTremp> getRideOptions(int maxConnections, Station from, Station to, int onDay){
        List<RideForTremp> allRideOptions = new ArrayList<>();

        allRideOptions.addAll(getRideOptionsWithNoConnections(from, to, onDay));
        allRideOptions.addAll(getRideOptionsWithConnections(maxConnections, from, to, onDay));

        return allRideOptions;
    }
}
