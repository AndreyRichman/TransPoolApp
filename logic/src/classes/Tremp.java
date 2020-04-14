package classes;


import java.util.ArrayList;

public class Tremp {
    private static int unique_id = 4000;
    private final int id;
    private Station startStation;
    private Station endStation;
    private User tremptistName;
    private String startTimeOfRide;
    private String endTimeOfRide;
    private boolean allowMultyRides;
    private ArrayList<Ride> Rides;

    public Tremp() {
        this.id = unique_id++;
    }

    public int getID(){
        return this.id;
    }


}
