package classes;

import java.util.ArrayList;

public class PartOfRide {

    private Road road;
    private int capacity;
    private ArrayList<Trempist> trempists;

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

}

