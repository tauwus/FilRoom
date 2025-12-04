package View;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AdminDashboard extends JPanel {
    private MainFrame mainFrame;

    public AdminDashboard(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 240)); // Light grey background

        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));
        headerPanel.add(new JLabel("Dashboard"));
        add(headerPanel, BorderLayout.NORTH);

        // Content (List)
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(240, 240, 240));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        for (int i = 0; i < 5; i++) {
            contentPanel.add(createListItem());
            contentPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        }

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom Nav (Placeholder)
        JPanel bottomNav = new JPanel();
        bottomNav.setBackground(Color.WHITE);
        bottomNav.setPreferredSize(new Dimension(getWidth(), 50));
        bottomNav.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 15));
        bottomNav.add(new JLabel("Home"));
        bottomNav.add(new JLabel("Search"));
        bottomNav.add(new JLabel("Profile"));
        add(bottomNav, BorderLayout.SOUTH);
    }

    private JPanel createListItem() {
        JPanel item = new JPanel();
        item.setLayout(new BorderLayout());
        item.setBackground(new Color(220, 220, 220)); // Grey box
        item.setPreferredSize(new Dimension(300, 80));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        item.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Icon placeholder
        JPanel icon = new JPanel();
        icon.setBackground(new Color(150, 150, 150));
        icon.setPreferredSize(new Dimension(60, 60));
        item.add(icon, BorderLayout.WEST);

        // Text lines
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.setBorder(new EmptyBorder(0, 10, 0, 0));
        
        JPanel line1 = new JPanel();
        line1.setBackground(new Color(180, 180, 180));
        line1.setPreferredSize(new Dimension(100, 10));
        textPanel.add(line1);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        JPanel line2 = new JPanel();
        line2.setBackground(new Color(180, 180, 180));
        line2.setPreferredSize(new Dimension(150, 10));
        textPanel.add(line2);

        item.add(textPanel, BorderLayout.CENTER);

        return item;
    }
}