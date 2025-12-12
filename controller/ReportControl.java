package Controller;

import Model.BookingStatus;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller untuk menangani laporan dan statistik.
 * Menyediakan data agregat untuk dashboard admin.
 */
public class ReportControl {

    /**
     * Mendapatkan statistik booking per status
     */
    public Map<BookingStatus, Integer> getBookingCountByStatus() {
        Map<BookingStatus, Integer> stats = new HashMap<>();
        String sql = "SELECT status_peminjaman, COUNT(*) as total FROM bookings GROUP BY status_peminjaman";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                BookingStatus status = BookingStatus.fromString(rs.getString("status_peminjaman"));
                stats.put(status, rs.getInt("total"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stats;
    }

    /**
     * Mendapatkan jumlah booking per bulan (untuk chart)
     */
    public List<int[]> getMonthlyBookingStats(int year) {
        List<int[]> monthlyStats = new ArrayList<>();
        String sql = "SELECT MONTH(tanggal_pengajuan) as bulan, COUNT(*) as total " +
                     "FROM bookings WHERE YEAR(tanggal_pengajuan) = ? " +
                     "GROUP BY MONTH(tanggal_pengajuan) ORDER BY bulan";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, year);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                monthlyStats.add(new int[]{rs.getInt("bulan"), rs.getInt("total")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return monthlyStats;
    }

    /**
     * Mendapatkan ruangan paling sering dipinjam
     */
    public List<String[]> getMostBookedRooms(int limit) {
        List<String[]> rooms = new ArrayList<>();
        String sql = "SELECT TOP (?) r.nama_ruangan, COUNT(*) as total " +
                     "FROM booking_details bd " +
                     "JOIN rooms r ON bd.room_id = r.room_id " +
                     "GROUP BY r.nama_ruangan " +
                     "ORDER BY total DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                rooms.add(new String[]{rs.getString("nama_ruangan"), String.valueOf(rs.getInt("total"))});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    /**
     * Mendapatkan total booking hari ini
     */
    public int getTodayBookingCount() {
        String sql = "SELECT COUNT(*) FROM booking_details WHERE tanggal_pemakaian = CAST(GETDATE() AS DATE)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Mendapatkan total booking bulan ini
     */
    public int getThisMonthBookingCount() {
        String sql = "SELECT COUNT(*) FROM bookings WHERE MONTH(tanggal_pengajuan) = MONTH(GETDATE()) AND YEAR(tanggal_pengajuan) = YEAR(GETDATE())";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}