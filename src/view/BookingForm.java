package view;

import java.awt.*;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.swing.*;
import model.Room;
import model.User;
import service.BookingService;

public class BookingForm extends JDialog {
    private User currentUser;
    private Room selectedRoom;
    private BookingService bookingService;
    private DashboardUser parentDashboard;
    
    private JTextField txtNamaDosen;
    private JTextField txtMataKuliah;
    private JSpinner spinnerTanggal;
    private JSpinner spinnerJamMulai;
    private JSpinner spinnerJamSelesai;
    private JSpinner spinnerMenitMulai;
    private JSpinner spinnerMenitSelesai;
    private JTextArea txtKeterangan;
    private JButton btnSubmit;
    private JButton btnCancel;
    
    public BookingForm(DashboardUser parent, User user, Room room) {
        super(parent, "Form Pengajuan Peminjaman Ruangan", true);
        this.parentDashboard = parent;
        this.currentUser = user;
        this.selectedRoom = room;
        this.bookingService = new BookingService();
        
        initComponents();
    }
    
    private void initComponents() {
        setSize(500, 750);
        setLocationRelativeTo(getParent());
        setResizable(false);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        int yPos = 20;
        int labelHeight = 25;
        int fieldHeight = 35;
        int spacing = 75;
        
        // Title
        JLabel lblTitle = new JLabel("Pengajuan Peminjaman Ruangan");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(34, 139, 34));
        lblTitle.setBounds(80, yPos, 350, 30);
        mainPanel.add(lblTitle);
        
        yPos += 50;
        
        // Room info
        JLabel lblRoomInfo = new JLabel("Ruangan: " + selectedRoom.getKodeRuang());
        lblRoomInfo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblRoomInfo.setBounds(30, yPos, 400, labelHeight);
        mainPanel.add(lblRoomInfo);
        
        yPos += 40;
        
        // Nama Dosen
        JLabel lblDosen = new JLabel("Nama Dosen Pengajar:");
        lblDosen.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblDosen.setBounds(30, yPos, 200, labelHeight);
        mainPanel.add(lblDosen);
        
        txtNamaDosen = new JTextField();
        txtNamaDosen.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtNamaDosen.setBounds(30, yPos + 28, 420, fieldHeight);
        txtNamaDosen.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(34, 139, 34), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        mainPanel.add(txtNamaDosen);
        
        yPos += spacing;
        
        // Mata Kuliah
        JLabel lblMatkul = new JLabel("Mata Kuliah:");
        lblMatkul.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblMatkul.setBounds(30, yPos, 200, labelHeight);
        mainPanel.add(lblMatkul);
        
        txtMataKuliah = new JTextField();
        txtMataKuliah.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtMataKuliah.setBounds(30, yPos + 28, 420, fieldHeight);
        txtMataKuliah.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(34, 139, 34), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        mainPanel.add(txtMataKuliah);
        
        yPos += spacing;
        
        // Tanggal
        JLabel lblTanggal = new JLabel("Tanggal:");
        lblTanggal.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTanggal.setBounds(30, yPos, 200, labelHeight);
        mainPanel.add(lblTanggal);
        
        java.util.Date today = new java.util.Date();
        SpinnerDateModel dateModel = new SpinnerDateModel(today, today, null, java.util.Calendar.DAY_OF_MONTH);
        spinnerTanggal = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(spinnerTanggal, "dd/MM/yyyy");
        spinnerTanggal.setEditor(dateEditor);
        spinnerTanggal.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        spinnerTanggal.setBounds(30, yPos + 28, 200, fieldHeight);
        spinnerTanggal.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 34), 1));
        mainPanel.add(spinnerTanggal);
        
        yPos += spacing;
        
        // Jam Mulai
        JLabel lblJamMulai = new JLabel("Jam Mulai (HH:mm):");
        lblJamMulai.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblJamMulai.setBounds(30, yPos, 200, labelHeight);
        mainPanel.add(lblJamMulai);
        
        SpinnerNumberModel hourModelStart = new SpinnerNumberModel(8, 8, 17, 1);
        spinnerJamMulai = new JSpinner(hourModelStart);
        JSpinner.NumberEditor hourEditorStart = new JSpinner.NumberEditor(spinnerJamMulai, "00");
        spinnerJamMulai.setEditor(hourEditorStart);
        spinnerJamMulai.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        spinnerJamMulai.setBounds(30, yPos + 28, 80, fieldHeight);
        spinnerJamMulai.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 34), 1));
        mainPanel.add(spinnerJamMulai);
        
        JLabel lblColon1 = new JLabel(":");
        lblColon1.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblColon1.setBounds(115, yPos + 28, 15, fieldHeight);
        mainPanel.add(lblColon1);
        
        SpinnerNumberModel minuteModelStart = new SpinnerNumberModel(0, 0, 59, 1);
        spinnerMenitMulai = new JSpinner(minuteModelStart);
        JSpinner.NumberEditor minuteEditorStart = new JSpinner.NumberEditor(spinnerMenitMulai, "00");
        spinnerMenitMulai.setEditor(minuteEditorStart);
        spinnerMenitMulai.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        spinnerMenitMulai.setBounds(135, yPos + 28, 80, fieldHeight);
        spinnerMenitMulai.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 34), 1));
        mainPanel.add(spinnerMenitMulai);
        
        yPos += spacing;
        
        // Jam Selesai
        JLabel lblJamSelesai = new JLabel("Jam Selesai (HH:mm):");
        lblJamSelesai.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblJamSelesai.setBounds(30, yPos, 200, labelHeight);
        mainPanel.add(lblJamSelesai);
        
        SpinnerNumberModel hourModelEnd = new SpinnerNumberModel(9, 9, 17, 1);
        spinnerJamSelesai = new JSpinner(hourModelEnd);
        JSpinner.NumberEditor hourEditorEnd = new JSpinner.NumberEditor(spinnerJamSelesai, "00");
        spinnerJamSelesai.setEditor(hourEditorEnd);
        spinnerJamSelesai.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        spinnerJamSelesai.setBounds(30, yPos + 28, 80, fieldHeight);
        spinnerJamSelesai.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 34), 1));
        mainPanel.add(spinnerJamSelesai);
        
        JLabel lblColon2 = new JLabel(":");
        lblColon2.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblColon2.setBounds(115, yPos + 28, 15, fieldHeight);
        mainPanel.add(lblColon2);
        
        SpinnerNumberModel minuteModelEnd = new SpinnerNumberModel(0, 0, 59, 1);
        spinnerMenitSelesai = new JSpinner(minuteModelEnd);
        JSpinner.NumberEditor minuteEditorEnd = new JSpinner.NumberEditor(spinnerMenitSelesai, "00");
        spinnerMenitSelesai.setEditor(minuteEditorEnd);
        spinnerMenitSelesai.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        spinnerMenitSelesai.setBounds(135, yPos + 28, 80, fieldHeight);
        spinnerMenitSelesai.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 34), 1));
        mainPanel.add(spinnerMenitSelesai);
        
        yPos += spacing;
        
        // Keterangan
        JLabel lblKeterangan = new JLabel("Keterangan (opsional):");
        lblKeterangan.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblKeterangan.setBounds(30, yPos, 200, labelHeight);
        mainPanel.add(lblKeterangan);
        
        txtKeterangan = new JTextArea();
        txtKeterangan.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtKeterangan.setLineWrap(true);
        txtKeterangan.setWrapStyleWord(true);
        txtKeterangan.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(34, 139, 34), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        JScrollPane scrollKeterangan = new JScrollPane(txtKeterangan);
        scrollKeterangan.setBounds(30, yPos + 28, 420, 70);
        mainPanel.add(scrollKeterangan);
        
        yPos += 110;
        
        // Buttons
        btnCancel = new JButton("BATAL");
        btnCancel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnCancel.setBounds(30, yPos, 200, 40);
        btnCancel.setBackground(Color.RED);
        btnCancel.setForeground(Color.RED);
        btnCancel.setFocusPainted(false);
        btnCancel.setBorder(BorderFactory.createLineBorder(Color.RED));
        btnCancel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancel.addActionListener(e -> dispose());
        mainPanel.add(btnCancel);
        
        btnSubmit = new JButton("AJUKAN");
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSubmit.setBounds(250, yPos, 200, 40);
        btnSubmit.setBackground(new Color(34, 139, 34));
        btnSubmit.setForeground(new Color(34, 139, 34));
        btnSubmit.setFocusPainted(false);
        btnSubmit.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 34), 2));
        btnSubmit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSubmit.addActionListener(e -> handleSubmit());
        mainPanel.add(btnSubmit);
        
        add(mainPanel);
    }
    
    private void handleSubmit() {
        String namaDosen = txtNamaDosen.getText().trim();
        String mataKuliah = txtMataKuliah.getText().trim();
        String keterangan = txtKeterangan.getText().trim();
        
        // Get tanggal from spinner
        java.util.Date spinnerDateValue = (java.util.Date) spinnerTanggal.getValue();
        java.util.Calendar calDate = java.util.Calendar.getInstance();
        calDate.setTime(spinnerDateValue);
        
        int year = calDate.get(java.util.Calendar.YEAR);
        int month = calDate.get(java.util.Calendar.MONTH);
        int day = calDate.get(java.util.Calendar.DAY_OF_MONTH);
        
        LocalDate bookingDate = LocalDate.of(year, month + 1, day);
        Date tanggal = Date.valueOf(bookingDate);
        
        // Get jam mulai from spinner
        int startHour = (Integer) spinnerJamMulai.getValue();
        int startMinute = (Integer) spinnerMenitMulai.getValue();
        
        LocalTime startTime = LocalTime.of(startHour, startMinute);
        Time jamMulai = Time.valueOf(startTime);
        
        // Get jam selesai from spinner
        int endHour = (Integer) spinnerJamSelesai.getValue();
        int endMinute = (Integer) spinnerMenitSelesai.getValue();
        
        LocalTime endTime = LocalTime.of(endHour, endMinute);
        Time jamSelesai = Time.valueOf(endTime);
        
        try {
            boolean success = bookingService.createBooking(
                currentUser.getId(),
                selectedRoom.getId(),
                namaDosen,
                mataKuliah,
                jamMulai,
                jamSelesai,
                tanggal,
                keterangan
            );
            
            if (success) {
                JOptionPane.showMessageDialog(this,
                    "Pengajuan peminjaman berhasil!\nMenunggu persetujuan admin.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Refresh parent dashboard
                parentDashboard.refreshRoomStatus();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Gagal mengajukan peminjaman. Silakan coba lagi.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                ex.getMessage(),
                "Validation Error",
                JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Error: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
