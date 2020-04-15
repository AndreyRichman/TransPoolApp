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

    public void setStartStation(Station startStation) {
        this.startStation = startStation;
    }

    public void setEndStation(Station endStation) {
        this.endStation = endStation;
    }

    public void setAllowMultyRides(boolean allowMultyRides) {
        this.allowMultyRides = allowMultyRides;
    }

    public void setEndTimeOfRide(String endTimeOfRide) {
        this.endTimeOfRide = endTimeOfRide;
    }

    public void setRides(ArrayList<Ride> rides) {
        Rides = rides;
    }

    public void setStartTimeOfRide(String startTimeOfRide) {
        this.startTimeOfRide = startTimeOfRide;
    }

    public void setTremptistName(User tremptistName) {
        this.tremptistName = tremptistName;
    }

    public static void setUnique_id(int unique_id) {
        Tremp.unique_id = unique_id;
    }

    public String getEndTimeOfRide() {
        return endTimeOfRide;
    }

    public ArrayList<Ride> getRides() {
        return Rides;
    }

    public int getId() {
        return id;
    }

    public static int getUnique_id() {
        return unique_id;
    }

    public Station getEndStation() {
        return endStation;
    }

    public Station getStartStation() {
        return startStation;
    }

    public String getStartTimeOfRide() {
        return startTimeOfRide;
    }

    public User getTremptistName() {
        return tremptistName;
    }

    public Tremp() {
        this.id = unique_id++;
    }

    public int getID(){
        return this.id;
    }


}
