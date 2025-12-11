package View;

import Controller.AdminRoomControl;
import Model.Room;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class AdminRoomPage extends JPanel {
    private AdminRoomControl roomControl;
    private JTable roomTable;
    private DefaultTableModel tableModel;
    
    // Colors matching User GUI
    private static final Color BG_COLOR = new Color(225, 255, 255);
    private static final Color PRIMARY_BLUE = new Color(30, 60, 120);
    private static final Color CARD_WHITE = Color.WHITE;

    public AdminRoomPage() {
        roomControl = new AdminRoomControl();
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        JLabel title = new JLabel("Manajemen Ruangan");
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(new Color(30, 30, 30));
        header.add(title, BorderLayout.WEST);

        JButton addBtn = createStyledButton("+ Tambah", PRIMARY_BLUE);
        addBtn.addActionListener(e -> showRoomDialog(null));
        header.add(addBtn, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // Table Card
        JPanel tableCard = createCard();
        tableCard.setLayout(new BorderLayout());
        tableCard.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        String[] columns = {"ID", "Nama Ruangan", "Lokasi", "Kapasitas"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        roomTable = new JTable(tableModel);
        roomTable.setRowHeight(40);
        roomTable.setShowGrid(false);
        roomTable.setIntercellSpacing(new Dimension(0, 0));
        roomTable.setFillsViewportHeight(true);
        roomTable.setSelectionBackground(new Color(200, 230, 255));
        roomTable.setSelectionForeground(Color.BLACK);
        roomTable.setFont(new Font("SansSerif", Font.PLAIN, 12));
        
        // Header Styling
        JTableHeader tableHeader = roomTable.getTableHeader();
        tableHeader.setFont(new Font("SansSerif", Font.BOLD, 12));
        tableHeader.setBackground(PRIMARY_BLUE);
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setPreferredSize(new Dimension(0, 35));
        
        // Center align cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < roomTable.getColumnCount(); i++) {
            roomTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        JScrollPane scrollPane = new JScrollPane(roomTable);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(CARD_WHITE);
        tableCard.add(scrollPane, BorderLayout.CENTER);

        add(tableCard, BorderLayout.CENTER);

        // Actions
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        actionPanel.setOpaque(false);
        
        JButton editBtn = createStyledButton("Edit", new Color(80, 80, 80));
        editBtn.addActionListener(e -> editSelectedRoom());
        
        JButton deleteBtn = createStyledButton("Hapus", new Color(200, 80, 80));
        deleteBtn.addActionListener(e -> deleteSelectedRoom());

        actionPanel.add(editBtn);
        actionPanel.add(deleteBtn);
        add(actionPanel, BorderLayout.SOUTH);
        
        // Refresh on show
        addAncestorListener(new AncestorListener() {
            @Override public void ancestorAdded(AncestorEvent e) { refreshData(); }
            @Override public void ancestorRemoved(AncestorEvent e) {}
            @Override public void ancestorMoved(AncestorEvent e) {}
        });

        refreshData();
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bgColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(90, 35));
        return btn;
    }
    
    private JPanel createCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            }
        };
        card.setOpaque(false);
        return card;
    }

    private void refreshData() {
        tableModel.setRowCount(0);
        List<Room> rooms = roomControl.getAllRooms();
        for (Room r : rooms) {
            tableModel.addRow(new Object[]{r.getId(), r.getName(), r.getLocation(), r.getCapacity()});
        }
    }

    private void showRoomDialog(Room room) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
            room == null ? "Tambah Ruangan" : "Edit Ruangan", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(350, 280);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(BG_COLOR);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JTextField nameField = new JTextField(room != null ? room.getName() : "");
        JTextField locField = new JTextField(room != null ? room.getLocation() : "");
        JTextField capField = new JTextField(room != null ? String.valueOf(room.getCapacity()) : "");

        formPanel.add(createFormField("Nama Ruangan", nameField));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(createFormField("Lokasi", locField));
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(createFormField("Kapasitas", capField));
        
        dialog.add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setOpaque(false);
        btnPanel.setBorder(new EmptyBorder(0, 20, 15, 20));
        
        JButton saveBtn = createStyledButton("Simpan", PRIMARY_BLUE);
        saveBtn.addActionListener(e -> {
            try {
                String name = nameField.getText();
                String loc = locField.getText();
                int cap = Integer.parseInt(capField.getText());

                boolean success;
                if (room == null) {
                    success = roomControl.addRoom(name, loc, cap);
                } else {
                    room.setName(name);
                    room.setLocation(loc);
                    room.setCapacity(cap);
                    success = roomControl.updateRoom(room);
                }

                if (success) {
                    JOptionPane.showMessageDialog(dialog, "Berhasil disimpan!");
                    dialog.dispose();
                    refreshData();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Gagal menyimpan.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Kapasitas harus angka.");
            }
        });
        btnPanel.add(saveBtn);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    private JPanel createFormField(String label, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setOpaque(false);
        
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        panel.add(lbl, BorderLayout.NORTH);
        
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            new EmptyBorder(8, 10, 8, 10)
        ));
        panel.add(field, BorderLayout.CENTER);
        
        return panel;
    }

    private void editSelectedRoom() {
        int row = roomTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih ruangan terlebih dahulu.");
            return;
        }
        
        int id = (int) tableModel.getValueAt(row, 0);
        String name = (String) tableModel.getValueAt(row, 1);
        String loc = (String) tableModel.getValueAt(row, 2);
        int cap = (int) tableModel.getValueAt(row, 3);
        
        Room room = new Room(id, name, loc, cap);
        showRoomDialog(room);
    }

    private void deleteSelectedRoom() {
        int row = roomTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih ruangan terlebih dahulu.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus ruangan ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int id = (int) tableModel.getValueAt(row, 0);
            if (roomControl.deleteRoom(id)) {
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus.");
            }
        }
    }
}