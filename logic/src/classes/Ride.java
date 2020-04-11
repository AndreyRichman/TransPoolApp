package classes;

public class Ride {

    private static int unique_id = 6000;
    private int id;

    public Ride() {

        this.id = unique_id++;
    }

    public int getID(){

        return this.id;
    }
}
