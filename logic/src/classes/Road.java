package classes;

public class Road {

    private Station startStation;
    private Station endStation;
    private int lengthInKM;
    private double fuelUsagePerKilometer;
    private int maxSpeed;

    public Road(Station startStation, Station endStation){
        this.startStation = startStation;
        this.endStation = endStation;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public void setLengthInKM(int lengthInKM) {
        this.lengthInKM = lengthInKM;
    }

    public void setFuelUsagePerKilometer(double fuelUsagePerKilometer) {
        this.fuelUsagePerKilometer = fuelUsagePerKilometer;
    }


    public int getMaxSpeed() {
        return maxSpeed;
    }

    public int getLengthInKM() {
        return lengthInKM;
    }

    public double getFuelUsagePerKilometer() {
        return fuelUsagePerKilometer;
    }

    public Station getStartStation() {
        return startStation;
    }

    public Station getEndStation() {
        return endStation;
    }
}
