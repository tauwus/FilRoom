package View;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class BottomNavPanel extends JPanel {
    // Colors
    private static final Color TEAL_COLOR = new Color(0, 128, 128);
    private static final Color INACTIVE_COLOR = new Color(150, 150, 150);

    public BottomNavPanel(MainFrame mainFrame, String activePage) {
        setLayout(new GridLayout(1, 4, 0, 0));
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(400, 70));
        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 230, 230)));

        add(createNavButton("Home", "🏠", "Home", activePage, mainFrame));
        add(createNavButton("Aktivitas", "🕒", "History", activePage, mainFrame));
        add(createNavButton("Profil", "👤", "Profile", activePage, mainFrame));
    }

    private JPanel createNavButton(String label, String icon, String viewName, String activePage, MainFrame mainFrame) {
        boolean isActive = label.equalsIgnoreCase(activePage) || 
                          (label.equals("Aktivitas") && activePage.equals("History")) ||
                          (label.equals("Profil") && activePage.equals("Profile"));

        JPanel navItem = new JPanel();
        navItem.setLayout(new BoxLayout(navItem, BoxLayout.Y_AXIS));
        navItem.setBackground(Color.WHITE);
        navItem.setBorder(new EmptyBorder(10, 5, 10, 5));
        navItem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Icon Label
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 22));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        iconLabel.setForeground(isActive ? TEAL_COLOR : INACTIVE_COLOR);
        
        // Text Label
        JLabel textLabel = new JLabel(label);
        textLabel.setFont(new Font("SansSerif", isActive ? Font.BOLD : Font.PLAIN, 11));
        textLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        textLabel.setForeground(isActive ? TEAL_COLOR : INACTIVE_COLOR);
        
        navItem.add(iconLabel);
        navItem.add(Box.createRigidArea(new Dimension(0, 3)));
        navItem.add(textLabel);
        
        // Active indicator line
        if (isActive) {
            JPanel indicator = new JPanel();
            indicator.setBackground(TEAL_COLOR);
            indicator.setMaximumSize(new Dimension(40, 3));
            indicator.setPreferredSize(new Dimension(40, 3));
            indicator.setAlignmentX(Component.CENTER_ALIGNMENT);
            navItem.add(Box.createRigidArea(new Dimension(0, 3)));
            navItem.add(indicator);
        }
        
        // Click listener
        navItem.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (!isActive) {
                    mainFrame.showView(viewName);
                }
            }
            
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (!isActive) {
                    navItem.setBackground(new Color(245, 245, 245));
                }
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                navItem.setBackground(Color.WHITE);
            }
        });

        return navItem;
    }
}
