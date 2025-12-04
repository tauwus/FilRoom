package View;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class SplashScreen extends JPanel {
    private MainFrame mainFrame;

    public SplashScreen(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(new Color(75, 0, 130)); // Indigo/Purple

        // Content Panel
        JPanel contentPanel = new JPanel() {
            private Image backgroundImage;

            {
                try {
                    // Try to load background image
                    // You can place "splash_bg.png" in the project root or adjust the path
                    backgroundImage = new ImageIcon("splash_bg.png").getImage();
                    if (backgroundImage.getWidth(null) == -1) {
                        backgroundImage = null;
                    }
                } catch (Exception e) {
                    backgroundImage = null;
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    // Gradient Background
                    GradientPaint gp = new GradientPaint(0, 0, new Color(80, 50, 200), 0, getHeight(), new Color(40, 20, 120));
                    g2d.setPaint(gp);
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        // Spacer
        contentPanel.add(Box.createVerticalGlue());

        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Title
        JLabel titleLabel = new JLabel("FILROOM");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(titleLabel);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Manage Space,");
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitleLabel.setForeground(Color.WHITE);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(subtitleLabel);

        JLabel subtitleLabel2 = new JLabel("Empower Events");
        subtitleLabel2.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitleLabel2.setForeground(Color.WHITE);
        subtitleLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(subtitleLabel2);

        // Spacer
        contentPanel.add(Box.createVerticalGlue());

        add(contentPanel, BorderLayout.CENTER);

        // Click to proceed (simulating splash timeout or user interaction)
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mainFrame.showView("Login");
            }
        });
        
        // Auto transition after 3 seconds
        Timer timer = new Timer(3000, e -> mainFrame.showView("Login"));
        timer.setRepeats(false);
        timer.start();
    }
}
