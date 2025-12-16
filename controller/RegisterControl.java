package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Controller untuk menangani proses registrasi user baru.
 * Menerapkan prinsip Single Responsibility - hanya menangani registrasi.
 */
public class RegisterControl {

    /**
     * Validasi input registrasi
     * @throws IllegalArgumentException jika validasi gagal
     */
    public void validateInput(String nim, String nama, String email, String password) throws IllegalArgumentException {
        if (nim == null || nim.trim().isEmpty()) {
            throw new IllegalArgumentException("NIM/NIP tidak boleh kosong!");
        }
        if (nama == null || nama.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama tidak boleh kosong!");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email tidak boleh kosong!");
        }
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Format email tidak valid!");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password tidak boleh kosong!");
        }
        // if (password.length() < 6) {
        //     throw new IllegalArgumentException("Password minimal 6 karakter!");
        // }
    }

    /**
     * Cek apakah NIM/NIP sudah terdaftar
     */
    public boolean isNimExists(String nim) {
        String sql = "SELECT COUNT(*) FROM civitas_akademik WHERE nim_nip = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nim);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Cek apakah email sudah terdaftar
     */
    public boolean isEmailExists(String email) {
        String sql = "SELECT COUNT(*) FROM civitas_akademik WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Registrasi user baru dengan validasi lengkap
     * @return true jika berhasil
     * @throws IllegalArgumentException jika validasi gagal
     * @throws SQLException jika terjadi error database
     */
    public boolean registerUser(String nim, String nama, String email, String password) 
            throws IllegalArgumentException, SQLException {
        
        // Validasi input
        validateInput(nim, nama, email, password);
        
        // Cek duplikasi
        if (isNimExists(nim)) {
            throw new IllegalArgumentException("NIM/NIP sudah terdaftar!");
        }
        if (isEmailExists(email)) {
            throw new IllegalArgumentException("Email sudah terdaftar!");
        }

        String sql = "INSERT INTO civitas_akademik (nim_nip, nama_lengkap, email, password) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nim.trim());
            stmt.setString(2, nama.trim());
            stmt.setString(3, email.trim().toLowerCase());
            stmt.setString(4, password);

            return stmt.executeUpdate() > 0;
        }
    }
}