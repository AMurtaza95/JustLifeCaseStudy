package com.example.JustLifeCaseStudy.Repository;

import com.example.JustLifeCaseStudy.Model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {

    @Query("SELECT b FROM Booking b JOIN b.cleaner c WHERE c.id = :cleanerId AND b.startDateTime BETWEEN :startOfDay AND :endOfDay")
    List<Booking> findByCleanerAndDate(@Param("cleanerId") String cleanerId,
                                       @Param("startOfDay") LocalDateTime startOfDay,
                                       @Param("endOfDay") LocalDateTime endOfDay);
}
