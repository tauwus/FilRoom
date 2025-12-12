package View;

import Controller.AuthControl;
import Controller.LoginControl;
import Model.User;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class LoginPage extends JPanel {
    private MainFrame mainFrame;
    private JTextField emailField;
    private JPasswordField passwordField;
    private LoginControl loginControl; // Controller

    public LoginPage(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.loginControl = new LoginControl(); // Init

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(new EmptyBorder(50, 40, 50, 40));

        // Title
        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(titleLabel);

        centerPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        // Email Field
        emailField = new JTextField();
        styleTextField(emailField, "Email atau NIM atau NIP");
        centerPanel.add(emailField);

        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Password Field
        passwordField = new JPasswordField();
        styleTextField(passwordField, "Kata Sandi");
        centerPanel.add(passwordField);

        centerPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        // Forgot Password
        JLabel forgotPassLabel = new JLabel("Lupa Password?");
        forgotPassLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        forgotPassLabel.setForeground(new Color(50, 50, 150));
        forgotPassLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        JPanel forgotPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        forgotPanel.setBackground(Color.WHITE);
        forgotPanel.setMaximumSize(new Dimension(400, 30));
        forgotPanel.add(forgotPassLabel);
        centerPanel.add(forgotPanel);

        centerPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Login Button
        JButton loginButton = new JButton("Masuk");
        styleButton(loginButton);
        
        // --- LOGIKA LOGIN ---
        loginButton.addActionListener(e -> {
            String id = getRealText(emailField, "Email atau NIM atau NIP");
            String pass = new String(passwordField.getPassword());
            if(pass.equals("Kata Sandi")) pass = "";

            try {
                // Validasi dan login dilakukan di Controller
                if (loginControl.login(id, pass)) {
                    User user = AuthControl.getCurrentUser();
                    JOptionPane.showMessageDialog(this, "Selamat datang, " + user.getName());
                    
                    // Arahkan user tergantung Role
                    if (user.getRole().equals("ADMIN")) {
                        mainFrame.showView("Dashboard"); // AdminDashboard
                    } else {
                        mainFrame.showView("Home"); // User Home Page
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Login Gagal. Cek username/password.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IllegalArgumentException ex) {
                // Error validasi dari Controller
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Validasi Error", JOptionPane.WARNING_MESSAGE);
            } catch (SecurityException ex) {
                // Akun dinonaktifkan/dibekukan
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Akses Ditolak", JOptionPane.ERROR_MESSAGE);
            }
        });
        centerPanel.add(loginButton);

        centerPanel.add(Box.createVerticalGlue());

        // Register Link
        JLabel registerLabel = new JLabel("Tidak punya akun? Daftar sekarang");
        registerLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        registerLabel.setForeground(new Color(50, 50, 150));
        registerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainFrame.showView("Register");
            }
        });
        centerPanel.add(registerLabel);

        add(centerPanel, BorderLayout.CENTER);
    }
    
    // Helper untuk mengambil text asli
    private String getRealText(JTextField field, String placeholder) {
        String text = field.getText();
        if (text.equals(placeholder)) return "";
        return text;
    }

    private void styleTextField(JTextField field, String placeholder) {
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setPreferredSize(new Dimension(300, 40));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(100, 100, 200), 1, true),
                new EmptyBorder(5, 10, 5, 10)
        ));
        
        // Placeholder logic
        field.setText(placeholder);
        field.setForeground(Color.GRAY);
        
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
                // Jika password, set echo char
                if (field instanceof JPasswordField) {
                     ((JPasswordField) field).setEchoChar('•');
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                    // Jika password, matikan echo char saat menampilkan placeholder "Kata Sandi"
                    if (field instanceof JPasswordField) {
                         ((JPasswordField) field).setEchoChar((char) 0);
                    }
                }
            }
        });
        // Inisialisasi awal untuk password (biar text "Kata Sandi" terbaca)
        if (field instanceof JPasswordField) {
             ((JPasswordField) field).setEchoChar((char) 0);
        }
    }

    private void styleButton(JButton button) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        button.setPreferredSize(new Dimension(300, 45));
        button.setBackground(new Color(30, 30, 140)); // Dark Blue
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
    }
}