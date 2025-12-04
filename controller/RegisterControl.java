package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterControl {

    public boolean registerUser(String nim, String nama, String email, String password, String role) {
        // Validasi input kosong
        if (nim.isEmpty() || nama.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return false;
        }

        // Default no_telepon kosong dulu karena tidak ada fieldnya di UI saat ini
        String noTelp = "-"; 

        String sql = "INSERT INTO civitas_akademik (nim_nip, nama_lengkap, email, password, no_telepon, role) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nim);
            stmt.setString(2, nama);
            stmt.setString(3, email);
            stmt.setString(4, password); // Ingat: Tanpa Hash sesuai permintaan
            stmt.setString(5, noTelp);
            stmt.setString(6, role); // Harus 'mahasiswa', 'dosen', atau 'staf'

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace(); // Cek console jika error
            return false;
        }
    }
}