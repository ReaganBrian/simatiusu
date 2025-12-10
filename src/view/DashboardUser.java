package view;

import java.awt.*;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import model.Booking;
import model.Room;
import model.User;
import service.BookingService;
import service.RoomService;

public class DashboardUser extends JFrame {
    private User currentUser;
    private RoomService roomService;
    private BookingService bookingService;
    
    private JPanel roomGridPanel;
    private Timer refreshTimer;
    
    private static final Color COLOR_AVAILABLE = new Color(34, 139, 34); // Green
    private static final Color COLOR_OCCUPIED = new Color(220, 20, 60); // Crimson Red
    private static final Color COLOR_BG = new Color(245, 245, 245);
    
    public DashboardUser(User user) {
        this.currentUser = user;
        this.roomService = new RoomService();
        this.bookingService = new BookingService();
        
        initComponents();
        startRealtimeUpdates();
    }
    
    private void initComponents() {
        setTitle("SIMATI - Dashboard User");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(COLOR_BG);
        
        // Header panel
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Content panel with scroll
        JScrollPane scrollPane = new JScrollPane(createContentPanel());
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(34, 139, 34));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        titlePanel.setBackground(new Color(34, 139, 34));
        
        JLabel lblTitle = new JLabel("SIMATI USU");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        titlePanel.add(lblTitle);
        
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setBackground(new Color(34, 139, 34));
        
        JLabel lblUser = new JLabel("User : " + currentUser.getNamaLengkap());
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblUser.setForeground(Color.WHITE);
        userPanel.add(lblUser);
        
        JButton btnRiwayat = new JButton("Peminjaman");
        btnRiwayat.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnRiwayat.setBackground(Color.WHITE);
        btnRiwayat.setForeground(new Color(34, 139, 34));
        btnRiwayat.setFocusPainted(false);
        btnRiwayat.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRiwayat.addActionListener(e -> openRiwayatPeminjaman());
        userPanel.add(btnRiwayat);
        
        userPanel.add(Box.createHorizontalStrut(10));
        
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
    
    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(COLOR_BG);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Info panel
        JPanel infoPanel = createInfoPanel();
        contentPanel.add(infoPanel);
        contentPanel.add(Box.createVerticalStrut(20));
        
        // Teknologi Informasi section
        JLabel lblTI = new JLabel("Gedung C - Teknologi Informasi");
        lblTI.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTI.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(lblTI);
        contentPanel.add(Box.createVerticalStrut(10));
        
        JPanel tiPanel = createRoomGridPanel("TI");
        contentPanel.add(tiPanel);
        contentPanel.add(Box.createVerticalStrut(30));
        
        // Ilmu Komputer section
        JLabel lblIK = new JLabel("Gedung D - Ilmu Komputer");
        lblIK.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblIK.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(lblIK);
        contentPanel.add(Box.createVerticalStrut(10));
        
        JPanel ikPanel = createRoomGridPanel("IK");
        contentPanel.add(ikPanel);
        
        return contentPanel;
    }
    
    private JPanel createInfoPanel() {
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(34, 139, 34), 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        infoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        
        JLabel lblLegend = new JLabel("Keterangan:");
        lblLegend.setFont(new Font("Segoe UI", Font.BOLD, 14));
        infoPanel.add(lblLegend);
        
        JPanel availableBox = new JPanel();
        availableBox.setPreferredSize(new Dimension(20, 20));
        availableBox.setBackground(COLOR_AVAILABLE);
        availableBox.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        infoPanel.add(availableBox);
        
        JLabel lblAvailable = new JLabel("Tersedia");
        lblAvailable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        infoPanel.add(lblAvailable);
        
        JPanel occupiedBox = new JPanel();
        occupiedBox.setPreferredSize(new Dimension(20, 20));
        occupiedBox.setBackground(COLOR_OCCUPIED);
        occupiedBox.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        infoPanel.add(occupiedBox);
        
        JLabel lblOccupied = new JLabel("Sedang Dipakai");
        lblOccupied.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        infoPanel.add(lblOccupied);
        
        return infoPanel;
    }
    
    private JPanel createRoomGridPanel(String prodi) {
        JPanel gridPanel = new JPanel(new GridLayout(2, 3, 15, 15));
        gridPanel.setBackground(COLOR_BG);
        gridPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        
        List<Room> rooms = roomService.getRoomsByProdi(prodi);
        Map<Integer, Booking> activeBookings = roomService.getAllActiveBookings();
        Map<Integer, model.JadwalTetap> activeJadwalTetap = roomService.getAllActiveJadwalTetap();
        
        for (Room room : rooms) {
            JPanel roomPanel = createRoomPanel(room, activeBookings.get(room.getId()), activeJadwalTetap.get(room.getId()));
            gridPanel.add(roomPanel);
        }
        
        return gridPanel;
    }
    
    private JPanel createRoomPanel(Room room, Booking activeBooking, model.JadwalTetap activeJadwal) {
        // Prioritize jadwal tetap over booking
        boolean isOccupied = activeJadwal != null || activeBooking != null;
        boolean isJadwalTetap = activeJadwal != null;
        
        JPanel roomPanel = new JPanel();
        roomPanel.setLayout(new BorderLayout());
        roomPanel.setBackground(isOccupied ? COLOR_OCCUPIED : COLOR_AVAILABLE);
        roomPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        roomPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(isOccupied ? COLOR_OCCUPIED : COLOR_AVAILABLE);
        
        JLabel lblRoomCode = new JLabel(room.getKodeRuang());
        lblRoomCode.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblRoomCode.setForeground(Color.WHITE);
        lblRoomCode.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(lblRoomCode);
        
        contentPanel.add(Box.createVerticalStrut(2));
        
        if (isOccupied) {
            JLabel lblStatus = new JLabel("SEDANG DIPAKAI");
            lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lblStatus.setForeground(Color.WHITE);
            lblStatus.setAlignmentX(Component.CENTER_ALIGNMENT);
            contentPanel.add(lblStatus);
            
            contentPanel.add(Box.createVerticalStrut(5));
            
            if (isJadwalTetap) {
                // Display jadwal tetap info
                JLabel lblType = new JLabel("(Jadwal Tetap)");
                lblType.setFont(new Font("Segoe UI", Font.ITALIC, 10));
                lblType.setForeground(Color.WHITE);
                lblType.setAlignmentX(Component.CENTER_ALIGNMENT);
                contentPanel.add(lblType);
                
                contentPanel.add(Box.createVerticalStrut(3));
                
                JLabel lblDosen = new JLabel("Dosen: " + activeJadwal.getNamaDosen());
                lblDosen.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                lblDosen.setForeground(Color.WHITE);
                lblDosen.setAlignmentX(Component.CENTER_ALIGNMENT);
                contentPanel.add(lblDosen);
                
                JLabel lblMatkul = new JLabel("MK: " + activeJadwal.getMataKuliah());
                lblMatkul.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                lblMatkul.setForeground(Color.WHITE);
                lblMatkul.setAlignmentX(Component.CENTER_ALIGNMENT);
                contentPanel.add(lblMatkul);
                
                String waktu = String.format("%s - %s", 
                    activeJadwal.getJamMulai().toString().substring(0, 5),
                    activeJadwal.getJamSelesai().toString().substring(0, 5));
                JLabel lblWaktu = new JLabel("Waktu: " + waktu);
                lblWaktu.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                lblWaktu.setForeground(Color.WHITE);
                lblWaktu.setAlignmentX(Component.CENTER_ALIGNMENT);
                contentPanel.add(lblWaktu);
            } else {
                // Display booking info
                JLabel lblType = new JLabel("(Peminjaman)");
                lblType.setFont(new Font("Segoe UI", Font.ITALIC, 10));
                lblType.setForeground(Color.WHITE);
                lblType.setAlignmentX(Component.CENTER_ALIGNMENT);
                contentPanel.add(lblType);
                
                contentPanel.add(Box.createVerticalStrut(3));
                
                JLabel lblDosen = new JLabel("Dosen: " + activeBooking.getNamaDosen());
                lblDosen.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                lblDosen.setForeground(Color.WHITE);
                lblDosen.setAlignmentX(Component.CENTER_ALIGNMENT);
                contentPanel.add(lblDosen);
                
                JLabel lblMatkul = new JLabel("MK: " + activeBooking.getMataKuliah());
                lblMatkul.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                lblMatkul.setForeground(Color.WHITE);
                lblMatkul.setAlignmentX(Component.CENTER_ALIGNMENT);
                contentPanel.add(lblMatkul);
                
                String waktu = String.format("%s - %s", 
                    activeBooking.getJamMulai().toString().substring(0, 5),
                    activeBooking.getJamSelesai().toString().substring(0, 5));
                JLabel lblWaktu = new JLabel("Waktu: " + waktu);
                lblWaktu.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                lblWaktu.setForeground(Color.WHITE);
                lblWaktu.setAlignmentX(Component.CENTER_ALIGNMENT);
                contentPanel.add(lblWaktu);
            }
        } else {
            JLabel lblStatus = new JLabel("TERSEDIA");
            lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblStatus.setForeground(Color.WHITE);
            lblStatus.setAlignmentX(Component.CENTER_ALIGNMENT);
            contentPanel.add(lblStatus);
            
            contentPanel.add(Box.createVerticalStrut(5));
            
            JLabel lblInfo = new JLabel("Klik untuk mengajukan peminjaman");
            lblInfo.setFont(new Font("Segoe UI", Font.ITALIC, 10));
            lblInfo.setForeground(Color.WHITE);
            lblInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
            contentPanel.add(lblInfo);
        }
        
        roomPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Add click listener only if room is available
        if (!isOccupied) {
            roomPanel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    openBookingForm(room);
                }
            });
        }
        
        return roomPanel;
    }
    
    private void openBookingForm(Room room) {
        BookingForm bookingForm = new BookingForm(this, currentUser, room);
        bookingForm.setVisible(true);
    }
    
    private void startRealtimeUpdates() {
        // Update every 3 seconds
        refreshTimer = new Timer(3000, e -> refreshRoomStatus());
        refreshTimer.start();
    }
    
    public void refreshRoomStatus() {
        // Refresh content panel
        Container contentPane = getContentPane();
        contentPane.removeAll();
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(COLOR_BG);
        
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        JScrollPane scrollPane = new JScrollPane(createContentPanel());
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        contentPane.add(mainPanel);
        revalidate();
        repaint();
    }
    
    private void openRiwayatPeminjaman() {
        RiwayatPeminjamanDialog dialog = new RiwayatPeminjamanDialog(this, currentUser);
        dialog.setVisible(true);
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
     * Format waktu untuk display
     */
    private String formatTimeDisplay(java.sql.Time startTime, java.sql.Time endTime) {
        if (startTime == null || endTime == null) {
            return "N/A";
        }
        return String.format("%s - %s", 
            startTime.toString().substring(0, 5),
            endTime.toString().substring(0, 5));
    }
    
    /**
     * Cek apakah user dapat melakukan booking
     */
    private boolean canUserBook() {
        if (currentUser == null) {
            return false;
        }
        return !currentUser.isAdmin();
    }
    
    /**
     * Hitung total ruangan tersedia
     */
    private int countAvailableRooms() {
        int count = 0;
        List<Room> allRooms = roomService.getAllRooms();
        Map<Integer, Booking> activeBookings = roomService.getAllActiveBookings();
        Map<Integer, model.JadwalTetap> activeJadwalTetap = roomService.getAllActiveJadwalTetap();
        
        for (Room room : allRooms) {
            if (activeBookings.get(room.getId()) == null && 
                activeJadwalTetap.get(room.getId()) == null) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Hitung total ruangan yang sedang dipakai
     */
    private int countOccupiedRooms() {
        int count = 0;
        List<Room> allRooms = roomService.getAllRooms();
        Map<Integer, Booking> activeBookings = roomService.getAllActiveBookings();
        Map<Integer, model.JadwalTetap> activeJadwalTetap = roomService.getAllActiveJadwalTetap();
        
        for (Room room : allRooms) {
            if (activeBookings.get(room.getId()) != null || 
                activeJadwalTetap.get(room.getId()) != null) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Get room status text
     */
    private String getRoomStatusText(Room room, Booking booking, model.JadwalTetap jadwal) {
        if (jadwal != null) {
            return "SEDANG DIPAKAI (Jadwal Tetap)";
        } else if (booking != null) {
            return "SEDANG DIPAKAI (Peminjaman)";
        } else {
            return "TERSEDIA";
        }
    }
    
    /**
     * Validasi room panel sebelum display
     */
    private boolean validateRoomData(Room room) {
        return room != null && 
               room.getKodeRuang() != null && 
               !room.getKodeRuang().isEmpty();
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
    
    @Override
    public void dispose() {
        stopBackgroundTasks();
        super.dispose();
    }
}
