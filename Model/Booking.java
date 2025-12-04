package Model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Booking {
    private int id;
    private int userId;
    private Integer approvedByAdminId; // Can be null
    private Timestamp submissionDate;
    private String activityDescription;
    private String status; // "menunggu_persetujuan", "disetujui", "ditolak", "dibatalkan", "selesai"
    private Timestamp approvalDate;
    private String adminNote;
    private List<BookingItem> bookingItems;

    public Booking(int id, int userId, Integer approvedByAdminId, Timestamp submissionDate, String activityDescription, String status, Timestamp approvalDate, String adminNote) {
        this.id = id;
        this.userId = userId;
        this.approvedByAdminId = approvedByAdminId;
        this.submissionDate = submissionDate;
        this.activityDescription = activityDescription;
        this.status = status;
        this.approvalDate = approvalDate;
        this.adminNote = adminNote;
        this.bookingItems = new ArrayList<>();
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public Integer getApprovedByAdminId() { return approvedByAdminId; }
    public Timestamp getSubmissionDate() { return submissionDate; }
    public String getActivityDescription() { return activityDescription; }
    public String getStatus() { return status; }
    public Timestamp getApprovalDate() { return approvalDate; }
    public String getAdminNote() { return adminNote; }
    public List<BookingItem> getBookingItems() { return bookingItems; }

    public void setBookingItems(List<BookingItem> bookingItems) {
        this.bookingItems = bookingItems;
    }
    
    public void addBookingItem(BookingItem item) {
        this.bookingItems.add(item);
    }
}
