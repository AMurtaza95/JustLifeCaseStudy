package com.example.JustLifeCaseStudy.Repository;

import com.example.JustLifeCaseStudy.Model.Cleaner;
import com.example.JustLifeCaseStudy.Model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {
}
