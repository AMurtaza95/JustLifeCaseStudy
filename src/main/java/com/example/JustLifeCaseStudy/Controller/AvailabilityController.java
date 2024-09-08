package com.example.JustLifeCaseStudy.Controller;

import com.example.JustLifeCaseStudy.Service.AvailabilityService;
import com.example.JustLifeCaseStudy.dto.response.AvailableCleanersByDateResponse;
import com.example.JustLifeCaseStudy.dto.response.AvailableCleanersByTimeResponse;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("v1/availability")
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    public AvailabilityController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    @GetMapping("/date")
    public ResponseEntity<List<AvailableCleanersByDateResponse>> getAvailableCleanersForDate(
            @RequestParam("date") String dateStr) {
        try {
            LocalDate date = LocalDate.parse(dateStr);
            List<AvailableCleanersByDateResponse> availableCleaners = availabilityService.getAvailableCleanersForDate(date);
            return ResponseEntity.ok(availableCleaners);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/time")
    public ResponseEntity<AvailableCleanersByTimeResponse> getAvailableCleanersForTime(
            @RequestParam("startDateTime") String startDateTimeStr,
            @RequestParam("duration") int duration) {
        try {
            LocalDateTime startDateTime = LocalDateTime.parse(startDateTimeStr);
            List<String> availableCleaners = availabilityService.getAvailableCleanersForTime(startDateTime, duration);
            AvailableCleanersByTimeResponse response = new AvailableCleanersByTimeResponse(availableCleaners);
            return ResponseEntity.ok(response);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
