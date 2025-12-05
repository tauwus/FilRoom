package View;

import Controller.AuthControl;
import Controller.BookingControl;
import Model.User;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

public class HomePage extends JPanel {
    private MainFrame mainFrame;
    private JPanel bookingsListPanel;
    private BookingControl bookingControl;
    
    // Label dinamis untuk angka Booking Aktif
    private JLabel activeBookingCountLabel; 

    public HomePage(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.bookingControl = new BookingControl();
        
        setLayout(new BorderLayout());
        setBackground(new Color(225, 255, 255)); // Light cyan background

        // --- CONTENT WRAPPER (Scrollable) ---
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(225, 255, 255)); 
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); 

        // 1. HEADER (Title)
        JLabel titleLabel = new JLabel("FILROOM");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(titleLabel);

        JLabel subtitleLabel = new JLabel("Sistem Peminjaman Ruangan");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(subtitleLabel);

        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // 2. INFO CARDS (Grid 1 baris, 2 kolom)
        JPanel cardsPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        cardsPanel.setBackground(new Color(225, 255, 255));
        cardsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        
        // -- Card 1: Booking Aktif (Dinamis) --
        activeBookingCountLabel = new JLabel("0"); // Default 0
        activeBookingCountLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        activeBookingCountLabel.setForeground(new Color(30, 30, 140)); // Biru tua
        
        // Masukkan ke helper createInfoCard
        // Parameter: Judul Baris 1, Judul Baris 2, Komponen Nilai
        cardsPanel.add(createInfoCard("Booking", "Aktif", activeBookingCountLabel));
        
        // -- Card 2: Jam Operasional (Statis) --
        JLabel jamLabel = new JLabel("07:00 - 21:00");
        jamLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        jamLabel.setForeground(Color.BLACK);
        
        cardsPanel.add(createInfoCard("Jam", "Operasional", jamLabel));
        
        contentPanel.add(cardsPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // 3. RECENT BOOKINGS TITLE
        JLabel recentLabel = new JLabel("Peminjaman Terbaru");
        recentLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        recentLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel recentLabelPanel = new JPanel(new BorderLayout());
        recentLabelPanel.setBackground(new Color(225, 255, 255));
        recentLabelPanel.add(recentLabel, BorderLayout.WEST);
        recentLabelPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        contentPanel.add(recentLabelPanel);

        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // 4. RECENT BOOKINGS LIST
        bookingsListPanel = new JPanel();
        bookingsListPanel.setLayout(new BoxLayout(bookingsListPanel, BoxLayout.Y_AXIS));
        bookingsListPanel.setBackground(new Color(225, 255, 255));
        contentPanel.add(bookingsListPanel);

        // Masukkan contentPanel ke ScrollPane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);

        // --- BOTTOM SECTION (Button + Nav) ---
        JPanel bottomContainer = new JPanel();
        bottomContainer.setLayout(new BoxLayout(bottomContainer, BoxLayout.Y_AXIS));
        bottomContainer.setBackground(new Color(225, 255, 255));

        // Floating Action Button
        JButton pinjamButton = new JButton("Pinjam Ruangan");
        pinjamButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        pinjamButton.setForeground(Color.WHITE);
        pinjamButton.setBackground(new Color(30, 60, 120)); // Dark Blue
        pinjamButton.setFocusPainted(false);
        pinjamButton.setBorder(new EmptyBorder(15, 30, 15, 30));
        pinjamButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        pinjamButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        pinjamButton.addActionListener(e -> mainFrame.showView("DateSelection"));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(225, 255, 255));
        buttonPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        buttonPanel.add(pinjamButton);
        bottomContainer.add(buttonPanel);

        // Bottom Navigation
        JPanel bottomNav = new JPanel();
        bottomNav.setBackground(Color.WHITE);
        bottomNav.setPreferredSize(new Dimension(getWidth(), 60));
        bottomNav.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 15));
        
        JLabel homeNav = new JLabel("Home");
        homeNav.setFont(new Font("SansSerif", Font.BOLD, 12));
        homeNav.setForeground(new Color(30, 60, 120));
        
        JLabel activityNav = new JLabel("Aktivitas");
        activityNav.setFont(new Font("SansSerif", Font.PLAIN, 12));
        
        JLabel profileNav = new JLabel("Profil");
        profileNav.setFont(new Font("SansSerif", Font.PLAIN, 12));
        
        bottomNav.add(homeNav);
        bottomNav.add(activityNav);
        bottomNav.add(profileNav);
        
        bottomContainer.add(bottomNav);
        
        add(bottomContainer, BorderLayout.SOUTH);

        // --- LISTENER UNTUK REFRESH DATA ---
        // Setiap kali halaman ini muncul, refreshData() dipanggil
        addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                refreshData();
            }
            @Override
            public void ancestorRemoved(AncestorEvent event) {}
            @Override
            public void ancestorMoved(AncestorEvent event) {}
        });
    }

    /**
     * Mengambil data terbaru dari database (BookingControl) 
     * dan memperbarui tampilan UI.
     */
    private void refreshData() {
        bookingsListPanel.removeAll();
        User user = AuthControl.getCurrentUser();
        
        if (user != null) {
            // A. Update List Peminjaman Terbaru
            List<String[]> bookings = bookingControl.getRecentBookings(user.getId());
            
            if (bookings.isEmpty()) {
                JLabel emptyLabel = new JLabel("Belum ada riwayat peminjaman.");
                emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                emptyLabel.setForeground(Color.GRAY);
                bookingsListPanel.add(emptyLabel);
            } else {
                for (String[] b : bookings) {
                    // b[0]=room, b[1]=date, b[2]=time, b[3]=status
                    Color statusColor = Color.LIGHT_GRAY;
                    String statusText = b[3];
                    
                    if ("Disetujui".equalsIgnoreCase(statusText)) {
                        statusColor = new Color(144, 238, 144); // Hijau
                    } else if ("Menunggu".equalsIgnoreCase(statusText)) {
                        statusColor = new Color(255, 228, 181); // Kuning/Orange
                    } else if ("Ditolak".equalsIgnoreCase(statusText)) {
                        statusColor = new Color(255, 100, 100); // Merah
                    }
                    
                    bookingsListPanel.add(createBookingItem(b[0], b[1], b[2], statusText, statusColor));
                    bookingsListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                }
            }

            // B. Update Angka Booking Aktif
            int activeCount = bookingControl.countActiveBookings(user.getId());
            activeBookingCountLabel.setText(String.valueOf(activeCount));
        }
        
        bookingsListPanel.revalidate();
        bookingsListPanel.repaint();
    }

    /**
     * Helper untuk membuat Card Info (Booking Aktif & Jam Ops)
     */
    private JPanel createInfoCard(String title1, String title2, Component valueComponent) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);
        
        JLabel t1 = new JLabel(title1);
        JLabel t2 = new JLabel(title2);
        
        t1.setFont(new Font("SansSerif", Font.PLAIN, 12));
        t2.setFont(new Font("SansSerif", Font.PLAIN, 12));
        t1.setForeground(Color.GRAY);
        t2.setForeground(Color.GRAY);
        
        textPanel.add(t1);
        textPanel.add(t2);
        textPanel.add(Box.createRigidArea(new Dimension(0, 8))); // Jarak antara judul dan nilai
        
        // Menambahkan komponen nilai (Angka/Jam)
        textPanel.add(valueComponent);
        
        card.add(textPanel, BorderLayout.CENTER);
        
        // Icon placeholder di sebelah kiri
        // JPanel icon = new JPanel();
        // icon.setPreferredSize(new Dimension(40, 40));
        // icon.setBackground(new Color(240, 240, 255)); // Ungu sangat muda
        // // Bisa ditambahkan JLabel gambar icon di sini jika ada
        
        // card.add(icon, BorderLayout.WEST);
        
        return card;
    }

    /**
     * Helper untuk membuat Item List Peminjaman
     */
    private JPanel createBookingItem(String roomName, String date, String time, String status, Color statusColor) {
        JPanel item = new JPanel();
        item.setLayout(new BorderLayout());
        item.setBackground(Color.WHITE);
        item.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(10, 15, 10, 15)));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        // Kiri: Info Ruangan
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBackground(Color.WHITE);
        
        JLabel name = new JLabel(roomName);
        name.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        JLabel d = new JLabel(date);
        d.setFont(new Font("SansSerif", Font.PLAIN, 12));
        d.setForeground(Color.DARK_GRAY);
        
        JLabel t = new JLabel(time);
        t.setFont(new Font("SansSerif", Font.PLAIN, 12));
        t.setForeground(Color.GRAY);
        
        left.add(name);
        left.add(Box.createRigidArea(new Dimension(0, 2)));
        left.add(d);
        left.add(t);
        
        item.add(left, BorderLayout.CENTER);
        
        // Kanan: Status Badge
        JLabel statusLabel = new JLabel(status);
        statusLabel.setOpaque(true);
        statusLabel.setBackground(statusColor);
        statusLabel.setBorder(new EmptyBorder(5, 10, 5, 10)); // Padding dalam badge
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 10));
        
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.setBackground(Color.WHITE);
        right.add(statusLabel);
        
        item.add(right, BorderLayout.EAST);
        
        return item;
    }
}