package View;

import Controller.AdminBookingControl;
import Controller.AuthControl;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AdminDashboard extends JPanel {
    private MainFrame mainFrame;
    private JPanel contentArea;
    private CardLayout cardLayout;
    private AdminBookingControl bookingControl;
    
    // Colors matching User GUI
    private static final Color BG_COLOR = new Color(225, 255, 255);
    private static final Color PRIMARY_BLUE = new Color(30, 60, 120);
    private static final Color CARD_WHITE = Color.WHITE;
    private static final Color TEXT_DARK = new Color(30, 30, 30);
    
    // Navigation buttons for active state tracking
    private JButton[] navButtons;
    private int activeNav = 0;

    public AdminDashboard(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.bookingControl = new AdminBookingControl();
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);

        // === HEADER ===
        JPanel header = createHeader();
        add(header, BorderLayout.NORTH);

        // === CONTENT AREA ===
        cardLayout = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(BG_COLOR);
        
        contentArea.add(new AdminHomePanel(), "Home");
        contentArea.add(new AdminRoomPage(), "Rooms");
        contentArea.add(new AdminBookingPage(), "Bookings");

        add(contentArea, BorderLayout.CENTER);

        // === BOTTOM NAVIGATION ===
        JPanel bottomNav = createBottomNav();
        add(bottomNav, BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(CARD_WHITE);
        header.setBorder(new EmptyBorder(15, 20, 15, 20));
        header.setPreferredSize(new Dimension(getWidth(), 70));

        // Title
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        
        JLabel title = new JLabel("FILROOM");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(PRIMARY_BLUE);
        
        JLabel subtitle = new JLabel("Admin Panel");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitle.setForeground(Color.GRAY);
        
        titlePanel.add(title);
        titlePanel.add(subtitle);
        header.add(titlePanel, BorderLayout.WEST);

        // Logout Button
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setBackground(new Color(200, 80, 80));
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.setPreferredSize(new Dimension(80, 35));
        logoutBtn.addActionListener(e -> {
            AuthControl.logout();
            mainFrame.showView("Login");
        });
        header.add(logoutBtn, BorderLayout.EAST);

        return header;
    }

    private JPanel createBottomNav() {
        JPanel nav = new JPanel(new GridLayout(1, 3, 0, 0));
        nav.setBackground(CARD_WHITE);
        nav.setPreferredSize(new Dimension(getWidth(), 60));
        nav.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)));

        navButtons = new JButton[3];
        String[] labels = {"🏠 Home", "🚪 Ruangan", "📋 Booking"};
        String[] cards = {"Home", "Rooms", "Bookings"};

        for (int i = 0; i < 3; i++) {
            final int index = i;
            JButton btn = new JButton(labels[i]);
            btn.setFont(new Font("SansSerif", Font.PLAIN, 12));
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setBackground(CARD_WHITE);
            btn.setForeground(Color.GRAY);
            
            btn.addActionListener(e -> {
                setActiveNav(index);
                cardLayout.show(contentArea, cards[index]);
            });
            
            navButtons[i] = btn;
            nav.add(btn);
        }
        
        setActiveNav(0); // Default Home active
        return nav;
    }
    
    private void setActiveNav(int index) {
        activeNav = index;
        for (int i = 0; i < navButtons.length; i++) {
            if (i == index) {
                navButtons[i].setForeground(PRIMARY_BLUE);
                navButtons[i].setFont(new Font("SansSerif", Font.BOLD, 12));
            } else {
                navButtons[i].setForeground(Color.GRAY);
                navButtons[i].setFont(new Font("SansSerif", Font.PLAIN, 12));
            }
        }
    }

    // ====== ADMIN HOME PANEL ======
    class AdminHomePanel extends JPanel {
        public AdminHomePanel() {
            setLayout(new BorderLayout());
            setBackground(BG_COLOR);
            setBorder(new EmptyBorder(20, 20, 20, 20));

            JPanel content = new JPanel();
            content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
            content.setBackground(BG_COLOR);

            // Welcome Card
            JPanel welcomeCard = createCard();
            welcomeCard.setLayout(new BorderLayout());
            welcomeCard.setBorder(new EmptyBorder(20, 20, 20, 20));
            
            JLabel welcomeLabel = new JLabel("Selamat Datang, Admin!");
            welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
            welcomeLabel.setForeground(TEXT_DARK);
            welcomeCard.add(welcomeLabel, BorderLayout.NORTH);
            
            JLabel descLabel = new JLabel("<html>Kelola ruangan dan persetujuan peminjaman<br>dari panel ini.</html>");
            descLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
            descLabel.setForeground(Color.GRAY);
            descLabel.setBorder(new EmptyBorder(10, 0, 0, 0));
            welcomeCard.add(descLabel, BorderLayout.CENTER);
            
            content.add(welcomeCard);
            content.add(Box.createRigidArea(new Dimension(0, 15)));

            // Stats Cards
            JPanel statsPanel = new JPanel(new GridLayout(1, 2, 15, 0));
            statsPanel.setOpaque(false);
            statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
            
            int pendingCount = bookingControl.countPendingBookings();
            statsPanel.add(createStatCard("📋", "Menunggu", String.valueOf(pendingCount), new Color(255, 180, 50)));
            statsPanel.add(createStatCard("🚪", "Ruangan", "Kelola", PRIMARY_BLUE));
            
            content.add(statsPanel);
            
            add(content, BorderLayout.NORTH);
        }

        private JPanel createStatCard(String icon, String label, String value, Color accentColor) {
            JPanel card = createCard();
            card.setLayout(new BorderLayout());
            card.setBorder(new EmptyBorder(15, 15, 15, 15));
            
            JLabel iconLabel = new JLabel(icon);
            iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 24));
            card.add(iconLabel, BorderLayout.WEST);
            
            JPanel textPanel = new JPanel();
            textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
            textPanel.setOpaque(false);
            textPanel.setBorder(new EmptyBorder(0, 10, 0, 0));
            
            JLabel valueLabel = new JLabel(value);
            valueLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
            valueLabel.setForeground(accentColor);
            
            JLabel titleLabel = new JLabel(label);
            titleLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
            titleLabel.setForeground(Color.GRAY);
            
            textPanel.add(valueLabel);
            textPanel.add(titleLabel);
            card.add(textPanel, BorderLayout.CENTER);
            
            return card;
        }
    }

    // Helper to create rounded card panel
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
}