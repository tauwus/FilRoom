package Controller;

import Model.CivitasAkademik;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Controller untuk mengelola data user (CivitasAkademik).
 * Menangani operasi CRUD dan update profil user.
 */
public class UserControl {

    /**
     * Mendapatkan data user berdasarkan ID
     */
    public CivitasAkademik getUserById(int userId) {
        String sql = "SELECT user_id, nama_lengkap, nim_nip, email, status_akun FROM civitas_akademik WHERE user_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new CivitasAkademik(
                    rs.getInt("user_id"),
                    rs.getString("nama_lengkap"),
                    rs.getString("nim_nip"),
                    rs.getString("email"),
                    rs.getString("status_akun")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Update profil user (nama, email)
     */
    public boolean updateProfile(int userId, String nama, String email) 
            throws IllegalArgumentException {
        
        // Validasi
        if (nama == null || nama.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama tidak boleh kosong!");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Email tidak valid!");
        }
        
        String sql = "UPDATE civitas_akademik SET nama_lengkap = ?, email = ? WHERE user_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nama.trim());
            stmt.setString(2, email.trim().toLowerCase());
            stmt.setInt(3, userId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Update password user
     */
    public boolean updatePassword(int userId, String oldPassword, String newPassword) 
            throws IllegalArgumentException, SecurityException {
        
        // Validasi
        if (newPassword == null || newPassword.length() < 6) {
            throw new IllegalArgumentException("Password baru minimal 6 karakter!");
        }
        
        // Verifikasi password lama
        if (!verifyPassword(userId, oldPassword)) {
            throw new SecurityException("Password lama tidak sesuai!");
        }
        
        String sql = "UPDATE civitas_akademik SET password = ? WHERE user_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, newPassword);
            stmt.setInt(2, userId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verifikasi password user
     */
    private boolean verifyPassword(int userId, String password) {
        String sql = "SELECT COUNT(*) FROM civitas_akademik WHERE user_id = ? AND password = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}