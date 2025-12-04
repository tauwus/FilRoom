package Model;

public class Room {
    private int id;
    private String name;
    private String location;
    private int capacity;
    private String description;
    private String facilities;
    private String status; // "tersedia", "pemeliharaan"

    public Room(int id, String name, String location, int capacity, String description, String facilities, String status) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.description = description;
        this.facilities = facilities;
        this.status = status;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public int getCapacity() { return capacity; }
    public String getDescription() { return description; }
    public String getFacilities() { return facilities; }
    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }
}