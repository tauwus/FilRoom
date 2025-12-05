package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterControl {

    public String registerUser(String nim, String nama, String email, String password) {
        if (nim.isEmpty() || nama.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return "Field tidak boleh kosong!";
        }

        String noTelp = "-"; 

        String sql = "INSERT INTO civitas_akademik (nim_nip, nama_lengkap, email, password, no_telepon) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nim);
            stmt.setString(2, nama);
            stmt.setString(3, email);
            stmt.setString(4, password);
            stmt.setString(5, noTelp);

            stmt.executeUpdate();
            return "SUCCESS";

        } catch (SQLException e) {
            e.printStackTrace();
            return "Database Error: " + e.getMessage();
        }
    }
}