package classes;

import enums.TrempPartType;

import java.util.ArrayList;
import java.util.List;

public class PartOfRide {

    private Road road;
    private int capacity;
    private ArrayList<Trempist> trempists;

    public PartOfRide(Road road, int capacity) {
        this.road = road;
        this.capacity = capacity;
        this.trempists = new ArrayList<>();
    }

    public void addTrempist(User user, TrempPartType partType){
        trempists.add(new Trempist(user, partType));
    }

    public ArrayList<Trempist> getTrempists() {
        return trempists;
    }

    public Road getRoad() {
        return road;
    }

    public int getCapacity() {
        return capacity;
    }
  
      public void setRoad(Road road) {
        this.road = road;
    }
  
      public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
  
    }
}

