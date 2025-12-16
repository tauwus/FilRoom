package Controller;

import Model.AccountStatus;
import Model.CivitasAkademik;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller untuk Admin mengelola data user.
 * Menangani operasi CRUD user oleh admin.
 */
public class AdminUserControl {

    /**
     * Mendapatkan semua user (civitas akademik)
     */
    public List<CivitasAkademik> getAllUsers() {
        List<CivitasAkademik> users = new ArrayList<>();
        String sql = "SELECT user_id, nama_lengkap, nim_nip, email, status_akun, peran FROM civitas_akademik ORDER BY nama_lengkap";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                users.add(new CivitasAkademik(
                    rs.getInt("user_id"),
                    rs.getString("nama_lengkap"),
                    rs.getString("nim_nip"),
                    rs.getString("email"),
                    rs.getString("status_akun"),
                    rs.getString("peran")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Mendapatkan user berdasarkan status akun
     */
    public List<CivitasAkademik> getUsersByStatus(AccountStatus status) {
        List<CivitasAkademik> users = new ArrayList<>();
        String sql = "SELECT user_id, nama_lengkap, nim_nip, email, status_akun, peran FROM civitas_akademik WHERE status_akun = ? ORDER BY nama_lengkap";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status.toString());
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                users.add(new CivitasAkademik(
                    rs.getInt("user_id"),
                    rs.getString("nama_lengkap"),
                    rs.getString("nim_nip"),
                    rs.getString("email"),
                    rs.getString("status_akun"),
                    rs.getString("peran")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Update status akun user oleh admin
     */
    public boolean updateUserStatus(int userId, AccountStatus newStatus) {
        String sql = "UPDATE civitas_akademik SET status_akun = ? WHERE user_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, newStatus.toString());
            stmt.setInt(2, userId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Aktivasi akun user
     */
    public boolean activateUser(int userId) {
        return updateUserStatus(userId, AccountStatus.AKTIF);
    }

    /**
     * Nonaktifkan akun user
     */
    public boolean deactivateUser(int userId) {
        return updateUserStatus(userId, AccountStatus.NONAKTIF);
    }

    /**
     * Bekukan akun user
     */
    public boolean freezeUser(int userId) {
        return updateUserStatus(userId, AccountStatus.DIBEKUKAN);
    }

    /**
     * Hapus user dari database
     */
    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM civitas_akademik WHERE user_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Hitung total user
     */
    public int countTotalUsers() {
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
     * Hitung user aktif
     */
    public int countActiveUsers() {
        String sql = "SELECT COUNT(*) FROM civitas_akademik WHERE status_akun = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, AccountStatus.AKTIF.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}