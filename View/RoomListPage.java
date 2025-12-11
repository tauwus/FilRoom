package View;

import Controller.RoomControl;
import Model.Room;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class RoomListPage extends JPanel {
    private MainFrame mainFrame;
    private RoomControl roomControl;
    private JPanel listPanel;
    private List<Room> allRooms = new ArrayList<>();
    private JTextField searchField;
    private JComboBox<String> buildingFilter;
    private LocalDate selectedDate = LocalDate.now();
    private JPanel datePanel;

    // Colors
    private static final Color BG_COLOR = new Color(225, 255, 255);
    private static final Color PRIMARY_BLUE = new Color(30, 60, 120);

    public RoomListPage(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.roomControl = new RoomControl();
        
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_COLOR);
        header.setBorder(new EmptyBorder(15, 15, 5, 15));
        
        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        titleRow.setBackground(BG_COLOR);
        
        JButton backBtn = new JButton("←");
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 18));
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setFocusPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> mainFrame.showView("DateSelection"));
        titleRow.add(backBtn);
        
        JLabel titleLabel = new JLabel("Daftar Ruangan");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(PRIMARY_BLUE);
        titleRow.add(titleLabel);
        
        header.add(titleRow, BorderLayout.NORTH);
        
        JLabel subTitle = new JLabel("Pilih ruangan yang ingin dipinjam");
        subTitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subTitle.setForeground(Color.GRAY);
        subTitle.setBorder(new EmptyBorder(5, 30, 0, 0));
        header.add(subTitle, BorderLayout.CENTER);
        
        add(header, BorderLayout.NORTH);

        // Content Wrapper
        JPanel contentWrapper = new JPanel();
        contentWrapper.setLayout(new BoxLayout(contentWrapper, BoxLayout.Y_AXIS));
        contentWrapper.setBackground(BG_COLOR);
        contentWrapper.setBorder(new EmptyBorder(10, 15, 20, 15));

        // Date Strip
        datePanel = new JPanel(new GridLayout(1, 4, 8, 0));
        datePanel.setBackground(BG_COLOR);
        datePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        datePanel.setBorder(new EmptyBorder(0, 0, 15, 0));
        updateDateStrip();
        contentWrapper.add(datePanel);

        // Search Field
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1, true),
            new EmptyBorder(8, 12, 8, 12)
        ));
        searchPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        
        JLabel searchIcon = new JLabel("🔍");
        searchIcon.setFont(new Font("SansSerif", Font.PLAIN, 14));
        searchPanel.add(searchIcon, BorderLayout.WEST);
        
        searchField = new JTextField("  Cari Ruangan...");
        searchField.setForeground(Color.GRAY);
        searchField.setBorder(null);
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().trim().equals("Cari Ruangan...")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("  Cari Ruangan...");
                    searchField.setForeground(Color.GRAY);
                }
            }
        });
        
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filterRooms(); }
            public void removeUpdate(DocumentEvent e) { filterRooms(); }
            public void changedUpdate(DocumentEvent e) { filterRooms(); }
        });
        
        searchPanel.add(searchField, BorderLayout.CENTER);
        contentWrapper.add(searchPanel);
        contentWrapper.add(Box.createRigidArea(new Dimension(0, 12)));

        // Building Filter
        buildingFilter = new JComboBox<>(new String[]{"Semua Gedung", "Gedung F", "Gedung G"});
        buildingFilter.setFont(new Font("SansSerif", Font.PLAIN, 14));
        buildingFilter.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        buildingFilter.setBorder(new LineBorder(new Color(220, 220, 220), 1, true));
        buildingFilter.addActionListener(e -> filterRooms());
        contentWrapper.add(buildingFilter);
        contentWrapper.add(Box.createRigidArea(new Dimension(0, 15)));

        // Room List
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

        // Listener to refresh when shown
        addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                updateDateStrip();
                loadRooms();
            }
            @Override
            public void ancestorRemoved(AncestorEvent event) {}
            @Override
            public void ancestorMoved(AncestorEvent event) {}
        });
    }
    
    public void setDate(LocalDate date) {
        this.selectedDate = date;
        if (this.selectedDate.isBefore(LocalDate.now())) {
            this.selectedDate = LocalDate.now();
        }
        updateDateStrip();
        loadRooms();
    }

    private void updateDateStrip() {
        datePanel.removeAll();
        LocalDate startDate = selectedDate;
        
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("EEE", new Locale("id", "ID"));
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM", new Locale("id", "ID"));

        for (int i = 0; i < 4; i++) {
            LocalDate date = startDate.plusDays(i);
            boolean isSelected = (i == 0); // First one is selected
            
            JPanel dateItem = new JPanel();
            dateItem.setLayout(new BoxLayout(dateItem, BoxLayout.Y_AXIS));
            dateItem.setBackground(isSelected ? PRIMARY_BLUE : Color.WHITE);
            dateItem.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(isSelected ? PRIMARY_BLUE : new Color(220, 220, 220), 1, true),
                new EmptyBorder(8, 10, 8, 10)
            ));
            dateItem.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            JLabel dayLabel = new JLabel(capitalize(date.format(dayFormatter)), SwingConstants.CENTER);
            dayLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
            dayLabel.setForeground(isSelected ? Color.WHITE : Color.GRAY);
            dayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JLabel dateLabel = new JLabel(date.format(dateFormatter), SwingConstants.CENTER);
            dateLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
            dateLabel.setForeground(isSelected ? Color.WHITE : Color.BLACK);
            dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            dateItem.add(dayLabel);
            dateItem.add(Box.createRigidArea(new Dimension(0, 3)));
            dateItem.add(dateLabel);
            
            final LocalDate clickDate = date;
            dateItem.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    selectedDate = clickDate;
                    updateDateStrip();
                    datePanel.revalidate();
                    datePanel.repaint();
                }
            });
            
            datePanel.add(dateItem);
        }
        
        datePanel.revalidate();
        datePanel.repaint();
    }
    
    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    private void loadRooms() {
        allRooms = roomControl.getAllRooms();
        filterRooms();
    }

    private void filterRooms() {
        listPanel.removeAll();
        String searchText = searchField.getText().trim();
        if (searchText.equals("Cari Ruangan...")) searchText = "";
        searchText = searchText.toLowerCase();
        
        String selectedBuilding = (String) buildingFilter.getSelectedItem();
        
        for (Room room : allRooms) {
            boolean matchesSearch = room.getName().toLowerCase().contains(searchText);
            boolean matchesBuilding = "Semua Gedung".equals(selectedBuilding) || 
                                      room.getLocation().contains(selectedBuilding);
            
            if (matchesSearch && matchesBuilding) {
                listPanel.add(createRoomCard(room));
                listPanel.add(Box.createRigidArea(new Dimension(0, 12)));
            }
        }
        
        listPanel.revalidate();
        listPanel.repaint();
    }

    private JPanel createRoomCard(Room room) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1, true),
            new EmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));

        // Top - Room Info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        
        JLabel nameLabel = new JLabel(room.getName());
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        nameLabel.setForeground(PRIMARY_BLUE);
        
        JLabel locLabel = new JLabel(room.getLocation());
        locLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        locLabel.setForeground(Color.GRAY);
        
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 3)));
        infoPanel.add(locLabel);
        
        card.add(infoPanel, BorderLayout.NORTH);
        
        // Center - Capacity
        JPanel capacityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
        capacityPanel.setBackground(Color.WHITE);
        
        JLabel capIcon = new JLabel("👥");
        capIcon.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        JLabel capLabel = new JLabel(" Kapasitas: " + room.getCapacity() + " orang");
        capLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        capLabel.setForeground(new Color(80, 80, 80));
        
        capacityPanel.add(capIcon);
        capacityPanel.add(capLabel);
        
        card.add(capacityPanel, BorderLayout.CENTER);
        
        // Bottom - Button
        JButton selectBtn = new JButton("Pilih Ruangan");
        selectBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        selectBtn.setForeground(Color.WHITE);
        selectBtn.setBackground(PRIMARY_BLUE);
        selectBtn.setBorderPainted(false);
        selectBtn.setFocusPainted(false);
        selectBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        selectBtn.setPreferredSize(new Dimension(0, 42));
        
        selectBtn.addActionListener(e -> {
            BookingForm bookingForm = (BookingForm) mainFrame.getView("BookingForm");
            if (bookingForm != null) {
                bookingForm.prepareBooking(room, selectedDate);
                mainFrame.showView("BookingForm");
            }
        });
        
        card.add(selectBtn, BorderLayout.SOUTH);
        
        return card;
    }
}
