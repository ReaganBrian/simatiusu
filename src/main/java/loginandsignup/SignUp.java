package loginandsignup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.Pattern;

/**
 * Sign Up form with modern responsive design and validation
 */
public class SignUp extends JFrame {
    private JTextField fullNameField;
    private JTextField emailField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton signUpButton;
    private JButton loginButton;
    private JLabel statusLabel;

    // Email validation pattern
    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public SignUp() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Sign Up - Simatiusu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(500, 700));
        setPreferredSize(new Dimension(500, 700));
        
        // Main panel with padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBackground(new Color(240, 240, 240));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 10, 8, 10);
        
        // Title
        JLabel titleLabel = new JLabel("Create Account", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(51, 51, 51));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Sign up to get started", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(102, 102, 102));
        gbc.gridy = 1;
        mainPanel.add(subtitleLabel, gbc);
        
        // Full Name label
        JLabel fullNameLabel = new JLabel("Full Name:");
        fullNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(fullNameLabel, gbc);
        
        // Full Name field
        fullNameField = new JTextField(20);
        fullNameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        fullNameField.setPreferredSize(new Dimension(350, 35));
        fullNameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        mainPanel.add(fullNameField, gbc);
        
        // Email label
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        mainPanel.add(emailLabel, gbc);
        
        // Email field
        emailField = new JTextField(20);
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        emailField.setPreferredSize(new Dimension(350, 35));
        emailField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        mainPanel.add(emailField, gbc);
        
        // Username label
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridy = 6;
        gbc.gridwidth = 1;
        mainPanel.add(usernameLabel, gbc);
        
        // Username field
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setPreferredSize(new Dimension(350, 35));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        mainPanel.add(usernameField, gbc);
        
        // Password label
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridy = 8;
        gbc.gridwidth = 1;
        mainPanel.add(passwordLabel, gbc);
        
        // Password field
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(350, 35));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        mainPanel.add(passwordField, gbc);
        
        // Confirm Password label
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridy = 10;
        gbc.gridwidth = 1;
        mainPanel.add(confirmPasswordLabel, gbc);
        
        // Confirm Password field
        confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        confirmPasswordField.setPreferredSize(new Dimension(350, 35));
        confirmPasswordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        gbc.gridy = 11;
        gbc.gridwidth = 2;
        mainPanel.add(confirmPasswordField, gbc);
        
        // Sign Up button
        signUpButton = new JButton("Sign Up");
        signUpButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        signUpButton.setPreferredSize(new Dimension(350, 45));
        signUpButton.setBackground(new Color(33, 150, 243));
        signUpButton.setForeground(Color.WHITE);
        signUpButton.setFocusPainted(false);
        signUpButton.setBorderPainted(false);
        signUpButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signUpButton.addActionListener(e -> handleSignUp());
        gbc.gridy = 12;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(15, 10, 8, 10);
        mainPanel.add(signUpButton, gbc);
        
        // Status label for error/success messages
        statusLabel = new JLabel("", JLabel.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(Color.RED);
        gbc.gridy = 13;
        gbc.insets = new Insets(8, 10, 8, 10);
        mainPanel.add(statusLabel, gbc);
        
        // Login label and button panel
        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        loginPanel.setBackground(new Color(240, 240, 240));
        
        JLabel loginLabel = new JLabel("Already have an account?");
        loginLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        loginPanel.add(loginLabel);
        
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        loginButton.setForeground(new Color(76, 175, 80));
        loginButton.setBorderPainted(false);
        loginButton.setContentAreaFilled(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(e -> openLogin());
        loginPanel.add(loginButton);
        
        gbc.gridy = 14;
        gbc.insets = new Insets(10, 10, 10, 10);
        mainPanel.add(loginPanel, gbc);
        
        // Add main panel to frame
        add(mainPanel);
        
        // Add Enter key listener to confirm password field
        confirmPasswordField.addActionListener(e -> handleSignUp());
    }

    private void handleSignUp() {
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        
        // Validation
        if (fullName.isEmpty() || email.isEmpty() || username.isEmpty() || 
            password.isEmpty() || confirmPassword.isEmpty()) {
            showError("All fields are required!");
            return;
        }
        
        // Validate email format
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            showError("Please enter a valid email address!");
            emailField.requestFocus();
            return;
        }
        
        // Validate password length
        if (password.length() < 6) {
            showError("Password must be at least 6 characters!");
            passwordField.requestFocus();
            return;
        }
        
        // Validate password match
        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match!");
            confirmPasswordField.setText("");
            confirmPasswordField.requestFocus();
            return;
        }
        
        // Check if username already exists
        if (DatabaseConnection.usernameExists(username)) {
            showError("Username already exists!");
            usernameField.requestFocus();
            return;
        }
        
        // Check if email already exists
        if (DatabaseConnection.emailExists(email)) {
            showError("Email already registered!");
            emailField.requestFocus();
            return;
        }
        
        // Create new user
        User newUser = new User(fullName, email, username, password);
        
        // Register user
        boolean success = DatabaseConnection.registerUser(newUser);
        
        if (success) {
            // Registration successful
            JOptionPane.showMessageDialog(this,
                "Registration successful!\nYou can now login with your credentials.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Open login window
            openLogin();
            
        } else {
            showError("Registration failed! Please try again.");
        }
    }

    private void openLogin() {
        // Open login window
        Login loginFrame = new Login();
        loginFrame.setVisible(true);
        loginFrame.pack();
        loginFrame.setLocationRelativeTo(null);
        
        // Close sign up window
        dispose();
    }

    private void showError(String message) {
        statusLabel.setText(message);
        statusLabel.setForeground(Color.RED);
    }
}
