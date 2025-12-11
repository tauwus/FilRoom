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
    private JLabel welcomeLabel;
    
    // Colors
    private static final Color BG_COLOR = new Color(225, 255, 255);
    private static final Color PRIMARY_BLUE = new Color(30, 60, 120);
    private static final Color CARD_WHITE = Color.WHITE;

    public HomePage(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.bookingControl = new BookingControl();
        
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);

        // --- CONTENT WRAPPER (Scrollable) ---
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(BG_COLOR);
        contentPanel.setBorder(new EmptyBorder(25, 20, 20, 20));

        // 1. HEADER (Title)
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel = new JLabel("FILROOM");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setForeground(PRIMARY_BLUE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(titleLabel);

        JLabel subtitleLabel = new JLabel("Sistem Peminjaman Ruangan");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(subtitleLabel);
        
        contentPanel.add(headerPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Welcome Card
        JPanel welcomeCard = createCard();
        welcomeCard.setLayout(new BorderLayout());
        welcomeCard.setBorder(new EmptyBorder(15, 20, 15, 20));
        welcomeCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        
        welcomeLabel = new JLabel("Selamat Datang!");
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        welcomeLabel.setForeground(PRIMARY_BLUE);
        welcomeCard.add(welcomeLabel, BorderLayout.CENTER);
        
        JLabel waveEmoji = new JLabel("👋");
        waveEmoji.setFont(new Font("SansSerif", Font.PLAIN, 24));
        welcomeCard.add(waveEmoji, BorderLayout.EAST);
        
        contentPanel.add(welcomeCard);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // 2. INFO CARDS (Grid 1 baris, 2 kolom)
        JPanel cardsPanel = new JPanel(new GridLayout(1, 2, 12, 0));
        cardsPanel.setOpaque(false);
        cardsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        
        // -- Card 1: Booking Aktif (Dinamis) --
        activeBookingCountLabel = new JLabel("0");
        activeBookingCountLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        activeBookingCountLabel.setForeground(PRIMARY_BLUE);
        
        cardsPanel.add(createInfoCard("📋", "Booking Aktif", activeBookingCountLabel));
        
        // -- Card 2: Jam Operasional (Statis) --
        JLabel jamLabel = new JLabel("07:00 - 21:00");
        jamLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        jamLabel.setForeground(new Color(60, 60, 60));
        
        cardsPanel.add(createInfoCard("🕐", "Jam Operasional", jamLabel));
        
        contentPanel.add(cardsPanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // 3. RECENT BOOKINGS TITLE
        JPanel recentHeader = new JPanel(new BorderLayout());
        recentHeader.setOpaque(false);
        recentHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        
        JLabel recentLabel = new JLabel("Peminjaman Terbaru");
        recentLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        recentLabel.setForeground(new Color(50, 50, 50));
        recentHeader.add(recentLabel, BorderLayout.WEST);
        
        contentPanel.add(recentHeader);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        // 4. RECENT BOOKINGS LIST
        bookingsListPanel = new JPanel();
        bookingsListPanel.setLayout(new BoxLayout(bookingsListPanel, BoxLayout.Y_AXIS));
        bookingsListPanel.setOpaque(false);
        contentPanel.add(bookingsListPanel);

        // Masukkan contentPanel ke ScrollPane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // --- BOTTOM SECTION (Button + Nav) ---
        JPanel bottomContainer = new JPanel();
        bottomContainer.setLayout(new BoxLayout(bottomContainer, BoxLayout.Y_AXIS));
        bottomContainer.setBackground(BG_COLOR);

        // Floating Action Button
        JButton pinjamButton = new JButton("+ Pinjam Ruangan");
        pinjamButton.setFont(new Font("SansSerif", Font.BOLD, 15));
        pinjamButton.setForeground(Color.WHITE);
        pinjamButton.setBackground(PRIMARY_BLUE);
        pinjamButton.setFocusPainted(false);
        pinjamButton.setBorderPainted(false);
        pinjamButton.setPreferredSize(new Dimension(280, 50));
        pinjamButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        pinjamButton.addActionListener(e -> mainFrame.showView("DateSelection"));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        buttonPanel.add(pinjamButton);
        bottomContainer.add(buttonPanel);

        // Bottom Navigation
        bottomContainer.add(new BottomNavPanel(mainFrame, "Home"));
        
        add(bottomContainer, BorderLayout.SOUTH);

        // --- LISTENER UNTUK REFRESH DATA ---
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
    
    private JPanel createCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            }
        };
        card.setOpaque(false);
        return card;
    }

    private void refreshData() {
        bookingsListPanel.removeAll();
        User user = AuthControl.getCurrentUser();
        
        if (user != null) {
            // Update welcome label
            welcomeLabel.setText("Selamat Datang, " + user.getName().split(" ")[0] + "!");
            
            // A. Update List Peminjaman Terbaru
            List<String[]> bookings = bookingControl.getRecentBookings(user.getId());
            
            if (bookings.isEmpty()) {
                JPanel emptyCard = createCard();
                emptyCard.setLayout(new GridBagLayout());
                emptyCard.setBorder(new EmptyBorder(30, 20, 30, 20));
                emptyCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
                
                JLabel emptyLabel = new JLabel("Belum ada riwayat peminjaman");
                emptyLabel.setForeground(Color.GRAY);
                emptyLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
                emptyCard.add(emptyLabel);
                
                bookingsListPanel.add(emptyCard);
            } else {
                for (String[] b : bookings) {
                    bookingsListPanel.add(createBookingItem(b[0], b[1], b[2], b[3]));
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

    private JPanel createInfoCard(String icon, String title, JLabel valueLabel) {
        JPanel card = createCard();
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        card.add(iconLabel, BorderLayout.WEST);
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.setBorder(new EmptyBorder(0, 12, 0, 0));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        titleLabel.setForeground(Color.GRAY);
        
        textPanel.add(titleLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        textPanel.add(valueLabel);
        
        card.add(textPanel, BorderLayout.CENTER);
        
        return card;
    }

    private JPanel createBookingItem(String roomName, String date, String time, String status) {
        JPanel card = createCard();
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(12, 15, 12, 15));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 85));

        // Left: Info
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);
        
        JLabel name = new JLabel(roomName);
        name.setFont(new Font("SansSerif", Font.BOLD, 14));
        name.setForeground(new Color(40, 40, 40));
        
        JLabel d = new JLabel("📅 " + date);
        d.setFont(new Font("SansSerif", Font.PLAIN, 12));
        d.setForeground(Color.GRAY);
        
        JLabel t = new JLabel("🕐 " + time);
        t.setFont(new Font("SansSerif", Font.PLAIN, 12));
        t.setForeground(Color.GRAY);
        
        left.add(name);
        left.add(Box.createRigidArea(new Dimension(0, 4)));
        left.add(d);
        left.add(t);
        
        card.add(left, BorderLayout.CENTER);
        
        // Right: Status Badge
        Color statusColor = new Color(180, 180, 180);
        Color textColor = Color.WHITE;
        
        if ("Disetujui".equalsIgnoreCase(status)) {
            statusColor = new Color(76, 175, 80);
        } else if ("Menunggu".equalsIgnoreCase(status)) {
            statusColor = new Color(255, 152, 0);
        } else if ("Ditolak".equalsIgnoreCase(status)) {
            statusColor = new Color(244, 67, 54);
        } else if ("Selesai".equalsIgnoreCase(status)) {
            statusColor = new Color(96, 125, 139);
        }
        
        JLabel statusLabel = new JLabel(status) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };
        statusLabel.setOpaque(false);
        statusLabel.setBackground(statusColor);
        statusLabel.setForeground(textColor);
        statusLabel.setBorder(new EmptyBorder(5, 10, 5, 10));
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 10));
        
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        right.add(statusLabel);
        
        card.add(right, BorderLayout.EAST);
        
        return card;
    }
}