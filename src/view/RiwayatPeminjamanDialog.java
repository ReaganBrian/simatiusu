package view;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.Booking;
import model.User;
import service.BookingService;

public class RiwayatPeminjamanDialog extends JDialog {
    private User currentUser;
    private BookingService bookingService;
    private JTable table;
    private DefaultTableModel tableModel;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    
    public RiwayatPeminjamanDialog(JFrame parent, User user) {
        super(parent, "Daftar Peminjaman Anda", true);
        this.currentUser = user;
        this.bookingService = new BookingService();
        
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setSize(1000, 600);
        setLocationRelativeTo(getParent());
        setResizable(true);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel lblTitle = new JLabel("Daftar Peminjaman Anda");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(new Color(34, 139, 34));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(lblTitle, BorderLayout.NORTH);
        
        // Table
        String[] columnNames = {"No", "Ruangan", "Tanggal", "Waktu", "Dosen", "Mata Kuliah", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(30);
        table.setBackground(new Color(240, 255, 240)); 
        table.setForeground(Color.BLACK);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(34, 139, 34));
        table.getTableHeader().setForeground(new Color(34, 139, 34));
        table.setSelectionBackground(new Color(34, 139, 34));
        table.setSelectionForeground(Color.WHITE);
        
        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(40);  // No
        table.getColumnModel().getColumn(1).setPreferredWidth(80);  // Ruangan
        table.getColumnModel().getColumn(2).setPreferredWidth(90);  // Tanggal
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // Jam
        table.getColumnModel().getColumn(4).setPreferredWidth(150); // Dosen
        table.getColumnModel().getColumn(5).setPreferredWidth(180); // Mata Kuliah
        table.getColumnModel().getColumn(6).setPreferredWidth(100); // Status
        
        // Custom cell renderer for status column
        table.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                String status = value.toString();
                setHorizontalAlignment(SwingConstants.CENTER);
                setFont(new Font("Segoe UI", Font.BOLD, 12));
                
                if (!isSelected) {
                    switch (status) {
                        case "MENUNGGU":
                            c.setBackground(new Color(255, 165, 0)); // Orange
                            c.setForeground(Color.WHITE);
                            break;
                        case "DISETUJUI":
                            c.setBackground(new Color(34, 139, 34)); // Green
                            c.setForeground(Color.WHITE);
                            break;
                        case "DITOLAK":
                            c.setBackground(new Color(220, 20, 60)); // Red
                            c.setForeground(Color.WHITE);
                            break;
                        default:
                            c.setBackground(Color.WHITE);
                            c.setForeground(Color.BLACK);
                    }
                }
                
                return c;
            }
        });
        
        // Center align for specific columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.CENTER);
                if (!isSelected) {
                    c.setBackground(new Color(240, 255, 240));
                    c.setForeground(Color.BLACK);
                } else {
                    c.setBackground(new Color(34, 139, 34));
                    c.setForeground(Color.WHITE);
                }
                return c;
            }
        };
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        
        // Left align for text columns
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.LEFT);
                if (!isSelected) {
                    c.setBackground(new Color(240, 255, 240)); // Light green
                    c.setForeground(Color.BLACK);
                } else {
                    c.setBackground(new Color(34, 139, 34));
                    c.setForeground(Color.WHITE);
                }
                return c;
            }
        };
        table.getColumnModel().getColumn(4).setCellRenderer(leftRenderer);
        table.getColumnModel().getColumn(5).setCellRenderer(leftRenderer);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 34), 2));
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Legend panel
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        legendPanel.setBackground(new Color(245, 255, 245));
        
        JLabel lblLegend = new JLabel("Keterangan Status:");
        lblLegend.setFont(new Font("Segoe UI", Font.BOLD, 13));
        legendPanel.add(lblLegend);
        
        addLegendItem(legendPanel, "MENUNGGU", new Color(255, 165, 0), "Menunggu Persetujuan");
        addLegendItem(legendPanel, "DISETUJUI", new Color(34, 139, 34), "Disetujui");
        addLegendItem(legendPanel, "DITOLAK", new Color(220, 20, 60), "Ditolak");
        
        // Bottom panel with legend and button
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(new Color(245, 255, 245));
        bottomPanel.add(legendPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(245, 255, 245));
        
        JButton btnRefresh = new JButton("Refresh");
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRefresh.setBackground(new Color(34, 139, 34));
        btnRefresh.setForeground(Color.BLACK);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.addActionListener(e -> loadData());
        buttonPanel.add(btnRefresh);
        
        JButton btnClose = new JButton("Tutup");
        btnClose.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnClose.setBackground(Color.RED);
        btnClose.setForeground(Color.RED);
        btnClose.setFocusPainted(false);

        
        btnClose.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

        btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnClose.addActionListener(e -> dispose());
        buttonPanel.add(btnClose);
        
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private void addLegendItem(JPanel panel, String text, Color color, String description) {
        JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        itemPanel.setBackground(new Color(245, 255, 245));
        
        JLabel colorBox = new JLabel("  " + text + "  ");
        colorBox.setOpaque(true);
        colorBox.setBackground(color);
        colorBox.setForeground(Color.WHITE);
        colorBox.setFont(new Font("Segoe UI", Font.BOLD, 11));
        colorBox.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        itemPanel.add(colorBox);
        
        JLabel lblDesc = new JLabel("(" + description + ")");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        itemPanel.add(lblDesc);
        
        panel.add(itemPanel);
    }
    
    private void loadData() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        // Get user's bookings
        List<Booking> bookings = bookingService.getUserBookings(currentUser.getId());
        
        if (bookings.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Anda belum memiliki riwayat peminjaman.",
                "Info",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Add data to table
        int no = 1;
        for (Booking booking : bookings) {
            String tanggal = dateFormat.format(booking.getTanggal());
            String jam = String.format("%s - %s",
                booking.getJamMulai().toString().substring(0, 5),
                booking.getJamSelesai().toString().substring(0, 5));
            
            Object[] row = {
                no++,
                booking.getKodeRuang(),
                tanggal,
                jam,
                booking.getNamaDosen(),
                booking.getMataKuliah(),
                booking.getStatus()
            };
            
            tableModel.addRow(row);
        }
    }
}
