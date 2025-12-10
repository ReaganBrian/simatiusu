package model;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class Booking {
    private int id;
    private int userId;
    private int roomId;
    private String namaDosen;
    private String mataKuliah;
    private Time jamMulai;
    private Time jamSelesai;
    private Date tanggal;
    private String keterangan;
    private String status;
    private Timestamp createdAt;
    
    
    private String namaPeminjam;
    private String nidnOrNim;
    private String kodeRuang;
    
    public Booking() {}
    
    public Booking(int id, int userId, int roomId, String namaDosen, String mataKuliah,
                   Time jamMulai, Time jamSelesai, Date tanggal, String keterangan,
                   String status, Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.roomId = roomId;
        this.namaDosen = namaDosen;
        this.mataKuliah = mataKuliah;
        this.jamMulai = jamMulai;
        this.jamSelesai = jamSelesai;
        this.tanggal = tanggal;
        this.keterangan = keterangan;
        this.status = status;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public int getRoomId() { return roomId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }
    
    public String getNamaDosen() { return namaDosen; }
    public void setNamaDosen(String namaDosen) { this.namaDosen = namaDosen; }
    
    public String getMataKuliah() { return mataKuliah; }
    public void setMataKuliah(String mataKuliah) { this.mataKuliah = mataKuliah; }
    
    public Time getJamMulai() { return jamMulai; }
    public void setJamMulai(Time jamMulai) { this.jamMulai = jamMulai; }
    
    public Time getJamSelesai() { return jamSelesai; }
    public void setJamSelesai(Time jamSelesai) { this.jamSelesai = jamSelesai; }
    
    public Date getTanggal() { return tanggal; }
    public void setTanggal(Date tanggal) { this.tanggal = tanggal; }
    
    public String getKeterangan() { return keterangan; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    
    public String getNamaPeminjam() { return namaPeminjam; }
    public void setNamaPeminjam(String namaPeminjam) { this.namaPeminjam = namaPeminjam; }
    
    public String getNidnOrNim() { return nidnOrNim; }
    public void setNidnOrNim(String nidnOrNim) { this.nidnOrNim = nidnOrNim; }
    
    public String getKodeRuang() { return kodeRuang; }
    public void setKodeRuang(String kodeRuang) { this.kodeRuang = kodeRuang; }
    
    /**
     * Check if booking is pending
     */
    public boolean isPending() {
        return "MENUNGGU".equals(status) || "PENDING".equals(status);
    }
    
    /**
     * Check if booking is approved
     */
    public boolean isApproved() {
        return "DISETUJUI".equals(status) || "APPROVED".equals(status);
    }
    
    /**
     * Check if booking is rejected
     */
    public boolean isRejected() {
        return "DITOLAK".equals(status) || "REJECTED".equals(status);
    }
    
    /**
     * Get duration in minutes
     */
    public int getDurationInMinutes() {
        if (jamMulai == null || jamSelesai == null) {
            return 0;
        }
        
        long diff = jamSelesai.getTime() - jamMulai.getTime();
        return (int) (diff / (1000 * 60));
    }
    
    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", userId=" + userId +
                ", roomId=" + roomId +
                ", namaDosen='" + namaDosen + '\'' +
                ", mataKuliah='" + mataKuliah + '\'' +
                ", tanggal=" + tanggal +
                ", jamMulai=" + jamMulai +
                ", jamSelesai=" + jamSelesai +
                ", status='" + status + '\'' +
                '}';
    }
}
