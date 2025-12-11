package View;

import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class DateSelectionPage extends JPanel {
    private MainFrame mainFrame;
    private YearMonth currentYearMonth;
    private JPanel daysGrid;
    private JLabel monthLabel;
    
    // Colors
    private static final Color BG_COLOR = new Color(225, 255, 255);
    private static final Color PRIMARY_BLUE = new Color(30, 60, 120);
    private static final Color CARD_WHITE = Color.WHITE;
    private static final Color DISABLED_GRAY = new Color(200, 200, 200);
    
    // Date constraints
    private static final int MAX_ADVANCE_DAYS = 30;

    public DateSelectionPage(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);

        currentYearMonth = YearMonth.now();

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG_COLOR);
        header.setBorder(new EmptyBorder(20, 20, 10, 20));
        
        JButton backBtn = new JButton("←");
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 20));
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setFocusPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.setPreferredSize(new Dimension(40, 40));
        backBtn.addActionListener(e -> mainFrame.showView("Home"));
        header.add(backBtn, BorderLayout.WEST);
        
        JLabel title = new JLabel("Pilih Tanggal", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(PRIMARY_BLUE);
        header.add(title, BorderLayout.CENTER);
        
        // Placeholder for symmetry
        JLabel placeholder = new JLabel();
        placeholder.setPreferredSize(new Dimension(40, 40));
        header.add(placeholder, BorderLayout.EAST);
        
        add(header, BorderLayout.NORTH);

        // Calendar Content
        JPanel calendarWrapper = new JPanel(new BorderLayout());
        calendarWrapper.setBackground(BG_COLOR);
        calendarWrapper.setBorder(new EmptyBorder(10, 20, 20, 20));
        
        JPanel calendarCard = new JPanel();
        calendarCard.setLayout(new BorderLayout());
        calendarCard.setBackground(CARD_WHITE);
        calendarCard.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(230, 230, 230), 1, true),
            new EmptyBorder(20, 15, 20, 15)
        ));

        JPanel calendarContent = new JPanel();
        calendarContent.setLayout(new BoxLayout(calendarContent, BoxLayout.Y_AXIS));
        calendarContent.setBackground(CARD_WHITE);

        // Month Navigation
        JPanel monthNav = new JPanel(new BorderLayout());
        monthNav.setBackground(CARD_WHITE);
        monthNav.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JButton prevBtn = createNavButton("<");
        prevBtn.addActionListener(e -> {
            YearMonth minMonth = YearMonth.now();
            if (currentYearMonth.isAfter(minMonth)) {
                currentYearMonth = currentYearMonth.minusMonths(1);
                updateCalendar();
            }
        });
        
        JButton nextBtn = createNavButton(">");
        nextBtn.addActionListener(e -> {
            YearMonth maxMonth = YearMonth.now().plusMonths(1);
            if (!currentYearMonth.isAfter(maxMonth)) {
                currentYearMonth = currentYearMonth.plusMonths(1);
                updateCalendar();
            }
        });

        monthLabel = new JLabel("", SwingConstants.CENTER);
        monthLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        monthLabel.setForeground(PRIMARY_BLUE);

        monthNav.add(prevBtn, BorderLayout.WEST);
        monthNav.add(monthLabel, BorderLayout.CENTER);
        monthNav.add(nextBtn, BorderLayout.EAST);

        calendarContent.add(monthNav);
        calendarContent.add(Box.createRigidArea(new Dimension(0, 20)));

        // Days Header
        JPanel daysHeader = new JPanel(new GridLayout(1, 7, 5, 5));
        daysHeader.setBackground(CARD_WHITE);
        daysHeader.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        String[] days = {"Sen", "Sel", "Rab", "Kam", "Jum", "Sab", "Min"};
        for (int i = 0; i < days.length; i++) {
            JLabel l = new JLabel(days[i], SwingConstants.CENTER);
            l.setFont(new Font("SansSerif", Font.BOLD, 12));
            if (i == 6) { // Sunday
                l.setForeground(new Color(200, 80, 80));
            } else {
                l.setForeground(Color.GRAY);
            }
            daysHeader.add(l);
        }
        calendarContent.add(daysHeader);
        calendarContent.add(Box.createRigidArea(new Dimension(0, 10)));

        // Days Grid
        daysGrid = new JPanel(new GridLayout(6, 7, 5, 5));
        daysGrid.setBackground(CARD_WHITE);
        calendarContent.add(daysGrid);
        
        calendarCard.add(calendarContent, BorderLayout.CENTER);
        calendarWrapper.add(calendarCard, BorderLayout.NORTH);

        // Info Panel
        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(BG_COLOR);
        infoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        infoPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        JLabel infoIcon = new JLabel("ℹ");
        infoIcon.setFont(new Font("SansSerif", Font.PLAIN, 14));
        infoIcon.setForeground(new Color(100, 150, 200));
        
        JLabel infoLabel = new JLabel(" Pemesanan maksimal 30 hari ke depan");
        infoLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        infoLabel.setForeground(Color.GRAY);
        
        infoPanel.add(infoIcon);
        infoPanel.add(infoLabel);
        
        calendarWrapper.add(infoPanel, BorderLayout.CENTER);

        add(calendarWrapper, BorderLayout.CENTER);

        updateCalendar();
    }
    
    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 18));
        btn.setForeground(Color.WHITE);
        btn.setBackground(PRIMARY_BLUE);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(45, 35));
        btn.setOpaque(true);
        return btn;
    }

    private void updateCalendar() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", new Locale("id", "ID"));
        monthLabel.setText(currentYearMonth.format(formatter));
        daysGrid.removeAll();

        LocalDate today = LocalDate.now();
        LocalDate maxDate = today.plusDays(MAX_ADVANCE_DAYS);
        LocalDate firstOfMonth = currentYearMonth.atDay(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue(); // 1=Monday, 7=Sunday

        // Empty slots for days before the 1st
        for (int i = 1; i < dayOfWeek; i++) {
            JLabel empty = new JLabel("");
            empty.setPreferredSize(new Dimension(40, 40));
            daysGrid.add(empty);
        }

        int daysInMonth = currentYearMonth.lengthOfMonth();
        for (int i = 1; i <= daysInMonth; i++) {
            int day = i;
            LocalDate date = currentYearMonth.atDay(day);
            
            JButton dayBtn = new JButton(String.valueOf(day));
            dayBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
            dayBtn.setFocusPainted(false);
            dayBtn.setPreferredSize(new Dimension(40, 40));
            dayBtn.setMargin(new Insets(0, 0, 0, 0));
            
            boolean isPast = date.isBefore(today);
            boolean isTooFar = date.isAfter(maxDate);
            boolean isSunday = date.getDayOfWeek().getValue() == 7;
            boolean isToday = date.equals(today);
            
            if (isPast || isTooFar) {
                // Disabled - past or too far
                dayBtn.setEnabled(false);
                dayBtn.setForeground(DISABLED_GRAY);
                dayBtn.setBackground(new Color(250, 250, 250));
                dayBtn.setBorderPainted(false);
                dayBtn.setContentAreaFilled(true);
                dayBtn.setOpaque(true);
                dayBtn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            } else if (isToday) {
                // Today - highlighted
                dayBtn.setForeground(Color.WHITE);
                dayBtn.setBackground(PRIMARY_BLUE);
                dayBtn.setBorderPainted(false);
                dayBtn.setContentAreaFilled(true);
                dayBtn.setOpaque(true);
                dayBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                dayBtn.addActionListener(e -> selectDate(day));
            } else if (isSunday) {
                // Sunday
                dayBtn.setForeground(new Color(200, 80, 80));
                dayBtn.setBackground(CARD_WHITE);
                dayBtn.setBorderPainted(false);
                dayBtn.setContentAreaFilled(false);
                dayBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                dayBtn.addActionListener(e -> selectDate(day));
            } else {
                // Normal day
                dayBtn.setForeground(Color.DARK_GRAY);
                dayBtn.setBackground(CARD_WHITE);
                dayBtn.setBorderPainted(false);
                dayBtn.setContentAreaFilled(false);
                dayBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                dayBtn.addActionListener(e -> selectDate(day));
            }

            daysGrid.add(dayBtn);
        }
        
        // Fill remaining empty cells to complete the grid
        int totalCells = (dayOfWeek - 1) + daysInMonth;
        int remaining = 42 - totalCells; // 6 rows x 7 columns = 42
        for (int i = 0; i < remaining; i++) {
            JLabel empty = new JLabel("");
            empty.setPreferredSize(new Dimension(40, 40));
            daysGrid.add(empty);
        }

        daysGrid.revalidate();
        daysGrid.repaint();
    }
    
    private void selectDate(int day) {
        LocalDate selectedDate = currentYearMonth.atDay(day);
        RoomListPage roomListPage = (RoomListPage) mainFrame.getView("RoomList");
        if (roomListPage != null) {
            roomListPage.setDate(selectedDate);
        }
        mainFrame.showView("RoomList");
    }
}
