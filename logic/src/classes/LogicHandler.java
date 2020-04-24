package classes;

import java.util.ArrayList;
import java.util.List;

public class LogicHandler {

    List<Ride> rides;
    List<TrempRequest> trempRequests;
    WorldMap map;

    public LogicHandler() {
        rides = new ArrayList<>();
        trempRequests = new ArrayList<>();
    }

    public void loadXMLFile(String pathToFile){
        //TODO: add implementation
    }
    public Ride createNewEmptyRide(){
        Ride newRide = new Ride();
        this.rides.add(newRide);

        return newRide;
    }

    public TrempRequest createNewEmptyTremp(){
        TrempRequest newTrempRequest = new TrempRequest();
        trempRequests.add(newTrempRequest);

        return newTrempRequest;
    }

    public List<Ride> getAllRides(){
        return this.rides;
    }

    public List<TrempRequest> getAllTrempRequests(){
        return this.trempRequests;
    }

}
