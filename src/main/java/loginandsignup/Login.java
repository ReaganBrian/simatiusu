package loginandsignup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Login form with modern responsive design
 */
public class Login extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox rememberMeCheckbox;
    private JButton loginButton;
    private JButton signUpButton;
    private JLabel statusLabel;

    public Login() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Login - Simatiusu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(450, 550));
        setPreferredSize(new Dimension(450, 550));
        
        // Main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(new Color(240, 240, 240));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // Title
        JLabel titleLabel = new JLabel("Welcome Back!", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(51, 51, 51));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Please login to continue", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(102, 102, 102));
        gbc.gridy = 1;
        mainPanel.add(subtitleLabel, gbc);
        
        // Username label
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(usernameLabel, gbc);
        
        // Username field
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setPreferredSize(new Dimension(300, 35));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        mainPanel.add(usernameField, gbc);
        
        // Password label
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        mainPanel.add(passwordLabel, gbc);
        
        // Password field
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(300, 35));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        mainPanel.add(passwordField, gbc);
        
        // Remember me checkbox
        rememberMeCheckbox = new JCheckBox("Remember Me");
        rememberMeCheckbox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        rememberMeCheckbox.setBackground(new Color(240, 240, 240));
        gbc.gridy = 6;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(rememberMeCheckbox, gbc);
        
        // Login button
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginButton.setPreferredSize(new Dimension(300, 45));
        loginButton.setBackground(new Color(76, 175, 80));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(e -> handleLogin());
        gbc.gridy = 7;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(loginButton, gbc);
        
        // Status label for error messages
        statusLabel = new JLabel("", JLabel.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(Color.RED);
        gbc.gridy = 8;
        mainPanel.add(statusLabel, gbc);
        
        // Sign up label and button panel
        JPanel signUpPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        signUpPanel.setBackground(new Color(240, 240, 240));
        
        JLabel signUpLabel = new JLabel("Don't have an account?");
        signUpLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        signUpPanel.add(signUpLabel);
        
        signUpButton = new JButton("Sign Up");
        signUpButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        signUpButton.setForeground(new Color(33, 150, 243));
        signUpButton.setBorderPainted(false);
        signUpButton.setContentAreaFilled(false);
        signUpButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signUpButton.setFocusPainted(false);
        signUpButton.addActionListener(e -> openSignUp());
        signUpPanel.add(signUpButton);
        
        gbc.gridy = 9;
        gbc.insets = new Insets(20, 10, 10, 10);
        mainPanel.add(signUpPanel, gbc);
        
        // Add main panel to frame
        add(mainPanel);
        
        // Add Enter key listener to password field
        passwordField.addActionListener(e -> handleLogin());
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        // Validation
        if (username.isEmpty() || password.isEmpty()) {
            showError("Please fill in all fields!");
            return;
        }
        
        // Validate login with database
        User user = DatabaseConnection.validateLogin(username, password);
        
        if (user != null) {
            // Login successful
            showSuccess("Login successful! Welcome " + user.getFullName());
            
            // Here you can navigate to the main application window
            // For now, we'll just show a success message
            JOptionPane.showMessageDialog(this,
                "Login successful!\nWelcome, " + user.getFullName() + "!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Close login window
            dispose();
            
        } else {
            // Login failed
            showError("Invalid username or password!");
            passwordField.setText("");
        }
    }

    private void openSignUp() {
        // Open sign up window
        SignUp signUpFrame = new SignUp();
        signUpFrame.setVisible(true);
        signUpFrame.pack();
        signUpFrame.setLocationRelativeTo(null);
        
        // Close login window
        dispose();
    }

    private void showError(String message) {
        statusLabel.setText(message);
        statusLabel.setForeground(Color.RED);
    }

    private void showSuccess(String message) {
        statusLabel.setText(message);
        statusLabel.setForeground(new Color(76, 175, 80));
    }
}
