package com.example.JustLifeCaseStudy.Service;

import com.example.JustLifeCaseStudy.Model.Booking;
import com.example.JustLifeCaseStudy.Model.Cleaner;
import com.example.JustLifeCaseStudy.Model.Vehicle;
import com.example.JustLifeCaseStudy.Repository.BookingRepository;
import com.example.JustLifeCaseStudy.Repository.CleanerRepository;
import com.example.JustLifeCaseStudy.dto.request.BookingRequestDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookingService {

    @Autowired
    private CleanerRepository cleanerRepository;

    @Autowired
    private BookingRepository bookingRepository;

    // Booking creation logic
    @Transactional
    public Booking createBooking(BookingRequestDto bookingRequestDto) {

        LocalDateTime now = LocalDateTime.now();
        if (bookingRequestDto.getStartDateTime().isBefore(now)) {
            throw new IllegalArgumentException("Start time cannot be in the past.");
        }

        if (bookingRequestDto.getStartDateTime().toLocalTime().isAfter(LocalTime.of(22, 0))) {
            throw new IllegalArgumentException("Service hours are completed for today. Try future times.");
        }

        // Ensure cleaner count (1-3)
        if (bookingRequestDto.getCleanerIds().isEmpty() || bookingRequestDto.getCleanerIds().size() > 3) {
            throw new IllegalArgumentException("You can only book 1, 2, or 3 cleaners.");
        }

        // Fetch the list of cleaners by their IDs
        List<Cleaner> cleaners = cleanerRepository.findByIdIn(bookingRequestDto.getCleanerIds());

        // Check that all cleaners are from the same vehicle
        Vehicle assignedVehicle = cleaners.get(0).getVehicle();
        for (Cleaner cleaner : cleaners) {
            if (!cleaner.getVehicle().getId().equals(assignedVehicle.getId())) {
                throw new IllegalArgumentException("All cleaners must belong to the same vehicle.");
            }
        }

        // Check availability for each cleaner
        for (Cleaner cleaner : cleaners) {
            if (!checkAvailability(cleaner, bookingRequestDto.getStartDateTime(), bookingRequestDto.getDuration(), "")) {
                throw new IllegalArgumentException("Cleaner " + cleaner.getName() + " is not available for the requested time.");
            }
        }

        LocalDateTime bookingEndTime = bookingRequestDto.getStartDateTime().plusHours(bookingRequestDto.getDuration());

        // Create a new booking and assign the cleaners
        Booking booking = new Booking(UUID.randomUUID().toString(), bookingRequestDto.getStartDateTime(), bookingEndTime, bookingRequestDto.getDuration(), cleaners);
        bookingRepository.save(booking);

        // After the booking is created, the cleaner professionals' available times are implicitly updated due to existing availability check logic

        return booking;
    }

    @Transactional
    public Booking updateBooking(String bookingId, LocalDateTime newStartDateTime, int newDuration) {

        LocalDateTime now = LocalDateTime.now();
        if (newStartDateTime.isBefore(now)) {
            throw new IllegalArgumentException("Start time cannot be in the past.");
        }

        if (newStartDateTime.toLocalTime().isAfter(LocalTime.of(22, 0))) {
            throw new IllegalArgumentException("Service hours are completed for today. Try future times.");
        }

        // Fetch the booking by ID
        Optional<Booking> bookingOptional = bookingRepository.findById(bookingId);
        if (bookingOptional.isEmpty()) {
            throw new IllegalArgumentException("Booking not found.");
        }

        Booking booking = bookingOptional.get();

        // Fetch the current assigned cleaners
        List<Cleaner> assignedCleaners = booking.getCleaner();

        // Check availability for each cleaner with new time
        for (Cleaner cleaner : assignedCleaners) {
            if (!checkAvailability(cleaner, newStartDateTime, newDuration, bookingId)) {
                throw new IllegalArgumentException("Cleaner " + cleaner.getName() + " is not available for the updated time.");
            }
        }

        // Update the booking start time and duration
        booking.setStartDateTime(newStartDateTime);
        booking.setDuration(newDuration);

        // Save the updated booking
        bookingRepository.save(booking);

        // After the booking is updated, the cleaner professionals' available times are updated automatically

        return booking;
    }

    private boolean checkAvailability(Cleaner cleaner, LocalDateTime startDateTime, int duration, String bookingIdToExclude) {
        // Define working hours
        LocalDateTime startOfWorkDay = LocalDateTime.of(startDateTime.toLocalDate(), LocalTime.of(8, 0));
        LocalDateTime endOfWorkDay = LocalDateTime.of(startDateTime.toLocalDate(), LocalTime.of(22, 0));

        // Calculate the proposed end time
        LocalDateTime proposedEndDateTime = startDateTime.plusHours(duration);

        // Ensure the proposed booking is not on Friday
        if (startDateTime.getDayOfWeek() == DayOfWeek.FRIDAY) {
            return false; // No work on Fridays
        }

        // Ensure the proposed booking is within working hours
        if (startDateTime.isBefore(startOfWorkDay) || proposedEndDateTime.isAfter(endOfWorkDay)) {
            return false;
        }

        // Fetch existing bookings for the cleaner on the given date
        List<Booking> bookings = bookingRepository.findByCleanerAndDate(cleaner.getId(), startOfWorkDay, endOfWorkDay);

        // Exclude the booking that is being updated
        bookings = bookings.stream()
                .filter(booking -> !booking.getId().equals(bookingIdToExclude))
                .toList();

        // Check if the proposed time overlaps with any existing bookings or violates the 30-minute break rule
        for (Booking booking : bookings) {
            LocalDateTime bookingStart = booking.getStartDateTime();
            LocalDateTime bookingEnd = bookingStart.plusHours(booking.getDuration());
            LocalDateTime proposedStartWithBreak = bookingEnd.plusMinutes(30); // 30-minute break after booking

            // Check for overlap
            if (startDateTime.isBefore(bookingEnd) && proposedEndDateTime.isAfter(bookingStart)) {
                return false; // There's an overlap
            }

            // Check if the proposed start time violates the 30-minute break
            if (startDateTime.isBefore(proposedStartWithBreak) && proposedStartWithBreak.isBefore(proposedEndDateTime)) {
                return false; // The break rule is violated
            }
        }

        return true;
    }
}
