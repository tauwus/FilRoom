package obj;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class BookingItem {
    private String itemID;
    private String bookingID;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Status status;

    public enum Status {
        
    }

    public Boolean create(String itemID, String bookingID, LocalDateTime startTime, LocalDateTime endTime) {
        
    }

    public Boolean isBooked(String itemID, LocalDateTime startTime, LocalDateTime endTime) {
        
    }

    public BookingItem findByBookingIDs(List<String> bookingIDs) {
        
    }

    public List<BookingItem> getDetailsForBookings(List<Booking> bookings) {
        
    }

    public List<Object> getBookingData(Map<String, Object> criteria) {
        
    }
}
