package View;

import Controller.AuthControl;
import Controller.BookingControl;
import Model.User;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.util.List;

public class HistoryPage extends JPanel {
    private MainFrame mainFrame;
    private BookingControl bookingControl;
    private JPanel listPanel;
    private JComboBox<String> statusFilter;
    private JComboBox<String> sortFilter;
    private JLabel confirmedCountLabel;
    private JLabel pendingCountLabel;

    // Colors
    private static final Color BG_COLOR = new Color(225, 255, 255);
    private static final Color PRIMARY_BLUE = new Color(30, 60, 120);

    public HistoryPage(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.bookingControl = new BookingControl();
        
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);
        
        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY_BLUE);
        header.setBorder(new EmptyBorder(20, 15, 20, 15));
        
        JLabel titleLabel = new JLabel("Aktivitas Peminjaman");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        header.add(titleLabel, BorderLayout.CENTER);
        
        add(header, BorderLayout.NORTH);
        
        // Content wrapper
        JPanel contentWrapper = new JPanel();
        contentWrapper.setLayout(new BoxLayout(contentWrapper, BoxLayout.Y_AXIS));
        contentWrapper.setBackground(BG_COLOR);
        contentWrapper.setBorder(new EmptyBorder(15, 15, 20, 15));
        
        // Stats Panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 2, 12, 0));
        statsPanel.setBackground(BG_COLOR);
        statsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        
        // Confirmed stat
        JPanel confirmedCard = createStatCard("Dikonfirmasi", "0", new Color(46, 125, 50));
        confirmedCountLabel = (JLabel) ((JPanel) confirmedCard.getComponent(0)).getComponent(0);
        statsPanel.add(confirmedCard);
        
        // Pending stat
        JPanel pendingCard = createStatCard("Menunggu", "0", new Color(255, 152, 0));
        pendingCountLabel = (JLabel) ((JPanel) pendingCard.getComponent(0)).getComponent(0);
        statsPanel.add(pendingCard);
        
        contentWrapper.add(statsPanel);
        contentWrapper.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Filters
        JPanel filterPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        filterPanel.setBackground(BG_COLOR);
        filterPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        
        statusFilter = new JComboBox<>(new String[]{"Semua", "Menunggu", "Disetujui", "Ditolak", "Selesai"});
        statusFilter.setFont(new Font("SansSerif", Font.PLAIN, 13));
        statusFilter.setBorder(new LineBorder(new Color(220, 220, 220), 1, true));
        statusFilter.addActionListener(e -> loadBookings());
        
        sortFilter = new JComboBox<>(new String[]{"Terbaru", "Terlama"});
        sortFilter.setFont(new Font("SansSerif", Font.PLAIN, 13));
        sortFilter.setBorder(new LineBorder(new Color(220, 220, 220), 1, true));
        sortFilter.addActionListener(e -> loadBookings());
        
        filterPanel.add(statusFilter);
        filterPanel.add(sortFilter);
        
        contentWrapper.add(filterPanel);
        contentWrapper.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // List Panel
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(BG_COLOR);
        contentWrapper.add(listPanel);
        
        // Scroll Pane
        JScrollPane scrollPane = new JScrollPane(contentWrapper);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);
        
        // Bottom Navigation
        add(new BottomNavPanel(mainFrame, "Aktivitas"), BorderLayout.SOUTH);
        
        // Refresh when shown
        addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                loadBookings();
            }
            @Override
            public void ancestorRemoved(AncestorEvent event) {}
            @Override
            public void ancestorMoved(AncestorEvent event) {}
        });
    }
    
    private JPanel createStatCard(String title, String count, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1, true),
            new EmptyBorder(12, 15, 12, 15)
        ));
        
        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
        innerPanel.setBackground(Color.WHITE);
        
        JLabel countLabel = new JLabel(count);
        countLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        countLabel.setForeground(color);
        countLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        titleLabel.setForeground(Color.GRAY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        innerPanel.add(countLabel);
        innerPanel.add(titleLabel);
        
        card.add(innerPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private void loadBookings() {
        listPanel.removeAll();
        
        User user = AuthControl.getCurrentUser();
        if (user == null) {
            JLabel noUser = new JLabel("Silakan login terlebih dahulu");
            noUser.setFont(new Font("SansSerif", Font.PLAIN, 14));
            noUser.setAlignmentX(Component.CENTER_ALIGNMENT);
            listPanel.add(noUser);
            listPanel.revalidate();
            listPanel.repaint();
            return;
        }
        
        // Get stats - returns [Total, Approved, Pending, Completed]
        int[] stats = bookingControl.getBookingStatistics(user.getId());
        confirmedCountLabel.setText(String.valueOf(stats[1])); // Approved
        pendingCountLabel.setText(String.valueOf(stats[2])); // Pending
        
        // Get filtered bookings - returns [roomName, date, start, end, purpose, status]
        String filter = (String) statusFilter.getSelectedItem();
        List<String[]> bookings = bookingControl.getUserBookingsFiltered(user.getId(), filter);
        
        // Sort based on selection (data already sorted DESC from DB, reverse for ASC)
        if (sortFilter.getSelectedIndex() == 1) { // Terlama
            java.util.Collections.reverse(bookings);
        }
        
        if (bookings.isEmpty()) {
            JLabel emptyLabel = new JLabel("Belum ada riwayat peminjaman");
            emptyLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            listPanel.add(Box.createRigidArea(new Dimension(0, 50)));
            listPanel.add(emptyLabel);
        } else {
            for (String[] booking : bookings) {
                // booking = [roomName, date, start, end, purpose, status, bookingId]
                String bookingId = booking.length > 6 ? booking[6] : "-1";
                listPanel.add(createBookingCard(booking[0], booking[1], booking[2], booking[3], booking[4], booking[5], bookingId));
                listPanel.add(Box.createRigidArea(new Dimension(0, 12)));
            }
        }
        
        listPanel.revalidate();
        listPanel.repaint();
    }
    
    private JPanel createBookingCard(String roomName, String date, String startTime, String endTime, String purpose, String status, String bookingId) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1, true),
            new EmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        
        // Top Row: Room Name + Status Badge
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setBackground(Color.WHITE);
        
        JLabel roomLabel = new JLabel(roomName);
        roomLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        roomLabel.setForeground(PRIMARY_BLUE);
        topRow.add(roomLabel, BorderLayout.WEST);
        
        JLabel statusBadge = createStatusBadge(status);
        topRow.add(statusBadge, BorderLayout.EAST);
        
        card.add(topRow, BorderLayout.NORTH);
        
        // Center: Date and Time info
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        JLabel dateLabel = new JLabel("📅 " + date);
        dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        dateLabel.setForeground(new Color(80, 80, 80));
        
        JLabel timeLabel = new JLabel("🕐 " + startTime + " - " + endTime);
        timeLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        timeLabel.setForeground(new Color(80, 80, 80));
        
        centerPanel.add(dateLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        centerPanel.add(timeLabel);
        
        card.add(centerPanel, BorderLayout.CENTER);
        
        // Bottom: Action Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton detailBtn = new JButton("Lihat Detail");
        detailBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
        detailBtn.setForeground(PRIMARY_BLUE);
        detailBtn.setBackground(Color.WHITE);
        detailBtn.setBorder(new LineBorder(PRIMARY_BLUE, 1, true));
        detailBtn.setFocusPainted(false);
        detailBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        detailBtn.addActionListener(e -> showDetailPopup(roomName, date, startTime, endTime, purpose, status));
        
        buttonPanel.add(detailBtn);
        
        // Show cancel button only for pending/approved bookings
        String statusLower = status.toLowerCase();
        if (statusLower.contains("menunggu") || statusLower.contains("disetujui")) {
            JButton cancelBtn = new JButton("Batalkan");
            cancelBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
            cancelBtn.setForeground(Color.WHITE);
            cancelBtn.setBackground(new Color(200, 60, 60));
            cancelBtn.setBorderPainted(false);
            cancelBtn.setFocusPainted(false);
            cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            cancelBtn.addActionListener(e -> showCancelConfirmation(roomName, bookingId));
            buttonPanel.add(cancelBtn);
        } else {
            buttonPanel.add(new JLabel()); // Empty placeholder
        }
        
        card.add(buttonPanel, BorderLayout.SOUTH);
        
        return card;
    }
    
    private JLabel createStatusBadge(String status) {
        JLabel badge = new JLabel();
        badge.setOpaque(true);
        badge.setFont(new Font("SansSerif", Font.BOLD, 11));
        badge.setBorder(new EmptyBorder(4, 10, 4, 10));
        
        String statusLower = status.toLowerCase();
        if (statusLower.contains("menunggu")) {
            badge.setText("Menunggu");
            badge.setBackground(new Color(255, 243, 224));
            badge.setForeground(new Color(230, 126, 34));
        } else if (statusLower.contains("disetujui")) {
            badge.setText("Dikonfirmasi");
            badge.setBackground(new Color(232, 245, 233));
            badge.setForeground(new Color(46, 125, 50));
        } else if (statusLower.contains("ditolak")) {
            badge.setText("Ditolak");
            badge.setBackground(new Color(255, 235, 238));
            badge.setForeground(new Color(198, 40, 40));
        } else if (statusLower.contains("dibatalkan")) {
            badge.setText("Dibatalkan");
            badge.setBackground(new Color(245, 245, 245));
            badge.setForeground(new Color(117, 117, 117));
        } else if (statusLower.contains("selesai")) {
            badge.setText("Selesai");
            badge.setBackground(new Color(227, 242, 253));
            badge.setForeground(new Color(25, 118, 210));
        } else {
            badge.setText(status);
            badge.setBackground(Color.LIGHT_GRAY);
            badge.setForeground(Color.BLACK);
        }
        
        return badge;
    }
    
    private void showDetailPopup(String roomName, String date, String startTime, String endTime, String purpose, String status) {
        // Create custom dialog
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), true);
        dialog.setUndecorated(true);
        dialog.setBackground(new Color(0, 0, 0, 0));
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(0, 0, 0, 0)
        ));
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_BLUE);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel headerTitle = new JLabel("Detail Peminjaman");
        headerTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        headerTitle.setForeground(Color.WHITE);
        headerPanel.add(headerTitle, BorderLayout.WEST);
        
        JButton closeBtn = new JButton("✕");
        closeBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setBorderPainted(false);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setFocusPainted(false);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> dialog.dispose());
        headerPanel.add(closeBtn, BorderLayout.EAST);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        addDetailRow(contentPanel, "Ruangan", roomName);
        addDetailRow(contentPanel, "Tanggal", date);
        addDetailRow(contentPanel, "Waktu", startTime + " - " + endTime);
        addDetailRow(contentPanel, "Keperluan", purpose);
        addDetailRow(contentPanel, "Status", getStatusText(status));
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Footer
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setBorder(new EmptyBorder(10, 20, 20, 20));
        
        JButton okBtn = new JButton("Tutup");
        okBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        okBtn.setForeground(Color.WHITE);
        okBtn.setBackground(PRIMARY_BLUE);
        okBtn.setBorderPainted(false);
        okBtn.setFocusPainted(false);
        okBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        okBtn.setPreferredSize(new Dimension(120, 38));
        okBtn.addActionListener(e -> dialog.dispose());
        footerPanel.add(okBtn);
        
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        dialog.setContentPane(mainPanel);
        dialog.pack();
        dialog.setSize(350, dialog.getHeight());
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private void addDetailRow(JPanel panel, String label, String value) {
        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(Color.WHITE);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("SansSerif", Font.BOLD, 12));
        labelComp.setForeground(Color.GRAY);
        labelComp.setPreferredSize(new Dimension(80, 25));
        
        JLabel valueComp = new JLabel(value != null ? value : "-");
        valueComp.setFont(new Font("SansSerif", Font.PLAIN, 12));
        valueComp.setForeground(Color.BLACK);
        
        row.add(labelComp, BorderLayout.WEST);
        row.add(valueComp, BorderLayout.CENTER);
        
        panel.add(row);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
    }
    
    private String getStatusText(String status) {
        String statusLower = status.toLowerCase();
        if (statusLower.contains("menunggu")) return "Menunggu Konfirmasi";
        if (statusLower.contains("disetujui")) return "Dikonfirmasi";
        if (statusLower.contains("ditolak")) return "Ditolak";
        if (statusLower.contains("dibatalkan")) return "Dibatalkan";
        if (statusLower.contains("selesai")) return "Selesai";
        return status;
    }
    
    private void showCancelConfirmation(String roomName, String bookingId) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), true);
        dialog.setUndecorated(true);
        dialog.setBackground(new Color(0, 0, 0, 0));
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(25, 25, 25, 25)
        ));
        
        // Icon
        JLabel icon = new JLabel("⚠️", SwingConstants.CENTER);
        icon.setFont(new Font("SansSerif", Font.PLAIN, 40));
        mainPanel.add(icon, BorderLayout.NORTH);
        
        // Message
        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.setBackground(Color.WHITE);
        messagePanel.setBorder(new EmptyBorder(15, 0, 20, 0));
        
        JLabel titleLabel = new JLabel("Batalkan Peminjaman?");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel msgLabel = new JLabel("Apakah Anda yakin ingin membatalkan");
        msgLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        msgLabel.setForeground(Color.GRAY);
        msgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel msgLabel2 = new JLabel("peminjaman " + roomName + "?");
        msgLabel2.setFont(new Font("SansSerif", Font.PLAIN, 13));
        msgLabel2.setForeground(Color.GRAY);
        msgLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        messagePanel.add(titleLabel);
        messagePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        messagePanel.add(msgLabel);
        messagePanel.add(msgLabel2);
        
        mainPanel.add(messagePanel, BorderLayout.CENTER);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 12, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton cancelBtn = new JButton("Batal");
        cancelBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        cancelBtn.setForeground(Color.GRAY);
        cancelBtn.setBackground(new Color(245, 245, 245));
        cancelBtn.setBorderPainted(false);
        cancelBtn.setFocusPainted(false);
        cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelBtn.setPreferredSize(new Dimension(100, 38));
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        JButton confirmBtn = new JButton("Ya, Batalkan");
        confirmBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        confirmBtn.setForeground(Color.WHITE);
        confirmBtn.setBackground(new Color(200, 60, 60));
        confirmBtn.setBorderPainted(false);
        confirmBtn.setFocusPainted(false);
        confirmBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        confirmBtn.setPreferredSize(new Dimension(100, 38));
        confirmBtn.addActionListener(e -> {
            dialog.dispose();
            try {
                int id = Integer.parseInt(bookingId);
                boolean success = bookingControl.cancelBooking(id);
                if (success) {
                    showInfoPopup("Peminjaman berhasil dibatalkan.");
                    loadBookings(); // Refresh list
                } else {
                    showInfoPopup("Gagal membatalkan peminjaman.");
                }
            } catch (NumberFormatException ex) {
                showInfoPopup("Terjadi kesalahan data peminjaman.");
            }
        });
        
        buttonPanel.add(cancelBtn);
        buttonPanel.add(confirmBtn);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setContentPane(mainPanel);
        dialog.pack();
        dialog.setSize(320, dialog.getHeight());
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private void showInfoPopup(String message) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), true);
        dialog.setUndecorated(true);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(30, 30, 30, 30)
        ));
        
        JLabel icon = new JLabel("ℹ️", SwingConstants.CENTER);
        icon.setFont(new Font("SansSerif", Font.PLAIN, 48));
        mainPanel.add(icon, BorderLayout.NORTH);
        
        JLabel msgLabel = new JLabel("<html><center>" + message + "</center></html>", SwingConstants.CENTER);
        msgLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        msgLabel.setBorder(new EmptyBorder(15, 0, 20, 0));
        mainPanel.add(msgLabel, BorderLayout.CENTER);
        
        JButton okBtn = new JButton("OK");
        okBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        okBtn.setForeground(Color.WHITE);
        okBtn.setBackground(PRIMARY_BLUE);
        okBtn.setBorderPainted(false);
        okBtn.setFocusPainted(false);
        okBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        okBtn.setPreferredSize(new Dimension(100, 38));
        okBtn.addActionListener(e -> dialog.dispose());
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(okBtn);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);
        
        dialog.setContentPane(mainPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}
