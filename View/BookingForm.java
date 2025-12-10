package View;

import Controller.AuthControl;
import Controller.BookingControl;
import Model.Room;
import Model.User;
import java.awt.*;
import java.time.LocalDate;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class BookingForm extends JPanel {
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
        
        // Generate jam 07:00 - 21:00
        String[] timeSlots = generateTimeSlots(7, 21);
        
        JPanel startWrapper = createInputWrapper("Jam Mulai", startCombo = new JComboBox<>(timeSlots));
        JPanel endWrapper = createInputWrapper("Jam Selesai", endCombo = new JComboBox<>(timeSlots));
        
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
        
        // Reset fields
        startCombo.setSelectedIndex(0);
        endCombo.setSelectedIndex(0);
        purposeCombo.setSelectedIndex(0);
        participantsField.setText("");
        descArea.setText("");
    }

    private void submitBooking() {
        // 1. Validasi Input
        if (purposeCombo.getSelectedIndex() == 0 || participantsField.getText().isEmpty() || descArea.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mohon lengkapi semua data!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String start = (String) startCombo.getSelectedItem();
        String end = (String) endCombo.getSelectedItem();
        
        // Validasi Jam (Start < End) dipindahkan ke Controller

        int participants = 0;
        try {
            participants = Integer.parseInt(participantsField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Jumlah peserta harus angka.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = AuthControl.getCurrentUser();
        if (user == null) {
            JOptionPane.showMessageDialog(this, "Sesi habis, silakan login ulang.");
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
                JOptionPane.showMessageDialog(this, "Pengajuan Berhasil! Menunggu persetujuan admin.");
                mainFrame.showView("Home");
            } else {
                JOptionPane.showMessageDialog(this, "Gagal mengajukan peminjaman. Coba lagi.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

    private String[] generateTimeSlots(int startHour, int endHour) {
        String[] slots = new String[endHour - startHour + 1];
        for (int i = 0; i < slots.length; i++) {
            slots[i] = String.format("%02d:00", startHour + i);
        }
        return slots;
    }
}