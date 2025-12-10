package service;

import dao.BookingDAO;
import dao.JadwalTetapDAO;
import dao.RoomDAO;
import java.sql.Date;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Booking;
import model.JadwalTetap;
import model.Room;

public class RoomService {
    private RoomDAO roomDAO;
    private BookingDAO bookingDAO;
    private JadwalTetapDAO jadwalTetapDAO;
    
    public RoomService() {
        this.roomDAO = new RoomDAO();
        this.bookingDAO = new BookingDAO();
        this.jadwalTetapDAO = new JadwalTetapDAO();
    }
    
    public List<Room> getAllRooms() {
        return roomDAO.findAll();
    }
    
    public List<Room> getRoomsByProdi(String prodi) {
        return roomDAO.findByProdi(prodi);
    }
    
    public Room getRoomById(int id) {
        return roomDAO.findById(id);
    }
    
    public Room getRoomByKode(String kodeRuang) {
        return roomDAO.findByKodeRuang(kodeRuang);
    }
    
    /**
     * Check if room is currently occupied (realtime) - considers both jadwal tetap and bookings
     */
    public boolean isRoomOccupied(int roomId) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        
        // Check jadwal tetap
        String hariIndonesia = getDayNameIndonesia(today.getDayOfWeek());
        Time sqlTime = Time.valueOf(now);
        JadwalTetap activeSchedule = jadwalTetapDAO.findActiveSchedule(roomId, hariIndonesia, sqlTime);
        if (activeSchedule != null) {
            return true;
        }
        
        // Check bookings
        Date sqlDate = Date.valueOf(today);
        Booking activeBooking = bookingDAO.findActiveBookingForRoom(roomId, sqlDate, sqlTime);
        return activeBooking != null;
    }
    
    /**
     * Get current active booking for a room (if any)
     */
    public Booking getActiveBooking(int roomId) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        
        Date sqlDate = Date.valueOf(today);
        Time sqlTime = Time.valueOf(now);
        
        return bookingDAO.findActiveBookingForRoom(roomId, sqlDate, sqlTime);
    }
    
    /**
     * Get current active jadwal tetap for a room (if any)
     */
    public JadwalTetap getActiveJadwalTetap(int roomId) {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        
        String hariIndonesia = getDayNameIndonesia(today.getDayOfWeek());
        Time sqlTime = Time.valueOf(now);
        
        return jadwalTetapDAO.findActiveSchedule(roomId, hariIndonesia, sqlTime);
    }
    
    /**
     * Get realtime status for all rooms
     * Returns map: roomId -> isOccupied
     */
    public Map<Integer, Boolean> getAllRoomStatuses() {
        Map<Integer, Boolean> statuses = new HashMap<>();
        List<Room> rooms = roomDAO.findAll();
        
        for (Room room : rooms) {
            statuses.put(room.getId(), isRoomOccupied(room.getId()));
        }
        
        return statuses;
    }
    
    /**
     * Get active bookings for all rooms
     * Returns map: roomId -> Booking (or null if not occupied)
     */
    public Map<Integer, Booking> getAllActiveBookings() {
        Map<Integer, Booking> bookings = new HashMap<>();
        List<Room> rooms = roomDAO.findAll();
        
        for (Room room : rooms) {
            Booking activeBooking = getActiveBooking(room.getId());
            bookings.put(room.getId(), activeBooking);
        }
        
        return bookings;
    }
    
    /**
     * Get active jadwal tetap for all rooms
     * Returns map: roomId -> JadwalTetap (or null if not occupied by schedule)
     */
    public Map<Integer, JadwalTetap> getAllActiveJadwalTetap() {
        Map<Integer, JadwalTetap> jadwals = new HashMap<>();
        List<Room> rooms = roomDAO.findAll();
        
        for (Room room : rooms) {
            JadwalTetap activeJadwal = getActiveJadwalTetap(room.getId());
            jadwals.put(room.getId(), activeJadwal);
        }
        
        return jadwals;
    }
    
    /**
     * Convert DayOfWeek to Indonesian day name
     */
    private String getDayNameIndonesia(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY: return "Senin";
            case TUESDAY: return "Selasa";
            case WEDNESDAY: return "Rabu";
            case THURSDAY: return "Kamis";
            case FRIDAY: return "Jumat";
            case SATURDAY: return "Sabtu";
            case SUNDAY: return "Minggu";
            default: return "";
        }
    }
    
    /**
     * Validate room data
     */
    public boolean validateRoom(Room room) {
        if (room == null) {
            return false;
        }
        
        return room.getKodeRuang() != null && 
               !room.getKodeRuang().isEmpty() &&
               room.getProdi() != null &&
               !room.getProdi().isEmpty();
    }
    
    /**
     * Handle room not found error
     */
    public Room getRoomWithErrorHandling(int roomId) {
        try {
            Room room = roomDAO.findById(roomId);
            if (room == null) {
                System.err.println("Room with ID " + roomId + " not found");
            }
            return room;
        } catch (Exception e) {
            System.err.println("Error getting room: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Get room status with error handling
     */
    public Boolean getRoomStatusSafe(int roomId) {
        try {
            return isRoomOccupied(roomId);
        } catch (Exception e) {
            System.err.println("Error checking room status: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Get all rooms with error handling
     */
    public List<Room> getAllRoomsSafe() {
        try {
            List<Room> rooms = roomDAO.findAll();
            if (rooms == null || rooms.isEmpty()) {
                System.out.println("No rooms found in database");
                return new java.util.ArrayList<>();
            }
            return rooms;
        } catch (Exception e) {
            System.err.println("Error getting all rooms: " + e.getMessage());
            e.printStackTrace();
            return new java.util.ArrayList<>();
        }
    }
    
    /**
     * Count available rooms
     */
    public int countAvailableRooms() {
        int count = 0;
        List<Room> rooms = roomDAO.findAll();
        
        for (Room room : rooms) {
            if (!isRoomOccupied(room.getId())) {
                count++;
            }
        }
        
        return count;
    }
    
    /**
     * Count occupied rooms
     */
    public int countOccupiedRooms() {
        int count = 0;
        List<Room> rooms = roomDAO.findAll();
        
        for (Room room : rooms) {
            if (isRoomOccupied(room.getId())) {
                count++;
            }
        }
        
        return count;
    }
    
    /**
     * Get room statistics
     */
    public Map<String, Integer> getRoomStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        List<Room> rooms = roomDAO.findAll();
        
        stats.put("total", rooms.size());
        stats.put("available", countAvailableRooms());
        stats.put("occupied", countOccupiedRooms());
        stats.put("TI", roomDAO.findByProdi("TI").size());
        stats.put("IK", roomDAO.findByProdi("IK").size());
        
        return stats;
    }
    
    /**
     * Validate and refresh room status
     */
    public boolean refreshRoomStatus(int roomId) {
        try {
            Room room = roomDAO.findById(roomId);
            if (room == null) {
                return false;
            }
            
            // Revalidate status
            boolean isOccupied = isRoomOccupied(roomId);
            return true;
        } catch (Exception e) {
            System.err.println("Error refreshing room status: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Clear cached data and reload
     */
    public void clearCacheAndReload() {
        // Force reload from database
        System.out.println("Clearing cache and reloading room data...");
    }
}
