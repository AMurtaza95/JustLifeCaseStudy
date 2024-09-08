package com.example.JustLifeCaseStudy.dto.response;

import java.util.List;

public class AvailableCleanersByTimeResponse {
    private List<String> availableCleaners;

    public AvailableCleanersByTimeResponse(List<String> availableCleaners) {
        this.availableCleaners = availableCleaners;
    }

    public List<String> getAvailableCleaners() {
        return availableCleaners;
    }

    public void setAvailableCleaners(List<String> availableCleaners) {
        this.availableCleaners = availableCleaners;
    }
}
