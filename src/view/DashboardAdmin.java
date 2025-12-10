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
    
    /**
     * Approve booking dengan konfirmasi detail
     */
    private boolean approveBookingWithConfirmation(int bookingId, Booking booking) {
        if (booking == null) {
            return false;
        }
        
        String message = String.format(
            "Setujui peminjaman berikut?\n\n" +
            "Peminjam: %s\n" +
            "Ruangan: %s\n" +
            "Mata Kuliah: %s\n" +
            "Tanggal: %s\n" +
            "Waktu: %s - %s",
            booking.getNamaPeminjam(),
            booking.getKodeRuang(),
            booking.getMataKuliah(),
            booking.getTanggal(),
            booking.getJamMulai(),
            booking.getJamSelesai()
        );
        
        int confirm = JOptionPane.showConfirmDialog(this,
            message,
            "Konfirmasi Persetujuan",
            JOptionPane.YES_NO_OPTION);
        
        return confirm == JOptionPane.YES_OPTION;
    }
    
    /**
     * Reject booking dengan alasan
     */
    private boolean rejectBookingWithReason(int bookingId, Booking booking) {
        if (booking == null) {
            return false;
        }
        
        String reason = JOptionPane.showInputDialog(this,
            "Masukkan alasan penolakan (opsional):",
            "Alasan Penolakan",
            JOptionPane.QUESTION_MESSAGE);
        
        if (reason != null) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Validate booking sebelum approval
     */
    private boolean validateBeforeApproval(Booking booking) {
        if (booking == null) {
            return false;
        }
        
        if (!booking.isPending()) {
            JOptionPane.showMessageDialog(this,
                "Peminjaman ini sudah diproses!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    /**
     * Show booking details dialog
     */
    private void showBookingDetails(int bookingId) {
        Booking booking = bookingService.getBookingById(bookingId);
        if (booking == null) {
            JOptionPane.showMessageDialog(this,
                "Data peminjaman tidak ditemukan!",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String details = String.format(
            "Detail Peminjaman\n\n" +
            "ID: %d\n" +
            "Peminjam: %s\n" +
            "NIDN/NIM: %s\n" +
            "Ruangan: %s\n" +
            "Mata Kuliah: %s\n" +
            "Dosen: %s\n" +
            "Tanggal: %s\n" +
            "Jam: %s - %s\n" +
            "Status: %s\n" +
            "Keterangan: %s\n" +
            "Dibuat: %s",
            booking.getId(),
            booking.getNamaPeminjam(),
            booking.getNidnOrNim(),
            booking.getKodeRuang(),
            booking.getMataKuliah(),
            booking.getNamaDosen(),
            booking.getTanggal(),
            booking.getJamMulai(),
            booking.getJamSelesai(),
            booking.getStatus(),
            booking.getKeterangan() != null ? booking.getKeterangan() : "-",
            booking.getCreatedAt()
        );
        
        JOptionPane.showMessageDialog(this,
            details,
            "Detail Peminjaman",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Batch approve multiple bookings
     */
    private void batchApproveBookings(List<Integer> bookingIds) {
        int successCount = 0;
        int failCount = 0;
        
        for (int bookingId : bookingIds) {
            boolean success = bookingService.approveBooking(bookingId);
            if (success) {
                successCount++;
            } else {
                failCount++;
            }
        }
        
        JOptionPane.showMessageDialog(this,
            String.format("Berhasil: %d\nGagal: %d", successCount, failCount),
            "Hasil Batch Approval",
            JOptionPane.INFORMATION_MESSAGE);
        
        loadData();
    }
    
    /**
     * Check if admin can approve booking
     */
    private boolean canApproveBooking(User admin) {
        return admin != null && admin.isAdmin();
    }
    
    /**
     * Log approval activity
     */
    private void logApprovalActivity(int bookingId, String action, boolean success) {
        String logMessage = String.format(
            "[%s] Admin %s %s booking ID %d - %s",
            new java.util.Date(),
            currentUser.getNamaLengkap(),
            action,
            bookingId,
            success ? "SUCCESS" : "FAILED"
        );
        System.out.println(logMessage);
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
    
    /**
     * Hitung total peminjaman pending
     */
    private int countPendingBookings() {
        List<Booking> bookings = bookingService.getPendingBookings();
        return bookings != null ? bookings.size() : 0;
    }
    
    /**
     * Hitung total peminjaman yang disetujui
     */
    private int countApprovedBookings() {
        List<Booking> allBookings = bookingService.getAllBookings();
        int count = 0;
        for (Booking b : allBookings) {
            if ("APPROVED".equals(b.getStatus())) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Hitung total peminjaman yang ditolak
     */
    private int countRejectedBookings() {
        List<Booking> allBookings = bookingService.getAllBookings();
        int count = 0;
        for (Booking b : allBookings) {
            if ("REJECTED".equals(b.getStatus())) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Export data peminjaman ke format text
     */
    private String exportBookingData(Booking booking) {
        if (booking == null) {
            return "";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(booking.getId()).append("\n");
        sb.append("Peminjam: ").append(booking.getNamaPeminjam()).append("\n");
        sb.append("NIDN/NIM: ").append(booking.getNidnOrNim()).append("\n");
        sb.append("Ruangan: ").append(booking.getKodeRuang()).append("\n");
        sb.append("Mata Kuliah: ").append(booking.getMataKuliah()).append("\n");
        sb.append("Dosen: ").append(booking.getNamaDosen()).append("\n");
        sb.append("Tanggal: ").append(booking.getTanggal()).append("\n");
        sb.append("Waktu: ").append(booking.getJamMulai()).append(" - ").append(booking.getJamSelesai()).append("\n");
        sb.append("Status: ").append(booking.getStatus()).append("\n");
        
        return sb.toString();
    }
    
    /**
     * Validasi booking sebelum approve
     */
    private boolean validateBookingForApproval(int bookingId) {
        if (bookingId <= 0) {
            return false;
        }
        
        List<Booking> bookings = bookingService.getPendingBookings();
        for (Booking b : bookings) {
            if (b.getId() == bookingId) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Refresh table data
     */
    private void refreshTables() {
        SwingUtilities.invokeLater(() -> {
            loadData();
        });
    }
    
    /**
     * Filter booking by status
     */
    private List<Booking> filterBookingsByStatus(String status) {
        List<Booking> allBookings = bookingService.getAllBookings();
        List<Booking> filtered = new java.util.ArrayList<>();
        
        for (Booking b : allBookings) {
            if (status.equals(b.getStatus())) {
                filtered.add(b);
            }
        }
        return filtered;
    }
    
    /**
     * Search booking by keyword
     */
    private List<Booking> searchBookings(String keyword) {
        List<Booking> allBookings = bookingService.getAllBookings();
        List<Booking> results = new java.util.ArrayList<>();
        
        if (keyword == null || keyword.trim().isEmpty()) {
            return allBookings;
        }
        
        String lowerKeyword = keyword.toLowerCase();
        for (Booking b : allBookings) {
            if (b.getNamaPeminjam().toLowerCase().contains(lowerKeyword) ||
                b.getNidnOrNim().toLowerCase().contains(lowerKeyword) ||
                b.getKodeRuang().toLowerCase().contains(lowerKeyword) ||
                b.getMataKuliah().toLowerCase().contains(lowerKeyword) ||
                b.getNamaDosen().toLowerCase().contains(lowerKeyword)) {
                results.add(b);
            }
        }
        return results;
    }
    
    /**
     * Format status untuk display
     */
    private String formatStatusDisplay(String status) {
        if (status == null) {
            return "N/A";
        }
        
        switch (status) {
            case "PENDING":
                return "Menunggu Persetujuan";
            case "APPROVED":
                return "Disetujui";
            case "REJECTED":
                return "Ditolak";
            case "COMPLETED":
                return "Selesai";
            default:
                return status;
        }
    }
    
    /**
     * Stop semua background task
     */
    private void stopBackgroundTasks() {
        if (refreshTimer != null && refreshTimer.isRunning()) {
            refreshTimer.stop();
        }
    }
    
    /**
     * Resume background task
     */
    private void resumeBackgroundTasks() {
        if (refreshTimer != null && !refreshTimer.isRunning()) {
            refreshTimer.start();
        }
    }
    
    /**
     * Get booking detail by ID
     */
    private Booking getBookingById(int bookingId) {
        List<Booking> allBookings = bookingService.getAllBookings();
        for (Booking b : allBookings) {
            if (b.getId() == bookingId) {
                return b;
            }
        }
        return null;
    }
    
    @Override
    public void dispose() {
        stopBackgroundTasks();
        super.dispose();
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
