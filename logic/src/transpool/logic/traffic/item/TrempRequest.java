package transpool.logic.traffic.item;

import enums.DesiredTimeType;
import enums.RepeatType;
import exception.InvalidDayException;
import transpool.logic.map.structure.Station;
import transpool.logic.time.Schedule;
import transpool.logic.user.User;

import java.time.LocalTime;

public class TrempRequest {
    private static int unique_id = 4000;
    private final int id;
    private final Station startStation;
    private final Station endStation;
    private User user;
    //private LocalTime desiredTime;
    private int day;
    private int maxNumberOfConnections = 0;
    private RideForTremp selectedRide = null;

    private DesiredTimeType desiredTimeType;
    private Schedule schedule;

    public TrempRequest(Station startStation, Station endStation) {
        this.id = unique_id++;
        this.startStation = startStation;
        this.endStation = endStation;

        //this.desiredTime = LocalTime.MIN;
        this.desiredTimeType = DesiredTimeType.DEPART;
        this.day = 1;

    }

//    public void addSubRide(SubRide subRide){
//        this.selectedRide.addSubRide(subRide);
//    }

    public RideForTremp getSelectedRide() {
        return selectedRide;
    }

    public boolean isNotAssignedToRides(){
        return this.selectedRide == null;
    }

    public void setMaxNumberOfConnections(int maxNumberOfConnections) {
        this.maxNumberOfConnections = maxNumberOfConnections;
    }

    public void setUser(User user) {
        this.user = user;
    }

//    public void setDesiredTime(LocalTime departTime) {
//        this.desiredTime = departTime;
//    }

    public void setDesiredDayAndTime(int day, LocalTime time){
        this.schedule = new Schedule(time, day, RepeatType.ONE_TIME);
    }

    public int getID(){
        return this.id;
    }

    public void setDesiredTimeType(DesiredTimeType desiredTimeType) {
        this.desiredTimeType = desiredTimeType;
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

    public LocalTime getDesiredTime() {
        return this.schedule.getStartTime();
    }

    public DesiredTimeType getDesiredTimeType() {
        return desiredTimeType;
    }

    public void assignRides(RideForTremp ridesToAssign){
        this.selectedRide = ridesToAssign;
        this.schedule.setEndDateTime(ridesToAssign.getArriveDateTime());
    }

    public void setDay(int day) throws InvalidDayException {
        if (day < 1)
            throw new InvalidDayException(day);

        this.day = day;
    }

    public int getDay() {
        return day;
    }
}
