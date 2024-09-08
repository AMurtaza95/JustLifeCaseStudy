package com.example.JustLifeCaseStudy.Service;


import com.example.JustLifeCaseStudy.Model.Booking;
import com.example.JustLifeCaseStudy.Model.Cleaner;
import com.example.JustLifeCaseStudy.Repository.BookingRepository;
import com.example.JustLifeCaseStudy.Repository.CleanerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AvailabilityService {

    @Autowired
    private CleanerRepository cleanerRepository;

    @Autowired
    private BookingRepository bookingRepository;

    public Map<String, List<LocalDateTime>> getAvailableCleanersForDate(LocalDate date) {
        List<Cleaner> allCleaners = cleanerRepository.findAll();
        Map<String, List<LocalDateTime>> availableCleaners = new HashMap<>();

        for (Cleaner cleaner : allCleaners) {
            List<LocalDateTime> availableTimes = findAvailableTimes(cleaner, date);
            if (!availableTimes.isEmpty()) {
                availableCleaners.put(cleaner.getName(), availableTimes);
            }
        }
        return availableCleaners;
    }

    public List<String> getAvailableCleanersForTime(LocalDateTime startDateTime, int duration) {
        List<Cleaner> allCleaners = cleanerRepository.findAll();
        List<String> availableCleaners = new ArrayList<>();

        for (Cleaner cleaner : allCleaners) {
            boolean isAvailable = checkAvailability(cleaner, startDateTime, duration);
            if (isAvailable) {
                availableCleaners.add(cleaner.getName());
            }
        }
        return availableCleaners;
    }

    private List<LocalDateTime> findAvailableTimes(Cleaner cleaner, LocalDate date) {
        // Define working hours and break time
        LocalDateTime startOfWorkDay = LocalDateTime.of(date, LocalTime.of(8, 0));
        LocalDateTime endOfWorkDay = LocalDateTime.of(date, LocalTime.of(22, 0));
        Duration breakDuration = Duration.ofMinutes(30);

        // Fetch existing bookings for the cleaner on the given date
        List<Booking> bookings = bookingRepository.findByCleanerAndDate(cleaner.getId(), startOfWorkDay, endOfWorkDay);

        // Collect occupied time slots
        List<LocalDateTime[]> occupiedSlots = new ArrayList<>();
        for (Booking booking : bookings) {
            LocalDateTime bookingStart = booking.getStartDateTime();
            LocalDateTime bookingEnd = bookingStart.plusHours(booking.getDuration());
            occupiedSlots.add(new LocalDateTime[]{bookingStart, bookingEnd});
        }

        // Add potential available times
        List<LocalDateTime> availableTimes = new ArrayList<>();
        LocalDateTime currentStartTime = startOfWorkDay;

        while (currentStartTime.plusHours(2).isBefore(endOfWorkDay) || currentStartTime.plusHours(4).isBefore(endOfWorkDay)) {
            boolean isAvailable = true;
            LocalDateTime currentEndTime = currentStartTime.plusHours(2);

            if (currentEndTime.isAfter(endOfWorkDay)) {
                break;
            }

            for (LocalDateTime[] slot : occupiedSlots) {
                if ((currentStartTime.isBefore(slot[1]) && currentEndTime.isAfter(slot[0])) ||
                        (currentStartTime.isEqual(slot[0]) || currentEndTime.isEqual(slot[1]))) {
                    isAvailable = false;
                    break;
                }
            }

            if (isAvailable) {
                availableTimes.add(currentStartTime);
            }

            // Move to the next slot, considering the break time
            currentStartTime = currentEndTime.plus(breakDuration);
        }

        return availableTimes;
    }

    private boolean checkAvailability(Cleaner cleaner, LocalDateTime startDateTime, int duration) {
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

        // Check if the proposed time overlaps with any existing bookings or violates the 30-minute break rule
        return bookings.stream().noneMatch(booking -> {
            LocalDateTime bookingEnd = booking.getStartDateTime().plusHours(booking.getDuration());
            LocalDateTime proposedStartWithBreak = bookingEnd.plusMinutes(30); // 30-minute break after booking

            return startDateTime.isBefore(bookingEnd) && proposedEndDateTime.isAfter(booking.getStartDateTime()) ||
                    startDateTime.isBefore(proposedStartWithBreak);
        });
    }
}


