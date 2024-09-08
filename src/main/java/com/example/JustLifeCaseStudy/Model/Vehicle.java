package com.example.JustLifeCaseStudy.Model;

import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "vehicle")
public class Vehicle {
    @Id
    private String id;

    private String name;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    private List<Cleaner> cleaners;

    public Vehicle() {
    }

    public Vehicle(String id, String name, List<Cleaner> cleaners) {
        this.id = id;
        this.name = name;
        this.cleaners = cleaners;
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

    public List<Cleaner> getCleaners() {
        return cleaners;
    }

    public void setCleaners(List<Cleaner> cleaners) {
        this.cleaners = cleaners;
    }
}
