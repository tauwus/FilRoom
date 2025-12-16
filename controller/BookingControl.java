package Controller;

import Model.BookingStatus;
import Model.Room;
import Model.User;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingControl {

    /**
     * Automatically update booking status to 'Selesai' if the start time has passed.
     * This should be called before retrieving booking data.
     */
    public void autoUpdateBookingStatus() {
        String sql = "UPDATE b " +
                     "SET b.status_peminjaman = ? " +
                     "FROM bookings b " +
                     "JOIN booking_details bd ON b.booking_id = bd.booking_id " +
                     "WHERE b.status_peminjaman = ? " +
                     "AND (" +
                     "    bd.tanggal_pemakaian < CAST(GETDATE() AS DATE) " +
                     "    OR (" +
                     "        bd.tanggal_pemakaian = CAST(GETDATE() AS DATE) " +
                     "        AND bd.waktu_mulai <= CAST(GETDATE() AS TIME)" +
                     "    )" +
                     ")";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, BookingStatus.SELESAI.toString());
            stmt.setString(2, BookingStatus.DISETUJUI.toString());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String[]> getRecentBookings(int userId) {
        autoUpdateBookingStatus(); // Update status first
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

    public boolean createBooking(User user, Room room, LocalDate date, String startTime, String endTime, 
                                 String purpose, int participants, String description) throws IllegalArgumentException {
        
        // Validation Logic
        if (startTime.compareTo(endTime) >= 0) {
            throw new IllegalArgumentException("Jam selesai harus lebih besar dari jam mulai.");
        }
        if (participants <= 0) {
            throw new IllegalArgumentException("Jumlah peserta harus lebih dari 0.");
        }

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
            stmtBooking.setInt(1, user.getId());
            stmtBooking.setString(2, fullDescription);
            stmtBooking.setString(3, BookingStatus.MENUNGGU_PERSETUJUAN.toString());
            
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
            stmtDetail.setInt(2, room.getId());
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
        autoUpdateBookingStatus(); // Update status first
        int count = 0;
        // Kita anggap "Aktif" adalah yang statusnya Menunggu atau Disetujui
        String sql = "SELECT COUNT(*) AS total FROM bookings " +
                     "WHERE user_id = ? AND status_peminjaman IN (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setString(2, BookingStatus.MENUNGGU_PERSETUJUAN.toString());
            stmt.setString(3, BookingStatus.DISETUJUI.toString());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                count = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public int[] getBookingStatistics(int userId) {
        autoUpdateBookingStatus(); // Update status first
        // Returns array: [Total, Approved, Pending, Completed]
        int[] stats = new int[4];
        String sql = "SELECT " +
                     "COUNT(*) as total, " +
                     "SUM(CASE WHEN status_peminjaman = ? THEN 1 ELSE 0 END) as approved, " +
                     "SUM(CASE WHEN status_peminjaman = ? THEN 1 ELSE 0 END) as pending, " +
                     "SUM(CASE WHEN status_peminjaman = ? THEN 1 ELSE 0 END) as completed " +
                     "FROM bookings WHERE user_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, BookingStatus.DISETUJUI.toString());
            stmt.setString(2, BookingStatus.MENUNGGU_PERSETUJUAN.toString());
            stmt.setString(3, BookingStatus.SELESAI.toString());
            stmt.setInt(4, userId);
            
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                stats[0] = rs.getInt("total");
                stats[1] = rs.getInt("approved");
                stats[2] = rs.getInt("pending");
                stats[3] = rs.getInt("completed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stats;
    }

    public List<String[]> getOccupiedSlots(int roomId, LocalDate date) {
        List<String[]> slots = new ArrayList<>();
        String sql = "SELECT waktu_mulai, waktu_selesai FROM booking_details bd " +
                     "JOIN bookings b ON bd.booking_id = b.booking_id " +
                     "WHERE bd.room_id = ? AND bd.tanggal_pemakaian = ? " +
                     "AND b.status_peminjaman IN (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, roomId);
            stmt.setDate(2, java.sql.Date.valueOf(date));
            stmt.setString(3, BookingStatus.DISETUJUI.toString());
            stmt.setString(4, BookingStatus.MENUNGGU_PERSETUJUAN.toString());
            
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String start = rs.getTime("waktu_mulai").toString().substring(0, 5);
                String end = rs.getTime("waktu_selesai").toString().substring(0, 5);
                slots.add(new String[]{start, end});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return slots;
    }

    public List<String> getAvailableStartTimes(int roomId, LocalDate date) {
        List<String[]> occupiedSlots = getOccupiedSlots(roomId, date);
        List<String> availableStartTimes = new ArrayList<>();
        
        LocalDate today = LocalDate.now();
        int currentHour = java.time.LocalTime.now().getHour();
        
        for (int i = 7; i <= 20; i++) {
            String time = String.format("%02d:00", i);
            
            // H-1 logic: if booking is for today, only show times at least 1 hour ahead
            if (date.equals(today) && i <= currentHour) {
                continue; // Skip past hours and current hour (H-1)
            }
            
            if (!isTimeOccupied(time, occupiedSlots)) {
                availableStartTimes.add(time);
            }
        }
        return availableStartTimes;
    }

    public List<String> getAvailableEndTimes(String startTime, int roomId, LocalDate date) {
        List<String[]> occupiedSlots = getOccupiedSlots(roomId, date);
        List<String> availableEndTimes = new ArrayList<>();
        
        if (startTime == null || startTime.equals("Penuh")) {
            return availableEndTimes;
        }

        int startHour = Integer.parseInt(startTime.substring(0, 2));
        int limitHour = 21; // Max operational hour
        
        LocalDate today = LocalDate.now();
        int currentHour = java.time.LocalTime.now().getHour();

        for (String[] slot : occupiedSlots) {
            int occupiedStart = Integer.parseInt(slot[0].substring(0, 2));
            if (occupiedStart > startHour && occupiedStart < limitHour) {
                limitHour = occupiedStart;
            }
        }

        for (int i = startHour + 1; i <= limitHour; i++) {
            // H-1 logic for end time: if today, end time must also be at least 1 hour ahead
            if (date.equals(today) && i <= currentHour) {
                continue;
            }
            availableEndTimes.add(String.format("%02d:00", i));
        }
        
        return availableEndTimes;
    }

    private boolean isTimeOccupied(String time, List<String[]> occupiedSlots) {
        int hour = Integer.parseInt(time.substring(0, 2));
        for (String[] slot : occupiedSlots) {
            int start = Integer.parseInt(slot[0].substring(0, 2));
            int end = Integer.parseInt(slot[1].substring(0, 2));
            if (hour >= start && hour < end) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Get user booking history with filter support.
     * Returns array: [roomName, bookingDate, startTime, endTime, purpose, status]
     */
    public List<String[]> getUserBookingsFiltered(int userId, String filter) {
        autoUpdateBookingStatus(); // Update status first
        List<String[]> bookings = new ArrayList<>();
        
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT b.booking_id, b.status_peminjaman, b.keterangan_kegiatan, ");
        sql.append("bd.tanggal_pemakaian, bd.waktu_mulai, bd.waktu_selesai, r.nama_ruangan ");
        sql.append("FROM bookings b ");
        sql.append("JOIN booking_details bd ON b.booking_id = bd.booking_id ");
        sql.append("JOIN rooms r ON bd.room_id = r.room_id ");
        sql.append("WHERE b.user_id = ? ");
        
        // Apply filter
        if (filter != null && !filter.equals("Semua")) {
            sql.append("AND b.status_peminjaman = ? ");
        }
        
        sql.append("ORDER BY bd.tanggal_pemakaian DESC, bd.waktu_mulai DESC");
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            stmt.setInt(1, userId);
            
            if (filter != null && !filter.equals("Semua")) {
                String statusValue = convertFilterToStatus(filter);
                stmt.setString(2, statusValue);
            }
            
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String id = String.valueOf(rs.getInt("booking_id"));
                String status = rs.getString("status_peminjaman");
                String purpose = rs.getString("keterangan_kegiatan");
                String dateStr = rs.getDate("tanggal_pemakaian").toString();
                String start = rs.getTime("waktu_mulai").toString();
                String end = rs.getTime("waktu_selesai").toString();
                String room = rs.getString("nama_ruangan");
                
                if (start.length() > 5) start = start.substring(0, 5);
                if (end.length() > 5) end = end.substring(0, 5);

                bookings.add(new String[]{room, dateStr, start, end, purpose, status, id});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }
    
    private String convertFilterToStatus(String filter) {
        switch (filter) {
            case "Menunggu":
                return BookingStatus.MENUNGGU_PERSETUJUAN.toString();
            case "Disetujui":
                return BookingStatus.DISETUJUI.toString();
            case "Ditolak":
                return BookingStatus.DITOLAK.toString();
            case "Selesai":
                return BookingStatus.SELESAI.toString();
            default:
                return filter;
        }
    }

    public boolean cancelBooking(int bookingId) {
        String sql = "UPDATE bookings SET status_peminjaman = ? WHERE booking_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, BookingStatus.DIBATALKAN.toString());
            stmt.setInt(2, bookingId);
            
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}