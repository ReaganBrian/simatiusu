package view;

import java.awt.*;
import javax.swing.*;
import service.AuthService;

public class RegisterFrame extends JFrame {
    private AuthService authService;
    
    private JTextField txtNamaLengkap;
    private JTextField txtNidnOrNim;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JButton btnRegister;
    private JButton btnBackToLogin;
    
    public RegisterFrame() {
        authService = new AuthService();
        initComponents();
    }
    
    private void initComponents() {
        setTitle("SIMATI - Register");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                Color color1 = new Color(34, 139, 34); 
                Color color2 = new Color(255, 255, 255); 
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setLayout(null);
        
        // Title
        JLabel lblTitle = new JLabel("REGISTER");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(180, 30, 200, 40);
        mainPanel.add(lblTitle);
        
        // Register panel
        JPanel registerPanel = new JPanel();
        registerPanel.setBackground(new Color(255, 255, 255, 230));
        registerPanel.setBounds(50, 90, 400, 450);
        registerPanel.setLayout(null);
        registerPanel.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 34), 2));
        
        int yPos = 15;
        int fieldHeight = 40;
        int spacing = 75;
        
        // Nama Lengkap
        JLabel lblNama = new JLabel("Nama Lengkap:");
        lblNama.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblNama.setBounds(30, yPos, 150, 25);
        registerPanel.add(lblNama);
        
        txtNamaLengkap = new JTextField();
        txtNamaLengkap.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtNamaLengkap.setBounds(30, yPos + 30, 340, fieldHeight);
        txtNamaLengkap.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(34, 139, 34), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        registerPanel.add(txtNamaLengkap);
        
        yPos += spacing;
        
        // NIDN/NIM
        JLabel lblNidn = new JLabel("NIDN / NIM:");
        lblNidn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblNidn.setBounds(30, yPos, 150, 25);
        registerPanel.add(lblNidn);
        
        txtNidnOrNim = new JTextField();
        txtNidnOrNim.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtNidnOrNim.setBounds(30, yPos + 30, 340, fieldHeight);
        txtNidnOrNim.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(34, 139, 34), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        registerPanel.add(txtNidnOrNim);
        
        yPos += spacing;
        
        // Email
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblEmail.setBounds(30, yPos, 150, 25);
        registerPanel.add(lblEmail);
        
        txtEmail = new JTextField();
        txtEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtEmail.setBounds(30, yPos + 30, 340, fieldHeight);
        txtEmail.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(34, 139, 34), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        registerPanel.add(txtEmail);
        
        yPos += spacing;
        
        // Password
        JLabel lblPassword = new JLabel("Password (min 8 karakter):");
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblPassword.setBounds(30, yPos, 200, 25);
        registerPanel.add(lblPassword);
        
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setBounds(30, yPos + 30, 340, fieldHeight);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(34, 139, 34), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        registerPanel.add(txtPassword);
        
        yPos += spacing;
        
        // Confirm Password
        JLabel lblConfirm = new JLabel("Konfirmasi Password:");
        lblConfirm.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblConfirm.setBounds(30, yPos, 200, 25);
        registerPanel.add(lblConfirm);
        
        txtConfirmPassword = new JPasswordField();
        txtConfirmPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtConfirmPassword.setBounds(30, yPos + 30, 340, fieldHeight);
        txtConfirmPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(34, 139, 34), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        registerPanel.add(txtConfirmPassword);
        
        yPos += spacing + 10;
        
        // Register button
        btnRegister = new JButton("REGISTER");
        btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRegister.setBounds(210, yPos, 160, 35);
        btnRegister.setBackground(new Color(34, 139, 34));
        btnRegister.setForeground(new Color(34, 139, 34));
        btnRegister.setFocusPainted(false);
        btnRegister.setBorder(BorderFactory.createEmptyBorder());
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegister.addActionListener(e -> handleRegister());
        registerPanel.add(btnRegister);
        
        // Back to Login button
        btnBackToLogin = new JButton("Kembali ke Login");
        btnBackToLogin.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBackToLogin.setBounds(30, yPos, 160, 35);
        btnBackToLogin.setBackground(new Color(34, 139, 34));
        btnBackToLogin.setForeground(Color.BLACK);
        btnBackToLogin.setFocusPainted(false);
        btnBackToLogin.setBorder(BorderFactory.createEmptyBorder());
        btnBackToLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBackToLogin.addActionListener(e -> backToLogin());
        registerPanel.add(btnBackToLogin);
        
        mainPanel.add(registerPanel);
        add(mainPanel);
    }
    
    private void handleRegister() {
        String namaLengkap = txtNamaLengkap.getText().trim();
        String nidnOrNim = txtNidnOrNim.getText().trim();
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());
        
        try {
            boolean success = authService.register(namaLengkap, nidnOrNim, email, password, confirmPassword);
            
            if (success) {
                JOptionPane.showMessageDialog(this, 
                    "Registrasi berhasil! Silakan login.", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                backToLogin();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Registrasi gagal! Silakan coba lagi.", 
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
        }
    }
    
    private void backToLogin() {
        new LoginFrame().setVisible(true);
        dispose();
    }
    
    private void clearForm() {
        txtNamaLengkap.setText("");
        txtNidnOrNim.setText("");
        txtEmail.setText("");
        txtPassword.setText("");
        txtConfirmPassword.setText("");
        txtNamaLengkap.requestFocus();
    }
    
    private boolean validateFields() {
        if (txtNamaLengkap.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Nama lengkap harus diisi!", 
                "Validasi Error", 
                JOptionPane.WARNING_MESSAGE);
            txtNamaLengkap.requestFocus();
            return false;
        }
        
        if (txtNidnOrNim.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "NIDN/NIM harus diisi!", 
                "Validasi Error", 
                JOptionPane.WARNING_MESSAGE);
            txtNidnOrNim.requestFocus();
            return false;
        }
        
        if (txtEmail.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Email harus diisi!", 
                "Validasi Error", 
                JOptionPane.WARNING_MESSAGE);
            txtEmail.requestFocus();
            return false;
        }
        
        String password = new String(txtPassword.getPassword());
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Password harus diisi!", 
                "Validasi Error", 
                JOptionPane.WARNING_MESSAGE);
            txtPassword.requestFocus();
            return false;
        }
        
        String confirmPassword = new String(txtConfirmPassword.getPassword());
        if (confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Konfirmasi password harus diisi!", 
                "Validasi Error", 
                JOptionPane.WARNING_MESSAGE);
            txtConfirmPassword.requestFocus();
            return false;
        }
        
        return true;
    }
}
