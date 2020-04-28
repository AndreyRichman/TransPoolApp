package classes;

import java.time.LocalTime;
import java.util.ArrayList;

public class PartOfRide {

    private Road road;
    private int capacity;
    private ArrayList<Trempist> trempists;
    private LocalTime startTime;
    private LocalTime endTime;

    public PartOfRide(Road road, int capacity) {
        this.road = road;
        this.capacity = capacity;
        this.trempists = new ArrayList<>(capacity);
    }

    public void addTrempist(Trempist trempist) {
        trempists.add(trempist);
    }

    public boolean canAddTrempist(){
        return getTotalCapacity() - getCurrentCapacity() > 0;
    }

    public int getTotalCapacity() {
        return capacity;
    }

    public int getCurrentCapacity(){
        return this.trempists.size();
    }

    public double getPeriodInMinutes(){
        double minutes = 60;
        return (road.getLengthInKM() * minutes) / road.getMaxSpeed();
    }

    public int getLengthOfRoad(){
        return road.getLengthInKM();
    }

    public double getFuelUsage(){
        return road.getFuelUsagePerKilometer();
    }

    public ArrayList<Trempist> getAllTrempists() {
        return this.trempists;
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

