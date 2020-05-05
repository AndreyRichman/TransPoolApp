package main.java.com.TransPool.logic.traffic.item;

import main.java.com.TransPool.logic.map.structure.Road;
import main.java.com.TransPool.logic.user.Trempist;
import main.java.com.TransPool.logic.user.TrempistsManager;

import java.time.LocalTime;

public class PartOfRide {

    private Road road;
    private int capacity;
    private LocalTime startTime;
    private LocalTime endTime;
    private TrempistsManager trempistsManager;

    public PartOfRide(Road road, int capacity) {
        this.road = road;
        this.capacity = capacity;
        this.trempistsManager = new TrempistsManager();
    }

    public void addTrempist(Trempist trempist) {
        trempistsManager.addTrempist(trempist);
    }

    public boolean canAddTrempist(){
        return getTotalCapacity() - getCurrentCapacity() > 0;
    }

    public int getTotalCapacity() {
        return capacity;
    }

    public int getCurrentCapacity(){
        return this.trempistsManager.getAllTrempists().size();
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

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
        int duration = this.road.getDurationInMinutes();
        LocalTime end = startTime.plusMinutes(duration);

        int minutesAtEnd = end.getMinute();
        int sheerit = minutesAtEnd % 5;
        int minutesToAdd = sheerit > 2 ? 5 - sheerit: -sheerit;
        this.endTime = end.plusMinutes(minutesToAdd);
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }
}

