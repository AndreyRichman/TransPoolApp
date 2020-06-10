package main.window.map.component.details;

import java.util.List;

/*
Dummy container to hold information needed to be shown upon clicking a station
 */
public class StationDetailsDTO {

    private String name;
    private int x;
    private int y;
    private List<String> drives;

    public StationDetailsDTO(List<String> drives) {
        this.drives = drives;
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

    public List<String> getDrives() {
        return drives;
    }
}
