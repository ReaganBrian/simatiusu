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
    
    /**
     * Check if room belongs to TI
     */
    public boolean isTI() {
        return "TI".equals(prodi);
    }
    
    /**
     * Check if room belongs to IK
     */
    public boolean isIK() {
        return "IK".equals(prodi);
    }
    
    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", kodeRuang='" + kodeRuang + '\'' +
                ", prodi='" + prodi + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return id == room.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
