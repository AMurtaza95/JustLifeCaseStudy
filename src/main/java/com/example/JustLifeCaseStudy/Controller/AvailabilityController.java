package com.example.JustLifeCaseStudy.Controller;

import com.example.JustLifeCaseStudy.Service.AvailabilityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.JustLifeCaseStudy.dto.response.AvailableCleanersByDateResponse;
import com.example.JustLifeCaseStudy.dto.response.AvailableCleanersByTimeResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("v1/availability")
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    public AvailabilityController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    @Operation(summary = "Get available cleaners for a specific date",
            description = "Returns a list of available cleaners and their available times for a given date.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of available cleaners for the date"),
            @ApiResponse(responseCode = "400", description = "Invalid date format"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/date")
    public ResponseEntity<AvailableCleanersByDateResponse> getAvailableCleanersForDate(
            @Parameter(description = "The date for which to get available cleaners",
                    example = "2024-09-08")
            @RequestParam("date") String dateStr) {
        try {
            LocalDate date = LocalDate.parse(dateStr);
            Map<String, List<LocalDateTime>> availableCleaners = availabilityService.getAvailableCleanersForDate(date);
            AvailableCleanersByDateResponse response = new AvailableCleanersByDateResponse(availableCleaners);
            return ResponseEntity.ok(response);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Operation(summary = "Get available cleaners for a specific time and duration",
            description = "Returns a list of available cleaners for a specific start time and duration.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of available cleaners for the time period"),
            @ApiResponse(responseCode = "400", description = "Invalid time format or duration"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/time")
    public ResponseEntity<AvailableCleanersByTimeResponse> getAvailableCleanersForTime(
            @Parameter(description = "The start date and time for the availability check",
                    example = "2024-09-08T10:00:00")
            @RequestParam("startDateTime") String startDateTimeStr,

            @Parameter(description = "The duration of the service in hours",
                    example = "2")
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
