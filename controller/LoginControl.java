package Controller;

import Model.Admin;
import Model.CivitasAkademik;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginControl {

    public boolean login(String identifier, String password) {
        // 1. Cek Admin
        if (checkAdminLogin(identifier, password)) {
            return true;
        }
        // 2. Cek Civitas Akademik
        if (checkCivitasLogin(identifier, password)) {
            return true;
        }
        return false;
    }

    private boolean checkAdminLogin(String username, String password) {
        String sql = "SELECT admin_id, nama_lengkap FROM admins WHERE username = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Admin admin = new Admin(rs.getInt("admin_id"), rs.getString("nama_lengkap"));
                AuthControl.createSession(admin);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean checkCivitasLogin(String emailOrNim, String password) {
        // Bisa login pakai Email atau NIM
        String sql = "SELECT user_id, nama_lengkap, nim_nip, email, role, status_akun FROM civitas_akademik WHERE (email = ? OR nim_nip = ?) AND password = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, emailOrNim);
            stmt.setString(2, emailOrNim);
            stmt.setString(3, password);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String status = rs.getString("status_akun");
                if ("nonaktif".equalsIgnoreCase(status)) return false;

                CivitasAkademik user = new CivitasAkademik(
                    rs.getInt("user_id"),
                    rs.getString("nama_lengkap"),
                    rs.getString("nim_nip"),
                    rs.getString("email"),
                    rs.getString("role")
                );
                AuthControl.createSession(user);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}