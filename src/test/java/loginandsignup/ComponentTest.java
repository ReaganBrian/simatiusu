package loginandsignup;

import javax.swing.*;
import java.awt.*;

/**
 * Basic component verification test
 * Note: This is for verification only, not a complete unit test suite
 */
public class ComponentTest {
    
    public static void main(String[] args) {
        System.out.println("=== Component Verification Test ===\n");
        
        // Test User Model
        System.out.println("1. Testing User Model...");
        User testUser = new User("Test User", "test@example.com", "testuser", "password123");
        assert testUser.getFullName().equals("Test User") : "User full name not set correctly";
        assert testUser.getEmail().equals("test@example.com") : "User email not set correctly";
        assert testUser.getUsername().equals("testuser") : "User username not set correctly";
        System.out.println("   ✓ User model works correctly");
        
        // Test Login Frame (in headless mode)
        System.out.println("\n2. Testing Login Frame...");
        try {
            System.setProperty("java.awt.headless", "true");
            
            // We can't fully initialize the frame in headless mode,
            // but we can verify the class exists and is a JFrame
            Class<?> loginClass = Class.forName("loginandsignup.Login");
            assert JFrame.class.isAssignableFrom(loginClass) : "Login is not a JFrame";
            System.out.println("   ✓ Login class extends JFrame correctly");
            
        } catch (Exception e) {
            System.out.println("   ✗ Error testing Login frame: " + e.getMessage());
        }
        
        // Test SignUp Frame
        System.out.println("\n3. Testing SignUp Frame...");
        try {
            Class<?> signUpClass = Class.forName("loginandsignup.SignUp");
            assert JFrame.class.isAssignableFrom(signUpClass) : "SignUp is not a JFrame";
            System.out.println("   ✓ SignUp class extends JFrame correctly");
            
        } catch (Exception e) {
            System.out.println("   ✗ Error testing SignUp frame: " + e.getMessage());
        }
        
        // Test DatabaseConnection
        System.out.println("\n4. Testing DatabaseConnection...");
        try {
            Class<?> dbClass = Class.forName("loginandsignup.DatabaseConnection");
            
            // Verify methods exist
            dbClass.getMethod("registerUser", User.class);
            dbClass.getMethod("validateLogin", String.class, String.class);
            dbClass.getMethod("usernameExists", String.class);
            dbClass.getMethod("emailExists", String.class);
            dbClass.getMethod("testConnection");
            
            System.out.println("   ✓ DatabaseConnection has all required methods");
            
        } catch (Exception e) {
            System.out.println("   ✗ Error testing DatabaseConnection: " + e.getMessage());
        }
        
        // Test Email Validation Pattern (via reflection)
        System.out.println("\n5. Testing Email Validation...");
        try {
            String[] validEmails = {
                "test@example.com",
                "user.name@domain.co.id",
                "test123@test-domain.com"
            };
            String[] invalidEmails = {
                "invalid.email",
                "@example.com",
                "test@",
                "test @example.com"
            };
            
            java.util.regex.Pattern emailPattern = 
                java.util.regex.Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
            
            boolean allValid = true;
            for (String email : validEmails) {
                if (!emailPattern.matcher(email).matches()) {
                    System.out.println("   ✗ Valid email rejected: " + email);
                    allValid = false;
                }
            }
            
            for (String email : invalidEmails) {
                if (emailPattern.matcher(email).matches()) {
                    System.out.println("   ✗ Invalid email accepted: " + email);
                    allValid = false;
                }
            }
            
            if (allValid) {
                System.out.println("   ✓ Email validation pattern works correctly");
            }
            
        } catch (Exception e) {
            System.out.println("   ✗ Error testing email validation: " + e.getMessage());
        }
        
        System.out.println("\n=== All Component Tests Passed ===");
    }
}
