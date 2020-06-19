package main.window.map.component.details;

import java.util.LinkedList;
import java.util.List;

/*
Dummy container to hold information needed to be shown upon clicking a station
 */
public class StationDetailsDTO {

    private String name;
    private int x;
    private int y;
    private List<String> allRiders;
    private List<String> onTremp;
    private List<String> offTremp;

    public StationDetailsDTO() {
        this.allRiders = new LinkedList<>();
        this.onTremp = new LinkedList<>();
        this.offTremp = new LinkedList<>();
    }

    public void setOnTremp(List<String> onTremp) {
        this.onTremp = onTremp;
    }

    public void setOffTremp(List<String> offTremp) {
        this.offTremp = offTremp;
    }

    public List<String> getOffTremp() {
        return offTremp;
    }

    public List<String> getOnTremp() {
        return onTremp;
    }

    public void setAllRiders(List<String> allRiders) {
        this.allRiders = allRiders;
    }



    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<String> getAllRiders() {
        return allRiders;
    }

    public void clearAllLists() {
        this.offTremp = new LinkedList<>();
        this.onTremp = new LinkedList<>();
        this.allRiders = new LinkedList<>();
    }
}
