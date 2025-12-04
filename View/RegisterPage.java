package View;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegisterPage extends JPanel {
    private MainFrame mainFrame;
    private JTextField nameField;
    private JTextField idField; // NIM/NIP
    private JTextField emailField;
    private JPasswordField passwordField;

    public RegisterPage(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Title
        JLabel titleLabel = new JLabel("Register");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(titleLabel);

        centerPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Name Field
        nameField = new JTextField();
        styleTextField(nameField, "Nama");
        centerPanel.add(nameField);

        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // ID Field
        idField = new JTextField();
        styleTextField(idField, "NIM/NIP");
        centerPanel.add(idField);

        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Email Field
        emailField = new JTextField();
        styleTextField(emailField, "Email");
        centerPanel.add(emailField);

        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Password Field
        passwordField = new JPasswordField();
        styleTextField(passwordField, "Kata Sandi");
        centerPanel.add(passwordField);

        centerPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Register Button
        JButton registerButton = new JButton("Daftar");
        styleButton(registerButton);
        registerButton.addActionListener(e -> {
            // Perform register logic here
            JOptionPane.showMessageDialog(this, "Register Clicked!");
        });
        centerPanel.add(registerButton);

        centerPanel.add(Box.createVerticalGlue());

        // Login Link (Back to Login)
        JLabel loginLabel = new JLabel("Sudah punya akun? Masuk");
        loginLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        loginLabel.setForeground(new Color(50, 50, 150));
        loginLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainFrame.showView("Login");
            }
        });
        centerPanel.add(loginLabel);

        add(centerPanel, BorderLayout.CENTER);
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
        button.setBackground(new Color(30, 30, 140)); // Dark Blue
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
    }
}