package classes;

public class User {

    private static int uniqueID = 100;
    private final int id;
    private String name;

    public User() {
        this.id = uniqueID++;
    }

    public int getID() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
