package Controller;

import Model.Room;
import java.sql.*;

public class AdminRoomControl {
    
    // Menggunakan RoomControl untuk getAllRooms (menghindari duplikasi)
    private RoomControl roomControl = new RoomControl();
    
    public java.util.List<Room> getAllRooms() {
        return roomControl.getAllRooms();
    }

    public boolean addRoom(String name, String location, int capacity) {
        String sql = "INSERT INTO rooms (nama_ruangan, lokasi, kapasitas) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, location);
            stmt.setInt(3, capacity);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateRoom(Room room) {
        String sql = "UPDATE rooms SET nama_ruangan = ?, lokasi = ?, kapasitas = ? WHERE room_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, room.getName());
            stmt.setString(2, room.getLocation());
            stmt.setInt(3, room.getCapacity());
            stmt.setInt(4, room.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteRoom(int roomId) {
        String sql = "DELETE FROM rooms WHERE room_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roomId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}