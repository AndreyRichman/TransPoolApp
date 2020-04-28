package classes;

import exception.RideNotExistsException;
import exception.TrempRequestNotExist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrafficManager {


    List<Ride> rides;
    List<TrempRequest> trempRequests;

    Map<Integer, Ride> mapIdToRide;
    Map<Integer, TrempRequest> mapIdToTrempReuqest;

    public TrafficManager() {
        this.rides = new ArrayList<>();
        this.trempRequests = new ArrayList<>();
        this.mapIdToRide = new HashMap<>();
        this.mapIdToTrempReuqest = new HashMap<>();
    }

    public List<Ride> getRides() {
        return rides;
    }

    public Ride getRideByID(int idOfRide) throws RideNotExistsException {
        if (!this.mapIdToRide.containsKey(idOfRide)){
            throw new RideNotExistsException(idOfRide);
        }

        return this.mapIdToRide.get(idOfRide);
    }

    public void addRide(Ride ride){
        this.rides.add(ride);
        this.mapIdToRide.put(ride.getID(), ride);
    }

    public List<TrempRequest> getTrempRequests() {
        return trempRequests;
    }

    public void addTrempRequest(TrempRequest trempRequest){
        this.trempRequests.add(trempRequest);
        this.mapIdToTrempReuqest.put(trempRequest.getID(), trempRequest);
    }

    public TrempRequest getTrempRequestById(int idOfTrempRequest) throws TrempRequestNotExist{
        if (!this.mapIdToTrempReuqest.containsKey(idOfTrempRequest))
            throw new TrempRequestNotExist(idOfTrempRequest);

        return this.mapIdToTrempReuqest.get(idOfTrempRequest);
    }


}
