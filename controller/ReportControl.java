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

    /**
     * Mendapatkan statistik booking harian untuk N hari terakhir
     * Returns List of Object[] {tanggal (String), jumlah (Integer)}
     */
    public List<Object[]> getDailyBookingStats(int days) {
        List<Object[]> dailyStats = new ArrayList<>();
        String sql = "SELECT CAST(tanggal_pengajuan AS DATE) as tanggal, COUNT(*) as total " +
                     "FROM bookings " +
                     "WHERE tanggal_pengajuan >= DATEADD(DAY, -?, GETDATE()) " +
                     "GROUP BY CAST(tanggal_pengajuan AS DATE) " +
                     "ORDER BY tanggal ASC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, days);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String tanggal = rs.getString("tanggal");
                int total = rs.getInt("total");
                dailyStats.add(new Object[]{tanggal, total});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dailyStats;
    }

    /**
     * Mendapatkan statistik booking harian per status untuk N hari terakhir
     */
    public List<Object[]> getDailyBookingStatsByStatus(int days) {
        List<Object[]> dailyStats = new ArrayList<>();
        String sql = "SELECT CAST(tanggal_pengajuan AS DATE) as tanggal, " +
                     "SUM(CASE WHEN status_peminjaman = 'MENUNGGU_PERSETUJUAN' THEN 1 ELSE 0 END) as menunggu, " +
                     "SUM(CASE WHEN status_peminjaman = 'DISETUJUI' THEN 1 ELSE 0 END) as disetujui, " +
                     "SUM(CASE WHEN status_peminjaman = 'DITOLAK' THEN 1 ELSE 0 END) as ditolak, " +
                     "SUM(CASE WHEN status_peminjaman = 'SELESAI' THEN 1 ELSE 0 END) as selesai, " +
                     "COUNT(*) as total " +
                     "FROM bookings " +
                     "WHERE tanggal_pengajuan >= DATEADD(DAY, -?, GETDATE()) " +
                     "GROUP BY CAST(tanggal_pengajuan AS DATE) " +
                     "ORDER BY tanggal ASC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, days);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String tanggal = rs.getString("tanggal");
                int menunggu = rs.getInt("menunggu");
                int disetujui = rs.getInt("disetujui");
                int ditolak = rs.getInt("ditolak");
                int selesai = rs.getInt("selesai");
                int total = rs.getInt("total");
                dailyStats.add(new Object[]{tanggal, menunggu, disetujui, ditolak, selesai, total});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dailyStats;
    }

    /**
     * Mendapatkan total semua booking
     */
    public int getTotalBookingCount() {
        String sql = "SELECT COUNT(*) FROM bookings";
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
     * Mendapatkan total ruangan aktif
     */
    public int getTotalActiveRooms() {
        String sql = "SELECT COUNT(*) FROM rooms";
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
     * Mendapatkan total pengguna
     */
    public int getTotalUsers() {
        String sql = "SELECT COUNT(*) FROM civitas_akademik";
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
     * Mendapatkan statistik booking per status dengan filter
     */
    public Map<BookingStatus, Integer> getBookingCountByStatus(int month, int year) {
        Map<BookingStatus, Integer> stats = new HashMap<>();
        String sql = "SELECT status_peminjaman, COUNT(*) as total FROM bookings WHERE YEAR(tanggal_pengajuan) = ?";
        if (month > 0) sql += " AND MONTH(tanggal_pengajuan) = ?";
        sql += " GROUP BY status_peminjaman";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, year);
            if (month > 0) stmt.setInt(2, month);
            
            ResultSet rs = stmt.executeQuery();
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
     * Mendapatkan ruangan paling sering dipinjam dengan filter
     */
    public List<String[]> getMostBookedRooms(int month, int year, int limit) {
        List<String[]> rooms = new ArrayList<>();
        String sql = "SELECT TOP (?) r.nama_ruangan, COUNT(*) as total " +
                     "FROM booking_details bd " +
                     "JOIN bookings b ON bd.booking_id = b.booking_id " +
                     "JOIN rooms r ON bd.room_id = r.room_id " +
                     "WHERE YEAR(b.tanggal_pengajuan) = ?";
        if (month > 0) sql += " AND MONTH(b.tanggal_pengajuan) = ?";
        sql += " GROUP BY r.nama_ruangan ORDER BY total DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, limit);
            stmt.setInt(2, year);
            if (month > 0) stmt.setInt(3, month);
            
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
     * Mendapatkan statistik booking harian untuk bulan tertentu
     */
    public List<Object[]> getDailyBookingStats(int month, int year) {
        List<Object[]> dailyStats = new ArrayList<>();
        String sql = "SELECT CAST(tanggal_pengajuan AS DATE) as tanggal, COUNT(*) as total " +
                     "FROM bookings " +
                     "WHERE YEAR(tanggal_pengajuan) = ?";
        if (month > 0) sql += " AND MONTH(tanggal_pengajuan) = ?";
        sql += " GROUP BY CAST(tanggal_pengajuan AS DATE) ORDER BY tanggal ASC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, year);
            if (month > 0) stmt.setInt(2, month);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String tanggal = rs.getString("tanggal");
                int total = rs.getInt("total");
                dailyStats.add(new Object[]{tanggal, total});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dailyStats;
    }
}