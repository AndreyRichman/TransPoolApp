package classes;

import java.util.ArrayList;

public class PartOfRide {

    private Road road;
    private int capacity;
    private ArrayList<Tremp> Trempists;

    public ArrayList<Tremp> getTrempists() {
        return Trempists;
    }

    public int getCapacity() {
        return capacity;
    }

    public Road getRoad() {
        return road;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setRoad(Road road) {
        this.road = road;
    }

    public void setTrempists(ArrayList<Tremp> trempists) {
        Trempists = trempists;
    }
}

