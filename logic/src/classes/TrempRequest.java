package classes;


import java.util.List;

public class TrempRequest {
    private static int unique_id = 4000;
    private final int id;
    private Station startStation;
    private Station endStation;
    private User user;
    private String startTimeOfRide;
    private String endTimeOfRide;
    private boolean allowMultiRides;
    private List<Ride.SubRide> subRides;

    public TrempRequest(Station startStation, Station endStation) {
        this.id = unique_id++;
        this.startStation = startStation;
        this.endStation = endStation;
    }

    public void addSubRide(Ride.SubRide subRide){
        this.subRides.add(subRide);
    }


    public int getID(){
        return this.id;
    }

    public User getUser() {
        return user;
    }

    public Station getStartStation() {
        return startStation;
    }

    public Station getEndStation() {
        return endStation;
    }
}
