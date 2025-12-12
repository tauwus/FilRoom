package Controller;

import Model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminBookingControl {

    public List<Booking> getPendingBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.booking_id, b.tanggal_pengajuan, b.keterangan_kegiatan, b.status_peminjaman, " +
                     "u.user_id, u.nama_lengkap, u.nim_nip, u.email, u.no_telepon, u.status_akun, " +
                     "bd.room_id, r.nama_ruangan, bd.tanggal_pemakaian, bd.waktu_mulai, bd.waktu_selesai " +
                     "FROM bookings b " +
                     "JOIN civitas_akademik u ON b.user_id = u.user_id " +
                     "JOIN booking_details bd ON b.booking_id = bd.booking_id " +
                     "JOIN rooms r ON bd.room_id = r.room_id " +
                     "WHERE b.status_peminjaman = 'Menunggu' " +
                     "ORDER BY b.tanggal_pengajuan ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                CivitasAkademik user = new CivitasAkademik(
                    rs.getInt("user_id"),
                    rs.getString("nama_lengkap"),
                    rs.getString("nim_nip"),
                    rs.getString("email"),
                    rs.getString("no_telepon"),
                    rs.getString("status_akun")
                );
                
                Booking booking = new Booking(
                    rs.getInt("booking_id"),
                    user,
                    null,
                    rs.getTimestamp("tanggal_pengajuan"),
                    rs.getString("keterangan_kegiatan"),
                    BookingStatus.MENUNGGU_PERSETUJUAN,
                    null,
                    null
                );
                
                Room room = new Room(rs.getInt("room_id"), rs.getString("nama_ruangan"), "", 0);
                BookingItem item = new BookingItem(
                    0, 
                    room, 
                    rs.getDate("tanggal_pemakaian"), 
                    rs.getTime("waktu_mulai"), 
                    rs.getTime("waktu_selesai")
                );
                booking.setBookingItem(item);
                
                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }
    
    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.booking_id, b.tanggal_pengajuan, b.keterangan_kegiatan, b.status_peminjaman, " +
                     "u.user_id, u.nama_lengkap, u.nim_nip, u.email, u.no_telepon, u.status_akun " +
                     "FROM bookings b " +
                     "JOIN civitas_akademik u ON b.user_id = u.user_id " +
                     "ORDER BY b.tanggal_pengajuan DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                CivitasAkademik user = new CivitasAkademik(
                    rs.getInt("user_id"),
                    rs.getString("nama_lengkap"),
                    rs.getString("nim_nip"),
                    rs.getString("email"),
                    rs.getString("no_telepon"),
                    rs.getString("status_akun")
                );
                
                Booking booking = new Booking(
                    rs.getInt("booking_id"),
                    user,
                    null,
                    rs.getTimestamp("tanggal_pengajuan"),
                    rs.getString("keterangan_kegiatan"),
                    rs.getString("status_peminjaman"),
                    null,
                    null
                );
                
                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }
    
    public boolean approveBooking(int bookingId) {
        Admin currentAdmin = AuthControl.getCurrentAdmin();
        if (currentAdmin == null) return false;
        return updateBookingStatus(bookingId, currentAdmin.getId(), BookingStatus.DISETUJUI);
    }
    
    public boolean rejectBooking(int bookingId) {
        Admin currentAdmin = AuthControl.getCurrentAdmin();
        if (currentAdmin == null) return false;
        return updateBookingStatus(bookingId, currentAdmin.getId(), BookingStatus.DITOLAK);
    }

    public boolean updateBookingStatus(int bookingId, int adminId, BookingStatus status) {
        String sql = "UPDATE bookings SET status_peminjaman = ?, approved_by_admin_id = ?, tanggal_approval = GETDATE() WHERE booking_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status.toString());
            stmt.setInt(2, adminId);
            stmt.setInt(3, bookingId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public int countPendingBookings() {
        String sql = "SELECT COUNT(*) FROM bookings WHERE status_peminjaman = 'Menunggu'";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}