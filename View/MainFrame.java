package View;

import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    // Simpan referensi page agar bisa diakses
    private BookingForm bookingForm; 

    public MainFrame() {
        setTitle("FILROOM");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 700); 
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Initialize pages
        bookingForm = new BookingForm(this); // Init Booking Form

        mainPanel.add(new SplashScreen(this), "Splash");
        mainPanel.add(new LoginPage(this), "Login");
        mainPanel.add(new RegisterPage(this), "Register");
        mainPanel.add(new AdminDashboard(this), "Dashboard");
        mainPanel.add(new HomePage(this), "Home");
        mainPanel.add(new DateSelectionPage(this), "DateSelection");
        mainPanel.add(new RoomListPage(this), "RoomList");
        
        // Add BookingForm
        mainPanel.add(bookingForm, "BookingForm");
        
        add(mainPanel);
        
        cardLayout.show(mainPanel, "Splash");
    }

    public void showView(String viewName) {
        cardLayout.show(mainPanel, viewName);
    }

    // Helper untuk mengambil object panel tertentu (untuk passing data)
    public Component getView(String name) {
        if ("BookingForm".equals(name)) return bookingForm;
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}