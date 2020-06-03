package transpool.logic.traffic.item;

import transpool.logic.map.structure.Road;
import transpool.logic.time.Schedule;
import transpool.logic.user.Trempist;
import transpool.logic.user.TrempistsManager;

public class PartOfRide {

    private Road road;
    private int capacity;
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
        return 1 + this.trempistsManager.getAllTrempists(onDay).size();
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

    public void updateEndDateTime(){
        int duration = this.road.getDurationInMinutes();

        this.schedule.addMintuesFromStart(duration);
    }

    public void setStartSchedule(Schedule startSchedule){
        this.schedule = startSchedule;
    }

    public Schedule getSchedule() {
        return schedule;
    }
}

