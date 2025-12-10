package view;

import model.User;
import service.AuthService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {
    private AuthService authService;
    
    private JTextField txtIdentifier;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnRegister;
    
    public LoginFrame() {
        authService = new AuthService();
        initComponents();
    }
    
    private void initComponents() {
        setTitle("SIMATI - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 400);
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
                Color color1 = new Color(34, 139, 34); // Forest Green
                Color color2 = new Color(255, 255, 255); // White
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setLayout(null);
        
        // Title
        JLabel lblTitle = new JLabel("SIMATI");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(170, 30, 150, 40);
        mainPanel.add(lblTitle);
        
        JLabel lblSubtitle = new JLabel("Sistem Manajemen Peminjaman Ruang Kelas");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitle.setForeground(Color.WHITE);
        lblSubtitle.setBounds(90, 70, 300, 20);
        mainPanel.add(lblSubtitle);
        
        // Login panel
        JPanel loginPanel = new JPanel();
        loginPanel.setBackground(new Color(255, 255, 255, 230));
        loginPanel.setBounds(50, 110, 350, 220);
        loginPanel.setLayout(null);
        loginPanel.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 34), 2));
        
        // Identifier label and field
        JLabel lblIdentifier = new JLabel("Email / NIDN / NIM:");
        lblIdentifier.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblIdentifier.setBounds(30, 30, 150, 25);
        loginPanel.add(lblIdentifier);
        
        txtIdentifier = new JTextField();
        txtIdentifier.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtIdentifier.setBounds(30, 60, 290, 35);
        txtIdentifier.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(34, 139, 34), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        loginPanel.add(txtIdentifier);
        
        // Password label and field
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblPassword.setBounds(30, 105, 100, 25);
        loginPanel.add(lblPassword);
        
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setBounds(30, 135, 290, 35);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(34, 139, 34), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        loginPanel.add(txtPassword);
        
        // Login button
        btnLogin = new JButton("LOGIN");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setBounds(180, 180, 140, 30);
        btnLogin.setBackground(Color.GREEN);
        btnLogin.setForeground(new Color(34, 139, 34));
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 34), 1));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.addActionListener(e -> handleLogin());
        loginPanel.add(btnLogin);
        
        // Register button
        btnRegister = new JButton("REGISTER");
        btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRegister.setBounds(30, 180, 140, 30);
        btnRegister.setBackground(Color.YELLOW);
        btnRegister.setForeground(Color.BLACK);
        btnRegister.setFocusPainted(false);
        btnRegister.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 34), 1));
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegister.addActionListener(e -> openRegisterFrame());
        loginPanel.add(btnRegister);
        
        mainPanel.add(loginPanel);
        add(mainPanel);
        
        // Enter key listener
        txtPassword.addActionListener(e -> handleLogin());
    }
    
    private void handleLogin() {
        String identifier = txtIdentifier.getText().trim();
        String password = new String(txtPassword.getPassword());
        
        if (identifier.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Email/NIDN/NIM dan password harus diisi!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            User user = authService.login(identifier, password);
            
            if (user != null) {
                // Login success
                dispose();
                
                if (user.isAdmin()) {
                    new DashboardAdmin(user).setVisible(true);
                } else {
                    new DashboardUser(user).setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Login gagal! Email/NIDN/NIM atau password salah.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                txtPassword.setText("");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void openRegisterFrame() {
        new RegisterFrame().setVisible(true);
        dispose();
    }
    
    private void clearForm() {
        txtIdentifier.setText("");
        txtPassword.setText("");
        txtIdentifier.requestFocus();
    }
    
    private boolean validateInput() {
        String identifier = txtIdentifier.getText().trim();
        String password = new String(txtPassword.getPassword());
        
        if (identifier.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Email/NIDN/NIM harus diisi!", 
                "Validasi Error", 
                JOptionPane.WARNING_MESSAGE);
            txtIdentifier.requestFocus();
            return false;
        }
        
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Password harus diisi!", 
                "Validasi Error", 
                JOptionPane.WARNING_MESSAGE);
            txtPassword.requestFocus();
            return false;
        }
        
        return true;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new LoginFrame().setVisible(true);
        });
    }
}
