package com.example.JustLifeCaseStudy.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class AvailableCleanersByDateResponse {
    private String cleanerName;
    private List<LocalDateTime> availableTimeSlots;

    public AvailableCleanersByDateResponse(String cleanerName, List<LocalDateTime> availableTimeSlots) {
        this.cleanerName = cleanerName;
        this.availableTimeSlots = availableTimeSlots;
    }

    public String getCleanerName() {
        return cleanerName;
    }

    public void setCleanerName(String cleanerName) {
        this.cleanerName = cleanerName;
    }

    public List<LocalDateTime> getAvailableTimeSlots() {
        return availableTimeSlots;
    }

    public void setAvailableTimeSlots(List<LocalDateTime> availableTimeSlots) {
        this.availableTimeSlots = availableTimeSlots;
    }
}
