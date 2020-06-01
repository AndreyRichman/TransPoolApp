package transpool.logic.traffic.item;

import enums.RepeatType;
import transpool.logic.map.structure.Road;
import transpool.logic.time.Schedule;
import transpool.logic.user.Trempist;
import transpool.logic.user.TrempistsManager;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class PartOfRide {

    private Road road;
    private int capacity;
    //private LocalTime startTime;
    //private LocalTime endTime;
    private TrempistsManager trempistsManager;
    private Schedule schedule;

    public PartOfRide(Road road, int capacity) {
        this.road = road;
        this.capacity = capacity;
        this.trempistsManager = new TrempistsManager();
    }

    public void addTrempist(Trempist trempist, int onDay) {
        trempistsManager.addTrempist(trempist, onDay);
    }

    public boolean canAddTrempist(int onDay){
        return getTotalCapacity() - getCurrentCapacity(onDay) > 0;
    }

    public int getTotalCapacity() {
        return capacity;
    }

    public int getCurrentCapacity(int onDay){
        return this.trempistsManager.getAllTrempists(onDay).size();
    }

    public double getPeriodInMinutes(){
        double minutes = 60;
        return (road.getLengthInKM() * minutes) / road.getMaxSpeed();
    }

    public double getLengthOfRoad(){
        return road.getLengthInKM();
    }

    public double getFuelUsage(){
        return road.getFuelUsagePerKilometer();
    }

    public TrempistsManager getTrempistsManager() {
        return trempistsManager;
    }

    public Road getRoad() {
        return road;
    }


//    public void setStartTimeAndDay(LocalTime startTime, int day, RepeatType repeatType) {
//    public void setStartTimeAndDay() {
//        this.schedule = new Schedule(startTime.getHour(), day, repeatType); //TODO: instead of passing all 3 params in the chain, pass Schedule for cloning
////        this.startTime = startTime;
//        int duration = this.road.getDurationInMinutes();
//
//        LocalTime end = startTime.plusMinutes(duration);
//
//        int minutesAtEnd = end.getMinute();
//        int sheerit = minutesAtEnd % 5;
//        int minutesToAdd = sheerit > 2 ? 5 - sheerit: -sheerit;
//        this.endTime = end.plusMinutes(minutesToAdd);
//    }

    public void updateEndDateTime(){
        int duration = this.road.getDurationInMinutes();

        this.schedule.addMintuesFromStart(duration);
    }

    public void setStartSchedule(Schedule startSchedule){
        this.schedule = startSchedule;
    }

//    public LocalTime getStartTime() {
//        return startTime;
//    }

    public Schedule getSchedule() {
        return schedule;
    }

//    public LocalTime getEndTime() {
//        return endTime;
//    }

    public int getStartDay() {return this.schedule.getStartDay();}

    public int getEndDay(){ return this.schedule.getEndDay();}
}

