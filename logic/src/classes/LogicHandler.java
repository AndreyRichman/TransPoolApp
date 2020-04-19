package classes;

import java.util.ArrayList;
import java.util.List;

public class LogicHandler {

    List<Ride> rides;
    List<Tremp> tremps;
    WorldMap map;

    public LogicHandler() {
        rides = new ArrayList<>();
        tremps = new ArrayList<>();
        map = new WorldMap();
    }

    public void loadXMLFile(String pathToFile){
        //TODO: add implementation
    }
    public Ride createNewEmptyRide(){
        Ride newRide = new Ride();
        this.rides.add(newRide);

        return newRide;
    }

    public Tremp createNewEmptyTremp(){
        Tremp newTremp = new Tremp();
        tremps.add(newTremp);

        return newTremp;
    }

    public List<Ride> getAllRides(){
        return this.rides;
    }

    public List<Tremp> getAllTrempRequests(){
        return this.tremps;
    }

}
