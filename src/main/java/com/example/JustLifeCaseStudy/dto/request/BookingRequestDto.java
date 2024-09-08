package com.example.JustLifeCaseStudy.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public class BookingRequestDto {

    @NotNull(message = "Start date and time cannot be null")
    private LocalDateTime startDateTime;
    @NotNull(message = "Duration cannot be null")
    private int duration; // 2 or 4 hours
    @NotNull(message = "Cleaner IDs cannot be null")
    private List<String> cleanerIds; // List of IDs of assigned cleaners

    // Getters and setters
    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public List<String> getCleanerIds() {
        return cleanerIds;
    }

    public void setCleanerIds(List<String> cleanerIds) {
        this.cleanerIds = cleanerIds;
    }
}


