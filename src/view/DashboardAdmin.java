package view;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.Booking;
import model.User;
import service.BookingService;

public class DashboardAdmin extends JFrame {
    private User currentUser;
    private BookingService bookingService;
    
    private JTable tablePending;
    private JTable tableHistory;
    private DefaultTableModel modelPending;
    private DefaultTableModel modelHistory;
    private Timer refreshTimer;
    
    private static final Color COLOR_GREEN = new Color(34, 139, 34);
    private static final Color COLOR_BG = new Color(245, 245, 245);
    
    public DashboardAdmin(User user) {
        this.currentUser = user;
        this.bookingService = new BookingService();
        
        initComponents();
        loadData();
        startRealtimeUpdates();
    }
    
    private void initComponents() {
        setTitle("SIMATI Admin");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 800);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(COLOR_BG);
        
        // Header
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Content with tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // Verification tab
        JPanel verificationPanel = createVerificationPanel();
        tabbedPane.addTab("Verifikasi Peminjaman", verificationPanel);
        
        // History tab
        JPanel historyPanel = createHistoryPanel();
        tabbedPane.addTab("Riwayat Peminjaman", historyPanel);
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(COLOR_GREEN);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(COLOR_GREEN);
        
        JLabel lblTitle = new JLabel("SIMATI - Admin Dashboard");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        titlePanel.add(lblTitle);
        
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setBackground(COLOR_GREEN);
        
        JLabel lblUser = new JLabel(currentUser.getNamaLengkap());
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblUser.setForeground(Color.WHITE);
        userPanel.add(lblUser);
        
        JButton btnLogout = new JButton("Logout");
        btnLogout.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogout.setBackground(Color.RED);
        btnLogout.setForeground(Color.RED);
        btnLogout.setFocusPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.addActionListener(e -> logout());
        userPanel.add(btnLogout);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(userPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createVerificationPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitle = new JLabel("Peminjaman Menunggu Persetujuan");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(COLOR_GREEN);
        panel.add(lblTitle, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"ID", "Peminjam", "NIDN/NIM", "Ruangan", "Mata Kuliah", 
                           "Dosen", "Tanggal", "Jam Mulai", "Jam Selesai", "Aksi"};
        modelPending = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 9; // Only action column editable
            }
        };
        
        tablePending = new JTable(modelPending);
        tablePending.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablePending.setRowHeight(60);
        tablePending.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablePending.getTableHeader().setBackground(COLOR_GREEN);
        tablePending.getTableHeader().setForeground(COLOR_GREEN);
        
        // Center align
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < columns.length - 1; i++) {
            tablePending.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Button renderer for action column
        tablePending.getColumnModel().getColumn(9).setCellRenderer(new ButtonRenderer());
        tablePending.getColumnModel().getColumn(9).setCellEditor(new ButtonEditor(new JCheckBox(), this));
        
        JScrollPane scrollPane = new JScrollPane(tablePending);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitle = new JLabel("Riwayat Peminjaman");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(COLOR_GREEN);
        panel.add(lblTitle, BorderLayout.NORTH);
        
        // Table
        String[] columns = {"ID", "Peminjam", "NIDN/NIM", "Ruangan", "Mata Kuliah", 
                           "Dosen", "Tanggal", "Jam Mulai", "Jam Selesai", "Status", "Keterangan"};
        modelHistory = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableHistory = new JTable(modelHistory);
        tableHistory.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableHistory.setRowHeight(50);
        tableHistory.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tableHistory.getTableHeader().setBackground(COLOR_GREEN);
        tableHistory.getTableHeader().setForeground(new Color(34, 139, 34));
        
        // Center align
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < columns.length; i++) {
            tableHistory.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        JScrollPane scrollPane = new JScrollPane(tableHistory);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void loadData() {
        loadPendingBookings();
        loadHistory();
    }
    
    private void loadPendingBookings() {
        modelPending.setRowCount(0);
        List<Booking> bookings = bookingService.getPendingBookings();
        
        for (Booking b : bookings) {
            Object[] row = {
                b.getId(),
                b.getNamaPeminjam(),
                b.getNidnOrNim(),
                b.getKodeRuang(),
                b.getMataKuliah(),
                b.getNamaDosen(),
                b.getTanggal().toString(),
                b.getJamMulai().toString().substring(0, 5),
                b.getJamSelesai().toString().substring(0, 5),
                b.getId() // For action buttons
            };
            modelPending.addRow(row);
        }
    }
    
    private void loadHistory() {
        modelHistory.setRowCount(0);
        List<Booking> bookings = bookingService.getAllBookings();
        
        for (Booking b : bookings) {
            Object[] row = {
                b.getId(),
                b.getNamaPeminjam(),
                b.getNidnOrNim(),
                b.getKodeRuang(),
                b.getMataKuliah(),
                b.getNamaDosen(),
                b.getTanggal().toString(),
                b.getJamMulai().toString().substring(0, 5),
                b.getJamSelesai().toString().substring(0, 5),
                b.getStatus(),
                b.getKeterangan() != null ? b.getKeterangan() : ""
            };
            modelHistory.addRow(row);
        }
    }
    
    public void approveBooking(int bookingId) {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Setujui peminjaman ini?",
            "Konfirmasi",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = bookingService.approveBooking(bookingId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Peminjaman disetujui!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menyetujui peminjaman!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public void rejectBooking(int bookingId) {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Tolak peminjaman ini?",
            "Konfirmasi",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = bookingService.rejectBooking(bookingId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Peminjaman ditolak!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menolak peminjaman!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void startRealtimeUpdates() {
        // Update every 5 seconds
        refreshTimer = new Timer(5000, e -> loadData());
        refreshTimer.start();
    }
    
    private void logout() {
        if (refreshTimer != null) {
            refreshTimer.stop();
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Apakah Anda yakin ingin logout?",
            "Konfirmasi Logout",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame().setVisible(true);
        }
    }
    
    // Button Renderer for table
    class ButtonRenderer extends JPanel implements javax.swing.table.TableCellRenderer {
        private JButton btnApprove;
        private JButton btnReject;
        
        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));
            setBackground(Color.WHITE);
            
            btnApprove = new JButton("SETUJUI");
            btnApprove.setFont(new Font("Segoe UI", Font.BOLD, 11));
            btnApprove.setBackground(COLOR_GREEN);
            btnApprove.setForeground(Color.GREEN);
            btnApprove.setFocusPainted(false);
            btnApprove.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            
            btnReject = new JButton("TOLAK");
            btnReject.setFont(new Font("Segoe UI", Font.BOLD, 11));
            btnReject.setBackground(new Color(220, 20, 60));
            btnReject.setForeground(Color.RED);
            btnReject.setFocusPainted(false);
            btnReject.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            
            add(btnApprove);
            add(btnReject);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }
    
    // Button Editor for table
    class ButtonEditor extends DefaultCellEditor {
        private JPanel panel;
        private JButton btnApprove;
        private JButton btnReject;
        private int bookingId;
        private DashboardAdmin dashboard;
        
        public ButtonEditor(JCheckBox checkBox, DashboardAdmin dashboard) {
            super(checkBox);
            this.dashboard = dashboard;
            
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
            panel.setBackground(Color.WHITE);
            
            btnApprove = new JButton("SETUJUI");
            btnApprove.setFont(new Font("Segoe UI", Font.BOLD, 11));
            btnApprove.setBackground(COLOR_GREEN);
            btnApprove.setForeground(Color.GREEN);
            btnApprove.setFocusPainted(false);
            btnApprove.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            btnApprove.addActionListener(e -> {
                dashboard.approveBooking(bookingId);
                fireEditingStopped();
            });
            
            btnReject = new JButton("TOLAK");
            btnReject.setFont(new Font("Segoe UI", Font.BOLD, 11));
            btnReject.setBackground(new Color(220, 20, 60));
            btnReject.setForeground(Color.RED);
            btnReject.setFocusPainted(false);
            btnReject.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            btnReject.addActionListener(e -> {
                dashboard.rejectBooking(bookingId);
                fireEditingStopped();
            });
            
            panel.add(btnApprove);
            panel.add(btnReject);
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            bookingId = (int) value;
            return panel;
        }
        
        @Override
        public Object getCellEditorValue() {
            return bookingId;
        }
    }
}
