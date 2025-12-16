package View;

import Controller.AuthControl;
import Controller.BookingControl;
import Controller.UserControl;
import Model.CivitasAkademik;
import Model.User;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

public class ProfilePage extends JPanel {
    private MainFrame mainFrame;
    private BookingControl bookingControl;
    private UserControl userControl;
    
    // UI Components to update
    private JLabel nameLabel;
    private JLabel nimLabel;
    private JLabel avatarLabel;
    
    private JTextField fullNameField;
    private JTextField emailField;
    private JTextField nimField;
    
    private JPanel statsGrid;

    public ProfilePage(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.bookingControl = new BookingControl();
        this.userControl = new UserControl();
        
        setLayout(new BorderLayout());
        setBackground(new Color(225, 255, 255)); // Light cyan background

        // --- CONTENT WRAPPER (Scrollable) ---
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(225, 255, 255));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // 1. HEADER SECTION
        JLabel titleLabel = new JLabel("Profil Anda");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(titleLabel);

        JLabel subtitleLabel = new JLabel("Detail informasi akun anda");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(subtitleLabel);

        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Profile Card
        JPanel profileCard = new JPanel();
        profileCard.setLayout(new BoxLayout(profileCard, BoxLayout.Y_AXIS));
        profileCard.setBackground(Color.WHITE);
        profileCard.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(20, 20, 20, 20)
        ));
        profileCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        profileCard.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Avatar
        avatarLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Background
                g2.setColor(new Color(130, 160, 230)); // Periwinkle blue
                g2.fill(new Ellipse2D.Double(0, 0, getWidth(), getHeight()));
                
                // Initials
                User u = AuthControl.getCurrentUser();
                String name = (u != null) ? u.getName() : "Guest";
                String initials = getInitials(name);
                
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 28));
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(initials)) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(initials, x, y);
                
                g2.dispose();
            }
            
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(80, 80);
            }
            
            @Override
            public Dimension getMaximumSize() {
                return new Dimension(80, 80);
            }
        };
        avatarLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        profileCard.add(avatarLabel);
        
        profileCard.add(Box.createRigidArea(new Dimension(0, 10)));

        // Name & Info
        nameLabel = new JLabel("Guest");
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        profileCard.add(nameLabel);

        nimLabel = new JLabel("-");
        nimLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        nimLabel.setForeground(Color.GRAY);
        nimLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        profileCard.add(nimLabel);

        contentPanel.add(profileCard);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // 2. INFORMASI PENGGUNA SECTION
        JPanel infoPanel = createSectionPanel("Informasi Pengguna");
        
        fullNameField = new JTextField();
        infoPanel.add(createReadOnlyField("Nama Lengkap", fullNameField));
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        emailField = new JTextField();
        infoPanel.add(createReadOnlyField("Email", emailField));
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        nimField = new JTextField();
        infoPanel.add(createReadOnlyField("NIM", nimField));
        infoPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Edit Profile Button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton editProfileBtn = new JButton("✏️ Edit Profil");
        editProfileBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        editProfileBtn.setForeground(Color.WHITE);
        editProfileBtn.setBackground(new Color(37, 99, 235));
        editProfileBtn.setFocusPainted(false);
        editProfileBtn.setBorderPainted(false);
        editProfileBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        editProfileBtn.setPreferredSize(new Dimension(130, 35));
        editProfileBtn.addActionListener(e -> showEditProfileDialog());
        
        JButton changePassBtn = new JButton("🔒 Ganti Password");
        changePassBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        changePassBtn.setForeground(Color.WHITE);
        changePassBtn.setBackground(new Color(249, 115, 22));
        changePassBtn.setFocusPainted(false);
        changePassBtn.setBorderPainted(false);
        changePassBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        changePassBtn.setPreferredSize(new Dimension(160, 35));
        changePassBtn.addActionListener(e -> showChangePasswordDialog());
        
        buttonPanel.add(editProfileBtn);
        buttonPanel.add(changePassBtn);
        infoPanel.add(buttonPanel);

        contentPanel.add(infoPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // 3. STATISTIK PEMINJAMAN SECTION
        JPanel statsPanel = createSectionPanel("Statistik Peminjaman");
        
        statsGrid = new JPanel(new GridLayout(2, 2, 10, 10));
        statsGrid.setBackground(Color.WHITE);
        
        // Initial empty stats
        updateStats(0);

        statsPanel.add(statsGrid);
        contentPanel.add(statsPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // 4. PENGATURAN SECTION
        JPanel settingsPanel = createSectionPanel("Pengaturan");
        
        settingsPanel.add(createToggleItem("Notifikasi", "Terima notifikasi peminjaman", true));
        settingsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        settingsPanel.add(createToggleItem("Mode Gelap", "Ubah tema aplikasi", false));
        settingsPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Logout Button
        JButton logoutBtn = new JButton("Keluar dari Akun");
        logoutBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setBackground(new Color(255, 80, 80)); // Red
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorder(new EmptyBorder(10, 0, 10, 0));
        logoutBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        logoutBtn.addActionListener(e -> showLogoutConfirmation());
        
        settingsPanel.add(logoutBtn);

        contentPanel.add(settingsPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Scroll Pane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom Navigation
        add(new BottomNavPanel(mainFrame, "Profil"), BorderLayout.SOUTH);
        
        // Add AncestorListener to refresh data when view is shown
        addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                refreshProfile();
            }

            @Override
            public void ancestorRemoved(AncestorEvent event) {
            }

            @Override
            public void ancestorMoved(AncestorEvent event) {
            }
        });
        
        // Initial refresh
        refreshProfile();
    }
    
    private void refreshProfile() {
        User currentUser = AuthControl.getCurrentUser();
        
        if (currentUser != null) {
            nameLabel.setText(currentUser.getName());
            fullNameField.setText(currentUser.getName());
            
            if (currentUser instanceof CivitasAkademik) {
                CivitasAkademik civitas = (CivitasAkademik) currentUser;
                String label = "NIM";
                if ("Dosen".equalsIgnoreCase(civitas.getPeran())) {
                    label = "NIP";
                }
                nimLabel.setText(label + ": " + civitas.getNim());
                emailField.setText(civitas.getEmail());
                nimField.setText(civitas.getNim());
                
                // Update label text for the read-only field as well if possible, 
                // but the label is created inside createReadOnlyField and not stored.
                // We might need to recreate the panel or just accept it says "NIM" for now 
                // or try to find the component. 
                // For simplicity, let's just update the nimLabel under the name.
            } else {
                nimLabel.setText("-");
                emailField.setText("-");
                nimField.setText("-");
            }
            
            updateStats(currentUser.getId());
        } else {
            nameLabel.setText("Guest");
            nimLabel.setText("-");
            fullNameField.setText("-");
            emailField.setText("-");
            nimField.setText("-");
            updateStats(0);
        }
        
        avatarLabel.repaint();
    }
    
    private void updateStats(int userId) {
        statsGrid.removeAll();
        int[] stats = bookingControl.getBookingStatistics(userId);
        // stats: [Total, Approved, Pending, Completed]

        statsGrid.add(createStatCard(String.valueOf(stats[0]), "Total Peminjaman", new Color(180, 180, 180)));
        statsGrid.add(createStatCard(String.valueOf(stats[1]), "Dikonfirmasi", new Color(144, 238, 144)));
        statsGrid.add(createStatCard(String.valueOf(stats[2]), "Menunggu", new Color(240, 230, 140))); 
        statsGrid.add(createStatCard(String.valueOf(stats[3]), "Selesai", new Color(200, 220, 255)));
        
        statsGrid.revalidate();
        statsGrid.repaint();
    }
    
    private String getInitials(String name) {
        if (name == null || name.isEmpty()) return "G";
        String[] parts = name.trim().split("\\s+");
        if (parts.length == 1) {
            if (parts[0].length() >= 2) {
                return parts[0].substring(0, 2).toUpperCase();
            } else {
                return parts[0].toUpperCase();
            }
        } else {
            return (parts[0].substring(0, 1) + parts[1].substring(0, 1)).toUpperCase();
        }
    }

    private JPanel createSectionPanel(String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(15, 15, 15, 15)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 500)); // Flexible height
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel label = new JLabel("<html><b>" + title + "</b></html>"); 
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        return panel;
    }

    private JPanel createReadOnlyField(String labelText, JTextField field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.PLAIN, 12));
        label.setForeground(Color.DARK_GRAY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        field.setEditable(false);
        field.setBackground(new Color(240, 240, 240)); // Light gray bg
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1, true),
            new EmptyBorder(8, 10, 8, 10)
        ));
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(field);
        
        return panel;
    }

    private JPanel createStatCard(String count, String label, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(color);
        card.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel countLabel = new JLabel(count);
        countLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        countLabel.setForeground(new Color(50, 50, 50));
        countLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel textLabel = new JLabel(label);
        textLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
        textLabel.setForeground(new Color(80, 80, 80));
        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalGlue());
        card.add(countLabel);
        card.add(textLabel);
        card.add(Box.createVerticalGlue());

        return card;
    }

    private JPanel createToggleItem(String title, String subtitle, boolean isOn) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);

        JLabel t = new JLabel(title);
        t.setFont(new Font("SansSerif", Font.BOLD, 12));
        
        JLabel s = new JLabel(subtitle);
        s.setFont(new Font("SansSerif", Font.PLAIN, 10));
        s.setForeground(Color.GRAY);

        textPanel.add(t);
        textPanel.add(s);

        JLabel toggle = new JLabel(isOn ? "ON" : "OFF"); 
        toggle.setFont(new Font("SansSerif", Font.BOLD, 12));
        toggle.setForeground(isOn ? new Color(0, 150, 0) : Color.GRAY);

        panel.add(textPanel, BorderLayout.CENTER);
        panel.add(toggle, BorderLayout.EAST);

        return panel;
    }

    private void showEditProfileDialog() {
        User currentUser = AuthControl.getCurrentUser();
        if (!(currentUser instanceof CivitasAkademik)) {
            JOptionPane.showMessageDialog(this, "Hanya Civitas Akademik yang dapat edit profil", 
                "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        CivitasAkademik civitas = (CivitasAkademik) currentUser;
        
        JTextField editNameField = new JTextField(civitas.getName(), 25);
        JTextField editEmailField = new JTextField(civitas.getEmail(), 25);
        
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        // panel.add(new JLabel("Nama Lengkap:"));
        // panel.add(editNameField);
        panel.add(new JLabel("Email:"));
        panel.add(editEmailField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, 
            "Edit Profil", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String newName = editNameField.getText().trim();
            String newEmail = editEmailField.getText().trim();
            
            try {
                userControl.updateProfile(civitas.getId(), newName, newEmail);
                // Update local object
                civitas.setName(newName);
                civitas.setEmail(newEmail);
                refreshProfile();
                JOptionPane.showMessageDialog(this, "Profil berhasil diupdate!", 
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal update profil: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void showChangePasswordDialog() {
        User currentUser = AuthControl.getCurrentUser();
        if (currentUser == null) return;
        
        JPasswordField currentPassField = new JPasswordField(20);
        JPasswordField newPassField = new JPasswordField(20);
        JPasswordField confirmPassField = new JPasswordField(20);
        
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.add(new JLabel("Password Saat Ini:"));
        panel.add(currentPassField);
        panel.add(new JLabel("Password Baru:"));
        panel.add(newPassField);
        panel.add(new JLabel("Konfirmasi Password:"));
        panel.add(confirmPassField);
        
        int result = JOptionPane.showConfirmDialog(this, panel, 
            "Ganti Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String currentPass = new String(currentPassField.getPassword());
            String newPass = new String(newPassField.getPassword());
            String confirmPass = new String(confirmPassField.getPassword());
            
            if (!newPass.equals(confirmPass)) {
                JOptionPane.showMessageDialog(this, "Password baru tidak sama dengan konfirmasi", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (newPass.length() < 6) {
                JOptionPane.showMessageDialog(this, "Password minimal 6 karakter", 
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                userControl.updatePassword(currentUser.getId(), currentPass, newPass);
                JOptionPane.showMessageDialog(this, "Password berhasil diubah!", 
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal ubah password: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showLogoutConfirmation() {
        JDialog dialog = new JDialog(mainFrame, "Konfirmasi Logout", true);
        dialog.setSize(300, 180);
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setLayout(new BorderLayout());
        
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);
        content.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel label = new JLabel("<html><center>Apakah Anda yakin<br>ingin Keluar?</center></html>");
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        content.add(label);
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnPanel.setBackground(Color.WHITE);
        
        JButton yesBtn = new JButton("Ya");
        yesBtn.setForeground(Color.RED);
        yesBtn.setBackground(Color.WHITE);
        yesBtn.setBorder(new LineBorder(Color.RED, 1));
        yesBtn.setPreferredSize(new Dimension(80, 30));
        yesBtn.setFocusPainted(false);
        yesBtn.addActionListener(e -> {
            AuthControl.destroySession();
            dialog.dispose();
            mainFrame.showView("Login");
        });
        
        JButton noBtn = new JButton("Tidak");
        noBtn.setForeground(Color.BLACK);
        noBtn.setBackground(Color.WHITE);
        noBtn.setBorder(new LineBorder(Color.GRAY, 1));
        noBtn.setPreferredSize(new Dimension(80, 30));
        noBtn.setFocusPainted(false);
        noBtn.addActionListener(e -> dialog.dispose());
        
        btnPanel.add(yesBtn);
        btnPanel.add(noBtn);
        
        dialog.add(content, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
}