package com.example.JustLifeCaseStudy.Controller;

import com.example.JustLifeCaseStudy.Service.BookingService;
import com.example.JustLifeCaseStudy.dto.request.BookingRequestDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("v1/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/create")
    public ResponseEntity<Void> createBooking(@Valid @RequestBody BookingRequestDto bookingRequestDto) {
        bookingService.createBooking(bookingRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/update/{bookingId}")
    public ResponseEntity<Void> updateBooking(
            @PathVariable("bookingId") String bookingId,
            @RequestParam("startDateTime") String newStartDateTimeStr,
            @RequestParam("duration") int newDuration
    ) {
        LocalDateTime newStartDateTime = LocalDateTime.parse(newStartDateTimeStr);
        bookingService.updateBooking(bookingId, newStartDateTime, newDuration);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
