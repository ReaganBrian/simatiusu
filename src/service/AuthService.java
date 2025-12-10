package service;

import dao.UserDAO;
import model.User;
import util.HashUtil;

public class AuthService {
    private UserDAO userDAO;
    
    public AuthService() {
        this.userDAO = new UserDAO();
    }
    
    /**
     * Login using email, NIDN, or NIM with password
     */
    public User login(String identifier, String password) {
        User user = userDAO.findByEmailOrNidnOrNim(identifier);
        
        if (user != null && HashUtil.checkPassword(password, user.getPasswordHash())) {
            return user;
        }
        return null;
    }
    
    /**
     * Register new user
     */
    public boolean register(String namaLengkap, String nidnOrNim, String email, 
                           String password, String confirmPassword) {
        
        // Validation
        if (namaLengkap == null || namaLengkap.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama lengkap harus diisi");
        }
        if (nidnOrNim == null || nidnOrNim.trim().isEmpty()) {
            throw new IllegalArgumentException("NIDN/NIM harus diisi");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email harus diisi");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Format email tidak valid");
        }
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Password minimal 8 karakter");
        }
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Password dan konfirmasi tidak sama");
        }
        if (userDAO.emailExists(email)) {
            throw new IllegalArgumentException("Email sudah terdaftar");
        }
        if (userDAO.existsByNimOrNidn(nidnOrNim)) {
            throw new IllegalArgumentException("NIDN/NIM sudah digunakan");
        }
        
        // Create user
        User user = new User();
        user.setNamaLengkap(namaLengkap.trim());
        user.setNidnOrNim(nidnOrNim.trim());
        user.setEmail(email.trim().toLowerCase());
        user.setPasswordHash(HashUtil.hashPassword(password));
        user.setRole("USER");
        
        return userDAO.insert(user);
    }
    
    /**
     * Validate registration fields before submit
     */
    public void validateRegistration(String namaLengkap, String nidnOrNim, String email, 
                                     String password, String confirmPassword) {
        if (namaLengkap == null || namaLengkap.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama lengkap harus diisi");
        }
        if (nidnOrNim == null || nidnOrNim.trim().isEmpty()) {
            throw new IllegalArgumentException("NIDN/NIM harus diisi");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email harus diisi");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Format email tidak valid");
        }
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Password minimal 8 karakter");
        }
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Password dan konfirmasi tidak sama");
        }
    }
    
    /**
     * Logout user - clear session if needed
     */
    public void logout(User user) {
        // Log logout activity
        System.out.println("User logged out: " + user.getEmail());
    }
    
    /**
     * Get user information by email
     */
    public User getUserInfo(String email) {
        return userDAO.findByEmailOrNidnOrNim(email);
    }
}
