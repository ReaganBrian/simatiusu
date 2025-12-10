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
}
