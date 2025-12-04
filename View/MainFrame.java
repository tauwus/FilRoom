package View;

import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public MainFrame() {
        setTitle("FILROOM");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 700); // Mobile-like aspect ratio as per image
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Initialize pages
        mainPanel.add(new SplashScreen(this), "Splash");
        mainPanel.add(new LoginPage(this), "Login");
        mainPanel.add(new RegisterPage(this), "Register");
        mainPanel.add(new AdminDashboard(this), "Dashboard");
        // Add other pages as needed
        
        add(mainPanel);
        
        // Start with Splash Screen
        cardLayout.show(mainPanel, "Splash");
    }

    public void showView(String viewName) {
        cardLayout.show(mainPanel, viewName);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}
