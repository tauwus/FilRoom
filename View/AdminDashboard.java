package View;

import Controller.AdminBookingControl;
import Controller.AdminRoomControl;
import Controller.AdminUserControl;
import Controller.AuthControl;
import Controller.ReportControl;
import Model.Booking;
import Model.CivitasAkademik;
import Model.Room;
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
    private JPanel reportPanel;
    
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
        reportPanel = createReportPanel();
        contentArea.add(reportPanel, "Laporan");
        
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
        
        JButton menuBtn = new JButton("<html><span style='font-size:18px;'>&#9776;</span></html>");
        menuBtn.setForeground(Color.WHITE);
        menuBtn.setBackground(PRIMARY_BLUE);
        menuBtn.setBorderPainted(false);
        menuBtn.setFocusPainted(false);
        menuBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        menuBtn.setPreferredSize(new Dimension(50, 36));
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
        
        JLabel adminLabel = new JLabel("Admin");
        adminLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
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
        
        menuItems.add(createMenuItem("📈", "Laporan", () -> navigateTo("Laporan")));
        
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
    
    // Filter state
    private int selectedMonth = 0; // 0 = Semua
    private int selectedYear = java.time.Year.now().getValue();

    private JPanel createReportPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);
        
        // Header Container (Vertical Layout)
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(BG_COLOR);
        header.setBorder(new EmptyBorder(15, 20, 10, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Laporan & Statistik");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_DARK);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.add(titleLabel);
        
        header.add(Box.createRigidArea(new Dimension(0, 10))); // Spacing
        
        // Filter Controls
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        filterPanel.setOpaque(false);
        filterPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        String[] months = {"Semua Bulan", "Januari", "Februari", "Maret", "April", "Mei", "Juni", 
                           "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
        JComboBox<String> monthCombo = new JComboBox<>(months);
        monthCombo.setSelectedIndex(selectedMonth);
        monthCombo.addActionListener(e -> {
            selectedMonth = monthCombo.getSelectedIndex();
            refreshReportPanel();
        });
        
        // Year combo (current year - 2 to current year + 1)
        int currentYear = java.time.Year.now().getValue();
        Integer[] years = {currentYear - 2, currentYear - 1, currentYear, currentYear + 1};
        JComboBox<Integer> yearCombo = new JComboBox<>(years);
        yearCombo.setSelectedItem(selectedYear);
        yearCombo.addActionListener(e -> {
            selectedYear = (Integer) yearCombo.getSelectedItem();
            refreshReportPanel();
        });
        
        filterPanel.add(monthCombo);
        filterPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        filterPanel.add(yearCombo);
        
        header.add(filterPanel);
        
        panel.add(header, BorderLayout.NORTH);
        
        // Content ScrollPane
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(BG_COLOR);
        content.setBorder(new EmptyBorder(0, 20, 20, 20));
        
        // Load Data
        java.util.Map<Model.BookingStatus, Integer> bookingStats = reportControl.getBookingCountByStatus(selectedMonth, selectedYear);
        List<String[]> topRooms = reportControl.getMostBookedRooms(selectedMonth, selectedYear, 5);
        List<Object[]> dailyStats = reportControl.getDailyBookingStats(selectedMonth, selectedYear);
        int totalRooms = reportControl.getTotalActiveRooms();
        int totalUsers = reportControl.getTotalUsers();
        
        int menunggu = bookingStats.getOrDefault(Model.BookingStatus.MENUNGGU_PERSETUJUAN, 0);
        int disetujui = bookingStats.getOrDefault(Model.BookingStatus.DISETUJUI, 0);
        int ditolak = bookingStats.getOrDefault(Model.BookingStatus.DITOLAK, 0);
        int dibatalkan = bookingStats.getOrDefault(Model.BookingStatus.DIBATALKAN, 0);
        int selesai = bookingStats.getOrDefault(Model.BookingStatus.SELESAI, 0);
        int total = menunggu + disetujui + ditolak + dibatalkan + selesai;
        
        // 1. Summary Cards
        JPanel summaryPanel = new JPanel(new GridLayout(1, 5, 10, 0));
        summaryPanel.setOpaque(false);
        summaryPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        
        summaryPanel.add(createReportStatCard("Total Booking", String.valueOf(total), PRIMARY_BLUE));
        summaryPanel.add(createReportStatCard("Menunggu", String.valueOf(menunggu), WARNING_ORANGE));
        summaryPanel.add(createReportStatCard("Disetujui", String.valueOf(disetujui), SUCCESS_GREEN));
        summaryPanel.add(createReportStatCard("Ruangan", String.valueOf(totalRooms), new Color(139, 92, 246)));
        summaryPanel.add(createReportStatCard("Pengguna", String.valueOf(totalUsers), new Color(6, 182, 212)));
        
        content.add(summaryPanel);
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // 2. Charts Row
        JPanel chartsPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        chartsPanel.setOpaque(false);
        chartsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        
        // Left: Trend Chart
        chartsPanel.add(new SimpleBarChartPanel(dailyStats));
        
        // Right: Status Distribution
        chartsPanel.add(new StatusDistributionPanel(bookingStats));
        
        content.add(chartsPanel);
        content.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // 3. Top Rooms
        JPanel topRoomsPanel = new JPanel(new BorderLayout());
        topRoomsPanel.setBackground(Color.WHITE);
        topRoomsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel topRoomsTitle = new JLabel("🏆 Ruangan Populer");
        topRoomsTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        topRoomsTitle.setForeground(TEXT_DARK);
        topRoomsPanel.add(topRoomsTitle, BorderLayout.NORTH);
        
        JPanel roomsList = new JPanel();
        roomsList.setLayout(new BoxLayout(roomsList, BoxLayout.Y_AXIS));
        roomsList.setOpaque(false);
        roomsList.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        if (topRooms.isEmpty()) {
            JLabel emptyLabel = new JLabel("Belum ada data");
            emptyLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
            emptyLabel.setForeground(TEXT_MUTED);
            emptyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            roomsList.add(emptyLabel);
        } else {
            int maxCount = 0;
            for (String[] r : topRooms) {
                int c = Integer.parseInt(r[1]);
                if (c > maxCount) maxCount = c;
            }
            
            for (int i = 0; i < topRooms.size(); i++) {
                String[] r = topRooms.get(i);
                String name = r[0];
                int count = Integer.parseInt(r[1]);
                int pct = maxCount > 0 ? (count * 100) / maxCount : 0;
                
                JPanel roomRow = new JPanel(new BorderLayout(10, 0));
                roomRow.setOpaque(false);
                roomRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
                roomRow.setBorder(new EmptyBorder(5, 0, 5, 0));
                
                JLabel rankLbl = new JLabel((i + 1) + ".");
                rankLbl.setPreferredSize(new Dimension(25, 20));
                rankLbl.setFont(new Font("SansSerif", Font.BOLD, 12));
                
                JLabel nameLbl = new JLabel(name);
                nameLbl.setPreferredSize(new Dimension(150, 20));
                nameLbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
                
                JProgressBar bar = new JProgressBar(0, 100);
                bar.setValue(pct);
                bar.setString(count + "x");
                bar.setStringPainted(true);
                bar.setForeground(PRIMARY_BLUE);
                bar.setBackground(new Color(241, 245, 249));
                bar.setBorderPainted(false);
                
                // Custom UI to change bar color
                bar.setUI(new javax.swing.plaf.basic.BasicProgressBarUI() {
                    protected Color getSelectionBackground() { return Color.BLACK; }
                    protected Color getSelectionForeground() { return Color.WHITE; }
                });
                
                roomRow.add(rankLbl, BorderLayout.WEST);
                roomRow.add(nameLbl, BorderLayout.CENTER);
                roomRow.add(bar, BorderLayout.EAST);
                
                roomsList.add(roomRow);
            }
        }
        topRoomsPanel.add(roomsList, BorderLayout.CENTER);
        
        content.add(topRoomsPanel);
        content.add(Box.createVerticalGlue());
        
        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createReportStatCard(String label, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
            new EmptyBorder(10, 15, 10, 15)
        ));
        
        JLabel valLabel = new JLabel(value);
        valLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        valLabel.setForeground(color);
        valLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel txtLabel = new JLabel(label);
        txtLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        txtLabel.setForeground(TEXT_MUTED);
        txtLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        card.add(valLabel, BorderLayout.CENTER);
        card.add(txtLabel, BorderLayout.SOUTH);
        
        return card;
    }
    
    private void refreshReportPanel() {
        contentArea.remove(reportPanel);
        reportPanel = createReportPanel();
        contentArea.add(reportPanel, "Laporan");
        contentArea.revalidate();
        contentArea.repaint();
        if ("Laporan".equals(activePage)) {
            cardLayout.show(contentArea, "Laporan");
        }
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
                if (b.getStatus().name().equals("MENUNGGU_PERSETUJUAN")) pendingCount++;
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
        private JComboBox<String> filterComboBox;

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
            
            // Filter Dropdown
            String[] filters = {"Semua", "Menunggu", "Disetujui", "Ditolak", "Dibatalkan", "Selesai"};
            filterComboBox = new JComboBox<>(filters);
            filterComboBox.setFont(new Font("SansSerif", Font.PLAIN, 12));
            filterComboBox.setBackground(Color.WHITE);
            filterComboBox.setFocusable(false);
            filterComboBox.addActionListener(e -> refreshBookings());
            
            header.add(filterComboBox, BorderLayout.EAST);
            
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
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            
            add(scrollPane, BorderLayout.CENTER);
        }
        
        private void loadBookings(JPanel container) {
            container.removeAll();
            try {
                List<Booking> bookings = bookingControl.getAllBookings();
                
                // Filter
                String selectedFilter = (String) filterComboBox.getSelectedItem();
                List<Booking> filteredBookings = new java.util.ArrayList<>();
                
                for (Booking b : bookings) {
                    if ("Semua".equals(selectedFilter)) {
                        filteredBookings.add(b);
                    } else {
                        String status = b.getStatus().name();
                        if (selectedFilter.equals("Menunggu") && status.equals("MENUNGGU_PERSETUJUAN")) filteredBookings.add(b);
                        else if (selectedFilter.equals("Disetujui") && status.equals("DISETUJUI")) filteredBookings.add(b);
                        else if (selectedFilter.equals("Ditolak") && status.equals("DITOLAK")) filteredBookings.add(b);
                        else if (selectedFilter.equals("Dibatalkan") && status.equals("DIBATALKAN")) filteredBookings.add(b);
                        else if (selectedFilter.equals("Selesai") && status.equals("SELESAI")) filteredBookings.add(b);
                    }
                }
                
                // Sort: Menunggu first, then by date descending
                filteredBookings.sort((b1, b2) -> {
                    boolean b1Pending = b1.getStatus().name().equals("MENUNGGU_PERSETUJUAN");
                    boolean b2Pending = b2.getStatus().name().equals("MENUNGGU_PERSETUJUAN");
                    
                    if (b1Pending && !b2Pending) return -1;
                    if (!b1Pending && b2Pending) return 1;
                    
                    // Secondary sort by date (newest first)
                    return b2.getSubmissionDate().compareTo(b1.getSubmissionDate());
                });

                if (filteredBookings.isEmpty()) {
                    JLabel emptyLabel = new JLabel("Tidak ada booking");
                    emptyLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
                    emptyLabel.setForeground(TEXT_MUTED);
                    emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                    container.add(Box.createVerticalGlue());
                    container.add(emptyLabel);
                    container.add(Box.createVerticalGlue());
                } else {
                    for (Booking booking : filteredBookings) {
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
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 145));
            
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
            
            // Right: Status
            JPanel rightPanel = new JPanel();
            rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
            rightPanel.setOpaque(false);
            
            String status = booking.getStatus().name();
            String statusDisplay = status.equals("MENUNGGU_PERSETUJUAN") ? "MENUNGGU" : status;
            JLabel statusBadge = createStatusBadge(status, statusDisplay);
            statusBadge.setAlignmentX(Component.RIGHT_ALIGNMENT);
            rightPanel.add(statusBadge);
            rightPanel.add(Box.createVerticalGlue());
            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
            btnPanel.setOpaque(false);
            btnPanel.setBorder(new EmptyBorder(8, 0, 0, 0));
            
            // Detail Button
            JButton detailBtn = new JButton("Detail");
            detailBtn.setFont(new Font("SansSerif", Font.BOLD, 11));
            detailBtn.setBackground(PRIMARY_BLUE);
            detailBtn.setForeground(Color.WHITE);
            detailBtn.setMargin(new Insets(5, 10, 5, 10));
            detailBtn.setBorderPainted(false);
            detailBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            detailBtn.setToolTipText("Detail Booking");
            detailBtn.addActionListener(e -> {
                StringBuilder details = new StringBuilder();
                details.append("Detail Booking:\n");
                details.append("ID: ").append(booking.getId()).append("\n");
                details.append("User: ").append(booking.getUser() != null ? booking.getUser().getName() : "Unknown").append("\n");
                details.append("Tanggal: ").append(booking.getSubmissionDate()).append("\n");
                details.append("Status: ").append(booking.getStatus()).append("\n");
                details.append("Deskripsi: ").append(booking.getActivityDescription()).append("\n");
                
                if (booking.getBookingItem() != null) {
                    details.append("\nItem:\n");
                    Model.BookingItem item = booking.getBookingItem();
                    details.append("- ").append(item.getRoom().getName())
                           .append(" (").append(item.getStartTime()).append(" - ").append(item.getEndTime()).append(")\n");
                }
                
                JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), 
                    details.toString(),
                    "Detail Booking",
                    JOptionPane.INFORMATION_MESSAGE);
            });
            btnPanel.add(detailBtn);
            
            if (status.equals("MENUNGGU_PERSETUJUAN")) {
                JButton approveBtn = new JButton("Approve");
                approveBtn.setFont(new Font("SansSerif", Font.BOLD, 11));
                approveBtn.setBackground(SUCCESS_GREEN);
                approveBtn.setForeground(Color.WHITE);
                approveBtn.setMargin(new Insets(5, 10, 5, 10));
                approveBtn.setBorderPainted(false);
                approveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                approveBtn.addActionListener(e -> {
                    bookingControl.approveBooking(booking.getId());
                    refreshBookings();
                });
                
                JButton rejectBtn = new JButton("Reject");
                rejectBtn.setFont(new Font("SansSerif", Font.BOLD, 11));
                rejectBtn.setBackground(DANGER_RED);
                rejectBtn.setForeground(Color.WHITE);
                rejectBtn.setMargin(new Insets(5, 10, 5, 10));
                rejectBtn.setBorderPainted(false);
                rejectBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                rejectBtn.addActionListener(e -> {
                    bookingControl.rejectBooking(booking.getId());
                    refreshBookings();
                });
                
                btnPanel.add(approveBtn);
                btnPanel.add(rejectBtn);
            }
            card.add(rightPanel, BorderLayout.EAST);
            card.add(btnPanel, BorderLayout.SOUTH);
            return card;
        }
        
        private void refreshBookings() {
            JPanel content = (JPanel) ((JScrollPane) getComponent(1)).getViewport().getView();
            loadBookings(content);
        }
    }

    // ====== ADMIN ROOM PANEL ======
    private class AdminRoomPanel extends JPanel {
        private CardLayout roomCardLayout;
        private JPanel roomContentArea;
        private JPanel listPanel;
        private JPanel formPanel;
        
        // Form fields
        private JTextField nameField;
        private JTextField locField;
        private JTextField capField;
        private JButton saveBtn;
        private JLabel formTitle;
        private Room currentEditingRoom = null; // null means adding new room
        
        private JPanel contentPanel; // Grid for room cards
        
        public AdminRoomPanel() {
            setLayout(new BorderLayout());
            setBackground(BG_COLOR);
            
            roomCardLayout = new CardLayout();
            roomContentArea = new JPanel(roomCardLayout);
            roomContentArea.setOpaque(false);
            
            // 1. List Panel
            listPanel = createListPanel();
            
            // 2. Form Panel
            formPanel = createFormPanel();
            
            roomContentArea.add(listPanel, "List");
            roomContentArea.add(formPanel, "Form");
            
            add(roomContentArea, BorderLayout.CENTER);
        }
        
        private JPanel createListPanel() {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setOpaque(false);
            
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
            addBtn.addActionListener(e -> showAddRoomForm());
            
            JButton refreshBtn = createRefreshButton(() -> refreshRooms());
            
            btnPanel.add(addBtn);
            btnPanel.add(refreshBtn);
            header.add(btnPanel, BorderLayout.EAST);
            
            panel.add(header, BorderLayout.NORTH);
            
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
            
            panel.add(scrollPane, BorderLayout.CENTER);
            
            return panel;
        }
        
        private JPanel createFormPanel() {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(BG_COLOR);
            
            // Header
            JPanel header = new JPanel(new BorderLayout());
            header.setBackground(BG_COLOR);
            header.setBorder(new EmptyBorder(15, 20, 10, 20));
            
            formTitle = new JLabel("Tambah Ruangan");
            formTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
            formTitle.setForeground(TEXT_DARK);
            header.add(formTitle, BorderLayout.WEST);
            
            JButton backBtn = new JButton("← Kembali");
            backBtn.setFont(new Font("SansSerif", Font.PLAIN, 12));
            backBtn.setBackground(Color.WHITE);
            backBtn.setForeground(TEXT_DARK);
            backBtn.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240)));
            backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            backBtn.addActionListener(e -> roomCardLayout.show(roomContentArea, "List"));
            header.add(backBtn, BorderLayout.EAST);
            
            panel.add(header, BorderLayout.NORTH);
            
            // Form Content
            JPanel formContent = new JPanel();
            formContent.setLayout(new BoxLayout(formContent, BoxLayout.Y_AXIS));
            formContent.setBackground(CARD_WHITE);
            formContent.setBorder(new EmptyBorder(30, 30, 30, 30));
            // formContent.setMaximumSize(new Dimension(600, 400)); // Removed max size to let it fill if needed, but wrapper handles it
            
            // Wrapper to center the form
            JPanel centerWrapper = new JPanel(new GridBagLayout());
            centerWrapper.setOpaque(false);
            centerWrapper.add(formContent);
            
            nameField = new JTextField(20);
            locField = new JTextField(20);
            capField = new JTextField(20);
            
            formContent.add(createFormField("Nama Ruangan", nameField));
            formContent.add(Box.createRigidArea(new Dimension(0, 15)));
            formContent.add(createFormField("Lokasi", locField));
            formContent.add(Box.createRigidArea(new Dimension(0, 15)));
            formContent.add(createFormField("Kapasitas", capField));
            formContent.add(Box.createRigidArea(new Dimension(0, 30)));
            
            saveBtn = new JButton("Simpan");
            saveBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
            saveBtn.setForeground(Color.WHITE);
            saveBtn.setBackground(PRIMARY_BLUE);
            saveBtn.setBorderPainted(false);
            saveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            saveBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            saveBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
            saveBtn.addActionListener(e -> saveRoom());
            
            formContent.add(saveBtn);
            
            panel.add(centerWrapper, BorderLayout.CENTER);
            
            return panel;
        }
        
        private void showAddRoomForm() {
            currentEditingRoom = null;
            formTitle.setText("Tambah Ruangan");
            nameField.setText("");
            locField.setText("");
            capField.setText("");
            saveBtn.setBackground(SUCCESS_GREEN);
            roomCardLayout.show(roomContentArea, "Form");
        }
        
        private void showEditRoomForm(Room room) {
            currentEditingRoom = room;
            formTitle.setText("Edit Ruangan");
            nameField.setText(room.getName());
            locField.setText(room.getLocation());
            capField.setText(String.valueOf(room.getCapacity()));
            saveBtn.setBackground(PRIMARY_BLUE);
            roomCardLayout.show(roomContentArea, "Form");
        }
        
        private void saveRoom() {
            try {
                String name = nameField.getText().trim();
                String loc = locField.getText().trim();
                String capStr = capField.getText().trim();
                
                if (name.isEmpty() || loc.isEmpty() || capStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                int cap = Integer.parseInt(capStr);
                
                if (currentEditingRoom == null) {
                    // Add
                    if (roomControl.addRoom(name, loc, cap)) {
                        JOptionPane.showMessageDialog(this, "Ruangan berhasil ditambahkan!");
                        refreshRooms();
                        roomCardLayout.show(roomContentArea, "List");
                    } else {
                        JOptionPane.showMessageDialog(this, "Gagal menambahkan ruangan", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    // Edit
                    currentEditingRoom.setName(name);
                    currentEditingRoom.setLocation(loc);
                    currentEditingRoom.setCapacity(cap);
                    if (roomControl.updateRoom(currentEditingRoom)) {
                        JOptionPane.showMessageDialog(this, "Ruangan berhasil diupdate!");
                        refreshRooms();
                        roomCardLayout.show(roomContentArea, "List");
                    } else {
                        JOptionPane.showMessageDialog(this, "Gagal mengupdate ruangan", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Kapasitas harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
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
            card.setPreferredSize(new Dimension(0, 130));
            
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
            
            JButton editBtn = new JButton("<html><b>✎</b></html>");
            editBtn.setFont(new Font("SansSerif", Font.PLAIN, 14));
            editBtn.setBackground(PRIMARY_BLUE);
            editBtn.setForeground(Color.WHITE);
            editBtn.setPreferredSize(new Dimension(36, 28));
            editBtn.setBorderPainted(false);
            editBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            editBtn.addActionListener(e -> showEditRoomForm(room));
            
            JButton delBtn = new JButton("<html><b>✕</b></html>");
            delBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
            delBtn.setBackground(DANGER_RED);
            delBtn.setForeground(Color.WHITE);
            delBtn.setPreferredSize(new Dimension(36, 28));
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
                banBtn.setPreferredSize(new Dimension(80, 24));
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
                unbanBtn.setPreferredSize(new Dimension(80, 24));
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
        JButton btn = new JButton("↻");
        btn.setFont(new Font("SansSerif", Font.BOLD, 18));
        btn.setBackground(new Color(241, 245, 249));
        btn.setForeground(TEXT_DARK);
        btn.setPreferredSize(new Dimension(35, 30));
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> action.run());
        return btn;
    }
    
    private JLabel createStatusBadge(String status, String displayText) {
        JLabel badge = new JLabel(displayText);
        badge.setFont(new Font("SansSerif", Font.BOLD, 10));
        badge.setForeground(Color.WHITE);
        badge.setOpaque(true);
        badge.setBackground(getStatusColor(status));
        badge.setBorder(new EmptyBorder(3, 8, 3, 8));
        return badge;
    }

    private JLabel createStatusBadge(String status) {
        return createStatusBadge(status, status);
    }
    
    private Color getStatusColor(String status) {
        switch (status) {
            case "MENUNGGU_PERSETUJUAN": return WARNING_ORANGE;
            case "DISETUJUI": return SUCCESS_GREEN;
            case "DITOLAK": return DANGER_RED;
            case "DIBATALKAN": return TEXT_MUTED;
            case "SELESAI": return PRIMARY_BLUE;
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
    
    // ====== CUSTOM CHART COMPONENTS ======
    
    private class SimpleBarChartPanel extends JPanel {
        private List<Object[]> data;
        private int maxValue;
        
        public SimpleBarChartPanel(List<Object[]> data) {
            this.data = data;
            this.maxValue = 0;
            for (Object[] item : data) {
                int val = (Integer) item[1];
                if (val > maxValue) maxValue = val;
            }
            if (maxValue == 0) maxValue = 1;
            
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                new EmptyBorder(20, 20, 20, 20)
            ));
            setPreferredSize(new Dimension(0, 250));
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int w = getWidth();
            int h = getHeight();
            int padding = 40;
            int chartW = w - 2 * padding;
            int chartH = h - 2 * padding;
            
            // Draw title
            g2.setColor(TEXT_DARK);
            g2.setFont(new Font("SansSerif", Font.BOLD, 14));
            g2.drawString("Tren Booking", padding, padding - 15);
            
            if (data == null || data.isEmpty()) {
                g2.setColor(TEXT_MUTED);
                g2.setFont(new Font("SansSerif", Font.ITALIC, 12));
                String msg = "Belum ada data booking untuk periode ini";
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(msg, (w - fm.stringWidth(msg)) / 2, h / 2);
                return;
            }
            
            // Draw axes
            g2.setColor(new Color(203, 213, 225));
            g2.drawLine(padding, h - padding, w - padding, h - padding); // X axis
            
            int barWidth = Math.min(40, Math.max(10, chartW / data.size() - 10));
            int spacing = (chartW - (barWidth * data.size())) / (data.size() + 1);
            
            for (int i = 0; i < data.size(); i++) {
                Object[] item = data.get(i);
                int val = (Integer) item[1];
                int barHeight = (int) ((double) val / maxValue * (chartH - 20));
                if (val > 0 && barHeight < 2) barHeight = 2;
                
                int x = padding + spacing + i * (barWidth + spacing);
                int y = h - padding - barHeight;
                
                // Bar
                g2.setColor(PRIMARY_BLUE);
                g2.fillRoundRect(x, y, barWidth, barHeight, 4, 4);
                
                // Value label
                if (val > 0) {
                    g2.setColor(TEXT_DARK);
                    g2.setFont(new Font("SansSerif", Font.BOLD, 10));
                    String valStr = String.valueOf(val);
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(valStr, x + (barWidth - fm.stringWidth(valStr)) / 2, y - 5);
                }
                
                // Date label
                g2.setColor(TEXT_MUTED);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 9));
                String dateStr = (String) item[0];
                try {
                    String[] parts = dateStr.split("-");
                    if (parts.length >= 3) dateStr = parts[2] + "/" + parts[1];
                } catch (Exception e) {}
                
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(dateStr, x + (barWidth - fm.stringWidth(dateStr)) / 2, h - padding + 15);
            }
        }
    }
    
    private class StatusDistributionPanel extends JPanel {
        private java.util.Map<Model.BookingStatus, Integer> stats;
        private int total;
        
        public StatusDistributionPanel(java.util.Map<Model.BookingStatus, Integer> stats) {
            this.stats = stats;
            this.total = stats.values().stream().mapToInt(Integer::intValue).sum();
            if (total == 0) total = 1;
            
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                new EmptyBorder(15, 15, 15, 15)
            ));
            
            JLabel title = new JLabel("Statistik per Status");
            title.setFont(new Font("SansSerif", Font.BOLD, 14));
            title.setForeground(TEXT_DARK);
            title.setAlignmentX(Component.LEFT_ALIGNMENT);
            add(title);
            add(Box.createRigidArea(new Dimension(0, 15)));
            
            addStatusRow("Menunggu", Model.BookingStatus.MENUNGGU_PERSETUJUAN, WARNING_ORANGE);
            add(Box.createRigidArea(new Dimension(0, 10)));
            addStatusRow("Disetujui", Model.BookingStatus.DISETUJUI, SUCCESS_GREEN);
            add(Box.createRigidArea(new Dimension(0, 10)));
            addStatusRow("Ditolak", Model.BookingStatus.DITOLAK, DANGER_RED);
            add(Box.createRigidArea(new Dimension(0, 10)));
            addStatusRow("Selesai", Model.BookingStatus.SELESAI, PRIMARY_BLUE);
            add(Box.createRigidArea(new Dimension(0, 10)));
            addStatusRow("Dibatalkan", Model.BookingStatus.DIBATALKAN, TEXT_MUTED);
        }
        
        private void addStatusRow(String label, Model.BookingStatus status, Color color) {
            int count = stats.getOrDefault(status, 0);
            int pct = (count * 100) / total;
            
            JPanel row = new JPanel(new BorderLayout(10, 0));
            row.setOpaque(false);
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
            
            JLabel lbl = new JLabel(label);
            lbl.setFont(new Font("SansSerif", Font.PLAIN, 12));
            lbl.setPreferredSize(new Dimension(80, 20));
            row.add(lbl, BorderLayout.WEST);
            
            JProgressBar progressBar = new JProgressBar(0, 100);
            progressBar.setValue(pct);
            progressBar.setString(count + " (" + pct + "%)");
            progressBar.setStringPainted(true);
            progressBar.setForeground(color);
            progressBar.setBackground(new Color(241, 245, 249));
            progressBar.setBorderPainted(false);
            progressBar.setFont(new Font("SansSerif", Font.BOLD, 10));
            
            // Custom UI to change bar color
            progressBar.setUI(new javax.swing.plaf.basic.BasicProgressBarUI() {
                protected Color getSelectionBackground() { return Color.BLACK; }
                protected Color getSelectionForeground() { return Color.WHITE; }
            });
            
            row.add(progressBar, BorderLayout.CENTER);
            add(row);
        }
    }
}
