package com.example.JustLifeCaseStudy.Service;

import com.example.JustLifeCaseStudy.Model.Booking;
import com.example.JustLifeCaseStudy.Model.Cleaner;
import com.example.JustLifeCaseStudy.Model.Vehicle;
import com.example.JustLifeCaseStudy.Repository.BookingRepository;
import com.example.JustLifeCaseStudy.Repository.CleanerRepository;
import com.example.JustLifeCaseStudy.dto.request.BookingRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BookingServiceTest {

    @Mock
    private CleanerRepository cleanerRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test Case 1: Successful Booking Creation
    @Test
    void testCreateBooking_Successful() {
        Vehicle vehicle = new Vehicle("1", "Vehicle1", new ArrayList<>());
        Cleaner cleaner1 = new Cleaner("1", "Cleaner1", vehicle, null);
        Cleaner cleaner2 = new Cleaner("2", "Cleaner2", vehicle, null);
        Cleaner cleaner3 = new Cleaner("3", "Cleaner3", vehicle, null);
        vehicle.setCleaners(List.of(cleaner1, cleaner2, cleaner3));
        BookingRequestDto requestDto = new BookingRequestDto(
                LocalDateTime.now().plusDays(100).withHour(9).withMinute(0),
                2,
                List.of("1", "2", "3")
        );

        when(cleanerRepository.findByIdIn(any())).thenReturn(List.of(cleaner1, cleaner2, cleaner3));
        when(bookingRepository.save(any())).thenAnswer(invocation -> invocation.getArguments()[0]);

        Booking booking = bookingService.createBooking(requestDto);

        assertNotNull(booking);
        assertEquals(3, booking.getCleaner().size());
        verify(bookingRepository).save(any());
    }

    // Test Case 2: Booking with Invalid Number of Cleaners
    @Test
    void testCreateBooking_InvalidNumberOfCleaners() {
        BookingRequestDto requestDto = new BookingRequestDto(
                LocalDateTime.now().plusDays(100).withHour(9).withMinute(0),
                2,
                List.of("1", "2", "3", "4")
        );

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            bookingService.createBooking(requestDto);
        });

        assertEquals("You can only book 1, 2, or 3 cleaners.", thrown.getMessage());
    }

    // Test Case 3: Booking with Cleaners from Different Vehicles
    @Test
    void testCreateBooking_DifferentVehicles() {
        Vehicle vehicle1 = new Vehicle("1", "Vehicle1", new ArrayList<>());
        Vehicle vehicle2 = new Vehicle("2", "Vehicle2", new ArrayList<>());
        Cleaner cleaner1 = new Cleaner("1", "Cleaner1", vehicle1, null);
        Cleaner cleaner2 = new Cleaner("2", "Cleaner2", vehicle2, null);
        vehicle1.setCleaners(List.of(cleaner1));
        vehicle2.setCleaners(List.of(cleaner2));
        BookingRequestDto requestDto = new BookingRequestDto(
                LocalDateTime.now().plusDays(100).withHour(9).withMinute(0),
                2,
                List.of("1", "2")
        );

        when(cleanerRepository.findByIdIn(any())).thenReturn(List.of(cleaner1, cleaner2));

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            bookingService.createBooking(requestDto);
        });

        assertEquals("All cleaners must belong to the same vehicle.", thrown.getMessage());
    }

    // Test Case 4: Cleaner Not Available
    @Test
    void testCreateBooking_CleanerNotAvailable() {
        Vehicle vehicle = new Vehicle("1", "Vehicle1", new ArrayList<>());
        Cleaner cleaner1 = new Cleaner("1", "Cleaner1", vehicle, null);
        Cleaner cleaner2 = new Cleaner("2", "Cleaner2", vehicle, null);
        vehicle.setCleaners(List.of(cleaner1, cleaner2));
        LocalDateTime tesTime = LocalDateTime.now().plusDays(100).withHour(9).withMinute(0);
        BookingRequestDto requestDto = new BookingRequestDto(
                tesTime,
                2,
                List.of("1", "2")
        );

        when(cleanerRepository.findByIdIn(any())).thenReturn(List.of(cleaner1, cleaner2));
        when(bookingRepository.findByCleanerAndDate(anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(new Booking("1", tesTime, tesTime.plusHours(2), 2, List.of(cleaner1))));

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            bookingService.createBooking(requestDto);
        });

        assertEquals("Cleaner Cleaner1 is not available for the requested time.", thrown.getMessage());
    }

    // Test Case 5: Successful Booking Update
    @Test
    void testUpdateBooking_Successful() {
        // Arrange
        Vehicle vehicle = new Vehicle("1", "Vehicle1", new ArrayList<>());
        Cleaner cleaner = new Cleaner("1", "Cleaner1", vehicle, null);
        vehicle.setCleaners(List.of(cleaner));
        LocalDateTime fixedStartDateTime = LocalDateTime.of(2024, 12, 18, 11, 0);
        Booking existingBooking = new Booking("1", LocalDateTime.of(2024, 9, 8, 9, 0), LocalDateTime.of(2024, 9, 8, 11, 0), 2, List.of(cleaner));
        BookingRequestDto requestDto = new BookingRequestDto(
                fixedStartDateTime,
                2,
                List.of("1")
        );

        when(bookingRepository.findById(anyString())).thenReturn(Optional.of(existingBooking));
        when(cleanerRepository.findByIdIn(any())).thenReturn(List.of(cleaner));
        when(bookingRepository.save(any())).thenAnswer(invocation -> invocation.getArguments()[0]);

        // Act
        Booking updatedBooking = bookingService.updateBooking("1", fixedStartDateTime, 2);

        // Assert
        assertNotNull(updatedBooking);
        assertEquals(fixedStartDateTime, updatedBooking.getStartDateTime());
        verify(bookingRepository).save(any());
    }


    // Test Case 6: Booking Update with Invalid Booking ID
    @Test
    void testUpdateBooking_InvalidBookingId() {
        when(bookingRepository.findById(anyString())).thenReturn(Optional.empty());

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            bookingService.updateBooking("invalid-id", LocalDateTime.now().plusDays(100).withHour(9).withMinute(0), 2);
        });

        assertEquals("Booking not found.", thrown.getMessage());
    }

    // Test Case 7: Booking Update with Cleaner Availability Issue
    @Test
    void testUpdateBooking_CleanerNotAvailable() {
        Vehicle vehicle = new Vehicle("1", "Vehicle1", new ArrayList<>());
        Cleaner cleaner = new Cleaner("1", "Cleaner1", vehicle, null);
        vehicle.setCleaners(List.of(cleaner));

        Booking existingBooking = new Booking("1",
                LocalDateTime.now().plusDays(100).withHour(9).withMinute(0),
                LocalDateTime.now().plusDays(100).withHour(11).withMinute(0),
                2,
                List.of(cleaner)
        );

        LocalDateTime newStartDateTime = LocalDateTime.now().plusDays(100).withHour(10).withMinute(0);
        int newDuration = 2;

        // Bookings that overlap with the new start time
        Booking conflictingBooking = new Booking("2",
                LocalDateTime.now().plusDays(100).withHour(10).withMinute(0),
                LocalDateTime.now().plusDays(100).withHour(12).withMinute(0),
                2,
                List.of(cleaner)
        );

        // Mock behavior
        when(bookingRepository.findById("1")).thenReturn(Optional.of(existingBooking));
        when(cleanerRepository.findByIdIn(any())).thenReturn(List.of(cleaner));
        when(bookingRepository.findByCleanerAndDate(anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(conflictingBooking));

        // Act and Assert
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            bookingService.updateBooking("1", newStartDateTime, newDuration);
        });

        assertEquals("Cleaner Cleaner1 is not available for the updated time.", thrown.getMessage());
    }


    @Test
    void testCreateBooking_StartTimeInPast() {
        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setStartDateTime(LocalDateTime.now().minusDays(1)); // Set to past time
        bookingRequestDto.setDuration(1);
        bookingRequestDto.setCleanerIds(List.of("cleaner1"));

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            bookingService.createBooking(bookingRequestDto);
        });

        assertEquals("Start time cannot be in the past.", thrown.getMessage());
    }

    @Test
    void testCreateBooking_TimeAfterWorkingHours() {
        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setStartDateTime(LocalDateTime.of(LocalDate.now(), LocalTime.of(22, 30))); // After working hours
        bookingRequestDto.setDuration(1);
        bookingRequestDto.setCleanerIds(List.of("cleaner1"));

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            bookingService.createBooking(bookingRequestDto);
        });

        assertEquals("Service hours are completed for today. Try future times.", thrown.getMessage());
    }

    @Test
    void testUpdateBooking_StartTimeInPast() {
        String bookingId = "someBookingId";
        LocalDateTime pastStartDateTime = LocalDateTime.now().minusDays(1);
        int newDuration = 1;

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            bookingService.updateBooking(bookingId, pastStartDateTime, newDuration);
        });

        assertEquals("Start time cannot be in the past.", thrown.getMessage());
    }



}
