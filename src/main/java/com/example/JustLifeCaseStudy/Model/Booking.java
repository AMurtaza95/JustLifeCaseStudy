package com.example.JustLifeCaseStudy.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "booking")
public class Booking extends BaseEntity {
    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "start_date_time", nullable = false)
    private LocalDateTime startDateTime;

    @Column(name = "end_date_time", nullable = false) // DATETIME
    private LocalDateTime endDateTime;

    @Column(name = "duration", nullable = false) // INT
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

    public Booking(String id, LocalDateTime startDateTime, LocalDateTime endDateTime, int duration, List<Cleaner> cleaner) {
        this.id = id;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
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

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
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
