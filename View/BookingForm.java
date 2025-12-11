package View;

import Controller.AuthControl;
import Controller.BookingControl;
import Model.Room;
import Model.User;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class BookingForm extends JPanel {
    private static final Color PRIMARY_BLUE = new Color(30, 60, 120);
    private MainFrame mainFrame;
    private BookingControl bookingControl;
    
    // Data yang dikirim dari halaman sebelumnya
    private Room selectedRoom;
    private LocalDate selectedDate;

    // Komponen Input
    private JComboBox<String> startCombo;
    private JComboBox<String> endCombo;
    private JComboBox<String> purposeCombo;
    private JTextField participantsField;
    private JTextArea descArea;
    
    // Occupied slots removed - logic moved to Controller

    public BookingForm(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.bookingControl = new BookingControl();

        setLayout(new BorderLayout());
        setBackground(new Color(225, 255, 255)); // Light Cyan Background

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(225, 255, 255));
        header.setBorder(new EmptyBorder(15, 15, 5, 15));

        JButton backBtn = new JButton("← Form Peminjaman");
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setFocusPainted(false);
        backBtn.setHorizontalAlignment(SwingConstants.LEFT);
        backBtn.addActionListener(e -> mainFrame.showView("RoomList")); // Kembali ke list
        header.add(backBtn, BorderLayout.NORTH);

        JLabel subTitle = new JLabel("Isi data ruangan yang akan dipinjam");
        subTitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subTitle.setBorder(new EmptyBorder(5, 10, 0, 0));
        header.add(subTitle, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);

        // Content Scroll
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(225, 255, 255));
        contentPanel.setBorder(new EmptyBorder(10, 20, 20, 20));

        // --- CARD 1: Waktu Peminjaman ---
        JPanel timeCard = createRoundedPanel();
        timeCard.setLayout(new BoxLayout(timeCard, BoxLayout.Y_AXIS));
        timeCard.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Header Card 1
        JPanel timeHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        timeHeader.setOpaque(false);
        JLabel timeIcon = new JLabel("🕒"); // Placeholder icon
        timeIcon.setFont(new Font("SansSerif", Font.PLAIN, 20));
        JLabel timeTitle = new JLabel("Waktu Peminjaman");
        timeTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        timeHeader.add(timeIcon);
        timeHeader.add(timeTitle);
        timeCard.add(timeHeader);
        
        timeCard.add(Box.createRigidArea(new Dimension(0, 15)));

        // Inputs Card 1
        JPanel timeInputs = new JPanel(new GridLayout(1, 2, 15, 0));
        timeInputs.setOpaque(false);
        
        // Initial empty combos
        startCombo = new JComboBox<>();
        endCombo = new JComboBox<>();
        endCombo.setEnabled(false); // Disabled initially
        
        startCombo.addActionListener(e -> updateEndCombo());
        
        JPanel startWrapper = createInputWrapper("Jam Mulai", startCombo);
        JPanel endWrapper = createInputWrapper("Jam Selesai", endCombo);
        
        timeInputs.add(startWrapper);
        timeInputs.add(endWrapper);
        timeCard.add(timeInputs);

        contentPanel.add(timeCard);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- CARD 2: Detail Kegiatan ---
        JPanel detailCard = createRoundedPanel();
        detailCard.setLayout(new BoxLayout(detailCard, BoxLayout.Y_AXIS));
        detailCard.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Header Card 2
        JPanel detailHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        detailHeader.setOpaque(false);
        JLabel detailIcon = new JLabel("📄"); 
        detailIcon.setFont(new Font("SansSerif", Font.PLAIN, 20));
        JLabel detailTitle = new JLabel("Detail Kegiatan");
        detailTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        detailHeader.add(detailIcon);
        detailHeader.add(detailTitle);
        detailCard.add(detailHeader);

        detailCard.add(Box.createRigidArea(new Dimension(0, 15)));

        // Inputs Card 2
        String[] purposes = {"Pilih tujuan penggunaan", "Kegiatan Organisasi", "Kelas Pengganti", "Kelas Tambahan", "Lainnya"};
        purposeCombo = new JComboBox<>(purposes);
        detailCard.add(createInputWrapper("Tujuan Penggunaan", purposeCombo));
        
        detailCard.add(Box.createRigidArea(new Dimension(0, 10)));
        
        participantsField = new JTextField();
        participantsField.setText("0");
        detailCard.add(createInputWrapper("Jumlah Peserta", participantsField));

        detailCard.add(Box.createRigidArea(new Dimension(0, 10)));

        descArea = new JTextArea(3, 20);
        descArea.setLineWrap(true);
        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setBorder(null);
        detailCard.add(createInputWrapper("Deskripsi Kegiatan", descScroll));

        contentPanel.add(detailCard);

        JScrollPane mainScroll = new JScrollPane(contentPanel);
        mainScroll.setBorder(null);
        add(mainScroll, BorderLayout.CENTER);

        // --- FOOTER: Submit Button ---
        JPanel footer = new JPanel();
        footer.setBackground(new Color(225, 255, 255));
        footer.setBorder(new EmptyBorder(10, 20, 20, 20));
        footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS));

        JButton submitBtn = new JButton("Ajukan Peminjaman");
        submitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        submitBtn.setPreferredSize(new Dimension(300, 50));
        submitBtn.setBackground(new Color(40, 80, 160)); // Blue
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        submitBtn.setFocusPainted(false);
        
        submitBtn.addActionListener(e -> submitBooking());
        
        footer.add(submitBtn);
        add(footer, BorderLayout.SOUTH);
    }

    // Method untuk menerima data dari halaman sebelumnya
    public void prepareBooking(Room room, LocalDate date) {
        this.selectedRoom = room;
        this.selectedDate = date;
        
        // Populate Start Combo
        populateStartCombo();
        
        // Reset fields
        purposeCombo.setSelectedIndex(0);
        participantsField.setText("");
        descArea.setText("");
    }
    
    private void populateStartCombo() {
        startCombo.removeAllItems();
        endCombo.removeAllItems();
        endCombo.setEnabled(false);
        
        List<String> availableStarts = bookingControl.getAvailableStartTimes(selectedRoom.getId(), selectedDate);
        
        for (String time : availableStarts) {
            startCombo.addItem(time);
        }
        
        if (startCombo.getItemCount() == 0) {
            startCombo.addItem("Penuh");
            startCombo.setEnabled(false);
        } else {
            startCombo.setEnabled(true);
            startCombo.setSelectedIndex(-1); // No selection initially
        }
    }
    
    private void updateEndCombo() {
        endCombo.removeAllItems();
        String selectedStart = (String) startCombo.getSelectedItem();
        
        if (selectedStart == null || selectedStart.equals("Penuh")) {
            endCombo.setEnabled(false);
            return;
        }
        
        List<String> availableEnds = bookingControl.getAvailableEndTimes(selectedStart, selectedRoom.getId(), selectedDate);
        
        for (String time : availableEnds) {
            endCombo.addItem(time);
        }
        
        endCombo.setEnabled(true);
    }

    private void submitBooking() {
        // 1. Validasi Input
        if (purposeCombo.getSelectedIndex() == 0 || participantsField.getText().isEmpty() || descArea.getText().isEmpty()) {
            showWarningPopup("Mohon lengkapi semua data!");
            return;
        }
        
        if (startCombo.getSelectedItem() == null || endCombo.getSelectedItem() == null) {
             showWarningPopup("Mohon pilih waktu peminjaman!");
             return;
        }

        String start = (String) startCombo.getSelectedItem();
        String end = (String) endCombo.getSelectedItem();
        
        // Validasi Jam (Start < End) dipindahkan ke Controller

        int participants = 0;
        try {
            participants = Integer.parseInt(participantsField.getText());
        } catch (NumberFormatException e) {
            showErrorPopup("Jumlah peserta harus angka.");
            return;
        }

        User user = AuthControl.getCurrentUser();
        if (user == null) {
            showWarningPopup("Sesi habis, silakan login ulang.");
            mainFrame.showView("Login");
            return;
        }

        // 2. Kirim ke Controller
        try {
            boolean success = bookingControl.createBooking(
                user,
                selectedRoom,
                selectedDate,
                start,
                end,
                (String) purposeCombo.getSelectedItem(),
                participants,
                descArea.getText()
            );

            if (success) {
                showSuccessPopup("Pengajuan Berhasil!", "Menunggu persetujuan admin.", () -> mainFrame.showView("Home"));
            } else {
                showErrorPopup("Gagal mengajukan peminjaman. Coba lagi.");
            }
        } catch (IllegalArgumentException e) {
            showErrorPopup(e.getMessage());
        }
    }

    // --- UI Helpers ---

    private JPanel createInputWrapper(String labelText, JComponent component) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        // Style Component
        if (component instanceof JComponent) {
            component.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1, true), // Rounded-ish border
                new EmptyBorder(8, 8, 8, 8)
            ));
            component.setBackground(new Color(245, 245, 245));
            component.setFont(new Font("SansSerif", Font.PLAIN, 12));
            component.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            component.setAlignmentX(Component.LEFT_ALIGNMENT);
        }
        
        // Khusus Text Area height beda
        if (component instanceof JScrollPane) {
            component.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
            JTextArea area = (JTextArea) ((JScrollPane) component).getViewport().getView();
            area.setBackground(new Color(245, 245, 245));
        }

        panel.add(component);
        return panel;
    }

    private JPanel createRoundedPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20); // Rounded 20px
                g2.setColor(new Color(200, 200, 200));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20); // Border
            }
        };
    }
    
    // Custom popup methods
    private void showSuccessPopup(String title, String message, Runnable onClose) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), true);
        dialog.setUndecorated(true);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(30, 30, 30, 30)
        ));
        
        JLabel icon = new JLabel("✅", SwingConstants.CENTER);
        icon.setFont(new Font("SansSerif", Font.PLAIN, 48));
        mainPanel.add(icon, BorderLayout.NORTH);
        
        JPanel msgPanel = new JPanel();
        msgPanel.setLayout(new BoxLayout(msgPanel, BoxLayout.Y_AXIS));
        msgPanel.setBackground(Color.WHITE);
        msgPanel.setBorder(new EmptyBorder(15, 0, 20, 0));
        
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel msgLabel = new JLabel(message, SwingConstants.CENTER);
        msgLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        msgLabel.setForeground(Color.GRAY);
        msgLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        msgPanel.add(titleLabel);
        msgPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        msgPanel.add(msgLabel);
        
        mainPanel.add(msgPanel, BorderLayout.CENTER);
        
        JButton okBtn = new JButton("OK");
        okBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        okBtn.setForeground(Color.WHITE);
        okBtn.setBackground(new Color(46, 125, 50));
        okBtn.setBorderPainted(false);
        okBtn.setFocusPainted(false);
        okBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        okBtn.setPreferredSize(new Dimension(100, 38));
        okBtn.addActionListener(e -> {
            dialog.dispose();
            if (onClose != null) onClose.run();
        });
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(okBtn);
        mainPanel.add(btnPanel, BorderLayout.SOUTH);
        
        dialog.setContentPane(mainPanel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private void showErrorPopup(String message) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), true);
        dialog.setUndecorated(true);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(30, 30, 30, 30)
        ));
        
        JLabel icon = new JLabel("❌", SwingConstants.CENTER);
        icon.setFont(new Font("SansSerif", Font.PLAIN, 48));
        mainPanel.add(icon, BorderLayout.NORTH);
        
        JLabel msgLabel = new JLabel(message, SwingConstants.CENTER);
        msgLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        msgLabel.setBorder(new EmptyBorder(15, 0, 20, 0));
        mainPanel.add(msgLabel, BorderLayout.CENTER);
        
        JButton okBtn = new JButton("OK");
        okBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        okBtn.setForeground(Color.WHITE);
        okBtn.setBackground(new Color(200, 60, 60));
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
    
    private void showWarningPopup(String message) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), true);
        dialog.setUndecorated(true);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(30, 30, 30, 30)
        ));
        
        JLabel icon = new JLabel("⚠️", SwingConstants.CENTER);
        icon.setFont(new Font("SansSerif", Font.PLAIN, 48));
        mainPanel.add(icon, BorderLayout.NORTH);
        
        JLabel msgLabel = new JLabel(message, SwingConstants.CENTER);
        msgLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        msgLabel.setBorder(new EmptyBorder(15, 0, 20, 0));
        mainPanel.add(msgLabel, BorderLayout.CENTER);
        
        JButton okBtn = new JButton("OK");
        okBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        okBtn.setForeground(Color.WHITE);
        okBtn.setBackground(new Color(255, 152, 0));
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