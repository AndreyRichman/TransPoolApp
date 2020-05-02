package classes;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TrempRequest {
    private static int unique_id = 4000;
    private final int id;
    private final Station startStation;
    private final Station endStation;
    private User user;
    private LocalTime departTime;
    private LocalTime arriveTime = null;
    private int day;
    private int maxNumberOfConnections = 0;
    private List<SubRide> subRides = null;

    public TrempRequest(Station startStation, Station endStation) {
        this.id = unique_id++;
        this.startStation = startStation;
        this.endStation = endStation;

        this.departTime = LocalTime.MIN;
    }

    public void addSubRide(SubRide subRide){
        if (this.subRides == null){
            this.subRides = new ArrayList<>();
        }
        this.subRides.add(subRide);
    }

    public List<SubRide> getSubRides() {
        return subRides;
    }

    public boolean isNotAssignedToRides(){
        return  this.subRides == null || this.subRides.size() == 0;
    }

    public void setMaxNumberOfConnections(int maxNumberOfConnections) {
        this.maxNumberOfConnections = maxNumberOfConnections;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setDepartTime(LocalTime departTime) {
        this.departTime = departTime;
    }

    public void setArriveTime(LocalTime arriveTime) {
        this.arriveTime = arriveTime;
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

    public int getMaxNumberOfConnections() {
        return maxNumberOfConnections;
    }

    public LocalTime getDepartTime() {
        return departTime;
    }
}
