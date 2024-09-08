package com.example.JustLifeCaseStudy;

import com.example.JustLifeCaseStudy.Model.Cleaner;
import com.example.JustLifeCaseStudy.Model.Vehicle;
import com.example.JustLifeCaseStudy.Repository.CleanerRepository;
import com.example.JustLifeCaseStudy.Repository.VehicleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class JustLifeCaseStudyApplication {

	public static void main(String[] args) {
		SpringApplication.run(JustLifeCaseStudyApplication.class, args);
	}

    @Bean
    public CommandLineRunner dataInitializer(
            VehicleRepository vehicleRepository,
            CleanerRepository cleanerRepository) {
        return args -> {
            // Check if vehicles and cleaners already exist
            if (vehicleRepository.count() == 0) {
                // Create Vehicle entities
                Vehicle vehicle1 = new Vehicle("1", "Vehicle 1", new ArrayList<>());
                Vehicle vehicle2 = new Vehicle("2", "Vehicle 2", new ArrayList<>());
                Vehicle vehicle3 = new Vehicle("3", "Vehicle 3", new ArrayList<>());
                Vehicle vehicle4 = new Vehicle("4", "Vehicle 4", new ArrayList<>());
                Vehicle vehicle5 = new Vehicle("5", "Vehicle 5", new ArrayList<>());

                // Save Vehicles
                vehicleRepository.saveAll(List.of(vehicle1, vehicle2, vehicle3, vehicle4, vehicle5));

                // Create Cleaner entities
                Cleaner cleaner1 = new Cleaner("1", "Cleaner 1", vehicle1, new ArrayList<>());
                Cleaner cleaner2 = new Cleaner("2", "Cleaner 2", vehicle1, new ArrayList<>());
                Cleaner cleaner3 = new Cleaner("3", "Cleaner 3", vehicle1, new ArrayList<>());
                Cleaner cleaner4 = new Cleaner("4", "Cleaner 4", vehicle1, new ArrayList<>());
                Cleaner cleaner5 = new Cleaner("5", "Cleaner 5", vehicle1, new ArrayList<>());
                Cleaner cleaner6 = new Cleaner("6", "Cleaner 6", vehicle2, new ArrayList<>());
                Cleaner cleaner7 = new Cleaner("7", "Cleaner 7", vehicle2, new ArrayList<>());
                Cleaner cleaner8 = new Cleaner("8", "Cleaner 8", vehicle2, new ArrayList<>());
                Cleaner cleaner9 = new Cleaner("9", "Cleaner 9", vehicle2, new ArrayList<>());
                Cleaner cleaner10 = new Cleaner("10", "Cleaner 10", vehicle2, new ArrayList<>());
                Cleaner cleaner11 = new Cleaner("11", "Cleaner 11", vehicle3, new ArrayList<>());
                Cleaner cleaner12 = new Cleaner("12", "Cleaner 12", vehicle3, new ArrayList<>());
                Cleaner cleaner13 = new Cleaner("13", "Cleaner 13", vehicle3, new ArrayList<>());
                Cleaner cleaner14 = new Cleaner("14", "Cleaner 14", vehicle3, new ArrayList<>());
                Cleaner cleaner15 = new Cleaner("15", "Cleaner 15", vehicle3, new ArrayList<>());
                Cleaner cleaner16 = new Cleaner("16", "Cleaner 16", vehicle4, new ArrayList<>());
                Cleaner cleaner17 = new Cleaner("17", "Cleaner 17", vehicle4, new ArrayList<>());
                Cleaner cleaner18 = new Cleaner("18", "Cleaner 18", vehicle4, new ArrayList<>());
                Cleaner cleaner19 = new Cleaner("19", "Cleaner 19", vehicle4, new ArrayList<>());
                Cleaner cleaner20 = new Cleaner("20", "Cleaner 20", vehicle4, new ArrayList<>());
                Cleaner cleaner21 = new Cleaner("21", "Cleaner 21", vehicle5, new ArrayList<>());
                Cleaner cleaner22 = new Cleaner("22", "Cleaner 22", vehicle5, new ArrayList<>());
                Cleaner cleaner23 = new Cleaner("23", "Cleaner 23", vehicle5, new ArrayList<>());
                Cleaner cleaner24 = new Cleaner("24", "Cleaner 24", vehicle5, new ArrayList<>());
                Cleaner cleaner25 = new Cleaner("25", "Cleaner 25", vehicle5, new ArrayList<>());

                // Add Cleaners to their respective Vehicles
                vehicle1.setCleaners(List.of(cleaner1, cleaner2, cleaner3, cleaner4, cleaner5));
                vehicle2.setCleaners(List.of(cleaner6, cleaner7, cleaner8, cleaner9, cleaner10));
                vehicle3.setCleaners(List.of(cleaner11, cleaner12, cleaner13, cleaner14, cleaner15));
                vehicle4.setCleaners(List.of(cleaner16, cleaner17, cleaner18, cleaner19, cleaner20));
                vehicle5.setCleaners(List.of(cleaner21, cleaner22, cleaner23, cleaner24, cleaner25));

                // Save Cleaners
                cleanerRepository.saveAll(List.of(
                        cleaner1, cleaner2, cleaner3, cleaner4, cleaner5,
                        cleaner6, cleaner7, cleaner8, cleaner9, cleaner10,
                        cleaner11, cleaner12, cleaner13, cleaner14, cleaner15,
                        cleaner16, cleaner17, cleaner18, cleaner19, cleaner20,
                        cleaner21, cleaner22, cleaner23, cleaner24, cleaner25
                ));
            }
        };
    }

}
