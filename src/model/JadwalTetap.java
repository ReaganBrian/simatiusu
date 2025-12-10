package model;

import java.sql.Time;

public class JadwalTetap {
    private int id;
    private int roomId;
    private String hari;
    private Time jamMulai;
    private Time jamSelesai;
    private String namaDosen;
    private String mataKuliah;
    
    
    private String kodeRuang;
    
    public JadwalTetap() {}
    
    public JadwalTetap(int id, int roomId, String hari, Time jamMulai, Time jamSelesai,
                      String namaDosen, String mataKuliah) {
        this.id = id;
        this.roomId = roomId;
        this.hari = hari;
        this.jamMulai = jamMulai;
        this.jamSelesai = jamSelesai;
        this.namaDosen = namaDosen;
        this.mataKuliah = mataKuliah;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getRoomId() { return roomId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }
    
    public String getHari() { return hari; }
    public void setHari(String hari) { this.hari = hari; }
    
    public Time getJamMulai() { return jamMulai; }
    public void setJamMulai(Time jamMulai) { this.jamMulai = jamMulai; }
    
    public Time getJamSelesai() { return jamSelesai; }
    public void setJamSelesai(Time jamSelesai) { this.jamSelesai = jamSelesai; }
    
    public String getNamaDosen() { return namaDosen; }
    public void setNamaDosen(String namaDosen) { this.namaDosen = namaDosen; }
    
    public String getMataKuliah() { return mataKuliah; }
    public void setMataKuliah(String mataKuliah) { this.mataKuliah = mataKuliah; }
    
    public String getKodeRuang() { return kodeRuang; }
    public void setKodeRuang(String kodeRuang) { this.kodeRuang = kodeRuang; }
}
