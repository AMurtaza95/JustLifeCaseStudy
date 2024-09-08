package com.example.JustLifeCaseStudy.Controller;

import com.example.JustLifeCaseStudy.Service.BookingService;
import com.example.JustLifeCaseStudy.dto.request.BookingRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateBookingSuccess() throws Exception {
        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setStartDateTime(LocalDateTime.now());
        bookingRequestDto.setDuration(2);
        bookingRequestDto.setCleanerIds(List.of("cleaner"));

        mockMvc.perform(post("/v1/booking/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingRequestDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void testCreateBookingBadRequest() throws Exception {
        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        // Set invalid data or leave fields empty

        mockMvc.perform(post("/v1/booking/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateBookingSuccess() throws Exception {
        LocalDateTime newStartDateTime = LocalDateTime.now().plusDays(1);
        String newStartDateTimeStr = newStartDateTime.toString();
        int newDuration = 120;

        mockMvc.perform(put("/v1/booking/update/{bookingId}", "bookingId123")
                        .param("startDateTime", newStartDateTimeStr)
                        .param("duration", String.valueOf(newDuration)))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateBookingBadRequest() throws Exception {
        // Assuming bad request when date is invalid
        mockMvc.perform(put("/v1/booking/update/{bookingId}", "bookingId123")
                        .param("startDateTime", "invalidDateTime")
                        .param("duration", "invalidDuration"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdateBookingInternalServerError() throws Exception {
        LocalDateTime newStartDateTime = LocalDateTime.now().plusDays(1);
        String newStartDateTimeStr = newStartDateTime.toString();
        int newDuration = 120;

        doThrow(new RuntimeException()).when(bookingService).updateBooking("bookingId123", newStartDateTime, newDuration);

        mockMvc.perform(put("/v1/booking/update/{bookingId}", "bookingId123")
                        .param("startDateTime", newStartDateTimeStr)
                        .param("duration", String.valueOf(newDuration)))
                .andExpect(status().isInternalServerError());
    }
}
