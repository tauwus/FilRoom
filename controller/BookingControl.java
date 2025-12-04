package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                String date = rs.getDate("tanggal_pemakaian").toString(); // YYYY-MM-DD
                String start = rs.getTime("waktu_mulai").toString();
                String end = rs.getTime("waktu_selesai").toString();
                String room = rs.getString("nama_ruangan");
                
                // Format Time (HH:mm)
                if (start.length() > 5) start = start.substring(0, 5);
                if (end.length() > 5) end = end.substring(0, 5);

                bookings.add(new String[]{room, date, start + " - " + end, status});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }
}