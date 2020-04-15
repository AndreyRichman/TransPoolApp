package classes;

public class Road {

    private Station startStation;
    private Station endStation;
    private int length;
    private int fuelCost;
    private int maxSpeed;

    public Road(Station startStation, Station endStation){
        this.startStation = startStation;
        this.endStation = endStation;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setFuelCost(int fuelCost) {
        this.fuelCost = fuelCost;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public int getLength() {
        return length;
    }

    public int getFuelCost() {
        return fuelCost;
    }

    public Station getStartStation() {
        return startStation;
    }

    public Station getEndStation() {
        return endStation;
    }
}
