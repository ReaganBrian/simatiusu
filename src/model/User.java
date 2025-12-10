package model;

import java.sql.Timestamp;

public class User {
    private int id;
    private String namaLengkap;
    private String nidnOrNim;
    private String email;
    private String passwordHash;
    private String role;
    private Timestamp createdAt;
    
    public User() {}
    
    public User(int id, String namaLengkap, String nidnOrNim, String email, 
                String passwordHash, String role, Timestamp createdAt) {
        this.id = id;
        this.namaLengkap = namaLengkap;
        this.nidnOrNim = nidnOrNim;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNamaLengkap() { return namaLengkap; }
    public void setNamaLengkap(String namaLengkap) { this.namaLengkap = namaLengkap; }
    
    public String getNidnOrNim() { return nidnOrNim; }
    public void setNidnOrNim(String nidnOrNim) { this.nidnOrNim = nidnOrNim; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    
    public boolean isAdmin() {
        return "ADMIN".equals(role);
    }
    
    public boolean isUser() {
        return "USER".equals(role);
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", namaLengkap='" + namaLengkap + '\'' +
                ", nidnOrNim='" + nidnOrNim + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
