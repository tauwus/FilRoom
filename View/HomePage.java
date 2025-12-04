package View;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class HomePage extends JPanel {
    private MainFrame mainFrame;

    public HomePage(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(new Color(225, 255, 255)); // Light cyan background

        // Content Panel with Scroll
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(225, 255, 255)); 
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); 

        // Header
        JLabel titleLabel = new JLabel("FILROOM");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(titleLabel);

        JLabel subtitleLabel = new JLabel("Sistem Peminjaman Ruangan");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(subtitleLabel);

        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Info Cards Panel
        JPanel cardsPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        cardsPanel.setBackground(new Color(225, 255, 255));
        cardsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        
        cardsPanel.add(createInfoCard("Booking", "aktif", "2", Color.WHITE));
        cardsPanel.add(createInfoCard("Jam", "Operasional", "07:00 - 21:00", Color.WHITE));
        
        contentPanel.add(cardsPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Recent Bookings Title
        JLabel recentLabel = new JLabel("Peminjaman Terbaru");
        recentLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        recentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel recentLabelPanel = new JPanel(new BorderLayout());
        recentLabelPanel.setBackground(new Color(225, 255, 255));
        recentLabelPanel.add(recentLabel, BorderLayout.WEST);
        recentLabelPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        contentPanel.add(recentLabelPanel);

        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Recent Bookings List
        contentPanel.add(createBookingItem("Lab Pembelajaran G1.3", "15 - 10 - 2025", "09:00 - 11:00", "Dikonfirmasi", new Color(144, 238, 144)));
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(createBookingItem("Ruang Kelas F2.8", "23 - 10 - 2025", "14:00 - 16:00", "Menunggu", new Color(255, 228, 181)));
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(createBookingItem("Lab MGM F9.2", "15 - 10 - 2025", "13:00 - 16:00", "Dikonfirmasi", new Color(144, 238, 144)));

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom Container (Button + Nav)
        JPanel bottomContainer = new JPanel();
        bottomContainer.setLayout(new BoxLayout(bottomContainer, BoxLayout.Y_AXIS));
        bottomContainer.setBackground(new Color(225, 255, 255));

        // Floating Button
        JButton pinjamButton = new JButton("Pinjam Ruangan");
        pinjamButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        pinjamButton.setForeground(Color.WHITE);
        pinjamButton.setBackground(new Color(30, 60, 120)); // Dark Blue
        pinjamButton.setFocusPainted(false);
        pinjamButton.setBorder(new EmptyBorder(15, 30, 15, 30));
        pinjamButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        pinjamButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Icon placeholder (using text for now)
        // pinjamButton.setIcon(...); 
        
        pinjamButton.addActionListener(e -> showDateSelectionDialog());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(225, 255, 255));
        buttonPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        buttonPanel.add(pinjamButton);
        bottomContainer.add(buttonPanel);

        // Bottom Nav
        JPanel bottomNav = new JPanel();
        bottomNav.setBackground(Color.WHITE);
        bottomNav.setPreferredSize(new Dimension(getWidth(), 60));
        bottomNav.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 15));
        
        JLabel homeNav = new JLabel("Home");
        JLabel activityNav = new JLabel("Aktivitas");
        JLabel profileNav = new JLabel("Profil");
        
        homeNav.setFont(new Font("SansSerif", Font.BOLD, 12));
        
        bottomNav.add(homeNav);
        bottomNav.add(activityNav);
        bottomNav.add(profileNav);
        
        bottomContainer.add(bottomNav);
        
        add(bottomContainer, BorderLayout.SOUTH);
    }

    private JPanel createInfoCard(String title1, String title2, String value, Color bgColor) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(bgColor);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(10, 10, 10, 10)
        ));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(bgColor);
        
        JLabel t1 = new JLabel(title1);
        JLabel t2 = new JLabel(title2);
        JLabel v = new JLabel(value);
        
        t1.setFont(new Font("SansSerif", Font.PLAIN, 12));
        t2.setFont(new Font("SansSerif", Font.PLAIN, 12));
        v.setFont(new Font("SansSerif", Font.BOLD, 16));
        
        textPanel.add(t1);
        textPanel.add(t2);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textPanel.add(v);
        
        card.add(textPanel, BorderLayout.CENTER);
        
        // Icon placeholder
        JPanel icon = new JPanel();
        icon.setPreferredSize(new Dimension(30, 30));
        icon.setBackground(new Color(230, 230, 250)); // Light purple placeholder
        card.add(icon, BorderLayout.WEST);
        
        return card;
    }

    private JPanel createBookingItem(String roomName, String date, String time, String status, Color statusColor) {
        JPanel item = new JPanel();
        item.setLayout(new BorderLayout());
        item.setBackground(Color.WHITE);
        item.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(10, 10, 10, 10)));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBackground(Color.WHITE);
        
        JLabel name = new JLabel(roomName);
        name.setFont(new Font("SansSerif", Font.BOLD, 14));
        JLabel d = new JLabel(date);
        d.setFont(new Font("SansSerif", Font.PLAIN, 12));
        JLabel t = new JLabel(time);
        t.setFont(new Font("SansSerif", Font.PLAIN, 12));
        
        left.add(name);
        left.add(d);
        left.add(t);
        
        item.add(left, BorderLayout.CENTER);
        
        JLabel statusLabel = new JLabel(status);
        statusLabel.setOpaque(true);
        statusLabel.setBackground(statusColor);
        statusLabel.setBorder(new EmptyBorder(5, 10, 5, 10));
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
        
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.setBackground(Color.WHITE);
        right.add(statusLabel);
        
        item.add(right, BorderLayout.EAST);
        
        return item;
    }

    private void showDateSelectionDialog() {
        JDialog dialog = new JDialog(mainFrame, "Pilih Tanggal", true);
        dialog.setSize(350, 500);
        dialog.setLocationRelativeTo(mainFrame);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(Color.WHITE);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JLabel title = new JLabel("Pilih Tanggal");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        header.add(title, BorderLayout.WEST);
        
        JButton closeBtn = new JButton("X");
        closeBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        closeBtn.setBorderPainted(false);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setFocusPainted(false);
        closeBtn.addActionListener(e -> dialog.dispose());
        header.add(closeBtn, BorderLayout.EAST);
        
        dialog.add(header, BorderLayout.NORTH);

        // Calendar Content
        JPanel calendarPanel = new JPanel();
        calendarPanel.setLayout(new BoxLayout(calendarPanel, BoxLayout.Y_AXIS));
        calendarPanel.setBackground(Color.WHITE);
        calendarPanel.setBorder(new EmptyBorder(10, 20, 20, 20));

        JLabel monthLabel = new JLabel("Oktober 2025");
        monthLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        monthLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        calendarPanel.add(monthLabel);
        calendarPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Days Grid
        JPanel daysGrid = new JPanel(new GridLayout(0, 7, 10, 10));
        daysGrid.setBackground(Color.WHITE);
        
        String[] days = {"Sen", "Sel", "Rab", "Kam", "Jum", "Sab", "Min"};
        for (String day : days) {
            JLabel l = new JLabel(day, SwingConstants.CENTER);
            l.setForeground(Color.GRAY);
            l.setFont(new Font("SansSerif", Font.PLAIN, 12));
            daysGrid.add(l);
        }

        // Dummy days for October 2025 (Starts Wednesday)
        // 1st Oct 2025 is Wednesday
        for (int i = 0; i < 2; i++) {
            daysGrid.add(new JLabel("")); // Empty slots
        }
        
        for (int i = 1; i <= 31; i++) {
            JLabel dayLabel = new JLabel(String.valueOf(i), SwingConstants.CENTER);
            dayLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
            if (i == 4 || i == 11 || i == 18 || i == 25) {
                dayLabel.setForeground(Color.RED); // Sundays/Holidays
            }
            daysGrid.add(dayLabel);
        }

        calendarPanel.add(daysGrid);
        dialog.add(calendarPanel, BorderLayout.CENTER);

        dialog.setVisible(true);
    }
}
