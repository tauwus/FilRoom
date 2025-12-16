package View;

import Controller.RegisterControl;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class RegisterPage extends JPanel {
    private MainFrame mainFrame;
    private JTextField nameField;
    private JTextField idField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private RegisterControl registerControl;

    public RegisterPage(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.registerControl = new RegisterControl();

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel titleLabel = new JLabel("Register");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(titleLabel);

        centerPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        nameField = new JTextField();
        styleTextField(nameField, "Nama");
        centerPanel.add(nameField);

        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Role Selection
        String[] roles = {"Mahasiswa", "Dosen"};
        JComboBox<String> roleCombo = new JComboBox<>(roles);
        roleCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        roleCombo.setBackground(Color.WHITE);
        roleCombo.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(5, 5, 5, 5)
        ));
        centerPanel.add(roleCombo);

        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        idField = new JTextField();
        styleTextField(idField, "NIM/NIP");
        centerPanel.add(idField);

        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        emailField = new JTextField();
        styleTextField(emailField, "Email");
        centerPanel.add(emailField);

        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        passwordField = new JPasswordField();
        styleTextField(passwordField, "Kata Sandi");
        centerPanel.add(passwordField);

        centerPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        JButton registerButton = new JButton("Daftar");
        styleButton(registerButton);
        
        registerButton.addActionListener(e -> {
            String nama = getRealText(nameField, "Nama");
            String nim = getRealText(idField, "NIM/NIP");
            String email = getRealText(emailField, "Email");
            String pass = new String(passwordField.getPassword());
            String peran = (String) roleCombo.getSelectedItem();

            if(pass.equals("Kata Sandi")) pass = "";

            try {
                // Validasi dan registrasi dilakukan di Controller
                boolean success = registerControl.registerUser(nim, nama, email, pass, peran);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Registrasi Berhasil! Silakan Login.");
                    mainFrame.showView("Login");
                } else {
                    JOptionPane.showMessageDialog(this, "Registrasi gagal. Coba lagi.", "Gagal", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IllegalArgumentException ex) {
                // Error validasi dari Controller
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Validasi Error", JOptionPane.WARNING_MESSAGE);
            } catch (java.sql.SQLException ex) {
                // Error database
                JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        centerPanel.add(registerButton);

        centerPanel.add(Box.createVerticalGlue());

        JLabel loginLabel = new JLabel("Sudah punya akun? Masuk");
        loginLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        loginLabel.setForeground(new Color(50, 50, 150));
        loginLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                mainFrame.showView("Login");
            }
        });
        centerPanel.add(loginLabel);

        add(centerPanel, BorderLayout.CENTER);
    }

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

        field.setText(placeholder);
        field.setForeground(Color.GRAY);

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
            }
        });
    }

    private void styleButton(JButton button) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        button.setPreferredSize(new Dimension(300, 45));
        button.setBackground(new Color(30, 30, 140));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
    }
}