package com.example.JustLifeCaseStudy.Repository;

import com.example.JustLifeCaseStudy.Model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, String> {
}
