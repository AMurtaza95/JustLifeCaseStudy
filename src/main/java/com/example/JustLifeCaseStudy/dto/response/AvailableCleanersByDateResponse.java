package com.example.JustLifeCaseStudy.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class AvailableCleanersByDateResponse {
    private Map<String, List<LocalDateTime>> availableCleaners;

    public AvailableCleanersByDateResponse(Map<String, List<LocalDateTime>> availableCleaners) {
        this.availableCleaners = availableCleaners;
    }

    public Map<String, List<LocalDateTime>> getAvailableCleaners() {
        return availableCleaners;
    }

    public void setAvailableCleaners(Map<String, List<LocalDateTime>> availableCleaners) {
        this.availableCleaners = availableCleaners;
    }
}
