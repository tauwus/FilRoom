package obj;
import java.util.List;
import java.util.Map;

public class Booking {
    private String bookingID;
    private User user;
    private String purpose;
    private List<BookingItem> items;

    public enum Status {
        
    }

    public Boolean create(Map<String, Object> detailPeminjaman) {
        
    }

    public List<Booking> findByUser(String userID) {
        
    }

    public List<Booking> findByStatus(Status status) {
        
    }

    public Boolean updateStatus(String bookingID, Status status) {
        
    }

    public Map<String, Object> getBookingSummary(Map<String, Object> criteria) {
        
    }
}
