package com.example.JustLifeCaseStudy.Service;

import com.example.JustLifeCaseStudy.Model.Booking;
import com.example.JustLifeCaseStudy.Model.Cleaner;
import com.example.JustLifeCaseStudy.Repository.BookingRepository;
import com.example.JustLifeCaseStudy.Repository.CleanerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

class AvailabilityServiceTest {

    @Mock
    private CleanerRepository cleanerRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private AvailabilityService availabilityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test Case 1.1: No Cleaners Available
    @Test
    void testGetAvailableCleanersForDate_NoCleaners() {
        when(cleanerRepository.findAll()).thenReturn(new ArrayList<>());

        Map<String, List<LocalDateTime>> result = availabilityService.getAvailableCleanersForDate(LocalDate.now());

        assertTrue(result.isEmpty());
    }

    // Test Case 1.2: Cleaners Available, No Bookings
    @Test
    void testGetAvailableCleanersForDate_NoBookings() {
        Cleaner cleaner = new Cleaner("1", "Cleaner1", null, new ArrayList<>());
        when(cleanerRepository.findAll()).thenReturn(List.of(cleaner));
        when(bookingRepository.findByCleanerAndDate(anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>());

        Map<String, List<LocalDateTime>> result = availabilityService.getAvailableCleanersForDate(LocalDate.now());

        assertEquals(1, result.size());
        assertTrue(result.get("Cleaner1").size() > 0); // Should return all available slots
    }

    // Test Case 1.3: Cleaners Available, Some Bookings
    @Test
    void testGetAvailableCleanersForDate_SomeBookings() {
        Cleaner cleaner = new Cleaner("1", "Cleaner1", null, new ArrayList<>());
        Booking booking = new Booking("1", LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0)), 2, List.of(cleaner));

        when(cleanerRepository.findAll()).thenReturn(List.of(cleaner));
        when(bookingRepository.findByCleanerAndDate(anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(booking));

        Map<String, List<LocalDateTime>> result = availabilityService.getAvailableCleanersForDate(LocalDate.now());

        assertEquals(1, result.size());
        assertTrue(result.get("Cleaner1").size() > 0); // Should return available slots excluding the booked ones
    }

    // Test Case 1.4: Cleaners with Overlapping Bookings
    @Test
    void testGetAvailableCleanersForDate_OverlappingBookings() {
        Cleaner cleaner = new Cleaner("1", "Cleaner1", null, new ArrayList<>());
        LocalDate date = LocalDate.now();
        Booking booking1 = new Booking("1", LocalDateTime.of(date, LocalTime.of(10, 0)), 2, List.of(cleaner));
        Booking booking2 = new Booking("2", LocalDateTime.of(date, LocalTime.of(12, 0)), 2, List.of(cleaner));

        when(cleanerRepository.findAll()).thenReturn(List.of(cleaner));
        when(bookingRepository.findByCleanerAndDate(anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(booking1, booking2));

        Map<String, List<LocalDateTime>> result = availabilityService.getAvailableCleanersForDate(date);

        assertEquals(1, result.size());
        assertTrue(result.get("Cleaner1").size() > 0); // Should return available slots excluding the overlapping ones
    }

    // Test Case 2.1: No Cleaners Available
    @Test
    void testGetAvailableCleanersForTime_NoCleaners() {
        when(cleanerRepository.findAll()).thenReturn(new ArrayList<>());

        List<String> result = availabilityService.getAvailableCleanersForTime(LocalDateTime.now(), 1);

        assertTrue(result.isEmpty());
    }

    // Test Case 2.2: All Cleaners Available
    @Test
    void testGetAvailableCleanersForTime_AllAvailable() {
        Cleaner cleaner = new Cleaner("1", "Cleaner1", null, new ArrayList<>());
        when(cleanerRepository.findAll()).thenReturn(List.of(cleaner));
        when(bookingRepository.findByCleanerAndDate(anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>());

        List<String> result = availabilityService.getAvailableCleanersForTime(LocalDateTime.now(), 1);

        assertEquals(1, result.size());
        assertTrue(result.contains("Cleaner1"));
    }

    // Test Case 2.3: Cleaners with Bookings
    @Test
    void testGetAvailableCleanersForTime_WithBookings() {
        Cleaner cleaner = new Cleaner("1", "Cleaner1", null, new ArrayList<>());
        LocalDateTime startDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0));
        int duration = 1;
        Booking booking = new Booking("1", startDateTime, 1, List.of(cleaner));

        when(cleanerRepository.findAll()).thenReturn(List.of(cleaner));
        when(bookingRepository.findByCleanerAndDate(anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(booking));

        List<String> result = availabilityService.getAvailableCleanersForTime(startDateTime, duration);

        assertTrue(result.isEmpty()); // Should be empty if the cleaner is booked
    }

    // Test Case 2.4: Duration Exceeds Working Hours
    @Test
    void testGetAvailableCleanersForTime_DurationExceedsWorkingHours() {
        Cleaner cleaner = new Cleaner("1", "Cleaner1", null, new ArrayList<>());
        LocalDateTime startDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(21, 0)); // Exceeds the working hours
        int duration = 3; // Exceeds the working hours

        when(cleanerRepository.findAll()).thenReturn(List.of(cleaner));

        List<String> result = availabilityService.getAvailableCleanersForTime(startDateTime, duration);

        assertTrue(result.isEmpty()); // Should be empty if the duration exceeds working hours
    }

    // Test Case 2.5: Booking Duration Across Break Time
    @Test
    void testGetAvailableCleanersForTime_AcrossBreak() {
        Cleaner cleaner = new Cleaner("1", "Cleaner1", null, new ArrayList<>());
        LocalDateTime startDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(15, 0));
        LocalDateTime previousBookingTimeStart = LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 45));
        int duration = 2; // Duration crosses over the break time
        Booking expectedBooking = new Booking("booking-id", previousBookingTimeStart, 2, List.of(cleaner));

        // Mock the cleaner repository
        when(cleanerRepository.findAll()).thenReturn(List.of(cleaner));

        // Mock the booking repository to return an empty list
        when(bookingRepository.findByCleanerAndDate(anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(expectedBooking));

        // Call the method
        List<String> result = availabilityService.getAvailableCleanersForTime(startDateTime, duration);

        // Validate the result
        assertTrue(result.isEmpty(), "Cleaner should not be available if the booking crosses break time");
    }

    // Test Case 4.1: Availability on Friday
    @Test
    void testCheckAvailability_OnFriday() {
        Cleaner cleaner = new Cleaner("1", "Cleaner1", null, new ArrayList<>());
        LocalDateTime startDateTime = LocalDateTime.of(LocalDate.now().with(DayOfWeek.FRIDAY), LocalTime.of(10, 0));
        int duration = 1;

        // Mock the public method to see if it's being correctly rejected
        List<String> result = availabilityService.getAvailableCleanersForTime(startDateTime, duration);

        assertFalse(result.contains("Cleaner1")); // Cleaner should not be available on Friday
    }

    // Test Case 4.2: Booking Outside Working Hours
    @Test
    void testCheckAvailability_OutsideWorkingHours() {
        Cleaner cleaner = new Cleaner("1", "Cleaner1", null, new ArrayList<>());
        LocalDateTime startDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(7, 0)); // Before working hours
        int duration = 1;

        // Mock the public method to see if it's being correctly rejected
        List<String> result = availabilityService.getAvailableCleanersForTime(startDateTime, duration);

        assertFalse(result.contains("Cleaner1")); // Cleaner should not be available before working hours
    }

    // Test Case 4.3: Booking Overlaps with Existing Booking
    @Test
    void testCheckAvailability_OverlappingBooking() {
        Cleaner cleaner = new Cleaner("1", "Cleaner1", null, new ArrayList<>());
        LocalDateTime startDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0));
        int duration = 2;
        Booking booking = new Booking("1", startDateTime, 2, List.of(cleaner));

        when(bookingRepository.findByCleanerAndDate(anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(booking));

        List<String> result = availabilityService.getAvailableCleanersForTime(startDateTime, duration);

        assertFalse(result.contains("Cleaner1")); // Cleaner should not be available if overlapping with an existing booking
    }

    // Test Case 4.4: Booking Immediately After an Existing Booking
    @Test
    void testCheckAvailability_AfterExistingBooking() {
        Cleaner cleaner = new Cleaner("1", "Cleaner1", null, new ArrayList<>());
        LocalDateTime startDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(11, 0));
        int duration = 1;
        Booking booking = new Booking("1", LocalDateTime.of(LocalDate.now(), LocalTime.of(10, 0)), 1, List.of(cleaner));

        when(bookingRepository.findByCleanerAndDate(anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(booking));

        List<String> result = availabilityService.getAvailableCleanersForTime(startDateTime, duration);

        assertFalse(result.contains("Cleaner1")); // Cleaner should not be available immediately after a booking if the 30-minute break is not considered
    }

    // Test Case 4.5: Booking During Break Time
    @Test
    void testCheckAvailability_DuringBreakTime() {
        Cleaner cleaner = new Cleaner("1", "Cleaner1", null, new ArrayList<>());
        LocalDateTime startDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0));
        int duration = 2;
        Booking booking = new Booking("1", LocalDateTime.of(LocalDate.now(), LocalTime.of(12, 0)), 2, List.of(cleaner));

        when(bookingRepository.findByCleanerAndDate(anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(booking));

        List<String> result = availabilityService.getAvailableCleanersForTime(startDateTime, duration);

        assertFalse(result.contains("Cleaner1")); // Cleaner should not be available if booking spans over the break time
    }
}
