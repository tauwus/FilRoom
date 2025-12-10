package Model;

import java.sql.Date;
import java.sql.Time;

public class BookingItem {
    private int id;
    private Room room;
    private Date usageDate;
    private Time startTime;
    private Time endTime;

    public BookingItem(int id, Room room, Date usageDate, Time startTime, Time endTime) {
        this.id = id;
        this.room = room;
        this.usageDate = usageDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getId() { return id; }

    public Room getRoom() { return room; }
    public void setRoom(Room room) { this.room = room; }

    public Date getUsageDate() { return usageDate; }
    public Time getStartTime() { return startTime; }
    public Time getEndTime() { return endTime; }
}
