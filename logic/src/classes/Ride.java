package classes;

import java.util.List;

public class Ride {

    private static int unique_id = 6000;
    private final int id;
    private List<PartOfRide> parts;

    public Ride() {

        this.id = unique_id++;
    }

    public int getID(){

        return this.id;
    }
}
