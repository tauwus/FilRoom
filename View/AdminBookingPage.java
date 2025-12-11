package View;

import Controller.AdminBookingControl;
import Controller.AuthControl;
import Model.Admin;
import Model.Booking;
import Model.BookingStatus;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class AdminBookingPage extends JPanel {
    private AdminBookingControl bookingControl;
    private JTable bookingTable;
    private DefaultTableModel tableModel;
    
    // Colors matching User GUI
    private static final Color BG_COLOR = new Color(225, 255, 255);
    private static final Color PRIMARY_BLUE = new Color(30, 60, 120);
    private static final Color CARD_WHITE = Color.WHITE;

    public AdminBookingPage() {
        bookingControl = new AdminBookingControl();
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        JLabel title = new JLabel("Persetujuan Peminjaman");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(new Color(30, 30, 30));
        header.add(title, BorderLayout.WEST);
        
        JButton refreshBtn = createStyledButton("⟳ Refresh", new Color(80, 80, 80));
        refreshBtn.addActionListener(e -> refreshData());
        header.add(refreshBtn, BorderLayout.EAST);
        
        add(header, BorderLayout.NORTH);

        // Table Card
        JPanel tableCard = createCard();
        tableCard.setLayout(new BorderLayout());
        tableCard.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        String[] columns = {"ID", "Peminjam", "Ruangan", "Tanggal", "Waktu", "Kegiatan"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bookingTable = new JTable(tableModel);
        bookingTable.setRowHeight(40);
        bookingTable.setShowGrid(false);
        bookingTable.setIntercellSpacing(new Dimension(0, 0));
        bookingTable.setFillsViewportHeight(true);
        bookingTable.setSelectionBackground(new Color(200, 230, 255));
        bookingTable.setSelectionForeground(Color.BLACK);
        bookingTable.setFont(new Font("SansSerif", Font.PLAIN, 11));
        
        // Header Styling
        JTableHeader tableHeader = bookingTable.getTableHeader();
        tableHeader.setFont(new Font("SansSerif", Font.BOLD, 11));
        tableHeader.setBackground(PRIMARY_BLUE);
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setPreferredSize(new Dimension(0, 35));
        
        // Center align cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < bookingTable.getColumnCount(); i++) {
            bookingTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Adjust column widths
        bookingTable.getColumnModel().getColumn(0).setPreferredWidth(30);  // ID
        bookingTable.getColumnModel().getColumn(1).setPreferredWidth(80);  // Peminjam
        bookingTable.getColumnModel().getColumn(2).setPreferredWidth(60);  // Ruangan
        bookingTable.getColumnModel().getColumn(3).setPreferredWidth(70);  // Tanggal
        bookingTable.getColumnModel().getColumn(4).setPreferredWidth(70);  // Waktu
        bookingTable.getColumnModel().getColumn(5).setPreferredWidth(100); // Kegiatan
        
        JScrollPane scrollPane = new JScrollPane(bookingTable);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(CARD_WHITE);
        tableCard.add(scrollPane, BorderLayout.CENTER);

        add(tableCard, BorderLayout.CENTER);

        // Actions
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        actionPanel.setOpaque(false);
        
        JButton approveBtn = createStyledButton("✓ Setujui", new Color(40, 160, 80));
        approveBtn.addActionListener(e -> processBooking(BookingStatus.DISETUJUI));
        
        JButton rejectBtn = createStyledButton("✗ Tolak", new Color(200, 80, 80));
        rejectBtn.addActionListener(e -> processBooking(BookingStatus.DITOLAK));

        actionPanel.add(approveBtn);
        actionPanel.add(rejectBtn);
        add(actionPanel, BorderLayout.SOUTH);
        
        // Refresh on show
        addAncestorListener(new AncestorListener() {
            @Override public void ancestorAdded(AncestorEvent e) { refreshData(); }
            @Override public void ancestorRemoved(AncestorEvent e) {}
            @Override public void ancestorMoved(AncestorEvent e) {}
        });

        refreshData();
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bgColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(100, 35));
        return btn;
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
        tableModel.setRowCount(0);
        List<Booking> bookings = bookingControl.getPendingBookings();
        for (Booking b : bookings) {
            String startTime = b.getBookingItem().getStartTime().toString();
            String endTime = b.getBookingItem().getEndTime().toString();
            if (startTime.length() > 5) startTime = startTime.substring(0, 5);
            if (endTime.length() > 5) endTime = endTime.substring(0, 5);
            String timeRange = startTime + " - " + endTime;
            
            tableModel.addRow(new Object[]{
                b.getId(),
                b.getUser().getName(),
                b.getBookingItem().getRoom().getName(),
                b.getBookingItem().getUsageDate(),
                timeRange,
                b.getActivityDescription()
            });
        }
    }

    private void processBooking(BookingStatus status) {
        int row = bookingTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih peminjaman terlebih dahulu.");
            return;
        }

        int bookingId = (int) tableModel.getValueAt(row, 0);
        Admin currentAdmin = (Admin) AuthControl.getCurrentUser();
        
        if (currentAdmin == null) {
             JOptionPane.showMessageDialog(this, "Sesi habis.");
             return;
        }

        String action = status == BookingStatus.DISETUJUI ? "menyetujui" : "menolak";
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Yakin ingin " + action + " peminjaman ini?", 
            "Konfirmasi", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            if (bookingControl.updateBookingStatus(bookingId, currentAdmin.getId(), status)) {
                JOptionPane.showMessageDialog(this, "Status berhasil diperbarui!");
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal memperbarui status.");
            }
        }
    }
}