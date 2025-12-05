package Controller;

import Model.Admin;
import Model.CivitasAkademik;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginControl {

    public boolean login(String identifier, String password) {
        if (checkAdminLogin(identifier, password)) {
            return true;
        }
        if (checkCivitasLogin(identifier, password)) {
            return true;
        }
        return false;
    }

    private boolean checkAdminLogin(String username, String password) {
        String sql = "SELECT admin_id, nama_lengkap, username FROM admins WHERE username = ? AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.setString(2, password);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Admin admin = new Admin(rs.getInt("admin_id"), rs.getString("nama_lengkap"), rs.getString("username"));
                AuthControl.createSession(admin);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean checkCivitasLogin(String emailOrNim, String password) {
        String sql = "SELECT user_id, nama_lengkap, nim_nip, email, status_akun, no_telepon FROM civitas_akademik WHERE (email = ? OR nim_nip = ?) AND password = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, emailOrNim);
            stmt.setString(2, emailOrNim);
            stmt.setString(3, password);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String status = rs.getString("status_akun");
                if ("Nonaktif".equalsIgnoreCase(status)) return false;

                CivitasAkademik user = new CivitasAkademik(
                    rs.getInt("user_id"),
                    rs.getString("nama_lengkap"),
                    rs.getString("nim_nip"),
                    rs.getString("email"),
                    rs.getString("no_telepon"),
                    rs.getString("status_akun")
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