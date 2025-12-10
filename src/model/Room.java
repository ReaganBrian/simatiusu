package model;

public class Room {
    private int id;
    private String kodeRuang;
    private String prodi;
    
    public Room() {}
    
    public Room(int id, String kodeRuang, String prodi) {
        this.id = id;
        this.kodeRuang = kodeRuang;
        this.prodi = prodi;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getKodeRuang() { return kodeRuang; }
    public void setKodeRuang(String kodeRuang) { this.kodeRuang = kodeRuang; }
    
    public String getProdi() { return prodi; }
    public void setProdi(String prodi) { this.prodi = prodi; }
}
