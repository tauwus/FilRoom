package Model;

public class Room {
    private int id;
    private String name;
    private String location;
    private int capacity;
    private String status; // "tersedia", "pemeliharaan"

    public Room(int id, String name, String location, int capacity, String status) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.status = status;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public int getCapacity() { return capacity; }
    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }
}