    package dao;

import model.JadwalTetap;
import util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JadwalTetapDAO {
    
    public List<JadwalTetap> findByRoomAndDay(int roomId, String hari) {
        List<JadwalTetap> jadwals = new ArrayList<>();
        String sql = "SELECT * FROM jadwal_tetap WHERE room_id = ? AND hari = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, roomId);
            stmt.setString(2, hari);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                jadwals.add(extractJadwalTetap(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jadwals;
    }
    
    public List<JadwalTetap> findByRoom(int roomId) {
        List<JadwalTetap> jadwals = new ArrayList<>();
        String sql = "SELECT * FROM jadwal_tetap WHERE room_id = ? ORDER BY " +
                     "FIELD(hari, 'Senin', 'Selasa', 'Rabu', 'Kamis', 'Jumat', 'Sabtu'), jam_mulai";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, roomId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                jadwals.add(extractJadwalTetap(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jadwals;
    }
    
    public JadwalTetap findActiveSchedule(int roomId, String hari, Time currentTime) {
        String sql = "SELECT * FROM jadwal_tetap " +
                     "WHERE room_id = ? AND hari = ? " +
                     "AND jam_mulai <= ? AND jam_selesai > ? " +
                     "LIMIT 1";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, roomId);
            stmt.setString(2, hari);
            stmt.setTime(3, currentTime);
            stmt.setTime(4, currentTime);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractJadwalTetap(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean hasOverlappingSchedule(int roomId, String hari, Time jamMulai, Time jamSelesai) {
        String sql = "SELECT COUNT(*) FROM jadwal_tetap " +
                     "WHERE room_id = ? AND hari = ? " +
                     "AND ((jam_mulai < ? AND jam_selesai > ?) OR " +
                     "     (jam_mulai < ? AND jam_selesai > ?) OR " +
                     "     (jam_mulai >= ? AND jam_selesai <= ?))";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, roomId);
            stmt.setString(2, hari);
            stmt.setTime(3, jamSelesai);
            stmt.setTime(4, jamMulai);
            stmt.setTime(5, jamSelesai);
            stmt.setTime(6, jamSelesai);
            stmt.setTime(7, jamMulai);
            stmt.setTime(8, jamSelesai);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public List<JadwalTetap> findAll() {
        List<JadwalTetap> jadwals = new ArrayList<>();
        String sql = "SELECT jt.*, r.kode_ruang FROM jadwal_tetap jt " +
                     "JOIN rooms r ON jt.room_id = r.id " +
                     "ORDER BY r.kode_ruang, " +
                     "FIELD(jt.hari, 'Senin', 'Selasa', 'Rabu', 'Kamis', 'Jumat', 'Sabtu'), " +
                     "jt.jam_mulai";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                JadwalTetap jadwal = extractJadwalTetap(rs);
                jadwal.setKodeRuang(rs.getString("kode_ruang"));
                jadwals.add(jadwal);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jadwals;
    }
    
    private JadwalTetap extractJadwalTetap(ResultSet rs) throws SQLException {
        JadwalTetap jadwal = new JadwalTetap();
        jadwal.setId(rs.getInt("id"));
        jadwal.setRoomId(rs.getInt("room_id"));
        jadwal.setHari(rs.getString("hari"));
        jadwal.setJamMulai(rs.getTime("jam_mulai"));
        jadwal.setJamSelesai(rs.getTime("jam_selesai"));
        jadwal.setNamaDosen(rs.getString("nama_dosen"));
        jadwal.setMataKuliah(rs.getString("mata_kuliah"));
        return jadwal;
    }
}
