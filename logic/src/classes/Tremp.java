package classes;


import java.util.ArrayList;
import java.util.List;

public class Tremp {
    private static int unique_id = 4000;
    private final int id;
    private Station startStation;
    private Station endStation;
    private User trempistName;
    private String startTimeOfRide;
    private String endTimeOfRide;
    private boolean allowMultiRides;
    private List<SubRide> subRides;

    public Tremp(Station startStation, Station endStation) {
        this.id = unique_id++;
        this.startStation = startStation;
        this.endStation = endStation;
    }


    public int getID(){
        return this.id;
    }

    public User getTrempistName() {
        return trempistName;
    }

    public Station getStartStation() {
        return startStation;
    }

    public Station getEndStation() {
        return endStation;
    }
}
