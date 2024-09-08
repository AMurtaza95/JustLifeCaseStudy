package com.example.JustLifeCaseStudy.Model;

import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "cleaner")
public class Cleaner {
    @Id
    @Column(length = 36, nullable = false)
    private String id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @ManyToMany(mappedBy = "cleaner")
    private List<Booking> bookings;
    public Cleaner() {
    }

    public Cleaner(String id, String name, Vehicle vehicle, List<Booking> bookings) {
        this.id = id;
        this.name = name;
        this.vehicle = vehicle;
        this.bookings = bookings;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}
