package View;

import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class DateSelectionPage extends JPanel {
    private MainFrame mainFrame;
    private YearMonth currentYearMonth;
    private JPanel daysGrid;
    private JLabel monthLabel;

    public DateSelectionPage(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        currentYearMonth = YearMonth.now();

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JLabel title = new JLabel("Pilih Tanggal");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        header.add(title, BorderLayout.WEST);
        
        JButton backBtn = new JButton("Kembali"); // Or X
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setFocusPainted(false);
        backBtn.addActionListener(e -> mainFrame.showView("Home"));
        header.add(backBtn, BorderLayout.EAST);
        
        add(header, BorderLayout.NORTH);

        // Calendar Content
        JPanel calendarPanel = new JPanel();
        calendarPanel.setLayout(new BoxLayout(calendarPanel, BoxLayout.Y_AXIS));
        calendarPanel.setBackground(Color.WHITE);
        calendarPanel.setBorder(new EmptyBorder(10, 20, 20, 20));

        // Month Navigation
        JPanel monthNav = new JPanel(new BorderLayout());
        monthNav.setBackground(Color.WHITE);
        monthNav.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JButton prevBtn = new JButton("<");
        prevBtn.addActionListener(e -> {
            currentYearMonth = currentYearMonth.minusMonths(1);
            updateCalendar();
        });
        
        JButton nextBtn = new JButton(">");
        nextBtn.addActionListener(e -> {
            currentYearMonth = currentYearMonth.plusMonths(1);
            updateCalendar();
        });

        monthLabel = new JLabel("", SwingConstants.CENTER);
        monthLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        monthNav.add(prevBtn, BorderLayout.WEST);
        monthNav.add(monthLabel, BorderLayout.CENTER);
        monthNav.add(nextBtn, BorderLayout.EAST);

        calendarPanel.add(monthNav);
        calendarPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Days Header
        JPanel daysHeader = new JPanel(new GridLayout(1, 7, 10, 10));
        daysHeader.setBackground(Color.WHITE);
        String[] days = {"Sen", "Sel", "Rab", "Kam", "Jum", "Sab", "Min"};
        for (String day : days) {
            JLabel l = new JLabel(day, SwingConstants.CENTER);
            l.setForeground(Color.GRAY);
            l.setFont(new Font("SansSerif", Font.PLAIN, 12));
            daysHeader.add(l);
        }
        calendarPanel.add(daysHeader);
        calendarPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        // Days Grid
        daysGrid = new JPanel(new GridLayout(0, 7, 10, 10));
        daysGrid.setBackground(Color.WHITE);
        calendarPanel.add(daysGrid);

        updateCalendar();

        add(calendarPanel, BorderLayout.CENTER);
    }

    private void updateCalendar() {
        monthLabel.setText(currentYearMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy", new Locale("id", "ID"))));
        daysGrid.removeAll();

        LocalDate firstOfMonth = currentYearMonth.atDay(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue(); // 1=Monday, 7=Sunday

        // Empty slots for days before the 1st
        for (int i = 1; i < dayOfWeek; i++) {
            daysGrid.add(new JLabel(""));
        }

        int daysInMonth = currentYearMonth.lengthOfMonth();
        for (int i = 1; i <= daysInMonth; i++) {
            int day = i;
            JButton dayBtn = new JButton(String.valueOf(day));
            dayBtn.setBorderPainted(false);
            dayBtn.setContentAreaFilled(false);
            dayBtn.setFocusPainted(false);
            dayBtn.setMargin(new Insets(0,0,0,0));
            dayBtn.setFont(new Font("SansSerif", Font.BOLD, 12));
            
            // Highlight Sundays
            LocalDate date = currentYearMonth.atDay(day);
            if (date.getDayOfWeek().getValue() == 7) {
                dayBtn.setForeground(Color.RED);
            }

            dayBtn.addActionListener(e -> {
                // Navigate to Room List Page
                // Pass the selected date if needed (can store in a session or pass via method)
                // For now just show the view
                mainFrame.showView("RoomList");
            });

            daysGrid.add(dayBtn);
        }

        daysGrid.revalidate();
        daysGrid.repaint();
    }
}
