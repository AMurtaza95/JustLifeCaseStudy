package com.example.JustLifeCaseStudy.Controller;

import com.example.JustLifeCaseStudy.Service.AvailabilityService;
import com.example.JustLifeCaseStudy.dto.response.AvailableCleanersByDateResponse;
import com.example.JustLifeCaseStudy.dto.response.AvailableCleanersByTimeResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@EnableWebMvc
public class AvailabilityControllerTest {

    @Mock
    private AvailabilityService availabilityService;

    @InjectMocks
    private AvailabilityController availabilityController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(availabilityController).build();
    }

    @Test
    public void testGetAvailableCleanersForDate() throws Exception {
        LocalDate date = LocalDate.of(2024, 9, 8);
        Map<String, List<LocalDateTime>> availableCleaners = Map.of(
                "Cleaner1", List.of(LocalDateTime.of(2024, 9, 8, 10, 0)),
                "Cleaner2", List.of(LocalDateTime.of(2024, 9, 8, 11, 0))
        );

        AvailableCleanersByDateResponse responseDto = new AvailableCleanersByDateResponse(availableCleaners);

        when(availabilityService.getAvailableCleanersForDate(date)).thenReturn(availableCleaners);

        mockMvc.perform(get("/v1/availability/date")
                        .param("date", "2024-09-08"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.availableCleaners.Cleaner1", hasSize(1)))
                .andExpect(jsonPath("$.availableCleaners.Cleaner1[0]").value("2024-09-08T10:00:00"))
                .andExpect(jsonPath("$.availableCleaners.Cleaner2", hasSize(1)))
                .andExpect(jsonPath("$.availableCleaners.Cleaner2[0]").value("2024-09-08T11:00:00"));
    }


    @Test
    public void testGetAvailableCleanersForTime() throws Exception {
        LocalDateTime startDateTime = LocalDateTime.of(2024, 9, 8, 10, 0);
        List<String> availableCleaners = List.of("Cleaner1", "Cleaner2");

        AvailableCleanersByTimeResponse responseDto = new AvailableCleanersByTimeResponse(availableCleaners);

        when(availabilityService.getAvailableCleanersForTime(startDateTime, 2)).thenReturn(availableCleaners);

        mockMvc.perform(get("/v1/availability/time")
                        .param("startDateTime", "2024-09-08T10:00:00")
                        .param("duration", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.availableCleaners[0]").value("Cleaner1"))
                .andExpect(jsonPath("$.availableCleaners[1]").value("Cleaner2"));
    }


    @Test
    public void testGetAvailableCleanersForDate_InvalidDateFormat() throws Exception {
        mockMvc.perform(get("/v1/availability/date")
                        .param("date", "invalid-date"))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testGetAvailableCleanersForTime_InvalidTimeFormat() throws Exception {
        mockMvc.perform(get("/v1/availability/time")
                        .param("startDateTime", "invalid-datetime")
                        .param("duration", "2"))
                .andExpect(status().isBadRequest());
    }

}
