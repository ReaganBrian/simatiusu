package service;

import dao.BookingDAO;
import dao.JadwalTetapDAO;
import java.sql.Date;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import model.Booking;

public class BookingService {
    private BookingDAO bookingDAO;
    private JadwalTetapDAO jadwalTetapDAO;
    
    public BookingService() {
        this.bookingDAO = new BookingDAO();
        this.jadwalTetapDAO = new JadwalTetapDAO();
    }
    
    /**
     * Create new booking request with custom start and end time
     */
    public boolean createBooking(int userId, int roomId, String namaDosen, 
                                 String mataKuliah, Time jamMulai, Time jamSelesai, 
                                 Date tanggal, String keterangan) {
        
        // Validation
        if (namaDosen == null || namaDosen.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama dosen harus diisi");
        }
        if (mataKuliah == null || mataKuliah.trim().isEmpty()) {
            throw new IllegalArgumentException("Mata kuliah harus diisi");
        }
        if (jamMulai == null) {
            throw new IllegalArgumentException("Jam mulai harus diisi");
        }
        if (jamSelesai == null) {
            throw new IllegalArgumentException("Jam selesai harus diisi");
        }
        if (tanggal == null) {
            throw new IllegalArgumentException("Tanggal harus diisi");
        }
        
        // Check if jamSelesai is after jamMulai
        LocalTime startTime = jamMulai.toLocalTime();
        LocalTime endTime = jamSelesai.toLocalTime();
        if (endTime.isBefore(startTime) || endTime.equals(startTime)) {
            throw new IllegalArgumentException("Jam selesai harus lebih dari jam mulai");
        }
        
        // Get day name from tanggal
        LocalDate localDate = tanggal.toLocalDate();
        String hariIndonesia = getDayNameIndonesia(localDate.getDayOfWeek());
        
        // Check for overlapping with jadwal tetap
        if (jadwalTetapDAO.hasOverlappingSchedule(roomId, hariIndonesia, jamMulai, jamSelesai)) {
            throw new IllegalArgumentException("Ruangan sedang digunakan berdasarkan jadwal tetap");
        }
        
        // Check for overlapping bookings
        if (bookingDAO.hasOverlappingBooking(roomId, tanggal, jamMulai, jamSelesai, null)) {
            throw new IllegalArgumentException("Ruangan sudah dibooking pada waktu tersebut");
        }
        
        // Create booking
        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setRoomId(roomId);
        booking.setNamaDosen(namaDosen.trim());
        booking.setMataKuliah(mataKuliah.trim());
        booking.setJamMulai(jamMulai);
        booking.setJamSelesai(jamSelesai);
        booking.setTanggal(tanggal);
        booking.setKeterangan(keterangan != null ? keterangan.trim() : "");
        booking.setStatus("MENUNGGU");
        
        return bookingDAO.insert(booking);
    }
    
    /**
     * Approve booking (admin only)
     */
    public boolean approveBooking(int bookingId) {
        return bookingDAO.updateStatus(bookingId, "DISETUJUI");
    }
    
    /**
     * Reject booking (admin only)
     */
    public boolean rejectBooking(int bookingId) {
        return bookingDAO.updateStatus(bookingId, "DITOLAK");
    }
    
    /**
     * Get all pending bookings for admin verification
     */
    public List<Booking> getPendingBookings() {
        return bookingDAO.findPendingBookings();
    }
    
    /**
     * Get booking history for a user
     */
    public List<Booking> getUserBookings(int userId) {
        return bookingDAO.findByUserId(userId);
    }
    
    /**
     * Get all bookings (history) for admin
     */
    public List<Booking> getAllBookings() {
        return bookingDAO.findAllBookingsWithDetails();
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
