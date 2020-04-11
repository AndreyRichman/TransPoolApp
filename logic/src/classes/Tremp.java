package classes;

public class Tremp {
    private static int unique_id = 4000;

    private int id;

    public Tremp() {
        this.id = unique_id++;
    }

    public int getID(){
        return this.id;
    }
}
