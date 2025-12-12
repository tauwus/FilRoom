package View;

import Controller.AdminBookingControl;
import Controller.AdminRoomControl;
import Controller.AdminUserControl;
import Controller.AuthControl;
import Controller.ReportControl;
import Model.CivitasAkademik;
import Model.Room;
import Model.Booking;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AdminDashboard extends JPanel {
    private MainFrame mainFrame;
    private JPanel contentArea;
    private CardLayout cardLayout;
    
    // Controllers
    private AdminBookingControl bookingControl;
    private AdminRoomControl roomControl;
    private AdminUserControl userControl;
    private ReportControl reportControl;
    
    // Modern Color Palette
    private static final Color BG_COLOR = new Color(241, 245, 249);
    private static final Color PRIMARY_BLUE = new Color(37, 99, 235);
    private static final Color SUCCESS_GREEN = new Color(34, 197, 94);
    private static final Color WARNING_ORANGE = new Color(249, 115, 22);
    private static final Color DANGER_RED = new Color(239, 68, 68);
    private static final Color CARD_WHITE = Color.WHITE;
    private static final Color TEXT_DARK = new Color(30, 41, 59);
    private static final Color TEXT_MUTED = new Color(100, 116, 139);
    private static final Color NAV_INACTIVE = new Color(150, 150, 150);
    
    // Navigation state
    private String activePage = "Dashboard";
    private JPanel bottomNav;
    
    // Side menu state
    private boolean sideMenuOpen = false;
    private JPanel sideMenuOverlay;

    public AdminDashboard(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.bookingControl = new AdminBookingControl();
        this.roomControl = new AdminRoomControl();
        this.userControl = new AdminUserControl();
        this.reportControl = new ReportControl();
        
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);
        
        // Main container with layered pane for overlay
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(new OverlayLayout(layeredPane));
        
        // Main content panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        
        // Header
        mainPanel.add(createHeader(), BorderLayout.NORTH);
        
        // Content Area
        cardLayout = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(BG_COLOR);
        
        contentArea.add(createDashboardPanel(), "Dashboard");
        contentArea.add(new AdminBookingPanel(), "Booking");
        contentArea.add(new AdminRoomPanel(), "Ruangan");
        contentArea.add(new AdminUserPanel(), "Pengguna");
        
        mainPanel.add(contentArea, BorderLayout.CENTER);
        
        // Bottom Navigation
        bottomNav = createBottomNav();
        mainPanel.add(bottomNav, BorderLayout.SOUTH);
        
        // Side Menu Overlay (hidden by default)
        sideMenuOverlay = createSideMenuOverlay();
        sideMenuOverlay.setVisible(false);
        
        layeredPane.add(mainPanel, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(sideMenuOverlay, JLayeredPane.POPUP_LAYER);
        
        add(layeredPane, BorderLayout.CENTER);
    }
    
    // ====== HEADER ======
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY_BLUE);
        header.setPreferredSize(new Dimension(getWidth(), 56));
        header.setBorder(new EmptyBorder(0, 15, 0, 15));
        
        // Left: Menu Button + Title
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        leftPanel.setOpaque(false);
        
        JButton menuBtn = new JButton("☰");
        menuBtn.setFont(new Font("SansSerif", Font.PLAIN, 20));
        menuBtn.setForeground(Color.WHITE);
        menuBtn.setBackground(PRIMARY_BLUE);
        menuBtn.setBorderPainted(false);
        menuBtn.setFocusPainted(false);
        menuBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        menuBtn.setPreferredSize(new Dimension(40, 36));
        menuBtn.addActionListener(e -> toggleSideMenu());
        leftPanel.add(menuBtn);
        
        JLabel titleLabel = new JLabel("FilRoom Admin");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        leftPanel.add(titleLabel);
        
        header.add(leftPanel, BorderLayout.WEST);
        
        // Right: Admin info
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        rightPanel.setOpaque(false);
        
        JLabel adminLabel = new JLabel("👤 Admin");
        adminLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        adminLabel.setForeground(Color.WHITE);
        rightPanel.add(adminLabel);
        
        header.add(rightPanel, BorderLayout.EAST);
        
        return header;
    }
    
    // ====== BOTTOM NAVIGATION ======
    private JPanel createBottomNav() {
        JPanel nav = new JPanel(new GridLayout(1, 4, 0, 0));
        nav.setBackground(CARD_WHITE);
        nav.setPreferredSize(new Dimension(getWidth(), 65));
        nav.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(226, 232, 240)));
        
        nav.add(createNavItem("Dashboard", "📊", "Dashboard"));
        nav.add(createNavItem("Booking", "📋", "Booking"));
        nav.add(createNavItem("Ruangan", "🚪", "Ruangan"));
        nav.add(createNavItem("Pengguna", "👥", "Pengguna"));
        
        return nav;
    }
    
    private JPanel createNavItem(String label, String icon, String cardName) {
        boolean isActive = label.equals(activePage);
        
        JPanel item = new JPanel();
        item.setLayout(new BoxLayout(item, BoxLayout.Y_AXIS));
        item.setBackground(CARD_WHITE);
        item.setBorder(new EmptyBorder(8, 5, 8, 5));
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        iconLabel.setForeground(isActive ? PRIMARY_BLUE : NAV_INACTIVE);
        
        JLabel textLabel = new JLabel(label);
        textLabel.setFont(new Font("SansSerif", isActive ? Font.BOLD : Font.PLAIN, 10));
        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        textLabel.setForeground(isActive ? PRIMARY_BLUE : NAV_INACTIVE);
        
        item.add(Box.createVerticalGlue());
        item.add(iconLabel);
        item.add(Box.createRigidArea(new Dimension(0, 2)));
        item.add(textLabel);
        
        if (isActive) {
            JPanel indicator = new JPanel();
            indicator.setBackground(PRIMARY_BLUE);
            indicator.setMaximumSize(new Dimension(30, 3));
            indicator.setPreferredSize(new Dimension(30, 3));
            indicator.setAlignmentX(Component.CENTER_ALIGNMENT);
            item.add(Box.createRigidArea(new Dimension(0, 2)));
            item.add(indicator);
        }
        item.add(Box.createVerticalGlue());
        
        item.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (!label.equals(activePage)) {
                    activePage = label;
                    cardLayout.show(contentArea, cardName);
                    refreshBottomNav();
                }
            }
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (!label.equals(activePage)) {
                    item.setBackground(new Color(248, 250, 252));
                }
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                item.setBackground(CARD_WHITE);
            }
        });
        
        return item;
    }
    
    private void refreshBottomNav() {
        bottomNav.removeAll();
        bottomNav.add(createNavItem("Dashboard", "📊", "Dashboard"));
        bottomNav.add(createNavItem("Booking", "📋", "Booking"));
        bottomNav.add(createNavItem("Ruangan", "🚪", "Ruangan"));
        bottomNav.add(createNavItem("Pengguna", "👥", "Pengguna"));
        bottomNav.revalidate();
        bottomNav.repaint();
    }
    
    // ====== SIDE MENU OVERLAY ======
    private JPanel createSideMenuOverlay() {
        JPanel overlay = new JPanel(new BorderLayout());
        overlay.setOpaque(false);
        
        // Dark backdrop
        JPanel backdrop = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(0, 0, 0, 100));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backdrop.setOpaque(false);
        backdrop.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                toggleSideMenu();
            }
        });
        
        // Side menu panel
        JPanel sideMenu = new JPanel(new BorderLayout());
        sideMenu.setBackground(CARD_WHITE);
        sideMenu.setPreferredSize(new Dimension(250, 0));
        sideMenu.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(226, 232, 240)));
        
        // Menu Header
        JPanel menuHeader = new JPanel(new BorderLayout());
        menuHeader.setBackground(PRIMARY_BLUE);
        menuHeader.setPreferredSize(new Dimension(250, 120));
        menuHeader.setBorder(new EmptyBorder(20, 15, 20, 15));
        
        JPanel headerContent = new JPanel();
        headerContent.setLayout(new BoxLayout(headerContent, BoxLayout.Y_AXIS));
        headerContent.setOpaque(false);
        
        JLabel avatarLabel = new JLabel("👤");
        avatarLabel.setFont(new Font("SansSerif", Font.PLAIN, 36));
        avatarLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel nameLabel = new JLabel("Administrator");
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel roleLabel = new JLabel("Super Admin");
        roleLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        roleLabel.setForeground(new Color(200, 220, 255));
        roleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        headerContent.add(avatarLabel);
        headerContent.add(Box.createRigidArea(new Dimension(0, 8)));
        headerContent.add(nameLabel);
        headerContent.add(roleLabel);
        
        menuHeader.add(headerContent, BorderLayout.CENTER);
        sideMenu.add(menuHeader, BorderLayout.NORTH);
        
        // Menu Items
        JPanel menuItems = new JPanel();
        menuItems.setLayout(new BoxLayout(menuItems, BoxLayout.Y_AXIS));
        menuItems.setBackground(CARD_WHITE);
        menuItems.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        menuItems.add(createMenuItem("📊", "Dashboard", () -> navigateTo("Dashboard")));
        menuItems.add(createMenuItem("📋", "Kelola Booking", () -> navigateTo("Booking")));
        menuItems.add(createMenuItem("🚪", "Kelola Ruangan", () -> navigateTo("Ruangan")));
        menuItems.add(createMenuItem("👥", "Kelola Pengguna", () -> navigateTo("Pengguna")));
        
        menuItems.add(createMenuDivider());
        
        menuItems.add(createMenuItem("📈", "Laporan", () -> {
            toggleSideMenu();
            JOptionPane.showMessageDialog(this, "Fitur Laporan dalam pengembangan", "Info", JOptionPane.INFORMATION_MESSAGE);
        }));
        menuItems.add(createMenuItem("⚙️", "Pengaturan", () -> {
            toggleSideMenu();
            JOptionPane.showMessageDialog(this, "Fitur Pengaturan dalam pengembangan", "Info", JOptionPane.INFORMATION_MESSAGE);
        }));
        
        menuItems.add(Box.createVerticalGlue());
        
        // Logout at bottom
        menuItems.add(createMenuDivider());
        menuItems.add(createMenuItem("🚪", "Logout", () -> {
            toggleSideMenu();
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Apakah Anda yakin ingin logout?", 
                "Konfirmasi Logout", 
                JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                AuthControl.logout();
                mainFrame.showView("Login");
            }
        }, DANGER_RED));
        
        JScrollPane scrollPane = new JScrollPane(menuItems);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sideMenu.add(scrollPane, BorderLayout.CENTER);
        
        overlay.add(sideMenu, BorderLayout.WEST);
        overlay.add(backdrop, BorderLayout.CENTER);
        
        return overlay;
    }
    
    private JPanel createMenuItem(String icon, String label, Runnable action) {
        return createMenuItem(icon, label, action, TEXT_DARK);
    }
    
    private JPanel createMenuItem(String icon, String label, Runnable action, Color textColor) {
        JPanel item = new JPanel(new BorderLayout());
        item.setBackground(CARD_WHITE);
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));
        item.setBorder(new EmptyBorder(12, 20, 12, 20));
        
        JPanel content = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        content.setOpaque(false);
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        
        JLabel textLabel = new JLabel(label);
        textLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        textLabel.setForeground(textColor);
        
        content.add(iconLabel);
        content.add(textLabel);
        
        item.add(content, BorderLayout.CENTER);
        
        item.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                action.run();
            }
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                item.setBackground(new Color(248, 250, 252));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                item.setBackground(CARD_WHITE);
            }
        });
        
        return item;
    }
    
    private JPanel createMenuDivider() {
        JPanel divider = new JPanel();
        divider.setBackground(new Color(226, 232, 240));
        divider.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        divider.setPreferredSize(new Dimension(0, 1));
        return divider;
    }
    
    private void toggleSideMenu() {
        sideMenuOpen = !sideMenuOpen;
        sideMenuOverlay.setVisible(sideMenuOpen);
        revalidate();
        repaint();
    }
    
    private void navigateTo(String page) {
        toggleSideMenu();
        activePage = page;
        cardLayout.show(contentArea, page);
        refreshBottomNav();
    }
    
    // ====== DASHBOARD PANEL ======
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);
        
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Welcome
        JLabel welcomeLabel = new JLabel("Selamat Datang, Admin!");
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        welcomeLabel.setForeground(TEXT_DARK);
        welcomeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(welcomeLabel);
        
        JLabel subtitleLabel = new JLabel("Panel administrasi FilRoom");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitleLabel.setForeground(TEXT_MUTED);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(subtitleLabel);
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Stats Grid
        JPanel statsGrid = new JPanel(new GridLayout(2, 2, 12, 12));
        statsGrid.setOpaque(false);
        statsGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        statsGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        
        int pendingCount = 0;
        int roomCount = 0;
        int userCount = 0;
        int todayBookings = 0;
        
        try {
            List<Booking> bookings = bookingControl.getAllBookings();
            for (Booking b : bookings) {
                if (b.getStatus().name().equals("PENDING")) pendingCount++;
            }
            todayBookings = bookings.size();
            roomCount = roomControl.getAllRooms().size();
            userCount = userControl.getAllUsers().size();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        statsGrid.add(createStatCard("📋", "Pending", String.valueOf(pendingCount), WARNING_ORANGE));
        statsGrid.add(createStatCard("🚪", "Ruangan", String.valueOf(roomCount), PRIMARY_BLUE));
        statsGrid.add(createStatCard("👥", "Pengguna", String.valueOf(userCount), SUCCESS_GREEN));
        statsGrid.add(createStatCard("📅", "Total Booking", String.valueOf(todayBookings), DANGER_RED));
        
        content.add(statsGrid);
        content.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Quick Actions
        JLabel actionsLabel = new JLabel("Aksi Cepat");
        actionsLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        actionsLabel.setForeground(TEXT_DARK);
        actionsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(actionsLabel);
        content.add(Box.createRigidArea(new Dimension(0, 12)));
        
        JPanel actionsPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        actionsPanel.setOpaque(false);
        actionsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        actionsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        
        actionsPanel.add(createActionButton("📋 Booking", PRIMARY_BLUE, () -> navigateToPage("Booking")));
        actionsPanel.add(createActionButton("🚪 Ruangan", SUCCESS_GREEN, () -> navigateToPage("Ruangan")));
        actionsPanel.add(createActionButton("👥 Pengguna", WARNING_ORANGE, () -> navigateToPage("Pengguna")));
        
        content.add(actionsPanel);
        content.add(Box.createVerticalGlue());
        
        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }
    
    private void navigateToPage(String page) {
        activePage = page;
        cardLayout.show(contentArea, page);
        refreshBottomNav();
    }
    
    private JPanel createStatCard(String icon, String label, String value, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 24));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        valueLabel.setForeground(accentColor);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel textLabel = new JLabel(label);
        textLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        textLabel.setForeground(TEXT_MUTED);
        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        content.add(Box.createVerticalGlue());
        content.add(iconLabel);
        content.add(valueLabel);
        content.add(textLabel);
        content.add(Box.createVerticalGlue());
        
        card.add(content, BorderLayout.CENTER);
        return card;
    }
    
    private JButton createActionButton(String text, Color bgColor, Runnable action) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 11));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bgColor);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> action.run());
        return btn;
    }

    // ====== ADMIN BOOKING PANEL ======
    private class AdminBookingPanel extends JPanel {
        public AdminBookingPanel() {
            setLayout(new BorderLayout());
            setBackground(BG_COLOR);
            
            // Header
            JPanel header = new JPanel(new BorderLayout());
            header.setBackground(BG_COLOR);
            header.setBorder(new EmptyBorder(15, 20, 10, 20));
            
            JLabel titleLabel = new JLabel("Kelola Booking");
            titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
            titleLabel.setForeground(TEXT_DARK);
            header.add(titleLabel, BorderLayout.WEST);
            
            JButton refreshBtn = createRefreshButton(() -> refreshBookings());
            header.add(refreshBtn, BorderLayout.EAST);
            
            add(header, BorderLayout.NORTH);
            
            // Content
            JPanel content = new JPanel();
            content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
            content.setOpaque(false);
            content.setBorder(new EmptyBorder(0, 20, 20, 20));
            
            loadBookings(content);
            
            JScrollPane scrollPane = new JScrollPane(content);
            scrollPane.setBorder(null);
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            
            add(scrollPane, BorderLayout.CENTER);
        }
        
        private void loadBookings(JPanel container) {
            container.removeAll();
            try {
                List<Booking> bookings = bookingControl.getAllBookings();
                if (bookings.isEmpty()) {
                    JLabel emptyLabel = new JLabel("Tidak ada booking");
                    emptyLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
                    emptyLabel.setForeground(TEXT_MUTED);
                    emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                    container.add(Box.createVerticalGlue());
                    container.add(emptyLabel);
                    container.add(Box.createVerticalGlue());
                } else {
                    for (Booking booking : bookings) {
                        container.add(createBookingCard(booking));
                        container.add(Box.createRigidArea(new Dimension(0, 10)));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            container.revalidate();
            container.repaint();
        }
        
        private JPanel createBookingCard(Booking booking) {
            JPanel card = new JPanel(new BorderLayout(10, 0));
            card.setBackground(CARD_WHITE);
            card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                new EmptyBorder(12, 15, 12, 15)
            ));
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
            
            // Left info
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.setOpaque(false);
            
            String userName = booking.getUser() != null ? booking.getUser().getName() : "Unknown";
            JLabel nameLabel = new JLabel("👤 " + userName);
            nameLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
            nameLabel.setForeground(TEXT_DARK);
            
            JLabel dateLabel = new JLabel("📅 " + booking.getSubmissionDate().toString());
            dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
            dateLabel.setForeground(TEXT_MUTED);
            
            JLabel idLabel = new JLabel("ID: " + booking.getId());
            idLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
            idLabel.setForeground(TEXT_MUTED);
            
            infoPanel.add(nameLabel);
            infoPanel.add(Box.createRigidArea(new Dimension(0, 3)));
            infoPanel.add(dateLabel);
            infoPanel.add(idLabel);
            
            card.add(infoPanel, BorderLayout.CENTER);
            
            // Right: Status + Actions
            JPanel rightPanel = new JPanel();
            rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
            rightPanel.setOpaque(false);
            
            String status = booking.getStatus().name();
            JLabel statusBadge = createStatusBadge(status);
            statusBadge.setAlignmentX(Component.RIGHT_ALIGNMENT);
            rightPanel.add(statusBadge);
            
            if (status.equals("PENDING")) {
                rightPanel.add(Box.createRigidArea(new Dimension(0, 8)));
                JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
                btnPanel.setOpaque(false);
                
                JButton approveBtn = new JButton("✓");
                approveBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
                approveBtn.setBackground(SUCCESS_GREEN);
                approveBtn.setForeground(Color.WHITE);
                approveBtn.setPreferredSize(new Dimension(35, 28));
                approveBtn.setBorderPainted(false);
                approveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                approveBtn.addActionListener(e -> {
                    bookingControl.approveBooking(booking.getId());
                    refreshBookings();
                });
                
                JButton rejectBtn = new JButton("✗");
                rejectBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
                rejectBtn.setBackground(DANGER_RED);
                rejectBtn.setForeground(Color.WHITE);
                rejectBtn.setPreferredSize(new Dimension(35, 28));
                rejectBtn.setBorderPainted(false);
                rejectBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                rejectBtn.addActionListener(e -> {
                    bookingControl.rejectBooking(booking.getId());
                    refreshBookings();
                });
                
                btnPanel.add(approveBtn);
                btnPanel.add(rejectBtn);
                rightPanel.add(btnPanel);
            }
            
            card.add(rightPanel, BorderLayout.EAST);
            return card;
        }
        
        private void refreshBookings() {
            JPanel content = (JPanel) ((JScrollPane) getComponent(1)).getViewport().getView();
            loadBookings(content);
        }
    }

    // ====== ADMIN ROOM PANEL ======
    private class AdminRoomPanel extends JPanel {
        private JPanel contentPanel;
        
        public AdminRoomPanel() {
            setLayout(new BorderLayout());
            setBackground(BG_COLOR);
            
            // Header
            JPanel header = new JPanel(new BorderLayout());
            header.setBackground(BG_COLOR);
            header.setBorder(new EmptyBorder(15, 20, 10, 20));
            
            JLabel titleLabel = new JLabel("Kelola Ruangan");
            titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
            titleLabel.setForeground(TEXT_DARK);
            header.add(titleLabel, BorderLayout.WEST);
            
            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
            btnPanel.setOpaque(false);
            
            JButton addBtn = new JButton("+ Tambah");
            addBtn.setFont(new Font("SansSerif", Font.BOLD, 11));
            addBtn.setBackground(SUCCESS_GREEN);
            addBtn.setForeground(Color.WHITE);
            addBtn.setBorderPainted(false);
            addBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            addBtn.addActionListener(e -> showAddRoomDialog());
            
            JButton refreshBtn = createRefreshButton(() -> refreshRooms());
            
            btnPanel.add(addBtn);
            btnPanel.add(refreshBtn);
            header.add(btnPanel, BorderLayout.EAST);
            
            add(header, BorderLayout.NORTH);
            
            // Content
            contentPanel = new JPanel(new GridLayout(0, 2, 12, 12));
            contentPanel.setOpaque(false);
            contentPanel.setBorder(new EmptyBorder(0, 20, 20, 20));
            
            loadRooms();
            
            JScrollPane scrollPane = new JScrollPane(contentPanel);
            scrollPane.setBorder(null);
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            
            add(scrollPane, BorderLayout.CENTER);
        }
        
        private void loadRooms() {
            contentPanel.removeAll();
            try {
                List<Room> rooms = roomControl.getAllRooms();
                for (Room room : rooms) {
                    contentPanel.add(createRoomCard(room));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            contentPanel.revalidate();
            contentPanel.repaint();
        }
        
        private JPanel createRoomCard(Room room) {
            JPanel card = new JPanel(new BorderLayout());
            card.setBackground(CARD_WHITE);
            card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                new EmptyBorder(12, 12, 12, 12)
            ));
            card.setPreferredSize(new Dimension(0, 100));
            
            JPanel info = new JPanel();
            info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
            info.setOpaque(false);
            
            JLabel nameLabel = new JLabel("🚪 " + room.getName());
            nameLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
            nameLabel.setForeground(TEXT_DARK);
            
            JLabel locLabel = new JLabel("📍 " + room.getLocation());
            locLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
            locLabel.setForeground(TEXT_MUTED);
            
            JLabel capLabel = new JLabel("👥 Kapasitas: " + room.getCapacity());
            capLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
            capLabel.setForeground(TEXT_MUTED);
            
            info.add(nameLabel);
            info.add(Box.createRigidArea(new Dimension(0, 4)));
            info.add(locLabel);
            info.add(capLabel);
            
            card.add(info, BorderLayout.CENTER);
            
            // Actions
            JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
            actions.setOpaque(false);
            
            JButton editBtn = new JButton("✏");
            editBtn.setFont(new Font("SansSerif", Font.PLAIN, 11));
            editBtn.setBackground(PRIMARY_BLUE);
            editBtn.setForeground(Color.WHITE);
            editBtn.setPreferredSize(new Dimension(30, 25));
            editBtn.setBorderPainted(false);
            editBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            editBtn.addActionListener(e -> showEditRoomDialog(room));
            
            JButton delBtn = new JButton("🗑");
            delBtn.setFont(new Font("SansSerif", Font.PLAIN, 11));
            delBtn.setBackground(DANGER_RED);
            delBtn.setForeground(Color.WHITE);
            delBtn.setPreferredSize(new Dimension(30, 25));
            delBtn.setBorderPainted(false);
            delBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            delBtn.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(this, 
                    "Hapus ruangan " + room.getName() + "?", 
                    "Konfirmasi", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    roomControl.deleteRoom(room.getId());
                    refreshRooms();
                }
            });
            
            actions.add(editBtn);
            actions.add(delBtn);
            card.add(actions, BorderLayout.SOUTH);
            
            return card;
        }
        
        private void showAddRoomDialog() {
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Tambah Ruangan", true);
            dialog.setSize(350, 250);
            dialog.setLocationRelativeTo(this);
            
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(new EmptyBorder(20, 20, 20, 20));
            
            JTextField nameField = new JTextField();
            JTextField locField = new JTextField();
            JTextField capField = new JTextField();
            
            panel.add(createFormField("Nama Ruangan", nameField));
            panel.add(Box.createRigidArea(new Dimension(0, 10)));
            panel.add(createFormField("Lokasi", locField));
            panel.add(Box.createRigidArea(new Dimension(0, 10)));
            panel.add(createFormField("Kapasitas", capField));
            panel.add(Box.createRigidArea(new Dimension(0, 20)));
            
            JButton saveBtn = new JButton("Simpan");
            saveBtn.setBackground(SUCCESS_GREEN);
            saveBtn.setForeground(Color.WHITE);
            saveBtn.setBorderPainted(false);
            saveBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            saveBtn.addActionListener(e -> {
                try {
                    String name = nameField.getText().trim();
                    String loc = locField.getText().trim();
                    int cap = Integer.parseInt(capField.getText().trim());
                    roomControl.addRoom(name, loc, cap);
                    dialog.dispose();
                    refreshRooms();
                    JOptionPane.showMessageDialog(this, "Ruangan berhasil ditambahkan!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            });
            panel.add(saveBtn);
            
            dialog.add(panel);
            dialog.setVisible(true);
        }
        
        private void showEditRoomDialog(Room room) {
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Edit Ruangan", true);
            dialog.setSize(350, 250);
            dialog.setLocationRelativeTo(this);
            
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(new EmptyBorder(20, 20, 20, 20));
            
            JTextField nameField = new JTextField(room.getName());
            JTextField locField = new JTextField(room.getLocation());
            JTextField capField = new JTextField(String.valueOf(room.getCapacity()));
            
            panel.add(createFormField("Nama Ruangan", nameField));
            panel.add(Box.createRigidArea(new Dimension(0, 10)));
            panel.add(createFormField("Lokasi", locField));
            panel.add(Box.createRigidArea(new Dimension(0, 10)));
            panel.add(createFormField("Kapasitas", capField));
            panel.add(Box.createRigidArea(new Dimension(0, 20)));
            
            JButton saveBtn = new JButton("Simpan");
            saveBtn.setBackground(PRIMARY_BLUE);
            saveBtn.setForeground(Color.WHITE);
            saveBtn.setBorderPainted(false);
            saveBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            saveBtn.addActionListener(e -> {
                try {
                    room.setName(nameField.getText().trim());
                    room.setLocation(locField.getText().trim());
                    room.setCapacity(Integer.parseInt(capField.getText().trim()));
                    roomControl.updateRoom(room);
                    dialog.dispose();
                    refreshRooms();
                    JOptionPane.showMessageDialog(this, "Ruangan berhasil diupdate!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            });
            panel.add(saveBtn);
            
            dialog.add(panel);
            dialog.setVisible(true);
        }
        
        private void refreshRooms() {
            loadRooms();
        }
    }

    // ====== ADMIN USER PANEL ======
    private class AdminUserPanel extends JPanel {
        private JPanel contentPanel;
        
        public AdminUserPanel() {
            setLayout(new BorderLayout());
            setBackground(BG_COLOR);
            
            // Header
            JPanel header = new JPanel(new BorderLayout());
            header.setBackground(BG_COLOR);
            header.setBorder(new EmptyBorder(15, 20, 10, 20));
            
            JLabel titleLabel = new JLabel("Kelola Pengguna");
            titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
            titleLabel.setForeground(TEXT_DARK);
            header.add(titleLabel, BorderLayout.WEST);
            
            JButton refreshBtn = createRefreshButton(() -> refreshUsers());
            header.add(refreshBtn, BorderLayout.EAST);
            
            add(header, BorderLayout.NORTH);
            
            // Content
            contentPanel = new JPanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setOpaque(false);
            contentPanel.setBorder(new EmptyBorder(0, 20, 20, 20));
            
            loadUsers();
            
            JScrollPane scrollPane = new JScrollPane(contentPanel);
            scrollPane.setBorder(null);
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            
            add(scrollPane, BorderLayout.CENTER);
        }
        
        private void loadUsers() {
            contentPanel.removeAll();
            try {
                List<CivitasAkademik> users = userControl.getAllUsers();
                if (users.isEmpty()) {
                    JLabel emptyLabel = new JLabel("Tidak ada pengguna");
                    emptyLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
                    emptyLabel.setForeground(TEXT_MUTED);
                    emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                    contentPanel.add(Box.createVerticalGlue());
                    contentPanel.add(emptyLabel);
                    contentPanel.add(Box.createVerticalGlue());
                } else {
                    for (CivitasAkademik user : users) {
                        contentPanel.add(createUserCard(user));
                        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            contentPanel.revalidate();
            contentPanel.repaint();
        }
        
        private JPanel createUserCard(CivitasAkademik user) {
            JPanel card = new JPanel(new BorderLayout(10, 0));
            card.setBackground(CARD_WHITE);
            card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                new EmptyBorder(12, 15, 12, 15)
            ));
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 85));
            
            // Avatar
            JLabel avatarLabel = new JLabel("👤");
            avatarLabel.setFont(new Font("SansSerif", Font.PLAIN, 28));
            avatarLabel.setPreferredSize(new Dimension(45, 45));
            card.add(avatarLabel, BorderLayout.WEST);
            
            // Info
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.setOpaque(false);
            
            JLabel nameLabel = new JLabel(user.getName());
            nameLabel.setFont(new Font("SansSerif", Font.BOLD, 13));
            nameLabel.setForeground(TEXT_DARK);
            
            JLabel nimLabel = new JLabel("NIM: " + user.getNim());
            nimLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
            nimLabel.setForeground(TEXT_MUTED);
            
            JLabel emailLabel = new JLabel("📧 " + user.getEmail());
            emailLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
            emailLabel.setForeground(TEXT_MUTED);
            
            infoPanel.add(nameLabel);
            infoPanel.add(nimLabel);
            infoPanel.add(emailLabel);
            
            card.add(infoPanel, BorderLayout.CENTER);
            
            // Status + Actions
            JPanel rightPanel = new JPanel();
            rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
            rightPanel.setOpaque(false);
            
            String status = user.getStatusAkun().name();
            Color statusColor = status.equals("AKTIF") ? SUCCESS_GREEN : DANGER_RED;
            JLabel statusLabel = new JLabel(user.getStatusAkun().toString());
            statusLabel.setFont(new Font("SansSerif", Font.BOLD, 10));
            statusLabel.setForeground(Color.WHITE);
            statusLabel.setOpaque(true);
            statusLabel.setBackground(statusColor);
            statusLabel.setBorder(new EmptyBorder(3, 8, 3, 8));
            statusLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
            
            rightPanel.add(statusLabel);
            rightPanel.add(Box.createRigidArea(new Dimension(0, 8)));
            
            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 3, 0));
            btnPanel.setOpaque(false);
            
            if (status.equals("AKTIF")) {
                JButton banBtn = new JButton("Ban");
                banBtn.setFont(new Font("SansSerif", Font.PLAIN, 10));
                banBtn.setBackground(DANGER_RED);
                banBtn.setForeground(Color.WHITE);
                banBtn.setPreferredSize(new Dimension(45, 24));
                banBtn.setBorderPainted(false);
                banBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                banBtn.addActionListener(e -> {
                    userControl.freezeUser(user.getId());
                    refreshUsers();
                });
                btnPanel.add(banBtn);
            } else {
                JButton unbanBtn = new JButton("Aktif");
                unbanBtn.setFont(new Font("SansSerif", Font.PLAIN, 10));
                unbanBtn.setBackground(SUCCESS_GREEN);
                unbanBtn.setForeground(Color.WHITE);
                unbanBtn.setPreferredSize(new Dimension(45, 24));
                unbanBtn.setBorderPainted(false);
                unbanBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                unbanBtn.addActionListener(e -> {
                    userControl.activateUser(user.getId());
                    refreshUsers();
                });
                btnPanel.add(unbanBtn);
            }
            
            rightPanel.add(btnPanel);
            card.add(rightPanel, BorderLayout.EAST);
            
            return card;
        }
        
        private void refreshUsers() {
            loadUsers();
        }
    }
    
    // ====== HELPER METHODS ======
    private JButton createRefreshButton(Runnable action) {
        JButton btn = new JButton("🔄");
        btn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btn.setBackground(new Color(241, 245, 249));
        btn.setForeground(TEXT_DARK);
        btn.setPreferredSize(new Dimension(35, 30));
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> action.run());
        return btn;
    }
    
    private JLabel createStatusBadge(String status) {
        JLabel badge = new JLabel(status);
        badge.setFont(new Font("SansSerif", Font.BOLD, 10));
        badge.setForeground(Color.WHITE);
        badge.setOpaque(true);
        badge.setBackground(getStatusColor(status));
        badge.setBorder(new EmptyBorder(3, 8, 3, 8));
        return badge;
    }
    
    private Color getStatusColor(String status) {
        switch (status) {
            case "PENDING": return WARNING_ORANGE;
            case "APPROVED": return SUCCESS_GREEN;
            case "REJECTED": return DANGER_RED;
            default: return TEXT_MUTED;
        }
    }
    
    private JPanel createFormField(String label, JTextField field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lbl.setForeground(TEXT_DARK);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(lbl);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(field);
        
        return panel;
    }
}
