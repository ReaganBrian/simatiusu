    package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.JadwalTetap;
import util.DBConnection;

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
    
    /**
     * Insert new jadwal tetap
     */
    public boolean insert(JadwalTetap jadwal) {
        String sql = "INSERT INTO jadwal_tetap (room_id, hari, jam_mulai, jam_selesai, nama_dosen, mata_kuliah) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, jadwal.getRoomId());
            stmt.setString(2, jadwal.getHari());
            stmt.setTime(3, jadwal.getJamMulai());
            stmt.setTime(4, jadwal.getJamSelesai());
            stmt.setString(5, jadwal.getNamaDosen());
            stmt.setString(6, jadwal.getMataKuliah());
            
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    jadwal.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Update jadwal tetap
     */
    public boolean update(JadwalTetap jadwal) {
        String sql = "UPDATE jadwal_tetap SET hari = ?, jam_mulai = ?, jam_selesai = ?, " +
                     "nama_dosen = ?, mata_kuliah = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, jadwal.getHari());
            stmt.setTime(2, jadwal.getJamMulai());
            stmt.setTime(3, jadwal.getJamSelesai());
            stmt.setString(4, jadwal.getNamaDosen());
            stmt.setString(5, jadwal.getMataKuliah());
            stmt.setInt(6, jadwal.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Delete jadwal tetap
     */
    public boolean delete(int jadwalId) {
        String sql = "DELETE FROM jadwal_tetap WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, jadwalId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Find by ID
     */
    public JadwalTetap findById(int jadwalId) {
        String sql = "SELECT * FROM jadwal_tetap WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, jadwalId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extractJadwalTetap(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Count jadwal by room
     */
    public int countByRoom(int roomId) {
        String sql = "SELECT COUNT(*) FROM jadwal_tetap WHERE room_id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, roomId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    /**
     * Find jadwal by dosen name
     */
    public List<JadwalTetap> findByDosen(String namaDosen) {
        List<JadwalTetap> jadwals = new ArrayList<>();
        String sql = "SELECT jt.*, r.kode_ruang FROM jadwal_tetap jt " +
                     "JOIN rooms r ON jt.room_id = r.id " +
                     "WHERE jt.nama_dosen LIKE ? " +
                     "ORDER BY FIELD(jt.hari, 'Senin', 'Selasa', 'Rabu', 'Kamis', 'Jumat', 'Sabtu'), jt.jam_mulai";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + namaDosen + "%");
            ResultSet rs = stmt.executeQuery();
            
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
}
