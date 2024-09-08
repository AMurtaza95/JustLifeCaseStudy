package com.example.JustLifeCaseStudy.Controller;

import com.example.JustLifeCaseStudy.Model.Booking;
import com.example.JustLifeCaseStudy.Service.BookingService;
import com.example.JustLifeCaseStudy.dto.request.BookingRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("v1/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Operation(summary = "Create a new booking", description = "Creates a new booking for cleaning professionals.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Booking created successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
    @PostMapping("/create")
    public ResponseEntity<Void> createBooking(@Valid @RequestBody BookingRequestDto bookingRequestDto) {
        try {
            bookingService.createBooking(bookingRequestDto);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Update an existing booking", description = "Updates the date and time of an existing booking.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking updated successfully", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error", content = @Content)
    })
    @PutMapping("/update/{bookingId}")
    public ResponseEntity<Void> updateBooking(
            @PathVariable("bookingId") String bookingId,
            @RequestParam("startDateTime") String newStartDateTimeStr,
            @RequestParam("duration") int newDuration
    ) {
        try {
            LocalDateTime newStartDateTime = LocalDateTime.parse(newStartDateTimeStr);
            bookingService.updateBooking(bookingId, newStartDateTime, newDuration);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
