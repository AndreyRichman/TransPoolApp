package classes;

public class Road {

    private Station startStation;
    private Station endStation;
    private int length;
    private int fuelCost;
    private int maxSpeed;

    public void setEndStation(Station endStation) {
        this.endStation = endStation;
    }

    public void setStartStation(Station startStation) {
        this.startStation = startStation;
    }

    public void setFuelCost(int fuelCost) {
        this.fuelCost = fuelCost;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public int getFuelCost() {
        return fuelCost;
    }

    public int getLength() {
        return length;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public Station getStartStation() {
        return startStation;
    }

    public Station getEndStation() {
        return endStation;
    }
}
