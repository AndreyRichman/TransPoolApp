package classes;

public class Road {

    private Station startStation;
    private Station endStation;
    private int length;
    private int fuelCost;
    private int maxSpeed;

    public Road(){

    }

    public Station getStartStation() {
        return startStation;
    }

    public Station getEndStation() {
        return endStation;
    }
}
