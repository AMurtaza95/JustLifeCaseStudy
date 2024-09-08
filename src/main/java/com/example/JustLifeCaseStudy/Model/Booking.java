package com.example.JustLifeCaseStudy.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "booking")
public class Booking {
    @Id
    private String id;

    private LocalDateTime startDateTime;
    private int duration; // 2 or 4 hours

    @ManyToMany
    @JoinTable(
            name = "booking_cleaner",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "cleaner_id")
    )
    private List<Cleaner> cleaner;

    public Booking() {
    }

    public Booking(String id, LocalDateTime startDateTime, int duration, List<Cleaner> cleaner) {
        this.id = id;
        this.startDateTime = startDateTime;
        this.duration = duration;
        this.cleaner = cleaner;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public List<Cleaner> getCleaner() {
        return cleaner;
    }

    public void setCleaner(List<Cleaner> cleaner) {
        this.cleaner = cleaner;
    }
}
