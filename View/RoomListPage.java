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

    public RoomListPage(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.roomControl = new RoomControl();
        
        setLayout(new BorderLayout());
        setBackground(new Color(225, 255, 255)); // Light cyan background

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(225, 255, 255));
        header.setBorder(new EmptyBorder(15, 15, 5, 15));
        
        JButton backBtn = new JButton("← Daftar Ruangan");
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setFocusPainted(false);
        backBtn.setHorizontalAlignment(SwingConstants.LEFT);
        backBtn.addActionListener(e -> mainFrame.showView("DateSelection"));
        header.add(backBtn, BorderLayout.NORTH);
        
        JLabel subTitle = new JLabel("Pilih ruangan yang ingin dipinjam");
        subTitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subTitle.setBorder(new EmptyBorder(5, 10, 0, 0));
        header.add(subTitle, BorderLayout.CENTER);
        
        add(header, BorderLayout.NORTH);

        // Content Wrapper
        JPanel contentWrapper = new ScrollablePanel();
        contentWrapper.setLayout(new BoxLayout(contentWrapper, BoxLayout.Y_AXIS));
        contentWrapper.setBackground(new Color(225, 255, 255));
        contentWrapper.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Date Scroll Section
        JPanel dateContainer = new JPanel(new BorderLayout());
        dateContainer.setBackground(new Color(225, 255, 255));
        dateContainer.setBorder(new EmptyBorder(5, 0, 10, 0));
        dateContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80)); // Limit height
        
        datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        datePanel.setBackground(new Color(225, 255, 255));
        updateDateStrip();
        
        JScrollPane dateScrollPane = new JScrollPane(datePanel);
        dateScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        dateScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        dateScrollPane.setBorder(null);
        dateScrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0)); // Hide scrollbar
        
        dateContainer.add(dateScrollPane, BorderLayout.CENTER);
        contentWrapper.add(dateContainer);

        // Search & Filter Section
        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.Y_AXIS));
        controlsPanel.setBackground(new Color(225, 255, 255));
        controlsPanel.setBorder(new EmptyBorder(0, 20, 0, 20));

        // Search Bar
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(new Color(240, 240, 240));
        searchPanel.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
        searchPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        
        searchField = new JTextField("Cari Ruangan...");
        searchField.setForeground(Color.GRAY);
        searchField.setBorder(new EmptyBorder(5, 10, 5, 10));
        searchField.setBackground(new Color(240, 240, 240));
        searchField.setOpaque(true);
        
        // Placeholder logic
        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Cari Ruangan...")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Cari Ruangan...");
                    searchField.setForeground(Color.GRAY);
                }
            }
        });
        
        // Filter logic
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filterRooms(); }
            public void removeUpdate(DocumentEvent e) { filterRooms(); }
            public void changedUpdate(DocumentEvent e) { filterRooms(); }
        });
        
        JLabel searchIcon = new JLabel("🔍");
        searchIcon.setBorder(new EmptyBorder(0, 10, 0, 0));
        
        searchPanel.add(searchIcon, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        
        controlsPanel.add(searchPanel);
        controlsPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Filters
        JPanel filterPanel = new JPanel(new GridLayout(1, 1)); // Only 1 filter now
        filterPanel.setBackground(new Color(225, 255, 255));
        filterPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        
        buildingFilter = new JComboBox<>(new String[]{"Semua Gedung", "Gedung F", "Gedung G"});
        buildingFilter.addActionListener(e -> filterRooms());
        
        filterPanel.add(buildingFilter);
        
        controlsPanel.add(filterPanel);
        controlsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        contentWrapper.add(controlsPanel);

        // Room List
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(new Color(225, 255, 255));
        listPanel.setBorder(new EmptyBorder(0, 20, 0, 20));
        
        contentWrapper.add(listPanel);

        JScrollPane mainScrollPane = new JScrollPane(contentWrapper);
        mainScrollPane.setBorder(null);
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(mainScrollPane, BorderLayout.CENTER);

        // Refresh data when shown
        addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                loadRooms();
            }
            @Override
            public void ancestorRemoved(AncestorEvent event) {}
            @Override
            public void ancestorMoved(AncestorEvent event) {}
        });
    }

    // Helper class to force panel to track viewport width
    private class ScrollablePanel extends JPanel implements Scrollable {
        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return getPreferredSize();
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 16;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 16;
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            return true; // Force width to match viewport
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            return false;
        }
    }

    private void updateDateStrip() {
        datePanel.removeAll();
        LocalDate startDate = LocalDate.now();
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("EEE", Locale.forLanguageTag("id-ID"));
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM", Locale.forLanguageTag("id-ID"));

        for (int i = 0; i < 14; i++) { // Show 2 weeks
            LocalDate date = startDate.plusDays(i);
            boolean isSelected = date.equals(selectedDate);
            
            JPanel dateItem = new JPanel(new BorderLayout());
            dateItem.setPreferredSize(new Dimension(80, 60));
            dateItem.setBackground(isSelected ? new Color(50, 80, 160) : Color.WHITE);
            dateItem.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
            dateItem.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            
            JLabel dayLabel = new JLabel(date.format(dayFormatter));
            dayLabel.setHorizontalAlignment(SwingConstants.CENTER);
            dayLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
            dayLabel.setForeground(isSelected ? Color.WHITE : Color.GRAY);
            
            JLabel dateLabel = new JLabel(date.format(dateFormatter));
            dateLabel.setHorizontalAlignment(SwingConstants.CENTER);
            dateLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
            dateLabel.setForeground(isSelected ? Color.WHITE : Color.BLACK);
            
            dateItem.add(dayLabel, BorderLayout.NORTH);
            dateItem.add(dateLabel, BorderLayout.CENTER);
            
            dateItem.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    selectedDate = date;
                    updateDateStrip();
                    datePanel.revalidate();
                    datePanel.repaint();
                    // Optionally reload rooms if availability depends on date
                    // loadRooms(); 
                }
            });
            
            datePanel.add(dateItem);
        }
    }

    private void loadRooms() {
        allRooms = roomControl.getAllRooms();
        filterRooms();
    }

    private void filterRooms() {
        listPanel.removeAll();
        String searchText = searchField.getText();
        if (searchText.equals("Cari Ruangan...")) searchText = "";
        searchText = searchText.toLowerCase();
        
        String selectedBuilding = (String) buildingFilter.getSelectedItem();
        
        for (Room room : allRooms) {
            boolean matchesSearch = room.getName().toLowerCase().contains(searchText);
            boolean matchesBuilding = "Semua Gedung".equals(selectedBuilding) || 
                                      room.getLocation().contains(selectedBuilding);
            
            if (matchesSearch && matchesBuilding) {
                listPanel.add(createRoomCard(room));
                listPanel.add(Box.createRigidArea(new Dimension(0, 15)));
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
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

        // Header: Name + Status
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        
        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setBackground(Color.WHITE);
        
        JLabel nameLabel = new JLabel(room.getName());
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        
        JLabel locLabel = new JLabel(room.getLocation());
        locLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        locLabel.setForeground(Color.GRAY);
        
        titlePanel.add(nameLabel);
        titlePanel.add(locLabel);
        
        header.add(titlePanel, BorderLayout.CENTER);
        
        JLabel statusLabel = new JLabel(room.getStatus());
        statusLabel.setOpaque(true);
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 10));
        statusLabel.setBorder(new EmptyBorder(5, 10, 5, 10));
        
        if ("tersedia".equalsIgnoreCase(room.getStatus())) {
            statusLabel.setBackground(new Color(144, 238, 144)); // Green
            statusLabel.setText("Tersedia");
        } else {
            statusLabel.setBackground(new Color(255, 182, 193)); // Red/Pink
            statusLabel.setText("Terpakai");
        }
        
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        statusPanel.setBackground(Color.WHITE);
        statusPanel.add(statusLabel);
        
        header.add(statusPanel, BorderLayout.EAST);
        
        card.add(header, BorderLayout.NORTH);
        
        // Body: Capacity
        JPanel body = new JPanel(new FlowLayout(FlowLayout.LEFT));
        body.setBackground(Color.WHITE);
        body.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        JLabel capLabel = new JLabel("👥 Kapasitas: " + room.getCapacity() + " orang");
        capLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        body.add(capLabel);
        
        card.add(body, BorderLayout.CENTER);
        
        // Footer: Button
        JButton selectBtn = new JButton("Pilih Ruangan");
        selectBtn.setBackground(new Color(50, 80, 160)); // Blue
        selectBtn.setForeground(Color.WHITE);
        selectBtn.setFocusPainted(false);
        selectBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        selectBtn.setPreferredSize(new Dimension(100, 40));
        
        if (!"tersedia".equalsIgnoreCase(room.getStatus())) {
            selectBtn.setBackground(Color.GRAY);
            selectBtn.setEnabled(false);
        }
        
        card.add(selectBtn, BorderLayout.SOUTH);
        
        return card;
    }
}
