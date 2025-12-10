package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Booking;
import util.DBConnection;

public class BookingDAO {
    
    public boolean insert(Booking booking) {
        String sql = "INSERT INTO bookings (user_id, room_id, nama_dosen, mata_kuliah, " +
                     "jam_mulai, jam_selesai, tanggal, keterangan, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, booking.getUserId());
            stmt.setInt(2, booking.getRoomId());
            stmt.setString(3, booking.getNamaDosen());
            stmt.setString(4, booking.getMataKuliah());
            stmt.setTime(5, booking.getJamMulai());
            stmt.setTime(6, booking.getJamSelesai());
            stmt.setDate(7, booking.getTanggal());
            stmt.setString(8, booking.getKeterangan());
            stmt.setString(9, booking.getStatus());
            
            int affected = stmt.executeUpdate();
            
            if (affected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    booking.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean updateStatus(int bookingId, String status) {
        String sql = "UPDATE bookings SET status = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, bookingId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public List<Booking> findPendingBookings() {
        String sql = "SELECT b.*, u.nama_lengkap, u.nidn_or_nim, r.kode_ruang " +
                     "FROM bookings b " +
                     "JOIN users u ON b.user_id = u.id " +
                     "JOIN rooms r ON b.room_id = r.id " +
                     "WHERE b.status = 'MENUNGGU' " +
                     "ORDER BY b.created_at";
        
        return executeQuery(sql);
    }
    
    public List<Booking> findByUserId(int userId) {
        String sql = "SELECT b.*, u.nama_lengkap, u.nidn_or_nim, r.kode_ruang " +
                     "FROM bookings b " +
                     "JOIN users u ON b.user_id = u.id " +
                     "JOIN rooms r ON b.room_id = r.id " +
                     "WHERE b.user_id = ? " +
                     "ORDER BY b.tanggal DESC, b.jam_mulai DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            List<Booking> bookings = new ArrayList<>();
            while (rs.next()) {
                bookings.add(extractBooking(rs));
            }
            return bookings;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
    
    public List<Booking> findAllBookingsWithDetails() {
        String sql = "SELECT b.*, u.nama_lengkap, u.nidn_or_nim, r.kode_ruang " +
                     "FROM bookings b " +
                     "JOIN users u ON b.user_id = u.id " +
                     "JOIN rooms r ON b.room_id = r.id " +
                     "ORDER BY b.tanggal DESC, b.jam_mulai DESC";
        
        return executeQuery(sql);
    }
    
    public Booking findActiveBookingForRoom(int roomId, Date tanggal, Time currentTime) {
        String sql = "SELECT b.*, u.nama_lengkap, u.nidn_or_nim, r.kode_ruang " +
                     "FROM bookings b " +
                     "JOIN users u ON b.user_id = u.id " +
                     "JOIN rooms r ON b.room_id = r.id " +
                     "WHERE b.room_id = ? AND b.tanggal = ? " +
                     "AND b.status = 'DISETUJUI' " +
                     "AND b.jam_mulai <= ? AND b.jam_selesai > ? " +
                     "LIMIT 1";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, roomId);
            stmt.setDate(2, tanggal);
            stmt.setTime(3, currentTime);
            stmt.setTime(4, currentTime);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractBooking(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean hasOverlappingBooking(int roomId, Date tanggal, Time jamMulai, Time jamSelesai, Integer excludeBookingId) {
        StringBuilder sql = new StringBuilder(
            "SELECT COUNT(*) FROM bookings " +
            "WHERE room_id = ? AND tanggal = ? AND status = 'DISETUJUI' " +
            "AND ((jam_mulai < ? AND jam_selesai > ?) OR " +
            "     (jam_mulai < ? AND jam_selesai > ?) OR " +
            "     (jam_mulai >= ? AND jam_selesai <= ?))"
        );
        
        if (excludeBookingId != null) {
            sql.append(" AND id != ?");
        }
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            stmt.setInt(1, roomId);
            stmt.setDate(2, tanggal);
            stmt.setTime(3, jamSelesai);
            stmt.setTime(4, jamMulai);
            stmt.setTime(5, jamSelesai);
            stmt.setTime(6, jamSelesai);
            stmt.setTime(7, jamMulai);
            stmt.setTime(8, jamSelesai);
            
            if (excludeBookingId != null) {
                stmt.setInt(9, excludeBookingId);
            }
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private List<Booking> executeQuery(String sql) {
        List<Booking> bookings = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                bookings.add(extractBooking(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }
    
    private Booking extractBooking(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setId(rs.getInt("id"));
        booking.setUserId(rs.getInt("user_id"));
        booking.setRoomId(rs.getInt("room_id"));
        booking.setNamaDosen(rs.getString("nama_dosen"));
        booking.setMataKuliah(rs.getString("mata_kuliah"));
        booking.setJamMulai(rs.getTime("jam_mulai"));
        booking.setJamSelesai(rs.getTime("jam_selesai"));
        booking.setTanggal(rs.getDate("tanggal"));
        booking.setKeterangan(rs.getString("keterangan"));
        booking.setStatus(rs.getString("status"));
        booking.setCreatedAt(rs.getTimestamp("created_at"));
        
        // Additional fields
        booking.setNamaPeminjam(rs.getString("nama_lengkap"));
        booking.setNidnOrNim(rs.getString("nidn_or_nim"));
        booking.setKodeRuang(rs.getString("kode_ruang"));
        
        return booking;
    }
}
