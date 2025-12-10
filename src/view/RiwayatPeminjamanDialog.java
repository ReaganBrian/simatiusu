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
    
    /**
     * Validate data before display
     */
    private boolean validateBookingData(Booking booking) {
        if (booking == null) {
            return false;
        }
        
        return booking.getKodeRuang() != null && 
               booking.getTanggal() != null && 
               booking.getJamMulai() != null && 
               booking.getJamSelesai() != null;
    }
    
    /**
     * Handle empty data display
     */
    private void handleEmptyData() {
        tableModel.setRowCount(0);
        Object[] emptyRow = {
            "-",
            "Tidak ada data",
            "-",
            "-",
            "-",
            "-",
            "-"
        };
        tableModel.addRow(emptyRow);
    }
    
    /**
     * Handle load data error
     */
    private void handleLoadError(Exception e) {
        JOptionPane.showMessageDialog(this,
            "Terjadi kesalahan saat memuat data: " + e.getMessage(),
            "Error",
            JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
    }
    
    /**
     * Format status text for display
     */
    private String formatStatusText(String status) {
        if (status == null) {
            return "UNKNOWN";
        }
        
        switch (status.toUpperCase()) {
            case "MENUNGGU":
            case "PENDING":
                return "MENUNGGU";
            case "DISETUJUI":
            case "APPROVED":
                return "DISETUJUI";
            case "DITOLAK":
            case "REJECTED":
                return "DITOLAK";
            default:
                return status.toUpperCase();
        }
    }
    
    /**
     * Export data to string
     */
    private String exportToString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Riwayat Peminjaman - ").append(currentUser.getNamaLengkap()).append("\n");
        sb.append("=".repeat(80)).append("\n\n");
        
        List<Booking> bookings = bookingService.getUserBookings(currentUser.getId());
        
        for (int i = 0; i < bookings.size(); i++) {
            Booking b = bookings.get(i);
            sb.append(String.format("%d. Ruangan: %s\n", i + 1, b.getKodeRuang()));
            sb.append(String.format("   Tanggal: %s\n", dateFormat.format(b.getTanggal())));
            sb.append(String.format("   Waktu: %s - %s\n", 
                b.getJamMulai().toString().substring(0, 5),
                b.getJamSelesai().toString().substring(0, 5)));
            sb.append(String.format("   Dosen: %s\n", b.getNamaDosen()));
            sb.append(String.format("   Mata Kuliah: %s\n", b.getMataKuliah()));
            sb.append(String.format("   Status: %s\n\n", b.getStatus()));
        }
        
        return sb.toString();
    }
    
    /**
     * Count bookings by status
     */
    private int countByStatus(String status) {
        List<Booking> bookings = bookingService.getUserBookings(currentUser.getId());
        int count = 0;
        
        for (Booking b : bookings) {
            if (status.equals(b.getStatus())) {
                count++;
            }
        }
        
        return count;
    }
    
    /**
     * Get statistics summary
     */
    private String getStatisticsSummary() {
        int total = bookingService.getUserBookings(currentUser.getId()).size();
        int pending = countByStatus("MENUNGGU");
        int approved = countByStatus("DISETUJUI");
        int rejected = countByStatus("DITOLAK");
        
        return String.format("Total: %d | Menunggu: %d | Disetujui: %d | Ditolak: %d",
            total, pending, approved, rejected);
    }
    
    /**
     * Filter bookings by status
     */
    private void filterByStatus(String status) {
        tableModel.setRowCount(0);
        List<Booking> bookings = bookingService.getUserBookings(currentUser.getId());
        
        int no = 1;
        for (Booking booking : bookings) {
            if (status == null || status.isEmpty() || status.equals(booking.getStatus())) {
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
    
    /**
     * Check if table has data
     */
    private boolean hasData() {
        return tableModel.getRowCount() > 0;
    }
    
    /**
     * Refresh table display
     */
    private void refreshTable() {
        table.revalidate();
        table.repaint();
    }
}
