package Controller;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingControl {

    public List<String[]> getRecentBookings(int userId) {
        List<String[]> bookings = new ArrayList<>();
        String sql = "SELECT TOP 3 b.status_peminjaman, bd.tanggal_pemakaian, bd.waktu_mulai, bd.waktu_selesai, r.nama_ruangan " +
                     "FROM bookings b " +
                     "JOIN booking_details bd ON b.booking_id = bd.booking_id " +
                     "JOIN rooms r ON bd.room_id = r.room_id " +
                     "WHERE b.user_id = ? " +
                     "ORDER BY b.tanggal_pengajuan DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String status = rs.getString("status_peminjaman");
                String date = rs.getDate("tanggal_pemakaian").toString();
                String start = rs.getTime("waktu_mulai").toString();
                String end = rs.getTime("waktu_selesai").toString();
                String room = rs.getString("nama_ruangan");
                
                if (start.length() > 5) start = start.substring(0, 5);
                if (end.length() > 5) end = end.substring(0, 5);

                bookings.add(new String[]{room, date, start + " - " + end, status});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public boolean createBooking(int userId, int roomId, LocalDate date, String startTime, String endTime, 
                                 String purpose, int participants, String description) {
        
        Connection conn = null;
        PreparedStatement stmtBooking = null;
        PreparedStatement stmtDetail = null;
        ResultSet generatedKeys = null;

        String insertBookingSQL = "INSERT INTO bookings (user_id, keterangan_kegiatan, status_peminjaman) VALUES (?, ?, ?)";
        String insertDetailSQL = "INSERT INTO booking_details (booking_id, room_id, tanggal_pemakaian, waktu_mulai, waktu_selesai) VALUES (?, ?, ?, ?, ?)";

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            String fullDescription = purpose + " (" + participants + " org) - " + description;
            
            stmtBooking = conn.prepareStatement(insertBookingSQL, Statement.RETURN_GENERATED_KEYS);
            stmtBooking.setInt(1, userId);
            stmtBooking.setString(2, fullDescription);
            stmtBooking.setString(3, "Menunggu");
            
            int affectedRows = stmtBooking.executeUpdate();
            if (affectedRows == 0) throw new SQLException("Gagal membuat booking.");

            generatedKeys = stmtBooking.getGeneratedKeys();
            int bookingId = 0;
            if (generatedKeys.next()) {
                bookingId = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Gagal mengambil ID booking.");
            }

            stmtDetail = conn.prepareStatement(insertDetailSQL);
            stmtDetail.setInt(1, bookingId);
            stmtDetail.setInt(2, roomId);
            stmtDetail.setDate(3, java.sql.Date.valueOf(date));
            stmtDetail.setString(4, startTime + ":00");
            stmtDetail.setString(5, endTime + ":00");
            
            stmtDetail.executeUpdate();

            conn.commit();
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (generatedKeys != null) generatedKeys.close();
                if (stmtBooking != null) stmtBooking.close();
                if (stmtDetail != null) stmtDetail.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

     public int countActiveBookings(int userId) {
        int count = 0;
        // Kita anggap "Aktif" adalah yang statusnya Menunggu atau Disetujui
        String sql = "SELECT COUNT(*) AS total FROM bookings " +
                     "WHERE user_id = ? AND status_peminjaman IN ('Menunggu', 'Disetujui')";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }
}