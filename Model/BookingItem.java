package Model;

import java.sql.Date;
import java.sql.Time;

public class BookingItem {
    private int id;
    private int bookingId;
    private int roomId;
    private Date usageDate;
    private Time startTime;
    private Time endTime;

    public BookingItem(int id, int bookingId, int roomId, Date usageDate, Time startTime, Time endTime) {
        this.id = id;
        this.bookingId = bookingId;
        this.roomId = roomId;
        this.usageDate = usageDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getId() { return id; }
    public int getBookingId() { return bookingId; }
    public int getRoomId() { return roomId; }
    public Date getUsageDate() { return usageDate; }
    public Time getStartTime() { return startTime; }
    public Time getEndTime() { return endTime; }
}
