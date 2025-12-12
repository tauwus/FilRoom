package Model;

import java.sql.Timestamp;

public class Booking {
    private int id;
    private User user;
    private Admin admin;
    private Timestamp submissionDate;
    private String activityDescription;
    private BookingStatus status; // "menunggu_persetujuan", "disetujui", "ditolak", "dibatalkan", "selesai"
    private Timestamp approvalDate;
    private String adminNote;
    private BookingItem bookingItem;

    public Booking(int id, User user, Admin admin, Timestamp submissionDate, String activityDescription, String status, Timestamp approvalDate, String adminNote) {
        this.id = id;
        this.user = user;
        this.admin = admin;
        this.submissionDate = submissionDate;
        this.activityDescription = activityDescription;
        this.status = BookingStatus.fromString(status);
        this.approvalDate = approvalDate;
        this.adminNote = adminNote;
    }

    public Booking(int id, User user, Admin admin, Timestamp submissionDate, String activityDescription, BookingStatus status, Timestamp approvalDate, String adminNote) {
        this.id = id;
        this.user = user;
        this.admin = admin;
        this.submissionDate = submissionDate;
        this.activityDescription = activityDescription;
        this.status = status;
        this.approvalDate = approvalDate;
        this.adminNote = adminNote;
    }

    public int getId() { return id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Admin getAdmin() { return admin; }
    public void setAdmin(Admin admin) { this.admin = admin; }

    public Timestamp getSubmissionDate() { return submissionDate; }
    public String getActivityDescription() { return activityDescription; }
    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }

    public Timestamp getApprovalDate() { return approvalDate; }
    public String getAdminNote() { return adminNote; }
    
    public BookingItem getBookingItem() { return bookingItem; }
    public void setBookingItem(BookingItem bookingItem) { this.bookingItem = bookingItem; }

    @Override
    public String toString() {
        return "Booking #" + id + " - " + activityDescription + " (" + status + ")";
    }
}
