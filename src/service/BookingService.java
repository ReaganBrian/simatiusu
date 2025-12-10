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
    
    /**
     * Validate booking time
     */
    public boolean isValidBookingTime(Time jamMulai, Time jamSelesai) {
        if (jamMulai == null || jamSelesai == null) {
            return false;
        }
        
        LocalTime start = jamMulai.toLocalTime();
        LocalTime end = jamSelesai.toLocalTime();
        
        // Check if end time is after start time
        if (end.isBefore(start) || end.equals(start)) {
            return false;
        }
        
        // Check if within operational hours (08:00 - 17:00)
        LocalTime operationStart = LocalTime.of(8, 0);
        LocalTime operationEnd = LocalTime.of(17, 0);
        
        return !start.isBefore(operationStart) && !end.isAfter(operationEnd);
    }
    
    /**
     * Check if booking date is valid (not in the past)
     */
    public boolean isValidBookingDate(Date tanggal) {
        if (tanggal == null) {
            return false;
        }
        
        LocalDate bookingDate = tanggal.toLocalDate();
        LocalDate today = LocalDate.now();
        
        return !bookingDate.isBefore(today);
    }
    
    /**
     * Calculate booking duration in hours
     */
    public double calculateDuration(Time jamMulai, Time jamSelesai) {
        if (jamMulai == null || jamSelesai == null) {
            return 0.0;
        }
        
        long diff = jamSelesai.getTime() - jamMulai.getTime();
        return diff / (1000.0 * 60 * 60);
    }
    
    /**
     * Get booking by ID
     */
    public Booking getBookingById(int bookingId) {
        List<Booking> allBookings = bookingDAO.findAllBookingsWithDetails();
        for (Booking b : allBookings) {
            if (b.getId() == bookingId) {
                return b;
            }
        }
        return null;
    }
    
    /**
     * Count total bookings by status
     */
    public int countBookingsByStatus(String status) {
        List<Booking> allBookings = bookingDAO.findAllBookingsWithDetails();
        int count = 0;
        for (Booking b : allBookings) {
            if (status.equals(b.getStatus())) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Check if room is available for booking
     */
    public boolean isRoomAvailable(int roomId, Date tanggal, Time jamMulai, Time jamSelesai) {
        LocalDate localDate = tanggal.toLocalDate();
        String hariIndonesia = getDayNameIndonesia(localDate.getDayOfWeek());
        
        // Check jadwal tetap
        if (jadwalTetapDAO.hasOverlappingSchedule(roomId, hariIndonesia, jamMulai, jamSelesai)) {
            return false;
        }
        
        // Check existing bookings
        return !bookingDAO.hasOverlappingBooking(roomId, tanggal, jamMulai, jamSelesai, null);
    }
    
    /**
     * Validate minimum booking duration (minimal 1 jam)
     */
    public boolean isValidMinimumDuration(Time jamMulai, Time jamSelesai) {
        if (jamMulai == null || jamSelesai == null) {
            return false;
        }
        
        double duration = calculateDuration(jamMulai, jamSelesai);
        return duration >= 1.0; // Minimal 1 jam
    }
    
    /**
     * Validate maximum booking duration (maksimal 4 jam)
     */
    public boolean isValidMaximumDuration(Time jamMulai, Time jamSelesai) {
        if (jamMulai == null || jamSelesai == null) {
            return false;
        }
        
        double duration = calculateDuration(jamMulai, jamSelesai);
        return duration <= 4.0; // Maksimal 4 jam
    }
    
    /**
     * Check if booking time is within allowed hours (08:00 - 17:00)
     */
    public boolean isWithinOperationalHours(Time jamMulai, Time jamSelesai) {
        if (jamMulai == null || jamSelesai == null) {
            return false;
        }
        
        LocalTime start = jamMulai.toLocalTime();
        LocalTime end = jamSelesai.toLocalTime();
        LocalTime operationStart = LocalTime.of(8, 0);
        LocalTime operationEnd = LocalTime.of(17, 0);
        
        return !start.isBefore(operationStart) && !end.isAfter(operationEnd);
    }
    
    /**
     * Check if booking is made in advance (minimal 1 hari sebelumnya)
     */
    public boolean isValidAdvanceBooking(Date tanggal) {
        if (tanggal == null) {
            return false;
        }
        
        LocalDate bookingDate = tanggal.toLocalDate();
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        
        // Minimal booking untuk besok
        return !bookingDate.isBefore(tomorrow);
    }
    
    /**
     * Check if booking is within maximum advance period (maksimal 30 hari ke depan)
     */
    public boolean isWithinMaxAdvancePeriod(Date tanggal) {
        if (tanggal == null) {
            return false;
        }
        
        LocalDate bookingDate = tanggal.toLocalDate();
        LocalDate today = LocalDate.now();
        LocalDate maxDate = today.plusDays(30);
        
        return !bookingDate.isAfter(maxDate);
    }
    
    /**
     * Validate if time is in 30-minute intervals
     */
    public boolean isValidTimeInterval(Time time) {
        if (time == null) {
            return false;
        }
        
        LocalTime localTime = time.toLocalTime();
        int minute = localTime.getMinute();
        
        // Only allow 00 or 30 minutes
        return minute == 0 || minute == 30;
    }
    
    /**
     * Check if booking conflicts with lunch break (12:00 - 13:00)
     */
    public boolean hasLunchBreakConflict(Time jamMulai, Time jamSelesai) {
        if (jamMulai == null || jamSelesai == null) {
            return false;
        }
        
        LocalTime start = jamMulai.toLocalTime();
        LocalTime end = jamSelesai.toLocalTime();
        LocalTime lunchStart = LocalTime.of(12, 0);
        LocalTime lunchEnd = LocalTime.of(13, 0);
        
        // Check if booking overlaps with lunch break
        return (start.isBefore(lunchEnd) && end.isAfter(lunchStart));
    }
    
    /**
     * Get recommended booking time slots
     */
    public List<String> getRecommendedTimeSlots() {
        List<String> slots = new java.util.ArrayList<>();
        slots.add("08:00 - 10:00");
        slots.add("10:00 - 12:00");
        slots.add("13:00 - 15:00");
        slots.add("15:00 - 17:00");
        return slots;
    }
    
    /**
     * Calculate remaining available hours for a date
     */
    public int calculateRemainingHours(int roomId, Date tanggal) {
        // Total operational hours per day (08:00 - 17:00 = 9 hours)
        // Minus lunch break (1 hour) = 8 hours
        int totalHours = 8;
        
        List<Booking> bookings = bookingDAO.findApprovedBookingsByRoomAndDate(roomId, tanggal);
        int bookedHours = 0;
        
        for (Booking b : bookings) {
            bookedHours += (int) calculateDuration(b.getJamMulai(), b.getJamSelesai());
        }
        
        return totalHours - bookedHours;
    }
    
    /**
     * Check if weekend booking is allowed
     */
    public boolean isWeekendBookingAllowed(Date tanggal) {
        if (tanggal == null) {
            return false;
        }
        
        LocalDate localDate = tanggal.toLocalDate();
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        
        // Weekend booking not allowed by default
        return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
    }
    
    /**
     * Validate complete booking time constraints
     */
    public String validateBookingTimeConstraints(Time jamMulai, Time jamSelesai, Date tanggal) {
        if (!isValidBookingTime(jamMulai, jamSelesai)) {
            return "Waktu peminjaman tidak valid";
        }
        
        if (!isValidMinimumDuration(jamMulai, jamSelesai)) {
            return "Durasi peminjaman minimal 1 jam";
        }
        
        if (!isValidMaximumDuration(jamMulai, jamSelesai)) {
            return "Durasi peminjaman maksimal 4 jam";
        }
        
        if (!isWithinOperationalHours(jamMulai, jamSelesai)) {
            return "Waktu peminjaman harus antara 08:00 - 17:00";
        }
        
        if (!isValidTimeInterval(jamMulai) || !isValidTimeInterval(jamSelesai)) {
            return "Waktu harus dalam interval 30 menit (contoh: 08:00, 08:30)";
        }
        
        if (!isWeekendBookingAllowed(tanggal)) {
            return "Peminjaman tidak diperbolehkan pada akhir pekan";
        }
        
        return null; // No errors
    }
}
